package com.atyan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atyan.domain.Menu;

import java.util.List;

public interface MenuService extends IService<Menu> {
    //查询用户的权限信息
    List<String> selectPermsByUserId(Long id);
    //查询权限菜单列表
    List<Menu> selectRouterMenuTreeByUserId(Long userId);
    //查询菜单列表
    List<Menu> selectMenuList(Menu menu);
    int count(Long menuId);
    //修改角色-根据角色id查询对应角色菜单列表树
    List<Long> selectMenuListByRoleId(Long roleId);
}