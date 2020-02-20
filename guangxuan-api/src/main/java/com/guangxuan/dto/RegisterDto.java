package com.guangxuan.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 注册dto
 *
 * @author zhuolin
 */
@Data
public class RegisterDto implements Serializable {
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^[1][\\d]{10}$", message = "手机号码验证失败")
    @ApiModelProperty(required = true)
    String phone;
    @ApiModelProperty(required = true)
    @Size(min = 6, max = 20, message = "请输入6到20位密码")
    String password;
    @ApiModelProperty(required = true)
    @NotNull(message = "未获取当前用户")
    private Long userId;
}
