package com.grape.cloud.response.res;

public class UpdateUserInfoRes {
    private Integer code = 0;
    private String msg = "fail";
    private Object result;


    //没必要写构造方法，用不上

    //getter and setter
    public Integer getCode() {
        return code;
    }

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

    public void setResult(Object result) {
        this.result = result;
    }
}
