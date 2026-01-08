package com.qf.electronic.controller;

import com.qf.electronic.result.ResponseState;
import com.qf.electronic.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理器
 */
@ControllerAdvice //在控制器层面的一个装置
public class GlobalExceptionController {

    @ExceptionHandler //这个注解就是一个异常处理器
    @ResponseBody
    public Result<String> handleException(Exception e){
        return Result.error(ResponseState.SERVER_ERROR, e.getMessage());
    }
}
