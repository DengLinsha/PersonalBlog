package com.dls.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dls.domain.ResponseResult;
import com.dls.domain.entity.User;

/**
 * 用户表(User)表服务接口
 *
 * @author denglinsha
 * @since 2023-12-14 16:48:42
 */
public interface UserService extends IService<User> {
    //个人信息查询
    ResponseResult userInfo();

    //更新个人信息
    ResponseResult updateUserInfo(User user);

    //用户注册功能
    ResponseResult register(User user);

    //查询用户列表
    ResponseResult selectUserPage(User user, Integer pageNum, Integer pageSize);

    // 增加用户-2.新增用户
    boolean checkUserNameUnique(String userName);
    boolean checkEmailUnique(User user);
    ResponseResult addUser(User user);

    //修改用户-2.更新用户信息
    void updateUser(User user);
}
