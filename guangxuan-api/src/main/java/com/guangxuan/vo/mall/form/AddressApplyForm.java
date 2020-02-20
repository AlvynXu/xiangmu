package com.guangxuan.vo.mall.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author deofly
 * @since 2019-05-28
 */
@Data
public class AddressApplyForm implements Serializable {

    private static final long serialVersionUID = 3225696123682297906L;

    @NotEmpty(message = "省市区不能为空")
    private String areaCode;

    private Integer streetId;

    @NotEmpty(message = "新添地址不能为空")
    private String name;
}
