package com.guangxuan.vo.admin.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author deofly
 * @since 2019-04-26
 */
@Data
public class UpdatePasswordForm implements Serializable {

    private static final long serialVersionUID = -6252543147072834228L;

    @ApiModelProperty(value = "原密码",required = true)
    @NotEmpty(message = "原密码不能为空")
    private String oldPassword;
    @ApiModelProperty(value = "新密码",required = true)
    @NotEmpty(message = "新密码不能为空")
    private String newPassword;
}
