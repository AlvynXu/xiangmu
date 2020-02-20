package com.guangxuan.vo.mall.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author deofly
 * @since 2019-05-14
 */
@Data
public class AddressForm implements Serializable {

    private static final long serialVersionUID = 9091798401707494228L;

    private Integer id;

    @NotEmpty(message = "联系人不能为空")
    private String name;

    @NotEmpty(message = "手机号不能为空")
    @Pattern(regexp = "^1[3|4|5|7|8][0-9]{9}$", message = "手机号不正确")
    private String phone;

    @NotEmpty(message = "地区不能为空")
    private String area;

    private Integer street;

    private Integer village;

    @NotEmpty(message = "地址不能为空")
    private String ext;

    private Boolean isDefault;
}
