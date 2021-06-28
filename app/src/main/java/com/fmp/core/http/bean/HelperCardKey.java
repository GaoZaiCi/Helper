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

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.listener.UpdateListener;

import static com.fmp.FMP_Tools.IntArrayToString;

public class HelperCardKey {
    public static final String CLASS_TABLE_NAME = FMP_Tools.IntArrayToString(new int[]{74,104,121,107,114,108,128})/*Cardkey*/;
    public String objectId;
    public String createdAt;
    public String updatedAt;
    public boolean enabled;
    public String key;
    public String data;

    public HelperCardKey(String objectId, String createdAt, String updatedAt, boolean enabled, String key, String data) {
        this.objectId = objectId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.enabled = enabled;
        this.key = key;
        this.data = data;
    }

    public HelperCardKey(HelperJSONObject object){
        updateLocalData(object);
    }


    public HelperCardKey updateLocalData(HelperJSONObject object) {
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
                enabled = (boolean) object.get(FMP_Tools.IntArrayToString(new int[]{108,117,104,105,115,108,107})/*enabled*/, boolean.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                data = (String) object.get(FMP_Tools.IntArrayToString(new int[]{104,101,120,101})/*data*/, String.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                key = (String) object.get(FMP_Tools.IntArrayToString(new int[]{110,104,124})/*key*/, String.class);
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

    public HelperCardKey updateEnabled(UpdateListener listener,boolean isEnabled) {
        BmobObject object = new BmobObject(CLASS_TABLE_NAME);
        object.setValue(FMP_Tools.IntArrayToString(new int[]{108,117,104,105,115,108,107})/*enabled*/,isEnabled);
        object.update(objectId, listener);
        return this;
    }

    public HelperCardKey readData() throws Exception {
        SharedPreferences sp = HelperNative.getApplication().getSharedPreferences(String.valueOf(CLASS_TABLE_NAME.hashCode()), Context.MODE_PRIVATE);
        DesUtils utils=new DesUtils(String.valueOf(CLASS_TABLE_NAME.hashCode()));
        String str=sp.getString(String.valueOf(CLASS_TABLE_NAME.length()), HelperCore.EMPTY_STRING);
        if (!TextUtils.isEmpty(str)){
            updateLocalData(new HelperJSONObject(utils.decrypt(str)));
        }
        return this;
    }

    public HelperCardKey saveData() throws Exception {
        SharedPreferences sp = HelperNative.getApplication().getSharedPreferences(String.valueOf(CLASS_TABLE_NAME.hashCode()), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        DesUtils utils=new DesUtils(String.valueOf(CLASS_TABLE_NAME.hashCode()));
        HelperJSONObject object=new HelperJSONObject();
        object.put(IntArrayToString(new int[]{119, 106, 114, 109, 107, 124, 81, 108})/*objectId*/, objectId);
        object.put(IntArrayToString(new int[]{126, 121, 109, 106, 125, 110, 109, 74, 125})/*updatedAt*/, updatedAt);
        object.put(IntArrayToString(new int[]{108, 123, 110, 106, 125, 110, 109, 74, 125})/*createdAt*/, createdAt);
        object.put(FMP_Tools.IntArrayToString(new int[]{108,117,104,105,115,108,107})/*enabled*/, enabled);
        object.put(FMP_Tools.IntArrayToString(new int[]{104,101,120,101})/*data*/, data);
        object.put(FMP_Tools.IntArrayToString(new int[]{110,104,124})/*key*/, key);
        editor.putString(String.valueOf(CLASS_TABLE_NAME.length()),utils.encrypt(object.toString()));
        editor.apply();
        return this;
    }
}
