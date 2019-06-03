package com.grape.cloud.dao.mapper;


import com.grape.cloud.model.TestUser;
import com.grape.cloud.model.UserTest;
import org.apache.ibatis.annotations.*;

//数据库 usertest表 name age,为了防止混乱，下面的name age都是代表数据库的，不是的一律换名
public interface TestUserMapper {
    //-----------------------------插入新数据--------------------------------
    //-----------------------------缺值插入
    @Insert("insert into    usertest(name)            values(#{nameTest})")
    int insertOne(String nameTest);//待测试
    @Insert("insert into    usertest(name,age)        values(#{nameTest},#{ageTest})")
    int insertTwo(String nameTest);//待测试
    //-----------------------------全值插入
    @Insert("insert into    usertest(name,age)        values(#{nameTestXXX},#{ageTestXXX})")
    int insert(String nameTestXXX,Integer ageTestXXX);//测试成功
    //-----------------------------对象插入
    @Insert("insert into    usertest(name,age)        values(#{nameTest},#{ageTest})")
    int insertByObject(TestUser userTest);//测试成功,出错了，我也是醉了，换个类全对了，这里的注解忘了加Test，现在好了

    @Insert("insert into    usertest(name,age)        values(#{name},#{age})")
    int insertByObjectTest(UserTest userTest);//测试成功，这个类只有name 和age  这可能和名字有关

    //-----------------------------数据查询-----------------------------------
    //-----------------------------as对应
    @Select("select     name as nameTest,age as ageTest   from usertest     where name = #{nameTestXXX}")
    TestUser findUserByName(String nameTestXXX);//语法错误                  删掉上面这个逗号where前面的
    //成了                 ，as是数据库列名和类中属性的对应
    //-----------------------------@Result对应
    @Results({
            @Result(property = "nameTest", column = "name"),
            @Result(property = "ageTest", column = "age")
    })//数据库列名和类中属性对应      property类中属性 column数据库列    #{里面叫什么无所谓}只是传给下面函数的
    @Select("select * from usertest WHERE name = #{nameTestXXX}")//大小写不敏感
    TestUser getUserByName(String nameTestXXX);//name值可以查到但是age都是null，这是因为没有写映射就是Result


    //================================================================

    @Select("select * from usertest where name = #{nameTestXXX}")
    TestUser obtainUserByName(@Param("nameTestXXX") String nameTestXXX);//待测试,这么写是错的
    //-----------------------------数据删除------------------------------------

    @Delete("delete from usertest where name = #{nameTestXXX}")
    int deleteUserByName(String nameTestXXX);//待测试，成功，删完返回1


    //-----------------------------数据更新-------------------------------------

    @Update("UPDATE usertest SET age=#{ageTest} WHERE name = #{nameTest}")
    int update(TestUser userTest);//先试试这个做能不能过，看看它够不够智能

}
