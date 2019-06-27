package com.etekcity.userservice.utils;

/**
 * 字符串处理，需要接收request中的X_Authorization将其通过空白符分割成userId和token
 * 前面的是userId后面是token
 * @author grape
 * */
public class StringUtils {
    private StringUtils() {

    }

    public static String[] splitStrings(String X_Authorization) {
        String[] arr = X_Authorization.split("\\s+");
        return arr;
    }

}
