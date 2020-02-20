package com.guangxuan.vo.mall.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author deofly
 * @since 2019-06-05
 */
@Data
public class OrderForm implements Serializable {

    private static final long serialVersionUID = 5460100422059948699L;

    @NotEmpty(message = "订单不存在")
    private String orderId;
}
