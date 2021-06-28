package com.fmp.core;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.fmp.Logger;
import com.fmp.util.Base64Util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class DeviceIdUtil {

    private static String getDeviceIdData() {
        Context ctx = HelperNative.getApplication();
        String uid = null;
        {
            File file = new File(com.fmp.core.HelperCore.getHelperDirectory(), ".android/.uid");
            try {
                uid = readData(file);
            } catch (IOException e) {

            }
        }
        {
            File file = new File(ctx.getFilesDir(), ".uid");
            try {
                uid = readData(file);
            } catch (IOException e) {

            }
        }
        return uid;
    }

    private static String readData(File file) throws IOException {
        FileInputStream inputStream = new FileInputStream(file);
        byte[] strByte = new byte[inputStream.available()];
        inputStream.read(strByte);
        String data = new String(strByte);
        inputStream.close();
        return Base64Util.decrypt(data);
    }

    private static boolean saveDeviceId(String deviceId) {
        Context ctx = HelperNative.getApplication();
        boolean bool = false;
        {
            File file = new File(com.fmp.core.HelperCore.getHelperDirectory(), ".android/.uid");
            try {
                file.getParentFile().mkdirs();
                writerData(file, Base64Util.encrypt(deviceId));
                bool = true;
            } catch (IOException e) {

            }
        }
        {
            File file = new File(ctx.getFilesDir(), ".uid");
            try {
                file.getParentFile().mkdirs();
                writerData(file, Base64Util.encrypt(deviceId));
                bool = true;
            } catch (IOException e) {

            }
        }
        return bool;
    }

    private static void writerData(File file, String deviceId) throws IOException {
        //如果文件存在-删除
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        //写入文件数据
        FileWriter fileWriter = new FileWriter(file, false);
        BufferedWriter writer = new BufferedWriter(fileWriter);
        //写入
        writer.append(deviceId);
        writer.flush();
        writer.close();
        fileWriter.close();
    }

    public static String getDeviceId() {
        if (getDeviceIdData() != null) {
            return getDeviceIdData();
        } else {
            Context ctx = HelperNative.getApplication();
            StringBuilder builder = new StringBuilder();
            try {
                builder.append(android.os.Build.BOARD.length() % 10);
                builder.append(android.os.Build.BRAND.length() % 10);
            } catch (Exception e) {
                builder.append(e.getMessage().hashCode() % 10);
            }
            try {
                builder.append(android.os.Build.SUPPORTED_32_BIT_ABIS.length % 10);
                builder.append(android.os.Build.SUPPORTED_64_BIT_ABIS.length % 10);
                builder.append(android.os.Build.SUPPORTED_ABIS.length % 10);
            } catch (Exception e) {
                builder.append(e.getMessage().hashCode() % 10);
            }
            try {
                builder.append(android.os.Build.DEVICE.length() % 10);
                builder.append(android.os.Build.DISPLAY.length() % 10);
                builder.append(android.os.Build.HOST.length() % 10);
            } catch (Exception e) {
                builder.append(e.getMessage().hashCode() % 10);
            }
            try {
                builder.append(android.os.Build.ID.length() % 10);
                builder.append(android.os.Build.MANUFACTURER.length() % 10);
                builder.append(android.os.Build.MODEL.length() % 10);
            } catch (Exception e) {
                builder.append(e.getMessage().hashCode() % 10);
            }
            try {
                builder.append(android.os.Build.PRODUCT.length() % 10);
                builder.append(android.os.Build.TAGS.length() % 10);
                builder.append(android.os.Build.TYPE.length() % 10);
            } catch (Exception e) {
                builder.append(e.getMessage().hashCode() % 10);
            }
            try {
                builder.append(android.os.Build.USER.length() % 10);
                builder.append(getDevicePixel() % 10);
                builder.append(getMacAddress(ctx).hashCode() % 10);
                if (getMacAddress(ctx).equals("02:00:00:00:00:00")) {
                    builder.append(String.valueOf(System.currentTimeMillis()).hashCode() % 10);
                }
            } catch (Exception e) {
                builder.append(e.getMessage().hashCode() % 10);
            }
            String UID = new UUID(builder.toString().hashCode(), getIMEI().hashCode()).toString();
            saveDeviceId(UID);
            return UID;
        }
    }

    public static String getIMEI() {
        Context ctx = HelperNative.getApplication();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            try {
                TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ctx.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    if (tm != null) {
                        return tm.getDeviceId();
                    }
                }
            } catch (Exception e) {
                return "no IMEI";
            }
        } else {
            return "this is android 10";
        }
        return "no permission";
    }


    private static int getDevicePixel() {
        DisplayMetrics dm = new DisplayMetrics();
        return dm.heightPixels + dm.widthPixels;
    }

    /**
     * 获取MAC地址
     *
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {
        String mac;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mac = getMacDefault(context);
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mac = getMacFromFile();
        } else {
            mac = getMacFromHardware();
        }
        return mac;
    }

    /**
     * Android  6.0 之前（不包括6.0）
     * 必须的权限  <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
     *
     * @param context
     * @return
     */
    private static String getMacDefault(Context context) {
        String mac = "02:00:00:00:00:00";
        if (context == null) {
            return mac;
        }

        WifiManager wifi = (WifiManager) context.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        if (wifi == null) {
            return mac;
        }
        WifiInfo info = null;
        try {
            info = wifi.getConnectionInfo();
        } catch (Exception e) {
        }
        if (info == null) {
            return null;
        }
        mac = info.getMacAddress();
        if (!TextUtils.isEmpty(mac)) {
            mac = mac.toUpperCase(Locale.ENGLISH);
        }
        return mac;
    }

    /**
     * Android 6.0（包括） - Android 7.0（不包括）
     *
     * @return
     */
    private static String getMacFromFile() {
        String WifiAddress = "02:00:00:00:00:00";
        try {
            WifiAddress = new BufferedReader(new FileReader(new File("/sys/class/net/wlan0/address"))).readLine();
        } catch (IOException e) {
        }
        return WifiAddress;
    }

    /**
     * 遍历循环所有的网络接口，找到接口是 wlan0
     * 必须的权限 <uses-permission android:name="android.permission.INTERNET" />
     *
     * @return
     */
    private static String getMacFromHardware() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception e) {
        }
        return "02:00:00:00:00:00";
    }
}
