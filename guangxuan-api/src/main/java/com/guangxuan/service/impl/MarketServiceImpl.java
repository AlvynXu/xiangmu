package com.guangxuan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guangxuan.config.SystemProperties;
import com.guangxuan.constant.*;
import com.guangxuan.dto.*;
import com.guangxuan.enumration.BusinessFailEnum;
import com.guangxuan.exception.BusinessException;
import com.guangxuan.mapper.MarketMapper;
import com.guangxuan.model.*;
import com.guangxuan.service.*;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhuolin
 * @since 2019-12-17
 */
@Service
public class MarketServiceImpl extends ServiceImpl<MarketMapper, Market> implements MarketService {

    @Resource
    private UsersService usersService;

    @Resource
    private BoothService boothService;

    @Resource
    private StreetService streetService;

    @Resource
    private SystemConfigService systemConfigService;

    @Resource
    private SystemProperties systemProperties;

    @Resource
    private MarketOrderService marketOrderService;
    @Resource
    private MessageService messageService;

    @Resource
    private PayLogService payLogService;


    @Override
    public Object getMyRent(Integer type, String areaCode, Long userId) {
        return marketOrderService.getMyCityMarket(areaCode, type, userId);
    }

    @Override
    public Object getRent(Integer type, String areaCode, Integer page, Integer size, Long userId) {
        return marketOrderService.getMyCityMarketPage(new Page(page, size), areaCode, type, userId);
    }

    @Override
    public Object rent(RentDTO rentDTO, Long id) {
        SystemConfig systemConfig = systemConfigService.getById(1L);
        Collection<Booth> booths = boothService.listByIds(rentDTO.getBoothIds());
        List<Market> markets = new ArrayList<>();
        for (Booth booth : booths) {
            if (!booth.getUseStatus().equals(UseStatus.NOT_USE)) {
                throw new BusinessException(BusinessFailEnum.BOOTH_IN_USER);
            }
            Street street = streetService.getById(booth.getStreetId());
            Market market = new Market();
            market.setBoothId(booth.getId());
            market.setStatus(MarketStatusConstant.WAIT_RENT);
            market.setAreaCode(street.getCode());
            market.setNumber(1);
            market.setPrice(rentDTO.getPrice());
            market.setDays(rentDTO.getDays());
            market.setMarkertEndTime(Date.from(LocalDateTime.now().plusDays(3).atZone(ZoneId.systemDefault()).toInstant()));
            market.setRate(systemConfig.getRentRate());
            market.setType(MarketType.RENT);
            market.setUserId(id);
            booth.setUseStatus(UseStatus.RENT_USE);
            markets.add(market);
        }
        this.saveBatch(markets);
        boothService.updateBatchById(booths);
        return null;
    }


