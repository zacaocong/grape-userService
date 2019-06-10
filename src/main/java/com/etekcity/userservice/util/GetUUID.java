package com.etekcity.userservice.util;

import java.util.UUID;

public class GetUUID {
    public static String getUUID32(){
        // 不是没有“-”是去掉了，userid有- token没-
        //return UUID.randomUUID().toString().replace("-", "").toLowerCase();
        return UUID.randomUUID().toString().toLowerCase();
    }
}