package com.atyan.controller;

import com.atyan.domain.ResponseResult;
import com.atyan.domain.Role;
import com.atyan.dto.ChangeRoleStatusDto;
import com.atyan.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    //------------------------------查询角色列表---------------------------------------
    @GetMapping("/list")
    public ResponseResult list(Role role, Integer pageNum, Integer pageSize) {
        return roleService.selectRolePage(role,pageNum,pageSize);
    }
    //-----------------------------修改角色的状态--------------------------------------

    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody ChangeRoleStatusDto roleStatusDto){
        Role role = new Role();
        role.setId(roleStatusDto.getRoleId());
        role.setStatus(roleStatusDto.getStatus());
        return ResponseResult.okResult(roleService.updateById(role));
    }

    //-----------------------------新增角色--------------------------------------
    @PostMapping
    public ResponseResult add( @RequestBody Role role) {
        roleService.insertRole(role);
        return ResponseResult.okResult();
    }
    //-----------------------------修改角色--------------------------------------
    //先根据角色id查询角色信息
    @GetMapping(value = "/{id}")
    public ResponseResult getInfo(@PathVariable(value = "id") Long id) {
        Role role = roleService.getById(id);
        return ResponseResult.okResult(role);
    }
    //-------------------------修改角色-保存修改好的角色信息------------------------------

    @PutMapping
    public ResponseResult edit(@RequestBody Role role) {
        roleService.updateRole(role);
        return ResponseResult.okResult();
    }

    //--------------------------------删除角色---------------------------------------

    @DeleteMapping()
    public ResponseResult remove(@RequestBody List<Long> ids) {
        ids.forEach(id -> roleService.removeById(id));
        return ResponseResult.okResult();
    }
}