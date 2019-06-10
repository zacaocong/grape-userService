package com.etekcity.userservice.response.res;

import java.sql.Timestamp;

public class RegisterRes {
    private Integer code = 0;
    private String msg = "fail";
    private Object result;

    //构造方法
    public RegisterRes() {

    }
    public RegisterRes(Integer code, String msg, String userId, Timestamp createAt) {
        this.code = code;
        this.msg = msg;
//        this.userId = userId;
//        this.createAt = createAt;
    }



    //getter and setter
    public Integer getCode() { return code; }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getResult() {
        return result;
    }

    //setResult里 初始化 result
    public void setResult(Object result) {
        this.result = result;
    }
}
