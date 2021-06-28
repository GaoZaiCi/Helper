package com.fmp.cloud;

import cn.bmob.v3.BmobObject;

public class FMP_USER_NOTICE extends BmobObject {
    private Boolean isShow;
    private String title;
    private String message;

    public FMP_USER_NOTICE() {
        this.setTableName("FMP_USER_NOTICE");
    }

    public Boolean getShow() {
        return isShow;
    }

    public void setShow(Boolean show) {
        isShow = show;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
