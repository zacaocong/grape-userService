package com.etekcity.userservice.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 *spring-redis中使用了RedisTemplate来进行redis的操作，通过泛型的K和V设置键值对的对象类型。这里使用了string作为key的对象类型，值为Object。
 *
 * 对于Object，spring-redis默认使用了jdk自带的序列化，不推荐使用默认了。所以使用了json的序列化方式
 *
 * 对spring-redis对redis的五种数据类型也有支持
 * HashOperations：对hash类型的数据操作
 * ValueOperations：对redis字符串类型数据操作
 * ListOperations：对链表类型的数据操作
 * SetOperations：对无序集合类型的数据操作
 * ZSetOperations：对有序集合类型的数据操作
 *
 * */



@Configuration
@EnableCaching
public class RedisConfiguration extends CachingConfigurerSupport {



//    @Bean
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }
//    @Bean
//    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
//        return new RestTemplate(factory);
//    }
//
//    @Bean
//    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
//        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
//        factory.setReadTimeout(30000); // ms
//        factory.setConnectTimeout(40000); // ms
//        return factory;
//    }

    /*
     * 选择redis作为默认缓存工具
     * */
//    @Bean
//    public CacheManager cacheManager(RedisTemplate redisTemplate){
//        RedisCacheManager rcm = new RedisCacheManager();
//
//        return rcm;
//    }


    /*
     * 重新实现RedisTemplate：解决序列化问题
     * @param RedisConnectionFactory
     * @return
     * */
    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory connectionFactory){
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();

        //配置连接工厂
        redisTemplate.setConnectionFactory(connectionFactory);

        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式），jackson的序列化方式
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer((Object.class));

        ObjectMapper objectMapper = new ObjectMapper();
        //指定要序列化的域，field，get和set，以及修饰符范围，ANY是都有包括private和public；设置任何字段可见
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String，Integer等会跑出异常；设置不是final的属性可以转换
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        //----使用StringRedisSerializer来序列化和反序列化redis的key值----
        RedisSerializer redisSerializer = new StringRedisSerializer();
        //key采用String的序列化方式
        redisTemplate.setKeySerializer(redisSerializer);//key采用String的序列化方式
        redisTemplate.setHashKeySerializer(redisSerializer);//hash的key采用String的序列化方式
        //value采用jackson序列化方式
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);//value采用jackson序列化方式
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);//hash的value采用jackson序列化方式
        redisTemplate.afterPropertiesSet();
        //redisTemplate.setEnableTransactionSupport(true);//事务支持
        return redisTemplate;
    }

    /*对hash类型的数据操作
     * @param redisTemplate
     * @return
     * */
    @Bean
    public HashOperations<String,String,Object> hashOperations(RedisTemplate<String,Object> redisTemplate){
        return redisTemplate.opsForHash();
    }

    /*
     * 对redis字符串类型数据操作
     * @param redisTemplate
     * @return
     * */
    @Bean
    public ValueOperations<String,Object> valueOperations(RedisTemplate<String,Object> redisTemplate){
        return redisTemplate.opsForValue();
    }
    /*
     * 对链表类型的数据操作
     * @param redisTemplate
     * @return
     * */
    @Bean
    public ListOperations<String,Object> listOperations(RedisTemplate<String,Object> redisTemplate){
        return redisTemplate.opsForList();
    }
    /*
     * 对无序集合类型的数据操作
     * @param redisTemplate
     * @return
     * */
    @Bean
    public SetOperations<String,Object> setOperations(RedisTemplate<String,Object> redisTemplate){
        return redisTemplate.opsForSet();
    }
    /**
     * 对有序集合类型的数据操作
     * @param redisTemplate
     * @return
     * */
    @Bean
    public ZSetOperations<String,Object> zSetOperations(RedisTemplate<String,Object> redisTemplate){
        return redisTemplate.opsForZSet();
    }

}