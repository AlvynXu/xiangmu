package com.guangxuan.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author zhuolin
 * @Date 2019/12/18
 */
@Data
public class RentToOtherDTO implements Serializable {

    @ApiModelProperty(value = "街道编码", required = true)
    @NotEmpty(message = "未获取街道信息")
    private HashSet<String> streetCodes;

    @ApiModelProperty(value = "订单id", required = true)
    @NotNull(message = "未获取订单")
    private Long orderId;


}
