//package com.guangxuan.job;
//
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.guangxuan.constant.*;
//import com.guangxuan.dto.BoothDetailDTO;
//import com.guangxuan.model.*;
//import com.guangxuan.service.*;
//import lombok.extern.slf4j.Slf4j;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.math.BigDecimal;
//import java.text.DecimalFormat;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.time.ZoneId;
//import java.util.*;
//import java.util.stream.Collectors;
//
///**
// * 返还用户租金
// *
// * @author zhuolin
// * @Date 2019/12/18
// */
//@Component
//@Slf4j
//public class MarketMoneySchedule {
//
//    private Logger logger = LoggerFactory.getLogger("payLogger");
//
//    @Resource
//    private MarketService marketService;
//
//    @Resource
//    private SystemConfigService systemConfigService;
//
//    @Resource
//    private BoothUsersService boothUsersService;
//
//    @Resource
//    private UsersService usersService;
//
//    @Resource
//    private PayLogService payLogService;
//
//    @Resource
//    private MessageService messageService;
//
//    @Resource
//    private BoothService boothService;
//
//
//    @Scheduled(cron = "${cron.marketMoneySchedule}")
//    public void marketMoneySchedule() {
//        log.info("给用户租金");
//        LocalDateTime maxTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
//        Date maxDate = Date.from(maxTime.atZone(ZoneId.systemDefault()).toInstant());
//        List<Market> markets = marketService.list(new LambdaQueryWrapper<Market>()
//                .ge(Market::getRentEndTime, maxDate).in(Market::getStatus, MarketStatusConstant.UN_USED, MarketStatusConstant.USING));
//        Set<Long> boothIds = markets.stream().map(Market::getBoothId).collect(Collectors.toSet());
//        Map<Long, Market> map = markets.stream().collect(Collectors.toMap(Market::getBoothId, a -> a, (a, b) -> a));
//        Map<Long, Users> usersMap = new HashMap<>();
//        List<PayLog> payLogs = new ArrayList<>();
//        SystemConfig systemConfig = systemConfigService.getById(1L);
//        if (boothIds.size() > 0) {
//            List<BoothDetailDTO> boothDetails = boothService.getBoothDetail(boothIds);
//            List<Message> messages = new ArrayList<>();
////            List<BoothUsers> boothUsers = boothUsersService.list(new LambdaQueryWrapper<BoothUsers>().in(BoothUsers::getBoothId, boothIds));
//            for (BoothDetailDTO boothDetailDTO : boothDetails) {
//                Market market = map.get(boothDetailDTO.getId());
//                Users user;
//                if (usersMap.containsKey(boothDetailDTO.getUserId())) {
//                    user = usersMap.get(boothDetailDTO.getUserId());
//                } else {
//                    user = usersService.getById(boothDetailDTO.getUserId());
//                }
//                BigDecimal fee = market.getPrice().multiply(systemConfig.getRentRate());
//                BigDecimal money = market.getPrice().subtract(fee);
//                BigDecimal finalMoney = new BigDecimal(String.format("%.2f", money));
//                user.setBalance(user.getBalance().add(finalMoney));
//                user.setProfit(user.getProfit().add(finalMoney));
//                PayLog payLog = PayLog.builder()
//                        .realAmount(market.getPrice()).fee(fee)
//                        .amount(finalMoney).userId(user.getId())
//                        .extend1("PAY_RENT" + UUID.randomUUID().toString()).createTime(new Date()).status(PayConstant.SUCCESS_PAY)
//                        .goodsId(user.getId()).payType(PayTypeConstant.BALANCE_PAY)
//                        .source(PayResourceConstant.PAY_RENT.getType()).build();
//                Message message = Message.builder()
//                        .content("您的租金已到账，请注意查收！\n" + boothDetailDTO.getCode()
//                                + "号电线杆，租金：" + finalMoney + "元")
//                        .toUserId(boothDetailDTO.getUserId())
//                        .deleted(false)
//                        .sender("租金到账")
//                        .status(MessageStatus.UNREAD)
//                        .createTime(new Date())
//                        .build();
//                messages.add(message);
//                logger.info("用户id{}获取租金{}手续费{}", user.getId(), money, 0);
//                payLogs.add(payLog);
//                usersMap.put(boothDetailDTO.getUserId(), user);
//            }
//            messageService.saveBatch(messages);
//            if (usersMap.size() > 0) {
//                List<Users> users = new ArrayList<>();
//                for (Long userId : usersMap.keySet()) {
//                    users.add(usersMap.get(userId));
//                }
//                usersService.updateBatchById(users);
//                payLogService.saveBatch(payLogs);
//            }
//        }
//    }
//}
