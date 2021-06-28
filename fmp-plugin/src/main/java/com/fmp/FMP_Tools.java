package com.fmp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.util.Base64;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class FMP_Tools {
    public static Integer isNullInteger(Integer value) {
        if (value == null) {
            return 0;
        }
        return value;
    }

    public static boolean Equals(String str1, String str2) {
        if (str1 == null && str2 == null) return true;
        if (str1 == null || str2 == null) return false;
        return str1.equals(str2) && str1.hashCode() == str2.hashCode() && str1.toCharArray()[1] == str2.toCharArray()[1] && str1.charAt(1) == str2.charAt(1);
    }

    public static boolean isEmpty(String str) {
        if (str == null) return true;
        return str.equals("");
    }

    public static int[] StringToIntArray(String str) {
        char[] cha = str.toCharArray();
        int[] in = new int[cha.length];
        for (int i = 0; cha.length > i; i++) {
            in[i] = cha[i] + in.length;
        }
        return in;
    }

    public static String IntArrayToString(int[] in) {
        StringBuilder Return = new StringBuilder();
        for (int anIn : in) {
            Return.append((char) (anIn - in.length));
        }
        return Return.toString();
    }

   /* public static boolean getDetect() {
        //检测应用是否可以备份
        //检查应用是否属于debug模式
        //检查应用是否处于调试状态
        if ((0 != (mContext.getApplicationInfo().flags &= mContext.getApplicationInfo().FLAG_ALLOW_BACKUP) || 0 != (mContext.getApplicationInfo().flags &= mContext.getApplicationInfo().FLAG_DEBUGGABLE) || android.os.Debug.isDebuggerConnected())) {
            FMP_Logger.LogInfo("onError:0");
            return true;
        }
        //正常返回null
        if (System.getProperty("http.proxyHost") != null || System.getProperty("http.proxyPort") != null) {
            FMP_Logger.LogInfo("检测到代理服务！");
        }
        return false;
    }*/


}
