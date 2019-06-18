package com.etekcity.userservice.request;

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
