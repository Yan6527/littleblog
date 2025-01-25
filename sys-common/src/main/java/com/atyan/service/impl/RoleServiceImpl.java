package com.atyan.service.impl;

import com.atyan.domain.ResponseResult;
import com.atyan.domain.RoleMenu;
import com.atyan.service.RoleMenuService;
import com.atyan.vo.PageVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atyan.domain.Role;
import com.atyan.mapper.RoleMapper;
import com.atyan.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Override
    //查询用户的角色信息
    public List<String> selectRoleKeyByUserId(Long id) {
        //判断是否是管理员，如果是，就返回集合中只需要有admin
        if(id == 1L){
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }

        //否则查询对应普通用户所具有的的角色信息
        List<String> otherRole = getBaseMapper().selectRoleKeyByOtherUserId(id);

        return otherRole;
    }

    @Override
    public ResponseResult selectRolePage(Role role, Integer pageNum, Integer pageSize) {
        //分页查询
        //对角色名称进行模糊查询。要求能够针对状态进行查询。要求按照role_sort进行升序排列
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(role.getRoleName()!=null,Role::getRoleName,role.getRoleName());
        queryWrapper.eq(role.getStatus()!=null,Role::getStatus,role.getStatus());
        queryWrapper.orderByAsc(Role::getRoleSort);
        //查询
        Page<Role> page = page(new Page<>(pageNum, pageSize), queryWrapper);
        //封装数据
        PageVo pageVo = new PageVo(page.getRecords(),page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Autowired

    private RoleMenuService roleMenuService;
    @Override
    @Transactional
    public void insertRole(Role role) {
        save(role);
        System.out.println(role.getId());
        if(role.getMenuIds()!=null&&role.getMenuIds().length>0){
            insertRoleMenu(role);
        }
    }

    private void insertRoleMenu(Role role) {
        List<RoleMenu> roleMenuList = Arrays.stream(role.getMenuIds())
                .distinct() // 去重
                .filter(Objects::nonNull) // 过滤空值
                .map(menuId -> new RoleMenu(role.getId(), menuId))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenuList);
    }
    @Override
    public void updateRole(Role role) {
        updateById(role);
        if(role.getMenuIds()!=null&&role.getMenuIds().length>0){
            roleMenuService.deleteRoleMenuByRoleId(role.getId());
            insertRoleMenu(role);
        }

    }
}