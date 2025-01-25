package com.atyan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.atyan.domain.Role;

import java.util.List;

/**
 * @author 35238
 * @date 2023/8/4 0004 13:32
 */
public interface RoleMapper extends BaseMapper<Role> {
    //查询普通用户的角色权限
    List<String> selectRoleKeyByOtherUserId(Long userId);
}