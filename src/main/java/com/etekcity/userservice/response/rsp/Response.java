package com.etekcity.userservice.response.rsp;

import com.etekcity.userservice.response.result.EmptyResult;
import com.etekcity.userservice.response.result.GetUserInfoResult;
import com.etekcity.userservice.response.result.LoginResult;
import com.etekcity.userservice.response.result.RegisterResult;
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
    private Response(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 枚举构造
     */
    private Response(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg();
    }

    /**
     * 构造方法,设置result
     *
     * 可以直接用
     * new Response<>(ErrorCode.PASSWORD_ILLEGAL,new EmptyResult());
     * new Response<>(ErrorCode.PASSWORD_ILLEGAL,new GetUserInfoResult());
     */
    private Response(ErrorCode errorCode, R result) {
        this(errorCode);
        this.result = result;
    }

    /*
    * 下面四个构造方法是在本处指定泛型，原构造方法也就是上面那个也可以直接使用，下面四个只是封装而已，这样我们可以把构造
    * 方法私有。只暴漏封装后的构造方法
    * */

    /**
     * 构造空result响应
     * */
    public static Response<EmptyResult> genResp(ErrorCode errorCode) {
        return new Response<>(errorCode, new EmptyResult());
    }

    /**
     * 构造GetUserInfoResult响应
     **/
    public static Response<GetUserInfoResult> genResp(ErrorCode errorCode, GetUserInfoResult result) {
        return new Response<>(errorCode, result);
    }

    /**
     * 构造LoginResult响应
     **/
    public static Response<LoginResult> genResp(ErrorCode errorCode, LoginResult result) {
        return new Response<>(errorCode, result);
    }

    /**
     * 构造RegisterResult响应
     **/
    public static Response<RegisterResult> genResp(ErrorCode errorCode, RegisterResult result) {
        return new Response<>(errorCode, result);
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

}
