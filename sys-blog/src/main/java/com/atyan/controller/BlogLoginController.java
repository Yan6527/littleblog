package com.atyan.controller;

import com.atyan.annotation.mySystemlog;
import com.atyan.domain.ResponseResult;
import com.atyan.domain.User;
import com.atyan.enums.AppHttpCodeEnum;
import com.atyan.exception.SystemException;
import com.atyan.service.BlogLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogLoginController {

    @Autowired
    private BlogLoginService blogLoginService;

    @PostMapping("/login")
    @mySystemlog(businessName = "登录")
    public ResponseResult login(@RequestBody User user){
    //用户名为空
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return blogLoginService.login(user);
    }

    @PostMapping("/logout")
    @mySystemlog(businessName = "退出")
    public ResponseResult logout(){
        return blogLoginService.logout();
    }
}