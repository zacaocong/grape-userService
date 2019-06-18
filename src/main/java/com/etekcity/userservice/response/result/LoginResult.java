package com.etekcity.userservice.response.result;

public class LoginResult {
    private String token;
    private String userId;
    private String nickname;
    private String address;
    private String email;
    private String createAt;
    private String updateAt;
    private Long expiresIn;

    public LoginResult(){

    }

public LoginResult(String token, String userId, String nickName, String address, String email,
                   String createAt, String updateAt, Long expiresIn) {
    this.token = token;
    this.userId = userId;
    this.nickname = nickName;
    this.address = address;
    this.email = email;
    this.createAt = createAt;
    this.updateAt = updateAt;
    this.expiresIn = expiresIn;
}

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
