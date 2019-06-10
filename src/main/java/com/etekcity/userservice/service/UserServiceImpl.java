package com.etekcity.userservice.service;


import com.etekcity.userservice.constant.ErrorCodes;
import com.etekcity.userservice.dao.mapper.UserMapper;
import com.etekcity.userservice.moudle.User;
import com.etekcity.userservice.moudle.X_Authorization;
import com.etekcity.userservice.response.res.*;
import com.etekcity.userservice.response.result.GetUserInfoResult;
import com.etekcity.userservice.response.result.LoginResult;
import com.etekcity.userservice.response.result.RegisterResult;
import com.etekcity.userservice.util.CheckUtils;
import com.etekcity.userservice.util.GetUUID;
import com.etekcity.userservice.util.RedisUtils;
import com.etekcity.userservice.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    @Autowired//接口调用必须注入
    private UserMapper userMapper;//数据库
    @Autowired
    private RedisUtils redisUtils;//redis
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //注册
    public RegisterRes registerService(String email, String password){
        RegisterRes registerRes = new RegisterRes();//响应消息
        //邮箱校验
        if(!CheckUtils.checkEmail(email)){
            registerRes.setCode(ErrorCodes.EMAILILLEGAL);
            registerRes.setMsg("email illegal");
            return registerRes;
        }
        //密码校验
        if(!CheckUtils.checkpassword(password)){
            registerRes.setCode(ErrorCodes.PASSWORDILLEGAL);
            registerRes.setMsg("password illegal");
            return registerRes;
        }
        //邮箱未注册
        if(userMapper.getUserByEmail(email)!= null){
            registerRes.setCode(ErrorCodes.EMAILREGISTERED);
            registerRes.setMsg("email registered");
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

    //登录
    public LoginRes loginService(String email, String password){
        LoginRes loginRes = new LoginRes();
        //邮箱校验
        if(!CheckUtils.checkEmail(email)){
            loginRes.setCode(ErrorCodes.EMAILILLEGAL);
            loginRes.setMsg("email illegal");
            return loginRes;
        }
        //密码校验
        if(!CheckUtils.checkpassword(password)){
            loginRes.setCode(ErrorCodes.PASSWORDILLEGAL);
            loginRes.setMsg("password illegal");
            return loginRes;
        }
        //邮箱未注册
        if(userMapper.getUserByEmail(email)==null){
            loginRes.setCode(ErrorCodes.EMAILUNEXIST);
            loginRes.setMsg("email unexist");
            return loginRes;
        }
        //密码不正确????这里出问题了，密码都成错的了
        if(!password.equals(userMapper.findPasswordByEmail(email))){
            loginRes.setCode(ErrorCodes.PASSWORDERROR);
            loginRes.setMsg("password error");
            return loginRes;
        }
        //读数据库，获取userId，nickname,address,creatAt,updateAt
        User user = userMapper.getUserByEmail(email);
        String userId = user.getId();
        String nickname = user.getNickname();
        String address = user.getAddress();
        Timestamp createAt = user.getCreateAt();
        Timestamp updateAt = user.getUpdateAt();

        //封装X_Authorization值  redis value
        String token = TokenUtils.getUUToken();
        X_Authorization x_authorization = new X_Authorization();
        x_authorization.setUserId(userId);
        x_authorization.setToken(token);
        //key value token  x_authorization(token,userid,count)写入redis
        redisUtils.set(token,x_authorization,86400L);

//        //redis string
//        String token = TokenUtils.getUUToken();
//        //key value token userid写入redis
//        redisUtils.set(token,userId,86400L);

        //result初始化：token，expiresIn,userId,email,nickname,address,creatAt,updateAt
        LoginResult loginResult = new LoginResult();
        loginResult.setToken(token);
        loginResult.setExpiresIn(86400L);
        loginResult.setUserId(userId);
        loginResult.setEmail(email);
        loginResult.setNickName(nickname);
        loginResult.setAddress(address);
        loginResult.setCreateAt(createAt);
        loginResult.setUpdateAt(updateAt);

        //响应消息初始化
        loginRes.setCode(ErrorCodes.SUCCESS);
        loginRes.setMsg("success");
        loginRes.setResult(loginResult);

        return loginRes;
    }

    //登出
    public LogoutRes logoutService(String token,String userId){
        LogoutRes logoutRes = new LogoutRes();//响应消息
        X_Authorization x_authorization = new X_Authorization();//接收redis中的value

        //token有效性验证
        if(!redisUtils.existsKey(token)){
            logoutRes.setCode(ErrorCodes.TOKENDISABLED);
            logoutRes.setMsg("token disabled");
            return logoutRes;
        }//到这都是正常的

        //存的id
        x_authorization= (X_Authorization) redisUtils.get(token);//查出来的是object，无法操作，怎么转成我们需要的类型呢，可强转
        String userIdInRedis = x_authorization.getUserId();

        //((X_Authorization) redisUtils.get(token)).getUserId()
        //token正确性验证,？？？？？？？这里又出错了
        if(!userId.equals(((X_Authorization) redisUtils.get(token)).getUserId())){
            logoutRes.setCode(ErrorCodes.TOKENERROR);
            return logoutRes;
        }
//        if(!userId.equals(userIdInRedis)){
//            logoutRes.setCode(ErrorCodes.TOKENERROR);
//            logoutRes.setMsg("token error");
//            return logoutRes;
//        }

        //redis删除
        redisUtils.deleteKey(token);

        //响应消息初始化
        logoutRes.setCode(ErrorCodes.SUCCESS);
        logoutRes.setMsg("success");
        logoutRes.setResult(null);
        return logoutRes;
    }

    //获取用户信息
    public GetUserInfoRes getUserInfoService(String token,String userId){
        GetUserInfoRes getUserInfoRes = new GetUserInfoRes();//响应消息
        X_Authorization x_authorization = new X_Authorization();//接收redis中的value
        //token有效性验证
        if(!redisUtils.existsKey(token)){
            getUserInfoRes.setCode(ErrorCodes.TOKENDISABLED);
            getUserInfoRes.setMsg("token disabled");
            return getUserInfoRes;
        }
        //存的id
        x_authorization= (X_Authorization) redisUtils.get(token);//查出来的是object，无法操作，怎么转成我们需要的类型呢，可强转
        String userIdInRedis = x_authorization.getUserId();
        //token正确性验证
        if(!userId.equals(userIdInRedis)){
            getUserInfoRes.setCode(ErrorCodes.TOKENERROR);
            getUserInfoRes.setMsg("token erroe");
            return getUserInfoRes;
        }


        //读数据库，获取userId，nickname,address,creatAt,updateAt
        //result:userId,email,nickname,address,createAt,updateAt
        GetUserInfoResult getUserInfoResult = new GetUserInfoResult();
        getUserInfoResult.setUserId(userMapper.getUserById(userId).getUserId());//这里出错了，为啥是空的啊，别的都正常
        getUserInfoResult.setEmail(userMapper.getUserById(userId).getEmail());
        getUserInfoResult.setNickname(userMapper.getUserById(userId).getNickname());
        getUserInfoResult.setAddress(userMapper.getUserById(userId).getAddress());
        getUserInfoResult.setCreateAt(userMapper.getUserById(userId).getCreateAt());
        getUserInfoResult.setUpdateAt(userMapper.getUserById(userId).getUpdateAt());

        //响应消息初始化
        getUserInfoRes.setCode(ErrorCodes.SUCCESS);
        getUserInfoRes.setMsg("success");
        getUserInfoRes.setResult(getUserInfoResult);

        return getUserInfoRes;
    }

    //更新用户信息
    public UpdateUserInfoRes updateUserInfoService(String token,String userId,String nickname,String address){
        UpdateUserInfoRes updateUserInfoRes = new UpdateUserInfoRes();//响应消息
        X_Authorization x_authorization = new X_Authorization();//接收redis中的value

        //校验昵称地址合法性

        //token有效性验证
        if(!redisUtils.existsKey(token)){
            updateUserInfoRes.setCode(ErrorCodes.TOKENDISABLED);
            updateUserInfoRes.setMsg("token disabled");
            return updateUserInfoRes;
        }
        //存的id
        x_authorization= (X_Authorization) redisUtils.get(token);//查出来的是object，无法操作，怎么转成我们需要的类型呢，可强转
        String userIdInRedis = x_authorization.getUserId();
        //token正确性验证
        if(!userId.equals(userIdInRedis)){
            updateUserInfoRes.setCode(ErrorCodes.TOKENERROR);
            updateUserInfoRes.setMsg("token erroe");
            return updateUserInfoRes;
        }

        //写入数据库
        userMapper.UpdateUserInfoByID(address,nickname,userId);

        //无result
        //初始化响应消息
        updateUserInfoRes.setCode(ErrorCodes.SUCCESS);
        updateUserInfoRes.setMsg("success");
        updateUserInfoRes.setResult(null);

        return updateUserInfoRes;
    }

    public ChangePasswordRes changePasswordService(String token,String userId,String oldPassword,String newPassword){
        ChangePasswordRes changePasswordRes = new ChangePasswordRes();

        X_Authorization x_authorization = new X_Authorization();//接收redis中的value

        //新老密码合法性
        if(!(CheckUtils.checkpassword(oldPassword))){
            changePasswordRes.setCode(ErrorCodes.OLDPASSWORDILLEGAL);
            return changePasswordRes;
        }
        if(!(CheckUtils.checkpassword(newPassword))){
            changePasswordRes.setCode(ErrorCodes.NEWPASSWORDILLEGAL);
            return changePasswordRes;
        }

        //token有效性验证
        if(!redisUtils.existsKey(token)){
            changePasswordRes.setCode(ErrorCodes.TOKENDISABLED);
            changePasswordRes.setMsg("token disabled");
            return changePasswordRes;
        }
        //存的id
        x_authorization= (X_Authorization) redisUtils.get(token);//查出来的是object，无法操作，怎么转成我们需要的类型呢，可强转
        String userIdInRedis = x_authorization.getUserId();
        //token正确性验证
        if(!userId.equals(userIdInRedis)){
            changePasswordRes.setCode(ErrorCodes.TOKENERROR);
            changePasswordRes.setMsg("token erroe");
            return changePasswordRes;
        }

        //查数据库，密码是否正确
        if(!oldPassword.equals(userMapper.findPasswordByID(userId))){
            changePasswordRes.setCode(ErrorCodes.PASSWORDERROR);
            changePasswordRes.setMsg("oldpassword error");
            return changePasswordRes;
        }

        //写入新密码
        userMapper.UpdatePasswordByID(newPassword,userId);

        changePasswordRes.setCode(ErrorCodes.SUCCESS);
        changePasswordRes.setMsg("success");
        changePasswordRes.setResult(null);
        return changePasswordRes;
    }

}
























