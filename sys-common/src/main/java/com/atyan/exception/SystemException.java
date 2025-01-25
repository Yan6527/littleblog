package com.atyan.exception;

import com.atyan.enums.AppHttpCodeEnum;

//统一异常处理
public class SystemException extends RuntimeException{

    private int code;

    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public SystemException(AppHttpCodeEnum httpCodeEnum) {
        super(httpCodeEnum.getMsg());
        //把某个枚举类里面的code和msg赋值给异常对象
        this.code = httpCodeEnum.getCode();
        this.msg = httpCodeEnum.getMsg();
    }
}