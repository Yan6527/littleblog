package com.atyan.service.impl;

import com.atyan.constants.SystemConstants;
import com.atyan.domain.Menu;
import com.atyan.mapper.MenuMapper;
import com.atyan.service.MenuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Override
    // 根据用户id查询权限
    public List<String> selectPermsByUserId(Long id) {
        // 根据用户id查询菜单权限
        if(id == 1L){
            // 超级管理员
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            // 查询所有菜单权限
            wrapper.in(Menu::getMenuType, SystemConstants.TYPE_MENU,SystemConstants.TYPE_BUTTON);
            // 查询状态为正常
            wrapper.eq(Menu::getStatus, SystemConstants.STATUS_NORMAL);
            //查询条件是permissions中需要未被删除的权限的权限
            List<Menu> menus = list(wrapper);
            return menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
        }

        return getBaseMapper().selectPermsByOtherUserId(id);
    }

    //----------------------------------查询用户的路由信息(权限菜单)-------------------------------------

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {

        MenuMapper menuMapper = getBaseMapper();

        List<Menu> menus = null;

        //判断是否是超级管理员，用户id为id代表超级管理员，如果是就返回所有符合要求的权限菜单
        if(userId.equals(1L)){
            menus = menuMapper.selectAllRouterMenu();
        }else{
            //如果不是超级管理员，就查询对应用户所具有的路由信息(权限菜单)
            menus = menuMapper.selectOtherRouterMenuTreeByUserId(userId);
        }
        //构建成tree，也就是子父菜单树，有层级关系
        //思路:先找出第一层的菜单，然后再找子菜单(也就是第二层)，把子菜单的结果赋值给Menu类的children字段

        return buildMenuTree(menus,0L);
    }

    //用于把List集合里面的数据构建成tree，也就是子父菜单树，有层级关系
    private List<Menu> buildMenuTree(List<Menu> menus, long parentId){
        return menus.stream()
                //过滤找出父菜单树，也就是第一层
                .filter(menu -> menu.getParentId().equals(parentId))
                //getChildren是我们在下面写的方法，用于获取子菜单的List集合
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
    }

    //用于获取传入参数的子菜单，并封装为List集合返回
    private List<Menu>getChildren(Menu menu, @NotNull List<Menu> menus) {
        return menus.stream()
                //通过过滤得到子菜单
                .filter(m -> m.getParentId().equals(menu.getId()))
                //如果有三层菜单的话，也就是子菜单的子菜单，我们就用下面那行递归(自己调用自己)来处理
                .map(m -> m.setChildren(getChildren(m,menus)))
                .collect(Collectors.toList());
    }

    @Override
    public List<Menu> selectMenuList(Menu menu) {
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.like(menu.getMenuName() != null, Menu::getMenuName, menu.getMenuName());
            wrapper.like(menu.getStatus() != null, Menu::getStatus, menu.getStatus());
            wrapper.orderByAsc(Menu::getParentId, Menu::getOrderNum);
            return list(wrapper);

    }

    @Override
    public int count(Long menuId) {
        // 查询子菜单数量
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getParentId, menuId);
        return count(wrapper);
    }

    @Override
    public List<Long> selectMenuListByRoleId(Long roleId) {
        return getBaseMapper().selectMenuListByRoleId(roleId);
    }
}
