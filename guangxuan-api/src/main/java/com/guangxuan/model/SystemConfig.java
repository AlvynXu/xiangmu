package com.guangxuan.model;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhuolin
 * @since 2019-12-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SystemConfig对象", description="")
public class SystemConfig extends Model<SystemConfig> {

    private static final long serialVersionUID=1L;

    private Integer id;

    @ApiModelProperty(value = "提现最大金额",required = true)
    private BigDecimal rechargeMax;

    @ApiModelProperty(value = "提现最小金额",required = true)
    private BigDecimal rechargeMin;

    @ApiModelProperty(value = "提现费率",required = true)
    @NotNull(message = "提现费率不能为空")
    private BigDecimal rechargeRate;

    @ApiModelProperty(value = "出租费率",required = true)
    @NotNull(message = "出租费率不能为空")
    private BigDecimal rentRate;

    @ApiModelProperty(value = "求租费率")
    private BigDecimal seekRentRate;

    private BigDecimal cityBannerPrice;

    private BigDecimal bannerPrice;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
