package com.guangxuan.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author zhuolin
 * @Date 2019/12/16
 */
@Data
public class MallItemInfoDetailDTO implements Serializable {

    private Long id;

    @NotNull(message = "项目id")
    private Long itemId;

    @ApiModelProperty(value = "描述")
    private String description;

    @NotNull(message = "序号不能为空")
    @ApiModelProperty(value = "序号")
    private Integer seq;

    @NotNull(message = "类型不能为空")
    @ApiModelProperty(value = "类型 1字符串 2图片 3视频")
    private Integer type;

}
