package com.etekcity.userservice.redis.entity;

import java.util.Date;

/**
 * key： userId token
 * value 创建时间  userId
 *
 * @author grape
 * 在redis中第一个简单查询结构中  key：userId token  value：authorizationValue（userId， createAt）此处userId便于查询
 * 在redis第二个结构中            key：userId        value：TreeSet<UserIdValue>   (authorization,Date) 便于查上表
 */
public class AuthorizationValue {
    /**
     * userId 便于查询
     */
    private String userId;
    /**
     * userId token的创建时间
     */
    private Date createTokenTime;

    public AuthorizationValue() {

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

    public AuthorizationValue(String userId, Date createTokenTime) {
        this.userId = userId;
        this.createTokenTime = createTokenTime;
    }
}
