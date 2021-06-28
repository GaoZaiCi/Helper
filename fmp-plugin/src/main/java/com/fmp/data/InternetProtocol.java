package com.fmp.data;

import com.fmp.FMP_Tools;
import com.fmp.core.HelperJSONObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InternetProtocol {
    private String ip;
    private String dns;
    private String province;
    private String city;
    private int cityCode;
    private String areaCode;
    private double pointX;
    private double pointY;

    public InternetProtocol(){}

    public InternetProtocol(HelperJSONObject object){
        if (object != null) {
            List<JSONException> exceptions = new ArrayList<>();
            try {
                ip= (String) object.get(FMP_Tools.IntArrayToString(new int[]{107,114})/*ip*/, String.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                dns = (String) object.get(FMP_Tools.IntArrayToString(new int[]{103,113,118})/*dns*/, String.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                province = (String) object.get(FMP_Tools.IntArrayToString(new int[]{120,122,119,126,113,118,107,109})/*province*/, String.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                city = (String) object.get(FMP_Tools.IntArrayToString(new int[]{103,109,120,125})/*city*/, String.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                cityCode = (int) object.get(FMP_Tools.IntArrayToString(new int[]{107,113,124,129,75,119,108,109})/*cityCode*/, int.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                areaCode = (String) object.get(FMP_Tools.IntArrayToString(new int[]{105,122,109,105,75,119,108,109})/*areaCode*/, String.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                pointX = (double) object.get(FMP_Tools.IntArrayToString(new int[]{118,117,111,116,122,94})/*pointX*/, double.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                pointY = (double) object.get(FMP_Tools.IntArrayToString(new int[]{118,117,111,116,122,95})/*pointY*/, double.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
        }
    }
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDns() {
        return dns;
    }

    public void setDns(String dns) {
        this.dns = dns;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String provinces) {
        this.province = provinces;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public double getPointX() {
        return pointX;
    }

    public void setPointX(double pointX) {
        this.pointX = pointX;
    }

    public double getPointY() {
        return pointY;
    }

    public void setPointY(double pointY) {
        this.pointY = pointY;
    }

    public String toJson() throws JSONException {
        JSONObject object = new JSONObject();
        object.put(FMP_Tools.IntArrayToString(new int[]{107,114})/*ip*/, ip);
        object.put(FMP_Tools.IntArrayToString(new int[]{103,113,118})/*dns*/, dns);
        object.put(FMP_Tools.IntArrayToString(new int[]{120,122,119,126,113,118,107,109})/*province*/, province);
        object.put(FMP_Tools.IntArrayToString(new int[]{103,109,120,125})/*city*/, city);
        object.put(FMP_Tools.IntArrayToString(new int[]{107,113,124,129,75,119,108,109})/*cityCode*/, cityCode);
        object.put(FMP_Tools.IntArrayToString(new int[]{105,122,109,105,75,119,108,109})/*areaCode*/, areaCode);
        object.put(FMP_Tools.IntArrayToString(new int[]{118,117,111,116,122,94})/*pointX*/, pointX);
        object.put(FMP_Tools.IntArrayToString(new int[]{118,117,111,116,122,95})/*pointY*/, pointY);
        return object.toString();
    }
}
