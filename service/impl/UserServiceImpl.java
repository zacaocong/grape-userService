package com.etekcity.userservice.service.impl;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.etekcity.userservice.redis.entity.AuthorizationValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.etekcity.userservice.constant.Const;
import com.etekcity.userservice.constant.ErrorCode;
import com.etekcity.userservice.constant.HeaderFields;
import com.etekcity.userservice.dao.UserMapper;
import com.etekcity.userservice.entity.User;
import com.etekcity.userservice.redis.impl.RedisServiceImpl;
import com.etekcity.userservice.response.result.GetUserInfoResult;
import com.etekcity.userservice.response.result.LoginResult;
import com.etekcity.userservice.response.result.RegisterResult;
import com.etekcity.userservice.response.rsp.Response;
import com.etekcity.userservice.utils.*;
import com.etekcity.userservice.response.result.EmptyResult;
import com.etekcity.userservice.entity.UserInfo;
import com.etekcity.userservice.service.UserService;
import com.etekcity.userservice.request.*;
/**
 * UserService实现
 * @author grape
 * */

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisServiceImpl<AuthorizationValue> redisService;

    private String userId;
    private String token;
    private String email;

    private static Response<EmptyResult> response = new Response<>();

    static  {
        response.setResult(new EmptyResult());
    }


    @Override
    public Response register(RegisterAndLoginBody requestBody) {
        log.info("Now method    : {}",">>>>>>>>>register");
        log.info("register      : {}:{}","get the requestBody",requestBody);

        email = requestBody.getEmail();
        log.info("register      : email:{}",email);

        Response responseSuccess = new Response(ErrorCode.SUCCESS);
        log.info("register      : {}:{}","create the success response",responseSuccess);
        userId = UUIDUtils.getUUID32();
        log.info("register      : {}:{}","create the UUID",userId);

        //存入时间和打印时间差了8小时，这应该是timezone导致的,UTC改为CTT显示正常
        Date now = new Date();
        SimpleDateFormat ft = new SimpleDateFormat(Const.TIMEPATTERN);
        String formatCreateAt = ft.format(now);
        log.info("register      : {}:{}","created the format time",formatCreateAt);

        //todo:1、怎么去？ 2、log中的重复字符串？ 3、try catch加密校验，写入可以在service里做，但是校验应该在切面里，能不能封装出一个方法
        responseSuccess.setResult(new RegisterResult(userId, formatCreateAt));

        //加密
        String encryptedPassword;
        try {
            encryptedPassword = MD5Utils.getMD5Str(requestBody.getPassword());
            log.info("register      : {}:{}","created the encryptedPassword",encryptedPassword);
        } catch (NoSuchAlgorithmException e) {
            log.debug("请求加密算法失败，请检查是否存在该算法");
            response.setCodeAndMsgByEnum(ErrorCode.SEVER_INTERNAL_ERROR);
            return response;
        }
        //写入
        log.info("register      : {}","write in database");
        log.info("register      : userId:{},email:{},encryptedPassword:{},createAt:{},updateAt:{}",userId,email,
                encryptedPassword,now,now);
        userMapper.insert(userId, email, encryptedPassword, now, now);

        log.info("register      : {}",">>>>>>>>>>register return");
        return responseSuccess;
    }

    @Override
    public Response login(RegisterAndLoginBody requestBody) {
        log.info("Now method    : {}",">>>>>>>>>>login");
        log.info("login         : {}:{}","get the requestBody",requestBody);

        email = requestBody.getEmail();
        log.info("login         : email:{}",email);

        User user = userMapper.getUserByEmail(email);

        Response responseSuccess = new Response(ErrorCode.SUCCESS);
        log.info("login         : {}:{}","create the success response",responseSuccess);
        token = TokenUtils.getUUToken();
        log.info("login         : {}:{}","create the token",token);

        AuthorizationValue authorization = new AuthorizationValue(user.getUserId(), token);
        log.info("login         : {}","create the redis value");
        redisService.set(token, authorization, Const.EXPIRETIME);
        log.info("login         : {}","write in redis");

        SimpleDateFormat ft = new SimpleDateFormat(Const.TIMEPATTERN);
        LoginResult loginResult = new LoginResult(token, user.getUserId(), user.getNickname(),
                user.getAddress(), user.getEmail(), ft.format(user.getCreateAt()), ft.format(user.getUpdateAt()),
                Const.EXPIRETIME);
        log.info("login         : {}","create the result");
        //todo:
        responseSuccess.setResult(loginResult);

        log.info("login         : {}",">>>>>>>>>>>login return");
        return responseSuccess;
    }

    @Override
    public Response logout(HttpServletRequest request) {
        log.info("Now method    : {}",">>>>>>>>>>logout");
        log.info("logout        : {}:{}","get the request",request);

        String[] params = StringUtils.splitStrings(request.getHeader(HeaderFields.AUTHORIZATION));
        userId = params[0];
        token = params[1];
        log.info("logout        : userId:{},token:{}",userId,token);

        log.info("logout        : {}","delete the token , write the redis");
        redisService.deleteKey(token);

        log.info("logout        : {}",">>>>>>>>>>>logout return");
        return response;
    }

    @Override
    public Response getUserInfo(HttpServletRequest request) {
        log.info("Now method    : {}",">>>>>>>>>>getUserInfo");
        log.info("getUserInfo   : {}:{}","get the request",request);

        String[] params = StringUtils.splitStrings(request.getHeader(HeaderFields.AUTHORIZATION));
        userId = params[0];
        log.info("getUserInfo   : userId:{}",userId);

        Response responseSuccess = new Response(ErrorCode.SUCCESS);
        log.info("getUserInfo   : {}:{}","create the success response",responseSuccess);

        SimpleDateFormat ft = new SimpleDateFormat(Const.TIMEPATTERN);

        log.info("getUserInfo   : {}","read the database get userInfo by userId");
        UserInfo userInfo = userMapper.getUserInfoById(userId);

        GetUserInfoResult getUserInfoResult = new GetUserInfoResult(
                userInfo.getUserId(),
                userInfo.getEmail(),
                userInfo.getNickname(),
                userInfo.getAddress(),
                ft.format(userInfo.getCreateAt()),
                ft.format(userInfo.getUpdateAt())
        );
        log.info("getUserInfo   : {}:{}","create the result",getUserInfoResult);

        responseSuccess.setResult(getUserInfoResult);

        log.info("getUserInfo   : {}",">>>>>>>>>>>getUserInfo return");
        return responseSuccess;
    }

    @Override
    public Response updateUserInfo(HttpServletRequest request,UpdateUserInfoBody requestBody) {
        log.info("Now method    : {}",">>>>>>>>>>updateUserInfo");
        log.info("updateUserInfo: {}:{}","get the request",request);

        //string处理，请求头参数，获取userId
        String[] params = StringUtils.splitStrings(request.getHeader(HeaderFields.AUTHORIZATION));
        userId = params[0];
        log.info("updateUserInfo: userId:{}",userId);

        //request信息nickname,address校验，不传则默认是null，null不做处理
        String nickname = requestBody.getNickname();
        String address = requestBody.getAddress();
        log.info("updateUserInfo: nickname:{},address:{}", nickname,address);

        //更新时间
        Date now = new Date();

        //昵称地址校验写入
        log.info("updateUserInfo: {}","write nickname and address in database");
        if (nickname != null && CheckUtils.checkNickname(nickname)) {
            userMapper.updateNicknamById(nickname,now,userId);
        }
        if (address != null && CheckUtils.checkNickname(address)) {
            userMapper.updateAddressById(address,now,userId);
        }

        log.info("updateUserInfo: {}",">>>>>>>>>>>updateUserInfo return");
        return response;
    }

    @Override
    public Response changePassword(HttpServletRequest request,ChangePasswordBody requestBody) {
        log.info("Now method    : {}",">>>>>>>>>>changePassword");
        log.info("changePassword: {}:{}","get the request",request);

        String oldPassword = requestBody.getOldPassword();
        String newPassword = requestBody.getNewPassword();
        log.info("changePassword: oldPassword:{},newPassword:{}",oldPassword,newPassword);

        //string 处理,userId还会用，token删了吧
        String[] params = StringUtils.splitStrings(request.getHeader(HeaderFields.AUTHORIZATION));
        userId = params[0];
        log.info("changePassword: userId:{}",userId);

        log.info("changePassword: {}","encrypt the new password");
        //新密码加密存储
        String encryptedNewPassword;
        try {
            encryptedNewPassword = MD5Utils.getMD5Str(newPassword);
            log.info("changePassword: encryptedNewPassword:{}",encryptedNewPassword);
        } catch (NoSuchAlgorithmException e) {
            log.error("请求加密算法失败，请检查是否存在该算法");
            response.setCodeAndMsgByEnum(ErrorCode.SEVER_INTERNAL_ERROR);
            return response;
        }

        log.info("changePassword: {}","write the newPassword in database");
        Date now = new Date();
        userMapper.updatePasswordById(encryptedNewPassword,now,userId);

        log.info("changePassword: {}",">>>>>>>>>>>changePassword return");
        return response;
    }

}
