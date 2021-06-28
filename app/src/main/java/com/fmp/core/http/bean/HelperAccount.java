package com.fmp.core.http.bean;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.fmp.Logger;
import com.fmp.core.CoreException;
import com.fmp.core.HelperCore;
import com.fmp.core.HelperJSONObject;
import com.fmp.core.HelperNative;
import com.fmp.util.DesUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import static com.fmp.FMP_Tools.IntArrayToString;

public class HelperAccount {
    public static final String CLASS_TABLE_NAME = IntArrayToString(new int[]{83, 90, 93, 108, 98, 96, 82, 95, 108, 81, 78, 97, 78});//FMP_USER_DATA
    public boolean isLogin;
    public String objectId;
    public String createdAt;
    public String updatedAt;
    public String userName;
    public String userType;
    public String headUrl;
    public int userSex;
    public String account;
    public int userLevel;
    public String versionInfo;
    public String deviceInfo;
    public String qq;
    public int id;
    public String uid;
    public int launchCount;
    public String signInInfo;
    public String openId;
    public String info;
    public Collection<Integer> gameMods=new ArrayList<>();
    public Collection<Integer> plugins=new ArrayList<>();

    public HelperAccount() {
    }

    public HelperAccount(String objectId, String createdAt, String updatedAt, String userName, String userType, String headUrl, int userSex, String account, int userLevel, String versionInfo, String deviceInfo, String qq, int id, String uid, int launchCount, String signInInfo, String openId, Collection<Integer> gameMods, String info,Collection<Integer> plugins) {
        this.objectId = objectId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.userName = userName;
        this.userType = userType;
        this.headUrl = headUrl;
        this.userSex = userSex;
        this.account = account;
        this.userLevel = userLevel;
        this.versionInfo = versionInfo;
        this.deviceInfo = deviceInfo;
        this.qq = qq;
        this.id = id;
        this.uid = uid;
        this.launchCount = launchCount;
        this.signInInfo = signInInfo;
        this.openId = openId;
        this.gameMods = gameMods;
        this.info = info;
        this.plugins = plugins;
    }

    public HelperAccount(HelperJSONObject object) {
        updateLocalData(object);
    }

