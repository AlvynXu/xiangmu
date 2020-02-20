package com.guangxuan.vo.admin.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author deofly
 * @since 2019-06-11
 */
@Data
public class AdminForm implements Serializable {

    private static final long serialVersionUID = 7668418363243393966L;

    @NotEmpty(message = "用户名不能为空")
    @ApiModelProperty(required = true)
    private String username;

    private String password;

    @ApiModelProperty(required = true)
    @NotNull(message = "角色不能为空")
    private Integer roleId;
}
