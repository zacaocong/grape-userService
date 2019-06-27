package com.etekcity.userservice.utils;

import java.util.UUID;
/**
 * uuid32位，中间有4个“-”作为target，共36位
 * @author grape
 * */
public class UUIDUtils {
    public static String getUUID32() {
        return UUID.randomUUID().toString().toLowerCase();
    }

    private UUIDUtils() {

    }
}
