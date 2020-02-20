package com.guangxuan.job;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guangxuan.constant.BoothStatus;
import com.guangxuan.constant.MessageStatus;
import com.guangxuan.constant.PayConstant;
import com.guangxuan.constant.PayResourceConstant;
import com.guangxuan.dto.BoothDetailDTO;
import com.guangxuan.dto.domain.BoothDTO;
import com.guangxuan.model.BoothUsers;
import com.guangxuan.model.Message;
import com.guangxuan.service.BoothService;
import com.guangxuan.service.BoothUsersService;
import com.guangxuan.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.time.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zhuolin
 * @Date 2019/12/5
 */
@Component
@Slf4j
public class BoothTimeSchedule {

    @Resource
    BoothUsersService boothUsersService;

    @Resource
    BoothService boothService;



    @Scheduled(cron = "${cron.boothTimeSchedule}")
    @Transactional(rollbackFor = Exception.class)
    public void boothTime() {
        LocalDateTime maxTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
//        LocalDateTime maxTime = LocalDateTime.now();
        Date maxDate = Date.from(maxTime.atZone(ZoneId.systemDefault()).toInstant());
        List<BoothUsers> boothUsers = boothUsersService.list(new LambdaQueryWrapper<BoothUsers>().lt(BoothUsers::getExpireTime, maxDate));
        Set<Long> boothIds = boothUsers.stream().map(BoothUsers::getBoothId).collect(Collectors.toSet());
        if (boothIds.size() > 0) {
            boothService.expire(boothIds);
            log.info("展位过期{}", JSON.toJSONString(boothIds));
        }
        boothUsersService.remove(new LambdaQueryWrapper<BoothUsers>().lt(BoothUsers::getExpireTime, maxDate));
    }



}
