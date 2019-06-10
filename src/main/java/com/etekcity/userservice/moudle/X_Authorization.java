package com.etekcity.userservice.moudle;

//redis的数据类型，存到redis里
public class X_Authorization {
    //必须要实现序列化，并且有序列化版本ID
    private final long serialVersionID = 1L;

    private String userId;
    private String token;

    private Integer count;//<5

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
    public String toString(){
        return "X_Authorization:{" +
                "token:"+token+"userID:"+userId+"}";
    }
}
