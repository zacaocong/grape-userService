package com.grape.cloud.service.userservice;

import com.grape.cloud.response.res.RegisterRes;

public interface UserService {
    /**
     * user register interface
     *
     * @param appRequest register
     * @return response
     */
    public RegisterRes registerService(String email, String password);

}
