package com.guangxuan.vo.admin.form;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author deofly
 * @since 2019-05-29
 */
@Data
public class AddressCheckForm implements Serializable {

    private static final long serialVersionUID = -7208626456086142283L;

    @NotNull(message = "申请编号不能为空")
    private Integer id;

    @NotNull(message = "审核结果不能为空")
    private Boolean success;
}
