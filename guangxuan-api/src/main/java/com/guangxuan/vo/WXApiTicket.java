package com.guangxuan.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author deofly
 * @since 2018-12-19
 */
@Data
public class WXApiTicket implements Serializable {

    private static final long serialVersionUID = -7188195009960011117L;

    private String ticket;

    private int expires_in;

    /**
     * 过期时间，单位秒
     */
    private long expiredTime;

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
        this.expiredTime = System.currentTimeMillis() / 1000 + (expires_in - 100);
    }
}
