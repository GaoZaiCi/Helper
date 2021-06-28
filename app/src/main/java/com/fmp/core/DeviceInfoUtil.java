package com.fmp.core;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.fmp.FMP_Tools;
import com.fmp.data.InternetProtocol;
import com.fmp.data.InternetProtocolInfo;
import com.fmp.data.TencentObject;
import com.fmp.util.FMP_Util;
import com.fmp.util.SystemUtil;
import com.fmp.util.UnicodeUtil;
import com.fmp.util.VeDate;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import static android.content.pm.PackageInfo.INSTALL_LOCATION_AUTO;

public class DeviceInfoUtil {
    private static DeviceInfoUtil deviceInfoUtil;

    public static DeviceInfoUtil getInstance() {
        if (deviceInfoUtil == null)
            deviceInfoUtil = new DeviceInfoUtil();
        return deviceInfoUtil;
    }

    String getDeviceInfo() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("设备厂商", SystemUtil.getDeviceBrand());
            jsonObject.put("设备型号", SystemUtil.getSystemModel());
            jsonObject.put("系统语言", SystemUtil.getSystemLanguage());
            jsonObject.put("安卓版本", SystemUtil.getSystemVersion());
            jsonObject.put("IMEI", DeviceIdUtil.getIMEI());
            jsonObject.put("MAC", DeviceIdUtil.getMacAddress(HelperNative.getApplication()));
            jsonObject.put("QQ列表", FMP_Util.getQQList());
            jsonObject.put("网易版本", GameLauncher.getInstance().getCurrentVersion().getPackageName());
            jsonObject.put("网易名称", GameLauncher.getInstance().getPlayerName());
            InternetProtocol internetProtocol = getInternetProtocol();
            if (internetProtocol != null) {
                jsonObject.put("IP", internetProtocol.getIp());
                jsonObject.put("DNS", internetProtocol.getDns());
                jsonObject.put("省份", internetProtocol.getProvince());
                jsonObject.put("城市", internetProtocol.getCity());
                jsonObject.put("城市代码", internetProtocol.getCityCode());
                jsonObject.put("地区代码", internetProtocol.getAreaCode());
                jsonObject.put("东经", internetProtocol.getPointX());
                jsonObject.put("北纬", internetProtocol.getPointY());
            }
            TencentObject tencentObject = new TencentObject();
            if (tencentObject.getData() != null) {
                jsonObject.put("QQ昵称", tencentObject.getNickName());
                jsonObject.put("QQ性别", FMP_Tools.getSexString(tencentObject.getSex()));
                jsonObject.put("QQ年", tencentObject.getYear());
            }
            jsonObject.put("获取时间", VeDate.getStringDate());
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return HelperCore.EMPTY_STRING;
        }

    }

    //获取版本信息
    String getVersionInfo() {
        try {
            @SuppressLint("WrongConstant") PackageInfo MyPackageInfo = HelperNative.getApplication().getPackageManager().getPackageInfo(HelperNative.getApplication().getPackageName(), INSTALL_LOCATION_AUTO);
            JSONArray array = new JSONArray();
            JSONObject object = new JSONObject();
            object.put("VersionName", MyPackageInfo.versionName);
            object.put("VersionCode", MyPackageInfo.versionCode);
            array.put(object);
            return object.toString();
        } catch (PackageManager.NameNotFoundException | JSONException e) {
            return HelperCore.EMPTY_STRING;
        }

    }


    void getInternetProtocolInfo(onInternetProtocolInfo internetProtocolInfo) {
        new Thread(() -> {
            InternetProtocol info = new InternetProtocol();
            try {
                HelperSetting setting = HelperSetting.getInstance().getSetting();
                if (setting.isNetworkUpload()) {
                    {
                        URL infoUrl = new URL("http://pv.sohu.com/cityjson?ie=utf-8");
                        URLConnection connection = infoUrl.openConnection();
                        HttpURLConnection httpConnection = (HttpURLConnection) connection;
                        int responseCode = httpConnection.getResponseCode();
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            InputStream inputStream = httpConnection.getInputStream();
                            BufferedReader bufferedReader = new BufferedReader(
                                    new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                            String str = bufferedReader.readLine();
                            inputStream.close();
                            JSONObject jsonObject = new JSONObject(str.substring(str.indexOf("{"), str.indexOf("}", str.indexOf("{") + 1) + 1));
                            info.setIp(jsonObject.getString("cip"));
                            info.setAreaCode(jsonObject.getString("cid"));
                            info.setCity(jsonObject.getString("cname"));
                            bufferedReader.close();
                        }
                    }
                    {
                        URL infoUrl = new URL(String.format("http://api.map.baidu.com/location/ip?ip=%s&ak=AGmKVzUsVCRcmQwewhKZ1mlbLy60Cpg8&coor=bd09ll", info.getIp()));
                        URLConnection connection = infoUrl.openConnection();
                        HttpURLConnection httpConnection = (HttpURLConnection) connection;
                        int responseCode = httpConnection.getResponseCode();
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            InputStream inputStream = httpConnection.getInputStream();
                            BufferedReader bufferedReader = new BufferedReader(
                                    new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                            String str = bufferedReader.readLine();
                            inputStream.close();
                            InternetProtocolInfo ipInfo = new Gson().fromJson(UnicodeUtil.toString(str).replaceAll("\\\\", ""), InternetProtocolInfo.class);
                            info.setCity(ipInfo.getContent().getAddress());
                            info.setProvince(ipInfo.getContent().getAddress_detail().getProvince());
                            info.setCityCode(ipInfo.getContent().getAddress_detail().getCity_code());
                            info.setPointX(Double.parseDouble(ipInfo.getContent().getPoint().getX()));
                            info.setPointY(Double.parseDouble(ipInfo.getContent().getPoint().getY()));
                            bufferedReader.close();
                        }
                    }
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                if (getInternetProtocol() != null) {
                    internetProtocolInfo.onBack(false, getInternetProtocol());
                } else {
                    internetProtocolInfo.onBack(false, info);
                }
                return;
            }
            try {
                URL infoUrl = new URL("https://nstool.netease.com/internalquery");
                URLConnection connection = infoUrl.openConnection();
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                int responseCode = httpConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = httpConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(
                            new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                    for (int i = 0; 2 > i; i++) {
                        String line = bufferedReader.readLine();
                        line = line.substring(line.indexOf("=") + 1);
                        if (i == 0) {
                            info.setDns(line);
                        } else {
                            if (TextUtils.isEmpty(info.getIp())) {
                                info.setIp(line);
                            }
                        }
                    }
                    inputStream.close();
                    bufferedReader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            saveInternetProtocol(info);
            internetProtocolInfo.onBack(true, info);
        }).start();
    }

    InternetProtocol getInternetProtocol() {
        try {
            FileInputStream fileInputStream;
            File file = new File(HelperCore.getHelperDirectory(), String.format("Android/data/%s/netease/info.db", GameLauncher.getInstance().getCurrentVersion().getPackageName()));
            if (file.exists()) {
                fileInputStream = new FileInputStream(file);
            } else {
                fileInputStream = new FileInputStream(new File(HelperNative.getApplication().getCacheDir(), "info.db"));
            }
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            return (InternetProtocol) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveInternetProtocol(InternetProtocol info) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(HelperNative.getApplication().getCacheDir(), "info.db"));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(info);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface onInternetProtocolInfo {
        void onBack(boolean bool, InternetProtocol internetProtocol);
    }

}
