package com.fmp.core.push;

import android.text.TextUtils;

import com.fmp.core.HelperNative;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class PushSetting implements Serializable {
    private static final long serialVersionUID = 3887985583601544385L;
    private boolean xposed;
    private boolean tencent;
    private boolean login;
    private String sharechain;
    private boolean virtual;

    public void setXposed(boolean xposed) {
        this.xposed = xposed;
    }

    public void setTencent(boolean tencent) {
        this.tencent = tencent;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public void setSharechain(String sharechain) {
        this.sharechain = sharechain;
    }

    public void setVirtual(boolean virtual) {
        this.virtual = virtual;
    }

    public static PushSetting getSetting() {
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(HelperNative.getApplication().getFilesDir(), "pushSetting.db"));
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            return (PushSetting) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new PushSetting();
        }
    }

    public boolean isXposed() {
        return xposed;
    }

    public boolean isTencent() {
        return tencent;
    }

    public boolean isLogin() {
        return login;
    }

    String getSharechain() {
        if (TextUtils.isEmpty(sharechain))
            return "https://sharechain.qq.com/d45799f39ff74263dab505b7119805e9";
        return sharechain;
    }

    public boolean isVirtual() {
        return virtual;
    }

    void saveSetting() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(HelperNative.getApplication().getFilesDir(), "pushSetting.db"));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
