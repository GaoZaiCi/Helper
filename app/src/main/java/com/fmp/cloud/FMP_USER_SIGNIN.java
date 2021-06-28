package com.fmp.cloud;

import java.util.List;

import cn.bmob.v3.BmobObject;

public class FMP_USER_SIGNIN extends BmobObject {
    private String SigninDate;
    private Integer SigninCount;
    private List<String> SigninUsers;

    public FMP_USER_SIGNIN() {
        this.setTableName("FMP_USER_SIGNIN");
    }

    public String getSigninDate() {
        return SigninDate;
    }

    public Integer getSigninCount() {
        return SigninCount;
    }

    public List<String> getSigninUsers() {
        return SigninUsers;
    }

    public void setSigninDate(String obj) {
        SigninDate = obj;
    }

    public void setSigninCount(Integer obj) {
        SigninCount = obj;
    }

    public void setSigninUsers(List<String> objs) {
        SigninUsers = objs;
    }
}
