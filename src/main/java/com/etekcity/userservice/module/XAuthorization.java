package com.etekcity.userservice.module;

public class XAuthorization {
    private String userId;
    private String token;
    private Integer count;

    public XAuthorization(){

    }

    public XAuthorization(String userId,String token) {
        this.userId = userId;
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "XAuthorization:{" + "token:" + token + "userID:" + userId + "}";
    }

}
