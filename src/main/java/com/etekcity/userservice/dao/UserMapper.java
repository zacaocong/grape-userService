package com.etekcity.userservice.dao;

import java.util.Date;

import org.apache.ibatis.annotations.*;

import com.etekcity.userservice.model.User;


/**
 * UserMapper封装数据库操作
 *
 * @author Grape
 */

@Mapper
public interface UserMapper {

    /**
     * 查询邮箱是否存在，返回User对象
     * 注册会调用此方法
     *
     * @param email email
     * @return User
     */
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
     *
     * @param userId   userId
     * @param email    email
     * @param password password
     * @param createAt createAt
     * @param updateAt updateAt
     */
    @Insert("insert into user_info (user_id,email,password,create_at,update_at) values(#{userId},#{email},"
            + "#{password},#{createAt},#{updateAt})")
    void insert(String userId, String email, String password, Date createAt, Date updateAt);


    /**
     * 根据邮箱查询密码
     * 登录会调用此方法
     *
     * @param email email
     * @return String
     */
    @Select("select password as password from user_info where email = #{email}")
    String findPasswordByEmail(String email);

    /**
     * 通过Id获取用户信息
     * User中多出来的属性比如password会被自动赋值为空
     *
     * @param userId userId
     * @return UserInfo
     */
    @Select("select user_id as userId,email as email,nickname as nickname,address as address,"
            + " create_at as createAt,update_at as updateAt from user_info where user_id = #{userId}")
    User getUserInfoById(String userId);

    /**
     * 通过Id来更新nickname
     * 更新用户信息会调用此方法
     *
     * @param nickname nickname
     * @param updateAt updateAt
     * @param userId   userId
     * @return int
     */
    @Update("UPDATE user_info SET nickname=#{nickname},update_at=#{updateAt} WHERE user_id = #{userId}")
    int updateNicknameById(String nickname, Date updateAt, String userId);

    /**
     * 通过Id来更新address
     * 更新用户信息会调用此方法
     *
     * @param address  address
     * @param updateAt updateAt
     * @param userId   userId
     * @return int
     */
    @Update("UPDATE user_info SET address=#{address},update_at=#{updateAt} WHERE user_id = #{userId}")
    int updateAddressById(String address, Date updateAt, String userId);

    /**
     * 通过Id来更新address和nickname
     * 更新用户信息会调用此方法
     *
     * @param address  address
     * @param nickname nickname
     * @param userId   userId
     */
    @Update("UPDATE user_info SET address=#{address},nickname=#{nickname},update_at=#{updateAt} "
            + "WHERE user_id = #{userId}")
    void updateUserInfoById(String address, String nickname, Date updateAt, String userId);

    /**
     * 通过Id来修改密码
     * 修改密码会调用此方法
     *
     * @param newPassword newPassword
     * @param userId      userId
     */
    @Update("UPDATE user_info SET password = #{newPassword},update_at=#{updateAt} WHERE user_id = #{userId}")
    void updatePasswordById(String newPassword, Date updateAt, String userId);

    /**
     * 通过Id来查询密码
     *
     * @param userId userId
     * @return String
     */
    @Select("select password as password from user_info where user_id = #{userId}")
    String findPasswordById(String userId);
}
