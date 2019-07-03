package com.etekcity.userservice.redis.entity;

import java.util.Date;

/**
 * userId value
 * 为了能逆向操作，根据userId获取 token方面全部信息
 * 这个是用来构造一个大小为5的数组或集合的
 *
 * @author grape
 */
public class UserIdValue implements Comparable<UserIdValue> {
    //todo：总说我没重写equals 和 hashcode
    /**
     * userId token
     * createAt
     */
    private String authorization;
    /**
     * Date可以转Calendar可以操作，比较before after
     */
    private Date createAt;

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public UserIdValue() {

    }

    public UserIdValue(String authorization, Date createAt) {
        this.authorization = authorization;
        this.createAt = createAt;
    }

    @Override
    public int compareTo(UserIdValue o) {
        //date也有compareto,这里顺序还不确定
        //todo：不确定顺序
        return createAt.compareTo(o.getCreateAt());
    }
}
