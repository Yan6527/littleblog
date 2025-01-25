package com.atyan.service;

import com.atyan.domain.ResponseResult;
import com.atyan.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author sunset
* @description 针对表【sys_user(用户表)】的数据库操作Service
* @createDate 2025-01-19 14:30:50
*/
public interface UserService extends IService<User> {
    //用户登录
    ResponseResult userInfo();
    //修改用户信息
    ResponseResult updateUserInfo(User user);
    //用户注册
    ResponseResult register(User user);
    //查询用户列表
    ResponseResult selectUserPage(User user, Integer pageNum, Integer pageSize);
    //增加用户-②新增用户
    boolean checkUserNameUnique(String userName);
    boolean checkPhoneUnique(User user);
    boolean checkEmailUnique(User user);
    ResponseResult addUser(User user);
    //修改用户-②更新用户信息
    void updateUser(User user);
}
