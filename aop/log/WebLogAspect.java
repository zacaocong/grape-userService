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
    @Pointcut("execution(public * com.etekcity.userservice.controller..*.*(..))")
    public void webLog() {

    }

    /**
     * 统一日志
     */
    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        //开始打印请求日志
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //todo:头部消息打印，这种方法还是不能准确解决问题，先放一放吧,根据方法去做操作还是算了吧
        //打印请求
        if (proceedingJoinPoint.getArgs().length > 1) {
            log.info("===========================Request===========================\nHTTP Method   : {}\n"
                            + "URL           : {}\nRequest Args  : {}",
                    request.getMethod(), request.getRequestURL().toString(),
                    new Gson().toJson(proceedingJoinPoint.getArgs()[0]));
        }
        if (proceedingJoinPoint.getArgs().length == 1) {
            log.info("===========================Request===========================\nHTTP Method   : {}\n"
                            + "URL           : {}",
                    request.getMethod(), request.getRequestURL().toString());
        }
        Object result = proceedingJoinPoint.proceed();
        //打印出参
        log.info("===========================Response===========================\nResponse Args : {}",
                JSONObject.toJSONString(result));
        //执行耗时
        log.info("Time-Consuming: {} ms", System.currentTimeMillis() - startTime);
        return result;
    }

}



















