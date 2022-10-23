package com.hujjun.dms.config.exception;

import com.hujjun.dms.sys.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @AUTHOR hujjun
 * @DESC 全部异常处理
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    public Result handler(RuntimeException e) {
        log.error("运行时异常：---------{}", e.getMessage());
        return Result.error(e.getMessage());
    }
}
