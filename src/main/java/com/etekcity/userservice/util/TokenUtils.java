package com.etekcity.userservice.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.UUID;

public class TokenUtils {
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    private static final String SECRET = "etekcity";
    private static final String ISS = "grape";

    // 过期时间是3600秒，既是1个小时
    private static final long EXPIRATION = 86400L;


    public static String getUUToken(){

        // 不是没有“-”是去掉了
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();

    }

    //这只是生成了一个jwt而已。
    public static String CreateToken(String email,boolean isRememberMe){
        Date issueAt = new Date();//签发时间
        Date expirationAt = new Date(System.currentTimeMillis()+EXPIRATION*1000);//有效截止时间
        //把截止时间存起来，和当前时间比对即可。
        //要有一个获取有效截止时间的功能，表（token，userid，expireAt）
        String token = Jwts.builder()
                .signWith(SignatureAlgorithm.ES512,SECRET )//算法，key
                .setIssuer(ISS)//签发人
                .setSubject(email)//这里用email，其实userid好一点，
                .setIssuedAt(issueAt)//签发时间
                .setExpiration(expirationAt)//截止时间
                .compact();
        return token;
    }

    public  static Claims getTokenBody(String token){
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token).getBody();
    }

    //从token中获取用户名，我这里用的是email 以后会改成 userid
    public static String getValue(String token){
        return getTokenBody(token).getSubject();
    }
    //是否过期,成了，我把时间改成一定过期的时间不就OK了
    public static boolean isExpiration(String token/*,Date date*/){
        return getTokenBody(token).getExpiration().before(new Date());
    }
}
