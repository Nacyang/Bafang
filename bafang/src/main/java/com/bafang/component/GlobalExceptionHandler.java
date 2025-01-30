package com.bafang.component;

import cn.dev33.satoken.util.SaResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler{

    @ExceptionHandler
    public SaResult ExceptionHandler(RuntimeException ex){
        log.error("异常信息：{}", ex.getMessage());
        return SaResult.error(ex.getMessage());
    }
}
