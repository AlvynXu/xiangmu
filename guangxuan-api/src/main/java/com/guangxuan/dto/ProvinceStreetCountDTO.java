package com.guangxuan.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhuolin
 * @Date 2020/1/14
 */
@Data
public class ProvinceStreetCountDTO implements Serializable {

    @ApiModelProperty("省份编码")
    private String provinceCode;

    @ApiModelProperty("省份名称")
    private String provinceName;

    @ApiModelProperty("街道数量")
    private Long streetCount;
}
