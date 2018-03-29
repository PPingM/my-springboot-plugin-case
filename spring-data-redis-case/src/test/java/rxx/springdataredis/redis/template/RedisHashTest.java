package com.rxx.springdataredis.redis.template;

import com.rxx.springdataredis.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author :zhangdan
 * @Description:
 * @Company :
 * @date :2018/3/20 20:00
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=Application.class)// 指定spring-boot的启动类
public class RedisHashTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    private String prefix = "zhang_test_";

    @Test
    public void set() throws Exception {
        redisTemplate.opsForHash().put(prefix + "hash_set", "key", "value");
    }

    @Test
    public void get() throws Exception {
        System.out.println(redisTemplate.opsForHash().get(prefix + "hash_set", "key"));
    }

    @Test
    public void setnx() throws Exception {
        redisTemplate.opsForHash().putIfAbsent(prefix + "hash_set", "key", "value2");
    }

    @Test
    public void mset() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("mkey1", "mvalue1");
        map.put("mkey2", "mvalue2");
        map.put("mkey3", "mvalue3");
        redisTemplate.opsForHash().putAll(prefix + "hash_mset", map);
    }

    @Test
    public void mget() throws Exception {
        List<String> keys = Arrays.asList("mkey1", "mkey2", "mkey3");
        List<String> values = redisTemplate.opsForHash().multiGet(prefix + "hash_mset", keys);
        System.out.println(values);
    }

    /**
     * 为哈希表 key 中的指定字段的整数值加上增量 increment
     * @throws Exception
     */
    @Test
    public void incrby() throws Exception {
    }

    @Test
    public void exists() throws Exception {
    }

    @Test
    public void len() throws Exception {
    }

    @Test
    public void del() throws Exception {
    }

    @Test
    public void keys() throws Exception {
    }

    @Test
    public void vals() throws Exception {
    }

    @Test
    public void getall() throws Exception {
    }

}
