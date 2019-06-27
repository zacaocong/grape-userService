package com.etekcity.userservice.service;

import javax.servlet.http.HttpServletRequest;

import com.etekcity.userservice.request.*;
import com.etekcity.userservice.response.rsp.Response;

public interface UserService {
    /**
     * 注册
     * @param requestBody requestBody
     * @return Response
     * */
    Response register(RegisterAndLoginBody requestBody) throws Exception;
    //todo:抛出异常处理

    /**
     * 登录
     * @param requestBody requestBody
     * @return Response
     * */
    Response login(RegisterAndLoginBody requestBody);

    /**
     * 登出
     * @param request http请求，这里主要是为了获取请求消息头
     * @return Response
     * */
    Response logout(HttpServletRequest request);

    /**
     * 获取用户信息
     * @param request http请求，这里主要是为了获取请求消息头
     * @return Response
     * */
    Response getUserInfo(HttpServletRequest request);

    /**
     * 更新用户信息
     * @param requestBody       requestBody
     * @param request           request
     * @return Response
     * */
    Response updateUserInfo(HttpServletRequest request, UpdateUserInfoBody requestBody);

    /**
     * 修改密码
     * @param requestBody       requestBody
     * @param request           request
     * @return Response
     * */
    Response changePassword(HttpServletRequest request,ChangePasswordBody requestBody);

}
