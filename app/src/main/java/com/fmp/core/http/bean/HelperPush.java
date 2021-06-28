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

public class HelperPush {
    public static final String CLASS_TABLE_NAME = "FMP_USER_PUSH";
    public String objectId;
    public String createdAt;
    public String updatedAt;
    public boolean push;
    public String name;
    public String type;
    public String tag;
    public String data;
    public String startTime;
    public String endTime;

    public HelperPush(String objectId, String createdAt, String updatedAt, boolean push, String name, String type, String tag, String data, String startTime, String endTime) {
        this.objectId = objectId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.push = push;
        this.name = name;
        this.type = type;
        this.tag = tag;
        this.data = data;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public HelperPush(HelperJSONObject object) {
        updateLocalData(object);
    }

    public HelperPush updateLocalData(HelperJSONObject object) {
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
                push = (boolean) object.get(FMP_Tools.IntArrayToString(new int[]{111, 121, 86, 123, 121, 110})/*isPush*/, boolean.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                name = (String) object.get(FMP_Tools.IntArrayToString(new int[]{114, 101, 113, 105})/*name*/, String.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                type = (String) object.get(FMP_Tools.IntArrayToString(new int[]{120, 125, 116, 105})/*type*/, String.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                tag = (String) object.get(FMP_Tools.IntArrayToString(new int[]{119, 100, 106})/*tag*/, String.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                data = (String) object.get(FMP_Tools.IntArrayToString(new int[]{104, 101, 120, 101})/*data*/, String.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                HelperJSONObject helperJSONObject=new HelperJSONObject((String) object.get(FMP_Tools.IntArrayToString(new int[]{124, 125, 106, 123, 125, 93, 114, 118, 110})/*startTime*/, String.class));
                startTime = helperJSONObject.getString("iso");
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                HelperJSONObject helperJSONObject=new HelperJSONObject((String) object.get(FMP_Tools.IntArrayToString(new int[]{108, 117, 107, 91, 112, 116, 108})/*endTime*/, String.class));
                endTime =  helperJSONObject.getString("iso");
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

    public HelperPush readData() throws Exception {
        SharedPreferences sp = HelperNative.getApplication().getSharedPreferences(String.valueOf(CLASS_TABLE_NAME.hashCode()), Context.MODE_PRIVATE);
        DesUtils utils = new DesUtils(String.valueOf(CLASS_TABLE_NAME.hashCode()));
        String str = sp.getString(String.valueOf(CLASS_TABLE_NAME.length()), HelperCore.EMPTY_STRING);
        if (!TextUtils.isEmpty(str)) {
            updateLocalData(new HelperJSONObject(utils.decrypt(str)));
        }
        return this;
    }

    public HelperPush saveData() throws Exception {
        SharedPreferences sp = HelperNative.getApplication().getSharedPreferences(String.valueOf(CLASS_TABLE_NAME.hashCode()), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        DesUtils utils = new DesUtils(String.valueOf(CLASS_TABLE_NAME.hashCode()));
        HelperJSONObject object = new HelperJSONObject();
        object.put(FMP_Tools.IntArrayToString(new int[]{111, 121, 86, 123, 121, 110})/*isPush*/, push);
        object.put(FMP_Tools.IntArrayToString(new int[]{114, 101, 113, 105})/*name*/, name);
        object.put(FMP_Tools.IntArrayToString(new int[]{120, 125, 116, 105})/*type*/, type);
        object.put(FMP_Tools.IntArrayToString(new int[]{119, 100, 106})/*tag*/, tag);
        object.put(FMP_Tools.IntArrayToString(new int[]{104, 101, 120, 101})/*data*/, data);
        object.put(FMP_Tools.IntArrayToString(new int[]{124, 125, 106, 123, 125, 93, 114, 118, 110})/*startTime*/, startTime);
        object.put(FMP_Tools.IntArrayToString(new int[]{108, 117, 107, 91, 112, 116, 108})/*endTime*/, endTime);
        editor.putString(String.valueOf(CLASS_TABLE_NAME.length()), utils.encrypt(object.toString()));
        editor.apply();
        return this;
    }
}
