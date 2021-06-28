package com.fmp.data;

import android.text.TextUtils;

import com.fmp.core.HelperNative;
import com.fmp.util.Base64Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import static com.fmp.core.HelperCore.EMPTY_STRING;

public class LauncherSetting implements Serializable {
    private static final long serialVersionUID = -8202729739722705506L;
    private String versionName;
    private String mcboxId;
    private int loadMode;
    private boolean deBugMode;
    private boolean safeMode;
    private boolean gameFloat;
    private boolean mudwFloat;
    private String coreRequest;

    private LauncherSetting() {
        versionName = EMPTY_STRING;
    }

    public static LauncherSetting getSetting() {
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(HelperNative.getApplication().getFilesDir(), "launcher.db"));
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            return (LauncherSetting) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new LauncherSetting();
        }
    }

    public int getLoadMode() {
        return loadMode;
    }

    public void setLoadMode(int loadMode) {
        this.loadMode = loadMode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getMcboxId() {
        if (TextUtils.isEmpty(mcboxId))
            return "-1";
        return mcboxId;
    }

    public void setMcboxId(String mcboxId) {
        this.mcboxId = mcboxId;
    }

    public boolean isDeBugMode() {
        return deBugMode;
    }

    public void setDeBugMode(boolean deBugMode) {
        this.deBugMode = deBugMode;
    }

    public boolean isSafeMode() {
        return safeMode;
    }

    public void setSafeMode(boolean safeMode) {
        this.safeMode = safeMode;
    }

    public boolean onGameFloat() {
        return gameFloat;
    }

    public void setGameFloat(boolean gameFloat) {
        this.gameFloat = gameFloat;
    }

    public boolean onMudwFloat() {
        return mudwFloat;
    }

    public void setMudwFloat(boolean mudwFloat) {
        this.mudwFloat = mudwFloat;
    }

    public void saveSetting() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(HelperNative.getApplication().getFilesDir(), "launcher.db"));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getCoreRequest() {
        return "duowan://home?mcbox_uid=%s&mcbox_id=%s&timestamp=%s";
        //return Base64Util.decrypt(coreRequest);
    }

    public void setCoreRequest(String coreRequest) {
        this.coreRequest = Base64Util.encrypt(coreRequest);
    }
}
