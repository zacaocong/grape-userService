package com.etekcity.userservice.controller;


import com.etekcity.userservice.request.body.*;
import com.etekcity.userservice.request.req.RegisterReq;
import com.etekcity.userservice.response.res.*;
import com.etekcity.userservice.service.UserService;
import com.etekcity.userservice.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    //-----------------------------------test
    @RequestMapping("/hello")
    public String hello(){
        return "helloworld";
    }//postman崩了、、hello都哈不出来


    //-----------------------------------注册
    /*
    * {
        "emali":"example@etekcity.com.cn",
        "password":"KC=~pwcnJ7"
        }
    * */
    @RequestMapping("/register")
    public RegisterRes RegisterController(@RequestBody RegisterReq request){
        return userService.registerService(request.getEmail(),request.getPassword());
    }//失败的原因是userid是32位加四个-，一共36位，数据库只有32位，所以失败
    @RequestMapping("/registers")
    public String RegisterControllers(@RequestBody RegisterReq request){
        return request.getEmail();
    }
    @RequestMapping("/registerp")
    public String RegisterControllerp(@RequestBody RegisterReq request){
        return request.getPassword();
    }

    //----------------------------------登录
    @RequestMapping("/login")
    public LoginRes LoginController(@RequestBody LoginBody request){
        return userService.loginService(request.getEmail(),request.getPassword());
    }
    //----------------------------------登出
    @RequestMapping("/logout")
    public LogoutRes LogoutController(@RequestBody LogoutBody request){
        return userService.logoutService(request.getToken(),request.getUserId());//????
    }
    //----------------------------------获取用户信息
    @RequestMapping("/getuserinfo")
    public GetUserInfoRes GetUserInfoController(@RequestBody GetUserInfoBody request){
        return userService.getUserInfoService(request.getToken(),request.getUserId());
    }
    //----------------------------------更新用户信息
    @RequestMapping("/updateuserinfo")
    public UpdateUserInfoRes UpdateUserInfoConreller(@RequestBody UpdateUserInfoBody request){
        return userService.updateUserInfoService(request.getToken(),request.getUserId(),request.getNickname(),request.getAddress());
    }
    //----------------------------------更新用户密码
    @RequestMapping("/changepassword")
    public ChangePasswordRes ChangePasswordController(@RequestBody ChangePasswordBody request){
        return userService.changePasswordService(request.getToken(),request.getUserId(),request.getOldPassword(),request.getNewPassword());
    }


}
