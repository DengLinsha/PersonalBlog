package com.dls.controller;

import com.dls.domain.ResponseResult;
import com.dls.domain.entity.LoginUser;
import com.dls.domain.entity.Menu;
import com.dls.domain.entity.User;
import com.dls.domain.vo.AdminUserInfoVo;
import com.dls.domain.vo.RoutersVo;
import com.dls.domain.vo.UserInfoVo;
import com.dls.enums.AppHttpCodeEnum;
import com.dls.exception.GlobalException;
import com.dls.service.MenuService;
import com.dls.service.RoleService;
import com.dls.service.SystemLoginService;
import com.dls.utils.BeanCopyUtils;
import com.dls.utils.RedisCache;
import com.dls.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoginController {

    @Autowired
    private SystemLoginService systemLoginService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleService roleService;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUserName())){
            //提示 必须要传用户名
            throw new GlobalException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return systemLoginService.login(user);
    }

    // 退出登录的接口（写在service中比较好）
    @Autowired
    private RedisCache redisCache;

    @PostMapping("/user/logout")
    public ResponseResult logout(){
        //退出登录
        return systemLoginService.logout();
    }

    //查询(超级管理员|非超级管理员)的权限和角色信息
    @GetMapping("getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo(){
        // 1.获取当前登录的用户。
        LoginUser loginUser = SecurityUtils.getLoginUser();
        // 2.根据用户id查询权限信息
        List<String> perms = menuService.selectPermsByUserId(loginUser.getUser().getId());
        // 3.根据用户id查询角色信息
        List<String> roleKeyList = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());

        //获取用户信息
        User user = loginUser.getUser();
        //BeanCopyUtils是写的类
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);

        //封装响应返回
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms,roleKeyList,userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }


    @GetMapping("getRouters")
    public ResponseResult<RoutersVo> getRouters() {
        //获取用户id
        Long userId = SecurityUtils.getUserId();

        // 1.查询menu 结果是tree（层级）的形式
        List<Menu> menus = menuService.selectRouterMenuTreeByUserId(userId);
        // 2.封装数据返回
        return ResponseResult.okResult(new RoutersVo(menus));
    }


}