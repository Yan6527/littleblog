package com.atyan.controller;

import com.atyan.domain.Menu;
import com.atyan.domain.ResponseResult;
import com.atyan.service.MenuService;
import com.atyan.utils.BeanCopyUtils;
import com.atyan.utils.SystemConverter;
import com.atyan.vo.MenuTreeVo;
import com.atyan.vo.MenuVo;
import com.atyan.vo.RoleMenuTreeSelectVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    //---------------------------------查询菜单列表--------------------------------------
    @GetMapping("/list")
    public ResponseResult list(Menu menu) {
        List<Menu> menus = menuService.selectMenuList(menu);
        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(menus, MenuVo.class);
        return ResponseResult.okResult(menuVos);
    }
    //---------------------------------新增菜单--------------------------------------
    @PostMapping
    public ResponseResult addMenu(@RequestBody Menu menu) {
        menuService.save(menu);
        return ResponseResult.okResult();
    }

    // ---------------------------------修改菜单--------------------------------------
    //①先查询根据菜单id查询对应的权限菜单
    @GetMapping(value = "/{menuId}")
    public ResponseResult getInfo(@PathVariable(value = "menuId")Long menuId){
        return ResponseResult.okResult(menuService.getById(menuId));
    }
    //②修改菜单
    @PutMapping
    public ResponseResult updateMenu(@RequestBody Menu menu){
        //判断父菜单设置是否为当前菜单
        if(menu.getParentId().equals(menu.getId())){
            return ResponseResult.errorResult(500,"修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        menuService.updateById(menu);
        return ResponseResult.okResult();
    }

    // ---------------------------------删除菜单--------------------------------------
    //删除菜单，但是如果要删除的菜单有子菜单则提示：存在子菜单不允许删除 并且删除失败
    @DeleteMapping("/{menuId}")
    public ResponseResult deleteMenu(@PathVariable(value = "menuId")Long menuId){
        //判断当前菜单是否有子菜单
        int count = menuService.count(menuId);
        if(count>0){
            return ResponseResult.errorResult(500,"存在子菜单不允许删除");
        }
        menuService.removeById(menuId);
        return ResponseResult.okResult();
    }

    //----------------------------新增角色-获取菜单下拉树列表-------------------------------

    @GetMapping("/treeselect")
    public ResponseResult treeselect() {
        //复用之前的selectMenuList方法。方法需要参数，参数可以用来进行条件查询，而这个方法不需要条件，所以直接new Menu()传入
        List<Menu> menus = menuService.selectMenuList(new Menu());
        List<MenuTreeVo> options =  SystemConverter.buildMenuSelectTree(menus);
        return ResponseResult.okResult(options);
    }
    //---------------------修改角色-根据角色id查询对应角色菜单列表树--------------------------

    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public ResponseResult roleMenuTreeSelect(@PathVariable("roleId") Long roleId) {
        List<Menu> menus = menuService.selectMenuList(new Menu());
        List<Long> checkedKeys = menuService.selectMenuListByRoleId(roleId);
        List<MenuTreeVo> menuTreeVos = SystemConverter.buildMenuSelectTree(menus);
        RoleMenuTreeSelectVo vo = new RoleMenuTreeSelectVo(checkedKeys,menuTreeVos);
        return ResponseResult.okResult(vo);
    }
}