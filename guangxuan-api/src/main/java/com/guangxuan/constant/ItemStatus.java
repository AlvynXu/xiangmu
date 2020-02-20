package com.guangxuan.constant;

/**
 * 商品状态
 * 修改状态需要同步修改 ItemUtils
 * @see
 *
 * @author deofly
 * @since 2019-05-15
 */
public class ItemStatus {

    public static final int AUDIT_FAIL = -1; // 审核失败
    public static final int NOT_AUDIT = 0; // 审核中
    public static final int OFF_SHELVE = 2; // 未发布
    public static final int ON_SALE = 3; // 发布中
}
