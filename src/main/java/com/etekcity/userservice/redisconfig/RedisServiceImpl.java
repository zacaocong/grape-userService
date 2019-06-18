package com.etekcity.userservice.redisconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;


@Service("RedisServiceImpl")
public class RedisServiceImpl<V> implements RedisService<String,V> {

    @Autowired
    private RedisTemplate<String,V> redisTemplate;

    @Override
    public boolean existsKey(String key) {
        //TODO:空指针
        return redisTemplate.hasKey(key);
    }

    @Override
    public boolean deleteKey(String key) {
        //TODO:空指针
        return redisTemplate.delete(key);
    }

    @Override
    public void expireKey(String key, long time, TimeUnit timeUnit) {
        redisTemplate.expire(key,time,timeUnit);
    }

    @Override
    public void expireKeyAt(String key, Date date) {
        redisTemplate.expireAt(key,date);
    }

    @Override
    public long getKeyExpire(String key, TimeUnit timeUnit) {
        //TODO:空指针
        return redisTemplate.getExpire(key,timeUnit);
    }

    @Override
    public void persistKey(String key) {
        redisTemplate.persist(key);
    }

    @Override
    public V get(String key) {
        ValueOperations<String,V> operations = redisTemplate.opsForValue();
        return operations.get(key);
    }

    @Override
    public boolean set(String key, V value) {
        boolean result = false;
        try {
            ValueOperations<String,V> operations = redisTemplate.opsForValue();
            operations.set(key,value);
            result = true;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean set(String key, V value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<String,V> operations = redisTemplate.opsForValue();
            operations.set(key,value);
            redisTemplate.expire(key,expireTime,TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Set<String> redisKeys() {
        return redisTemplate.keys("*");
    }
}
