package com.guangxuan.enumration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhuolin
 * 业务失败返回编码
 */
@Getter
@AllArgsConstructor
public enum BusinessFailEnum implements IResultEnum {

    /**
     * 发送验证码失败
     */
    GET_RANDOM_FAIL(1001, "发送验证码失败"),
    /**
     * 获取验证码失败
     */
    NOT_GET_RANDOM(1002, "未获取验证码或者验证码已过期"),
    /**
     * 验证码错误
     */
    VALID_RANDOM_FAIL(1003, "验证码错误"),
    /**
     * 未获取到当前用户
     */
    NOT_GET_CURRENT_USER(1004, "未获取到当前用户"),

    /**
     * 与当前登陆用户手机号不一致
     */
    NOT_EQUAL_CURRENT_USER_PHONE(1005, "与当前登陆用户手机号不一致"),
    /**
     * 手机号验证失败
     */
    PHONE_VALID_FAIL(1006, "手机号验证失败"),
    /**
     * 用户名或者密码错误
     */
    USER_NAME_OR_PASSWORD_ERROR(1007, "用户名或者密码错误"),
    /**
     * 当前手机号未注册，请先进行注册
     */
    NOT_REGISTER_PHONE(1008, "当前手机号未注册，请先进行注册"),
    /**
     * 当前手机号未设置密码，请先设置密码
     */
    NOT_SET_PASSWORD(1009, "当前手机号未设置密码，请先设置密码"),
    /**
     * 您无特权展位，请购买地主
     */
    NO_CHOOSE_COUNT(1010, "您无特权展位，请购买地主"),
    /**
     * 您暂无提现权限，请先升级为代理
     */
    NO_PRIVILEGES(1011, "您暂无提现权限，请先进入个人中心升级为代理"),
    /**
     * 请先进行微信支付才能提现
     */
    NO_WECHAT_PAY(1012, "请先进行微信支付才能提现"),
    /**
     * 获取的消息不存在
     */
    NOT_GET_CURRENT_MESSAGE(2001, "获取的消息不存在"),
    /**
     * 获取分页数据失败
     */
    NOT_GET_PAGE_INFO(2002, "获取分页数据失败"),
    /**
     * 未获取到邀请人
     */
    NOT_GET_PROMOTER_USER(2003, "未获取到邀请人"),

    /**
     * 非待发布状态
     */
    NOT_WAIT_PUBLISH(2004, "非待发布状态"),
    /**
     * 展位正在被使用
     */
    BOOTH_IN_USER(2005, "展位正在被使用"),
    /**
     * 文件不能为空
     */
    FILE_NOT_NULL(2006, "文件不能为空"),
    /**
     * 未获取到文件后缀名
     */
    FILE_SUFFIX_IS_NULL(2009, "未获取到文件后缀名"),
    /**
     * 数据验证失败
     */
    DATA_VALID_FAIL(2007, "数据验证失败"),
    /**
     * 下架失败
     */
    OFF_SHELVE_FAIL(2008, "下架失败"),
    /**
     * 下架失败
     */
    GRAPH_TOO_LONG(2010, "输入段落过长"),
    /**
     * 页码不能为空
     */
    PAGE_NOT_NULL(3001, "页码不能为空"),
    /**
     * 数据条数不能为空
     */
    SIZE_NOT_NULL(3002, "数据条数不能为空"),
    /**
     * 类型不能为空
     */
    TYPE_NOT_NULL(3003, "类型不能为空"),
    /**
     * 区域编码不能为空
     */
    AREA_CODE_NOT_NULL(3004, "区域编码不能为空"),
    /**
     * 区域编码不能为空
     */
    CATEGORY_TYPE_NOT_NULL(3005, "商品类目不能为空"),
    /**
     * 天数不能为空
     */
    DAYS_NOT_NULL(3006, "天数不能为空"),
    /**
     * 区域已存在一个正在使用的头条
     */
    HEADLINE_AREA_USED(3006, "区域已存在一个正在使用的头条"),
    /**
     * 该头条正在使用
     */
    HEADLINE_USED(3007, "该头条正在使用"),
    /**
     * 区域已存在一个正在使用的banner
     */
    BANNER_AREA_USED(3008, "区域已存在一个正在使用的banner"),
    /**
     * 街道编码错误
     */
    STREET_CODE_VALID_FAIL(3009, "街道编码错误"),
    /**
     * 城市编码错误
     */
    CITY_CODE_VALID_FAIL(3010, "城市编码错误"),
    /**
     * 该展位非售卖状态
     */
    BOOTH_NOT_SALE_STATUS(4001, "该展位非售卖状态"),
    /**
     * 该物品正在售卖中
     */
    ON_SALE(4002, "该商品已存在一笔订单"),
    /**
     * 未获取当前数据
     */
    NOT_GET_DATA(4003, "未获取当前数据"),

    /**
     * 余额不足
     */
    BALANCE_CANT_PAY(4004, "余额不足"),
    /**
     * 为保留展位只可以地主选择，不允许购买
     */
    BOOTH_IS_SAVED(4005, "该展位为保留展位只可以地主选择，不允许购买"),
    /**
     * 求租数量不争气
     */
    SEEK_RENT_VALID_FAILURE(4006, "该租数量不争气"),
    /**
     * 当前项目为非上架状态
     */
    NOT_ON_SALE(4007, "当前项目为非上架状态"),
    /**
     * 已收藏该项目
     */
    COLLECTED(4008, "已收藏该项目"),
    /**
     * banner正在使用中
     */
    BANNER_USED(4009, "banner正在使用中"),
    /**
     * 头条正在使用中
     */
    HEADLINE_USED_ITEM(4010, "头条正在使用中"),
    /**
     * 未获取到项目
     */
    NOT_GET_ITM(4011, "未获取到项目"),
    /**
     * 请勿重复选择展位
     */
    BOOTH_REPEAT(4011, "请勿重复选择展位"),
    /**
     * 当前求组数量已达上限
     */
    SEEK_RENT_MAX_COUNT(4012, "当前求租数量已达上限"),
    /**
     * 充值金额过多
     */
    MONEY_MUCH(4013, "输入金额过多"),
    /**
     * 支付金额不能大于50000
     */
    MONEY_MUCH_1(4013, "支付金额不能大于50000"),
    /**
     * 充值金额过少
     */
    MONEY_LESS(4013, "输入金额应大于0元"),
    /**
     * 输入金额应为制定倍数
     */
    VALID_AMOUNT_FAIL(4014, "提现金额应为10倍数"),
    /**
     * 不能将展位租给自己
     */
    CANNOT_RENT_SELF(4015, "不能将展位租给自己"),
    /**
     * 无匹配展位
     */
    BOOTH_NOT_MATCH(4016, "无匹配展位"),
    /**
     * 当前项目非下架状态
     */
    ITEM_NOT_OFF_SHELVE(4017, "当前项目不可删除"),
    /**
     * 该项目选择街道重复
     */
    MALLITEM_IN_SAME_STREET(4018, "该项目选择街道重复"),
    /**
     * 当前状态不可操作
     */
    CURRENT_STATUS_CANNOT_OPERATE(4019,"当前状态不可操作")
            ;

    private Integer code;

    private String message;

}
