package com.dls.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dls.dao.UserRoleDao;
import com.dls.domain.entity.UserRole;
import com.dls.service.UserRoleService;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleDao, UserRole> implements UserRoleService {

}