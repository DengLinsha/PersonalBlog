package com.dls.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dls.domain.entity.User;
import org.springframework.stereotype.Service;

/**
 * 用户表(User)表数据库访问层
 *
 * @author denglinsha
 * @since 2023-12-13 19:22:51
 */
@Service
public interface UserDao extends BaseMapper<User> {

}
