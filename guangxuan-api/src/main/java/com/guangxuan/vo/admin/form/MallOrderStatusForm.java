package com.guangxuan.vo.admin.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author deofly
 * @since 2019-05-24
 */
@Data
public class MallOrderStatusForm implements Serializable {

    private static final long serialVersionUID = 8779172514788476755L;

    @NotEmpty(message = "订单不存在")
    private String orderId;
}
