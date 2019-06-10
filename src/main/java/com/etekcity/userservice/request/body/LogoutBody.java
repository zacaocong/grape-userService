package com.etekcity.userservice.request.body;

public class LogoutBody {
    private String token;
    private String userId;//postman里的body里 必须和此处对应，不然无法接收

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
