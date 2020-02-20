package com.guangxuan.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beust.jcommander.internal.Lists;
import com.guangxuan.config.SystemProperties;
import com.guangxuan.constant.*;
import com.guangxuan.dto.ItemCanUseBoothDTO;
import com.guangxuan.enumration.BusinessFailEnum;
import com.guangxuan.exception.BusinessException;
import com.guangxuan.mapper.BoothMapper;
import com.guangxuan.model.*;
import com.guangxuan.mapper.ItemBootMapper;
import com.guangxuan.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Duration;
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
 * @since 2019-11-28
 */
@Service
public class ItemBootServiceImpl extends ServiceImpl<ItemBootMapper, ItemBoot> implements ItemBootService {

    @Resource
    private MallItemInfoService mallItemInfoService;

    @Resource
    private ItemBootService itemBootService;

    @Resource
    private BoothUsersService boothUsersService;

    @Resource
    private MarketService marketService;

    @Resource
    private BoothService boothService;

    @Resource
    private SystemProperties systemProperties;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object saveItemBooth(ItemBoot itemBoot) {
        Integer count = this.count(new LambdaQueryWrapper<ItemBoot>().in(ItemBoot::getBoothId, itemBoot.getBoothIds())
                .eq(ItemBoot::getEnable, true));
        if (count > 0) {
            throw new BusinessException(BusinessFailEnum.BOOTH_IN_USER);
        }
        MallItemInfo mallItemInfo = mallItemInfoService.getById(itemBoot.getItemId());
        if (mallItemInfo.getStatus() == ItemStatus.AUDIT_FAIL || mallItemInfo.getStatus() == ItemStatus.NOT_AUDIT) {
            throw new BusinessException(BusinessFailEnum.NOT_WAIT_PUBLISH);
        }
        Set<Long> boothIds = new HashSet<>();
        boothIds.addAll(itemBoot.getBoothIds());
        if (boothIds.size() != itemBoot.getBoothIds().size()) {
            throw new BusinessException(BusinessFailEnum.BOOTH_REPEAT);
        }
        List<Long> saveStreetIds = new ArrayList<>();
        if (systemProperties.getNotAllowMallItemSameBooth()) {
            List<ItemBoot> itemBoots = this.list(new LambdaQueryWrapper<ItemBoot>().eq(ItemBoot::getItemId, itemBoot.getItemId())
                    .eq(ItemBoot::getEnable, true));
            if (itemBoots.size() > 0) {
                Set<Long> itemBoothIds = itemBoots.stream().map(ItemBoot::getBoothId).collect(Collectors.toSet());
                Collection<Booth> saveBooths = boothService.listByIds(itemBoothIds);
                saveStreetIds = saveBooths.stream().map(Booth::getStreetId).collect(Collectors.toList());
            }
        }
        mallItemInfo.setStatus(ItemStatus.ON_SALE);
        mallItemInfoService.updateById(mallItemInfo);
        List<ItemBoot> itemBoots = new ArrayList<>();
        for (Long boothId : itemBoot.getBoothIds()) {
            Booth booth = boothService.getById(boothId);
            if (systemProperties.getNotAllowMallItemSameBooth()) {
                if (saveStreetIds.contains(booth.getStreetId())) {
                    throw new BusinessException(BusinessFailEnum.MALLITEM_IN_SAME_STREET);
                }
                saveStreetIds.add(booth.getStreetId());
            }
            if (booth.getUseStatus().equals(UseStatus.NOT_USE)) {
                booth.setUseStatus(UseStatus.BOOTH_USE);
                boothService.updateById(booth);
            } else if (booth.getUseStatus().equals(UseStatus.RENT_USED)) {
                Market market = marketService.getOne(new LambdaQueryWrapper<Market>()
                        .eq(Market::getBoothId, boothId)
                        .eq(Market::getStatus, MarketStatusConstant.UN_USED));
                if (market == null) {
                    throw new BusinessException(BusinessFailEnum.BOOTH_IN_USER);
                }
                market.setStatus(MarketStatusConstant.USING);
                marketService.updateById(market);
            } else {
                throw new BusinessException(BusinessFailEnum.BOOTH_IN_USER);
            }
            ItemBoot itemBoot1 = new ItemBoot();
            itemBoot1.setEnable(true);
            itemBoot1.setBoothId(boothId);
            itemBoot1.setItemId(itemBoot.getItemId());
            itemBoots.add(itemBoot1);
        }
        this.saveBatch(itemBoots);
        return itemBoot;
    }

    @Override
    public Object cancelItemBooth(ItemBoot itemBoot) {
        ItemBoot saveItemBoot = this.getOne(new LambdaQueryWrapper<ItemBoot>()
                .eq(ItemBoot::getBoothId, itemBoot.getBoothId())
                .eq(ItemBoot::getItemId, itemBoot.getItemId()));
        if (saveItemBoot == null) {
            throw new BusinessException(BusinessFailEnum.NOT_GET_DATA);
        }
        this.removeById(saveItemBoot.getId());
        Booth booth = boothService.getById(saveItemBoot.getBoothId());
        if (booth != null) {
            booth.setUseStatus(UseStatus.NOT_USE);
            boothService.updateById(booth);
        }
        Market market = marketService.getOne(new LambdaQueryWrapper<Market>()
                .eq(Market::getBoothId, booth.getId())
                .eq(Market::getStatus, MarketStatusConstant.USING));
        if (market != null) {
            market.setStatus(MarketStatusConstant.UN_USED);
            marketService.updateById(market);
        }
        if (booth == null && market == null) {
            log.error(String.format("解除项目绑定有问题%s", JSON.toJSONString(itemBoot)));
            throw new BusinessException(BusinessFailEnum.OFF_SHELVE_FAIL);
        }
        Integer count = this.count(new LambdaQueryWrapper<ItemBoot>()
                .eq(ItemBoot::getItemId, itemBoot.getItemId()));
        if (count == 0) {
            MallItemInfo mallItemInfo = mallItemInfoService.getById(itemBoot.getItemId());
            mallItemInfo.setStatus(ItemStatus.OFF_SHELVE);
            mallItemInfoService.updateById(mallItemInfo);
        }
        return saveItemBoot;
    }

