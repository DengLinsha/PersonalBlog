package com.dls.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dls.dao.UserDao;
import com.dls.domain.ResponseResult;
import com.dls.domain.entity.User;
import com.dls.domain.entity.UserRole;
import com.dls.domain.vo.PageVo;
import com.dls.domain.vo.UserInfoVo;
import com.dls.domain.vo.UserVo;
import com.dls.enums.AppHttpCodeEnum;
import com.dls.exception.GlobalException;
import com.dls.service.UserRoleService;
import com.dls.service.UserService;
import com.dls.utils.BeanCopyUtils;
import com.dls.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author denglisnha
 * @since 2023-12-14 16:49:05
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {
    @Override
    //查询个人信息
    public ResponseResult userInfo() {

        //获取当前用户的用户id。
        Long userId = SecurityUtils.getUserId();

        //根据用户id查询用户信息
        User user = getById(userId);

        //封装成UserInfoVo，然后返回
        UserInfoVo vo = BeanCopyUtils.copyBean(user,UserInfoVo.class);
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }

    @Autowired
    //解密和加密用的这套算法。securityConfig类里面覆盖过PasswordEncoder这个bean
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseResult register(User user) {
        // 1.对数据进行非空判断
        if (!StringUtils.hasText(user.getUserName())) {
            throw new GlobalException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        //密码
        if(!StringUtils.hasText(user.getPassword())){
            throw new GlobalException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        //邮箱
        if(!StringUtils.hasText(user.getEmail())){
            throw new GlobalException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        //昵称
        if(!StringUtils.hasText(user.getNickName())){
            throw new GlobalException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        // 2.对数据进行是否存在的判断
        if(userNameExist(user.getUserName())){
            throw new GlobalException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(NickNameExist(user.getNickName())){
            throw new GlobalException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        if(EmailExist(user.getEmail())){
            throw new GlobalException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        // 3.对密码进行加密
        // 用户传给我们的密码是明文，对于密码我们要转成密文之后再存入数据库。注意加密要和解密用同一套算法
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        // 4.存入数据库
        save(user);
        return ResponseResult.okResult();
    }

    // 查询用户列表
    @Override
    public ResponseResult selectUserPage(User user, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();

        queryWrapper.like(StringUtils.hasText(user.getUserName()),User::getUserName,user.getUserName());
        queryWrapper.eq(StringUtils.hasText(user.getStatus()),User::getStatus,user.getStatus());

        Page<User> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page,queryWrapper);

        //转换成VO
        List<User> users = page.getRecords();
        List<UserVo> userVoList = new ArrayList<>();
        for (User u : users) {
            UserVo userVo = BeanCopyUtils.copyBean(u, UserVo.class);
            userVoList.add(userVo);
        }
        PageVo pageVo = new PageVo();
        pageVo.setTotal(page.getTotal());
        pageVo.setRows(userVoList);
        return ResponseResult.okResult(pageVo);
    }

    // 新增用户

    @Autowired
    private UserRoleService userRoleService;

    @Override
    public boolean checkUserNameUnique(String userName) {
        return count(Wrappers.<User>lambdaQuery().eq(User::getUserName,userName))==0;
    }

    @Override
    public boolean checkEmailUnique(User user) {
        return count(Wrappers.<User>lambdaQuery().eq(User::getEmail,user.getEmail()))==0;
    }

    @Override
    @Transactional
    public ResponseResult addUser(User user) {
        //密码加密处理
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        save(user);

        if(user.getRoleIds()!=null&&user.getRoleIds().length>0){
            insertUserRole(user);
        }
        return ResponseResult.okResult();
    }

    //修改用户-2.更新用户信息
    @Override
    @Transactional
    public void updateUser(User user) {
        // 删除用户与角色关联
        LambdaQueryWrapper<UserRole> userRoleUpdateWrapper = new LambdaQueryWrapper<>();
        userRoleUpdateWrapper.eq(UserRole::getUserId,user.getId());
        userRoleService.remove(userRoleUpdateWrapper);

        // 新增用户与角色管理
        insertUserRole(user);
        // 更新用户信息
        updateById(user);
    }

    private void insertUserRole(User user) {
        List<UserRole> sysUserRoles = Arrays.stream(user.getRoleIds())
                .map(roleId -> new UserRole(user.getId(), roleId)).collect(Collectors.toList());
        userRoleService.saveBatch(sysUserRoles);
    }

    //'判断用户传给我们的用户名是否在数据库已经存在' 的方法
    private boolean userNameExist(String userName) {
        //要知道是否存在，首先就是根据条件去数据库查
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        //拿用户写的用户名跟数据库里面的用户名进行比较
        queryWrapper.eq(User::getUserName,userName);
        //如果大于0就说明从数据库查出来了，也就说明是已经存在数据库的
        boolean result = count(queryWrapper) > 0;
        //为true就说明已存在
        return result;
    }

    //'判断用户传给我们的昵称是否在数据库已经存在' 的方法
    private boolean NickNameExist(String nickName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName,nickName);
        boolean result = count(queryWrapper) > 0;
        return result;
    }

    //'判断用户传给我们的邮箱是否在数据库已经存在' 的方法
    private boolean EmailExist(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail,email);
        boolean result = count(queryWrapper) > 0;
        return result;
    }

//    //'判断用户传给我们的手机号码是否在数据库已经存在' 的方法
//    private boolean PhonenumberExist(String Phonenumber) {
//        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(User::getPhonenumber,Phonenumber);
//        boolean result = count(queryWrapper) > 0;
//        return result;
//    }
}
