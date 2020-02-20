package com.guangxuan.vo.admin.filter;

import lombok.Data;

import java.io.Serializable;

/**
 * @author deofly
 * @since 2019-06-20
 */
@Data
public class WithdrawRecordFilter implements Serializable {

    private static final long serialVersionUID = -2822987452943299801L;

    private Boolean isWithdraw;
}
