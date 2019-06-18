package com.etekcity.userservice.redisconfig;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author 作者：grape
 * @version 创建时间：2019年6月7日 下午5:56:41
 *
 *          项目启动加载redis
 * */

@Configuration
@EnableCaching
public class RedisConfiguration extends CachingConfigurerSupport {
    /**
     * 这里声明V即可，前面声明String V会把String变成泛型，
     * */
    @Bean
    public <V> RedisTemplate<String, V> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, V> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        setSerializer(template);
        template.afterPropertiesSet();
        return template;
    }

    private void setSerializer(RedisTemplate template) {
        //key的序列化器为String
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        //value的序列化为json格式，使用Jackson2JsonRedisSerializer
        @SuppressWarnings({"rawtypes","unchecked"})
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new
                Jackson2JsonRedisSerializer<Object>(Object.class);
        //jackson核心对象，Jackson是用来转换json和java格式的
        ObjectMapper om = new ObjectMapper();
        //指定要序列化的域，field，get和set，以及修饰符范围，ANY是都有包括private和public；设置任何字段可见
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setDefaultSerializer(jackson2JsonRedisSerializer);
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
    }
}
