package com.guangxuan.dto.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhuolin
 * @Date 2019/12/19
 */
@Data
public class CategorySaveDTO implements Serializable {

    @ApiModelProperty(required = true)
    private String name;

    @ApiModelProperty(required = true)
    private Integer seq;
}
