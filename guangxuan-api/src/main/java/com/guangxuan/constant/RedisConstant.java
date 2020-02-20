package com.guangxuan.constant;

public class RedisConstant {

    private static String REDIS_PREFIX = "GUANGXUAN:";
    /**
     * 注册验证码
     */
    public static String REGISTER_RANDOM_CODE = REDIS_PREFIX + "REGISTER_RANDOM_CODE:";
    /**
     * 登陆验证码
     */
    public static String LOGIN_RANDOM_CODE = REDIS_PREFIX + "LOGIN_RANDOM_CODE:";
    /**
     * 登陆验证码
     */
    public static String USER_PROMOTE_COUNT = REDIS_PREFIX + "USER_PROMOTE_COUNT:";
    /**
     * 微信TOKEN
     */
    public static String WECHAT_TOKEN = REDIS_PREFIX + "WECHAT:TOKEN:";
    /**
     * 微信jsApiTicket
     */
    public static String WECHAT_TICKET = REDIS_PREFIX + "WECHAT:TICKET:";
    /**
     * 虚假销售的数量
     */
    public static String FAKE_SOLD_BOOTH_COUNT = REDIS_PREFIX + "FAKE_SOLD_COUNT:BOOTH";
    /**
     * 虚假销售的数量
     */
    public static String REG_CODE_SEQ = REDIS_PREFIX + "REG_CODE_SEQ";
    /**
     * 虚假销售的数量
     */
    public static String FAKE_STREET_SOLD_COUNT = REDIS_PREFIX + "FAKE_SOLD_COUNT:STREET";
    /**
     * 购买vip
     */
    public static String BUY_VIP = REDIS_PREFIX + "BUY_VIP:";
    /**
     * 购买展位
     */
    public static String BUY_BOOTH = REDIS_PREFIX + "BUY_BOOTH:";
    /**
     * 购买展位结束时间
     */
    public static String BUY_BOOTH_END = REDIS_PREFIX + "BUY_BOOTH_END";
    /**
     * 购买街道地主
     */
    public static String BUY_STREET = REDIS_PREFIX + "BUY_STREET:";
}
