package com.fmp.core.http;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Locale;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.helper.BmobNative;
import cn.bmob.v3.helper.RequestHelper;
import cn.bmob.v3.http.acknowledge;
import cn.bmob.v3.http.darkness;
import cn.bmob.v3.util.BmobContentProvider;
import cn.bmob.v3.util.Tempest;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class BmobHttp {


    public static Request getDeviceInfo(String var0, JSONObject var1) {
        String var2 = RequestHelper.getUserAgent(Bmob.getApplicationContext());
        var1 = Code(Bmob.getApplicationContext(), var1 == null ? new JSONObject() : var1, var0);
        cn.bmob.v3.util.thing.Code("params:" + (var1 == null ? "" : var1.toString()));
        String var3;
        if (var0.equals(darkness.Code().I())) {
            var3 = Tempest.Code(var2, var1.toString());
        } else {
            String var10000 = BmobNative.SECRET_KEY;
            var3 = Tempest.Code(var10000, var10000, var1.toString());
        }

        RequestBody var4 = RequestBody.create( MediaType.parse("application/json; charset=UTF-8"), var3);
        okhttp3.Request.Builder var5 = (new okhttp3.Request.Builder())
                .header("Content-Type", "text/plain; charset=utf-8")
                .header("Accept-Encoding", "gzip,deflate,sdch")
                .header("User-Agent", var2)
                .url(var0)
                .post(var4);
        if (!var0.equals(darkness.Code().I())) {
            var5.addHeader("Accept-Id", BmobNative.getAcceptId());
        }

        return var5.build();
    }

    private static JSONObject Code(Context var0, JSONObject var1, String var2) {
        try {
            if (var2.equals(darkness.Code().I())) {
                var1.put("appKey", BmobNative.getAppId());
                cn.bmob.v3.util.thing.Code("appKey" + BmobNative.getAppId());
            }

            String var3 = BmobContentProvider.getSessionToken();
            cn.bmob.v3.util.thing.Code("sessionToken:" + var3);
            var1.put("sessionToken", var3);
            var1.put("appSign", acknowledge.Code(var0));//签名数组

            JSONObject var4= new JSONObject().put("version", Build.VERSION.RELEASE);
            var4.put("package", var0.getPackageName());
            var4.put("uuid", I(var0));

            JSONObject var9= new JSONObject().put("caller", "Android");
            var9.put("ex", var4);

            var1.put("client", var9);
            var1.put("v", "v3.7.4");
            if (!var2.equals(darkness.Code().I()) && !var2.equals(darkness.Code().Z())) {
                long var6 = acknowledge.Code();
                var1.put("timestamp", var6);
            }
        } catch (Exception var8) {
            var8.printStackTrace();
        }

        return var1;
    }

    public static String I(Context var0) {
        String var1 = getDeviceId(var0);
        String var2 = getDeviceInfo();
        String var3 = androidId(var0);
        String var4 = getMacAddress(var0);
        String var6 = getBluetoothAddress(var0);
        var6 = var1 + var2 + var3 + var4 + var6;

        try {
            MessageDigest var7;
            (var7 = MessageDigest.getInstance("MD5")).update(var6.getBytes(), 0, var6.length());
            byte[] var8 = var7.digest();
            var2 = new String();

            for(int var9 = 0; var9 < var8.length; ++var9) {
                int var10;
                if ((var10 = 255 & var8[var9]) <= 15) {
                    var2 = var2 + "0";
                }

                var2 = var2 + Integer.toHexString(var10);
            }

            return var2.toUpperCase(Locale.CHINA);
        } catch (Exception var5) {
            var5.printStackTrace();
            return var6.toLowerCase(Locale.CHINA);
        }
    }

    private static String getDeviceId(Context var0) {
        if (Code(var0, "android.permission.READ_PHONE_STATE")) {
            try {
                return ((TelephonyManager)var0.getSystemService("phone")).getDeviceId();
            } catch (Exception var1) {
                return "";
            }
        } else {
            return "";
        }
    }

    private static String getDeviceInfo() {
        return "35" + Build.BOARD.length() % 10 + Build.BRAND.length() % 10 + Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 + Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 + Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 + Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 + Build.TAGS.length() % 10 + Build.TYPE.length() % 10 + Build.USER.length() % 10;
    }

    private static String androidId(Context var0) {
        try {
            return Settings.Secure.getString(var0.getContentResolver(), "android_id");
        } catch (Exception var1) {
            return "";
        }
    }

    private static String getMacAddress(Context var0) {
        if (Code(var0, "android.permission.ACCESS_WIFI_STATE")) {
            try {
                return ((WifiManager)var0.getSystemService("wifi")).getConnectionInfo().getMacAddress();
            } catch (Throwable var1) {
                return "";
            }
        } else {
            return "";
        }
    }

    private static String getBluetoothAddress(Context var0) {
        if (Code(var0, "android.permission.BLUETOOTH")) {
            try {
                return BluetoothAdapter.getDefaultAdapter().getAddress();
            } catch (Throwable var1) {
                return "";
            }
        } else {
            return "";
        }
    }

    private static boolean Code(Context var0, String var1) {
        PackageManager var2 = var0.getPackageManager();
        String[] var3 = null;

        try {
            var3 = var2.getPackageInfo(var0.getPackageName(), 4096).requestedPermissions;
        } catch (PackageManager.NameNotFoundException var5) {
            var5.printStackTrace();
        }

        ArrayList var6 = new ArrayList();
        if (var3 != null && var3.length > 0) {
            int var7 = var3.length;

            for(int var4 = 0; var4 < var7; ++var4) {
                var6.add(var3[var4]);
            }
        }

        return var6.contains(var1);
    }
}
