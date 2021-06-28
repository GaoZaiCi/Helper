package com.fmp.core.push;

import java.io.Serializable;
import java.util.List;

public class PushMessage implements Serializable {
    private String title;
    private String message;
    private List<String> pictures;
    private boolean forced;
    private boolean quit;

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
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
}
