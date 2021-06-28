package com.fmp.core.http.bean;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.fmp.FMP_Tools;
import com.fmp.Logger;
import com.fmp.core.CoreException;
import com.fmp.core.HelperCore;
import com.fmp.core.HelperJSONObject;
import com.fmp.core.HelperNative;
import com.fmp.util.DesUtils;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.fmp.FMP_Tools.IntArrayToString;

public class HelperBanDevice {
    public static final String CLASS_TABLE_NAME = "BAN_DEVICE";
    public String objectId;
    public String createdAt;
    public String updatedAt;
    public String reason;
    public String qq;
    public String uid;
    public String imei;
    public String systemModel;
    public String deviceBrand;
    public String ip;
    public boolean isBan;
    public String endTime;
    public String mac;

    public HelperBanDevice() {

    }

    public HelperBanDevice(HelperJSONObject object) {
        updateLocalData(object);
    }

    public HelperBanDevice(String objectId, String createdAt, String updatedAt, String reason, String qq, String uid, String imei, String systemModel, String deviceBrand, String ip, boolean isBan, String endTime, String mac) {
        this.objectId = objectId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.reason = reason;
        this.qq = qq;
        this.uid = uid;
        this.imei = imei;
        this.systemModel = systemModel;
        this.deviceBrand = deviceBrand;
        this.ip = ip;
        this.isBan = isBan;
        this.endTime = endTime;
        this.mac = mac;
    }

    public HelperBanDevice updateLocalData(HelperJSONObject object) {
        if (object != null) {
            List<JSONException> exceptions = new ArrayList<>();
            try {
                objectId = (String) object.get(IntArrayToString(new int[]{119, 106, 114, 109, 107, 124, 81, 108})/*objectId*/, String.class);
                updatedAt = (String) object.get(IntArrayToString(new int[]{126, 121, 109, 106, 125, 110, 109, 74, 125})/*updatedAt*/, String.class);
                createdAt = (String) object.get(IntArrayToString(new int[]{108, 123, 110, 106, 125, 110, 109, 74, 125})/*createdAt*/, String.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                reason = (String) object.get(FMP_Tools.IntArrayToString(new int[]{120, 107, 103, 121, 117, 116})/*reason*/, String.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                qq = (String) object.get(FMP_Tools.IntArrayToString(new int[]{115, 115})/*qq*/, String.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                uid = (String) object.get(FMP_Tools.IntArrayToString(new int[]{120, 108, 103})/*uid*/, String.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                imei = (String) object.get(FMP_Tools.IntArrayToString(new int[]{109, 113, 105, 109})/*imei*/, String.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                systemModel = (String) object.get(FMP_Tools.IntArrayToString(new int[]{126, 132, 126, 127, 112, 120, 88, 122, 111, 112, 119})/*systemModel*/, String.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                deviceBrand = (String) object.get(FMP_Tools.IntArrayToString(new int[]{111, 112, 129, 116, 110, 112, 77, 125, 108, 121, 111})/*deviceBrand*/, String.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                ip = (String) object.get(FMP_Tools.IntArrayToString(new int[]{107, 114})/*ip*/, String.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                isBan = (boolean) object.get(FMP_Tools.IntArrayToString(new int[]{110, 120, 71, 102, 115})/*isBan*/, boolean.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                HelperJSONObject helperJSONObject = new HelperJSONObject((String) object.get(FMP_Tools.IntArrayToString(new int[]{108, 117, 107, 91, 112, 116, 108})/*endTime*/, String.class));
                endTime = helperJSONObject.getString("iso");
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                mac = (String) object.get(FMP_Tools.IntArrayToString(new int[]{112, 100, 102})/*mac*/, String.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            if (CoreException.deBugMode) {
                for (JSONException exception : exceptions) {
                    Logger.toString(CLASS_TABLE_NAME, exception);
                }
            }
        }
        return this;
    }

    public HelperBanDevice readData() throws Exception {
        SharedPreferences sp = HelperNative.getApplication().getSharedPreferences(String.valueOf(CLASS_TABLE_NAME.hashCode()), Context.MODE_PRIVATE);
        DesUtils utils = new DesUtils(String.valueOf(CLASS_TABLE_NAME.hashCode()));
        String str = sp.getString(String.valueOf(CLASS_TABLE_NAME.length()), HelperCore.EMPTY_STRING);
        if (!TextUtils.isEmpty(str)) {
            updateLocalData(new HelperJSONObject(utils.decrypt(str)));
        }
        return this;
    }

    public HelperBanDevice saveData() throws Exception {
        SharedPreferences sp = HelperNative.getApplication().getSharedPreferences(String.valueOf(CLASS_TABLE_NAME.hashCode()), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        DesUtils utils = new DesUtils(String.valueOf(CLASS_TABLE_NAME.hashCode()));
        HelperJSONObject object = new HelperJSONObject();
        object.put(IntArrayToString(new int[]{119, 106, 114, 109, 107, 124, 81, 108})/*objectId*/, objectId);
        object.put(IntArrayToString(new int[]{126, 121, 109, 106, 125, 110, 109, 74, 125})/*updatedAt*/, updatedAt);
        object.put(IntArrayToString(new int[]{108, 123, 110, 106, 125, 110, 109, 74, 125})/*createdAt*/, createdAt);
        object.put(IntArrayToString(new int[]{120, 107, 103, 121, 117, 116})/*reason*/, reason);
        object.put(IntArrayToString(new int[]{115, 115})/*qq*/, qq);
        object.put(IntArrayToString(new int[]{120, 108, 103})/*uid*/, uid);
        object.put(IntArrayToString(new int[]{109, 113, 105, 109})/*imei*/, imei);
        object.put(IntArrayToString(new int[]{126, 132, 126, 127, 112, 120, 88, 122, 111, 112, 119})/*systemModel*/, systemModel);
        object.put(IntArrayToString(new int[]{111, 112, 129, 116, 110, 112, 77, 125, 108, 121, 111})/*deviceBrand*/, deviceBrand);
        object.put(IntArrayToString(new int[]{108, 117, 107, 91, 112, 116, 108})/*endTime*/, endTime);
        object.put(IntArrayToString(new int[]{107, 114})/*ip*/, ip);
        object.put(IntArrayToString(new int[]{110, 120, 71, 102, 115})/*isBan*/, isBan);
        object.put(IntArrayToString(new int[]{112, 100, 102})/*mac*/, mac);
        editor.putString(String.valueOf(CLASS_TABLE_NAME.length()), utils.encrypt(object.toString()));
        editor.apply();
        return this;
    }

    public void removeData(){
        SharedPreferences sp = HelperNative.getApplication().getSharedPreferences(String.valueOf(CLASS_TABLE_NAME.hashCode()), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }
}
