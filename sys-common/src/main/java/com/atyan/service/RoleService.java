package com.atyan.service;

import com.atyan.domain.ResponseResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atyan.domain.Role;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RoleService extends IService<Role> {
    //查询用户的角色信息
    List<String> selectRoleKeyByUserId(Long id);
    //查询所角色列表
    ResponseResult selectRolePage(Role role, Integer pageNum, Integer pageSize);
    //新增角色
    void insertRole(Role role);
    //修改角色-保存修改好的角色信息
    void updateRole(Role role);

}