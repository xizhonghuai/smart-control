package com.smart.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author: xizhonghuai
 * @description: ExceptionHandler
 * @create: 2022-06-17 13:41
 **/
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public RestResponse<?> handlerThrowable(Exception exception) {
        log.info(exception.getMessage());
        exception.printStackTrace();
        return RestResponse.fail(exception.getMessage());
    }

    @ExceptionHandler(Throwable.class)
    public RestResponse<?> handlerThrowable(Throwable throwable) {
        log.error("异常:", throwable);
        return RestResponse.fail(throwable.getMessage());
    }
}
