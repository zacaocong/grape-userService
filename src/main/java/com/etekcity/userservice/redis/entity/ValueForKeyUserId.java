package com.etekcity.userservice.redis.entity;

import java.util.TreeSet;

public class ValueForKeyUserId {
    /**
     * 为了保证同一时刻只有一个线程可以操作该对象
     * */
    public boolean accessible = true;

    private TreeSet<ValueTokenAndAtOfKeyUserId> uSet;

}
