package com.etekcity.userservice.aop.log;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 统一打印日志
 *
 * @author grape
 */
@Aspect
@Component
@Slf4j
public class WebLogAspect {

    Gson gson = new Gson();

    @Pointcut("execution(public * com.etekcity.userservice.controller..*.*(..))")
    public void webLog() {

    }

    /**
     * 统一日志
     */
    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        //开始打印请求日志
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //打印请求
        //参数列表长度大于1证明有body
        if (proceedingJoinPoint.getArgs().length > 1) {
            log.info("Request HTTP Method   : {} URL           : {} Request Args  : {}",
                    request.getMethod(), request.getRequestURL().toString(),
                    new Gson().toJson(proceedingJoinPoint.getArgs()[1]));
        }
        //参数列表为1没有body，打一下request就行了
        if (proceedingJoinPoint.getArgs().length == 1) {
            log.info("Request HTTP Method   : {} URL           : {}",
                    request.getMethod(), request.getRequestURL().toString());
        }

        Object result = proceedingJoinPoint.proceed();
        //打印出参
        log.info("Response Response Args : {}", JSONObject.toJSONString(result));
        return result;
    }

}



















