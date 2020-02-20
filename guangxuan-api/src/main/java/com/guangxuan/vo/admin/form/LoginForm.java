package com.guangxuan.vo.admin.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author deofly
 * @since 2019-04-25
 */
@Data
public class LoginForm implements Serializable {

    private static final long serialVersionUID = -8143934523126217924L;

    @ApiModelProperty(required = true)
    @NotEmpty(message = "用户名不能为空")
    private String username;

    @ApiModelProperty(required = true)
    @NotEmpty(message = "密码不能为空")
    private String password;
}