    public HelperAccount updateLocalData(HelperJSONObject object) {
        if (object != null) {
            List<Throwable> exceptions = new ArrayList<>();
            try {
                objectId = (String) object.get(IntArrayToString(new int[]{119, 106, 114, 109, 107, 124, 81, 108})/*objectId*/, String.class);
                updatedAt = (String) object.get(IntArrayToString(new int[]{126, 121, 109, 106, 125, 110, 109, 74, 125})/*updatedAt*/, String.class);
                createdAt = (String) object.get(IntArrayToString(new int[]{108, 123, 110, 106, 125, 110, 109, 74, 125})/*createdAt*/, String.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                userName = (String) object.get(IntArrayToString(new int[]{93, 123, 109, 122, 86, 105, 117, 109})/*UserName*/, String.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                userType = (String) object.get(IntArrayToString(new int[]{93, 123, 109, 122, 92, 129, 120, 109})/*UserType*/, String.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                headUrl = (String) object.get(IntArrayToString(new int[]{79, 108, 104, 107, 92, 121, 115})/*HeadUrl*/, String.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                userSex = (int) object.get(IntArrayToString(new int[]{92, 122, 108, 121, 90, 108, 127})/*UserSex*/, int.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                account = (String) object.get(IntArrayToString(new int[]{72, 106, 106, 118, 124, 117, 123})/*Account*/, String.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                userLevel = (int) object.get(IntArrayToString(new int[]{94, 124, 110, 123, 85, 110, 127, 110, 117})/*UserLevel*/, int.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                versionInfo = (String) object.get(IntArrayToString(new int[]{97, 112, 125, 126, 116, 122, 121, 84, 121, 113, 122})/*VersionInfo*/, String.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                deviceInfo = (String) object.get(IntArrayToString(new int[]{89, 113, 120, 119, 110, 82, 119, 111, 120})/*PhoneInfo*/, String.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                qq = (String) object.get(IntArrayToString(new int[]{83, 83})/*QQ*/, String.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                id = (int) object.get(IntArrayToString(new int[]{75, 70})/*ID*/, int.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                uid = (String) object.get(IntArrayToString(new int[]{88, 76, 71})/*UID*/, String.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                launchCount = (int) object.get(IntArrayToString(new int[]{87, 108, 128, 121, 110, 115, 78, 122, 128, 121, 127})/*LaunchCount*/, int.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                signInInfo = (String) object.get(IntArrayToString(new int[]{93, 115, 113, 120, 83, 120, 83, 120, 112, 121})/*SignInInfo*/, String.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                openId = (String) object.get(IntArrayToString(new int[]{85, 118, 107, 116, 79, 106})/*OpenId*/, String.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                JSONArray array = (JSONArray) object.get(IntArrayToString(new int[]{79, 105, 117, 109, 85, 119, 108, 123})/*GameMods*/, JSONArray.class);
                Integer[] ints=new Integer[array.length()];
                for (int i=0;ints.length>i;i++){
                    ints[i]=array.getInt(i);
                }
                gameMods.addAll(Arrays.asList(ints));
            } catch (Throwable e) {
                exceptions.add(e);
            }
            try {
                info = (String) object.get(IntArrayToString(new int[]{77, 114, 106, 115})/*Info*/, String.class);
            } catch (JSONException e) {
                exceptions.add(e);
            }
            try {
                JSONArray array = (JSONArray) object.get(IntArrayToString(new int[]{86, 114, 123, 109, 111, 116})/*Plugin*/, JSONArray.class);
                Integer[] ints=new Integer[array.length()];
                for (int i=0;ints.length>i;i++){
                    ints[i]=array.getInt(i);
                }
                plugins.addAll(Arrays.asList(ints));
            } catch (Throwable e) {
                exceptions.add(e);
            }
            if (CoreException.deBugMode) {
                for (Throwable exception : exceptions) {
                    Logger.toString(CLASS_TABLE_NAME, exception);
                }
            }
        }
        return this;
    }


    public HelperAccount createUser(SaveListener<String> listener) {
        try {
            HelperCore.getInstance().runCheck();
        } catch (Throwable e) {
            HelperNative.onError();
            e.printStackTrace();
        }
        BmobObject object = new BmobObject(CLASS_TABLE_NAME);
        object.setValue(IntArrayToString(new int[]{93, 123, 109, 122, 86, 105, 117, 109})/*UserName*/, userName);
        object.setValue(IntArrayToString(new int[]{93, 123, 109, 122, 92, 129, 120, 109})/*UserType*/, userType);
        object.setValue(IntArrayToString(new int[]{79, 108, 104, 107, 92, 121, 115})/*HeadUrl*/, headUrl);
        object.setValue(IntArrayToString(new int[]{92, 122, 108, 121, 90, 108, 127})/*UserSex*/, userSex);
        object.setValue(IntArrayToString(new int[]{72, 106, 106, 118, 124, 117, 123})/*Account*/, account);
        object.setValue(IntArrayToString(new int[]{94, 124, 110, 123, 85, 110, 127, 110, 117})/*UserLevel*/, userLevel);
        object.setValue(IntArrayToString(new int[]{97, 112, 125, 126, 116, 122, 121, 84, 121, 113, 122})/*VersionInfo*/, versionInfo);
        object.setValue(IntArrayToString(new int[]{89, 113, 120, 119, 110, 82, 119, 111, 120})/*PhoneInfo*/, deviceInfo);
        object.setValue(IntArrayToString(new int[]{83, 83})/*QQ*/, qq);
        object.setValue(IntArrayToString(new int[]{75, 70})/*ID*/, id);
        object.setValue(IntArrayToString(new int[]{88, 76, 71})/*UID*/, uid);
        object.setValue(IntArrayToString(new int[]{87, 108, 128, 121, 110, 115, 78, 122, 128, 121, 127})/*LaunchCount*/, launchCount);
        object.setValue(IntArrayToString(new int[]{79, 105, 117, 109, 85, 119, 108, 123})/*GameMods*/, gameMods.toArray());
        object.setValue(IntArrayToString(new int[]{93, 115, 113, 120, 83, 120, 83, 120, 112, 121})/*SignInInfo*/, signInInfo);
        object.setValue(IntArrayToString(new int[]{85, 118, 107, 116, 79, 106})/*OpenId*/, openId);
        object.setValue(IntArrayToString(new int[]{77, 114, 106, 115})/*Info*/, info);
        object.setValue(IntArrayToString(new int[]{86, 114, 123, 109, 111, 116})/*Plugin*/, plugins.toArray());
        object.save(listener);
        return this;
    }

    public HelperAccount updateUser(UpdateListener listener) {
        try {
            HelperCore.getInstance().runCheck();
        } catch (Throwable e) {
            HelperNative.onError();
        }
        BmobObject object = new BmobObject(CLASS_TABLE_NAME);
        object.setValue(IntArrayToString(new int[]{93, 123, 109, 122, 86, 105, 117, 109})/*UserName*/, userName);
        object.setValue(IntArrayToString(new int[]{93, 123, 109, 122, 92, 129, 120, 109})/*UserType*/, userType);
        object.setValue(IntArrayToString(new int[]{79, 108, 104, 107, 92, 121, 115})/*HeadUrl*/, headUrl);
        object.setValue(IntArrayToString(new int[]{92, 122, 108, 121, 90, 108, 127})/*UserSex*/, userSex);
        object.setValue(IntArrayToString(new int[]{72, 106, 106, 118, 124, 117, 123})/*Account*/, account);
        object.setValue(IntArrayToString(new int[]{94, 124, 110, 123, 85, 110, 127, 110, 117})/*UserLevel*/, userLevel);
        object.setValue(IntArrayToString(new int[]{97, 112, 125, 126, 116, 122, 121, 84, 121, 113, 122})/*VersionInfo*/, versionInfo);
        object.setValue(IntArrayToString(new int[]{89, 113, 120, 119, 110, 82, 119, 111, 120})/*PhoneInfo*/, deviceInfo);
        object.setValue(IntArrayToString(new int[]{83, 83})/*QQ*/, qq);
        object.setValue(IntArrayToString(new int[]{75, 70})/*ID*/, id);
        object.setValue(IntArrayToString(new int[]{88, 76, 71})/*UID*/, uid);
        object.setValue(IntArrayToString(new int[]{87, 108, 128, 121, 110, 115, 78, 122, 128, 121, 127})/*LaunchCount*/, launchCount);
        object.setValue(IntArrayToString(new int[]{79, 105, 117, 109, 85, 119, 108, 123})/*GameMods*/, gameMods.toArray());
        object.setValue(IntArrayToString(new int[]{93, 115, 113, 120, 83, 120, 83, 120, 112, 121})/*SignInInfo*/, signInInfo);
        object.setValue(IntArrayToString(new int[]{85, 118, 107, 116, 79, 106})/*OpenId*/, openId);
        object.setValue(IntArrayToString(new int[]{77, 114, 106, 115})/*Info*/, info);
        object.setValue(IntArrayToString(new int[]{86, 114, 123, 109, 111, 116})/*Plugin*/, plugins.toArray());
        object.update(objectId, listener);
        return this;
    }

    public HelperAccount readData() throws Exception {
        SharedPreferences sp = HelperNative.getApplication().getSharedPreferences(String.valueOf(CLASS_TABLE_NAME.hashCode()), Context.MODE_PRIVATE);
        DesUtils utils = new DesUtils(String.valueOf(CLASS_TABLE_NAME.hashCode()));
        String str = sp.getString(String.valueOf(CLASS_TABLE_NAME.length()), HelperCore.EMPTY_STRING);
        if (!TextUtils.isEmpty(str)) {
            updateLocalData(new HelperJSONObject(utils.decrypt(str)));
        }
        return this;
    }

    public HelperAccount saveData() throws Exception {
        SharedPreferences sp = HelperNative.getApplication().getSharedPreferences(String.valueOf(CLASS_TABLE_NAME.hashCode()), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        DesUtils utils = new DesUtils(String.valueOf(CLASS_TABLE_NAME.hashCode()));
        HelperJSONObject object = new HelperJSONObject();
        object.put(IntArrayToString(new int[]{119, 106, 114, 109, 107, 124, 81, 108})/*objectId*/, objectId);
        object.put(IntArrayToString(new int[]{126, 121, 109, 106, 125, 110, 109, 74, 125})/*updatedAt*/, updatedAt);
        object.put(IntArrayToString(new int[]{108, 123, 110, 106, 125, 110, 109, 74, 125})/*createdAt*/, createdAt);
        object.put(IntArrayToString(new int[]{93, 123, 109, 122, 86, 105, 117, 109})/*UserName*/, userName);
        object.put(IntArrayToString(new int[]{93, 123, 109, 122, 92, 129, 120, 109})/*UserType*/, userType);
        object.put(IntArrayToString(new int[]{79, 108, 104, 107, 92, 121, 115})/*HeadUrl*/, headUrl);
        object.put(IntArrayToString(new int[]{92, 122, 108, 121, 90, 108, 127})/*UserSex*/, userSex);
        object.put(IntArrayToString(new int[]{72, 106, 106, 118, 124, 117, 123})/*Account*/, account);
        object.put(IntArrayToString(new int[]{94, 124, 110, 123, 85, 110, 127, 110, 117})/*UserLevel*/, userLevel);
        object.put(IntArrayToString(new int[]{97, 112, 125, 126, 116, 122, 121, 84, 121, 113, 122})/*VersionInfo*/, versionInfo);
        object.put(IntArrayToString(new int[]{89, 113, 120, 119, 110, 82, 119, 111, 120})/*PhoneInfo*/, deviceInfo);
        object.put(IntArrayToString(new int[]{83, 83})/*QQ*/, qq);
        object.put(IntArrayToString(new int[]{75, 70})/*ID*/, id);
        object.put(IntArrayToString(new int[]{88, 76, 71})/*UID*/, uid);
        object.put(IntArrayToString(new int[]{87, 108, 128, 121, 110, 115, 78, 122, 128, 121, 127})/*LaunchCount*/, launchCount);
        object.put(IntArrayToString(new int[]{79, 105, 117, 109, 85, 119, 108, 123})/*GameMods*/, gameMods.toArray(new Integer[0]));
        object.put(IntArrayToString(new int[]{93, 115, 113, 120, 83, 120, 83, 120, 112, 121})/*SignInInfo*/, signInInfo);
        object.put(IntArrayToString(new int[]{85, 118, 107, 116, 79, 106})/*OpenId*/, openId);
        object.put(IntArrayToString(new int[]{77, 114, 106, 115})/*Info*/, info);
        object.put(IntArrayToString(new int[]{86, 114, 123, 109, 111, 116})/*Plugin*/, plugins.toArray(new Integer[0]));
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