    @Override
    public Object getCanRentStreet(Long userId, Long orderId) {
        MarketOrder marketOrder = marketOrderService.getById(orderId);
        if (marketOrder == null) {
            throw new BusinessException(BusinessFailEnum.NOT_GET_DATA);
        }
        if (marketOrder.getUserId().equals(userId)) {
            throw new BusinessException(BusinessFailEnum.CANNOT_RENT_SELF);
        }
        List<UserStreetDTO> streetDTOS = this.getBaseMapper().getUserStreetDTO(userId, orderId);
        List<UserStreetDTO> userStreetDTOS = streetService.getMarketStreet(orderId);
        Map<String, UserStreetDTO> map = streetDTOS.stream().collect(Collectors.toMap(UserStreetDTO::getStreetCode, a -> a));
        for (UserStreetDTO userStreetDTO : userStreetDTOS) {
            if (map.containsKey(userStreetDTO.getStreetCode())) {
                userStreetDTO.setHasBooth(true);
            } else {
                userStreetDTO.setHasBooth(false);
            }
            userStreetDTO.setPrice(marketOrder.getPrice());
        }
        SystemConfig systemConfig = systemConfigService.getById(1L);
        userStreetDTOS.sort((o1, o2) -> o2.getHasBooth().compareTo(o1.getHasBooth()));
        UserStreetCountDTO userStreetCountDTO = new UserStreetCountDTO();
        userStreetCountDTO.setCount(streetDTOS.size());
        userStreetCountDTO.setPrice(marketOrder.getPrice());
        userStreetCountDTO.setRentRate(systemConfig.getRentRate());
        userStreetCountDTO.setUserStreetDTOList(userStreetDTOS);
        return userStreetCountDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object rentToOther(RentToOtherDTO rentOtherDTO, Long userId) {
        MarketOrder marketOrder = marketOrderService.getById(rentOtherDTO.getOrderId());
        if (marketOrder == null) {
            throw new BusinessException(BusinessFailEnum.NOT_GET_DATA);
        }
        if (marketOrder.getUserId().equals(userId)) {
            throw new BusinessException(BusinessFailEnum.CANNOT_RENT_SELF);
        }
        List<Market> markets = this.list(new LambdaQueryWrapper<Market>()
                .eq(Market::getOrderId, rentOtherDTO.getOrderId())
                .eq(Market::getStatus, MarketStatusConstant.WAIT_RENT)
                .in(Market::getAreaCode, rentOtherDTO.getStreetCodes()));
        if (markets.size() <= 0) {
            throw new BusinessException(BusinessFailEnum.NOT_GET_DATA);
        }
        List<SoldInfoDTO> soldInfoDTOS = this.getCanRentBooth(userId, rentOtherDTO.getStreetCodes());
        List<Booth> booths = new ArrayList<>();
        Set<Long> boothIds = new HashSet<>();
        Map<String, List<SoldInfoDTO>> map = soldInfoDTOS.stream().collect(Collectors.groupingBy(SoldInfoDTO::getStreetCode));
        for (Market market : markets) {
            if (map.containsKey(market.getAreaCode())) {
                SoldInfoDTO soldInfoDTO = map.get(market.getAreaCode()).get(0);
                Booth booth = boothService.getById(soldInfoDTO.getId());
                booth.setUseStatus(UseStatus.RENT_USED);
                market.setRentEndTime(Date.from(LocalDateTime.now().plusDays(market.getDays()).atZone(ZoneId.systemDefault()).toInstant()));
                market.setStatus(MarketStatusConstant.UN_USED);
                market.setBoothId(booth.getId());
                market.setRentUserId(userId);
                marketOrder.setSoldCount((marketOrder.getSoldCount() == null ? 0 : marketOrder.getSoldCount()) + 1);
                boothIds.add(soldInfoDTO.getId());
                booths.add(booth);
            }
        }
        if (boothIds.size() > 0) {
            List<BoothDetailDTO> boothDetailDTOS = this.boothService.getBoothDetail(boothIds);
            List<Message> messages = new ArrayList<>();
            for (BoothDetailDTO boothDetailDTO : boothDetailDTOS) {
                Message message = Message.builder()
                        .content("您成功租到了1个电线竿！\n"
                                + boothDetailDTO.getCode() + "号电线竿，租用期剩余： "
                                + marketOrder.getDays() + "天")
                        .toUserId(marketOrder.getUserId())
                        .deleted(false)
                        .sender("求租提示")
                        .status(MessageStatus.UNREAD)
                        .createTime(new Date())
                        .build();
                messages.add(message);
            }

            messageService.saveBatch(messages);
        }
        SystemConfig systemConfig = systemConfigService.getById(1L);
        BigDecimal allMoney = marketOrder.getPrice().multiply(new BigDecimal(marketOrder.getDays()))
                .multiply(new BigDecimal(boothIds.size()));
        BigDecimal fee = allMoney.multiply(systemConfig.getRentRate());
        BigDecimal finalMoney = new BigDecimal(String.format("%.2f", allMoney.subtract(fee)));
        PayLog payLog = PayLog.builder()
                .realAmount(allMoney).fee(fee)
                .amount(finalMoney).userId(userId)
                .extend1("PAY_RENT" + UUID.randomUUID().toString()).createTime(new Date()).status(PayConstant.SUCCESS_PAY)
                .goodsId(userId).payType(PayTypeConstant.BALANCE_PAY)
                .source(PayResourceConstant.PAY_RENT.getType()).build();
        payLogService.save(payLog);
        Message message = Message.builder()
                .content("您的租金已到账，请注意查收！租金：" + finalMoney + "元")
                .toUserId(userId)
                .deleted(false)
                .sender("租金到账")
                .status(MessageStatus.UNREAD)
                .createTime(new Date())
                .build();
        Users user = usersService.getById(userId);
        user.setBalance(user.getBalance().add(finalMoney));
        usersService.updateById(user);
        messageService.save(message);
        if (marketOrder.getTotalCount().equals(marketOrder.getSoldCount())) {
            marketOrderService.removeById(marketOrder.getId());
        } else {
            marketOrderService.updateById(marketOrder);
        }
        if (booths.size() <= 0) {
            throw new BusinessException(BusinessFailEnum.BOOTH_NOT_MATCH);
        }
        boothService.updateBatchById(booths);
        this.updateBatchById(markets);
        return null;
    }

    private List<SoldInfoDTO> getCanRentBooth(Long userId, Set<String> streetCodes) {
        return boothService.getCanRentBooth(userId, streetCodes);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object rentOther(RentOtherDTO rentOtherDTO, Long userId) {
        // 使用展位
        List<Market> markets = new ArrayList<>();
        if (rentOtherDTO.getAreaType() == 3) {
            markets = this.getBaseMapper().getCityMarket(rentOtherDTO.getAreaCode(), rentOtherDTO.getDays(), MarketStatusConstant.WAIT_RENT,
                    rentOtherDTO.getPrice(), MarketType.RENT);
        } else if (rentOtherDTO.getAreaType() == 2) {
            markets = this.getBaseMapper().getDistrictMarket(rentOtherDTO.getAreaCode(), rentOtherDTO.getDays(), MarketStatusConstant.WAIT_RENT,
                    rentOtherDTO.getPrice(), MarketType.RENT);
        } else if (rentOtherDTO.getAreaType() == 1) {
            markets = this.getBaseMapper().getStreetMarket(rentOtherDTO.getAreaCode(), rentOtherDTO.getDays(), MarketStatusConstant.WAIT_RENT,
                    rentOtherDTO.getPrice(), MarketType.RENT);
        }
        List<Market> rentMarkets = new ArrayList<>();
        List<Booth> saveBooths = new ArrayList<>();
        for (int i = 0; i < rentOtherDTO.getNumber(); i++) {
            if (markets.size() <= i) {
                break;
            }
            Market market = markets.get(i);
            market.setRentUserId(userId);
            market.setStatus(MarketStatusConstant.UN_USED);
            market.setRentEndTime(Date.from(LocalDateTime.of(LocalDate.now(), LocalTime.MAX).plusDays(market.getDays()).atZone(ZoneId.systemDefault()).toInstant()));
            rentMarkets.add(market);
            Booth booth = boothService.getById(market.getBoothId());
            booth.setUseStatus(UseStatus.RENT_USED);
            saveBooths.add(booth);
        }
        if (rentMarkets.size() > 0) {
            boothService.updateBatchById(saveBooths);
            this.updateBatchById(rentMarkets);
        }
        return null;
    }

    @Override
    public Object getRentRate() {
        return systemConfigService.getById(1L);
    }

    @Resource
    private AreaService areaService;

    @Override
    public void seeKProvince(String provinceCode, Integer days, BigDecimal price, Long userId, Long majorId) {
        List<Area> cities = areaService.getCities(provinceCode);
        SystemConfig systemConfig = systemConfigService.getById(1L);
        for (Area area : cities) {
            this.saveCitySeekRent(area.getCode(), days, price, userId, majorId, systemConfig);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveCitySeekRent(String cityCode, Integer days, BigDecimal price, Long userId, Long majorId, SystemConfig systemConfig) {
        List<Market> markets = new ArrayList<>();
        List<Street> streets = streetService.getCityStreet(cityCode);
        if (streets.size() <= 0) {
            return;
        }
        MarketOrder marketOrder = new MarketOrder();
        marketOrder.setCityCode(cityCode);
        marketOrder.setCreateTime(new Date());
        marketOrder.setPayStatus(PayConstant.SUCCESS_PAY);
        marketOrder.setType(MarketType.RENT_SEEK);
        marketOrder.setExpireDate(Date.from(LocalDate.now().plusDays(systemProperties.getMarketDays())
                .atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        marketOrder.setDays(days);
        marketOrder.setPrice(price);
        marketOrder.setTotalCount(streets.size());
        marketOrder.setSoldCount(0);
        marketOrder.setUserId(userId);
        marketOrder.setStreetCodes(String.join(",", streets.stream().map(Street::getCode).collect(Collectors.toList())));
        marketOrder.setMajorId(majorId);
        marketOrderService.save(marketOrder);
        for (Street street : streets) {
            Market market = new Market();
            market.setOrderId(marketOrder.getId());
            market.setStatus(MarketStatusConstant.WAIT_RENT);
            market.setAreaCode(street.getCode());
            market.setNumber(1);
            market.setPrice(marketOrder.getPrice());
            market.setDays(marketOrder.getDays());
            market.setMarkertEndTime(Date.from(LocalDateTime.now().plusDays(systemProperties.getMarketDays()).atZone(ZoneId.systemDefault()).toInstant()));
            market.setRate(systemConfig.getSeekRentRate());
            market.setType(MarketType.RENT_SEEK);
            market.setUserId(userId);
            market.setOrderId(marketOrder.getId());
            markets.add(market);
        }
        this.saveBatch(markets);
    }

}
