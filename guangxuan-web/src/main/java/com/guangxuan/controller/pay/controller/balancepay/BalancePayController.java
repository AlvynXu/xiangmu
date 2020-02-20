package com.guangxuan.controller.pay.controller.balancepay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.beust.jcommander.internal.Lists;
import com.guangxuan.config.ApiVersion;
import com.guangxuan.config.ApiVersionConstant;
import com.guangxuan.config.RechargeProperties;
import com.guangxuan.config.SystemProperties;
import com.guangxuan.constant.*;
import com.guangxuan.controller.pay.constant.ProductConstant;
import com.guangxuan.controller.pay.utils.StringUtils;
import com.guangxuan.distribution.Distribution;
import com.guangxuan.dto.ProvinceStreetCountDTO;
import com.guangxuan.dto.Result;
import com.guangxuan.dto.SeekRentDTO;
import com.guangxuan.dto.SeekRentProvinceDTO;
import com.guangxuan.enumration.BusinessFailEnum;
import com.guangxuan.exception.BusinessException;
import com.guangxuan.locker.annotation.Lock;
import com.guangxuan.model.*;
import com.guangxuan.service.*;
import com.guangxuan.shiro.ThreadLocalCurrentUser;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.enums.TradeType;
import com.ijpay.core.kit.IpKit;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.WxPayApiConfig;
import com.ijpay.wxpay.WxPayApiConfigKit;
import com.ijpay.wxpay.model.UnifiedOrderModel;
import com.jfinal.kit.StrKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.guangxuan.constant.GoodStatusConstant.NOT_SOLD;
import static com.guangxuan.constant.GoodStatusConstant.SOLD;

/**
 * 余额支付
 *
 * @author zhuolin
 * @Date 2019/12/16
 */
@RestController
@RequestMapping("/balancePay")
@Api(value = "余额controller", tags = {"余额支付接口"})
public class BalancePayController {

    private Logger logger = LoggerFactory.getLogger("payLogger");


    LinkedBlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<>(100);

