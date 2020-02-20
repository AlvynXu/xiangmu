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
public class HeadlinesPriceDTO implements Serializable {

    @ApiModelProperty(value = "id",required = true)
    @NotNull(message = "未获取数据")
    private Long id;

    @ApiModelProperty(value = "权重",required = true)
    @NotNull(message = "权重不能为空")
    private Integer sort;

    @ApiModelProperty(value = "单价",required = true)
    @NotNull(message = "单价不能为空")
    private BigDecimal price;
    /**
     * 0 首页 1地区
     */
    @ApiModelProperty(value = "类型",required = true)
    @NotNull(message = "类型不能为空")
    private Integer type;
}
