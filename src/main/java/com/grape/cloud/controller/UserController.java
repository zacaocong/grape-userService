package com.grape.cloud.controller;

import com.grape.cloud.model.UserTest;
import com.grape.cloud.request.req.RegisterReq;
import com.grape.cloud.response.res.RegisterRes;
import com.grape.cloud.service.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;//由于userService调用了mapper接口，而接口只能由spring来特殊实现，所以必须注入，一开始就将其初始化

    //-------------------------------test--------------------------------------
    @RequestMapping("/testJSON")
    public UserTest testJSON(){
        return new UserTest("wangxiaohu",300);
    }//发送简单类没问题，看来是我响应类写出错了

    @RequestMapping("/requestOK")
    public RegisterReq registerrqJSON(@RequestBody RegisterReq request){
        return request;
    }//接受没问题

    //-------------------------------------------------------------------------
    /*
    * {
        "emali":"example@etekcity.com.cn",
        "password":"KC=~pwcnJ7"
        }
    * */


    @RequestMapping("/register")
    public RegisterRes RegisterController(@RequestBody RegisterReq request){
        return userService.registerService(request.getEmail(),request.getPassword());
    }

}
