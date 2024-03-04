package com.dls.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dls.constants.SystemConstants;
import com.dls.dao.MenuDao;
import com.dls.domain.entity.Menu;
import com.dls.service.MenuService;
import com.dls.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author denglinsha
 * @since 2023-12-15 21:07:50
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuDao, Menu> implements MenuService {
    @Override
    //查询用户的权限信息
    public List<String> selectPermsByUserId(Long id) {
        //根据用户id查询用户的权限信息。用户id为1代表管理员，如果是管理员就返回所有的权限
        if(id == 1L){
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            //所有菜单类型为C或者F的权限。
            wrapper.in(Menu::getMenuType, SystemConstants.TYPE_MENU,SystemConstants.TYPE_BUTTON);
            //状态为正常的权限。
            wrapper.eq(Menu::getStatus,SystemConstants.CATEGORY__STATUS_NORMAL);

            List<Menu> menus = list(wrapper);
            List<String> perms = menus.stream()
                    // 得到权限信息
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }

        //如果不是管理员就返回对应用户所具有的权限
        List<String> otherPerms = getBaseMapper().selectPermsByOtherUserId(id);
        return otherPerms;
    }

//    查询用户的路由信息(权限菜单)
    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuDao menuDao = getBaseMapper();
        List<Menu> menus = null;
        // 判断是否是管理员
        if (SecurityUtils.isAdmin()) {
            // 如果是 返回所有符合要求的Menu
            menus = menuDao.selectAllRouterMenu();
        } else {
            //如果不是超级管理员，就查询对应用户所具有的路由信息(权限菜单)
            menus = menuDao.selectOtherRouterMenuTreeByUserId(userId);
        }
        //构建成tree，也就是子父菜单树，有层级关系
        //思路:先找出第一层的菜单，然后再找子菜单(也就是第二层)，把子菜单的结果赋值给Menu类的children字段
        List<Menu> menuTree = buildMenuTree(menus,0L);

        return menuTree;
    }

    // 查询菜单列表
    @Override
    public List<Menu> selectMenuList(Menu menu) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        //menuName模糊查询
        queryWrapper.like(StringUtils.hasText(menu.getMenuName()),Menu::getMenuName,menu.getMenuName());
        queryWrapper.eq(StringUtils.hasText(menu.getStatus()),Menu::getStatus,menu.getStatus());
        //排序 parent_id和order_num
        queryWrapper.orderByAsc(Menu::getParentId,Menu::getOrderNum);
        List<Menu> menus = list(queryWrapper);;
        return menus;
    }

    // 删除菜单-是否存在子菜单
    @Override
    public boolean hasChild(Long menuId) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentId,menuId);
        return count(queryWrapper) != 0;
    }

    // 修改角色-根据角色id查询对应角色菜单列表树
    @Override
    public List<Long> selectMenuListByRoleId(Long roleId) {

        return getBaseMapper().selectMenuListByRoleId(roleId);
    }

    //用于把List集合里面的数据构建成tree，也就是子父菜单树，有层级关系
    private List<Menu> buildMenuTree(List<Menu> menus, long parentId) {
        //过滤找出父菜单树，也就是第一层
        //getChildren是我们在下面写的方法，用于获取子菜单的List集合
        List<Menu> menuTree = new ArrayList<>();
        for (Menu menu : menus) {
            if (menu.getParentId().equals(parentId)) {
                Menu setChildren = menu.setChildren(getChildren(menu, menus));
                menuTree.add(setChildren);
            }
        }
        return menuTree;
    }

    //用于获取传入参数的子菜单，并封装为List集合返回
    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        //通过过滤得到子菜单
        //如果有三层菜单的话，也就是子菜单的子菜单，我们就用下面那行递归(自己调用自己)来处理
        List<Menu> childrenList = new ArrayList<>();
        for (Menu m : menus) {
            if (m.getParentId().equals(menu.getId())) {
                Menu setChildren = m.setChildren(getChildren(m, menus));
                childrenList.add(setChildren);
            }
        }
        return childrenList;
    }
}
