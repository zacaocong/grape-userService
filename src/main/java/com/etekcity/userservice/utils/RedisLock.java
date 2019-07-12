package com.etekcity.userservice.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 *
 *
 * @author grape
 * @date 2019/07/11
 * */
public class RedisLock {
    private static Logger logger = LoggerFactory.getLogger(RedisLock.class);

    /////////////////////静态常量定义//////////////////////
    /**
     * 存储到redis中的锁标志
     * 原本的默认value
     * */
    private static final String LOCKED = "LOCKED";

    /**
     * 默认请求锁的超时时间（ms 毫秒）
     * */
    private static final long TIME_OUT = 30;

    /**
     * 默认锁的有效时间（s）
     * */
    private static final int EXPIRE = 6;
    /////////////////////////静态常量定义结束///////////////////////////

    /**
     * 锁标志对应的key
     * userId去掉-，StringUtils里有函数
     * */
    private String lockKeyUserId;

    /**
     * value存点啥，存个当前时间戳的字符串吧
     * */
    private String value = LOCKED;

    /**
     * 请求锁的超时时间（ms）
     * 本地看
     * */
    private long timeOut = TIME_OUT;

    /**
     * 锁的有效时间（s）
     * 存入redis
     * */
    private long expireTime = EXPIRE;

    /**
     * 锁flag
     * 对象中的标志
     * 就删key放锁的时候if了一下
     * */
    private volatile boolean isLocked = false;

    /**
     * redis模板
     * */
    private StringRedisTemplate redisTemplate;

    /**
     * 构造方法
     *
     * @param redisTemplate redis管理模板
     * @param userId        userId 用来生成 key
     * @param value         value  设置存储字符串
     * @param expireTime    key过期时间
     * @param timeOut
     *
     * */
    public RedisLock(StringRedisTemplate redisTemplate, String userId, String value, long expireTime, long timeOut) {
        this.lockKeyUserId = StringUtils.lockKeyUserId(userId);
        this.value = value;
        this.redisTemplate = redisTemplate;
        this.expireTime = expireTime;
        this.timeOut = timeOut;
    }

    /**
     * 构造方法
     *
     * @param redisTemplate Redis管理模板
     * @param userId        锁定key
     * @param expireTime    锁定过期时间（秒）
     * @param timeOut       请求锁超时时间（毫秒）
     * */
    public RedisLock(StringRedisTemplate redisTemplate, String userId, long expireTime, long timeOut) {
        this.lockKeyUserId = StringUtils.lockKeyUserId(userId);
        this.expireTime = expireTime;
        this.timeOut = timeOut;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 构造方法
     *
     * @param redisTemplate Redis模板
     * @param userId        锁定key
     * @param expireTime    锁定过期
     * */
    public RedisLock(StringRedisTemplate redisTemplate, String userId, long expireTime) {
        this.lockKeyUserId = StringUtils.lockKeyUserId(userId);
        this.expireTime = expireTime;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 构造方法（默认请求锁超时时间30秒，锁过期时间60秒）
     *
     * @param redisTemplate Redis管理模板
     * @param userId        锁定key
     * */
    public RedisLock(StringRedisTemplate redisTemplate, String userId) {
        this.lockKeyUserId = StringUtils.lockKeyUserId(userId);
        this.redisTemplate = redisTemplate;
    }

    /**
     * 请求锁+加锁
     *
     *
     * */
    public boolean lock() throws Exception {
        //当前系统时间，毫秒
        long nowTime = System.currentTimeMillis();
        //请求锁超时时间，毫秒
        long timeout = timeOut;
        //随机数,休眠时会用
        final Random random = new Random();

        //不断循环向Master节点请求锁，当请求时间（System.naoTime（） - nowTime）超过设定的超时时间则放弃请求锁
        //这个可以防止一个客户端在某个宕掉的master节点上阻塞过长时间
        //如果一个master节点不可用了，应该尽快尝试下一个master节点
        while ((System.currentTimeMillis() - nowTime) < timeout) {
            //将lockKeyUserId value存储到redis缓存中，存储成功则获得锁，空则可存，不然sleep再试，超时为止
            if (redisTemplate.opsForValue().setIfAbsent(lockKeyUserId, value)) {
                isLocked = true;
                //设置锁的有效期， 也是锁的自动释放时间， 也是一个客户端在其他客户端能抢占锁之前可以执行任务的时间
                //可以防止因异常情况无法释放锁而造成死锁的情况发生
                redisTemplate.expire(lockKeyUserId, expireTime, TimeUnit.SECONDS);
                //上锁成功结束请求
                break;
            }
            //获取锁失败时，随机延时后进行重试
            //休眠 指定的毫秒 + 指定的纳秒
            Thread.sleep(10, random.nextInt(50000));
        }
        return isLocked;
    }


    /**
     * 判断锁没锁
     * true 有key，锁了
     * false 没key，没锁
     * */
    public boolean isLock() {
        return redisTemplate.hasKey(lockKeyUserId);
    }

    /**
     * 释放锁
     * */
    public void unlock() {
        //释放锁
        //不管请求锁是否成功， 只要已经上锁， 客户端都会进行释放锁的操作
        if (isLocked) {
            redisTemplate.delete(lockKeyUserId);
        }
    }

}
























































