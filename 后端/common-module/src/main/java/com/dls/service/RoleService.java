package com.dls.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dls.domain.ResponseResult;
import com.dls.domain.entity.Role;

import java.util.List;

/**
 * 角色信息表(Role)表服务接口
 *
 * @author denglinsha
 * @since 2023-12-15 20:57:42
 */
public interface RoleService extends IService<Role> {
    //查询用户的角色信息
    List<String> selectRoleKeyByUserId(Long id);

    //查询角色列表
    ResponseResult selectRolePage(Role role, Integer pageNum, Integer pageSize);

    //新增角色
    void insertRole(Role role);

    // 修改角色-保存修改好的角色信息
    void updateRole(Role role);

    // 新增用户-1.查询角色列表接口
    List<Role> selectRoleAll();

    //修改用户-1.根据id查询用户信息
    List<Long> selectRoleIdByUserId(Long userId);
}
