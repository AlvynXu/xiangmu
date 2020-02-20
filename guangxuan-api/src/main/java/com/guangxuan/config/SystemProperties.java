package com.guangxuan.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhuolin
 * @Date 2019/12/14
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "system")
public class SystemProperties {

    private String shareUrl;
    private Boolean debugOff;
    private Boolean smsOff;
    private Boolean auditOff;
    private String filePath;
    private String sharePath;
    private Integer payMinutes;
    private Integer marketDays;
    private String itemShare;
    private Integer maxMarketCount;
    private Boolean notAllowMallItemSameBooth;
    private Boolean showSameStreetBooth;
}
