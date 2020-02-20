package com.guangxuan.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhuolin
 * @Date 2019/11/25
 */
@Data
@Builder
public class SoldCountDTO implements Serializable {

    @ApiModelProperty("总数量")
    private Integer  totalCount;

    @ApiModelProperty("已售数量")
    private Integer soldCount;
}
