package com.guangxuan.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.guangxuan.enumration.IResultEnum;
import com.guangxuan.enumration.ResultEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author ZhuoLin
 * @date 2019/7/13 10:54
 */
@Data
@AllArgsConstructor
@ToString
public class Result<T> implements Serializable {
    /**
     * 接口调用结果
     */
    private Integer code;
    /**
     * 接口调用失败编码
     */
    private Integer failCode;
    /**
     * 接口调用返回的信息
     */
    private String message;
    /**
     * 接口调用成功数据
     * 数据返回不要基础类型
     */
    private T data;


    public static <T> Result<T> success(T data, String message) {
        return new Result<>(ResultEnum.SUCCESS.getCode(), null, message, data);
    }

    public static <T> Result<T> fail(Integer failCode, String failMessage) {
        return new Result<T>(ResultEnum.FAIL.getCode(), failCode, failMessage, null);
    }

    public static Result fail(IResultEnum resultCode) {
        return fail(resultCode.getCode(), resultCode.getMessage());
    }

    public static Result fail(String message) {
        return fail(null, message);
    }

}