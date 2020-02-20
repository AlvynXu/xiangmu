package com.guangxuan.dto.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
@ApiModel("头条展示")
public class HeadlineDTO implements Serializable {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("关联项目id")
    private Long itemId;

    @ApiModelProperty("地区编码")
    private String areaCode;

    @ApiModelProperty("地区名称")
    private String areaName;

    @ApiModelProperty("剩余天数")
    private Long leftDays;

    @ApiModelProperty("总天数")
    private Long totalDays;

    @ApiModelProperty("权重")
    private Integer sort;

    @ApiModelProperty("单价")
    private BigDecimal price;

    @ApiModelProperty("总价")
    private BigDecimal totalFee;

    @ApiModelProperty("状态 0禁用 1启用")
    private Integer status;

    @ApiModelProperty("区域编码")
    private List<String> areaCodes;

    private String description;

    private Date endTime;


}
