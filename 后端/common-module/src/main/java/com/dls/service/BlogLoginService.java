package com.dls.service;

import com.dls.domain.ResponseResult;
import com.dls.domain.entity.User;

public interface BlogLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
