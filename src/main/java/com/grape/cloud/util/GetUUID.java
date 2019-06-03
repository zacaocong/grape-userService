package com.grape.cloud.util;

import java.util.UUID;

public class GetUUID {
    public static String getUUID32(){

        return UUID.randomUUID().toString().replace("-", "").toLowerCase();

    }
}
