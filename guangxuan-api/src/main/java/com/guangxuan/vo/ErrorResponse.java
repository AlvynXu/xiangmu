package com.guangxuan.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author deofly
 * @since 2019-03-29
 */
@Data
@EqualsAndHashCode(callSuper=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse extends BaseResponse {

    private static final long serialVersionUID = -4454184030795590682L;

    public ErrorResponse(int code, String msg) {
        super(code, msg);
    }

    public ErrorResponse(int code) {
        this(code, "Error");
    }

    public ErrorResponse(String msg) {
        this(-1, msg);
    }

    public ErrorResponse() {
        this(-1);
    }

    public boolean isSuccess() {
        return false;
    }
}
