package com.rxx.hivecase.checkhive.dao;

import com.rxx.hivecase.checkhive.entry.HiveReqParam;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author :zhangdan
 * @Description:
 * @Company :
 * @date :2018/1/19 9:55
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SearchHiveDaoTest {

    @Autowired
    private SearchHiveDao searchHiveDao;

    @Test
    public void getDatabases() throws Exception {
    }

    @Test
    public void getTables() throws Exception {

        System.out.println("month=201709/week=201739/day=20170928".replaceAll("/*\\w*=", "-"));
        System.out.println("month=201709/week=201739/day=20170928".replaceAll("/", " "));
    }

    @Test
    public void getTableInfo() throws Exception {
        HiveReqParam param = new HiveReqParam();
        param.setDatabase("secsight");
        param.setTable("ack_log");
        Object result = searchHiveDao.getTableInfo(param);
        System.out.println(result);
    }

}