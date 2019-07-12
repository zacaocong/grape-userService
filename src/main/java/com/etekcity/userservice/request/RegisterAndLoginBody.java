package com.etekcity.userservice.request;

import javax.validation.constraints.NotEmpty;

/**
 * 邮箱密码
 * 登录注册都用同一请求体
 *
 * @author grape
 */
public class RegisterAndLoginBody {
    @NotEmpty(message = "email 不能为空")
    private String email;
    @NotEmpty(message = "password 不能为空")
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
