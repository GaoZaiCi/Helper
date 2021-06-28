package com.fmp.skins;

import android.graphics.Bitmap;

public class Skin4DItem {
    private String name;
    private String blur;
    private String json;
    private Bitmap skin;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBlur() {
        return blur;
    }

    public void setBlur(String blur) {
        this.blur = blur;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public Bitmap getSkin() {
        return skin;
    }

    public void setSkin(Bitmap skin) {
        this.skin = skin;
    }
}
