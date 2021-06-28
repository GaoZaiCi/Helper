package com.fmp.core.push;

import java.io.Serializable;

public class PushPlugin implements Serializable {
    private static final long serialVersionUID = -1835334078442501765L;
    private String size;
    private String url;

    public String getSize() {
        return size;
    }

    public String getUrl() {
        return url;
    }
}
