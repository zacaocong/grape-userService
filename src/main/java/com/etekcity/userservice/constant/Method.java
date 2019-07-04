package com.etekcity.userservice.constant;

/**
 * method 主要是用来区分不同方法的
 *
 * @author grape
 */

public class Method {
    public static final String REGISTER = "register";
    public static final String LOGIN = "login";
    public static final String LOGOUT = "logout";
    public static final String GET_USER_INFO = "getUserInfo";
    public static final String UPDATE_USER_INFO = "updateUserInfo";
    public static final String CHANGE_PASSWORD = "changePassword";

    private Method() {
    }
}
