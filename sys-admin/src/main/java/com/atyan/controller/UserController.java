package com.atyan.controller;

import com.atyan.domain.ResponseResult;
import com.atyan.domain.Role;
import com.atyan.domain.User;
import com.atyan.dto.ChangeUserStatusDto;
import com.atyan.enums.AppHttpCodeEnum;
import com.atyan.exception.SystemException;
import com.atyan.service.RoleService;
import com.atyan.service.UserService;
import com.atyan.utils.BeanCopyUtils;
import com.atyan.vo.UserInfoAndRoleIdsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/system/user")
public class UserController {

    @Autowired
    private UserService userService;

    //--------------------------------查询用户列表-------------------------------------

    @GetMapping("/list")
    public ResponseResult list(User user, Integer pageNum, Integer pageSize) {
        return userService.selectUserPage(user,pageNum,pageSize);
    }
    @PostMapping
    public ResponseResult add(@RequestBody User user) {
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        if (!userService.checkUserNameUnique(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (!userService.checkPhoneUnique(user)){
            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_EXIST);
        }
        if (!userService.checkEmailUnique(user)){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        return userService.addUser(user);
    }
    //--------------------------------修改用户状态-------------------------------------
    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody ChangeUserStatusDto changeUserStatusDto) {
        User user = new User();
        user.setId(changeUserStatusDto.getUserId());
        user.setStatus(changeUserStatusDto.getStatus());
        return ResponseResult.okResult(userService.updateById(user));
    }
    //--------------------------------删除用户信息-------------------------------------
    @DeleteMapping()
    public ResponseResult remove(@RequestParam(value = "ids") String ids) {
        if (!ids.contains(",")) {
            userService.removeById(ids);
        } else {
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                userService.removeById(id);
            }
        }
        return ResponseResult.okResult();
    }

    //-----------------------修改用户-①根据id查询用户信息-----------------------------

    @Autowired
    private RoleService roleService;

    @GetMapping(value = { "/{userId}" })
    public ResponseResult getUserInfoAndRoleIds(@PathVariable(value = "userId") Long userId) {
        List<Role> roles = roleService.selectRoleAll();
        User user = userService.getById(userId);
        //当前用户所具有的角色id列表
        List<Long> roleIds = roleService.selectRoleIdByUserId(userId);

        UserInfoAndRoleIdsVo vo = new UserInfoAndRoleIdsVo(user,roles,roleIds);
        return ResponseResult.okResult(vo);
    }

    //-------------------------修改用户-②更新用户信息--------------------------------
    @PutMapping
    public ResponseResult edit(@RequestBody User user) {
        userService.updateUser(user);
        return ResponseResult.okResult();
    }
}