    ThreadFactory threadFactory = new ThreadFactory() {
        //  int i = 0;  用并发安全的包装类
        AtomicInteger atomicInteger = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            //创建线程 吧任务传进来
            Thread thread = new Thread(r);
            // 给线程起个名字
            thread.setName("balancePay" + atomicInteger.getAndIncrement());
            return thread;
        }
    };

    private ThreadPoolExecutor pool = new ThreadPoolExecutor(10, 10, 1, TimeUnit.SECONDS, blockingQueue, threadFactory);

    @Resource
    private RechargeProperties rechargeProperties;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private MallConfigService mallConfigService;

    @Resource
    private PayLogService payLogService;

    @Resource
    private StreetService streetService;

    @Resource
    private BoothService boothService;

    @Resource
    private StreetPartnerOrderService streetPartnerOrderService;

    @Resource
    private BoothUsersService boothUsersService;

    @Resource
    private Distribution distribution;

    @Resource
    private UsersService usersService;

    @Resource
    private SystemProperties systemProperties;

    @Resource
    private SystemConfigService systemConfigService;

    @Resource
    private MarketOrderService marketOrderService;

    @Resource
    private MarketService marketService;

    @Resource
    private AreaService areaService;

    @Resource
    private MajorMarketOrderService majorMarketOrderService;

    /**
     * app支付
     */
    @GetMapping(value = "/buyVip")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    @ApiOperation("购买代理")
    @ApiVersion(group = ApiVersionConstant.FAP_APP101)
    public Result<?> buyVip(HttpServletRequest request) {
        logger.info("===============================================================余额购买vip");
        Users user = ThreadLocalCurrentUser.getUsers();
        Long userId = user.getId();
        BigDecimal amount = mallConfigService.getById(1L).getThreshold1().setScale(2, BigDecimal.ROUND_HALF_UP);
        if (user.getBalance().compareTo(amount) == -1) {
            throw new BusinessException(BusinessFailEnum.BALANCE_CANT_PAY);
        }
        logger.info("当前用户ID{}", userId);
        if (redisTemplate.hasKey(RedisConstant.BUY_VIP + userId)) {
            throw new BusinessException(BusinessFailEnum.ON_SALE);
        }
        redisTemplate.opsForValue().set(RedisConstant.BUY_VIP + userId, userId, 11, TimeUnit.MINUTES);
        String orderNo = WxPayKit.generateStr();
        logger.info("订单号{}付款金额{}", orderNo, amount);
        PayLog payLog = PayLog.builder()
                .realAmount(amount)
                .amount(new BigDecimal("0").subtract(amount)).userId(ThreadLocalCurrentUser.getUsers().getId())
                .extend1(orderNo).createTime(new Date()).status(PayConstant.SUCCESS_PAY)
                .goodsId(ThreadLocalCurrentUser.getUsers().getId()).payType(PayTypeConstant.BALANCE_PAY)
                .source(PayResourceConstant.BUY_VIP.getType()).product(ProductConstant.VIP).build();
        payLogService.save(payLog);
        user.setBalance(user.getBalance().subtract(amount));
        user.setIsVip(true);
        user.setLevel(1);
        return Result.success(null, null);
    }

    /**
     * app支付
     *
     * @param boothId 展位id
     */
    @GetMapping(value = "/buyBooth")
    @ResponseBody
    @Lock(value = "GUANGXUAN:BUY_BOOTH")
    @ApiOperation("购买展位")
    @Transactional(rollbackFor = Exception.class)
    @ApiVersion(group = ApiVersionConstant.FAP_APP101)
    public Result<?> buyBooth(Long boothId, HttpServletRequest request) {
        logger.info("===============================================================余额购买展位");
        Users user = ThreadLocalCurrentUser.getUsers();
        Long userId = user.getId();
        BigDecimal amount = mallConfigService.getById(1L).getThreshold2().setScale(2, BigDecimal.ROUND_HALF_UP);
        if (user.getBalance().compareTo(amount) == -1) {
            throw new BusinessException(BusinessFailEnum.BALANCE_CANT_PAY);
        }
        Booth booth = boothService.getById(boothId);
        if (redisTemplate.hasKey(RedisConstant.BUY_BOOTH + boothId)) {
            throw new BusinessException(BusinessFailEnum.ON_SALE);
        }
        if (booth == null) {
            throw new BusinessException(BusinessFailEnum.NOT_GET_DATA);
        }
        if (booth.getStatus() != NOT_SOLD) {
            throw new BusinessException(BusinessFailEnum.ON_SALE);
        }
        if (booth.getSaved()) {
            throw new BusinessException(BusinessFailEnum.BOOTH_IS_SAVED);
        }
        redisTemplate.opsForValue().set(RedisConstant.BUY_BOOTH + boothId, boothId, 11, TimeUnit.MINUTES);
        String orderNo = WxPayKit.generateStr();
        logger.info("发起人{}订单号{}付款金额{},展位ID{}", userId, orderNo, amount, boothId);
        PayLog payLog = PayLog.builder()
                .realAmount(amount)
                .amount(new BigDecimal("0").subtract(amount)).userId(ThreadLocalCurrentUser.getUsers().getId())
                .extend1(orderNo).createTime(new Date()).status(PayConstant.SUCCESS_PAY)
                .goodsId(boothId).payType(PayTypeConstant.WECHAT_PAY)
                .source(PayResourceConstant.BUY_BOOTH.getType()).product(ProductConstant.STREET).build();
        payLogService.save(payLog);
        BoothUsers boothUsers = new BoothUsers();
        boothUsers.setBoothId(booth.getId());
        boothUsers.setCreateTime(new Date());
        boothUsers.setOrderNo(orderNo);
        boothUsers.setOrderStatus(OrderStatus.PAID);
        boothUsers.setStatus(PayConstant.SUCCESS_PAY);
        boothUsers.setUserId(userId);
        boothUsersService.save(boothUsers);
        user.setLevel(2);
        user.setBalance(user.getBalance().subtract(amount));
        usersService.updateById(user);
        booth.setStatus(SOLD);
        booth.setUseStatus(UseStatus.NOT_USE);
        boothService.updateById(booth);
        pool.execute(() -> {
            try {
                Street street = streetService.getById(booth.getStreetId());
                redisTemplate.opsForZSet().add(RedisConstant.BUY_BOOTH_END, boothId, boothUsers.getExpireTime().getTime());
                distribution.distribution(userId, new BigDecimal(0).subtract(amount), -1, street.getCode(), null);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        logger.info("================================================================================");
        return Result.success(null, null);
    }


    /**
     * app支付
     *
     * @param streetId 购买
     */
    @GetMapping(value = "/bugStreet")
    @ResponseBody
    @ApiOperation("购买地主")
    @Lock(value = "GUANGXUAN:BUY_STREET")
    @Transactional(rollbackFor = Exception.class)
    @ApiVersion(group = ApiVersionConstant.FAP_APP101)
    public Result<?> bugStreet(Long streetId, HttpServletRequest request) {
        logger.info("===============================================================余额购买街道地主");
        Users user = ThreadLocalCurrentUser.getUsers();
        Long userId = user.getId();
        final BigDecimal amount = mallConfigService.getById(1L).getThreshold3().setScale(2, BigDecimal.ROUND_HALF_UP);
        if (user.getBalance().compareTo(amount) == -1) {
            throw new BusinessException(BusinessFailEnum.BALANCE_CANT_PAY);
        }
        Street street = streetService.getById(streetId);
        if (redisTemplate.hasKey(RedisConstant.BUY_STREET + streetId)) {
            throw new BusinessException(BusinessFailEnum.ON_SALE);
        }
        if (street == null) {
            throw new BusinessException(BusinessFailEnum.NOT_GET_DATA);
        }
        if (street.getStatus() != NOT_SOLD) {
            throw new BusinessException(BusinessFailEnum.ON_SALE);
        }
        logger.info("{}发起购买街道地主，街道地主编号{}", userId, streetId);
        redisTemplate.opsForValue().set(RedisConstant.BUY_STREET + streetId, streetId, 11, TimeUnit.MINUTES);
        String orderNo = StringUtils.getOutTradeNo();
        logger.info("发起人{}订单号{}付款金额{},街道ID{}", userId, orderNo, amount, streetId);
        PayLog payLog = PayLog.builder()
                .realAmount(amount)
                .amount(new BigDecimal("0").subtract(amount)).userId(ThreadLocalCurrentUser.getUsers().getId())
                .extend1(orderNo).createTime(new Date()).status(PayConstant.SUCCESS_PAY)
                .goodsId(streetId)
                .payType(PayTypeConstant.WECHAT_PAY)
                .source(PayResourceConstant.BUY_STREET.getType())
                .product(ProductConstant.STREET).build();
        payLogService.save(payLog);
        StreetPartnerOrder streetPartnerOrder = new StreetPartnerOrder();
        streetPartnerOrder.setAmount(amount);
        streetPartnerOrder.setCreateTime(new Date());
        streetPartnerOrder.setOrderNo(orderNo);
        streetPartnerOrder.setPayType(PayTypeConstant.WECHAT_PAY);
        streetPartnerOrder.setOrderStatus(OrderStatus.PAID);
        streetPartnerOrder.setStatus(PayConstant.SUCCESS_PAY);
        streetPartnerOrder.setStreetId(streetId);
        streetPartnerOrder.setUserId(userId);
        streetPartnerOrderService.save(streetPartnerOrder);
        user.setLevel(3);
        user.setBalance(user.getBalance().subtract(amount));
        usersService.updateById(user);
        street.setStatus(SOLD);
        streetService.updateById(street);
        pool.execute(() -> {
            try {
                distribution.distribution(userId, new BigDecimal(0).subtract(amount), 1, street.getAreaCode(), null);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return Result.success(null, null);
    }

    @ApiOperation("获取提现设置最大值最小值及费率")
    @GetMapping("/getRecharge")
    @ApiVersion(group = ApiVersionConstant.FAP_APP101)
    public Result<?> getRecharge() {
        SystemConfig systemConfig = systemConfigService.getById(1L);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("min", systemConfig.getRechargeMin());
        jsonObject.put("max", systemConfig.getRechargeMax());
        jsonObject.put("rate", systemConfig.getRechargeRate());
        return Result.success(jsonObject, null);
    }

    @PostMapping("/seekRent")
    @ApiOperation("求租")
    @ApiVersion(group = ApiVersionConstant.FAP_APP101)
    @Transactional(rollbackFor = Exception.class)
    public Result<?> seekRent(@RequestBody @Valid SeekRentDTO seekRentDTO, HttpServletRequest request) {
        if (seekRentDTO.getPrice().compareTo(new BigDecimal(0)) == 0) {
            throw new BusinessException(BusinessFailEnum.MONEY_LESS);
        }
        logger.info("===============================================================余额求租");
        Users user = ThreadLocalCurrentUser.getUsers();
        Long userId = user.getId();
        if (!user.getMajor()) {
            Integer count = marketOrderService.count(new LambdaQueryWrapper<MarketOrder>()
                    .eq(MarketOrder::getUserId, userId).eq(MarketOrder::getPayStatus, PayConstant.SUCCESS_PAY));
            if (systemProperties.getMaxMarketCount() != -1 && count >= systemProperties.getMaxMarketCount()) {
                throw new BusinessException(BusinessFailEnum.SEEK_RENT_MAX_COUNT);
            }
        }
        logger.info("{}发起求租,{}", userId, JSON.toJSONString(seekRentDTO));
        MarketOrder marketOrder = new MarketOrder();
        marketOrder.setCityCode(seekRentDTO.getCityCode());
        marketOrder.setCreateTime(new Date());
        marketOrder.setExpireDate(Date.from(LocalDate.now().plusDays(systemProperties.getMarketDays())
                .atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        marketOrder.setDays(seekRentDTO.getDays());
        marketOrder.setPrice(seekRentDTO.getPrice());
        marketOrder.setPayStatus(PayConstant.SUCCESS_PAY);
        marketOrder.setTotalCount(seekRentDTO.getAreaCodes().size());
        marketOrder.setSoldCount(0);
        marketOrder.setType(MarketType.RENT_SEEK);
        marketOrder.setUserId(userId);
        marketOrder.setStreetCodes(String.join(",", seekRentDTO.getAreaCodes()));
        marketOrderService.save(marketOrder);
        // 扣除保证金
        SystemConfig systemConfig = systemConfigService.getById(1L);
        BigDecimal boothMoney = seekRentDTO.getPrice()
                .multiply(new BigDecimal(seekRentDTO.getAreaCodes().size()))
                .multiply(new BigDecimal(marketOrder.getDays()));
        BigDecimal fee = boothMoney.multiply(systemConfig.getSeekRentRate());
        BigDecimal money = boothMoney.add(fee);
        BigDecimal allMoney = new BigDecimal(String.format("%.2f", money));
        if (user.getBalance().compareTo(allMoney) == -1) {
            throw new BusinessException(BusinessFailEnum.BALANCE_CANT_PAY);
        }
        String orderNo = StringUtils.getOutTradeNo();
        PayLog payLog = PayLog.builder()
                .realAmount(boothMoney)
                .amount(new BigDecimal("0").subtract(allMoney)).userId(ThreadLocalCurrentUser.getUsers().getId())
                .extend1(orderNo).createTime(new Date()).status(PayConstant.SUCCESS_PAY)
                .goodsId(marketOrder.getId())
                .payType(PayTypeConstant.BALANCE_PAY)
                .source(PayResourceConstant.RENT.getType())
                .product(ProductConstant.SEEK_RENT).build();
        String ip = IpKit.getRealIp(request);
        if (StrKit.isBlank(ip)) {
            ip = "127.0.0.1";
        }
        payLogService.save(payLog);
        user.setBalance(user.getBalance().subtract(allMoney));
//        // 发布消息
        List<Market> markets = new ArrayList<>();
        List<String> streetCodes = Lists.newArrayList(marketOrder.getStreetCodes().split(","));
        int streetCount = streetService.count(new LambdaQueryWrapper<Street>().in(Street::getCode, streetCodes));
        if (streetCount != streetCodes.size()) {
            throw new BusinessException(BusinessFailEnum.STREET_CODE_VALID_FAIL);
        }
        for (String areaCode : streetCodes) {
            Market market = new Market();
            market.setStatus(MarketStatusConstant.WAIT_RENT);
            market.setAreaCode(areaCode);
            market.setNumber(1);
            market.setPrice(marketOrder.getPrice());
            market.setDays(marketOrder.getDays());
            market.setMarkertEndTime(Date.from(LocalDateTime.now().plusDays(systemProperties.getMarketDays()).atZone(ZoneId.systemDefault()).toInstant()));
            market.setRate(systemConfig.getSeekRentRate());
            market.setType(MarketType.RENT_SEEK);
            market.setUserId(user.getId());
            market.setOrderId(marketOrder.getId());
            markets.add(market);
        }
        usersService.updateById(user);
        marketService.saveBatch(markets);
        logger.info("================================================================================");
        return Result.success(null, null);
    }


    @PostMapping("/seekRentProvince")
    @ApiOperation("求租省份")
    @ApiVersion(group = ApiVersionConstant.FAP_APP103)
    @Transactional(rollbackFor = Exception.class)
    public Result<?> seekRentProvince(@RequestBody @Valid SeekRentProvinceDTO seekRentDTO,
                                      HttpServletRequest request) {
        logger.info("===============================================================余额省份求租");
        Users user = ThreadLocalCurrentUser.getUsers();
        Long userId = user.getId();
        List<ProvinceStreetCountDTO> provinceStreetCountDTOS = areaService.getProviceInfos();
        long total = provinceStreetCountDTOS.stream().filter(a -> seekRentDTO.getAreaCodes().contains(a.getProvinceCode()))
                .mapToLong(ProvinceStreetCountDTO::getStreetCount).sum();
        // 验证保证金
        SystemConfig systemConfig = systemConfigService.getById(1L);
        BigDecimal boothMoney = seekRentDTO.getPrice()
                .multiply(new BigDecimal(total))
                .multiply(new BigDecimal(seekRentDTO.getDays()));
        BigDecimal fee = boothMoney.multiply(systemConfig.getSeekRentRate());
        BigDecimal money = boothMoney.add(fee);
        BigDecimal allMoney = new BigDecimal(String.format("%.2f", money));
        if (user.getBalance().compareTo(allMoney) == -1) {
            throw new BusinessException(BusinessFailEnum.BALANCE_CANT_PAY);
        }
        user.setBalance(user.getBalance().subtract(allMoney));
        user.setMajor(true);
        usersService.updateById(user);
        MajorMarketOrder majorMarketOrder = new MajorMarketOrder();
        majorMarketOrder.setAreaCodes(String.join(",", seekRentDTO.getAreaCodes()));
        majorMarketOrder.setCreateTime(new Date());
        majorMarketOrder.setDays(seekRentDTO.getDays());
        majorMarketOrder.setEndTime(Date.from(LocalDate.now().plusDays(systemProperties.getMarketDays())
                .atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        majorMarketOrder.setPrice(seekRentDTO.getPrice());
        majorMarketOrder.setRate(systemConfig.getSeekRentRate());
        majorMarketOrder.setUserId(userId);
        majorMarketOrderService.save(majorMarketOrder);
        PayLog payLog = PayLog.builder()
                .realAmount(boothMoney)
                .amount(new BigDecimal("0").subtract(allMoney)).userId(ThreadLocalCurrentUser.getUsers().getId())
                .extend1("RENTPROVINCE" + UUID.randomUUID().toString()).createTime(new Date()).status(PayConstant.SUCCESS_PAY)
                .goodsId(majorMarketOrder.getId())
                .payType(PayTypeConstant.BALANCE_PAY)
                .source(PayResourceConstant.RENT.getType())
                .product(ProductConstant.SEEK_PROVINCE_RENT).build();
        payLogService.save(payLog);
        for (String provinceCode : seekRentDTO.getAreaCodes()) {
            pool.execute(() -> {
                marketService.seeKProvince(provinceCode, seekRentDTO.getDays(), seekRentDTO.getPrice(), userId, majorMarketOrder.getId());
            });
        }
        return Result.success(null, null);
    }

    @GetMapping("getProvinceInfos")
    @ApiOperation("获取省份和数量")
    @ApiVersion(group = ApiVersionConstant.FAP_APP103)
    @Transactional(rollbackFor = Exception.class)
    public Result<?> getProvinceInfos() {
        return Result.success(areaService.getProviceInfos(), null);
    }

}
