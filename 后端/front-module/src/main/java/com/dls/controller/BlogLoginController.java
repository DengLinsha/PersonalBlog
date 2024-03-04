package com.dls.controller;

import com.dls.domain.ResponseResult;
import com.dls.domain.entity.User;
import com.dls.enums.AppHttpCodeEnum;
import com.dls.exception.GlobalException;
import com.dls.service.BlogLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogLoginController {

    @Autowired
    private BlogLoginService blogLoginService;

    @PostMapping("/login")
    // 获取请求体中的Json格式参数
    public ResponseResult login(@RequestBody User user) {
        if (!StringUtils.hasText(user.getUserName())) {
            // 提示要传入用户名
            throw new GlobalException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return blogLoginService.login(user);
    }

    @PostMapping("/logout")
    public ResponseResult logout() {
        return blogLoginService.logout();
    }
}
