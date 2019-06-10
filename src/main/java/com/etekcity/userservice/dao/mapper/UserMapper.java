package com.etekcity.userservice.dao.mapper;

import com.etekcity.userservice.moudle.User;

import com.etekcity.userservice.moudle.UserInfo;
import org.apache.ibatis.annotations.*;

import java.sql.Timestamp;

//1mapper 2service 3impl
/*
*   private String id; 主键
    private String email;
    private String password;
    private String address;
    private String nickname;
    private Timestamp createAt;
    private Timestamp updateAt;
    * private String token;
    *
    *userinfo  user_id email password address nickname create_at update_at token
* */
@Mapper
public interface UserMapper {
    //---------------------------------注册
    //查询邮箱存在,返回User对象
    @Results({
            @Result(property = "id", column = "user_id"),
            @Result(property = "email", column = "email"),
            @Result(property = "password", column = "password"),
            @Result(property = "nickname", column = "nickname"),
            @Result(property = "address", column = "address"),
            @Result(property = "createAt", column = "create_at"),
            @Result(property = "updateAt", column = "update_at")
//            @Result(property = "token", column = "token")
    })//数据库列名和类中属性对应      property类中属性 column数据库列    #{里面叫什么无所谓}只是传给下面函数的
    @Select("select * from userinfo WHERE email = #{email}")//大小写不敏感
    User getUserByEmail(String email);
    //邮箱密码userid创建时间写入
    @Insert("insert into   userinfo (user_id,email,password,create_at)     values(#{id},#{email},#{password},#{createAt})")
    int insert(String id, String email, String password, Timestamp createAt);

    //-------------------------------登录
    //根据邮箱查密码
    @Select("select  password as password   from userinfo    where email = #{email}")
    String findPasswordByEmail(String email);

    //-------------------------------登出，token失效，不在这

    //-------------------------------获取信息
    @Select("select  " +
            "user_id as userId   , " +//所有的数据都正常，就他没了是null，因为后面写的是id不是UserInfo里的userId
            "email as email   , " +
            "nickname as nickname   , " +
            "address as address  , " +
            "create_at as createAt  , " +
            "update_at as updateAt   " +
            "  from userinfo    " +
            "where user_id = #{id}")//前面的是库里的，后面的是程序里的
    UserInfo getUserById(String id);

    //-------------------------------更新用户信息
    @Update("UPDATE userinfo" +
            " SET nickname=#{nickname} " +
            "WHERE user_id = #{id}")
    int UpdateNicknamByID(String nickname,String id);
    @Update("UPDATE userinfo" +
            " SET address=#{address} " +
            "WHERE user_id = #{id}")
    int UpdateAddressByID(String address,String id);
    @Update("UPDATE userinfo" +
            " SET address=#{address}   ,  " +
            "nickname=#{nickname} " +
            "WHERE user_id = #{id}")
    int UpdateUserInfoByID(String address,String nickname,String id);

    //-------------------------------修改用户密码
    @Update("UPDATE userinfo" +
            " SET password=#{newPassword}  " +
            "WHERE user_id = #{id}")
    int UpdatePasswordByID(String newPassword,String id);

    //根据ID查密码
    @Select("select  password as password   from userinfo    where user_id = #{userId}")
    String findPasswordByID(String userId);


}
