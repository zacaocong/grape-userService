package com.etekcity.userservice.aop.token;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;

import com.alibaba.fastjson.JSONObject;
import com.etekcity.userservice.request.ChangePasswordBody;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.etekcity.userservice.constant.ErrorCode;
import com.etekcity.userservice.dao.UserMapper;
import com.etekcity.userservice.redis.entity.AuthorizationValue;
import com.etekcity.userservice.redis.impl.RedisServiceImpl;
import com.etekcity.userservice.response.result.EmptyResult;
import com.etekcity.userservice.response.rsp.Response;
import com.etekcity.userservice.utils.CheckUtils;
import com.etekcity.userservice.constant.HeaderFields;
import com.etekcity.userservice.constant.Method;
import com.etekcity.userservice.request.RegisterAndLoginBody;
import com.etekcity.userservice.utils.MD5Utils;
import com.etekcity.userservice.utils.StringUtils;

@Aspect
@Component
@Slf4j
public class WebCheckAspect {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisServiceImpl<AuthorizationValue> redisService;

    /**
     * 空响应消息
     * */
    private static Response<EmptyResult> response = new Response<>();
    static {
        response.setResult(new EmptyResult());
    }

    /**
     * 定义切点为service包下所有方法
     * */
    @Pointcut("execution(public * com.etekcity.userservice.service.impl..*.*(..))")
    public void webCkeck() {

    }

    /**
     * 定义注解切点
     * */
    @Pointcut("@annotation(com.etekcity.userservice.aop.annotation.CheckToken)")
    public void logForCheckToken() {

    }

    /**
     * 检查邮箱,密码
     * 匹配参数为RequestBody类型的方法,也就是注册和登录
     * 功能为校验邮箱密码合法性，校验其是否未注册或已注册，若不满足正确流程则直接返回response
     * 捕获以下错误情况：
     *      1、邮箱不规范
     *      2、密码不规范
     *      3、未注册时登录
     *      4、已注册时注册
     *      5、已注册时登录，但密码错误，密码正确性校验
     * */
    @Around(value = "(webCkeck()&&args(requestBody))")
    public Object doAroundCheckEmailAndPassword(ProceedingJoinPoint proceedingJoinPoint,
                                                RegisterAndLoginBody requestBody) throws Throwable {
        //这里只拦截了两个方法，都是有请求体的，所以打印参数，没有请求体调用下方法则会报错
        log.info("Request Args  : {}", JSONObject.toJSONString(proceedingJoinPoint.getArgs()));
        log.info("Now method    : {}","doAroundCheckEmailAndPassword");
        String email = requestBody.getEmail();
        String password = requestBody.getPassword();
        log.info("method get email:{} and password:{}",email,password);
        //邮箱密码规范性校验
        log.info("check email and password style");
        if (email == null || !CheckUtils.checkEmail(email)) {
            response.setCodeAndMsgByEnum(ErrorCode.EMAIL_ILLEGAL);
            return response;
        }
        if (password == null || !CheckUtils.checkPassword(password)) {
            response.setCodeAndMsgByEnum(ErrorCode.PASSWORD_ILLEGAL);
            return response;
        }
        //判断是否已注册
        log.info("check the email is registered or not, read database");
        if (userMapper.getUserByEmail(email) == null) {
            //未注册，登录方法要求不能未注册
            log.info("unregister");
            if (proceedingJoinPoint.getSignature().getName().equals(Method.LOGIN)) {
                //未注册 && 调用方法为 登录时
                log.info("unregister && method is login");
                response.setCodeAndMsgByEnum(ErrorCode.EMAIL_UNEXIST);
                return response;
            }
            //未注册 && 调用方法为 注册 则进入UserService正常流程进行业务处理
        } else {
            //已注册，注册方法要求不能已注册
            log.info("registered");
            if (proceedingJoinPoint.getSignature().getName().equals(Method.REGISTER)) {
                //已注册 && 调用方法为 注册时
                log.info("registered && method is register");
                response.setCodeAndMsgByEnum(ErrorCode.EMAIL_REGISTERED);
                return response;
            }
            //已注册，检查登录密码是否正确，即属于该邮箱,在调用方法为登陆时检验
            if (proceedingJoinPoint.getSignature().getName().equals(Method.LOGIN)) {
                //生成加密后的暗文密码用于校验
                log.info("registered && method is login");
                //todo:密码校验这里能不能做一个封装处理，封装成一个方法  checkpasserror（password，usermapper。findpassword）
                String encryptedPassword;
                try {
                    encryptedPassword = MD5Utils.getMD5Str(password);
                } catch (NoSuchAlgorithmException e) {
                    log.error("请求加密算法失败，请检查是否存在该算法");
                    response.setCodeAndMsgByEnum(ErrorCode.SEVER_INTERNAL_ERROR);
                    return response;
                }
                if (!encryptedPassword.equals(userMapper.findPasswordByEmail(email))) {
                    //密码错误
                    log.info("registered && method is login && password error");
                    response.setCodeAndMsgByEnum(ErrorCode.PASSWORD_ERROR);
                    return response;
                }
            }

        }
        //正常流程
        log.info("check success");
        Object result;
        try {
            //执行方法
            result = proceedingJoinPoint.proceed();
         } catch (Exception e) {
            //异常则返回内部错误
            response.setCodeAndMsgByEnum(ErrorCode.SEVER_INTERNAL_ERROR);
            return response;
        }
        log.info("Method doAroundCheckEmailAndPassword return");
        return result;
    }

