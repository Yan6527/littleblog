package com.atyan.domain;

import com.atyan.enums.AppHttpCodeEnum;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * 响应格式体类，用于封装返回给前端的数据
 * 实现了Serializable接口以支持序列化
 * 使用@JsonInclude注解，仅当字段值不为null时才进行序列化
 *
 * @param <T> 泛型参数，用于表示响应数据的类型
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseResult<T> implements Serializable {
    private Integer code;
    private String msg;
    private T data;

    /**
     * 默认构造方法，初始化为成功状态
     */
    public ResponseResult() {
        this.code = AppHttpCodeEnum.SUCCESS.getCode();
        this.msg = AppHttpCodeEnum.SUCCESS.getMsg();
    }

    /**
     * 构造方法，指定状态码和数据
     *
     * @param code 状态码
     * @param data 响应数据
     */
    public ResponseResult(Integer code, T data) {
        this.code = code;
        this.data = data;
    }
    /**
     * 构造方法，指定状态码和消息
     *
     * @param code 状态码
     * @param msg  消息
     */
    public ResponseResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 构造方法，指定状态码、消息和数据
     *
     * @param code 状态码
     * @param msg  消息
     * @param data 响应数据
     */
    public ResponseResult(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }


    /**
     * 创建一个成功的响应结果
     *
     * @return ResponseResult实例
     */
    public static ResponseResult okResult() {
        ResponseResult result = new ResponseResult();
        return result;
    }

    /**
     * 创建一个成功的响应结果，带有指定的状态码和消息
     *
     * @param code 状态码
     * @param msg  消息
     * @return ResponseResult实例
     */
    public static ResponseResult okResult(int code, String msg) {
        ResponseResult result = new ResponseResult();
        return result.ok(code, null, msg);
    }

    /**
     * 创建一个成功的响应结果，带有指定的数据
     *
     * @param data 响应数据
     * @return ResponseResult实例
     */
    public static ResponseResult okResult(Object data) {
        ResponseResult result = setAppHttpCodeEnum(AppHttpCodeEnum.SUCCESS, AppHttpCodeEnum.SUCCESS.getMsg());
        if (data != null) {
            result.setData(data);
        }
        return result;
    }
    /**
     * 创建一个错误的响应结果
     *
     * @param code 状态码
     * @param msg  消息
     * @return ResponseResult实例
     */
    public static ResponseResult errorResult(int code, String msg) {
        ResponseResult result = new ResponseResult();
        return result.error(code, msg);
    }


    /**
     * 创建一个错误的响应结果，带有指定的枚举类型
     *
     * @param enums AppHttpCodeEnum枚举实例
     * @return ResponseResult实例
     */
    public static ResponseResult errorResult(AppHttpCodeEnum enums) {
        return setAppHttpCodeEnum(enums, enums.getMsg());
    }

    /**
     * 创建一个错误的响应结果，带有指定的枚举类型和消息
     *
     * @param enums AppHttpCodeEnum枚举实例
     * @param msg   消息
     * @return ResponseResult实例
     */
    public static ResponseResult errorResult(AppHttpCodeEnum enums, String msg) {
        return setAppHttpCodeEnum(enums, msg);
    }

    /**
     * 创建一个成功的响应结果，带有指定的状态码和消息
     *
     * @param enums AppHttpCodeEnum枚举实例
     * @return ResponseResult实例
     */
    public static ResponseResult setAppHttpCodeEnum(AppHttpCodeEnum enums) {
        return okResult(enums.getCode(), enums.getMsg());
    }

    /**
     * 创建一个成功的响应结果，带有指定的状态码和消息
     *
     * @param enums AppHttpCodeEnum枚举实例
     * @param msg   消息
     * @return ResponseResult实例
     */
    private static ResponseResult setAppHttpCodeEnum(AppHttpCodeEnum enums, String msg) {
        return okResult(enums.getCode(), msg);
    }

    /**
     * 设置错误响应
     *
     * @param code 状态码
     * @param msg  消息
     * @return 当前实例
     */
    public ResponseResult<?> error(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
        return this;
    }

    /**
     * 设置成功响应
     *
     * @param code 状态码
     * @param data 响应数据
     * @return 当前实例
     */
    public ResponseResult<?> ok(Integer code, T data) {
        this.code = code;
        this.data = data;
        return this;
    }

    /**
     * 设置成功响应
     *
     * @param code 状态码
     * @param data 响应数据
     * @param msg  消息
     * @return 当前实例
     */
    public ResponseResult<?> ok(Integer code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        return this;
    }

    /**
     * 设置成功响应
     *
     * @param data 响应数据
     * @return 当前实例
     */
    public ResponseResult<?> ok(T data) {
        this.data = data;
        return this;
    }

    /**
     * 获取状态码
     *
     * @return 状态码
     */
    public Integer getCode() {
        return code;
    }

    /**
     * 设置状态码
     *
     * @param code 状态码
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * 获取消息
     *
     * @return 消息
     */
    public String getMsg() {
        return msg;
    }

    /**
     * 设置消息
     *
     * @param msg 消息
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 获取响应数据
     *
     * @return 响应数据
     */
    public T getData() {
        return data;
    }

    /**
     * 设置响应数据
     *
     * @param data 响应数据
     */
    public void setData(T data) {
        this.data = data;
    }
}
