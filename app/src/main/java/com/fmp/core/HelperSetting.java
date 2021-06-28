package com.fmp.core;

import android.content.Context;
import android.content.SharedPreferences;

public class HelperSetting {
    private static HelperSetting helperSetting;
    private static SharedPreferences sharedPreferences;
    private boolean app_notification;
    private boolean app_startMessage;
    private boolean app_networkUpload;
    private boolean game_loadMessage;


    public static HelperSetting getInstance() {
        if (helperSetting == null) {
            helperSetting = new HelperSetting();
            sharedPreferences = HelperNative.getApplication().getSharedPreferences("HelperSetting", Context.MODE_PRIVATE);
        }
        return helperSetting;
    }

    public HelperSetting getSetting() {
        app_notification = sharedPreferences.getBoolean("app_notification", true);
        app_startMessage = sharedPreferences.getBoolean("app_startMessage", true);
        app_networkUpload = sharedPreferences.getBoolean("app_networkUpload", false);
        game_loadMessage = sharedPreferences.getBoolean("game_loadMessage", true);
        return helperSetting;
    }

    public boolean isNotification() {
        return app_notification;
    }

    public void setNotification(boolean notification) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("app_notification", notification);
        editor.apply();
        this.app_notification = notification;
    }

    public boolean isStartMessage() {
        return app_startMessage;
    }

    public void setStartMessage(boolean startMessage) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("app_startMessage", startMessage);
        editor.apply();
        this.app_startMessage = startMessage;
    }

    public boolean isNetworkUpload() {
        return app_networkUpload;
    }

    public void setNetworkUpload(boolean networkUpload) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("app_networkUpload", networkUpload);
        editor.apply();
        this.app_networkUpload = networkUpload;
    }

    public boolean isLoadMessage() {
        return game_loadMessage;
    }

    public void setLoadMessage(boolean loadMessage) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("game_loadMessage", loadMessage);
        editor.apply();
        this.game_loadMessage = loadMessage;
    }


}
