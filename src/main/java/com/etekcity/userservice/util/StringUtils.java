package com.etekcity.userservice.util;

public class StringUtils {
    //拆分字符串
    public static String[] splitString(String X_Authorization) {
        String[] arr = X_Authorization.split("\\s+");
        return arr;
    }
}