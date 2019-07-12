package com.etekcity.userservice.redis.entity;

import java.util.Date;

/**
 * key   token  为了保证需求，返回token错误码
 * value 创建时间  userId
 *
 * 正常情况下是不会把userId这种信息和token一起传过来的，这里只是考虑到一个简单的匹配，正常情况为加密解密
 *
 * @author grape
 * 在redis中第一个简单查询结构中  key：token  value：TokenValue（userId， createAt）此处userId便于查询
 * 在redis第二个结构中            key：userId        value：TreeSet<ValueTokenAndAtOfKeyUserId>   (authorization,Date) 便于查上表
 */
public class TokenValue {
    /**
     * userId 便于查询
     */
    private String userId;
    /**
     * userId token的创建时间
     */
    private Date createTokenTime;

    /**
     * 反序列化需要
     * */
    public TokenValue() {

    }

    public TokenValue(String userId, Date createTokenTime) {
        this.userId = userId;
        this.createTokenTime = createTokenTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreateTokenTime() {
        return createTokenTime;
    }

    public void setCreateTokenTime(Date createTokenTime) {
        this.createTokenTime = createTokenTime;
    }
}
