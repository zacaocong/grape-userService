package com.grape.cloud.service.userservice;

import com.grape.cloud.constant.ErrorCodes;
import com.grape.cloud.dao.mapper.UserMapper;
import com.grape.cloud.response.*;
import com.grape.cloud.response.res.*;
import com.grape.cloud.response.result.RegisterResult;
import com.grape.cloud.util.CheckUtils;
import com.grape.cloud.util.GetUUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService  {

    @Autowired
    private UserMapper userMapper;//接口调用必须注入

    //注册
    public RegisterRes registerService(String email, String password){
        RegisterRes registerRes = new RegisterRes();//响应消息
        //邮箱校验
        if(!CheckUtils.checkEmail(email)){
            registerRes.setCode(ErrorCodes.EMAILILLEGAL);
            return registerRes;
        }
        //密码校验
        if(!CheckUtils.checkpassword(password)){
            registerRes.setCode(ErrorCodes.PASSWORDILLEGAL);
            return registerRes;
        }
        //邮箱未注册
        if(userMapper.getUserByEmail(email)!= null){
            registerRes.setCode(ErrorCodes.EMAILREGISTERED);
            return registerRes;
        }
        //生成ID和创建时间。
        String id = GetUUID.getUUID32();
        Timestamp timestamp = new Timestamp(new Date().getTime());
        //对象初始化
        registerRes.setCode(ErrorCodes.SUCCESS);
        registerRes.setMsg("success");
        registerRes.setResult(new RegisterResult(id, timestamp));//这里初始化result

        //数据库插入
        userMapper.insert(id,email,password,timestamp);
        return registerRes;
    }

    //--------------------------------void需改成其所对应的的类型
    //用户登录
    public LoginRes loginService(String email,String password){
        LoginRes loginRes = new LoginRes();
        //邮箱密码校验
        //邮箱查库拿密码
        //两密码一样通过
        //生成发token，查库拿用户数据。
        //初始化响应数据
        return loginRes;
    }
    //用户登出
    public LogoutRes logoutService(String id){
        LogoutRes logoutRes = new LogoutRes();
        //token未失效则失效
        //初始化响应数据
        return logoutRes;
    }

    //获取用户信息
    public GetUserInfoRes getUserInfoService(String id){
        GetUserInfoRes getUserInfoRes = new GetUserInfoRes();
        //token有效
        //查库拿数据
        //初始化相应数据
        return getUserInfoRes;
    }
    //更新用户信息
    public UpdateUserInfoRes updateUserInfoRes(){
        UpdateUserInfoRes updateUserInfoRes = new UpdateUserInfoRes();
        //昵称地址合法校验
        //token有效性校验
        //写入数据库
        //初始化相响应数据
        return updateUserInfoRes;
    }
    //修改密码
    public  ChangePasswordRes changePasswordService(){
        ChangePasswordRes changePasswordRes = new ChangePasswordRes();
        //新老密码合法性校验
        //token有效性验证
        //老密码查库正确
        //新密码写入
        //初始化响应数据
        return changePasswordRes;
    }
}
