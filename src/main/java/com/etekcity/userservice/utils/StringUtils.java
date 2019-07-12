package com.etekcity.userservice.utils;

/**
 * 字符串处理，需要接收request中的X_Authorization将其通过空白符分割成userId和token
 * 前面的是userId后面是token
 *
 * @author grape
 */
public class StringUtils {
    private StringUtils() {

    }

    /**
     * 接收参数
     * 把一个字符串由其内部空白符分割，返回字符数组，正确的结果应该是长度为2
     * 该方法主要用来获取userId 和 token
     */
    public static String[] splitStrings(String authorization) {
        return authorization.split("\\s+");
    }

    public static String[] splitStringsByColon(String authorization) {
        return authorization.split(":");
    }

    /**
     * 接收参数
     * 把字符串内部空白符替换为空格
     * 该方法主要用来格式化 request 中的 authorization参数，以便于redis校验
     */
    public static String formatBlank(String authorization) {
        return authorization.replaceAll("\\s+", " ");
    }

    /**
     * 去掉字符串中的-
     * */
    public static String lockKeyUserId(String userId) {
        return userId.replaceAll("-", "");
    }

    /**
     * 生成redis中药存储的key ： userId token:
     */
    public static String formatCreateKey(String userId, String token) {
        return userId.concat(":").concat(token);
    }

    /**
     * 规范为冒号格式
     */
    public static String formatCreateKey(String authorization) {
        return authorization.replaceAll("\\s+", ":");
    }

}
