package com.dls.utils;

import com.dls.domain.entity.Menu;
import com.dls.domain.vo.MenuTreeVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

//新增角色-获取菜单下拉树列表
public class SystemConverter {

    private SystemConverter() {}

    public static List<MenuTreeVo> buildMenuSelectTree(List<Menu> menus) {
        List<MenuTreeVo> MenuTreeVos = new ArrayList<>();
        for (Menu m : menus) {
            MenuTreeVo menuTreeVo = new MenuTreeVo(m.getId(), m.getMenuName(), m.getParentId(), null);
            MenuTreeVos.add(menuTreeVo);
        }
        List<MenuTreeVo> options = new ArrayList<>();
        for (MenuTreeVo o : MenuTreeVos) {
            // 父节点
            if (o.getParentId().equals(0L)) {
                MenuTreeVo menuTreeVo = o.setChildren(getChildList(MenuTreeVos, o));
                options.add(menuTreeVo);
            }
        }
        return options;
    }

    /**
     * 得到子节点列表
     */
    private static List<MenuTreeVo> getChildList(List<MenuTreeVo> list, MenuTreeVo option) {
        List<MenuTreeVo> options = new ArrayList<>();
        for (MenuTreeVo o : list) {
            if (Objects.equals(o.getParentId(), option.getId())) {
                MenuTreeVo menuTreeVo = o.setChildren(getChildList(list, o));
                options.add(menuTreeVo);
            }
        }
        return options;

    }
}