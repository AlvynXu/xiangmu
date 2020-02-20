package com.guangxuan.controller.pay.controller.wxpay;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.beust.jcommander.internal.Lists;
import com.guangxuan.config.ApiVersion;
import com.guangxuan.config.ApiVersionConstant;
import com.guangxuan.config.SystemProperties;
import com.guangxuan.constant.*;
import com.guangxuan.controller.pay.constant.ProductConstant;
import com.guangxuan.controller.pay.entity.WxPayBean;
import com.guangxuan.controller.pay.utils.StringUtils;
import com.guangxuan.distribution.Distribution;
import com.guangxuan.dto.Result;
import com.guangxuan.dto.SeekRentDTO;
import com.guangxuan.enumration.BusinessFailEnum;
import com.guangxuan.exception.BusinessException;
import com.guangxuan.locker.annotation.Lock;
import com.guangxuan.model.*;
import com.guangxuan.service.*;
import com.guangxuan.shiro.ThreadLocalCurrentUser;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.enums.TradeType;
import com.ijpay.core.kit.HttpKit;
import com.ijpay.core.kit.IpKit;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.WxPayApiConfig;
import com.ijpay.wxpay.WxPayApiConfigKit;
import com.ijpay.wxpay.model.TransferModel;
import com.ijpay.wxpay.model.UnifiedOrderModel;
import com.jfinal.kit.StrKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.guangxuan.constant.GoodStatusConstant.NOT_SOLD;
import static com.guangxuan.constant.GoodStatusConstant.SOLD;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875</p>
 *
 * <p>Node.js 版: https://gitee.com/javen205/TNW</p>
 *
 * <p>微信支付 Demo</p>
 *
 * @author Javen
 */
@RestController
@RequestMapping("/wxPay")
@Api(value = "展位controller", tags = {"微信支付接口"})
public class WxPayController extends AbstractWxPayApiController {

    private Logger logger = LoggerFactory.getLogger("payLogger");

    @Autowired
    WxPayBean wxPayBean;

    LinkedBlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<>(100);

