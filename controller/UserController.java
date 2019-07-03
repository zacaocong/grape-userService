package com.etekcity.userservice.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.etekcity.userservice.request.ChangePasswordBody;
import com.etekcity.userservice.request.RegisterAndLoginBody;
import com.etekcity.userservice.request.UpdateUserInfoBody;
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

    @PostMapping("/register")
    public Response registerControl(@RequestBody RegisterAndLoginBody requestBody) {

        return userService.register(requestBody);
    }

    @PostMapping("/login")
    public Response loginControl(@RequestBody RegisterAndLoginBody requestBody) {
        return userService.login(requestBody);
    }

    @PostMapping("/logout")
    public Response logoutControl(HttpServletRequest request) {
        return userService.logout(request);
    }

    @PostMapping("/getUserInfo")
    public Response getUserInfoControl(HttpServletRequest request) {
        return userService.getUserInfo(request);
    }

    @PostMapping("/updateUserInfo")
    public Response updateUserInfoControl(@RequestBody UpdateUserInfoBody requestBody, HttpServletRequest request) {
        return userService.updateUserInfo(request, requestBody);
    }

    @PostMapping("/changePassword")
    public Response changePasswordControl(@RequestBody ChangePasswordBody requestBody, HttpServletRequest request) {
        return userService.changePassword(request, requestBody);
    }

}
