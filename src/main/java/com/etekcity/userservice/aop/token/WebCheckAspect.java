package com.etekcity.userservice.aop.token;

import javax.servlet.http.HttpServletRequest;

import com.etekcity.userservice.redis.entity.TokenValue;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.etekcity.userservice.constant.ErrorCode;
import com.etekcity.userservice.constant.HeaderFields;
import com.etekcity.userservice.constant.Method;
import com.etekcity.userservice.dao.UserMapper;
import com.etekcity.userservice.redis.impl.RedisServiceImpl;
import com.etekcity.userservice.request.RegisterAndLoginBody;
import com.etekcity.userservice.response.rsp.Response;
import com.etekcity.userservice.utils.CheckUtils;
import com.etekcity.userservice.utils.MD5Utils;
import com.etekcity.userservice.utils.StringUtils;

/**
 * 拦截校验参数
 *
 * @author grape
 */
@Aspect
@Component
@Slf4j
public class WebCheckAspect {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisServiceImpl<TokenValue> tokenValueRedisService;

    /**
     * 定义切点为service包下所有方法
     */
    @Pointcut("execution(public * com.etekcity.userservice.service.impl..*.*(..))")
    public void webCheck() {

    }

    /**
     * 定义注解切点
     */
    @Pointcut("@annotation(com.etekcity.userservice.aop.annotation.CheckToken)")
    public void logForCheckToken() {

    }

    /**
     * 检查邮箱,密码
     * 匹配参数为RequestBody类型的方法,也就是注册和登录
     * 功能为校验邮箱密码合法性，校验其是否未注册或已注册，若不满足正确流程则直接返回response
     * 捕获以下错误情况：
     * 1、邮箱不规范
     * 2、密码不规范
     * 3、未注册时登录
     * 4、已注册时注册
     * 5、已注册时登录，但密码错误，密码正确性校验
     */
    @Around(value = "(webCheck()&&args(requestBody))")
    public Object doAroundCheckEmailAndPassword(ProceedingJoinPoint proceedingJoinPoint,
                                                RegisterAndLoginBody requestBody) throws Throwable {
        //空体响应消息
        //获取body参数，email password
        log.debug(requestBody.getEmail());
        String email = requestBody.getEmail();
        String password = requestBody.getPassword();
        //邮箱密码规范性校验
        if (email == null || !CheckUtils.checkEmail(email)) {
            return Response.emptyResp(ErrorCode.EMAIL_ILLEGAL);
        }
        if (password == null || !CheckUtils.checkPassword(password)) {
            return Response.emptyResp(ErrorCode.PASSWORD_ILLEGAL);
        }
        //数据库操作
        try {
            //判断是否已注册
            if (userMapper.getUserByEmail(email) == null) {
                //未注册，登录方法要求不能未注册
                if (proceedingJoinPoint.getSignature().getName().equals(Method.LOGIN)) {
                    //未注册 && 调用方法为 登录时
                    return Response.emptyResp(ErrorCode.EMAIL_UNEXIST);
                }
                //未注册 && 调用方法为 注册 则进入UserService正常流程进行业务处理
            } else {
                //已注册，注册方法要求不能已注册
                if (proceedingJoinPoint.getSignature().getName().equals(Method.REGISTER)) {
                    //已注册 && 调用方法为 注册时
                    return Response.emptyResp(ErrorCode.EMAIL_REGISTERED);
                }
                //已注册，检查登录密码是否正确，即属于该邮箱,在调用方法为登陆时检验
                if (proceedingJoinPoint.getSignature().getName().equals(Method.LOGIN)) {
                    //生成加密后的暗文密码用于校验
                    String encryptedPassword= MD5Utils.getMD5Str(password);
                    if (!encryptedPassword.equals(userMapper.findPasswordByEmail(email))) {
                        //密码错误
                        return Response.emptyResp(ErrorCode.PASSWORD_ERROR);
                    }
                }

            }
        } catch (Exception e) {
            log.error("邮箱密码校验时发生异常", e);
            return Response.emptyResp(ErrorCode.SEVER_INTERNAL_ERROR);
        }

        //原有流程
        try {
            //执行方法
            return proceedingJoinPoint.proceed();
        } catch (Exception e) {
            //异常则返回内部错误
            log.error("internal error", e);
            return Response.emptyResp(ErrorCode.SEVER_INTERNAL_ERROR);
        }
    }

    /**
     * token校验
     * 匹配参数为HttpServletRequest类型的方法，也就是logout和getUserInfo
     * args(request,..)表示第一个参数为request，后面为任意参数
     * 检验两种情况：
     * 1、token有效
     * 2、token正确
     */
    @Around(value = "(webCheck()&&args(request,..))")
    public Object doAroundCheckRequestToken(ProceedingJoinPoint proceedingJoinPoint, HttpServletRequest request)
            throws Throwable {
        //获取头部消息authorization
        String authorization = request.getHeader(HeaderFields.AUTHORIZATION);
        //传入为userId token，校验authorization合法性
        if (!CheckUtils.checkAuthor(authorization)) {
            return Response.emptyResp(ErrorCode.TOKEN_ILLEGAL);
        }
        //authorization为合法参数，获取userId和token
        String[] params = StringUtils.splitStrings(authorization);
        String userId = params[0];
        String token = params[1];

        //token验证，有效，且正确
        //读redis，是否存在该userId:token
        try {
            if (!tokenValueRedisService.existsKey(token)) {
                return Response.emptyResp(ErrorCode.TOKEN_DISABLED);
            }
            if (!tokenValueRedisService.get(token).getUserId().equals(userId)) {
                return Response.emptyResp(ErrorCode.TOKEN_ERROR);
            }
        } catch (Exception e) {
            log.error("redis exist 校验时发生异常", e);
            return Response.emptyResp(ErrorCode.SEVER_INTERNAL_ERROR);
        }

        //原有流程
        try {
            //执行方法
            return proceedingJoinPoint.proceed();
        } catch (Exception e) {
            //异常则返回内部错误
            log.error("internal error", e);
            return Response.emptyResp(ErrorCode.SEVER_INTERNAL_ERROR);
        }
    }
}
