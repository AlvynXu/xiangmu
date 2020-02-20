package com.guangxuan.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * @author deofly
 * @since 2019-03-29
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = 8000446397928312985L;

    protected T data;

    protected int code;

    protected String msg;

    public BaseResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public BaseResponse(int code, String msg) {
        this(code, msg, null);
    }

    public BaseResponse(String msg, T data) {
        this(200, msg, data);
    }

    public BaseResponse(String msg) {
        this(200, msg, null);
    }

    public BaseResponse(T data) {
        this(200, "ok", data);
    }

    public BaseResponse() {
        this(200, "ok", null);
    }

    public boolean isSuccess() {
        return code == 200;
    }
}
