package com.guangxuan.job;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guangxuan.constant.MessageStatus;
import com.guangxuan.constant.PayConstant;
import com.guangxuan.dto.BoothDetailDTO;
import com.guangxuan.model.BoothUsers;
import com.guangxuan.model.Message;
import com.guangxuan.service.BoothService;
import com.guangxuan.service.BoothUsersService;
import com.guangxuan.service.MessageService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zhuolin
 * @Date 2020/1/3
 */
@Component
public class BoothWarnSchedule {

    @Resource
    BoothUsersService boothUsersService;

    @Resource
    BoothService boothService;

    @Resource
    private MessageService messageService;

    @Scheduled(cron = "${cron.boothWarnSchedule}")
    @Transactional(rollbackFor = Exception.class)
    public void boothWarnTime() {
        Date maxDate = Date.from(LocalDateTime.of(LocalDate.now().plusDays(10), LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());
        List<BoothUsers> boothUsers = boothUsersService.list(new LambdaQueryWrapper<BoothUsers>()
                .lt(BoothUsers::getExpireTime, maxDate));
        if (boothUsers.size() <= 0) {
            return;
        }
        Set<Long> boothIds = boothUsers.stream().filter(a -> a.getStatus().equals(PayConstant.SUCCESS_PAY))
                .map(BoothUsers::getBoothId).collect(Collectors.toSet());
        if (boothIds.size() <= 0) {
            return;
        }
        List<BoothDetailDTO> boothDetails = boothService.getBoothDetail(boothIds);
        List<Message> messages = new ArrayList<>();
        for (BoothDetailDTO boothDetailDTO : boothDetails) {
            LocalDateTime expireTime = LocalDateTime.of(LocalDateTime.ofInstant(boothDetailDTO.getExpireTime().toInstant(),
                    ZoneId.systemDefault()).toLocalDate(), LocalTime.MAX);
            LocalDateTime end = LocalDateTime.now();
            Duration duration = Duration.between(end, expireTime);
            Message message = Message.builder()
                    .content("您的电线竿快到期啦!\n" +  boothDetailDTO.getCode()
                            + "号电线竿，期限剩余：" + duration.toDays() + "天 ")
                    .toUserId(boothDetailDTO.getUserId())
                    .deleted(false)
                    .sender("到期提醒")
                    .status(MessageStatus.UNREAD)
                    .createTime(new Date())
                    .build();
            messages.add(message);
        }
        messageService.saveBatch(messages);
    }
}
