package com.etekcity.userservice.aop.annotation;

import java.lang.annotation.*;

/**
 * 自定义注解，标识需要校验token的方法
 *
 * @author grape
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckToken {
    String value() default "";
}













