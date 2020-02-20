package com.guangxuan.constant;

/**
 * 订单状态
 *
 * @author deofly
 * @since 2019-05-15
 */
public class OrderStatus {

    public static final int CLOSE = -1; // 订单关闭

    public static final int NOT_PAY = 1; // 待付款

    public static final int PAYING = 2; // 付款中
    public static final int PAID = 9; // 完成付款

    public static final int NOT_DELIVER = 10; // 商家待发货
    public static final int NOT_RECEIVE = 11; // 待收货
    public static final int NOT_RATE = 20; // 待评价
    public static final int AFTER_SALE = 30; // 售后
}
