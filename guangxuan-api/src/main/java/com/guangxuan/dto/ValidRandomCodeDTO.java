package com.guangxuan.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class ValidRandomCodeDTO implements Serializable {
    @NotBlank(message = "手机号不能为空")
    @ApiModelProperty(required = true)
    @Pattern(regexp = "^[1][\\d]{10}$", message = "手机号码验证失败")
    String phone;
    @Size(min = 4, max = 4, message = "请输入四位验证码")
    @ApiModelProperty(required = true)
    String randomCode;
    Long promoterId;


}
