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
public class SuccessResponse extends BaseResponse {

    private static final long serialVersionUID = 3077160685045060701L;

    public SuccessResponse(String msg, Object data) {
        super(200, msg, data);
    }

    public SuccessResponse(String msg) {
        this(msg, null);
    }

    public SuccessResponse(Object data) {
        this("Ok", data);
    }

    public SuccessResponse() {
        this(null);
    }

    public boolean isSuccess() {
        return true;
    }
}
