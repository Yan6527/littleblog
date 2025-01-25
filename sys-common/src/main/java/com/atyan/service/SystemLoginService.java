package com.atyan.service;

import com.atyan.domain.ResponseResult;
import com.atyan.domain.User;


public interface SystemLoginService {
    //登录
    ResponseResult login(User user);
    //退出
    ResponseResult logout();

}