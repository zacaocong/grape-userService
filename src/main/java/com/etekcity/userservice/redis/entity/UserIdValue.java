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
//    private String authorization;

    /**
     * token 该用户的token，此处无法保证一定有效，但由于存储了生成时间，在知道生存周期的情况下可以手动更新
     * */
    private String token;

    /**
     * Date可以转Calendar可以操作，比较before after
     */
    private Date createAt;


//    public String getAuthorization() {
//        return authorization;
//    }
//
//    public void setAuthorization(String authorization) {
//        this.authorization = authorization;
//    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }


    /**
     * 反序列化需要一个空的构造函数
     * */
    public UserIdValue() {

    }

    public UserIdValue(String token, Date createAt) {
        this.token = token;
        this.createAt = createAt;
    }

    @Override
    public int compareTo(UserIdValue o) {
        //date也有compareto,这里顺序还不确定，就是由早到晚
        return createAt.compareTo(o.getCreateAt());
    }
}
