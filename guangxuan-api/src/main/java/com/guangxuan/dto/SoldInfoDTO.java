package com.guangxuan.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhuolin
 * @Date 2019/12/4
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("我的展位")
public class SoldInfoDTO implements Serializable {
    private Long id;

    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("编码")
    private String code;
    @ApiModelProperty("剩余天数")
    private Long days;
    @ApiModelProperty("剩余时间")
    private Long rentHours;
    @ApiModelProperty("城市")
    private String city;
    @ApiModelProperty("区县")
    private String district;
    @ApiModelProperty("街道")
    private String street;
    @ApiModelProperty("街道编码")
    private String streetCode;

    private Date rentEndDate;

    private Date expireDate;
    @ApiModelProperty("项目id")
    private Long itemId;
    @ApiModelProperty("项目展示")
    private String description;
    @ApiModelProperty("使用状态  1闲置 2待租  4 已租<别人租自己的用> 5租用<自己租别人的用> 6挂展")
    private Integer useStatus;

}
