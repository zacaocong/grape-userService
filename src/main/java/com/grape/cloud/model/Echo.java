package com.grape.cloud.model;

//这是响应的JSON消息，有getter的private变量就会收到，在postman中以JSON数据的形式出现
//public无需getter，看来只要是对外可见的，就会出现
public class Echo {
    //final写出来会报错由于没初始化，但是alter回车后，会增加构造方法，就没事了
    //初始化可以手动赋值，也可构造中赋值
    private final long id;//构造赋值+getter  ->  postman收到JSON数据
    private final String content;//构造赋值+getter  ->  postman收到JSON数据
    private int   echoint = 1;//int 在无getter情况下没有在响应消息中存在 +"getter" -> postman收到JSON数据
    public  int   intint  = 2;//同样可见


    //构造方法，为上面两个final赋初值
    public Echo(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return this.id;
    }

    public String getContent() {
        return this.content;
    }

    public int getEchoint() {
        return echoint;
    }
}
