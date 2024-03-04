package com.dls.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dls.domain.entity.Role;

import java.util.List;

/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author denglinsha
 * @since 2023-12-15 20:59:28
 */
public interface RoleDao extends BaseMapper<Role> {
    //查询普通用户的角色权限
    List<String> selectRoleKeyByOtherUserId(Long userId);

    //修改用户-1.根据id查询用户信息
    List<Long> selectRoleIdByUserId(Long userId);
}
