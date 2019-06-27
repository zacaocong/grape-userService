package com.etekcity.userservice.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密工具
 * @author grape
 * */
public class MD5Utils {

    /**
     * 返回传入字符串加密后的值
     * @param str       传入字符串
     * @return String
     * */
    public static String getMD5Str(String str) throws NoSuchAlgorithmException {

        // 生成一个MD5加密计算摘要
        MessageDigest md = MessageDigest.getInstance("MD5");
        // 计算md5函数
        md.update(str.getBytes());
        byte[] hash = md.digest();
        StringBuilder secpwd = new StringBuilder();
        //todo:foreach写法
        for (int i = 0; i < hash.length; i++) {
            int v = hash[i] & 0xFF;
            if (v < 16) {
                secpwd.append(0);
            }
            secpwd.append(Integer.toString(v, 16));
        }
        return secpwd.toString();

    }
}
