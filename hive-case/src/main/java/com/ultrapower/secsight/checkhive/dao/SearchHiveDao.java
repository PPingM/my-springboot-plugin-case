package com.ultrapower.secsight.checkhive.dao;

import com.ultrapower.secsight.checkhive.entry.HiveReqParam;
import com.ultrapower.secsight.util.HiveJdbcCliUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author :zhangdan
 * @Description:
 * @Company :
 * @date :2018/1/16 15:19
 */
@Repository
public class SearchHiveDao {
    static final Logger LOG = Logger.getLogger(SearchHiveDao.class);

    public List<String> getDatabases() {
        String sql = "show databases";
        return doQuerySqlToList(null, sql, "查询数据库");
    }

    public List<String> getTables(HiveReqParam hiveReqParam) {
        String sql = "show tables";
        return doQuerySqlToList(hiveReqParam.getDatabase(), sql, "查询数据表");
    }

    public Map<String, String> getTableInfo(HiveReqParam hiveReqParam) {
        Map<String, String> data = new LinkedHashMap<>();
        String sql = "desc formatted " + hiveReqParam.getTable();
        ArrayList<Map<String, String>> resultSet = HiveJdbcCliUtil.doQuerySql(hiveReqParam.getDatabase(), sql);
        if (resultSet == null || resultSet.size() == 0) {
            throw new RuntimeException("查询数据表信息为空");
        }
        resultSet.forEach(result -> {
            String colName = result.get("col_name");
            String dataType = result.get("data_type");
            if(StringUtils.isEmpty(dataType) || "null".equals(colName)){
                dataType = "";
            }
            if(!(StringUtils.isEmpty(colName) || "null".equals(colName))){
                data.put(result.get("col_name"), dataType);
            }

        });
        return data;
    }

    public List<String> getPartitions(HiveReqParam hiveReqParam) {
        String sql = "show partitions " + hiveReqParam.getTable();
        return doQuerySqlToList(hiveReqParam.getDatabase(), sql, "查询数据表分区失败");
    }

    public String getDataToFile(HiveReqParam hiveReqParam) {
        String sql = String.format("select * from %s where %s limit %s",
                hiveReqParam.getTable(), hiveReqParam.getPartition().replaceAll("/", " and "), hiveReqParam.getLimit());

        StringBuilder builderTitle = new StringBuilder();
        StringBuilder builderContent = new StringBuilder();
        ArrayList<Map<String, String>> resultSet = HiveJdbcCliUtil.doQuerySql(hiveReqParam.getDatabase(), sql);
        if (resultSet == null || resultSet.size() == 0) {
            throw new RuntimeException("查询详细数据为空");
        }

        int size = resultSet.size();
        for (int i = 0; i < size; i++) {
            Map<String, String> result = resultSet.get(i);
            // 构建文档标题
            if (i == 0) {
                result.entrySet().forEach(entry -> builderTitle.append(entry.getKey() + "\t"));
                builderTitle.append("\r\n");
            }
            // 构建文档内容
            result.entrySet().forEach(entry -> builderContent.append(entry.getValue() + "\t"));
            builderContent.append("\r\n");
        }
        return builderTitle.toString() + builderContent.toString();
    }

    private List<String> doQuerySqlToList(String database, String sql, String errMsg) {
        // 执行 SQL
        ArrayList<Map<String, String>> resultSet = HiveJdbcCliUtil.doQuerySql(database, sql);
        // 盘空处理
        if (resultSet == null) {
            throw new RuntimeException(errMsg + "为空");
        }
        // 获取数据
        final List<String> data = new ArrayList<>();
        resultSet.forEach(result -> data.addAll(result.values()));
        // 返回结果
        return data;
    }

}
