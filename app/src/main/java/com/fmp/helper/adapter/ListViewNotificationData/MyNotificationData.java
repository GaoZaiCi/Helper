package com.fmp.helper.adapter.ListViewNotificationData;

public class MyNotificationData {
    private String[] result;
    private String title, time, info;

    public MyNotificationData() {
    }

    public MyNotificationData(String[] result) {
        this.result = result;
        this.title = result[0];
        this.time = result[1];
        this.info = result[2];
    }

    public String[] getResult() {
        return this.result;
    }

    public void setResult(String[] strings) {
        result = strings;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getInfo() {
        return this.info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
