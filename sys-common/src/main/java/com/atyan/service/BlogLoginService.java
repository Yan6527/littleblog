package com.atyan.service;

import com.atyan.domain.ResponseResult;
import com.atyan.domain.User;


public interface BlogLoginService {
    //登录
    ResponseResult login(User user);



    //退出登录
    ResponseResult logout();
}