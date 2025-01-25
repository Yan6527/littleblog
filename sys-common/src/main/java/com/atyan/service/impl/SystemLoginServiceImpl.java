package com.atyan.service.impl;

import com.atyan.domain.LoginUser;
import com.atyan.domain.ResponseResult;
import com.atyan.domain.User;
import com.atyan.service.SystemLoginService;
import com.atyan.utils.JwtUtil;
import com.atyan.utils.RedisCache;
import com.atyan.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Service
//认证，判断用户登录是否成功
public class SystemLoginServiceImpl implements SystemLoginService {

    @Autowired
    //AuthenticationManager是security官方提供的接口
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        //封装认证信息
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        // 执行认证
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        // 获取登录用户信息
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        // 生成JWT
        String jwt = JwtUtil.createJWT(userId);

        // 存入redis
        redisCache.setCacheObject("login:"+userId,loginUser);
        // 封装响应数据
        Map<String,String> map = new HashMap<>();
        map.put("token",jwt);
        // 返回响应数据
        return  ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult logout() {
        // 获取中的用户id
        Long userId = SecurityUtils.getUserId();
        // 删除redis中的用户信息
        redisCache.deleteObject("login:"+userId);
        return ResponseResult.okResult();
    }

}