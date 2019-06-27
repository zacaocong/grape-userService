package com.etekcity.userservice.aop.log;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.RequestFacade;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Aspect
@Component
@Slf4j
public class WebLogAspect {
    @Pointcut("execution(public * com.etekcity.userservice.controller..*.*(..))")
    public void webLog() {

    }
    /**
     * 在切点之前织入
     * @param joinPoint
     *
     * */
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        //开始打印请求日志
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        //打印请求相关参数
        log.info("==========================Start===========================");
        //打印请求 url
        log.info("URL           : {}",request.getRequestURL().toString());
        //打印 Http method
        log.info("HTTP Method   : {}",request.getMethod());
        //打印调用 controller 的全路径以及执行方法
        log.info("Class Method  : {}.{}",joinPoint.getSignature().getDeclaringTypeName(),
        joinPoint.getSignature().getName());
        //打印请求的 IP
        log.info("IP            : {}",request.getRemoteAddr());
        //打印请求入参,这里我就跟奇怪，用joinpoint不用request,打印body，但是有的没有body
//        log.info("Request Args  : {}", joinPoint.getArgs()[0].toString());
//        log.info("Request Args  : {}", new Gson().toJson(joinPoint.getArgs()[0]));
//        log.info("-___-------- : {}", JSONObject.toJSONString(joinPoint.getArgs()));

    }
    @After("webLog()")
    public void doAfter() {
        log.info("===========================End============================");
        //每个请求之间空一行
        log.info("");
    }
    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        //打印出参
        log.info("Response Args : {}",JSONObject.toJSONString(result));
        //执行耗时
        log.info("Time-Consuming: {} ms", System.currentTimeMillis() - startTime);
        /*
        * result 作为Object，赋值后变成了response
        * */
        log.info("result class  : {}",result.getClass());
        return result;
    }





}



