    ThreadFactory threadFactory = new ThreadFactory() {
        //  int i = 0;  用并发安全的包装类
        AtomicInteger atomicInteger = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            //创建线程 吧任务传进来
            Thread thread = new Thread(r);
            // 给线程起个名字
            thread.setName("wxpay" + atomicInteger.getAndIncrement());
            return thread;
        }
    };

    private ThreadPoolExecutor pool = new ThreadPoolExecutor(10, 10, 1, TimeUnit.SECONDS, blockingQueue, threadFactory);


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
    private CommissionLogService commissionLogService;

    @Resource
    private UsersService usersService;
    @Resource
    private SystemProperties systemProperties;
    @Resource
    private SystemConfigService systemConfigService;

    @Resource
    private MarketService marketService;

    @Resource
    private MarketOrderService marketOrderService;


    private String notifyUrl;
    private String refundNotifyUrl;
    private static final String USER_PAYING = "USERPAYING";


    @Override
    public WxPayApiConfig getApiConfig() {
        WxPayApiConfig apiConfig;

        try {
            apiConfig = WxPayApiConfigKit.getApiConfig(wxPayBean.getAppId());
        } catch (Exception e) {
            apiConfig = WxPayApiConfig.builder()
                    .appId(wxPayBean.getAppId())
                    .mchId(wxPayBean.getMchId())
                    .partnerKey(wxPayBean.getPartnerKey())
                    .certPath(wxPayBean.getCertPath())
                    .domain(wxPayBean.getDomain())
                    .build();
        }
        notifyUrl = apiConfig.getDomain().concat("/wxPay/payNotify");
        refundNotifyUrl = apiConfig.getDomain().concat("/wxPay/refundNotify");
        return apiConfig;
    }

    /**
     * 企业付款到零钱
     */
    @ApiOperation("提现到零钱")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "amount", value = "体现金额", required = true, dataType = "BigDecimal")
    })
    @ApiVersion(group = ApiVersionConstant.FAP_APP101)
    @RequestMapping(value = "/transfer", method = {RequestMethod.POST})
    @Lock(value = "GUANGXUAN:WITHDRAWAL")
    public Result<?> transfer(HttpServletRequest request,
                              @RequestParam(value = "amount") Integer amount) {
        Users user = ThreadLocalCurrentUser.getUsers();
        if (!user.getIsVip()) {
            throw new BusinessException(BusinessFailEnum.NO_PRIVILEGES);
        }
        SystemConfig systemConfig = systemConfigService.getById(1L);
        if (amount % 10 != 0) {
            throw new BusinessException(BusinessFailEnum.VALID_AMOUNT_FAIL);
        }
        if (amount <= 0) {
            throw new BusinessException(BusinessFailEnum.MONEY_LESS);
        }
        if (amount > 5000) {
            throw new BusinessException(BusinessFailEnum.MONEY_MUCH);
        }
        if (user.getBalance().compareTo(new BigDecimal(amount)) == -1) {
            throw new BusinessException(BusinessFailEnum.BALANCE_CANT_PAY);
        }
        if (new BigDecimal(amount).compareTo(systemConfig.getRechargeMin()) == -1
                || new BigDecimal(amount).compareTo(systemConfig.getRechargeMax()) == 1) {
            throw new BusinessException(BusinessFailEnum.BALANCE_CANT_PAY);
        }
        BigDecimal finalAmount = new BigDecimal(amount).subtract(new BigDecimal(amount).multiply(systemConfig.getRechargeRate()));
        String openId = user.getWxOpenId();
        if (openId == null) {
            throw new BusinessException(BusinessFailEnum.NO_WECHAT_PAY);
        }
        String ip = IpKit.getRealIp(request);
        if (StrKit.isBlank(ip)) {
            ip = "127.0.0.1";
        }
        WxPayApiConfig wxPayApiConfig = WxPayApiConfigKit.getWxPayApiConfig();
        String orderNo = WxPayKit.generateStr();

        Map<String, String> params = TransferModel.builder()
                .mch_appid(wxPayApiConfig.getAppId())
                .mchid(wxPayApiConfig.getMchId())
                .nonce_str(WxPayKit.generateStr())
                .partner_trade_no(orderNo)
                .openid(openId)
                .check_name("NO_CHECK")
                .amount(String.valueOf(finalAmount.multiply(new BigDecimal(100)).intValue()))
                .desc("提现")
                .spbill_create_ip(ip)
                .build()
                .createSign(wxPayApiConfig.getPartnerKey(), SignType.MD5, false);
        // 提现
        String transfers = WxPayApi.transfers(params, wxPayApiConfig.getCertPath(), wxPayApiConfig.getMchId());
        logger.info("提现结果:" + transfers);
        Map<String, String> map = WxPayKit.xmlToMap(transfers);
        String returnCode = map.get("return_code");
        String resultCode = map.get("result_code");
        if (WxPayKit.codeIsOk(returnCode) && WxPayKit.codeIsOk(resultCode)) {
            // 提现成功
            PayLog payLog = PayLog.builder()
                    .realAmount(finalAmount).fee(new BigDecimal(amount).subtract(finalAmount))
                    .amount(new BigDecimal("0").subtract(new BigDecimal(amount))).userId(ThreadLocalCurrentUser.getUsers().getId())
                    .extend1(orderNo).createTime(new Date()).status(PayConstant.SUCCESS_PAY)
                    .goodsId(ThreadLocalCurrentUser.getUsers().getId()).payType(PayTypeConstant.WECHAT_PAY)
                    .source(PayResourceConstant.WITHDRAWAL.getType()).product(ProductConstant.WITHDRAWAL).build();
            user.setBalance(user.getBalance().subtract(new BigDecimal(amount)));
            payLogService.save(payLog);
            usersService.updateById(user);
            return Result.success(user, null);
        } else {
            // 提现失败
            PayLog payLog = PayLog.builder()
                    .realAmount(finalAmount).fee(new BigDecimal(amount).subtract(finalAmount))
                    .amount(new BigDecimal(amount)).userId(ThreadLocalCurrentUser.getUsers().getId())
                    .extend1(orderNo).createTime(new Date()).status(PayConstant.FAIL_PAY)
                    .goodsId(ThreadLocalCurrentUser.getUsers().getId()).payType(PayTypeConstant.WECHAT_PAY)
                    .source(PayResourceConstant.WITHDRAWAL.getType()).product(ProductConstant.WITHDRAWAL).build();
            payLogService.save(payLog);
            return Result.fail(null, "提现失败");
        }

    }

    private void buyVip(String orderNo, String openId) {
        PayLog payLog = payLogService.getOne(new LambdaQueryWrapper<PayLog>().eq(PayLog::getExtend1, orderNo));
        // 订单状态验证
        if (payLog.getStatus() != PayConstant.WAIT_PAY) {
            return;
        }
        payLog.setPayType(PayTypeConstant.WECHAT_PAY);
        payLog.setStatus(PayConstant.SUCCESS_PAY);
        payLogService.updateById(payLog);
        Users user = usersService.getById(payLog.getUserId());
        user.setIsVip(true);
        if (user.getLevel() < 1) {
            user.setLevel(1);
        }
        user.setWxOpenId(openId);
        usersService.updateById(user);
    }


    private void buyBooth(String orderNo, String openId) throws NoSuchFieldException, IllegalAccessException {
        PayLog payLog = payLogService.getOne(new LambdaQueryWrapper<PayLog>().eq(PayLog::getExtend1, orderNo));
        // 订单状态验证
        if (payLog.getStatus() != PayConstant.WAIT_PAY) {
            return;
        }
        payLog.setPayType(PayTypeConstant.WECHAT_PAY);
        payLog.setStatus(PayConstant.SUCCESS_PAY);
        payLogService.updateById(payLog);
        Users user = usersService.getById(payLog.getUserId());

        if (user.getLevel() < 2) {
            user.setLevel(2);
        }

        user.setWxOpenId(openId);
        usersService.updateById(user);

        Booth booth = boothService.getById(payLog.getGoodsId());
        BoothUsers boothUsers = new BoothUsers();
        boothUsers.setBoothId(booth.getId());
        boothUsers.setCreateTime(new Date());
        boothUsers.setOrderNo(orderNo);
        boothUsers.setOrderStatus(OrderStatus.PAID);
        boothUsers.setStatus(PayConstant.SUCCESS_PAY);
        boothUsers.setUserId(user.getId());
        boothUsers.setExpireTime(Date.from(LocalDate.now().plusYears(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        booth.setStatus(SOLD);
        booth.setUseStatus(UseStatus.NOT_USE);
        boothService.updateById(booth);
        boothUsersService.save(boothUsers);
        Street street = streetService.getById(booth.getStreetId());
        distribution.distribution(user.getId(), new BigDecimal(0).subtract(payLog.getAmount()), -1, street.getCode(), null);
    }

    private void buyStreet(String orderNo, String openId) throws NoSuchFieldException, IllegalAccessException {
        PayLog payLog = payLogService.getOne(new LambdaQueryWrapper<PayLog>().eq(PayLog::getExtend1, orderNo));
        // 订单状态验证
        if (payLog.getStatus() != PayConstant.WAIT_PAY) {
            return;
        }
        payLog.setPayType(PayTypeConstant.WECHAT_PAY);
        payLog.setStatus(PayConstant.SUCCESS_PAY);
        payLogService.updateById(payLog);
        Users user = usersService.getById(payLog.getUserId());

        if (user.getLevel() < 3) {
            user.setLevel(3);
        }
        user.setChooseBoothCount(user.getChooseBoothCount() + 10);
        user.setTotalChooseBoothCount(user.getTotalChooseBoothCount() + 10);
        user.setWxOpenId(openId);
        usersService.updateById(user);

        Street street = streetService.getById(payLog.getGoodsId());
        StreetPartnerOrder streetPartnerOrder = new StreetPartnerOrder();
        streetPartnerOrder.setAmount(payLog.getAmount());
        streetPartnerOrder.setCreateTime(new Date());
        streetPartnerOrder.setOrderNo(orderNo);
        streetPartnerOrder.setPayType(PayTypeConstant.WECHAT_PAY);
        streetPartnerOrder.setStreetId(street.getId());
        streetPartnerOrder.setUserId(user.getId());
        streetPartnerOrder.setOrderStatus(OrderStatus.PAID);
        streetPartnerOrder.setStatus(PayConstant.SUCCESS_PAY);
        streetPartnerOrderService.save(streetPartnerOrder);
        street.setStatus(SOLD);
        streetService.updateById(street);
        distribution.distribution(user.getId(), new BigDecimal(0).subtract(payLog.getAmount()), 1, street.getAreaCode(), null);

    }

    private void recharge(String orderNo, String openId) throws NoSuchFieldException, IllegalAccessException {
        //充值
        PayLog payLog = payLogService.getOne(new LambdaQueryWrapper<PayLog>().eq(PayLog::getExtend1, orderNo));
        // 订单状态验证
        if (payLog.getStatus() != PayConstant.WAIT_PAY) {
            return;
        }
        payLog.setPayType(PayTypeConstant.WECHAT_PAY);
        payLog.setStatus(PayConstant.SUCCESS_PAY);
        payLogService.updateById(payLog);
        Users user = usersService.getById(payLog.getUserId());
        user.setBalance(user.getBalance().add(payLog.getAmount()));
        user.setProfit(user.getProfit().add(payLog.getAmount()));
        user.setWxOpenId(openId);
        usersService.updateById(user);
        SystemConfig systemConfig = systemConfigService.getById(1L);
        CommissionLog commissionLog = CommissionLog.builder()
                .amount(payLog.getAmount())
                .createTime(new Date())
                .fromUserId(user.getId())
                .rate(systemConfig.getRechargeRate())
                .totalAmount(payLog.getAmount())
                .userId(user.getId())
                .type(CommissionLogConstant.RECHARGE)
                .build();
        commissionLogService.save(commissionLog);
    }

    private void seekRent(String orderNo, String openId) throws NoSuchFieldException, IllegalAccessException {
        //充值
        PayLog payLog = payLogService.getOne(new LambdaQueryWrapper<PayLog>().eq(PayLog::getExtend1, orderNo));
        // 订单状态验证
        if (payLog.getStatus() != PayConstant.WAIT_PAY) {
            return;
        }
        payLog.setPayType(PayTypeConstant.WECHAT_PAY);
        payLog.setStatus(PayConstant.SUCCESS_PAY);
        payLogService.updateById(payLog);
        Users user = usersService.getById(payLog.getUserId());
        user.setWxOpenId(openId);
        usersService.updateById(user);
        MarketOrder marketOrder = marketOrderService.getById(payLog.getGoodsId());
        marketOrder.setPayStatus(PayConstant.SUCCESS_PAY);
        marketOrderService.updateById(marketOrder);
        // 发布消息
        List<Market> markets = new ArrayList<>();
        List<String> streetCodes = Lists.newArrayList(marketOrder.getStreetCodes().split(","));
        SystemConfig systemConfig = systemConfigService.getById(1L);
        for (String areaCode : streetCodes) {
            Market market = new Market();
            market.setStatus(MarketStatusConstant.WAIT_RENT);
            market.setAreaCode(areaCode);
            market.setNumber(1);
            market.setPrice(marketOrder.getPrice());
            marketOrder.setType(MarketType.RENT_SEEK);
            market.setDays(marketOrder.getDays());
            market.setMarkertEndTime(Date.from(LocalDateTime.now().plusDays(systemProperties.getMarketDays()).atZone(ZoneId.systemDefault()).toInstant()));
            market.setRate(systemConfig.getSeekRentRate());
            market.setType(MarketType.RENT_SEEK);
            market.setUserId(user.getId());
            market.setOrderId(marketOrder.getId());
            markets.add(market);
        }
        marketService.saveBatch(markets);
    }

    /**
     * 异步通知
     */
    @RequestMapping(value = "/payNotify", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String payNotify(HttpServletRequest request) {
        logger.info("================================================================================");
        String xmlMsg = HttpKit.readData(request);
        logger.info("支付通知=" + xmlMsg);
        Map<String, String> params = WxPayKit.xmlToMap(xmlMsg);
        logger.info("支付结果" + JSON.toJSONString(params));
        String returnCode = params.get("return_code");
        String openId = params.get("openid");

        // 注意重复通知的情况，同一订单号可能收到多次通知，请注意一定先判断订单状态
        // 注意此处签名方式需与统一下单的签名类型一致
        Map<String, String> xml = new HashMap<String, String>(2);
        xml.put("return_code", "SUCCESS");
        xml.put("return_msg", "OK");
        if (WxPayKit.verifyNotify(params, WxPayApiConfigKit.getWxPayApiConfig().getPartnerKey(), SignType.HMACSHA256)) {
            if (WxPayKit.codeIsOk(returnCode)) {
                // 更新订单信息
                String outOrderId = params.get("out_trade_no");
                PayLog payLog = payLogService.getOne(new LambdaQueryWrapper<PayLog>().eq(PayLog::getExtend1, outOrderId));
                pool.execute(() -> {
                    try {
                        // 分销
                        if (payLog.getSource().equals(PayResourceConstant.BUY_VIP.getType())) {
                            buyVip(outOrderId, openId);
                        } else if (payLog.getSource().equals(PayResourceConstant.BUY_BOOTH.getType())) {
                            buyBooth(outOrderId, openId);
                        } else if (payLog.getSource().equals(PayResourceConstant.BUY_STREET.getType())) {
                            buyStreet(outOrderId, openId);
                        } else if (payLog.getSource().equals(PayResourceConstant.RECHARGE.getType())) {
                            recharge(outOrderId, openId);
                        } else if (payLog.getSource().equals(PayResourceConstant.RENT.getType())) {
                            seekRent(outOrderId, openId);
                        }
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
                return WxPayKit.toXml(xml);
            }
            // 支付失败111
            else {
                // 更新订单信息
                String outOrderId = params.get("nonce_str");
                // 非成功
                PayLog payLog = payLogService.getOne(new LambdaQueryWrapper<PayLog>().eq(PayLog::getExtend1, outOrderId));
                // 订单状态验证
                payLog.setStatus(PayConstant.FAIL_PAY);
                payLogService.updateById(payLog);
                return WxPayKit.toXml(xml);
            }
        }
        return null;
    }

    /**
     * 充值app支付
     */
    @PostMapping(value = "/buy")
    @ApiOperation("充值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "amount", value = "充值金额", required = true, dataType = "BigDecimal")
    })
    @ApiVersion(group = ApiVersionConstant.FAP_APP101)
    public Result<?> buyVip(HttpServletRequest request, @RequestParam("amount") BigDecimal amount) {
        logger.info("===============================================================微信购买充值");
        if (amount.compareTo(new BigDecimal(50000)) == 1) {
            throw new BusinessException(BusinessFailEnum.MONEY_MUCH);
        }
        if (amount.compareTo(new BigDecimal(0)) <= 0) {
            throw new BusinessException(BusinessFailEnum.MONEY_LESS);
        }
        Long userId = ThreadLocalCurrentUser.getUsers().getId();
        logger.info("当前用户ID{}", userId);
        String orderNo = WxPayKit.generateStr();
        logger.info("订单号{}付款金额{}", orderNo, amount);
        String ip = IpKit.getRealIp(request);
        if (StrKit.isBlank(ip)) {
            ip = "127.0.0.1";
        }
        WxPayApiConfig wxPayApiConfig = WxPayApiConfigKit.getWxPayApiConfig();
        Map<String, String> params = UnifiedOrderModel
                .builder()
                .appid(wxPayApiConfig.getAppId())
                .mch_id(wxPayApiConfig.getMchId())
                .nonce_str(WxPayKit.generateStr())
                .body("会员")
                .time_expire(DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now().plusMinutes(systemProperties.getPayMinutes())))
                .out_trade_no(orderNo)
//                .total_fee("1")
                .total_fee(String.valueOf(amount.multiply(new BigDecimal(100)).intValue()))
                .spbill_create_ip(ip)
                .notify_url(notifyUrl)
                .trade_type(TradeType.APP.getTradeType())
                .build()
                .createSign(wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.pushOrder(false, params);

        logger.info(xmlResult);
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);

        String returnCode = result.get("return_code");
        String returnMsg = result.get("return_msg");
        if (!WxPayKit.codeIsOk(returnCode)) {
            return Result.fail(returnMsg);
        }
        String prepayId = result.get("prepay_id");
        Map<String, String> packageParams = WxPayKit
                .appPrepayIdCreateSign(wxPayApiConfig.getAppId(), wxPayApiConfig.getMchId(), prepayId,
                        wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);

        String jsonStr = JSON.toJSONString(packageParams);
        packageParams.put("time", systemProperties.getPayMinutes().toString());
        logger.info("返回apk的参数:" + jsonStr);
        PayLog payLog = PayLog.builder()
                .realAmount(amount)
                .amount(amount).userId(ThreadLocalCurrentUser.getUsers().getId())
                .extend1(orderNo).createTime(new Date()).status(PayConstant.WAIT_PAY)
                .goodsId(ThreadLocalCurrentUser.getUsers().getId()).payType(PayTypeConstant.WECHAT_PAY)
                .source(PayResourceConstant.RECHARGE.getType()).product(ProductConstant.RECHARGE).build();
        payLogService.save(payLog);
        logger.info("===============================================================");
        return Result.success(packageParams, null);
    }

    /**
     * app支付
     */
    @GetMapping(value = "/buyVip")
    @ResponseBody
    public Result<?> buyVip(HttpServletRequest request) {
        logger.info("===============================================================微信购买vip");
        Long userId = ThreadLocalCurrentUser.getUsers().getId();
        logger.info("当前用户ID{}", userId);
        if (redisTemplate.hasKey(RedisConstant.BUY_VIP + userId)) {
            throw new BusinessException(BusinessFailEnum.ON_SALE);
        }

        BigDecimal amount = mallConfigService.getById(1L).getThreshold1().setScale(2, BigDecimal.ROUND_HALF_UP);
        String orderNo = WxPayKit.generateStr();
        logger.info("订单号{}付款金额{}", orderNo, amount);
        String ip = IpKit.getRealIp(request);
        if (StrKit.isBlank(ip)) {
            ip = "127.0.0.1";
        }
        WxPayApiConfig wxPayApiConfig = WxPayApiConfigKit.getWxPayApiConfig();
        if (systemProperties.getDebugOff()) {
            amount = new BigDecimal("0.01");
        }
        Map<String, String> params = UnifiedOrderModel
                .builder()
                .appid(wxPayApiConfig.getAppId())
                .mch_id(wxPayApiConfig.getMchId())
                .nonce_str(WxPayKit.generateStr())
                .body("会员")
                .time_expire(DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now().plusMinutes(systemProperties.getPayMinutes())))
                .out_trade_no(orderNo)
//                .total_fee("1")
                .total_fee(String.valueOf(amount.multiply(new BigDecimal(100)).intValue()))
                .spbill_create_ip(ip)
                .notify_url(notifyUrl)
                .trade_type(TradeType.APP.getTradeType())
                .build()
                .createSign(wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.pushOrder(false, params);

        logger.info(xmlResult);
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);

        String returnCode = result.get("return_code");
        String returnMsg = result.get("return_msg");
        if (!WxPayKit.codeIsOk(returnCode)) {
            return Result.fail(returnMsg);
        }
        String prepayId = result.get("prepay_id");
        Map<String, String> packageParams = WxPayKit
                .appPrepayIdCreateSign(wxPayApiConfig.getAppId(), wxPayApiConfig.getMchId(), prepayId,
                        wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);

        String jsonStr = JSON.toJSONString(packageParams);
        redisTemplate.opsForValue().set(RedisConstant.BUY_VIP + userId, userId, systemProperties.getPayMinutes(), TimeUnit.MINUTES);
        packageParams.put("time", systemProperties.getPayMinutes().toString());
        PayLog payLog = PayLog.builder()
                .realAmount(amount)
                .amount(new BigDecimal("0").subtract(amount)).userId(ThreadLocalCurrentUser.getUsers().getId())
                .extend1(orderNo).createTime(new Date()).status(PayConstant.WAIT_PAY)
                .goodsId(ThreadLocalCurrentUser.getUsers().getId()).payType(PayTypeConstant.WECHAT_PAY)
                .source(PayResourceConstant.BUY_VIP.getType()).product(ProductConstant.VIP).build();
        payLogService.save(payLog);
        logger.info("返回apk的参数:" + jsonStr);
        logger.info("===============================================================");
        return Result.success(packageParams, null);
    }

    /**
     * app支付
     *
     * @param boothId 展位id
     */
    @GetMapping(value = "/buyBooth")
    @ResponseBody
    @Lock(value = "GUANGXUAN:BUY_BOOTH")
    @Transactional(rollbackFor = Exception.class)
    public Result<?> buyBooth(Long boothId, HttpServletRequest request) {
        logger.info("===============================================================购买展位");
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
        Long userId = ThreadLocalCurrentUser.getUsers().getId();

        BigDecimal amount = mallConfigService.getById(1L).getThreshold2().setScale(2, BigDecimal.ROUND_HALF_UP);
        String orderNo = WxPayKit.generateStr();
        logger.info("发起人{}订单号{}付款金额{},展位ID{}", userId, orderNo, amount, boothId);

        String ip = IpKit.getRealIp(request);
        if (StrKit.isBlank(ip)) {
            ip = "127.0.0.1";
        }

        WxPayApiConfig wxPayApiConfig = WxPayApiConfigKit.getWxPayApiConfig();
        if (systemProperties.getDebugOff()) {
            amount = new BigDecimal("0.01");
        }
        Map<String, String> params = UnifiedOrderModel
                .builder()
                .appid(wxPayApiConfig.getAppId())
                .mch_id(wxPayApiConfig.getMchId())
                .nonce_str(WxPayKit.generateStr())
                .body("会员")
                .time_expire(DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now().plusMinutes(systemProperties.getPayMinutes())))
                .out_trade_no(orderNo)
//                .total_fee("1")
                .total_fee(String.valueOf(amount.multiply(new BigDecimal(100)).intValue()))
                .spbill_create_ip(ip)
                .notify_url(notifyUrl)
                .trade_type(TradeType.APP.getTradeType())
                .build()
                .createSign(wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.pushOrder(false, params);

        logger.info(xmlResult);
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);

        String returnCode = result.get("return_code");
        String returnMsg = result.get("return_msg");
        if (!WxPayKit.codeIsOk(returnCode)) {
            return Result.fail(returnMsg);
        }
        String prepayId = result.get("prepay_id");
        Map<String, String> packageParams = WxPayKit
                .appPrepayIdCreateSign(wxPayApiConfig.getAppId(), wxPayApiConfig.getMchId(), prepayId,
                        wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);
        String jsonStr = JSON.toJSONString(packageParams);
        redisTemplate.opsForValue().set(RedisConstant.BUY_BOOTH + boothId, boothId, systemProperties.getPayMinutes(), TimeUnit.MINUTES);
        packageParams.put("time", systemProperties.getPayMinutes().toString());
        PayLog payLog = PayLog.builder()
                .realAmount(amount)
                .amount(new BigDecimal("0").subtract(amount)).userId(ThreadLocalCurrentUser.getUsers().getId())
                .extend1(orderNo).createTime(new Date()).status(PayConstant.WAIT_PAY)
                .goodsId(boothId).payType(PayTypeConstant.WECHAT_PAY)
                .source(PayResourceConstant.BUY_BOOTH.getType()).product(ProductConstant.STREET).build();
        payLogService.save(payLog);
        logger.info("返回apk的参数:" + jsonStr);
        logger.info("================================================================================");
        return Result.success(packageParams, null);
    }


    /**
     * app支付
     *
     * @param streetId 购买
     */
    @GetMapping(value = "/bugStreet")
    @ResponseBody
    @Lock(value = "GUANGXUAN:BUY_STREET")
    @Transactional(rollbackFor = Exception.class)
    public Result<?> bugStreet(Long streetId, HttpServletRequest request) {
        logger.info("===============================================================购买街道地主");
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
        Long userId = ThreadLocalCurrentUser.getUsers().getId();
        logger.info("{}发起购买街道地主，街道地主编号{}", userId, streetId);
        String orderNo = StringUtils.getOutTradeNo();
        BigDecimal amount = mallConfigService.getById(1L).getThreshold3().setScale(2, BigDecimal.ROUND_HALF_UP);
        logger.info("发起人{}订单号{}付款金额{},街道ID{}", userId, orderNo, amount, streetId);
        if (systemProperties.getDebugOff()) {
            amount = new BigDecimal("0.01");
        }
        String ip = IpKit.getRealIp(request);
        if (StrKit.isBlank(ip)) {
            ip = "127.0.0.1";
        }
        if (systemProperties.getDebugOff()) {
            amount = new BigDecimal("0.01");
        }
        WxPayApiConfig wxPayApiConfig = WxPayApiConfigKit.getWxPayApiConfig();
        Map<String, String> params = UnifiedOrderModel
                .builder()
                .appid(wxPayApiConfig.getAppId())
                .time_expire(DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now().plusMinutes(systemProperties.getPayMinutes())))
                .mch_id(wxPayApiConfig.getMchId())
                .nonce_str(WxPayKit.generateStr())
                .body("会员")
                .out_trade_no(orderNo)
//                .total_fee("1")
                .total_fee(String.valueOf(amount.multiply(new BigDecimal(100)).intValue()))
                .spbill_create_ip(ip)
                .notify_url(notifyUrl)
                .trade_type(TradeType.APP.getTradeType())
                .build()
                .createSign(wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.pushOrder(false, params);

        logger.info(xmlResult);
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);

        String returnCode = result.get("return_code");
        String returnMsg = result.get("return_msg");
        if (!WxPayKit.codeIsOk(returnCode)) {
            return Result.fail(returnMsg);
        }
        String prepayId = result.get("prepay_id");
        Map<String, String> packageParams = WxPayKit
                .appPrepayIdCreateSign(wxPayApiConfig.getAppId(), wxPayApiConfig.getMchId(), prepayId,
                        wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);
        redisTemplate.opsForValue().set(RedisConstant.BUY_STREET + streetId, streetId, systemProperties.getPayMinutes(), TimeUnit.MINUTES);
        packageParams.put("time", systemProperties.getPayMinutes().toString());
        PayLog payLog = PayLog.builder()
                .realAmount(amount)
                .amount(new BigDecimal("0").subtract(amount)).userId(ThreadLocalCurrentUser.getUsers().getId())
                .extend1(orderNo).createTime(new Date()).status(PayConstant.WAIT_PAY)
                .goodsId(streetId)
                .payType(PayTypeConstant.WECHAT_PAY)
                .source(PayResourceConstant.BUY_STREET.getType())
                .product(ProductConstant.STREET).build();
        payLogService.save(payLog);
        logger.info("返回apk的参数:" + packageParams);
        logger.info("================================================================================");
        return Result.success(packageParams, null);
    }

    @PostMapping("/seekRent")
    @ApiOperation("求租")
    @ApiVersion(group = ApiVersionConstant.FAP_APP101)
    @Lock(value = "GUANGXUAN:SEEK_RENT")
    @Transactional(rollbackFor = Exception.class)
    public Result<?> seekRent(@RequestBody @Valid SeekRentDTO seekRentDTO, HttpServletRequest request) {
        logger.info("===============================================================求租");
        if (seekRentDTO.getPrice().compareTo(new BigDecimal(0)) == 0) {
            throw new BusinessException(BusinessFailEnum.MONEY_LESS);
        }
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
        marketOrder.setPayStatus(PayConstant.WAIT_PAY);
        marketOrder.setTotalCount(seekRentDTO.getAreaCodes().size());
        marketOrder.setSoldCount(0);
        marketOrder.setUserId(userId);
        marketOrder.setStreetCodes(String.join(",", seekRentDTO.getAreaCodes()));
        marketOrderService.save(marketOrder);
        int streetCount = streetService.count(new LambdaQueryWrapper<Street>().in(Street::getCode, seekRentDTO.getAreaCodes()));
        if (streetCount != seekRentDTO.getAreaCodes().size()) {
            throw new BusinessException(BusinessFailEnum.STREET_CODE_VALID_FAIL);
        }
        // 扣除保证金
//        Users user = usersService.getById(userId);
        SystemConfig systemConfig = systemConfigService.getById(1L);
        BigDecimal boothMoney = marketOrder.getPrice()
                .multiply(new BigDecimal(seekRentDTO.getAreaCodes().size()))
                .multiply(new BigDecimal(marketOrder.getDays()));
        BigDecimal fee = boothMoney.multiply(systemConfig.getSeekRentRate());
        BigDecimal money = boothMoney.add(fee);
        BigDecimal allMoney = new BigDecimal(String.format("%.2f", money));
        if (allMoney.compareTo(new BigDecimal(50000)) == 1) {
            throw new BusinessException(BusinessFailEnum.MONEY_MUCH_1);
        }
        String orderNo = StringUtils.getOutTradeNo();
        String ip = IpKit.getRealIp(request);
        if (StrKit.isBlank(ip)) {
            ip = "127.0.0.1";
        }
        WxPayApiConfig wxPayApiConfig = WxPayApiConfigKit.getWxPayApiConfig();
        Map<String, String> params = UnifiedOrderModel
                .builder()
                .appid(wxPayApiConfig.getAppId())
                .mch_id(wxPayApiConfig.getMchId())
                .time_expire(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
                        .format(LocalDateTime.now().plusMinutes(systemProperties.getPayMinutes())))
                .nonce_str(WxPayKit.generateStr())
                .body("求租")
                .out_trade_no(orderNo)
//                .total_fee("1")
                .total_fee(String.valueOf(allMoney.multiply(new BigDecimal(100)).intValue()))
                .spbill_create_ip(ip)
                .notify_url(notifyUrl)
                .trade_type(TradeType.APP.getTradeType())
                .build()
                .createSign(wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.pushOrder(false, params);

        logger.info(xmlResult);
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);

        String returnCode = result.get("return_code");
        String returnMsg = result.get("return_msg");
        if (!WxPayKit.codeIsOk(returnCode)) {
            return Result.fail(returnMsg);
        }
        String prepayId = result.get("prepay_id");
        Map<String, String> packageParams = WxPayKit
                .appPrepayIdCreateSign(wxPayApiConfig.getAppId(), wxPayApiConfig.getMchId(), prepayId,
                        wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);
        PayLog payLog = PayLog.builder()
                .realAmount(boothMoney)
                .amount(new BigDecimal("0").subtract(allMoney)).userId(ThreadLocalCurrentUser.getUsers().getId())
                .extend1(orderNo).createTime(new Date()).status(PayConstant.WAIT_PAY)
                .goodsId(marketOrder.getId())
                .payType(PayTypeConstant.WECHAT_PAY)
                .source(PayResourceConstant.RENT.getType())
                .product(ProductConstant.SEEK_RENT).build();
        payLogService.save(payLog);
        packageParams.put("time", systemProperties.getPayMinutes().toString());
        logger.info("返回apk的参数:" + packageParams);
        logger.info("================================================================================");
        return Result.success(packageParams, null);
    }

}
