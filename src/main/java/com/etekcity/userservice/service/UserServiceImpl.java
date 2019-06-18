package com.etekcity.userservice.service;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.etekcity.userservice.constant.ErrorCode;
import com.etekcity.userservice.constant.HeaderFields;
import com.etekcity.userservice.dao.UserMapper;
import com.etekcity.userservice.module.User;
import com.etekcity.userservice.module.XAuthorization;
import com.etekcity.userservice.redisconfig.RedisServiceImpl;
import com.etekcity.userservice.request.ChangePasswordBody;
import com.etekcity.userservice.request.LoginBody;
import com.etekcity.userservice.request.RegisterBody;
import com.etekcity.userservice.request.UpdateUserInfoBody;
import com.etekcity.userservice.response.result.GetUserInfoResult;
import com.etekcity.userservice.response.result.LoginResult;
import com.etekcity.userservice.response.result.RegisterResult;
import com.etekcity.userservice.response.rsp.Response;
import com.etekcity.userservice.utils.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisServiceImpl<XAuthorization> redisService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Override
    public Response register(RegisterBody requestBody) {
        logger.debug("register accept request");
        //邮箱密码规范性校验
        if (!CheckUtils.checkEmail(requestBody.getEmail()) || requestBody.getEmail() == null) {
            return new Response(ErrorCode.EMAIL_ILLEGAL);
        }
        if (!CheckUtils.checkpassword(requestBody.getPassword()) || requestBody.getPassword() == null) {
            return new Response((ErrorCode.PASSWORD_ILLEGAL));
        }

        logger.debug("register read");
        if (userMapper.getUserByEmail(requestBody.getEmail()) != null) {
            //这里需要邮箱未注册，即数据库中不存在该邮箱，否则按如下处理
            return new Response(ErrorCode.EMAIL_REGISTERED);
        }

        Response response = new Response(ErrorCode.SUCCESS);
        String userId = UUIDUtils.getUUID32();

        //存入时间和打印时间差了8小时，这应该是timezone导致的,UTC改为CTT显示正常
        Date now = new Date();
        String formatCreateAt = ft.format(now);
        response.setResult(new RegisterResult(userId, formatCreateAt));

        //加密
        String encryptedPassword;
        try {
            encryptedPassword = MD5Utils.getMD5Str(requestBody.getPassword());
        } catch (NoSuchAlgorithmException e) {
            logger.debug("请求加密算法失败，请检查是否存在该算法");
            return new Response(ErrorCode.SEVER_INTERNAL_ERROR);
        }

        logger.debug("register write sql");
        userMapper.insert(userId, requestBody.getEmail(), encryptedPassword, now, now);

        return response;
    }

    @Override
    public Response login(LoginBody requestBody) {
        //注册登录时的校验，也可封装，不过只用了两次
        //邮箱密码规范性校验
        logger.debug("login accept request ");
        if (!CheckUtils.checkEmail(requestBody.getEmail()) || requestBody.getEmail() == null) {
            return new Response(ErrorCode.EMAIL_ILLEGAL);
        }
        if (!CheckUtils.checkpassword(requestBody.getPassword()) || requestBody.getPassword() == null) {
            return new Response((ErrorCode.PASSWORD_ILLEGAL));
        }
        logger.debug("register read sql");
        if (userMapper.getUserByEmail(requestBody.getEmail()) == null) {
            //这里需要邮箱已注册，即数据库中存在该邮箱，否则按如下处理
            return new Response(ErrorCode.EMAIL_REGISTERED);
        }

        //这里实际上是需要加密后比对的
        String encryptedPassword;
        try {
            encryptedPassword = MD5Utils.getMD5Str(requestBody.getPassword());
        } catch (NoSuchAlgorithmException e) {
            logger.debug("请求加密算法失败，请检查是否存在该算法");
            return new Response(ErrorCode.SEVER_INTERNAL_ERROR);
        }
        if (!encryptedPassword.equals(userMapper.findPasswordByEmail(requestBody.getEmail()))) {
            return new Response(ErrorCode.PASSWORD_ERROR);
        }

        Response response = new Response(ErrorCode.SUCCESS);
        logger.debug("register read sql");
        User user = userMapper.getUserByEmail(requestBody.getEmail());
        String token = TokenUtils.getUUToken();
        XAuthorization xAuthorization = new XAuthorization(user.getUserId(), token);
        logger.debug("register write redis");
        redisService.set(token, xAuthorization, 86400L);

        LoginResult loginResult = new LoginResult(token, user.getUserId(), user.getNickname(),
                user.getAddress(), user.getEmail(), ft.format(user.getCreateAt()), ft.format(user.getUpdateAt()),
                86400L);

        response.setResult(loginResult);

        return response;
    }

    @Override
    public Response logout(HttpServletRequest request) {
        logger.debug("logout accept request");
        if (request.getHeader(HeaderFields.authorization) == null) {
            return new Response(ErrorCode.TOKEN_NULL);
        }
        //string处理,封装一下，重复使用了4次
        String[] params = StringUtils.splitStrings(request.getHeader(HeaderFields.authorization));
        String userId = params[0];
        String token = params[1];
        //token有效性正确性
        if (!redisService.existsKey(token)) {
            return new Response(ErrorCode.TOKEN_DISABLED);
        }
        if (!redisService.get(token).getUserId().equals(userId)) {
            return new Response(ErrorCode.TOKEN_ERROR);
        }

        redisService.deleteKey(token);

        return new Response(ErrorCode.SUCCESS);
    }

    @Override
    public Response getUserInfo(HttpServletRequest request) {
        logger.debug("getUserInfo accept request");
        if (request.getHeader(HeaderFields.authorization) == null) {
            return new Response(ErrorCode.TOKEN_NULL);
        }
        //string处理，封装
        String[] params = StringUtils.splitStrings(request.getHeader(HeaderFields.authorization));
        String userId = params[0];
        String token = params[1];
        //token有效性正确性
        logger.debug("getUserInfo read redis");
        if (!redisService.existsKey(token)) {
            return new Response(ErrorCode.TOKEN_DISABLED);
        }
        if (!redisService.get(token).getUserId().equals(userId)) {
            return new Response(ErrorCode.TOKEN_ERROR);
        }

        Response response = new Response(ErrorCode.SUCCESS);
        logger.debug("getUserInfo read redis");
        GetUserInfoResult getUserInfoResult = new GetUserInfoResult(
                userMapper.getUserById(userId).getUserId(),
                userMapper.getUserById(userId).getEmail(),
                userMapper.getUserById(userId).getNickname(),
                userMapper.getUserById(userId).getAddress(),
                ft.format(userMapper.getUserById(userId).getCreateAt()),
                ft.format(userMapper.getUserById(userId).getUpdateAt())
        );

        response.setResult(getUserInfoResult);

        return response;
    }

    @Override
    public Response updateUserInfo(UpdateUserInfoBody requestBody, HttpServletRequest request) {
        logger.debug("updateUserInfo accept request");
        if (request.getHeader(HeaderFields.authorization) == null) {
            return new Response(ErrorCode.TOKEN_NULL);
        }
        //string处理，请求头参数，封装
        String[] params = StringUtils.splitStrings(request.getHeader(HeaderFields.authorization));
        String userId = params[0];
        String token = params[1];
        //token有效性正确性
        logger.debug("updateUserInfo read redis");
        if (!redisService.existsKey(token)) {
            return new Response(ErrorCode.TOKEN_DISABLED);
        }
        if (!redisService.get(token).getUserId().equals(userId)) {
            return new Response(ErrorCode.TOKEN_ERROR);
        }

        //request信息nickname,address校验，不传则默认是null，null不做处理
        String nickname = requestBody.getNickname();
        String address = requestBody.getAddress();
        logger.debug("updateUserInfo read sql");
        if (requestBody.getNickname() == null) {
            nickname = userMapper.getUserById(userId).getNickname();
        }
        if (requestBody.getAddress() == null) {
            address = userMapper.getUserById(userId).getAddress();
        }
        logger.debug("updateUserInfo write sql");
        userMapper.updateUserInfoById(address, nickname, userId);

        return new Response(ErrorCode.SUCCESS);
    }

    @Override
    public Response changePassword(ChangePasswordBody requestBody, HttpServletRequest request) {
        logger.debug("changePassword accept request");
        if (request.getHeader(HeaderFields.authorization) == null) {
            return new Response(ErrorCode.TOKEN_NULL);
        }
        //string 处理
        String[] params = StringUtils.splitStrings(request.getHeader(HeaderFields.authorization));
        String userId = params[0];
        String token = params[1];
        //新老密码合法性校验
        if (!CheckUtils.checkpassword(requestBody.getOldPassword())) {
            return new Response(ErrorCode.OLDPASSWORD_ILLEGAL);
        }
        if (!CheckUtils.checkpassword(requestBody.getNewPassword())) {
            return new Response((ErrorCode.NEWPASSWORD_ILLEGAL));
        }

        logger.debug("changePassword read redis");
        if (!redisService.existsKey(token)) {
            return new Response(ErrorCode.TOKEN_DISABLED);
        }
        if (!redisService.get(token).getUserId().equals(userId)) {
            return new Response(ErrorCode.TOKEN_ERROR);
        }

        //老密码校验正确
        String encryptedOldPassword;
        try {
            encryptedOldPassword = MD5Utils.getMD5Str(requestBody.getOldPassword());
        } catch (NoSuchAlgorithmException e) {
            logger.debug("请求加密算法失败，请检查是否存在该算法");
            return new Response(ErrorCode.SEVER_INTERNAL_ERROR);
        }
        logger.debug("changePassword read sql");
        if (!encryptedOldPassword.equals(userMapper.findPasswordById(userId))) {
            return new Response(ErrorCode.PASSWORD_ERROR);
        }

        //新密码校验存储
        String encryptedNewPassword;
        try {
            encryptedNewPassword = MD5Utils.getMD5Str(requestBody.getNewPassword());
        } catch (NoSuchAlgorithmException e) {
            logger.debug("请求加密算法失败，请检查是否存在该算法");
            return new Response(ErrorCode.SEVER_INTERNAL_ERROR);
        }

        logger.debug("changePassword write sql");
        userMapper.updatePasswordById(encryptedNewPassword, userId);

        return new Response(ErrorCode.SUCCESS);
    }

    /**
     * token校验
     */
    public ErrorCode tokenCheck(String token, String userId) {
        //token有效性正确性
        if (!redisService.existsKey(token)) {
            return ErrorCode.TOKEN_DISABLED;
        }
        if (!redisService.get(token).getUserId().equals(userId)) {
            return ErrorCode.TOKEN_ERROR;
        }

        return ErrorCode.SUCCESS;
    }
}
