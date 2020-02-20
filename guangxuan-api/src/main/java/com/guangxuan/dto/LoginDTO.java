package com.guangxuan.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author zhuolin
 * @Date 2019/11/26
 */
@Data
@ApiModel("登陆参数")
public class LoginDTO implements Serializable {
    @ApiModelProperty(value = "手机号",required = true)
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^[1][\\d]{10}$", message = "手机号码验证失败")
    String phone;

    @NotNull(message = "未获取当前用户")
    @ApiModelProperty(value = "密码或者验证码错误",required = true)
    private String password;
    private Long promoterId;
}
