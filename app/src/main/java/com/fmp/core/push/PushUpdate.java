package com.fmp.core.push;

import java.io.Serializable;
import java.util.List;

public class PushUpdate implements Serializable {
    private int code;
    private String title;
    private String message;
    private String url;
    private List<String> pictures;
    private boolean cover;
    private boolean forced;
    private boolean quit;

    public int getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getUrl() {
        return url;
    }

    public boolean isForced() {
        return forced;
    }

    public boolean isQuit() {
        return quit;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public boolean isCover() {
        return cover;
    }
}