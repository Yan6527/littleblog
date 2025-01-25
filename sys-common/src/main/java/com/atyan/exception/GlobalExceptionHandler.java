package com.atyan.exception;

import com.atyan.domain.ResponseResult;
import com.atyan.enums.AppHttpCodeEnum;
import com.atyan.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;


//@ControllerAdvice //对controller层的增强
//@ResponseBody
//代替上面的两个注解
@RestControllerAdvice
@Slf4j
//全局异常处理。最终都会在这个类进行处理异常
public class GlobalExceptionHandler {

    //SystemException是我们写的类。用户登录的异常交给这里处理
    @ExceptionHandler(SystemException.class)
    public ResponseResult systemExceptionHandler(SystemException e){
        log.error("出现了异常! {}",e);
        return ResponseResult.errorResult(e.getCode(),e.getMsg());
    }

    // 处理SpringSecurity的权限异常
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseResult handleAccessDeniedException(AccessDeniedException e) {
        return ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH.getCode(),e.getMessage());//枚举值是500
    }
    //其它异常交给这里处理
    @ExceptionHandler(Exception.class)
    public ResponseResult exceptionHandler(Exception e){
        log.error("出现了异常! {}",e);
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),e.getMessage());//枚举值是500
    }
}