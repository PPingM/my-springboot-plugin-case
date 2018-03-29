package com.ultrapower.secsight.checkhive.service;

import com.ultrapower.secsight.checkhive.entry.HiveReqParam;
import com.ultrapower.secsight.checkhive.entry.TableInfo;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * @author :zhangdan
 * @Description:
 * @Company :
 * @date :2018/1/16 15:16
 */
public interface SearchHiveService {
    /**
     *  获取数据库
     * @return
     */
    List<String> getDatabases();

    /**
     *  获取数据表
     * @param hiveReqParam
     * @return
     */
    List<String> getTables(HiveReqParam hiveReqParam);

    /**
     * 获取数据表信息
     * @param hiveReqParam
     * @return
     */
    TableInfo getTableInfo(HiveReqParam hiveReqParam);

    /**
     * 获取数据表分区
     * @param hiveReqParam
     * @return
     */
    List<String> getPartitions(HiveReqParam hiveReqParam);

    /**
     * 获取详细数据
     * @param hiveReqParam
     * @return
     */
    byte[] getDataToFile(HiveReqParam hiveReqParam) throws UnsupportedEncodingException;
}
