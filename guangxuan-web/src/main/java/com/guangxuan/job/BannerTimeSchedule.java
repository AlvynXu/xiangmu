package com.guangxuan.job;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guangxuan.model.BannerArea;
import com.guangxuan.model.HeadlineItem;
import com.guangxuan.model.MallBanner;
import com.guangxuan.service.BannerAreaService;
import com.guangxuan.service.MallBannerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * @author zhuolin
 * @Date 2019/12/17
 */
@Component
@Slf4j
public class BannerTimeSchedule {

    @Resource
    private MallBannerService mallBannerService;

    @Resource
    private BannerAreaService bannerAreaService;

    @Scheduled(cron = "${cron.bannerTimeSchedule}")
    public void headlinesSchedule() {
        LocalDateTime maxTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
//        LocalDateTime maxTime = LocalDateTime.now();
        Date maxDate = Date.from(maxTime.atZone(ZoneId.systemDefault()).toInstant());
        List<MallBanner> mallBanners = mallBannerService.list(
                new LambdaQueryWrapper<MallBanner>().lt(MallBanner::getEndTime, maxDate));
        for (MallBanner banner : mallBanners) {
            banner.setVisible(false);
        }
        if (mallBanners.size() > 0) {
            mallBannerService.updateBatchById(mallBanners);
        }
    }


}
