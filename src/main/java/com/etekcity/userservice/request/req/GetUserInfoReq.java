package com.etekcity.userservice.request.req;


//我认为req没有存在的必要
public class GetUserInfoReq {
    private String Authorization;

    public GetUserInfoReq(String authorization) {
        Authorization = authorization;
    }

    public String getAuthorization() {
        return Authorization;
    }

    public void setAuthorization(String authorization) {
        Authorization = authorization;
    }
}
