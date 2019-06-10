package com.etekcity.userservice.request.req;

public class LogoutReq {
    private String Authorization;

    public LogoutReq(String authorization) {
        Authorization = authorization;
    }

    public String getAuthorization() {
        return Authorization;
    }

    public void setAuthorization(String authorization) {
        Authorization = authorization;
    }
}
