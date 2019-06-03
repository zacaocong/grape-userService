package com.grape.cloud.request.req;

import org.apache.tomcat.util.http.parser.Authorization;

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
