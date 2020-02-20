package com.guangxuan.dto.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zhuolin
 * @Date 2019/12/18
 */
@Data
@ApiModel("用户信息")
public class UserDTO implements Serializable {

    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("注册日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date registerDate;
    @ApiModelProperty("邀请码")
    private String regCode;
    @ApiModelProperty("用户等级")
    private Integer level;
    @ApiModelProperty("展位数量")
    private Integer boothCount;
    @ApiModelProperty("街道数量")
    private Integer streetCount;
    @ApiModelProperty("项目数量")
    private Integer itemCount;
    @ApiModelProperty("直推")
    private Integer direct;
    @ApiModelProperty("全网")
    private Integer indirect;
    @ApiModelProperty("余额")
    private BigDecimal balance;
    @ApiModelProperty("状态")
    private Boolean enabled;

    private Boolean deleted;
}
