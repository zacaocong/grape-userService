package com.etekcity.userservice.utils;

import java.util.UUID;

/**
 * UUid token
 * */
public class TokenUtils {
    /**
     * uuid做token
     * */
    public static String getUUToken() {
        // 不是没有“-”是去掉了
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();

    }
}
