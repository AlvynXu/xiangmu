package com.guangxuan.dto.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author zhuolin
 * @Date 2019/12/20
 */
@Data
public class HeadlineSaveDTO implements Serializable {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty(value = "关联项目id",required = true)
    @NotNull(message = "未获取关联项目")
    private Long itemId;

    @ApiModelProperty(value = "地区编码 -1首页 其余为各市编码",required = true)
    @NotEmpty(message = "未获取区域")
    private List<String> areaCodes;

    @ApiModelProperty(value = "总天数",required = true)
    @NotNull(message = "未获取天数")
    private Integer totalDays;

    @ApiModelProperty(value = "权重",required = true)
    @NotNull(message = "未获取权重")
    private Integer sort;

    @ApiModelProperty(value = "状态 0禁用 1启用",required = true)
    @NotNull(message = "未获取状态")
    private Integer status;

}
