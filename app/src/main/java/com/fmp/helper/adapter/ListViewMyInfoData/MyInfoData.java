package com.fmp.helper.adapter.ListViewMyInfoData;

public class MyInfoData {
    private String str;
    private int res;

    public MyInfoData(int res, String str) {
        this.str = str;
        this.res = res;
    }

    public MyInfoData() {
    }

    public void setStr(String str) {
        this.str = str;
    }

    public String getStr() {
        return this.str;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public int getRes() {
        return this.res;
    }
}
