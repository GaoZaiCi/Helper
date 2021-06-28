package com.fmp.cloud;

import cn.bmob.v3.BmobObject;

public class FMP_APP_AD extends BmobObject {
    private Integer position;
    private String name;
    private String image;
    private String url;

    public FMP_APP_AD() {
        this.setTableName("FMP_APP_AD");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

}
