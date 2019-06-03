package com.grape.cloud.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.Date;

@RestController
public class TimeController {

    @RequestMapping("/time")
    public Timestamp testJSON(){
        return new Timestamp(new Date().getTime());
    }//时间也是对的。那是什么错了？,复合类出了问题


}
