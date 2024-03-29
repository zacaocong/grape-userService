package com.etekcity.userservice.model;

import java.util.Date;

/**
 * User用户类，存储用户相关属性，与数据库中user_info对应
 *
 * @author grape
 * @since 0.0.1
 */
public class User {
    /**
     * 数据库（user_id, email password address nickname create_at,update_at）
     */
    private String userId;
    private String email;
    private String password;
    private String address;
    private String nickname;
    private Date createAt;
    private Date updateAt;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

}
