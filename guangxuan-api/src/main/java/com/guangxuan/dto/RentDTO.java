package com.guangxuan.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * @author zhuolin
 * @Date 2019/12/17
 */
@Data
@ApiModel("出租model")
public class RentDTO implements Serializable {

    @ApiModelProperty("展位id")
    @NotEmpty(message = "未获取出租展位")
    private ArrayList<Long> boothIds;

    @ApiModelProperty("价格")
    @NotNull(message = "未获取价格")
    private BigDecimal price;

    @ApiModelProperty("出租天数")
    @NotNull(message = "天数不能为空")
    @Min(value = 1,message = "至少出租一天")
    private Integer days;

}
