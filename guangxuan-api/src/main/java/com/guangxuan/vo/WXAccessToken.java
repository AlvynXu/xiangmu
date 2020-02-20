package com.guangxuan.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author deofly
 * @since 2018-12-25
 */
@Data
public class WXAccessToken implements Serializable {

    private static final long serialVersionUID = -6119486234132641179L;

    private String accessToken;

    /**
     * 单位秒
     */
    private int expires;

    /**
     * 过期时间，单位秒
     */
    private long expiredTime;

    public void setExpires(int expires) {
        this.expires = expires;
        this.expiredTime = System.currentTimeMillis() / 1000 + (expires - 100);
    }
}
