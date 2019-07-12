package com.etekcity.userservice.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.etekcity.userservice.constant.MediaType;
import com.etekcity.userservice.request.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.etekcity.userservice.response.rsp.Response;
import com.etekcity.userservice.service.impl.UserServiceImpl;

/**
 * UserController,接收分发用户请求
 *
 * @author grape
 * @since 0.0.1
 */
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @PostMapping(value = "/register", produces = MediaType.APP_JSON, consumes = MediaType.APP_JSON)
    public Response registerControl(HttpServletRequest request, @RequestBody @Valid RegisterBody requestBody) {
        return userService.register(requestBody);
    }

    @PostMapping(value = "/login", produces = MediaType.APP_JSON, consumes = MediaType.APP_JSON)
    public Response loginControl(HttpServletRequest request, @RequestBody @Valid LoginBody requestBody) {
        return userService.login(requestBody);
    }

    @PostMapping(value = "/logout", produces = MediaType.APP_JSON, consumes = MediaType.APP_JSON)
    public Response logoutControl(HttpServletRequest request) {
        return userService.logout(request);
    }

    @PostMapping(value = "/getUserInfo", produces = MediaType.APP_JSON, consumes = MediaType.APP_JSON)
    public Response getUserInfoControl(HttpServletRequest request) {
        return userService.getUserInfo(request);
    }

    @PostMapping(value = "/updateUserInfo", produces = MediaType.APP_JSON, consumes = MediaType.APP_JSON)
    public Response updateUserInfoControl(HttpServletRequest request, @RequestBody @Valid UpdateUserInfoBody
            requestBody) {
        return userService.updateUserInfo(request, requestBody);
    }

    @PostMapping(value = "/changePassword", produces = MediaType.APP_JSON, consumes = MediaType.APP_JSON)
    public Response changePasswordControl(HttpServletRequest request, @RequestBody @Valid ChangePasswordBody
            requestBody) {
        return userService.changePassword(request, requestBody);
    }

}
