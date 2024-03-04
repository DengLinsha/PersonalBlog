package com.dls.service;

import com.dls.domain.ResponseResult;
import com.dls.domain.entity.User;

public interface SystemLoginService {

    //登录
    ResponseResult login(User user);

    //退出登录
    ResponseResult logout();
}