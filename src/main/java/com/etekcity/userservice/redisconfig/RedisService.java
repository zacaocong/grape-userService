package com.etekcity.userservice.redisconfig;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface RedisService<K,V> {

    /**
     * 判断key是否有内容
     * @param key
     * @return
     * */
    boolean existsKey(final K key);

    /**
     * 删除key
     * @param key
     * */
    boolean deleteKey(final K key);

    /**
     * 设置key的生命周期
     * @param key
     * @param time
     * @param timeUnit
     * */
    void expireKey(final K key, long time, TimeUnit timeUnit);

    /**
     * 指定key在指定的日期过期
     * @param key
     * @param date
     * */
    void expireKeyAt(final K key, Date date);

    /**
     * 查询key的生命周期
     * @param key
     * @param timeUnit
     * @return
     * */
    long getKeyExpire(final K key,TimeUnit timeUnit);

    /**
     * 将key设置为永久有效
     * @param key
     * */
    void persistKey(final K key);

    /**
     * 读取数据
     * @param key
     * @return
     * */
    V get(final K key);

    /**
     *
     * */

    /**
     * 写入缓存
     * @param key
     * @param value
     * @return
     * */
    boolean set(final K key,V value);

    /**
     * 写入缓存
     * @param key
     * @param value
     * @param expireTime
     * @return
     * */
    boolean set(final K key,V value,Long expireTime);


    /**
     * 查看库内key
     * @return
     * */
    Set<K> redisKeys();
}
