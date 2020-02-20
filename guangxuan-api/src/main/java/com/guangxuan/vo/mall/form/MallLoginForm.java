package com.guangxuan.vo.mall.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author deofly
 * @since 2019-04-25
 */
@Data
public class MallLoginForm implements Serializable {

    private static final long serialVersionUID = 409871179810385708L;

    @NotEmpty(message = "手机号不能为空")
    private String phone;

    @NotEmpty(message = "非法登录")
    private String code;

    @NotEmpty(message = "验证码不能为空")
    private String vcode;

    private String avatar;

    private String nick;

    private String inviteCode;
}
