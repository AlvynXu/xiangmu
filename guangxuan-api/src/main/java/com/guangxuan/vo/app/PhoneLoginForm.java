package com.guangxuan.vo.app;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author deofly
 * @since 2019-04-09
 */
@Data
public class PhoneLoginForm implements Serializable {

    private static final long serialVersionUID = -3969850559572624251L;

    @NotEmpty(message = "手机号不能为空")
    private String mobile;

    @NotEmpty(message = "密码不能为空")
    private String password;
}
