package com.grape.cloud.response.result;

import java.sql.Timestamp;

public class RegisterResult {
    private String userId;
    private Timestamp createAt;

    public RegisterResult(String userId, Timestamp createAt){
        this.userId = userId;
        this.createAt = createAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }
}
