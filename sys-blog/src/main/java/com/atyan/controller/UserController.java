package com.atyan.controller;

import com.atyan.annotation.mySystemlog;
import com.atyan.domain.ResponseResult;
import com.atyan.domain.User;
import com.atyan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    // 获取用户信息
    @GetMapping("/userInfo")
    @mySystemlog(businessName = "获取用户信息")
    public ResponseResult userInfo(){
        return userService.userInfo();
    }

    // 更新用户信息
    @PutMapping("userInfo")
    @mySystemlog(businessName = "更新用户信息")
    public ResponseResult  updateUserInfo(@RequestBody User user){
        return userService.updateUserInfo(user);
    }
    // 注册
    @PostMapping("/register")
    @mySystemlog(businessName = "注册")
    public ResponseResult register(@RequestBody User user){
        return userService.register(user);
    }
}