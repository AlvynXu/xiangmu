package com.guangxuan.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author deofly
 * @since 2019-02-12
 */
@Data
public class WXOAuthJsToken implements Serializable {

    private static final long serialVersionUID = 1902783210596179254L;

    private String openid;

    private int expires_in = 7200;      //凭证有效时间，单位：秒

    private String session_key;

    private long expiredTime;

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
        this.expiredTime = System.currentTimeMillis() + (expires_in - 100) * 1000;
    }

    /**
     * 判断用户凭证是否过期
     *
     * @return 过期返回 true,否则返回false
     */
    public boolean isExpired() {
        return System.currentTimeMillis() >= this.expiredTime;
    }
}
