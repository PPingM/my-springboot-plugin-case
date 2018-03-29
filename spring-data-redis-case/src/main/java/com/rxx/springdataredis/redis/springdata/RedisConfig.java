package com.rxx.springdataredis.redis.springdata;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.*;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author zhang
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    /**
     * 生成key的策略
     * 自定义缓存策略
     * @return
     */
    @Override
    @Bean
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            String[] value = new String[1];
             sb.append(target.getClass().getName());
             sb.append(":" + method.getName());
            Cacheable cacheable = method.getAnnotation(Cacheable.class);
            if (cacheable != null) {
                value = cacheable.value();
            }
            CachePut cachePut = method.getAnnotation(CachePut.class);
            if (cachePut != null) {
                value = cachePut.value();
            }
            CacheEvict cacheEvict = method.getAnnotation(CacheEvict.class);
            if (cacheEvict != null) {
                value = cacheEvict.value();
            }
            sb.append(value[0]);
            for (Object obj : params) {
                sb.append(":" + obj.toString());
            }
            return sb.toString();
        };
    }

    /**
     * 管理缓存
     * @param redisTemplate
     * @return
     */
    @Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
        RedisCacheManager rcm = new RedisCacheManager(redisTemplate);
        // 设置缓存过期时间
        rcm.setDefaultExpiration(600);// 秒
        return rcm;
    }

    /**
     * RedisTemplate配置
     * @param factory
     * @return
     */
    @Bean(initMethod = "test")
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<String, String>();

        redisTemplate.setConnectionFactory(factory);
        // 设置默认的序列化，不是注入方法的话，必须调用它。Spring注入的话，会在注入时调用
        redisTemplate.afterPropertiesSet();
        setSerializer(redisTemplate);
        return redisTemplate;
    }

    public void test(){
        System.out.println("test=====================================================");
    }

    private void setSerializer(RedisTemplate<String, String> template) {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(jackson2JsonRedisSerializer);
    }

}
