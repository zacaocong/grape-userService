package com.etekcity.userservice.controller;


import com.etekcity.userservice.moudle.X_Authorization;
import com.etekcity.userservice.util.GetUUID;
import com.etekcity.userservice.util.RedisUtils;
import com.etekcity.userservice.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;//用的是自带的
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
public class RedisController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //------------------------------StringRedis---------------------------------
    //------------------------------添加数据
    @GetMapping("/addStringToRedis")
    @ResponseBody
    public Boolean addStringToRedis(String key,String value){
        //http://localhost:8080/addStringToRedis?key="hellokey"&value="worldvalue"
        stringRedisTemplate.opsForValue().set(key,value);
        return true;
    }//成功
    //------------------------------查数据
    @GetMapping("/getStringFromRedis")
    @ResponseBody
    public String getStringFromRedis(String key){
        //http://localhost:8080/getStringFromRedis?key="hellokey"
        return stringRedisTemplate.opsForValue().get(key);
    }//成功
    /**
     *     "name",
     *     "79204072381b4c53804d983ea8e2c587",
     *     "\"hellokey\""
     * */

    //-----------------------------Redis Token--------------------------------
    @Autowired
    private RedisUtils redisUtils;

    //----------------------------创建一个发过去，添加数据
    @GetMapping("/redistoken")
    @ResponseBody
    public Object testToken(String key){//直接写参数，返回Object这都特么行
        String user_id = GetUUID.getUUID32();
        String token = TokenUtils.getUUToken();
        X_Authorization x_authorization = new X_Authorization();
        x_authorization.setUserId(user_id);
        x_authorization.setToken(token);
        redisUtils.set(token,x_authorization,120L);//这个时间是有效的

        return redisUtils.get(token);
    }//通过
    //查看库中全部的键值
    @RequestMapping("redisKeys")
    public Set redisTest() {
        return redisUtils.redisKeys();
    }//通过
    //------------------------------查数据
    //根据key查看该值
    @GetMapping("/getByKey")
    @ResponseBody
    public Object getRedis(String key){
        //localhost:8080/getByKey?key=79204072381b4c53804d983ea8e2c587
        return redisUtils.get(key);
    }
    //-----------------------------删数据



}