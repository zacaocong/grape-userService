package com.etekcity.userservice.response.result;

/**
 * 获取用户信息响应消息的结果
 *
 * @author grape
 */
public class GetUserInfoResult {
    private String userId;
    private String email;
    private String nickname;
    private String address;
    private String createAt;
    private String updateAt;

    public GetUserInfoResult() {

    }

    /**
     * 传参构造方法
     *
     * @param userId   userId
     * @param email    email
     * @param nickname nickname
     * @param address  address
     * @param createAt createAt
     * @param updateAt updateAt
     */
    public GetUserInfoResult(String userId, String email, String nickname, String address,
                             String createAt, String updateAt) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.address = address;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

}
