package com.guangxuan.job;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guangxuan.constant.MarketStatusConstant;
import com.guangxuan.constant.MessageStatus;
import com.guangxuan.constant.UseStatus;
import com.guangxuan.dto.BoothDetailDTO;
import com.guangxuan.model.Booth;
import com.guangxuan.model.ItemBoot;
import com.guangxuan.model.Market;
import com.guangxuan.model.Message;
import com.guangxuan.service.BoothService;
import com.guangxuan.service.ItemBootService;
import com.guangxuan.service.MarketService;
import com.guangxuan.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 市场租赁到期检测
 *
 * @author zhuolin
 * @Date 2019/12/18
 */
@Component
@Slf4j
public class MarketRentSchedule {

    @Resource
    private MarketService marketService;

    @Resource
    private BoothService boothService;

    @Resource
    private ItemBootService itemBootService;

    @Resource
    private MessageService messageService;

    @Scheduled(cron = "${cron.marketRentSchedule}")
    @Transactional(rollbackFor = Exception.class)
    public void marketRentSchedule() {
        log.info("集市租赁关闭");
        LocalDateTime maxTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
//        LocalDateTime maxTime = LocalDateTime.now();
        Date maxDate = Date.from(maxTime.atZone(ZoneId.systemDefault()).toInstant());
        // 未使用
        List<Market> unUseMarket = marketService.list(new LambdaQueryWrapper<Market>().lt(Market::getRentEndTime, maxDate)
                .eq(Market::getStatus, MarketStatusConstant.UN_USED));
        dealUnUseBooth(unUseMarket);
        // 正在使用
        List<Market> useMarket = marketService.list(new LambdaQueryWrapper<Market>().lt(Market::getRentEndTime, maxDate)
                .eq(Market::getStatus, MarketStatusConstant.USING));

        dealUsingBooth(useMarket);

        Set<Market> markets = new HashSet<>();
        markets.addAll(unUseMarket);
        markets.addAll(useMarket);
        Set<Long> boothIds = markets.stream().map(Market::getBoothId).collect(Collectors.toSet());
        if (boothIds.size() <= 0) {
            return;
        }
        List<BoothDetailDTO> boothDetailDTOS = boothService.getBoothDetail(boothIds);
        Map<Long,BoothDetailDTO> boothDetailDTOMap = boothDetailDTOS.stream().collect(Collectors.toMap(BoothDetailDTO::getId,a->a));
        List<Message> messages = new ArrayList<>();
        for (Market market : markets) {
            if(!boothDetailDTOMap.containsKey(market.getBoothId())){
                continue;
            }
            Message message = Message.builder()
                    .content("您租用的电线竿已到期！" + boothDetailDTOMap.get(market.getBoothId()).getCode()
                            + "号电线竿，租用已到期")
                    .toUserId(market.getUserId())
                    .deleted(false)
                    .sender("租用到期")
                    .status(MessageStatus.UNREAD)
                    .createTime(new Date())
                    .build();
            messages.add(message);
        }
        messageService.saveBatch(messages);
    }

    private void dealUsingBooth(List<Market> useMarket) {
        List<Long> boothIds = useMarket.stream().filter(a -> a.getBoothId() != null)
                .map(Market::getBoothId).collect(Collectors.toList());
        if (boothIds.size() > 0) {
            List<Booth> booths = new ArrayList<>();
            for (Long boothId : boothIds) {
                Booth booth = new Booth();
                booth.setId(boothId);
                booth.setUseStatus(UseStatus.NOT_USE);
                booths.add(booth);
                List<ItemBoot> itemBoots = itemBootService.list(new LambdaQueryWrapper<ItemBoot>().eq(ItemBoot::getBoothId, booth.getId()));
                for (ItemBoot itemBoot : itemBoots) {
                    itemBootService.cancelItemBooth(itemBoot);
                }
            }
            marketService.remove(new LambdaQueryWrapper<Market>().in(Market::getBoothId, boothIds));
            boothService.updateBatchById(booths);
        }
    }

    private void dealUnUseBooth(List<Market> unUseMarket) {
        List<Long> boothIds = unUseMarket.stream().filter(a -> a.getBoothId() != null).map(Market::getBoothId).collect(Collectors.toList());
        if (boothIds.size() > 0) {
            marketService.remove(new LambdaQueryWrapper<Market>().in(Market::getBoothId, boothIds));
            List<Booth> booths = new ArrayList<>();
            for (Long boothId : boothIds) {
                Booth booth = new Booth();
                booth.setId(boothId);
                booth.setUseStatus(UseStatus.NOT_USE);
                booths.add(booth);
            }
            boothService.updateBatchById(booths);
        }
    }


}
