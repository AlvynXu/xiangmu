package com.guangxuan.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

/**
 * @author zhuolin
 * @Date 2019/12/16
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "recharge")
public class RechargeProperties {
    /**
     * 最小提现金额
     */
    private BigDecimal min;
    /**
     * 最大提现金额
     */
    private BigDecimal max;
    /**
     * 手续费 小数
     */
    private BigDecimal rate;
}
