package com.etekcity.userservice.dao;

import org.apache.ibatis.annotations.*;
import java.sql.Timestamp;
import java.util.Date;

import com.etekcity.userservice.module.User;
import com.etekcity.userservice.module.UserInfo;

/**    @author Grape
 *     private String userId; 主键
 *     private String email;
 *     private String password;
 *     private String address;
 *     private String nickname;
 *     private Timestamp createAt;
 *     private Timestamp updateAt;
 *     数据库
 *     user_info  user_id email password address nickname create_at update_at
 * */
@Mapper
public interface UserMapper {

    /**
     * 查询邮箱是否存在，返回User对象
     * 注册会调用此方法
     * */
    @Results({
            @Result(property = "userId", column = "user_id"),
            @Result(property = "email", column = "email"),
            @Result(property = "password", column = "password"),
            @Result(property = "nickname", column = "nickname"),
            @Result(property = "address", column = "address"),
            @Result(property = "createAt", column = "create_at"),
            @Result(property = "updateAt", column = "update_at")
    })
    @Select("select * from user_info WHERE email = #{email}")
    User getUserByEmail(String email);

    /**
     * 插入用户信息
     * 注册会调用此方法
     * */
    @Insert("insert into  user_info (user_id,email,password,create_at,update_at) " +
            " values(#{userId},#{email},#{password},#{createAt},#{updateAt})")
    int insert(String userId, String email, String password, Date createAt,Date updateAt);

    /**
     * 根据邮箱查询密码
     * 登录会调用此方法
     * */
    @Select("select  password as password   from user_info    where email = #{email}")
    String findPasswordByEmail(String email);

    /**
     * 通过Id获取用户信息
     * @param userId not null
     * @return UserInfo
     * */
    @Select("select  " + "user_id as userId   , " + "email as email   , " + "nickname as nickname   , "
            + "address as address  , " + "create_at as createAt  , " + "update_at as updateAt   " + "  from user_info  "
             + "where user_id = #{userId}")
    UserInfo getUserById(String userId);

    /**
     * 更新用户信息
     *      通过Id来更新nickname
     * */
    @Update("UPDATE user_info" + " SET nickname=#{nickname} " + "WHERE user_id = #{userId}")
    int updateNicknamById(String nickname,String userId);

    /**
     * 更新用户信息
     *      通过Id来更新address
     * */
    @Update("UPDATE user_info" + " SET address=#{address} " + "WHERE user_id = #{userId}")
    int updateAddressById(String address,String userId);

    /**
     * 更新用户信息
     *      通过Id来更新address和nickname
     * */
    @Update("UPDATE user_info" + " SET address=#{address}   ,  " + "nickname=#{nickname} " + "WHERE user_id = #{userId}")
    int updateUserInfoById(String address,String nickname,String userId);

    /**
     * 修改密码
     *      通过Id来修改密码
     * */
    @Update("UPDATE user_info" + " SET password = #{newPassword} " + "WHERE user_id = #{userId}")
    int updatePasswordById(String newPassword,String userId);

    /**
     * 通过Id来查询密码
     * */
    @Select("select  password as password   from user_info    where user_id = #{userId}")
    String findPasswordById(String userId);
}
