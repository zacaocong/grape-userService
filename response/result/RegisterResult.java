package com.etekcity.userservice.response.result;

/**
 * 注册响应结果
 *
 * @author grape
 */
public class RegisterResult {
    private String userId;
    private String createAt;

    public RegisterResult(String userId, String createAt) {
        this.userId = userId;
        this.createAt = createAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

}
