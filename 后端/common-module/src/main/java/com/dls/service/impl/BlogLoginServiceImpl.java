package com.dls.service.impl;

import com.dls.domain.ResponseResult;
import com.dls.domain.entity.LoginUser;
import com.dls.domain.entity.User;
import com.dls.domain.vo.BlogUserLoginVo;
import com.dls.domain.vo.UserInfoVo;
import com.dls.service.BlogLoginService;
import com.dls.utils.BeanCopyUtils;
import com.dls.utils.JwtUtil;
import com.dls.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class BlogLoginServiceImpl implements BlogLoginService {

    @Autowired
    //AuthenticationManager是security官方提供的接口
    private AuthenticationManager authenticationManager;

    @Autowired
    //RedisCache是我们在common-module工程的config目录写的类
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        //封装登录的用户名和密码
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        //在下一行之前，封装的数据会先走UserDetailsServiceImpl实现类
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //需要判断用户认证是否通过，如果authenticate的值是null，就说明认证没有通过
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        //获取userid
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        //把这个userid通过我们写的JwtUtil工具类转成密文，这个密文就是token值
        String jwt = JwtUtil.createJWT(userId);

        //第一个参数: 存到时候是键值对的形式，key要加上 "bloglogin:" 前缀
        //第二个参数: 要把哪个对象存入Redis。
        redisCache.setCacheObject("bloglogin:"+userId,loginUser);


        //把User转化为UserInfoVo，再放入vo对象的第二个参数
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        BlogUserLoginVo blogUserLoginVo = new BlogUserLoginVo(jwt,userInfoVo);
        //封装响应返回
        return ResponseResult.okResult(blogUserLoginVo);
    }

    @Override
    public ResponseResult logout() {

        // 删除redis中的用户信息
        // 解析并获取userId
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        //获取userid
        Long userid = loginUser.getUser().getId();

        //在redis根据key来删除用户的value值，key加了'bloglogin:'前缀
        redisCache.deleteObject("bloglogin:"+userid);
        //封装响应返回
        return ResponseResult.okResult();
    }
}
