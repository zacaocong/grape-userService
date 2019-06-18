package com.etekcity.userservice.constant;

public enum ErrorCode {
    /**
     * 错误码
     * */
    SUCCESS(0, "success"),
    SEVER_INTERNAL_ERROR(-10001,"server internal error:服务器内部错误"),
    SERVER_BUSY(-10002,"server error:服务器繁忙"),
    SEVER_TIMEOUT(-10003,"server timeout:服务器超时"),

    EMAIL_ILLEGAL(-20101,"email illegal：邮箱不合法"),
    PASSWORD_ILLEGAL(-20102,"password illegal:密码不合法"),
    EMAIL_REGISTERED(-20111,"email registered:邮箱已注册"),
    //老密码错误也是这个
    PASSWORD_ERROR(-20112,"password error:密码错误"),
    EMAIL_UNEXIST(-20113,"email unexist:邮箱不存在"),

    NICKNAME_ILLEGAL(-20103,"nickname illegal:昵称不合法"),
    ADDRESS_ILLEGAL(-20104,"address illegal:地址不合法"),

    OLDPASSWORD_ILLEGAL(-20105,"oldpassword illegal:老密码不合法"),
    NEWPASSWORD_ILLEGAL(-20106,"newpassword illegal:新密码不合法"),

    TOKEN_DISABLED(-20201,"token disabled:凭证失效"),
    TOKEN_ERROR(-20202,"token error:凭证错误"),
    TOKEN_NULL(20203,"token null:X-Authorization不能为空");

    private int code;
    private String msg;

    ErrorCode(int code,String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "ErrorCode{" + "code=" + code + ", msg='" + msg + '\'' + '}';
    }
}
