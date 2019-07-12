package com.etekcity.userservice.request;

import javax.validation.constraints.NotEmpty;

/**
 * 更新用户信息请求消息体
 *
 * @author grape
 */
public class UpdateUserInfoBody {

    private String nickname;

    private String address;

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


}
