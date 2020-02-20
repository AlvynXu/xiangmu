package com.guangxuan.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * @author zhuolin
 * @Date 2020/1/11
 */
@Data
public class SeekRentProvinceDTO implements Serializable {

    @ApiModelProperty(value = "街道编码", required = true)
    @NotEmpty(message = "地区编码不能为空")
    private ArrayList<String> areaCodes;

    @ApiModelProperty(value = "价格", required = true)
    @NotNull(message = "未获取价格")
    @Max(value = 10000, message = "求租金额最高位10000")
    @Min(value = 0, message = "求租金额不应小于0元")
    private BigDecimal price;

    @ApiModelProperty(value = "出租天数", required = true)
    @NotNull(message = "天数不能为空")
    @Min(value = 1, message = "求租天数至少为一天")
    @Max(value = 365, message = "求租天数至多为365天")
    private Integer days;
}
