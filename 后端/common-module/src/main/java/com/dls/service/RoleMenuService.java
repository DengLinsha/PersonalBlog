package com.dls.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dls.domain.entity.RoleMenu;

/**
 * 角色和菜单关联表(RoleMenu)表服务接口
 *
 * @author denglinsha
 * @since 2023-12-18 21:29:56
 */
public interface RoleMenuService extends IService<RoleMenu> {

    //修改角色-保存修改好的角色信息
    void deleteRoleMenuByRoleId(Long id);
}
