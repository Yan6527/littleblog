package com.atyan.filter;

import com.alibaba.fastjson.JSON;
import com.atyan.domain.LoginUser;
import com.atyan.domain.ResponseResult;
import com.atyan.enums.AppHttpCodeEnum;
import com.atyan.utils.JwtUtil;
import com.atyan.utils.RedisCache;
import com.atyan.utils.WebUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * JWT认证过滤器，用于拦截请求并进行JWT令牌验证
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;

    /**
     * 执行过滤器的主要方法，用于验证JWT令牌并设置认证信息
     * @param request 请求对象
     * @param response 响应对象
     * @param filterChain 过滤链对象
     * @throws ServletException 如果过滤过程中发生Servlet异常
     * @throws IOException 如果过滤过程中发生IO异常
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 从请求头中获取令牌
        String token = request.getHeader("token");
        // 如果令牌为空，则直接放行
        if(!StringUtils.hasText(token)){
            filterChain.doFilter(request,response);
            return;
        }

        // 解析JWT令牌
        Claims claims = null;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            // 如果解析失败，返回错误信息
            e.printStackTrace();
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }

        // 获取用户ID
        String userid = claims.getSubject();

        // 从Redis中获取登录用户信息
        LoginUser loginUser = redisCache.getCacheObject("bloglogin:" + userid);
        // 如果登录用户信息为空，返回错误信息
        if(Objects.isNull(loginUser)){
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }

        // 创建认证令牌并设置到安全上下文中
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser,null,null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // 放行请求
        filterChain.doFilter(request,response);
    }
}
