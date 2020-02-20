package com.guangxuan.job;

import com.guangxuan.service.MessageService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author zhuolin
 * @Date 2020/1/10
 */
@Component
public class MessageSchedule {

    @Resource
    private MessageService messageService;

    @Scheduled(cron = "${cron.messageSchedule}")
    @Transactional(rollbackFor = Exception.class)
    public void messageSchedule() {
        LocalDateTime maxTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN).minusMonths(1);
        Date maxDate = Date.from(maxTime.atZone(ZoneId.systemDefault()).toInstant());
        Date minDate = Date.from(maxTime.minusMonths(1).atZone(ZoneId.systemDefault()).toInstant());
        messageService.deleteMessage(minDate, maxDate);
    }
}
