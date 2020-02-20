package com.guangxuan.dto.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author zhuolin
 * @Date 2019/12/20
 */
@Data
public class ProductPriceDTO implements Serializable {

    @NotNull(message = "vip价格不能为空")
    @ApiModelProperty(value = "vip价格",required = true)
    private BigDecimal vipPrice;

    @NotNull(message = "展位价格不能为空")
    @ApiModelProperty(value = "展位价格",required = true)
    private BigDecimal boothPrice;

    @NotNull(message = "街道价格不能为空")
    @ApiModelProperty(value = "街道价格",required = true)
    private BigDecimal streetPrice;

    @NotNull(message = "首页banner价格不能为空")
    @ApiModelProperty(value = "首页banner",required = true)
    private BigDecimal bannerPrice;

    @NotNull(message = "市级banner价格不能为空")
    @ApiModelProperty(value = "市级banner",required = true)
    private BigDecimal cityBannerPrice;
}