    /**
     * token校验
     * 匹配参数为HttpServletRequest类型的方法，也就是logout和getUserInfo
     * args(request,..)表示第一个参数为request，后面为任意参数
     * 检验两种情况：
     *      1、token有效
     *      2、token正确
     * */

    @Around(value = "(webCkeck()&&args(request,..))")
    public Object doAroundCheckRequestToken(ProceedingJoinPoint proceedingJoinPoint, HttpServletRequest request)
            throws Throwable {
        log.info("Now method    : {}","doAroundCheckRequestToken");
        //todo:其实这里可以用正则来完全匹配我想要的输入的，但是没这个要求就暂时不做了
        //获取头部消息authorization
        String authorization = request.getHeader(HeaderFields.AUTHORIZATION);

        //string处理，拆分，获得userId token
        String[] params = StringUtils.splitStrings(request.getHeader(HeaderFields.AUTHORIZATION));
        String userId = params[0];
        String token = params[1];
        log.info("get userId:{} and token:{}",userId,token);

        //需要userId，token有值才能继续，否则
        if (authorization == null || userId == null || token == null) {
            response.setCodeAndMsgByEnum(ErrorCode.TOKEN_NULL);
            return response;
        }

        //token验证，有效，且正确
        log.info("check token, read redis");
        if (!redisService.existsKey(token)) {
            response.setCodeAndMsgByEnum(ErrorCode.TOKEN_DISABLED);
            return response;
        }
        if (!redisService.get(token).getUserId().equals(userId)) {
            response.setCodeAndMsgByEnum(ErrorCode.TOKEN_ERROR);
            return response;
        }

        //正常流程
        Object result;
        try {
            //执行方法
            result = proceedingJoinPoint.proceed();
        } catch (Exception e) {
            //异常则返回内部错误
            response.setCodeAndMsgByEnum(ErrorCode.SEVER_INTERNAL_ERROR);
            return response;
        }
        return  result;
    }

    //todo:request,..  再来一个..XXbody，两个切面作用到一个service上
    //todo:新老密码合法性校验，老密码正确性校验，单独做一个切面，有三种方式1 注解 2 方法匹配 3 参数匹配

    /**
     * 用来校验changePassword的密码问题，
     * 由于该方法需要校验新老密码合法性以及老密码正确性，正确型校验还需加密比对，在业务中过于冗杂，所以在切面中处理
     * */
    @Around(value = "(webCkeck()&&args(request,requestBody))")
    public Object doAroundCheckNewOldPassword(ProceedingJoinPoint proceedingJoinPoint,HttpServletRequest request,
                                              ChangePasswordBody requestBody) throws Throwable  {
        log.info("Now method    : {}","doAroundCheckNewOldPassword");

        String oldPassword = requestBody.getOldPassword();
        String newPassword = requestBody.getNewPassword();
        log.info("get oldPassword:{},newPassword:{}",oldPassword,newPassword);

        String[] params = StringUtils.splitStrings(request.getHeader(HeaderFields.AUTHORIZATION));
        String userId = params[0];

        //新老密码合法性
        log.info("check password style");
        if (oldPassword  == null || !CheckUtils.checkPassword(oldPassword)) {
            response.setCodeAndMsgByEnum(ErrorCode.OLDPASSWORD_ILLEGAL);
            return response;
        }
        if (newPassword  == null || !CheckUtils.checkPassword(newPassword)) {
            response.setCodeAndMsgByEnum(ErrorCode.NEWPASSWORD_ILLEGAL);
            return response;
        }

        //老密码正确性
        String encryptedOldPassword;
        try {
            encryptedOldPassword = MD5Utils.getMD5Str(oldPassword);
        } catch (NoSuchAlgorithmException e) {
            log.error("请求加密算法失败，请检查是否存在该算法");
            response.setCodeAndMsgByEnum(ErrorCode.SEVER_INTERNAL_ERROR);
            return response;
        }
        log.info("encrypt the oldPassword:{}",encryptedOldPassword);

        if (!encryptedOldPassword.equals(userMapper.findPasswordById(userId))) {
            //密码错误
            log.info("oldPassword is error");
            response.setCodeAndMsgByEnum(ErrorCode.PASSWORD_ERROR);
            return response;
        }
        log.info("oldPassword is right");

        //正常流程
        Object result;
        try {
            //执行方法
            result = proceedingJoinPoint.proceed();
        } catch (Exception e) {
            //异常则返回内部错误
            response.setCodeAndMsgByEnum(ErrorCode.SEVER_INTERNAL_ERROR);
            return response;
        }
        return  result;
    }





}
