package com.guangxuan.job;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.guangxuan.model.HeadlineItem;
import com.guangxuan.model.MallItemInfo;
import com.guangxuan.service.HeadlineItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * @author zhuolin
 * @Date 2019/12/16
 */
@Component
@Slf4j
public class HeadlinesSchedule {

    @Resource
    private HeadlineItemService headlineItemService;

    @Scheduled(cron = "${cron.headlinesSchedule}")
    public void headlinesSchedule() {
        LocalDateTime maxTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
//        LocalDateTime maxTime = LocalDateTime.now();
        Date maxDate = Date.from(maxTime.atZone(ZoneId.systemDefault()).toInstant());
        List<HeadlineItem> headlineItems = headlineItemService.list(new LambdaQueryWrapper<HeadlineItem>().lt(HeadlineItem::getEndTime, maxDate));
        for (HeadlineItem headlineItem : headlineItems) {
            headlineItem.setStatus(0);
            headlineItem.setFee(BigDecimal.ZERO);
        }
        if (headlineItems.size() > 0) {
            log.info("头条到期，{}", JSON.toJSONString(headlineItems));
            headlineItemService.updateBatchById(headlineItems);
        }
    }
}
