package com.dls.controller;

import com.dls.annotation.mySystemlog;
import com.dls.domain.ResponseResult;
import com.dls.domain.entity.User;
import com.dls.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    //UserService是写的接口
    private UserService userService;

    @GetMapping("/userInfo")
    @mySystemlog(businessName = "查询个人信息")//接口描述，用于'日志记录'功能
    public ResponseResult userInfo(){
        return userService.userInfo();
    }

    @PutMapping("userInfo")
    public ResponseResult  updateUserInfo(@RequestBody User user){
        //更新个人信息
        return userService.updateUserInfo(user);
    }

    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user){
        //注册功能
        return userService.register(user);
    }
}