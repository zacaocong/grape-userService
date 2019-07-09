package com.etekcity.userservice.response.rsp;

import com.etekcity.userservice.response.result.EmptyResult;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

import com.etekcity.userservice.constant.ErrorCode;

/**
 * 相应消息统一格式
 *
 * @author grape
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
public class Response<R> {
    private Integer code;
    private String msg;
    private R result;

    /**
     * 无参构造
     */
    public Response() {

    }

    /**
     * 传参构造
     */
    public Response(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 枚举构造
     */
    public Response(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg();
    }

    /**
     * 构造空result响应
     * */
    public static Response<EmptyResult> emptyResp(ErrorCode errorCode) {
        return new Response<>(errorCode, new EmptyResult());
    }

    /**
     * 构造方法
     */
    public Response(ErrorCode errorCode, R result) {
        this(errorCode);
        this.result = result;
    }

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

    public R getResult() {
        return result;
    }

    public void setResult(R result) {
        this.result = result;
    }

    /**
     * 枚举set
     */
    public void setCodeAndMsgByEnum(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg();
    }
}
