package com.guangxuan.job;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guangxuan.constant.*;
import com.guangxuan.model.*;
import com.guangxuan.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 集市到期
 *
 * @author zhuolin
 * @Date 2019/12/18
 */
@Component
@Slf4j
public class MarketSchedule {

    @Resource
    private MarketService marketService;

    @Resource
    private MarketOrderService marketOrderService;

    @Resource
    private BoothService boothService;

    @Resource
    private UsersService usersService;

    @Resource
    private PayLogService payLogService;

    @Resource
    private MessageService messageService;

    @Scheduled(cron = "${cron.marketSchedule}")
    @Transactional(rollbackFor = Exception.class)
    public void marketSchedule() {
        log.info("集市关闭");
        LocalDateTime maxTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        Date maxDate = Date.from(maxTime.atZone(ZoneId.systemDefault()).toInstant());
        List<MarketOrder> marketOrders = marketOrderService.list(new LambdaQueryWrapper<MarketOrder>().lt(MarketOrder::getExpireDate, maxDate));
        log.info("关闭订单{}", JSON.toJSONString(marketOrders));
        List<Long> marketOrderIds = marketOrders.stream().map(MarketOrder::getId).collect(Collectors.toList());
        Map<Long, Users> userMap = new HashMap<>();
        List<PayLog> payLogs = new ArrayList<>();
        List<Long> boothIds = new ArrayList<>();
        Map<Long, BigDecimal> majorMarketMap = new HashMap<>();
        for (MarketOrder marketOrder : marketOrders) {
            if (!marketOrder.getPayStatus().equals(PayConstant.SUCCESS_PAY)) {
                continue;
            }
            Users user;
            if (userMap.containsKey(marketOrder.getUserId())) {
                user = userMap.get(marketOrder.getUserId());
            } else {
                user = usersService.getById(marketOrder.getUserId());
            }
            List<Market> markets = marketService.list(new LambdaQueryWrapper<Market>()
                    .eq(Market::getStatus, MarketStatusConstant.WAIT_RENT).eq(Market::getOrderId, marketOrder.getId()));
            if (markets.size() > 0) {
                boothIds.addAll(markets.stream().filter(a -> a.getBoothId() != null).map(Market::getBoothId).collect(Collectors.toList()));
                BigDecimal money = marketOrder.getPrice().multiply(new BigDecimal(marketOrder.getDays()))
                        .multiply(new BigDecimal(markets.size())).multiply(new BigDecimal(1).add(markets.get(0).getRate()));
                BigDecimal allMoney = new BigDecimal(String.format("%.2f", money));
                user.setBalance(user.getBalance().add(allMoney));
                user.setProfit(user.getProfit().add(allMoney));
                userMap.put(user.getId(), user);
                if (marketOrder.getMajorId() == null) {
                    PayLog payLog = PayLog.builder()
                            .realAmount(allMoney).fee(BigDecimal.ZERO)
                            .amount(allMoney).userId(user.getId())
                            .extend1("RENTBACK" + UUID.randomUUID().toString())
                            .createTime(new Date()).status(PayConstant.SUCCESS_PAY)
                            .goodsId(user.getId()).payType(PayTypeConstant.BALANCE_PAY)
                            .source(PayResourceConstant.RENT_BACK.getType())
                            .build();
                    payLogs.add(payLog);
                } else {
                    if (!majorMarketMap.containsKey(user.getId())) {
                        majorMarketMap.put(user.getId(), new BigDecimal(0));
                    }
                    majorMarketMap.put(user.getId(), majorMarketMap.get(user.getId()).add(allMoney));
                }

            }
        }
        for (Long userId : majorMarketMap.keySet()) {
            PayLog payLog = PayLog.builder()
                    .realAmount(majorMarketMap.get(userId)).fee(BigDecimal.ZERO)
                    .amount(majorMarketMap.get(userId)).userId(userId)
                    .extend1("RENTBACK" + UUID.randomUUID().toString())
                    .createTime(new Date()).status(PayConstant.SUCCESS_PAY)
                    .goodsId(userId).payType(PayTypeConstant.BALANCE_PAY)
                    .source(PayResourceConstant.RENT_BACK.getType())
                    .build();
            payLogs.add(payLog);
        }
        if (marketOrderIds.size() > 0) {
            marketOrderService.removeByIds(marketOrderIds);
            marketService.remove(new LambdaQueryWrapper<Market>().eq(Market::getStatus, MarketStatusConstant.WAIT_RENT).in(Market::getOrderId, marketOrderIds));
        }
        if (payLogs.size() > 0) {
            payLogService.saveBatch(payLogs);
            List<Users> users = new ArrayList<>();
            for (Long userId : userMap.keySet()) {
                users.add(userMap.get(userId));
                Message message = Message.builder()
                        .content("您发布的求租下架了，如有需要，可再次发布哦!")
                        .toUserId(userId)
                        .deleted(false)
                        .sender("求租提示")
                        .status(MessageStatus.UNREAD)
                        .createTime(new Date())
                        .build();
                messageService.createMessage(message);
            }
            usersService.updateBatchById(users);
        }
        if (boothIds.size() > 0) {
            Collection<Booth> booths = boothService.listByIds(boothIds);
            for (Booth booth : booths) {
                booth.setUseStatus(UseStatus.NOT_USE);
                booth.setSaved(false);
            }
            boothService.updateBatchById(booths);
        }
    }
}
