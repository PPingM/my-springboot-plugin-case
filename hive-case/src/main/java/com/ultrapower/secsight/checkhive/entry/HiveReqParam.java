package com.ultrapower.secsight.checkhive.entry;

/**
 * @author :zhangdan
 * @Description:
 * @Company :
 * @date :2018/1/16 15:11
 */
public class HiveReqParam {
    String database;
    String table;
    String partition;
    String limit;

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getPartition() {
        return partition;
    }

    public void setPartition(String partition) {
        this.partition = partition;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }
}
