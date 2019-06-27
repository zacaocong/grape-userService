package com.etekcity.userservice.response.result;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * change password result
 * logout result
 * update user info result
 * @author grape
 * */
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.NONE)
public class EmptyResult {
    //这个注解解决了序列化问题
}
