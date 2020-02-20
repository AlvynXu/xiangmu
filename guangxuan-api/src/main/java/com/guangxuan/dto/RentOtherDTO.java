package com.guangxuan.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author zhuolin
 * @Date 2019/12/18
 */
@Data
public class RentOtherDTO implements Serializable {


    @NotNull(message = "租用数量")
    @Min(value = 1, message = "至少自用一个")
    private Integer number;

    @NotBlank(message = "地区编码不能为空")
    private String areaCode;

    @NotNull(message = "地区类型不能为空")
    private Integer areaType;

    @NotNull(message = "价格不能为空")
    private BigDecimal price;

    @NotNull(message = "天数不能为空")
    private Integer days;
}
