package com.atyan.service.impl;

import com.atyan.domain.LoginUser;
import com.atyan.domain.ResponseResult;
import com.atyan.domain.User;
import com.atyan.service.BlogLoginService;
import com.atyan.utils.JwtUtil;
import com.atyan.utils.RedisCache;
import com.atyan.vo.BlogUserLoginVo;
import com.atyan.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.atyan.utils.BeanCopyUtils;

import java.util.Objects;

/**
 * 实现用户认证（登录）功能的服务类
 */
@Service
//认证，判断用户登录是否成功
public class BlogLoginServiceImpl implements BlogLoginService {
    /**
     * Spring Security 提供的认证管理器，用于处理用户认证
     */
    @Autowired
    //AuthenticationManager是security官方提供的接口
    private AuthenticationManager authenticationManager;

    /**
     * Redis缓存工具类，用于缓存用户登录信息
     */
    @Autowired
    private RedisCache redisCache;

    /**
     * 用户登录方法
     *
     * @param user 用户名和密码信息
     * @return 登录成功后的响应结果，包含用户信息和JWT令牌
     * @throws RuntimeException 如果用户名或密码错误，抛出运行时异常
     */
    @Override
    public ResponseResult login(User user) {
        // 创建认证令牌，包含用户名和密码
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        // 执行认证
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        // 如果认证结果为空，抛出异常
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }

        // 获取登录用户信息
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        // 获取用户ID并转换为字符串
        String userId = loginUser.getUser().getId().toString();

        // 创建JWT令牌
        String jwt = JwtUtil.createJWT(userId);
        // 将登录用户信息缓存到Redis
        redisCache.setCacheObject("bloglogin:"+userId,loginUser);
        // 将登录用户信息转换为UserInfoVo对象
        UserInfoVo userInfoVo = BeanCopyUtils.copyProperties(loginUser.getUser(), UserInfoVo.class);
        // 创建包含JWT令牌和用户信息的登录响应对象
        BlogUserLoginVo vo = new BlogUserLoginVo(jwt,userInfoVo);

        // 返回登录成功后的响应结果
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult logout() {

        //获取token，然后解析token值获取其中的userid。SecurityContextHolder是security官方提供的类
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //LoginUser是我们写的类
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        //获取userid
        Long userid = Long.valueOf(loginUser.getUser().getId());

        //在redis根据key来删除用户的value值，注意之前我们在存key的时候，key是加了'bloglogin:'前缀
        redisCache.deleteObject("bloglogin:"+userid);
        //封装响应返回
        return ResponseResult.okResult();
    }
}
