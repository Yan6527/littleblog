package com.atyan.controller;

import com.atyan.domain.LoginUser;
import com.atyan.domain.Menu;
import com.atyan.domain.ResponseResult;
import com.atyan.domain.User;
import com.atyan.enums.AppHttpCodeEnum;
import com.atyan.exception.SystemException;
import com.atyan.service.MenuService;
import com.atyan.service.RoleService;
import com.atyan.service.SystemLoginService;
import com.atyan.utils.BeanCopyUtils;
import com.atyan.utils.SecurityUtils;
import com.atyan.vo.AdminUserInfoVo;
import com.atyan.vo.RoutersVo;
import com.atyan.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoginController {
    @Autowired
    private SystemLoginService systemLoginService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleService roleService;

    // 登录
    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUserName())){
            //提示 必须要传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return systemLoginService.login(user);
    }

    //查询角色信息
    @GetMapping("/getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo(){
        //获取当前登录的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //根据id查询角色信息
        List<String> roleKeyList = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());
        //根据id查询权限信息
        List<String> perms = menuService.selectPermsByUserId(loginUser.getUser().getId());

        //获取用户信息
        User user = loginUser.getUser();
        //属性拷贝
        UserInfoVo userInfoVo = BeanCopyUtils.copyProperties(user, UserInfoVo.class);
        //封装数据返回
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms,roleKeyList,userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }
    //查询路由信息
    @GetMapping("/getRouters")
    public ResponseResult<RoutersVo> getRouters(){
        //获取用户id
        Long userId = SecurityUtils.getUserId();

        //根据用户id来查询menu(权限菜单)。要求查询结果是tree的形式，也就是子父菜单树
        List<Menu> menus = menuService.selectRouterMenuTreeByUserId(userId);
        //封装响应返回
        return ResponseResult.okResult(new RoutersVo(menus));
    }
    //退出登录
    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return systemLoginService.logout();
    }
}