package com.etekcity.userservice.response.rsp;

import com.etekcity.userservice.constant.ErrorCode;
import com.etekcity.userservice.response.result.EmptyResult;

public class Response {
    private Integer code = 0;
    private String msg = "success";
    private Object result = new EmptyResult();

    /**
     * 无参构造
     * */
    public Response() {

    }

    /**
     * 传参构造
     * */
    public Response(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 枚举构造
     * */
    public Response(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg();
    }

    /**
     * 构造方法
     * */
    public Response(Integer code, String msg, Object result) {
        this.code = code;
        this.msg = msg;
        this.result = result;
    }



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

    //setResult里 初始化 result
    public void setResult(Object result) {
        this.result = result;
    }
}
