package com.etekcity.userservice.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * redis的工具类/其实这个应该叫redisUtil放到utils里
 * key存在
 * 重命名keyname，如果newkey已经存在，则newkey的原值被覆盖
 * newKey不存在时才重命名
 * 批量删除对应的value
 * 删除对应的value
 * 批量删除key
 * 删除key
 * 删除多个key
 * 删除key的集合
 * 设置key的生命周期
 * 指定key在指定的日期过期
 * 查询key的生命周期
 * 设置为永久有效
 * 读取缓存
 * 写入缓存
 * */

@Component
public class RedisUtils {
    @Autowired
    private RedisTemplate redisTemplate;//这个应该没问题
    //private RedisTemplate<String,String> redisTemplate;//这个是string string的，可能需要改

    //默认过期时长，单位：秒
    public static final long DEFAULT_EXPIRE = 86400;
    //不设置过期时长
    public static final long NOT_EXPIRE = -1;

    /**
     * key存在
     *
     * @param key
     * @return
     * */
    public boolean existsKey(String key){
        return redisTemplate.hasKey(key);
    }

    /**
     * 重命名key，如果newkey已经存在，则newkey的原值被覆盖
     *
     * @param oldKey
     * @param newKey
     * @return
     * */
    public void renameKey(String oldKey,String newKey){
        redisTemplate.rename(oldKey,newKey);
    }

    /**
     * newKey不存在时才重命名
     *
     * @param oldKey
     * @param newKey
     * @return 修改成功返回true
     * */
    public boolean renameKeyNotExist(String oldKey,String newKey){
        return redisTemplate.renameIfAbsent(oldKey,newKey);
    }

    /**
     * 批量删除对应的value
     *
     * @param  keys
     * */
    public void remove(final String... keys){
        for(String key:keys){
            remove(key);
        }
    }
    /**
     * 删除对应的value
     *
     * @param key
     * */
    public void remove(final String key){
        if(existsKey(key)){
            redisTemplate.delete(key);
        }
    }


    /**
     * 批量删除key
     *
     * @param pattern
     * */
//    public void removePattern(final String pattern){
//        Set<Serializable> keys=redisTemplate.keys(pattern);
//        if(keys.size()>0){
//            redisTemplate.delete(keys);
//        }
//    }//报错了


    /**
     * 删除key
     *
     * @param key
     * */
    public void deleteKey(String key){
        redisTemplate.delete(key);
    }

    /**
     * 删除多个key
     *
     * @param keys
     * */
    public void deleteKey(String... keys){
        Set<String> kSet = Stream.of(keys).map(k->k).collect(Collectors.toSet());
        redisTemplate.delete(kSet);
    }

    /**
     * 删除key的集合
     *
     * @param keys
     * */
    public void deleteKey(Collection<String> keys){
        Set<String> kSet = keys.stream().map(k->k).collect(Collectors.toSet());
        redisTemplate.delete(kSet);
    }


    /**
     * 设置key的生命周期
     *
     * @param key
     * @param time
     * @param timeUnit
     * */
    public void expireKey(String key, long time, TimeUnit timeUnit){
        redisTemplate.expire(key,time,timeUnit);
    }

    /**
     * 指定key在指定的日期过期
     *
     * @param key
     * @param date
     *
     * */
    public void expireKeyAt(String key, Date date){
        redisTemplate.expireAt(key,date);
    }

    /**
     * 查询key的生命周期
     *
     * @param key
     * @param timeUnit
     * @return
     * */
    public long getKeyExpire(String key,TimeUnit timeUnit){
        return redisTemplate.getExpire(key,timeUnit);
    }

    /**
     * 将key设置为永久有效
     *
     * @param key
     * */
    public void persistKey(String key){
        redisTemplate.persist(key);
    }

    /**
     * 读取缓存
     *
     * @param key
     * @return
     * */
    public Object get(final String key){
        Object result = null;
        ValueOperations<Serializable,Object> operations = redisTemplate.opsForValue();//这一行什么意思
        result = operations.get(key);
        return result;
    }//为什么没了呢，因为有效期太短了，没等你查呢就过期了

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     * */
    public boolean set(final String key,Object value){
        boolean result = false;
        try{
            ValueOperations<Serializable,Object> operations = redisTemplate.opsForValue();
        }catch(Exception e){
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @param expireTime
     * @return
     * */
    public boolean set(final String key,Object value,Long expireTime){
        boolean result = false;
        try{
            ValueOperations<Serializable,Object> operations = redisTemplate.opsForValue();
            operations.set(key,value);
            redisTemplate.expire(key,expireTime,TimeUnit.SECONDS);
            result = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }//成功

    /**
     * 查看库内key
     * @return
     * */
    public Set redisKeys() {
        return redisTemplate.keys("*");
    }//成功


}