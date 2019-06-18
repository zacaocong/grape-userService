package com.etekcity.userservice.utils;

public class CheckUtils {
    //邮箱正则
    private static final String EMAIL_REG = "^([a-zA-Z0-9_\\.%\\+\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$";

    //密码正则
    private static final String PASSWORD_REG = "^[0-9a-zA-Z\\_]+$";

    private static final int MIN_EMAIL_LENGTH = 6;
    private static final int MAX_EMAIL_LENGTH = 64;
    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MAX_PASSWORD_LENGTH = 20;

    /**
     * 校验邮箱规则
     *  @param email not null
     *  @return 0  符合要求;1 邮箱长度不符合要求;2 邮箱格式错误
     */
    public static boolean checkEmail(String email) {
        if (email.length() > MAX_EMAIL_LENGTH || email.length() < MIN_EMAIL_LENGTH) {
            return false;
        }
        return email.matches(EMAIL_REG);
    }


    /**
     * 校验密码规则
     * @param password not null
     * @return
     */
    public static boolean checkpassword(String password) {
        if (password.length() > MAX_PASSWORD_LENGTH || password.length() < MIN_PASSWORD_LENGTH) {
            return false;
        }
        return password.matches(PASSWORD_REG);
    }
}
