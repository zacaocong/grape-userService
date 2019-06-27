package com.etekcity.userservice.redis.entity;

/**
 * 用户权限，封装token和userId，作为redis键值对中的值
 * @author grape
 * */
public class AuthorizationValue {

    private String userId;
    private String token;
    private Integer count;

    public AuthorizationValue(){

    }

    public AuthorizationValue(String userId, String token) {
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
        return "AuthorizationValue:{" + "token:" + token + "userID:" + userId + "}";
    }

}
