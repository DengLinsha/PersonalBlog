package com.dls.controller;

import com.dls.domain.ResponseResult;
import com.dls.domain.entity.Role;
import com.dls.domain.entity.User;
import com.dls.domain.vo.UserInfoAndRoleIdsVo;
import com.dls.enums.AppHttpCodeEnum;
import com.dls.exception.GlobalException;
import com.dls.service.RoleService;
import com.dls.service.UserService;
import com.dls.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/user")
public class UserController {

    @Autowired
    private UserService userService;

    //查询用户列表
    @GetMapping("/list")
    public ResponseResult list(User user, Integer pageNum, Integer pageSize) {
        return userService.selectUserPage(user,pageNum,pageSize);
    }

    // 新增用户
    @PostMapping
    public ResponseResult add(@RequestBody User user) {
        if(!StringUtils.hasText(user.getUserName())){
            throw new GlobalException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        if (!userService.checkUserNameUnique(user.getUserName())){
            throw new GlobalException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (!userService.checkEmailUnique(user)){
            throw new GlobalException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        return userService.addUser(user);
    }

    // 删除用户
    @DeleteMapping("/{userIds}")
    public ResponseResult remove(@PathVariable List<Long> userIds) {
        if(userIds.contains(SecurityUtils.getUserId())){
            return ResponseResult.errorResult(500,"不能删除当前你正在使用的用户");
        }
        userService.removeByIds(userIds);
        return ResponseResult.okResult();
    }

    //修改用户-1.根据id查询用户信息
    @Autowired
    private RoleService roleService;

    @GetMapping(value = { "/{userId}" })
    public ResponseResult getUserInfoAndRoleIds(@PathVariable(value = "userId") Long userId) {
        List<Role> roles = roleService.selectRoleAll();
        User user = userService.getById(userId);
        //当前用户所具有的角色id列表
        List<Long> roleIds = roleService.selectRoleIdByUserId(userId);

        UserInfoAndRoleIdsVo vo = new UserInfoAndRoleIdsVo(user,roles,roleIds);
        return ResponseResult.okResult(vo);
    }

    //修改用户-2.更新用户信息
    @PutMapping
    public ResponseResult edit(@RequestBody User user) {
        userService.updateUser(user);
        return ResponseResult.okResult();
    }
}