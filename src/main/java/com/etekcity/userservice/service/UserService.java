package com.etekcity.userservice.service;

import com.etekcity.userservice.request.*;
import com.etekcity.userservice.response.rsp.Response;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    /**
     * 注册
     * */
    Response register(RegisterBody requestBody) throws Exception;

    /**
     * 登录
     * */
    Response login(LoginBody requestBody);

    /**
     * 登出
     * */
    Response logout(HttpServletRequest request);

    /**
     * 获取用户信息
     * */
    Response getUserInfo(HttpServletRequest request);

    /**
     * 更新用户信息
     * */
    Response updateUserInfo(UpdateUserInfoBody requestBody, HttpServletRequest request);

    /**
     * 修改密码
     * */
    Response changePassword(ChangePasswordBody requestBody, HttpServletRequest request);

}
