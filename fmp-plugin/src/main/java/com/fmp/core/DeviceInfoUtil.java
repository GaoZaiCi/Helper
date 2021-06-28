package com.fmp.core;

import android.text.TextUtils;

import com.fmp.data.InternetProtocol;
import com.fmp.util.UnicodeUtil;

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

public class DeviceInfoUtil {
    private static DeviceInfoUtil deviceInfoUtil;

    public static DeviceInfoUtil getInstance() {
        if (deviceInfoUtil == null)
            deviceInfoUtil = new DeviceInfoUtil();
        return deviceInfoUtil;
    }


    void getInternetProtocolInfo(onInternetProtocolInfo internetProtocolInfo) {
        new Thread(() -> {
            InternetProtocol info = new InternetProtocol();
            try {
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
                        JSONObject object = new JSONObject(UnicodeUtil.toString(str).replaceAll("\\\\", ""));
                        JSONObject content = object.getJSONObject("content");
                        JSONObject point = content.getJSONObject("point");
                        JSONObject address_detail = content.getJSONObject("address_detail");
                        info.setCity(content.getString("address"));
                        info.setProvince(address_detail.getString("province"));
                        info.setCityCode(address_detail.getInt("city_code"));
                        info.setPointX(Double.parseDouble(point.getString("x")));
                        info.setPointY(Double.parseDouble(point.getString("y")));
                        bufferedReader.close();
                    }
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
            File file = new File(GameManager.getApplication().getExternalFilesDir("netease"), "info.db");
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            return (InternetProtocol) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveInternetProtocol(InternetProtocol info) {
        try {
            File file = new File(GameManager.getApplication().getExternalFilesDir("netease"), "info.db");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
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
