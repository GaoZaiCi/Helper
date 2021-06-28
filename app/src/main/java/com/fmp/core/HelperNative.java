package com.fmp.core;

import android.app.Application;
import android.content.Context;

import androidx.annotation.Keep;


@SuppressWarnings("ALL")
@Keep
public class HelperNative {
    public static native int initNative(Context ctx);

    public static native int initUser(Object userData,int level);

    public static native Application getApplication();

    public static native boolean devMode(String key);

    public static native byte[] encrypt(byte[] bytes);

    public static native byte[] decrypt(byte[] bytes);

    public static native byte[] encrypt(int key, byte[] bytes);

    public static native byte[] decrypt(int key, byte[] bytes);

    public static native void onError();

    public static native void Toast(String str);

    public static native void startNetEaseMc(String packageName, String parse, String modArr, String gameModArr, String uid, int loadMode, boolean showMuFloat, boolean showGameFloat, String skinPath, String texturesPath, boolean removeCacheMode ,String hookChecks);

}
