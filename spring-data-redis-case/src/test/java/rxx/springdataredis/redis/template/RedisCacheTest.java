package com.rxx.springdataredis.redis.template;

import com.rxx.springdataredis.Application;
import com.rxx.springdataredis.Entry.Person;
import com.rxx.springdataredis.util.PersonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :zhangdan
 * @Description:
 * @Company :
 * @date :2018/3/20 21:24
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=Application.class)// 指定spring-boot的启动类
public class RedisCacheTest {


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    private String prefix = "zhang_test_";

    /**
     * 缓存基本的对象，Integer、String、实体类等
     * @return 缓存的对象
     */
    @Test
    public void setCacheObject() {
        Person person = PersonUtil.getOnePerson(prefix + "cache");
        redisTemplate.opsForValue().set(prefix + "cache", person);
    }

    /**
     * 获得缓存的基本对象。
     */
    @Test
    public void getCacheObject() {
        System.out.println(redisTemplate.opsForValue().get(prefix + "cache"));
    }

    /**
     * 缓存List数据
     */
    @Test
    public void setCacheList() {
        String key = prefix + "cachelist";
        List<Person> dataList = PersonUtil.getPersons(key, 5);

        ListOperations<String, Object> listOperation = redisTemplate.opsForList();
        if (null != dataList) {
            int size = dataList.size();
            for (int i = 0; i < size; i++) {
                listOperation.rightPush(key, dataList.get(i));
            }
        }
        System.out.println(listOperation);
    }

    /**
     * 获得缓存的list对象
     */
    @Test
    public void getCacheList() {
        String key = prefix + "cachelist";
        List<Person> dataList = new ArrayList<>();
        ListOperations<String, Person> listOperation = redisTemplate.opsForList();
        Long size = listOperation.size(key);

        for (int i = 0; i < size; i++) {
            dataList.add(listOperation.leftPop(key));
        }
        System.out.println(dataList);
    }

    /**
     * 获得缓存的list对象
     */
    @Test
    public void range() {
        String key = prefix + "cachelist";
        long start = 2; // 起始
        long end = 3;  // 截止
        ListOperations<String, Object> listOperation = redisTemplate.opsForList();
        System.out.println(listOperation.range(key, start, end));
    }
}
