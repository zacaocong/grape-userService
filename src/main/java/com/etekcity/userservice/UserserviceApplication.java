package com.etekcity.userservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@MapperScan("com.etekcity.userservice.dao.mapper")
public class UserserviceApplication {

    @Bean
    public RestTemplate restTemplate() {//这是什么？，加了它我的config就不报错了
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(UserserviceApplication.class, args);
    }

}
