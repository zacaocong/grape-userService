package com.etekcity.userservice.redis.impl;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.etekcity.userservice.redis.RedisService;

/**
 * Redis操作的封装实现类
 *
 * @author grape
 */
@Slf4j
@Service("RedisServiceImpl")
public class RedisServiceImpl<V> implements RedisService<String, V> {

    @Autowired
    private RedisTemplate<String, V> redisTemplate;

    @Override
    public boolean existsKey(String key) {
        log.info("method: existsKey ,key:{}", key);
        return redisTemplate.hasKey(key);
    }

    @Override
    public boolean deleteKey(String key) {
        log.info("method: deleteKey ,key:{}", key);
        return redisTemplate.delete(key);
    }

    @Override
    public void expireKey(String key, long time, TimeUnit timeUnit) {
        log.info("set expireAt by time,method: expireKey,key:{}", key);
        redisTemplate.expire(key, time, timeUnit);
    }

    @Override
    public void expireKeyAt(String key, Date date) {
        log.info("set expireAt by date,method: expireKeyAt,key:{}", key);
        redisTemplate.expireAt(key, date);
    }

    @Override
    public long getKeyExpire(String key, TimeUnit timeUnit) {
        log.info("get expire time,method: getKeyExpire,key:{}", key);
        return redisTemplate.getExpire(key, timeUnit);
    }

    @Override
    public void persistKey(String key) {
        log.info("method: psersistKey,key:{}", key);
        redisTemplate.persist(key);
    }

    @Override
    public V get(String key) {
        log.info("method: get,key:{}", key);
        ValueOperations<String, V> operations = redisTemplate.opsForValue();
        return operations.get(key);
    }

    @Override
    public void set(String key, V value) {
        log.info("method: set,key:{},value:{}", key, value);
        ValueOperations<String, V> operations = redisTemplate.opsForValue();
        operations.set(key, value);
    }

    @Override
    public void set(String key, V value, Long expireTime) {
        log.info("method: set,key:{},value:{},expireTIme:{}", key, value, expireTime);
        ValueOperations<String, V> operations = redisTemplate.opsForValue();
        operations.set(key, value);
        redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
    }

    /**
     * 获取指定key的值并重新写入
     * 这里可以更新时间
     * 在该场景中主要用来添加，这里的long给一个86400即可完成更新
     */
    public void getAndSetAddUpdateAt(String key, V value, Long expireTime) {
        ValueOperations<String, V> operations = redisTemplate.opsForValue();
        operations.getAndSet(key, value);
        redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
    }

    /**
     * 获取指定key的值并重新写入
     * 这里不更新时间
     * 在该场景中主要用来删减
     */
    public void getAndSetJustCut(String key, V value) {
        ValueOperations<String, V> operations = redisTemplate.opsForValue();
        operations.getAndSet(key, value);
    }

}
