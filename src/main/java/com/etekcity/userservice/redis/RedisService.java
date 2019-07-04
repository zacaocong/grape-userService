package com.etekcity.userservice.redis;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * RedisService接口规范
 *
 * @author grape
 */
public interface RedisService<K, V> {

    /**
     * 判断key是否有内容
     *
     * @param key key
     * @return boolean
     */
    boolean existsKey(final K key);

    /**
     * 删除key
     *
     * @param key key
     */
    boolean deleteKey(final K key);

    /**
     * 设置key的生命周期
     *
     * @param key      key
     * @param time     time
     * @param timeUnit timeUnit
     */
    void expireKey(final K key, long time, TimeUnit timeUnit);

    /**
     * 指定key在指定的日期过期
     *
     * @param key  key
     * @param date date
     */
    void expireKeyAt(final K key, Date date);

    /**
     * 查询key的生命周期
     *
     * @param key      key
     * @param timeUnit timeUnit
     * @return long
     */
    long getKeyExpire(final K key, TimeUnit timeUnit);

    /**
     * 将key设置为永久有效
     *
     * @param key key
     */
    void persistKey(final K key);

    /**
     * 读取数据
     *
     * @param key key
     * @return V
     */
    V get(final K key);

    /**
     * 写入缓存
     *
     * @param key   key
     * @param value value
     */
    void set(final K key, V value);

    /**
     * 写入缓存
     *
     * @param key        key
     * @param value      value
     * @param expireTime expireTime
     */
    void set(final K key, V value, Long expireTime);

}
