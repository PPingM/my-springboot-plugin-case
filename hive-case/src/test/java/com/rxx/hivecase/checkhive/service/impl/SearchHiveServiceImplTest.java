package com.rxx.hivecase.checkhive.service.impl;

import com.rxx.hivecase.checkhive.entry.HiveReqParam;
import com.rxx.hivecase.checkhive.entry.TableInfo;
import com.rxx.hivecase.checkhive.service.SearchHiveService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author :zhangdan
 * @Description:
 * @Company :
 * @date :2018/1/19 10:40
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SearchHiveServiceImplTest {

    @Autowired
    private SearchHiveService service;

    @Test
    public void getDatabases() throws Exception {
    }

    @Test
    public void getTables() throws Exception {
    }

    @Test
    public void getTableInfo() throws Exception {
        HiveReqParam param = new HiveReqParam();
        param.setDatabase("secsight");
        param.setTable("ack_log");
        TableInfo tableInfo = service.getTableInfo(param);
        System.out.println(tableInfo);
    }

}