package com.guangxuan.vo.mall;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author deofly
 * @since 2019-06-06
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SystemPreferencesVO implements Serializable {

    private static final long serialVersionUID = 930994273604497796L;

    @Data
    @AllArgsConstructor
    public static class VipInfo implements Serializable {

        private static final long serialVersionUID = -4972802124295589344L;

        private Integer level;

        private BigDecimal threshold;

        private BigDecimal directRate;

        private BigDecimal globalRate;
    }

    /**
     * 不同等级会员的充值门槛
     */
    private Map<Integer, BigDecimal> memberLevelThresholds;

    private BigDecimal villagePrice;
    private BigDecimal streetPrice;
    private BigDecimal cityPrice;

    private List<VipInfo> vips;
}
