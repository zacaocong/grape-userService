package com.etekcity.userservice.controller;

import com.etekcity.userservice.utils.StringUtils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RestController
public class TestController {

    @RequestMapping(value = "/userId",method = RequestMethod.POST)
    public String testGetHeaderUserIdByRequest(@RequestHeader Map<String,String> headers,
                                      HttpServletRequest request) {
        return StringUtils.splitStrings(request.getHeader("X-Authorization"))[0];
    }
    @RequestMapping(value = "/userIdByHeaders",method = RequestMethod.POST)
    public String testGetHeaderUserIdByHeaders(@RequestHeader Map<String,String> headers,
                                      HttpServletRequest request) {
        return StringUtils.splitStrings(headers.get("X-Authorization"))[0];
    }

    @RequestMapping(value = "/token",method = RequestMethod.POST)
    public String testGetHeaderTokenByRequest(@RequestHeader Map<String,String> headers,
                                     HttpServletRequest request) {
        return StringUtils.splitStrings(request.getHeader("X-Authorization"))[1];
    }
    @RequestMapping(value = "/tokenByHeaders",method = RequestMethod.POST)
    public String testGetHeaderTokenByHeaders(@RequestHeader Map<String,String> headers,
                                     HttpServletRequest request) {
        return StringUtils.splitStrings(headers.get("X-Authorization"))[1];
    }
    @RequestMapping("/time")
    public String testTime() {
        //当前时间标准格式输出
        Date now = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return ft.format(now);
    }
}
























