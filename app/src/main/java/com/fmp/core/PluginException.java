package com.fmp.core;

import com.fmp.Logger;

public class PluginException extends Exception {
    private static final String TAG = "PluginException";
    private static final long serialVersionUID = -3861868043731644041L;
    private static boolean deBugMode = false;

    public PluginException() {
        super();
    }

    public PluginException(String message) {
        super(message);
        if (deBugMode)
            Logger.e(TAG, message);
    }

    public PluginException(String message, Throwable cause) {
        super(message, cause);
        if (deBugMode)
            Logger.toString(message, cause);
    }

    public PluginException(Throwable cause) {
        super(cause);
        if (deBugMode)
            Logger.toString(TAG, cause);
    }

    public static void setDeBugMode(boolean mode) {
        deBugMode = mode;
    }

}
