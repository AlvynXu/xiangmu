package com.guangxuan.enumration;

/**
 * @author ZhuoLin
 */
public enum ResultEnum implements IResultEnum {
    /**
     * 接口调用成功
     */
    SUCCESS(0, "SUCCESS"),
    /**
     * 接口调用失败
     */
    FAIL(1, "FAIL"),
    ;

    private Integer code;

    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}