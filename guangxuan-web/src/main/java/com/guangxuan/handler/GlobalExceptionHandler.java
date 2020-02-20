package com.guangxuan.handler;

import com.alibaba.fastjson.JSON;
import com.guangxuan.dto.Result;
import com.guangxuan.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author ZhuoLin
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 业务异常处理器
     *
     * @param request
     * @param exception
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = BusinessException.class)
    public void businessException(HttpServletRequest request, HttpServletResponse response,
                                  BusinessException exception) throws Exception {
        log.error(exception.toString());
        Result result = Result.fail(exception.getResultEnum().getCode(), exception.getResultEnum().getMessage());
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.write(JSON.toJSONString(result));
        out.flush();
        out.close();
    }

    /**
     * Bean 校验异常 Validate
     *
     * @param request
     * @param exception
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public void methodArgumentValidationHandler(HttpServletRequest request, HttpServletResponse response,
                                                MethodArgumentNotValidException exception) throws IOException {
        exception.printStackTrace();
        log.error(exception.toString());
        Result result = Result.fail(1000, exception.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.write(JSON.toJSONString(result));
        out.flush();
        out.close();
    }

    /**
     * Bean 校验异常 Validate
     *
     * @param request
     * @param exception
     * @return
     */
    @ExceptionHandler(value = AuthenticationException.class)
    public void AuthenticationException(HttpServletRequest request, HttpServletResponse response,
                                                MethodArgumentNotValidException exception) throws IOException {
        exception.printStackTrace();
        log.error(exception.toString());
        Result result = Result.fail(1000, exception.getMessage());
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.write(JSON.toJSONString(result));
        out.flush();
        out.close();
    }


    /**
     * 绑定异常
     *
     * @param request
     * @param exception
     * @return
     */
    @ExceptionHandler(BindException.class)
    public void bindException(HttpServletRequest request, HttpServletResponse response, BindException exception) throws IOException {
        exception.printStackTrace();
        log.error(exception.toString());
        Result result = Result.fail(1000, exception.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.write(JSON.toJSONString(result));
        out.flush();
        out.close();
    }


}