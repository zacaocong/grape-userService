package com.grape.cloud.util;

import java.security.MessageDigest;

public class MD5Utils {
    public static String getMD5Str(String str) throws Exception {

        // 生成一个MD5加密计算摘要
        MessageDigest md = MessageDigest.getInstance("MD5");
        // 计算md5函数
        md.update(str.getBytes());
        byte[] hash = md.digest();
        StringBuilder secpwd = new StringBuilder();
        for (int i = 0; i < hash.length; i++) {
            int v = hash[i] & 0xFF;
            if (v < 16) secpwd.append(0);
            secpwd.append(Integer.toString(v, 16));
        }
        return secpwd.toString();

    }
}
