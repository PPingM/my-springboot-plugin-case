package com.ultrapower.secsight.checkhive.entry;

import org.springframework.stereotype.Component;

/**
 * @author :zhangdan
 * @Description:
 * @Company :
 * @date :2018/1/16 15:03
 */
@Component
public class ResultData {

    private String notice;
    private boolean success;
    private Object data;

    public ResultData(){
        this.setSuccess(true);
        this.setNotice("操作成功");
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
