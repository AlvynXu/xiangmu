package com.guangxuan.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhuolion
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "sms")
public class SmsConfig {

    private String regionId;
    private String accessKeyId;
    private String secret;
    private String domain;
    private String version;
    private String action;
    private String signName;

}
