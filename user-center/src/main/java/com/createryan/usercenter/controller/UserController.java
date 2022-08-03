package com.createryan.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.createryan.usercenter.model.domain.User;
import com.createryan.usercenter.model.domain.request.UserLoginRequest;
import com.createryan.usercenter.model.domain.request.UserRegisterRequest;
import com.createryan.usercenter.service.UserService;
import com.createryan.usercenter.utils.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.createryan.usercenter.constant.UserConstants.ADMIN_ROLE;
import static com.createryan.usercenter.constant.UserConstants.USER_LOGIN_STATUS;

/**
 * 用户接口
 *
 * @author: createryan
 * @date 2022/8/3 21:52
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Result userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return Result.fail("请输入用户信息！");
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return Result.fail("请输入用户信息！");
        }
        return userService.userRegister( userAccount, userPassword, checkPassword);
    }

    @PostMapping("/login")
    public Result userLogin(@RequestBody UserLoginRequest userLoginRequest,
                            HttpServletRequest request) {
        if (userLoginRequest == null) {
            return Result.fail("用户名或密码不能为空！");
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return Result.fail("用户名或密码不能为空！");
        }
        return userService.userLogin(userAccount, userPassword, request);
    }

    @GetMapping("/search")
    public Result searchUsers(String username, HttpServletRequest request) {
        Result result = checkAdmin(request);
        if (Boolean.FALSE.equals(result.getSuccess())) {
            return Result.fail(result.getErrorMsg());
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNoneBlank(username)) {
            queryWrapper.like("username", username);
        }
        return Result.ok(userService.list(queryWrapper));
    }

    @PostMapping("/delete")
    public Result deleteUser(@RequestBody long id, HttpServletRequest request) {
        Result result = checkAdmin(request);
        if (Boolean.FALSE.equals(result.getSuccess())) {
            return Result.fail(result.getErrorMsg());
        }
        if (id <= 0) {
            return Result.fail("没有找到要删除的用户！");
        }
        // removeById 逻辑删除
        return Result.ok(userService.removeById(id));
    }

    /**
     * 鉴权是否为管理员
     *
     * @param request
     * @return
     */
    private Result checkAdmin(HttpServletRequest request) {
        // 鉴权仅管理员可查询
        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATUS);
        if (user == null) {
            return Result.fail("用户信息不存在！");
        }
        if (user.getUserRole() != ADMIN_ROLE) {
            return Result.fail("你没有操作权限！");
        }
        return Result.ok();
    }
}