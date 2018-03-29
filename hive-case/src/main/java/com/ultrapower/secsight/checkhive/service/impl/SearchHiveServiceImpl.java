package com.ultrapower.secsight.checkhive.service.impl;

import com.ultrapower.secsight.checkhive.dao.SearchHiveDao;
import com.ultrapower.secsight.checkhive.entry.HiveReqParam;
import com.ultrapower.secsight.checkhive.entry.TableInfo;
import com.ultrapower.secsight.checkhive.service.SearchHiveService;
import javafx.scene.control.Tab;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author :zhangdan
 * @Description:
 * @Company :
 * @date :2018/1/16 15:18
 */
@Service
public class SearchHiveServiceImpl implements SearchHiveService {

    @Autowired
    private SearchHiveDao searchHiveDao;

    @Override
    public List<String> getDatabases() {
        return searchHiveDao.getDatabases();
    }

    @Override
    public List<String> getTables(HiveReqParam hiveReqParam) {
        return searchHiveDao.getTables(hiveReqParam);
    }

    @Override
    public TableInfo getTableInfo(HiveReqParam hiveReqParam) {
        Map<String, String> data = searchHiveDao.getTableInfo(hiveReqParam);
        if(data == null){
            return null;
        }

        TableInfo tableInfo = new TableInfo();
        Map<String, String> d = null;
        for (Map.Entry<String, String> entry : data.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if(StringUtils.isEmpty(key)){
                continue;
            }
            if(key.contains("# col_name")){
                d = new LinkedHashMap<>();
                tableInfo.setBaseInfo(d);
                continue;
            }
            if(key.contains("# Partition Information")){
                d = new LinkedHashMap<>();
                tableInfo.setPartitionInfo(d);
                continue;
            }
            if(key.contains("# Detailed Table Information")){
                d = new LinkedHashMap<>();
                tableInfo.setDetailedTableInfo(d);
                continue;
            }
            if(key.contains("# Storage Information")){
                d = new LinkedHashMap<>();
                tableInfo.setStorageInfo(d);
                continue;
            }
            if(StringUtils.isEmpty(value) || d == null){
                continue;
            }
            d.put(key.trim(), value.trim().replace(":",""));
        }
        return tableInfo;
    }

    @Override
    public List<String> getPartitions(HiveReqParam hiveReqParam) {
        return searchHiveDao.getPartitions(hiveReqParam);
    }

    @Override
    public byte[] getDataToFile(HiveReqParam hiveReqParam) throws UnsupportedEncodingException {
        String data = searchHiveDao.getDataToFile(hiveReqParam);
        if(data == null){
            return null;
        }
        return searchHiveDao.getDataToFile(hiveReqParam).getBytes("utf-8");
    }

}
