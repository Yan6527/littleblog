package com.atyan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atyan.domain.UserRole;
import com.atyan.mapper.UserRoleMapper;
import com.atyan.service.UserRoleService;
import org.springframework.stereotype.Service;


@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}