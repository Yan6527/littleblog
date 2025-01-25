package com.atyan.service.impl;

import com.atyan.domain.ResponseResult;
import com.atyan.domain.UserRole;
import com.atyan.exception.SystemException;
import com.atyan.service.UserRoleService;
import com.atyan.utils.BeanCopyUtils;
import com.atyan.utils.SecurityUtils;
import com.atyan.enums.AppHttpCodeEnum;
import com.atyan.vo.PageVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.atyan.vo.UserInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atyan.domain.User;
import com.atyan.service.UserService;
import com.atyan.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author sunset
* @description 针对表【sys_user(用户表)】的数据库操作Service实现
* @createDate 2025-01-19 14:30:50
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>implements UserService{

    @Autowired
    //解密和加密用的这套算法。securityConfig类
    private PasswordEncoder passwordEncoder;
    @Override
    public ResponseResult userInfo() {
        // 获取用户id
        Long userId = SecurityUtils.getUserId();
        // 根据用户id查询用户信息
        User user = getById(userId);
        // 封装vo
        UserInfoVo userInfoVo = BeanCopyUtils.copyProperties(user, UserInfoVo.class);

        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        //将前端传来的密码加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        updateById(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult register(User user) {
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        //密码
        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        //邮箱
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        //昵称
        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        //手机号码
        if(!StringUtils.hasText(user.getPhonenumber())){
            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_NOT_NULL);
        }

        //判断用户传给我们的用户名是否在数据库已经存在。userNameExist方法是下面定义的
        if(userNameExist(user.getUserName())){
            //SystemException是我们写的异常类、AppHttpCodeEnum是我们写的枚举类
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        //判断用户传给我们的昵称是否在数据库已经存在。NickNameExist方法是下面定义的
        if(NickNameExist(user.getNickName())){
            //SystemException是我们写的异常类、AppHttpCodeEnum是我们写的枚举类
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        //判断用户传给我们的邮箱是否在数据库已经存在。NickNameExist方法是下面定义的
        if(EmailExist(user.getEmail())){
            //SystemException是我们写的异常类、AppHttpCodeEnum是我们写的枚举类
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        //判断用户传给我们的手机号码是否在数据库已经存在。PhonenumberExist方法是下面定义的
        if(PhonenumberExist(user.getPhonenumber())){
            //SystemException是我们写的异常类、AppHttpCodeEnum是我们写的枚举类
            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_EXIST);
        }
        //加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        //保存到数据库
        save(user);
        //返回结果
        return ResponseResult.okResult();
    }


    private boolean PhonenumberExist(String phonenumber) {
        return count(new LambdaQueryWrapper<User>().eq(User::getPhonenumber, phonenumber))>0;
    }

    private boolean EmailExist(String email) {
        return count(new LambdaQueryWrapper<User>().eq(User::getEmail, email))>0;
    }

    private boolean NickNameExist(String nickName) {
        return count(new LambdaQueryWrapper<User>().eq(User::getNickName, nickName))>0;
    }

    private boolean userNameExist(String userName) {
        return count(new LambdaQueryWrapper<User>().eq(User::getUserName, userName))>0;
    }

    @Override
    public ResponseResult selectUserPage(User user, Integer pageNum, Integer pageSize) {
        //分页查询
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(user.getUserName()),User::getUserName,user.getUserName());
        queryWrapper.eq(StringUtils.hasText(user.getStatus()),User::getStatus,user.getStatus());
        queryWrapper.eq(StringUtils.hasText(user.getPhonenumber()),User::getPhonenumber,user.getPhonenumber());
        Page<User> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);
        //封装数据
        List<User> records = page.getRecords();
        List<User> userVoList = BeanCopyUtils.copyBeanList(records, User.class);
        PageVo pageVo = new PageVo(userVoList,page.getTotal());
        return ResponseResult.okResult(pageVo);

    }

    @Override
    public boolean checkUserNameUnique(String userName) {
        // 查询用户名是否唯一
        return count(new LambdaQueryWrapper<User>().eq(User::getUserName, userName)) == 0;
    }

    @Override
    public boolean checkPhoneUnique(User user) {
        // 查询手机号是否唯一
        return count(new LambdaQueryWrapper<User>().eq(User::getPhonenumber, user.getPhonenumber())) == 0;
    }

    @Override
    public boolean checkEmailUnique(User user) {
        // 查询邮箱是否唯一
        return count(new LambdaQueryWrapper<User>().eq(User::getEmail, user.getEmail())) == 0;
    }

    @Autowired
    private UserRoleService userRoleService;
    @Override
    public ResponseResult addUser(User user) {
        //密码加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        save(user);
        if(user.getRoleIds()!=null&&user.getRoleIds().length>0){
            insertUserRole(user);
        }
        return ResponseResult.okResult();
    }

    //插入用户角色关联表
    private void insertUserRole(User user) {
        List<UserRole> userRoleList = Arrays.stream(user.getRoleIds())
                .map(roleId -> new UserRole(user.getId(), roleId))
                .collect(Collectors.toList());
        userRoleService.saveBatch(userRoleList);
    }

    //-----------------------------修改用户-②更新用户信息-------------------------------
    @Override
    @Transactional
    public void updateUser(User user) {
        // 删除用户与角色关联
        LambdaQueryWrapper<UserRole> userRoleUpdateWrapper = new LambdaQueryWrapper<>();
        userRoleUpdateWrapper.eq(UserRole::getUserId,user.getId());
        userRoleService.remove(userRoleUpdateWrapper);

        // 新增用户与角色管理
        insertUserRole(user);
        // 更新用户信息
        updateById(user);
    }

}




