package com.grape.cloud.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 作者 Grape:
 * @version 创建时间:2019年6月2日
 */
@RestController
@RequestMapping("/userService/v1/user/")
public class CheckAliveController {
    @RequestMapping("healthy")
    public String checkAlive(){
        return "cloud-userService is healthy";
    }
}
