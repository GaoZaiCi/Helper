package com.fmp.core.push;

import java.io.Serializable;

public class PushHotUpdate implements Serializable {
    private int code;
    private String title;
    private String message;
    private String url;
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
}
