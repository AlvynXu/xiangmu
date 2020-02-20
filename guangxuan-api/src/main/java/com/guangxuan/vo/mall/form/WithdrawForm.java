package com.guangxuan.vo.mall.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author deofly
 * @since 2019-06-19
 */
@Data
public class WithdrawForm implements Serializable {

    private static final long serialVersionUID = -4403455032784310230L;

    @NotEmpty(message = "姓名不能为空")
    private String name;

    @NotEmpty(message = "支付宝账号不能为空")
    private String alipay;

    @NotNull(message = "提现金额不能为空")
    private Integer amountCents;
}
