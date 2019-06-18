package com.etekcity.userservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.etekcity.userservice.request.*;
import com.etekcity.userservice.response.rsp.Response;
import com.etekcity.userservice.service.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;

@RestController
//@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public Response registerControl(@RequestBody RegisterBody requestBody) {

        return userService.register(requestBody);
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public Response loginControl(@RequestBody LoginBody requestBody) {
        return userService.login(requestBody);
    }

    @RequestMapping(value = "/logout",method = RequestMethod.POST)
    public Response logoutControl( HttpServletRequest request) {
        return userService.logout(request);
    }

    @RequestMapping(value = "/getUserInfo",method = RequestMethod.POST)
    public Response getUserInfoControl( HttpServletRequest request) {
        return userService.getUserInfo(request);
    }

    @RequestMapping(value = "/updateUserInfo",method = RequestMethod.POST)
    public Response updateUserInfoControl(@RequestBody UpdateUserInfoBody requestBody, HttpServletRequest request) {
        return userService.updateUserInfo(requestBody,request);
    }

    @RequestMapping(value = "/changePassword",method = RequestMethod.POST)
    public Response changePasswordControl(@RequestBody ChangePasswordBody requestBody, HttpServletRequest request) {
        return userService.changePassword(requestBody,request);
    }

}
