package com.etekcity.userservice.utils;

/**
 * 校验传参合法性
 *
 * @author grape
 * */
public class CheckUtils {
    /**
     * 邮箱正则
     */
    private static final String EMAIL_REG = "^([a-zA-Z0-9_\\.%\\+\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$";

    /**
     * 密码正则
     */
    private static final String PASSWORD_REG = "^[0-9a-zA-Z\\_]+$";
    /**
     * Authorization正则
     */
    private static final String AUTHOR_REG = "[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}\\s+[a-z0-9]"
            + "{32}";

    private static final int MIN_EMAIL_LENGTH = 6;
    private static final int MAX_EMAIL_LENGTH = 64;
    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MAX_PASSWORD_LENGTH = 20;
    private static final int MAX_NICKNAME_LENGTH = 32;
    private static final int MAX_ADDRESS_LENGTH = 255;

    /**
     * 校验邮箱规则
     *
     * @param email not null
     * @return boolean  符合要求;1 邮箱长度不符合要求;2 邮箱格式错误
     */
    public static boolean checkEmail(String email) {
        if (email.length() > MAX_EMAIL_LENGTH || email.length() < MIN_EMAIL_LENGTH) {
            return false;
        }
        return email.matches(EMAIL_REG);
    }


    /**
     * 校验密码规则
     *
     * @param password not null
     * @return boolean
     */
    public static boolean checkPassword(String password) {
        //这里不是密码正则，是范围ASCII
        if (password.length() > MAX_PASSWORD_LENGTH || password.length() < MIN_PASSWORD_LENGTH) {
            return false;
        }
        char[] chars = password.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if ((int) chars[i] < 33 || (int) chars[i] > 126) {
                return false;
            }
        }
        return true;
    }

    /**
     * 昵称32且支持emoji ,支持可在类中或Impl中实现
     */
    public static boolean checkNickname(String nickname) {
        return nickname.length() < MAX_NICKNAME_LENGTH;
    }

    /**
     * 地址255
     */
    public static boolean checkAddress(String address) {
        return address.length() < MAX_ADDRESS_LENGTH;
    }

    /**
     * 校验Authorization是否符合规范
     */
    public static boolean checkAuthor(String authorization) {
        return authorization.matches(AUTHOR_REG);
    }
}






















