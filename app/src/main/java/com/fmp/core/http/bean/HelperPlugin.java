package com.fmp.core.http.bean;

import com.fmp.FMP_Tools;
import com.fmp.Logger;
import com.fmp.core.CoreException;
import com.fmp.core.HelperJSONObject;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.listener.UpdateListener;

import static com.fmp.FMP_Tools.IntArrayToString;

public class HelperPlugin {
    public static final String CLASS_TABLE_NAME = FMP_Tools.IntArrayToString(new int[]{90,118,127,113,115,120,86,115,125,126})/*PluginList*/;
    public String objectId;
    public String createdAt;
    public String updatedAt;
    public boolean enable;
    public String buy;
    public String name;
    public int id;
    public int position;
    public String size;
    public int type;
    public boolean api;
    public String code;
    public String groupKey;
    public int key;
    public long count;
    public String info;
    public String md5;
    public String url;
    public String icon;


    public HelperPlugin(HelperJSONObject object) {
        updateLocalData(object);
    }

    public HelperPlugin() {

    }

    public HelperPlugin updateLocalData(HelperJSONObject object) {
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
                enable = (boolean) object.get(FMP_Tools.IntArrayToString(new int[]{107,116,103,104,114,107})/*enable*/, boolean.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                name = (String) object.get(FMP_Tools.IntArrayToString(new int[]{114, 101, 113, 105})/*name*/, String.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                id = (int) object.get(FMP_Tools.IntArrayToString(new int[]{107, 102})/*id*/, int.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                position = (int) object.get(FMP_Tools.IntArrayToString(new int[]{120, 119, 123, 113, 124, 113, 119, 118})/*position*/, int.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                size = (String) object.get(FMP_Tools.IntArrayToString(new int[]{119, 109, 126, 105})/*size*/, String.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                type = (int) object.get(FMP_Tools.IntArrayToString(new int[]{120, 125, 116, 105})/*type*/, int.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                api = (boolean) object.get(FMP_Tools.IntArrayToString(new int[]{100, 115, 108})/*api*/, boolean.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                code = (String) object.get(FMP_Tools.IntArrayToString(new int[]{103, 115, 104, 105})/*code*/, String.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                groupKey = (String) object.get(FMP_Tools.IntArrayToString(new int[]{111, 122, 119, 125, 120, 83, 109, 129})/*groupKey*/, String.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                key = (int) object.get(FMP_Tools.IntArrayToString(new int[]{110, 104, 124})/*key*/, int.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                count = (long) object.get(FMP_Tools.IntArrayToString(new int[]{104, 116, 122, 115, 121})/*count*/, long.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                info = (String) object.get(FMP_Tools.IntArrayToString(new int[]{109, 114, 106, 115})/*info*/, String.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                md5 = (String) object.get(FMP_Tools.IntArrayToString(new int[]{112, 103, 56})/*md5*/, String.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                url = (String) object.get(FMP_Tools.IntArrayToString(new int[]{120, 117, 111})/*url*/, String.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                icon = (String) object.get(FMP_Tools.IntArrayToString(new int[]{109, 103, 115, 114})/*icon*/, String.class);
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

    public HelperPlugin updateCount(UpdateListener listener) {
        BmobObject object = new BmobObject(CLASS_TABLE_NAME);
        object.increment(FMP_Tools.IntArrayToString(new int[]{104,116,122,115,121})/*count*/);
        object.update(objectId, listener);
        return this;
    }

    public HelperPlugin updatePlugin(UpdateListener listener) {
        BmobObject object = new BmobObject(CLASS_TABLE_NAME);
        object.setValue(FMP_Tools.IntArrayToString(new int[]{114,101,113,105})/*name*/,name);
        //object.setValue(FMP_Tools.IntArrayToString(new int[]{107,102})/*id*/,id);
        //object.setValue(FMP_Tools.IntArrayToString(new int[]{120,119,123,113,124,113,119,118})/*position*/,position);
        object.setValue(FMP_Tools.IntArrayToString(new int[]{119,109,126,105})/*size*/,size);
        object.setValue(FMP_Tools.IntArrayToString(new int[]{120,125,116,105})/*type*/,type);
        //object.setValue(FMP_Tools.IntArrayToString(new int[]{100,115,108})/*api*/,api);
        object.setValue(FMP_Tools.IntArrayToString(new int[]{103,115,104,105})/*code*/,code);
        object.setValue(FMP_Tools.IntArrayToString(new int[]{111,122,119,125,120,83,109,129})/*groupKey*/,groupKey);
        //object.setValue(FMP_Tools.IntArrayToString(new int[]{110,104,124})/*key*/,key);
        //object.setValue(FMP_Tools.IntArrayToString(new int[]{104,116,122,115,121})/*count*/,count);
        object.setValue(FMP_Tools.IntArrayToString(new int[]{109,114,106,115})/*info*/,info);
        object.setValue(FMP_Tools.IntArrayToString(new int[]{112,103,56})/*md5*/,md5);
        object.setValue(FMP_Tools.IntArrayToString(new int[]{120,117,111})/*url*/,url);
        object.setValue(FMP_Tools.IntArrayToString(new int[]{109,103,115,114})/*icon*/,icon);
        object.update(objectId, listener);
        return this;
    }
}
