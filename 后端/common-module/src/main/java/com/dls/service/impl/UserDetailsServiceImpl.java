package com.dls.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dls.constants.SystemConstants;
import com.dls.dao.MenuDao;
import com.dls.dao.UserDao;
import com.dls.domain.entity.LoginUser;
import com.dls.domain.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

//当BlogLoginServiceImpl类封装好登录的用户名和密码之后，就会传到当前这个实现类
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserDao userDao;
    private String userName;

    @Autowired
    private MenuDao menuDao;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        this.userName = userName;
        // 根据用户名查询用户信息
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUserName, userName);
        User user = userDao.selectOne(userLambdaQueryWrapper);
        // 如果没查到用户信息则抛出异常
        if (Objects.isNull(user)) {
            throw new RuntimeException("用户不存在");
        }
        // 返回用户信息
        // 如果是管理员用户，才需要查询权限，也就是只对管理员用户做权限校验
        if(user.getType().equals(SystemConstants.IS_ADMAIN)){
            //根据用户id查询权限关键字，即list是权限信息的集合
            List<String> list = menuDao.selectPermsByOtherUserId(user.getId());
            return new LoginUser(user,list);
        }

        // 如果不是管理员用户，就只封装用户信息，不封装权限信息
        //返回查询到的用户信息。返回LoginUser对象
        return new LoginUser(user,null);
    }
}
