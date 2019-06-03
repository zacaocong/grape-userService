package com.grape.cloud.controller;


import com.grape.cloud.dao.mapper.TestUserMapper;
import com.grape.cloud.model.Echo;
import com.grape.cloud.model.Fizzle;
import com.grape.cloud.model.TestUser;
import com.grape.cloud.model.UserTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class TestContrlloer {

    @RequestMapping("/hello")
    public String hello(){return "Hello World!";}

    @Autowired
    private TestUserMapper testUserMapper;
    //数据库 usertest表 name age
    //-------------------------插入-------------------------
    @RequestMapping("/testsql1")
    public void TestInsert1(){
        //准备好插入对象后，直接调用mapper接口即可
        testUserMapper.insert("xiaoxiaoxiao",222);
    }//成功11
    @RequestMapping("/testsql11")
    public int TestInsert11(){
        //准备好插入对象后，直接调用mapper接口即可
        return testUserMapper.insert("xiaoxiaoxiaoxiao",2222);//成功返回了1，需要清楚的是1代表成功还是成功地改变了一个什么
    }//成功会返回1，失败会直接报一堆错11
    @RequestMapping("/testsql2")
    public TestUser TestInsert2(){
        TestUser testUser = new TestUser();
        testUser.setAgeTest(100);
        testUser.setNameTest("xiaochuan");
        //准备好插入对象后，直接调用mapper接口即可
        testUserMapper.insertByObject(testUser);
        return testUser;
    }//成功，出错，mapper注解值名无Test所以对应不到TestUser ageTest nameeTest
    @RequestMapping("/testsql3")
    public UserTest TestInsert3(){
        UserTest userTest = new UserTest("xiaochuan",100);
        userTest.setAge(100);
        userTest.setName("xiaochuan");
        testUserMapper.insertByObjectTest(userTest);
        return userTest;
    }//成功

    //--------------------------------查询--------------------------------
    @RequestMapping("/testSelect")
    public TestUser TestSelect(){
        return testUserMapper.findUserByName("xiaochuan");
    }//正常
    @RequestMapping("/testSelect2")
    public TestUser TestSelect2(){
        return testUserMapper.getUserByName("xiaochuan");//age null，正常
    }
    //-----------------------------接收URL参数，下面是直接根据URL的参数来查值。，没有返回空值
    @RequestMapping("/testSelect3")
    public TestUser TestSelect3(@RequestParam(value="name",required=true) String nameTest){
        //函数接收URL传参 localhost:8080/testSelect3?name="postman那边输入的值"
        return testUserMapper.getUserByName(nameTest);//age null,未对应数据库名和本地类名
    }
    @RequestMapping("/testSelect4")
    public TestUser TestSelect4(@RequestParam(value="name",required=true) String nameTest){
        //函数接收URL传参 localhost:8080/testSelect4?name="postman那边输入的值"
        return testUserMapper.findUserByName(nameTest);//正常
    }
    @RequestMapping("/testSelect5")
    public  TestUser TestSelect5(@RequestParam(value="name",required=true) String nameTest){
        //函数接收URL传参 localhost:8080/testSelect5?name="postman那边输入的值"
        return testUserMapper.obtainUserByName(nameTest);
    }

    //-------------------------------删除------------------------------
    @RequestMapping("/testDelete")
    public int TestDelete(@RequestParam(value="name",required=true) String nameTest){
        return testUserMapper.deleteUserByName(nameTest);//成功
    }

    //-------------------------------更新------------------------------

    public int TestUpdate(){
        return 1;
    }//待完成

    //------------------------------接受JSON 返回JSON---------------------
    @RequestMapping("/JSON")
    @ResponseBody//由于有RestController，所以这个ResponseBody不是必须的。
    public Fizzle doSomeThing1(@RequestBody Fizzle input){
        input.setBaz(1);
        return input;
    }

    //接收url中的参数，直接映射的默认方式， format格式化输出
    //counter自增的计数，template模式串
    private final AtomicLong counter = new AtomicLong(100);//具有自增功能100 101 102
    private static final String echoTemplate1 = "receidved %s!";//模式串format中用
    private static final String echoTemplate2 = "%s speak to %s \'%s\'";//模式串format中用
    @RequestMapping(value="/echoAndContent",method = RequestMethod.POST)
    public Echo getterPattern1(String content){//直接接收url中的content，默认此content和url中content映射
        //函数接收content：localhost:8080/echoAndContent？content=postman那边输入的值
        return new Echo(counter.incrementAndGet(),String.format(echoTemplate1,content));
    }

}
