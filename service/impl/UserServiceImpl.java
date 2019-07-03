package com.etekcity.userservice.service.impl;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.etekcity.userservice.constant.Const;
import com.etekcity.userservice.constant.ErrorCode;
import com.etekcity.userservice.constant.HeaderFields;
import com.etekcity.userservice.dao.UserMapper;
import com.etekcity.userservice.entity.UserInfo;
import com.etekcity.userservice.modle.User;
import com.etekcity.userservice.redis.entity.AuthorizationValue;
import com.etekcity.userservice.redis.entity.UserIdValue;
import com.etekcity.userservice.redis.impl.RedisServiceImpl;
import com.etekcity.userservice.request.ChangePasswordBody;
import com.etekcity.userservice.request.RegisterAndLoginBody;
import com.etekcity.userservice.request.UpdateUserInfoBody;
import com.etekcity.userservice.response.result.EmptyResult;
import com.etekcity.userservice.response.result.GetUserInfoResult;
import com.etekcity.userservice.response.result.LoginResult;
import com.etekcity.userservice.response.result.RegisterResult;
import com.etekcity.userservice.response.rsp.Response;
import com.etekcity.userservice.service.UserService;
import com.etekcity.userservice.utils.*;

/**
 * UserService实现
 *
 * @author grape
 */

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisServiceImpl<AuthorizationValue> authorizationValueRedisService;
    @Autowired
    private RedisServiceImpl<TreeSet<UserIdValue>> userIdValueRedisService;

    @Override
    public Response register(RegisterAndLoginBody requestBody) {
        //空体响应，方法体内保证线程安全
        Response<EmptyResult> response = new Response<>();
        response.setResult(new EmptyResult());

        //接收参数
        String email = requestBody.getEmail();

        //成功响应消息体
        Response responseSuccess = new Response(ErrorCode.SUCCESS);

        //生成响应结果
        String userId = UUIDUtils.getUUID32();
        //存入时间和打印时间差了8小时，这应该是timezone导致的,UTC改为CTT显示正常
        Date now = new Date();
        SimpleDateFormat ft = new SimpleDateFormat(Const.TIMEPATTERN);
        String formatCreateAt = ft.format(now);
        responseSuccess.setResult(new RegisterResult(userId, formatCreateAt));

        //加密：
        String encryptedPassword;
        try {
            encryptedPassword = MD5Utils.getMD5Str(requestBody.getPassword());
        } catch (NoSuchAlgorithmException e) {
            log.error("请求加密算法失败，请检查是否存在该算法", e);
            response.setCodeAndMsgByEnum(ErrorCode.SEVER_INTERNAL_ERROR);
            return response;
        }

        //数据库：写入
        try {
            userMapper.insert(userId, email, encryptedPassword, now, now);
        } catch (Exception e) {
            log.error("数据库操作失败", e);
            response.setCodeAndMsgByEnum(ErrorCode.SEVER_INTERNAL_ERROR);
            return response;
        }

        return responseSuccess;
    }

    @Override
    public Response login(RegisterAndLoginBody requestBody) {
        //空体响应
        Response<EmptyResult> response = new Response<>();
        response.setResult(new EmptyResult());

        //接收参数
        String email = requestBody.getEmail();

        //数据库：读取信息
        User user;
        try {
            user = userMapper.getUserByEmail(email);
        } catch (Exception e) {
            log.error("数据库操作失败", e);
            response.setCodeAndMsgByEnum(ErrorCode.SEVER_INTERNAL_ERROR);
            return response;
        }

        //生成redis数据
        String token = TokenUtils.getUUToken();
        String userId = user.getUserId();
        //userId：token
        String authorization = StringUtils.formatCreateKey(userId, token);
        //当前时间
        Date createTokenAt = new Date();
        //生成校验value
        AuthorizationValue authorizationValue = new AuthorizationValue(userId, createTokenAt);

        try {
            //判断是否有userId
            TreeSet<UserIdValue> uSet;
            if (!userIdValueRedisService.existsKey(userId)) {
                uSet = new TreeSet<>();
            } else {
                //get 获取该用户所有token信息
                uSet = userIdValueRedisService.get(userId);
                //更新过期token ,将uSet中过期的token删除掉
                updateToken(uSet, createTokenAt);
            }
            //处理uSet：加入本次新生成token，
            UserIdValue userIdValue = new UserIdValue(authorization, createTokenAt);
            uSet.add(userIdValue);
            //加入后不能超过5个token，超过就删除老的
            while (uSet.size() > Const.TOKENMAX) {
                authorizationValueRedisService.deleteKey(uSet.first().getAuthorization());
                uSet.remove(uSet.first());
            }
            //写入
            authorizationValueRedisService.set(authorization, authorizationValue, Const.EXPIRETIME);
            userIdValueRedisService.getAndSetAddUpdateAt(userId, uSet, Const.EXPIRETIME + Const.ZONE);
        } catch (Exception e) {
            log.error("redis操作失败", e);
            response.setCodeAndMsgByEnum(ErrorCode.SEVER_INTERNAL_ERROR);
            return response;
        }

        //生成成功响应消息
        Response responseSuccess = new Response(ErrorCode.SUCCESS);
        //生成响应结果
        SimpleDateFormat ft = new SimpleDateFormat(Const.TIMEPATTERN);
        LoginResult loginResult = new LoginResult(token, user.getUserId(), user.getNickname(),
                user.getAddress(), user.getEmail(), ft.format(user.getCreateAt()), ft.format(user.getUpdateAt()),
                Const.EXPIRETIME);

        responseSuccess.setResult(loginResult);

        return responseSuccess;
    }

    @Override
    public Response logout(HttpServletRequest request) {
        //空体响应
        Response<EmptyResult> response = new Response<>();
        response.setResult(new EmptyResult());

        //接收参数
        //获取并格式化为userId：token
        String authorization = request.getHeader(HeaderFields.AUTHORIZATION);
        authorization = StringUtils.formatCreateKey(authorization);
        //获取userId
        String[] params = StringUtils.splitStringsByColon(authorization);
        String userId = params[0];

        Date now = new Date();

        //redis：删除token
        try {
            //get
            TreeSet<UserIdValue> uSet = userIdValueRedisService.get(userId);
            //处理，遍历删除该authorization的value
            Iterator<UserIdValue> it = uSet.iterator();
            while (it.hasNext()) {
                if (it.next().getAuthorization().equals(authorization)) {
                    it.remove();
                }
            }
            authorizationValueRedisService.deleteKey(authorization);
            //更新删除过期token
            updateToken(uSet, now);
            //get and set
            if (uSet.isEmpty()) {
                userIdValueRedisService.deleteKey(userId);
            } else {
                Long expire = authorizationValueRedisService.getKeyExpire(uSet.last().getAuthorization(),
                        TimeUnit.SECONDS);
                userIdValueRedisService.getAndSetAddUpdateAt(userId, uSet, expire + Const.ZONE);
            }
        } catch (Exception e) {
            log.error("redis操作失败", e);
            response.setCodeAndMsgByEnum(ErrorCode.SEVER_INTERNAL_ERROR);
            return response;
        }

        return response;
    }

    @Override
    public Response getUserInfo(HttpServletRequest request) {
        //空体响应
        Response<EmptyResult> response = new Response<>();
        response.setResult(new EmptyResult());

        //接收参数
        String[] params = StringUtils.splitStrings(request.getHeader(HeaderFields.AUTHORIZATION));
        String userId = params[0];

        //成功响应消息体
        Response responseSuccess = new Response(ErrorCode.SUCCESS);

        SimpleDateFormat ft = new SimpleDateFormat(Const.TIMEPATTERN);
        //数据库：获取用户信息
        UserInfo userInfo;
        try {
            userInfo = userMapper.getUserInfoById(userId);
        } catch (Exception e) {
            log.error("数据库操作失败", e);
            response.setCodeAndMsgByEnum(ErrorCode.SEVER_INTERNAL_ERROR);
            return response;
        }

        try {
            updateToken(userId);
        } catch (Exception e) {
            log.error("自动更新失败", e);
            response.setCodeAndMsgByEnum(ErrorCode.SEVER_INTERNAL_ERROR);
            return response;
        }

        //生成响应结果
        GetUserInfoResult getUserInfoResult = new GetUserInfoResult(
                userInfo.getUserId(),
                userInfo.getEmail(),
                userInfo.getNickname(),
                userInfo.getAddress(),
                ft.format(userInfo.getCreateAt()),
                ft.format(userInfo.getUpdateAt())
        );

        responseSuccess.setResult(getUserInfoResult);

        return responseSuccess;
    }

    @Override
    public Response updateUserInfo(HttpServletRequest request, UpdateUserInfoBody requestBody) {
        //空体响应
        Response<EmptyResult> response = new Response<>();
        response.setResult(new EmptyResult());

        //string处理，请求头参数，获取userId
        String[] params = StringUtils.splitStrings(request.getHeader(HeaderFields.AUTHORIZATION));
        String userId = params[0];
        //request信息nickname,address校验，不传则默认是null，null不做处理
        String nickname = requestBody.getNickname();
        String address = requestBody.getAddress();

        //更新时间
        Date now = new Date();

        try {
            updateToken(userId);
        } catch (Exception e) {
            log.error("自动更新失败", e);
            response.setCodeAndMsgByEnum(ErrorCode.SEVER_INTERNAL_ERROR);
            return response;
        }

        //数据库：昵称地址校验，写入数据库
        try {
            if (nickname != null && CheckUtils.checkNickname(nickname)) {
                userMapper.updateNicknameById(nickname, now, userId);
            }
            if (address != null && CheckUtils.checkNickname(address)) {
                userMapper.updateAddressById(address, now, userId);
            }
        } catch (Exception e) {
            log.error("数据库操作失败", e);
            response.setCodeAndMsgByEnum(ErrorCode.SEVER_INTERNAL_ERROR);
            return response;
        }

        return response;
    }

    @Override
    public Response changePassword(HttpServletRequest request, ChangePasswordBody requestBody) {
        //空体响应
        Response<EmptyResult> response = new Response<>();
        response.setResult(new EmptyResult());

        //获取新老密码
        String oldPassword = requestBody.getOldPassword();
        String newPassword = requestBody.getNewPassword();

        //新老密码合法性
        if (oldPassword == null || !CheckUtils.checkPassword(oldPassword)) {
            response.setCodeAndMsgByEnum(ErrorCode.OLDPASSWORD_ILLEGAL);
            return response;
        }
        if (newPassword == null || !CheckUtils.checkPassword(newPassword)) {
            response.setCodeAndMsgByEnum(ErrorCode.NEWPASSWORD_ILLEGAL);
            return response;
        }

        String[] params = StringUtils.splitStrings(request.getHeader(HeaderFields.AUTHORIZATION));
        String userId = params[0];

        //加密：获得暗文密码
        String encryptedOldPassword;
        String encryptedNewPassword;

        try {
            encryptedOldPassword = MD5Utils.getMD5Str(oldPassword);
            encryptedNewPassword = MD5Utils.getMD5Str(newPassword);
        } catch (NoSuchAlgorithmException e) {
            log.error("请求加密算法失败，请检查是否存在该算法", e);
            response.setCodeAndMsgByEnum(ErrorCode.SEVER_INTERNAL_ERROR);
            return response;
        }

        //数据库：老密码校验，正确，新密码写入数据库
        try {
            if (!encryptedOldPassword.equals(userMapper.findPasswordById(userId))) {
                //密码错误
                log.info("oldPassword is error");
                response.setCodeAndMsgByEnum(ErrorCode.PASSWORD_ERROR);
                return response;
            }
            //更新密码
            Date now = new Date();
            userMapper.updatePasswordById(encryptedNewPassword, now, userId);
        } catch (Exception e) {
            log.error("操作数据库失败", e);
            response.setCodeAndMsgByEnum(ErrorCode.SEVER_INTERNAL_ERROR);
            return response;
        }

        try {
            //get
            TreeSet<UserIdValue> uSet = userIdValueRedisService.get(userId);
            //处理，遍历删除对应的Authorization
            Iterator<UserIdValue> it = uSet.iterator();
            while (it.hasNext()) {
                authorizationValueRedisService.deleteKey(it.next().getAuthorization());
            }
            userIdValueRedisService.deleteKey(userId);
        } catch (Exception e) {
            log.error("操作redis失败", e);
            response.setCodeAndMsgByEnum(ErrorCode.SEVER_INTERNAL_ERROR);
            return response;
        }

        return response;
    }


    /**
     * 每次拿到userId都可以在服务器内部自动更新一次token
     */
    private void updateToken(TreeSet<UserIdValue> uSet, Date createTokenAt) {
        //------------------更新过期token
        //计算当前时间所对应的过期时间:1、获取一个日历2、使用给定的date设置日历3、add4、日历getTime赋值给date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(createTokenAt);
        calendar.add(Calendar.SECOND, Const.SUBEXPIRETIME);
        Date expireDate = calendar.getTime();
        //最早的比目前早过期,全删,这里没用itr遍历，应该没问题
        while (uSet.first().getCreateAt().before(expireDate) && !uSet.isEmpty()) {
            uSet.remove(uSet.first());
        }
    }

    private void updateToken(String userId) {
        TreeSet<UserIdValue> uSet = userIdValueRedisService.get(userId);
        updateToken(uSet, new Date());
        if (uSet.isEmpty()) {
            userIdValueRedisService.deleteKey(userId);
        }
    }

}
