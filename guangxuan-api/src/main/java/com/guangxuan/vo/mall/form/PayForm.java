package com.guangxuan.vo.mall.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author deofly
 * @since 2019-05-15
 */
@Data
public class PayForm implements Serializable {

    private static final long serialVersionUID = -1569460898106997708L;

    @NotEmpty(message = "订单不存在")
    private String orderId;

    private Integer payType;
}
