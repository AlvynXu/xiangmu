package com.guangxuan.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author zhuolin
 * @Date 2019/11/27
 */
@Data
public class SetPromoterDTO implements Serializable {

    @NotNull(message = "用户id不能为空")
    @ApiModelProperty(required = true)
    private Long userId;

    @NotNull(message = "邀请码不能为空")
    @Size(min = 6, max = 6,message = "邀请码应为6位")
    @ApiModelProperty(required = true)
    private String promoterId;
}
