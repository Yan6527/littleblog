package com.atyan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atyan.domain.RoleMenu;


public interface RoleMenuService extends IService<RoleMenu> {
    //修改角色-删除旧的的角色信息
    void deleteRoleMenuByRoleId(Long id);

}