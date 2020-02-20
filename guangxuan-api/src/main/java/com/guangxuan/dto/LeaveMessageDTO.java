package com.guangxuan.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhuolin
 * @Date 2020/1/13
 */
@Data
public class LeaveMessageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty(value = "留言信息")
    private String message;

    @ApiModelProperty(value = "时间")
    private Date time;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "用户手机号")
    private String phone;

    @ApiModelProperty(value = "项目id")
    private Long itemId;

    @ApiModelProperty(value = "用户等级")
    private Integer userLevel;

}
