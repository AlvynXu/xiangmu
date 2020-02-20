package com.guangxuan.vo;

import lombok.Data;

/**
 * @author deofly
 * @since 2019-02-17
 */
@Data
public class SMSSendResponse {

    private String Code;

    private String Message;

    private String RequestId;

    private String BizId;

    public boolean isSuccess() {
        return "OK".equals(this.Code);
    }
}
