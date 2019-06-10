package com.etekcity.userservice.constant;

public class ErrorCodes {
    public final static int SUCCESS     =         0;
    public final static int EMAILILLEGAL =        -20101;//邮箱不合法
    public final static int PASSWORDILLEGAL =     -20102;//密码不合法
    public final static int EMAILREGISTERED =     -20111;//邮箱已注册
    public final static int PASSWORDERROR =       -20112;//密码错误
    public final static int EMAILUNEXIST    =     -20113;//邮箱不存在
    public final static int NICKNAMEILLEGAL =     -20103;//昵称不合法
    public final static int ADDRESSILLEGAL =      -20104;//地址不合法

    public final static int OLDPASSWORDILLEGAL =  -20105;//老密码不合法
    public final static int NEWPASSWORDILLEGAL =  -20106;//新密码不合法
    public final static int OLDPASSWORDERROR =    -20106;//老密码错误

    public final static int TOKENDISABLED =       -20201;//凭证失效
    public final static int TOKENERROR =          -20202;//凭证错误
}