    @Override
    public Object getCanUseBooth(Long userId, Integer page, Integer size, Long itemId) {
        List<BoothUsers> boothUsers = boothUsersService.list(new LambdaQueryWrapper<BoothUsers>()
                .eq(BoothUsers::getUserId, userId)
                .eq(BoothUsers::getStatus, PayConstant.SUCCESS_PAY));
        List<Long> boothUsersboothIds = boothUsers.stream().map(BoothUsers::getBoothId).collect(Collectors.toList());
        List<Booth> canUseBooths = new ArrayList<>();
        if (boothUsersboothIds.size() > 0) {
            canUseBooths = boothService.list(new LambdaQueryWrapper<Booth>().eq(Booth::getUseStatus, UseStatus.NOT_USE)
                    .in(Booth::getId, boothUsersboothIds));
        }
        List<Market> rentMarkets = marketService.list(new LambdaQueryWrapper<Market>().eq(Market::getUserId, userId)
                .eq(Market::getStatus, MarketStatusConstant.UN_USED));

        List<Long> boothIds = canUseBooths.stream().map(Booth::getId).collect(Collectors.toList());
        boothIds.addAll(rentMarkets.stream().map(Market::getBoothId).collect(Collectors.toList()));

        Map<Long, Market> map = rentMarkets.stream().collect(Collectors.toMap(Market::getBoothId, a -> a));
        IPage<ItemCanUseBoothDTO> booths = new Page<>();

        if (boothIds.size() > 0) {
            List<Long> saveStreetIds = new ArrayList<>();
            if (systemProperties.getNotAllowMallItemSameBooth() && itemId != null) {
                List<ItemBoot> itemBoots = this.list(new LambdaQueryWrapper<ItemBoot>().eq(ItemBoot::getItemId, itemId)
                        .eq(ItemBoot::getEnable, true));
                if (itemBoots.size() > 0) {
                    Set<Long> itemBoothIds = itemBoots.stream().map(ItemBoot::getBoothId).collect(Collectors.toSet());
                    Collection<Booth> saveBooths = boothService.listByIds(itemBoothIds);
                    saveStreetIds = saveBooths.stream().map(Booth::getStreetId).collect(Collectors.toList());
                }
            }
            booths = boothMapper.getItemCanUsePage(new Page<>(page, size), boothIds,
                    Lists.newArrayList(UseStatus.RENT_USED, UseStatus.NOT_USE),
                    saveStreetIds,systemProperties.getShowSameStreetBooth());
            for (ItemCanUseBoothDTO booth : booths.getRecords()) {
                if (map.containsKey(booth.getId())) {
                    Market market = map.get(booth.getId());
                    LocalDateTime localDateTime = LocalDateTime.of(LocalDateTime.ofInstant(market.getRentEndTime().toInstant(),
                            ZoneId.systemDefault()).toLocalDate(), LocalTime.MAX);
                    LocalDateTime end = LocalDateTime.now();
                    Duration duration = Duration.between(end, localDateTime);
                    booth.setLeftDate(duration.toDays());
                } else {
                    LocalDateTime localDateTime = LocalDateTime.of(LocalDateTime.ofInstant(booth.getExpireEndTime().toInstant(),
                            ZoneId.systemDefault()).toLocalDate(), LocalTime.MAX);
                    LocalDateTime end = LocalDateTime.now();
                    Duration duration = Duration.between(end, localDateTime);
                    booth.setLeftDate(duration.toDays());
                }
            }
        }
        return booths;
    }

    @Resource
    private BoothMapper boothMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object removeItem(MallItemInfo itemInfo) {
        MallItemInfo mallItemInfo = mallItemInfoService.getById(itemInfo.getId());
        if (mallItemInfo == null) {
            throw new BusinessException(BusinessFailEnum.NOT_GET_DATA);
        }
        List<ItemBoot> itemBoots = itemBootService.list(new LambdaQueryWrapper<ItemBoot>().eq(ItemBoot::getItemId, itemInfo.getId()));
        this.remove(new LambdaQueryWrapper<ItemBoot>().eq(ItemBoot::getItemId, itemInfo.getId()));
        List<Long> boothIds = itemBoots.stream().map(ItemBoot::getBoothId).collect(Collectors.toList());
        if (boothIds.size() > 0) {
            Collection<Booth> booths = boothService.listByIds(boothIds);
            List<Market> markets = new ArrayList<>();
            for (Booth booth : booths) {
                if (booth.getUseStatus().equals(UseStatus.RENT_USED)) {
                    Market market = marketService.getOne(new LambdaQueryWrapper<Market>()
                            .eq(Market::getBoothId, booth.getId()));
                    market.setStatus(MarketStatusConstant.UN_USED);
                    markets.add(market);
                } else {
                    booth.setUseStatus(UseStatus.NOT_USE);
                }
            }
            if (markets.size() > 0) {
                marketService.updateBatchById(markets);
            }
            boothService.updateBatchById(booths);
        }
        mallItemInfo.setStatus(ItemStatus.OFF_SHELVE);
        mallItemInfoService.updateById(mallItemInfo);
        return null;
    }
}
