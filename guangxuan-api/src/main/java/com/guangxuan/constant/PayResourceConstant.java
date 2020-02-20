package com.guangxuan.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * @author zhuolin
 * @Date 2019/12/14
 */
@Getter
@AllArgsConstructor
public enum PayResourceConstant {
    /**
     * 购买会员
     */
    BUY_VIP(0, "购买会员"),
    /**
     * 购买展位
     */
    BUY_BOOTH(1, "购买展位"),
    /**
     * 购买地主
     */
    BUY_STREET(2, "购买地主"),
    /**
     * 购买物品
     */
    BUY_GOODS(3, "购买物品"),
    /**
     * 充值
     */
    RECHARGE(4, "充值"),
    /**
     * 提现
     */
    WITHDRAWAL(5, "提现"),
    /**
     * 求组押金
     */
    RENT(6, "求租"),
    /**
     * 出租
     */
    PAY_RENT(7, "出租"),

    /**
     * 直推
     */
    DIRECT(8, "直推"),
    /**
     * 全网
     */
    INDIRECT(9, "全网"),
    /**
     * 区域
     */
    AREA(10, "区域内购买电线竿"),
    /**
     * 求租押金退回
     */
    RENT_BACK(11, "退押金"),
    ;

    Integer type;

    String typeName;

    public static PayResourceConstant valueOfType(Integer type) {
        if (type == null) {
            return null;
        }
        for (PayResourceConstant payResourceConstant : PayResourceConstant.values()) {
            if (payResourceConstant.getType().equals(type)) {
                return payResourceConstant;
            }
        }
        return null;
    }


}
