package com.dls.filter;

import com.alibaba.fastjson.JSON;
import com.dls.domain.ResponseResult;
import com.dls.domain.entity.LoginUser;
import com.dls.enums.AppHttpCodeEnum;
import com.dls.utils.JwtUtil;
import com.dls.utils.RedisCache;
import com.dls.utils.WebUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
//博客前台的登录认证过滤器。OncePerRequestFilter是springsecurity提供的类
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    //RedisCache是写的工具类，用于操作redis
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取请求头中的token值
        String token = request.getHeader("token");
        //判断上面那行有没有拿到token值
        if(!StringUtils.hasText(token)){
            //说明该接口不需要登录，直接放行，不拦截
            filterChain.doFilter(request,response);
            return;
        }
        //JwtUtil是工具类。解析获取的token，得到userId。把原来的密文解析为原文
        Claims claims = null;
        try {
            // 将密文解析成原文
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            //当token超时或token被篡改就会进入下面那行的异常处理
            e.printStackTrace();
            //报异常之后，把异常响应给前端，需要重新登录。ResponseResult、AppHttpCodeEnum、WebUtils是自己写的类
            ResponseResult errorResult = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(errorResult));
            return;
        }
        // 获得其中的userid
        String userid = claims.getSubject();

        //在redis中，通过key来获取value，注意key是加过前缀的，取的时候也要加上前缀
        LoginUser loginUser = redisCache.getCacheObject("bloglogin:" + userid);
        //如果在redis获取不到值，说明登录过期，需要重新登录
        if(Objects.isNull(loginUser)){
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }

        //把从redis获取到的value，存入到SecurityContextHolder(Security官方提供的类)
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser,null,null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request,response);

    }
}