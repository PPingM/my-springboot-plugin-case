package com.rxx.hivecase.checkhive.entry;

import java.util.Map;

/**
 * @author :zhangdan
 * @Description:
 * @Company :
 * @date :2018/1/19 10:09
 */
public class TableInfo {
    private Map<String, String> baseInfo;
    private Map<String, String> partitionInfo;
    private Map<String, String> detailedTableInfo;
    private Map<String, String> storageInfo;

    public Map<String, String> getBaseInfo() {
        return baseInfo;
    }

    public void setBaseInfo(Map<String, String> baseInfo) {
        this.baseInfo = baseInfo;
    }

    public Map<String, String> getPartitionInfo() {
        return partitionInfo;
    }

    public void setPartitionInfo(Map<String, String> partitionInfo) {
        this.partitionInfo = partitionInfo;
    }

    public Map<String, String> getDetailedTableInfo() {
        return detailedTableInfo;
    }

    public void setDetailedTableInfo(Map<String, String> detailedTableInfo) {
        this.detailedTableInfo = detailedTableInfo;
    }

    public Map<String, String> getStorageInfo() {
        return storageInfo;
    }

    public void setStorageInfo(Map<String, String> storageInfo) {
        this.storageInfo = storageInfo;
    }
}
