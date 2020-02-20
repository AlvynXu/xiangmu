package com.guangxuan.exception;

import com.guangxuan.enumration.IResultEnum;
import lombok.Data;
import lombok.ToString;

/**
 * 业务异常
 */
@Data
@ToString
public class BusinessException extends RuntimeException {

    private IResultEnum resultEnum;

    private Integer code;

    private String message;

    public BusinessException(IResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.resultEnum = resultEnum;
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}