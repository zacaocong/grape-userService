package com.etekcity.userservice.request;

/**
 * 注册请求消息体
 *
 * @author grape
 */
public class RegisterBody {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
