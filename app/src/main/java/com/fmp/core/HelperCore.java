package com.fmp.core;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fmp.FMP_Tools;
import com.fmp.Logger;
import com.fmp.UploadHelper;
import com.fmp.activity.LaunchActivity;
import com.fmp.activity.LogInActivity;
import com.fmp.core.http.bean.HelperAccount;
import com.fmp.core.http.bean.HelperBanDevice;
import com.fmp.core.http.bean.HelperCardKey;
import com.fmp.core.push.ClientPush;
import com.fmp.core.push.PushSetting;
import com.fmp.data.InternetProtocol;
import com.fmp.data.LauncherSetting;
import com.fmp.data.TencentObject;
import com.fmp.dialog.HelperDialog;
import com.fmp.library.EasyProtectorLib;
import com.fmp.receiver.LoaderReceiver;
import com.fmp.service.HelperService;
import com.fmp.textures.TextureManager;
import com.fmp.util.CheckVirtual;
import com.fmp.util.SpUtil;
import com.fmp.util.SystemUtil;
import com.fmp.util.TimeUtil;
import com.fmp.util.VeDate;
import com.lwkandroid.imagepicker.ImagePicker;
import com.lwkandroid.imagepicker.data.ImageBean;
import com.lwkandroid.imagepicker.data.ImagePickType;
import com.lwkandroid.imagepicker.utils.GlideImagePickerDisplayer;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import net.fmp.helper.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.ValueEventListener;
import dalvik.system.DexFile;
import es.dmoral.toasty.Toasty;
import okhttp3.OkHttpClient;
import per.goweii.anylayer.AnyLayer;
import per.goweii.anylayer.DragLayout;
import per.goweii.anylayer.Layer;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.CLIPBOARD_SERVICE;
import static com.fmp.FMP_Tools.IntArrayToString;

@SuppressLint({"DefaultLocale", "SetTextI18n"})
public class HelperCore {
    public static final String EMPTY_STRING = "";
    private static final int PORTRAIT_REQUEST_CODE = 1000;
    private static HelperCore helperCore;
    private HelperAccount coreHelperAccount;
    private AppCompatActivity mMainActivity = null;
    private LoaderReceiver receiver = new LoaderReceiver();
    //QQ登录
    private Tencent mTencent;
    private BaseUiListener baseUiListener;
    private onTencentLogin tencentLogin;
    private onUpdateTencentOpenId updateTencentOpenId;
    //头像上传
    private onUploadPortrait uploadPortrait;

    private BmobRealTimeData userReal = new BmobRealTimeData();
    private boolean unShowLogin = false;

    HelperCore() {
        helperCore = this;
        LauncherSetting setting = LauncherSetting.getSetting();
        CoreException.setDeBugMode(setting.isDeBugMode());
        try {
            OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
            okHttpClient.proxy(Proxy.NO_PROXY);//设置无代理
            runCheck();
        } catch (CoreException | IOException | PackageManager.NameNotFoundException e) {
            Logger.e(e.getMessage());
            HelperNative.onError();
            Process.killProcess(Process.myPid());
            System.exit(1);
        }
        DeviceInfoUtil.getInstance().getInternetProtocolInfo(new DeviceInfoUtil.onInternetProtocolInfo() {
            @Override
            public void onBack(boolean bool, InternetProtocol internetProtocol) {
                HelperBanDevice device = getBanData();
                if (device != null && device.isBan && device.ip.equals(internetProtocol.getIp()) && checkTime(device.endTime)) {
                    setObjectId(null);
                    HelperAccount userData = getUserData();
                    if (userData != null) {
                        userData.userLevel = -1;
                        saveUserData(userData);
                    }
                    Logger.i(String.format("you are banned %s.", device.endTime));
                }
            }

            private boolean checkTime(String endTime) {
                long networkTime = TimeUtil.getCurTimeMills();
                Date end = VeDate.strToDateLong(endTime);
                return end.getTime() >= networkTime;
            }
        });
        GamePluginManager.getInstance().refreshAllPlugin((e, modList) -> {
            if (e == null) {
                Logger.i(String.format("plugin count %d.", modList.size()));
            } else {
                Logger.e("plugin is null.");
            }
        });
        IntentFilter filter = new IntentFilter();
        filter.addAction("HelperClient");
        HelperNative.getApplication().registerReceiver(receiver, filter);
    }

    public static HelperCore getInstance() {
        if (helperCore == null) {
            helperCore = new HelperCore();
        }
        return helperCore;
    }

    /**
     * 获取外部存储
     *
     * @return 手机内部存储目录
     */
    public static File getHelperDirectory() {
        //安卓Q会在2020年停止这个获取内部存储的方法
        //尽快适配新的获取方法
        return Environment.getExternalStorageDirectory();
    }

    /**
     * 调试模式
     *
     * @return 状态
     */
    private boolean deBugMode() {
        if (new File(getHelperDirectory(), ".fmp").exists()) {
            ClipboardManager cm = (ClipboardManager) HelperNative.getApplication().getSystemService(CLIPBOARD_SERVICE);
            try {
                if (cm != null) {
                    ClipData data = cm.getPrimaryClip();
                    assert data != null;
                    ClipData.Item item = data.getItemAt(0);
                    return HelperNative.devMode(item.getText().toString());
                } else {
                    return false;
                }
            } catch (Throwable e) {
                return false;
            }
        }
        return false;
    }

    public String getObjectId() {
        SharedPreferences sharedPreferences = HelperNative.getApplication().getSharedPreferences("Database", Context.MODE_PRIVATE);
        return sharedPreferences.getString("mObject", EMPTY_STRING);
    }

    private void setObjectId(String objectId) {
        SharedPreferences sharedPreferences = HelperNative.getApplication().getSharedPreferences("Database", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (TextUtils.isEmpty(objectId)) {
            editor.remove("mObject");
        } else {
            editor.putString("mObject", objectId);
        }
        editor.apply();
    }

    private int getExceptionCode() {
        SharedPreferences sharedPreferences = HelperNative.getApplication().getSharedPreferences("Database", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("mCode", -1);
    }

    private void setExceptionCode(int code) {
        SharedPreferences sharedPreferences = HelperNative.getApplication().getSharedPreferences("Database", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (code == -1) {
            editor.remove("mCode");
        } else {
            editor.putInt("mCode", code);
        }
        editor.apply();
    }

    /**
     * 获取用户能力
     *
     * @return 用户等级
     */
    public int getUserAbility() {
        if (deBugMode()) {
            HelperNative.initUser(new HelperAccount(), 100);
            return 100;
        }
        HelperAccount userData = getUserData();
        int code = getExceptionCode();
        if (TextUtils.isEmpty(getObjectId()) || userData == null || code == 0) {
            if (code == 0) {
                HelperNative.initUser(userData, -3);
                return -3;
            } else {
                HelperNative.initUser(userData, -2);
                return -2;
            }
        } else {
            HelperNative.initUser(userData, userData.userLevel);
            return userData.userLevel;
        }
    }

    /**
     * 获取用户数据
     *
     * @return 用户数据
     */
    public HelperAccount getUserData() {
        /*try {
            FileInputStream fileInputStream = new FileInputStream(new File(HelperNative.getApplication().getFilesDir(), "data.db"));
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            return (HelperAccount) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }*/
        try {
            if (coreHelperAccount != null && coreHelperAccount.isLogin)
                return coreHelperAccount;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void saveUserData(HelperAccount userData) {
        /*try {
            File file = new File(HelperNative.getApplication().getFilesDir(), "data.db");
            if (userData != null) {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(userData);
                objectOutputStream.close();
            } else {
                file.delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        if (userData == null) {
            coreHelperAccount.removeData();
            coreHelperAccount = null;
        } else {
            coreHelperAccount = userData;
            coreHelperAccount.isLogin = true;
            try {
                coreHelperAccount.saveData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Nullable
    private HelperBanDevice getBanData() {
        /*try {
            FileInputStream fileInputStream = new FileInputStream(new File(HelperNative.getApplication().getFilesDir(), "bd.db"));
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            return (BAN_DEVICE) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }*/
        try {
            return new HelperBanDevice().readData();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveBanData(HelperBanDevice banDevice) {
        /*try {
            File file = new File(HelperNative.getApplication().getFilesDir(), "bd.db");
            if (banDevice != null) {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(banDevice);
                objectOutputStream.close();
            } else {
                file.delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        try {
            if (banDevice == null) {
                new HelperBanDevice().removeData();
            } else {
                banDevice.saveData();
            }
        } catch (Exception e) {
            if (CoreException.deBugMode) {
                Logger.toString(e);
            }
        }
    }

    void subRowUpdate(String objectId) {
        if (userReal != null && !TextUtils.isEmpty(objectId))
            userReal.start(new ValueEventListener() {
                @Override
                public void onDataChange(JSONObject data) {
                    try {
                        HelperAccount userData = new HelperAccount(new HelperJSONObject(data.toString()));
                        if (userData != null && getUserData() != null) {
                            coreHelperAccount = null;
                            coreHelperAccount = userData;
                            saveUserData(userData);
                            if (mMainActivity != null)
                                mMainActivity.runOnUiThread(() -> checkNewMessage(mMainActivity));
                        } else {
                            throw new CoreException("connected error");
                        }
                    } catch (CoreException | Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConnectCompleted(Exception e) {
                    if (e == null) {
                        Logger.i("isConnected");
                        if (userReal.isConnected()) {
                            userReal.subRowUpdate(IntArrayToString(new int[]{83, 90, 93, 108, 98, 96, 82, 95, 108, 81, 78, 97, 78}), objectId);
                        }
                    } else {
                        Logger.i("not Connected " + e.getMessage());
                    }
                }
            });

    }

    private void unSubRowUpdate(String objectId) {
        if (userReal != null && !TextUtils.isEmpty(objectId))
            userReal.unsubRowUpdate(IntArrayToString(new int[]{83, 90, 93, 108, 98, 96, 82, 95, 108, 81, 78, 97, 78}), objectId);
    }

    private void checkNewMessage(Activity activity) {
        HelperAccount userData = getUserData();
        if (userData != null) {
            if (!TextUtils.isEmpty(userData.uid) && !DeviceIdUtil.getDeviceId().equals(userData.uid) && !unShowLogin) {
                logout(activity);
                AnyLayer.dialog(anyLayer -> anyLayer.contentView(R.layout.dialog_normal_unlogin)
                        .backgroundDimDefault()
                        .onVisibleChangeListener(new Layer.OnVisibleChangeListener() {
                            @Override
                            public void onShow(Layer layer) {
                                ((TextView) layer.getView(R.id.tv_dialog_title)).setText("下线通知");
                                ((TextView) layer.getView(R.id.tv_dialog_content)).setText(String.format("您的账号于%s在其它设备登录，本设备已下线", userData.updatedAt));
                                ((TextView) layer.getView(R.id.tv_dialog_no)).setText("重新登录");
                                ((TextView) layer.getView(R.id.tv_dialog_yes)).setText("知道了");
                                Toasty.normal(activity, String.format("您的账号于%s在其它设备登录，本设备已下线", userData.updatedAt)).show();
                                unShowLogin = true;
                            }

                            @Override
                            public void onDismiss(Layer layer) {
                                unShowLogin = false;
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes)
                        .onClick((layer, v) -> {
                            Intent intent = new Intent();
                            intent.setClass(activity, LogInActivity.class);
                            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivityForResult(intent, 100);
                            layer.dismiss();
                        }, R.id.tv_dialog_no)
                        .show());
            }

        }

    }

    public void checkUserVerify(onUserVerify userVerify) {
        if (TextUtils.isEmpty(getObjectId())) {
            userVerify.done(null, null);
            return;
        }
        BmobQuery query = new BmobQuery(IntArrayToString(new int[]{83, 90, 93, 108, 98, 96, 82, 95, 108, 81, 78, 97, 78}));//FMP_USER_DATA
        query.addWhereEqualTo("objectId", getObjectId());
        query.findObjectsByTable(new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray jsonArray, BmobException e) {
                if (e == null) {
                    try {
                        if (jsonArray.length() == 0) {
                            setExceptionCode(0);
                            userVerify.done(new CoreException("登录状态失效，请重新登录"), null);
                            return;
                        }
                        if (jsonArray.length() >= 2) {
                            setExceptionCode(0);
                            userVerify.done(new CoreException("登录状态失效，您的账号数据异常，请联系管理员解决"), null);
                            return;
                        }
                        HelperAccount userData = new HelperAccount(new HelperJSONObject(jsonArray.getJSONObject(0).toString()));
                        if (userData.userLevel <= -2) {
                            setExceptionCode(0);
                            saveUserData(userData);
                            userVerify.done(new CoreException("登录状态失效，此账号被停止使用"), null);
                        } else {
                            setExceptionCode(-1);
                            saveUserData(userData);
                            userVerify.done(null, userData);
                        }
                    } catch (JSONException ex) {
                        setExceptionCode(0);
                        userVerify.done(new CoreException(ex.getMessage()), null);
                    }
                } else {
                    if (e.getErrorCode() == 10210) {
                        setExceptionCode(1);
                    } else {
                        setExceptionCode(0);
                    }
                    userVerify.done(new CoreException(e), null);
                }
            }
        });
    }

    private void checkBanDevice(onBanDevice banDevice) {
        String UID = com.fmp.core.DeviceIdUtil.getDeviceId();
        String IMEI = DeviceIdUtil.getIMEI();
        String MAC = DeviceIdUtil.getMacAddress(HelperNative.getApplication());
        String SystemModel = SystemUtil.getSystemModel();
        String DeviceBrand = SystemUtil.getDeviceBrand();
        String IP = EMPTY_STRING;
        if (DeviceInfoUtil.getInstance().getInternetProtocol() != null) {
            IP = DeviceInfoUtil.getInstance().getInternetProtocol().getIp();
        }

        List<BmobQuery> queries = new ArrayList<>();
        if (!TextUtils.isEmpty(UID)) {
            BmobQuery bmobQuery = new BmobQuery();
            bmobQuery.addWhereEqualTo(FMP_Tools.IntArrayToString(new int[]{120, 108, 103})/*uid*/, UID);//UID
            queries.add(bmobQuery);
        }
        if (!TextUtils.isEmpty(IMEI)) {
            BmobQuery bmobQuery = new BmobQuery();
            bmobQuery.addWhereEqualTo(FMP_Tools.IntArrayToString(new int[]{109, 113, 105, 109})/*imei*/, IMEI);//IMEI
            queries.add(bmobQuery);
        }
        if (!TextUtils.isEmpty(MAC)) {
            BmobQuery bmobQuery = new BmobQuery();
            bmobQuery.addWhereEqualTo(FMP_Tools.IntArrayToString(new int[]{112, 100, 102})/*mac*/, MAC);//Mac
            queries.add(bmobQuery);
        }
        {
            BmobQuery bmobQuery = new BmobQuery();
            bmobQuery.addWhereEqualTo(FMP_Tools.IntArrayToString(new int[]{111, 112, 129, 116, 110, 112, 77, 125, 108, 121, 111})/*deviceBrand*/, DeviceBrand);//DeviceBrand
            bmobQuery.addWhereEqualTo(FMP_Tools.IntArrayToString(new int[]{126, 132, 126, 127, 112, 120, 88, 122, 111, 112, 119})/*systemModel*/, SystemModel);//SystemModel
            queries.add(bmobQuery);
        }
        if (!TextUtils.isEmpty(IP)) {
            BmobQuery bmobQuery = new BmobQuery();
            bmobQuery.addWhereEqualTo(FMP_Tools.IntArrayToString(new int[]{107, 114})/*ip*/, IP);//IP
            queries.add(bmobQuery);
        }

        BmobQuery query = new BmobQuery(HelperBanDevice.CLASS_TABLE_NAME);
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);// 先从网络读取数据，如果没有，再从缓存中获取。
        query.or(queries);
        query.findObjectsByTable(new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray jsonArray, BmobException e) {
                if (e == null) {
                    Logger.d(jsonArray.toString());
                    switch (jsonArray.length()) {
                        case 0:
                            saveBanData(null);
                            banDevice.onFind(false, null, null);
                            break;
                        case 1: {
                            try {
                                HelperBanDevice device = new HelperBanDevice(new HelperJSONObject(jsonArray.getJSONObject(0).toString()));
                                if (device.isBan && checkTime(device.endTime)) {
                                    setObjectId(null);
                                    HelperAccount userData = getUserData();
                                    if (userData != null) {
                                        userData.userLevel = -2;
                                        saveUserData(userData);
                                    }
                                    saveBanData(device);
                                    banDevice.onFind(true, device.reason, device.endTime);
                                } else {
                                    saveBanData(null);
                                }
                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }

                            break;
                        }
                        default: {
                            for (int i = 0; jsonArray.length() > i; i++) {
                                try {
                                    HelperBanDevice device = new HelperBanDevice(new HelperJSONObject(jsonArray.getJSONObject(i).toString()));
                                    if (device.isBan && checkTime(device.endTime)) {
                                        setObjectId(null);
                                        HelperAccount userData = getUserData();
                                        if (userData != null) {
                                            userData.userLevel = -2;
                                            saveUserData(userData);
                                        }
                                        saveBanData(device);
                                        banDevice.onFind(true, device.reason, device.endTime);
                                        break;
                                    } else {
                                        saveBanData(null);
                                    }
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }
                            }
                            break;
                        }
                    }
                } else {
                    HelperBanDevice device = getBanData();
                    if (device != null && device.isBan) {
                        setObjectId(null);
                        banDevice.onFind(true, device.reason, device.endTime);
                    } else {
                        banDevice.onFind(false, null, null);
                    }
                }
            }

            private boolean checkTime(String endTime) {
                long networkTime = TimeUtil.getCurTimeMills();
                Date end = VeDate.strToDateLong(endTime);
                return end.getTime() >= networkTime;
            }
        });
    }

    public void login(String account, onLogin login) {
        try {
            HelperCore.getInstance().runCheck();
        } catch (Throwable e) {
            HelperNative.onError();
        }
        PushSetting setting = PushSetting.getSetting();
        if (setting.isLogin()) {
            login.done(new CoreException(IntArrayToString(new int[]{24410, 21076, 30338, 24412, 19988, 21494, 29999})), null);//当前登录不可用
            return;
        }
        BmobQuery queryAccount = new BmobQuery(IntArrayToString(new int[]{83, 90, 93, 108, 98, 96, 82, 95, 108, 81, 78, 97, 78}));//FMP_USER_DATA
        queryAccount.addWhereEqualTo(FMP_Tools.IntArrayToString(new int[]{72, 106, 106, 118, 124, 117, 123})/*Account*/, account);
        queryAccount.findObjectsByTable(new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray jsonArray, BmobException e) {
                if (e == null) {
                    try {
                        if (jsonArray.length() == 0) {
                            login.done(new CoreException("您输入的账号有误，请重新输入"), null);
                            return;
                        }
                        if (jsonArray.length() >= 2) {
                            login.done(new CoreException("登录发生错误，您的账号数据异常，请联系管理员解决"), null);
                            return;
                        }
                        HelperAccount userData = new HelperAccount(new HelperJSONObject(jsonArray.getJSONObject(0).toString()));
                        if (!TextUtils.isEmpty(userData.uid) && userData.uid.equals(DeviceIdUtil.getDeviceId())) {
                            setExceptionCode(-1);
                            setObjectId(userData.objectId);
                            saveUserData(userData);
                            subRowUpdate(userData.objectId);
                            login.done(null, userData);
                        } else {
                            login.done(new CoreException("密码错误，请重新核对"), null);
                        }
                    } catch (JSONException ex) {
                        login.done(new CoreException(ex.getMessage()), null);
                    }
                } else {
                    if (e.getErrorCode() == 10210) {
                        setExceptionCode(1);
                    } else {
                        setExceptionCode(0);
                    }
                    login.done(new CoreException(e), null);
                }
            }
        });
    }

    public void login(Activity activity, onTencentLogin login) {
        try {
            HelperCore.getInstance().runCheck();
        } catch (Throwable e) {
            HelperNative.onError();
        }
        PushSetting setting = PushSetting.getSetting();
        if (setting.isTencent()) {
            login.done(new CoreException(IntArrayToString(new int[]{24410, 21076, 30338, 24412, 19988, 21494, 29999})), null);//当前登录不可用
            return;
        }
        updateTencentOpenId = null;
        //初始化腾讯SDK
        if (mTencent == null) {
            mTencent = Tencent.createInstance(IntArrayToString(new int[]{58, 57, 58, 64, 60, 57, 57, 60, 60}), activity);//101730033
        }
        //判断登录情况
        if (mTencent.isSessionValid()) {
            mTencent.logout(activity);//退出登录
        }
        //开始登录
        tencentLogin = login;
        baseUiListener = new BaseUiListener(activity);
        mTencent.login(activity, "all", baseUiListener, true);
    }

    public void register(String account, onRegister register) {
        try {
            HelperCore.getInstance().runCheck();
        } catch (Throwable e) {
            HelperNative.onError();
        }
        BmobQuery query = new BmobQuery(IntArrayToString(new int[]{83, 90, 93, 108, 98, 96, 82, 95, 108, 81, 78, 97, 78}));//FMP_USER_DATA
        query.addWhereEqualTo(FMP_Tools.IntArrayToString(new int[]{72, 106, 106, 118, 124, 117, 123})/*Account*/, account);
        query.findObjectsByTable(new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray jsonArray, BmobException e) {
                if (e == null) {
                    if (jsonArray.length() == 0) {
                        HelperAccount helperAccount = new HelperAccount();
                        helperAccount.account = account;
                        helperAccount.userName = "FMP用户";
                        helperAccount.userType = "普通用户";
                        helperAccount.uid = DeviceIdUtil.getDeviceId();
                        helperAccount.versionInfo = DeviceInfoUtil.getInstance().getVersionInfo();
                        helperAccount.deviceInfo = DeviceInfoUtil.getInstance().getDeviceInfo();
                        helperAccount.userLevel = -1;
                        helperAccount.userSex = 1;
                        helperAccount.launchCount = 0;
                        helperAccount.createUser(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
                                    setObjectId(s);
                                    saveUserData(helperAccount);
                                    register.done(null, helperAccount);
                                } else {
                                    register.done(new CoreException(e), null);
                                }
                            }
                        });


                    } else {
                        register.done(new CoreException("此账号已被注册！"), null);
                    }
                } else {
                    register.done(new CoreException(e), null);
                }
            }
        });
    }

    public boolean logout(Activity activity) {
        try {
            saveUserData(null);
            SharedPreferences preferences = HelperNative.getApplication().getSharedPreferences("Database", Context.MODE_PRIVATE);
            unSubRowUpdate(preferences.getString("mObject", EMPTY_STRING));
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("mObject");
            editor.apply();
            SpUtil.remove(Constants.PARAM_ACCESS_TOKEN);
            SpUtil.remove(Constants.PARAM_EXPIRES_IN);
            SpUtil.remove(Constants.PARAM_OPEN_ID);
            //判断登录情况
            if (mTencent != null && mTencent.isSessionValid()) {
                mTencent.logout(activity);//退出登录
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void joinQQGroup(Activity activity, String key) {
        //初始化腾讯SDK
        if (mTencent == null) {
            mTencent = Tencent.createInstance(IntArrayToString(new int[]{58, 57, 58, 64, 60, 57, 57, 60, 60}), activity);//101730033
        }
        mTencent.joinQQGroup(activity, key);
    }

    public void uploadPortrait(@NotNull Activity activity, onUploadPortrait portrait) {
        try {
            HelperCore.getInstance().runCheck();
        } catch (Throwable e) {
            HelperNative.onError();
        }
        uploadPortrait = portrait;
        //发起图片选择
        new ImagePicker()
                .pickType(ImagePickType.SINGLE) //设置选取类型(拍照ONLY_CAMERA、单选SINGLE、多选MUTIL)
                .maxNum(1) //设置最大选择数量(此选项只对多选生效，拍照和单选都是1，修改后也无效)
                .needCamera(true) //是否需要在界面中显示相机入口(类似微信那样)
                .cachePath(activity.getCacheDir().getAbsolutePath()) //自定义缓存路径(拍照和裁剪都需要用到缓存)
                .displayer(new GlideImagePickerDisplayer()) //自定义图片加载器，默认是Glide实现的,可自定义图片加载器
                .start(activity, PORTRAIT_REQUEST_CODE); //自定义RequestCode
    }

    private void uploadImageFile(final String imageUrl) {
        try {
            HelperCore.getInstance().runCheck();
        } catch (Throwable e) {
            HelperNative.onError();
        }
        BmobObject object = new BmobObject(IntArrayToString(new int[]{83, 90, 93, 108, 98, 96, 82, 95, 108, 81, 78, 97, 78}));//FMP_USER_DATA
        object.setValue("HeadUrl", imageUrl);
        object.update(getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    HelperAccount userData = getUserData();
                    userData.headUrl = imageUrl;
                    saveUserData(userData);
                    uploadPortrait.onUpload(null, imageUrl);
                } else {
                    uploadPortrait.onUpload(new CoreException(e), null);
                }
            }
        });
    }

    public void updateUserData(Activity activity) {
        try {
            HelperCore.getInstance().runCheck();
        } catch (Throwable e) {
            HelperNative.onError();
        }
        try {
            //初始化腾讯SDK
            if (mTencent == null) {
                mTencent = Tencent.createInstance("101730033", activity);
            }
            String token = (String) SpUtil.get(Constants.PARAM_ACCESS_TOKEN, EMPTY_STRING);
            String expires = (String) SpUtil.get(Constants.PARAM_EXPIRES_IN, EMPTY_STRING);
            String openId = (String) SpUtil.get(Constants.PARAM_OPEN_ID, EMPTY_STRING);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires) && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
                if (mTencent.isSessionValid()) {
                    UserInfo mInfo = new UserInfo(activity, mTencent.getQQToken());
                    mInfo.getUserInfo(new IUiListener() {

                        @Override
                        public void onError(UiError e) {
                            Logger.w(e.errorMessage);
                        }

                        @Override
                        public void onComplete(Object object) {
                            TencentObject tencentObject = new TencentObject((JSONObject) object);
                            tencentObject.saveData();
                        }

                        @Override
                        public void onCancel() {
                        }
                    });
                }
            }
        } catch (Throwable e) {
            if (CoreException.deBugMode) {
                Logger.toString(e);
            }
        }
        HelperAccount userData = getUserData();
        if (!TextUtils.isEmpty(getObjectId()) && userData != null) {
            BmobObject object = new BmobObject(IntArrayToString(new int[]{83, 90, 93, 108, 98, 96, 82, 95, 108, 81, 78, 97, 78}));//FMP_USER_DATA
            object.setValue(FMP_Tools.IntArrayToString(new int[]{97, 112, 125, 126, 116, 122, 121, 84, 121, 113, 122})/*VersionInfo*/, DeviceInfoUtil.getInstance().getVersionInfo());
            object.setValue(FMP_Tools.IntArrayToString(new int[]{89, 113, 120, 119, 110, 82, 119, 111, 120})/*PhoneInfo*/, DeviceInfoUtil.getInstance().getDeviceInfo());
            if (!TextUtils.isEmpty(userData.objectId)) {
                //修复26X版本移除的设备标识
                object.setValue(FMP_Tools.IntArrayToString(new int[]{88, 76, 71})/*UID*/, DeviceIdUtil.getDeviceId());
            }
            object.increment("LaunchCount");
            object.update(getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Logger.i("update user info.");
                    } else {
                        Logger.e(e.getMessage());
                    }
                }
            });
        }
    }

    public void updateNickNme(String nickName, onUpdateNickName listener) {
        try {
            HelperCore.getInstance().runCheck();
        } catch (Throwable e) {
            HelperNative.onError();
        }
        if (!TextUtils.isEmpty(getObjectId())) {
            BmobObject object = new BmobObject(IntArrayToString(new int[]{83, 90, 93, 108, 98, 96, 82, 95, 108, 81, 78, 97, 78}));//FMP_USER_DATA
            object.setValue(FMP_Tools.IntArrayToString(new int[]{93, 123, 109, 122, 86, 105, 117, 109})/*UserName*/, nickName);
            object.update(getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        HelperAccount userData = getUserData();
                        userData.userName = nickName;
                        saveUserData(userData);
                        listener.onUpdate(null, nickName);
                    } else {
                        listener.onUpdate(new CoreException(e), null);
                    }
                }
            });
        } else {
            listener.onUpdate(new CoreException("用户登录失效"), null);
        }
    }

    public void updateSex(int sex, onUpdateSex listener) {
        try {
            HelperCore.getInstance().runCheck();
        } catch (Throwable e) {
            HelperNative.onError();
        }
        if (!TextUtils.isEmpty(getObjectId())) {
            BmobObject object = new BmobObject(IntArrayToString(new int[]{83, 90, 93, 108, 98, 96, 82, 95, 108, 81, 78, 97, 78}));//FMP_USER_DATA
            object.setValue("UserSex", sex);
            object.update(getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        HelperAccount userData = getUserData();
                        userData.userSex = sex;
                        saveUserData(userData);
                        listener.onUpdate(null, sex, FMP_Tools.getSexString(sex));
                    } else {
                        listener.onUpdate(new CoreException(e), 1, null);
                    }
                }
            });
        } else {
            listener.onUpdate(new CoreException("用户登录失效"), 1, null);
        }
    }

    public void updateTencentOpenId(Activity activity, onUpdateTencentOpenId listener) {
        try {
            HelperCore.getInstance().runCheck();
        } catch (Throwable e) {
            HelperNative.onError();
        }
        tencentLogin = null;
        //初始化腾讯SDK
        if (mTencent == null) {
            mTencent = Tencent.createInstance(IntArrayToString(new int[]{58, 57, 58, 64, 60, 57, 57, 60, 60}), activity);//101730033
        }
        //判断登录情况
        if (mTencent.isSessionValid()) {
            mTencent.logout(activity);//退出登录
        }
        //开始登录
        updateTencentOpenId = listener;
        baseUiListener = new BaseUiListener(activity);
        mTencent.login(activity, "all", baseUiListener, true);
    }

    public void findCDK(String cdk, onFindCDK listener) {
        try {
            HelperCore.getInstance().runCheck();
        } catch (Throwable e) {
            HelperNative.onError();
        }
        BmobQuery query = new BmobQuery(HelperCardKey.CLASS_TABLE_NAME);
        query.addWhereEqualTo("key", cdk);
        query.findObjectsByTable(new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray jsonArray, BmobException e) {
                if (e == null) {
                    switch (jsonArray.length()) {
                        case 0:
                            listener.onFind(new CoreException("CDK不存在"), null);
                            break;
                        case 1: {
                            try {
                                HelperCardKey cardKey = new HelperCardKey(new HelperJSONObject(jsonArray.getJSONObject(0).toString()));
                                if (cardKey.enabled) {
                                    if (!TextUtils.isEmpty(cardKey.data)) {
                                        HelperAccount userData = getUserData();
                                        if (userData != null) {
                                            try {
                                                userData.updateLocalData(new HelperJSONObject(cardKey.data));
                                            } catch (JSONException ex) {
                                                listener.onFind(new CoreException("数据异常"), null);
                                                return;
                                            }
                                            cardKey.updateEnabled(new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {
                                                    if (e != null) {
                                                        listener.onFind(new CoreException(e), null);
                                                    }
                                                }
                                            }, false);
                                            userData.updateUser(new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {
                                                    if (e == null) {
                                                        saveUserData(userData);
                                                        listener.onFind(null, userData);
                                                    } else {
                                                        listener.onFind(new CoreException(e), null);
                                                    }
                                                }
                                            });
                                        } else {
                                            listener.onFind(new CoreException("用户登录失效"), null);
                                        }
                                    } else {
                                        listener.onFind(new CoreException("CDK无法使用"), null);
                                    }
                                } else {
                                    listener.onFind(new CoreException("CDK已经失效"), null);
                                }
                            } catch (JSONException ex) {
                                ex.printStackTrace();
                                listener.onFind(new CoreException(ex.getMessage()), null);
                            }
                            break;
                        }
                        default:
                            listener.onFind(new CoreException("CDK无法使用"), null);
                            break;
                    }
                } else {
                    listener.onFind(new CoreException(e), null);
                }
            }
        });
    }

    public void showBanDeviceDialog(Activity activity) {
        checkBanDevice((isBan, banMessage, endTime) -> {
            if (isBan) {
                AnyLayer.dialog(anyLayer -> anyLayer.contentView(R.layout.dialog_normal_ban_msg)
                        .backgroundDimDefault()
                        .onVisibleChangeListener(new Layer.OnVisibleChangeListener() {
                            @Override
                            public void onShow(Layer layer) {
                                ((TextView) layer.getView(R.id.tv_dialog_title)).setText("您的账号被停止使用");
                                ((TextView) layer.getView(R.id.tv_dialog_content)).setText(String.format("%s\n\n解除时间：%s", banMessage, endTime));
                                ((TextView) layer.getView(R.id.tv_dialog_no)).setText("申请解除");
                                ((TextView) layer.getView(R.id.tv_dialog_yes)).setText("知道了");
                            }

                            @Override
                            public void onDismiss(Layer layer) {

                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .onClick((layer, v) -> AnyLayer.dialog(activity)
                                .avoidStatusBar(true)
                                .contentView(R.layout.dialog_notificationl)
                                .gravity(Gravity.TOP)
                                .outsideInterceptTouchEvent(false)
                                .dragDismiss(DragLayout.DragStyle.Top)
                                .onVisibleChangeListener(new Layer.OnVisibleChangeListener() {
                                    @Override
                                    public void onShow(Layer layer) {
                                        ((TextView) layer.getView(R.id.tv_dialog_content)).setText("发送邮件到helper@gaozaici.cn申请解除");
                                    }

                                    @Override
                                    public void onDismiss(Layer layer) {

                                    }
                                })
                                .onClick((layer1, view) -> {
                                    Intent data = new Intent(Intent.ACTION_SENDTO);
                                    data.setData(Uri.parse("mailto:helper@gaozaici.cn"));
                                    data.putExtra(Intent.EXTRA_SUBJECT, "申请解除账号停用");
                                    data.putExtra(Intent.EXTRA_TEXT, String.format("我的设备标识：%s\n", DeviceIdUtil.getDeviceId()));
                                    activity.startActivity(data);
                                }, R.id.tv_dialog_content)
                                .show(), R.id.tv_dialog_no)
                        .show());
            }
        });
    }

    public void showAgreementDialog(Activity activity, boolean bool) throws IOException {
        boolean look = (boolean) SpUtil.get("lookAgreement", false);
        if (!look || bool) {
            InputStream inputStream = activity.getAssets().open("用户使用协议.txt");
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            HelperDialog dialog = new HelperDialog(activity);
            dialog.setTitle(activity.getString(R.string.helper_user_protocol_title));
            dialog.setMessage(new String(buffer, StandardCharsets.UTF_8));
            dialog.setButton2(activity.getString(R.string.helper_user_protocol_no), v -> {
                activity.finish();
                Process.killProcess(Process.myPid());
                System.exit(0);
            });
            dialog.setButton3(activity.getString(R.string.helper_user_protocol_yes), v -> {
                SpUtil.put("lookAgreement", true);
                dialog.dismiss();
            });
            dialog.setCancelable(false);
            dialog.show();
        }
    }

    public AppCompatActivity getMainActivity() {
        return mMainActivity;
    }

    public void initMainActivity(AppCompatActivity activity) {
        this.mMainActivity = activity;
        HelperNative.initNative(activity);
        try {
            showAgreementDialog(activity, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HelperSetting setting = HelperSetting.getInstance().getSetting();
        if (setting.isNotification()) {
            activity.startService(new Intent(activity, HelperService.class));
        }
        showBanDeviceDialog(activity);
        updateUserData(activity);//更新用户信息
        ClientPush.getInstance().checkAllPush(activity, ClientPush.TYPE_ALL, false);//检查推送
        TextureManager.getInstance().onRefresh();
        SpUtil.put(LaunchActivity.START_EXCEPTION, false);//清除异常
    }

    public void onActivityStart(AppCompatActivity activity) {
        this.mMainActivity = activity;
        try {
            runCheck();
        } catch (CoreException | IOException | PackageManager.NameNotFoundException e) {
            Logger.e(e.getMessage());
            HelperNative.onError();
            Process.killProcess(Process.myPid());
            System.exit(0);
        }
        checkNewMessage(activity);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN || requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode, resultCode, data, baseUiListener);
        }
        if (requestCode == PORTRAIT_REQUEST_CODE && resultCode == RESULT_OK && null != data) {
            //获取选择的图片数据
            List<ImageBean> resultList = data.getParcelableArrayListExtra(ImagePicker.INTENT_RESULT_DATA);
            if (resultList != null) {
                try {
                    File file = new File(resultList.get(0).getImagePath());
                    if (file.length() < 1024 * 1024 * 4) {
                        uploadImageFile(UploadHelper.uploadPortrait(file.getAbsolutePath()));
                    } else {
                        uploadPortrait.onUpload(new CoreException("图片大小超过4MB"), null);
                    }
                } catch (Exception e) {
                    uploadPortrait.onUpload(new CoreException("上传图片失败"), null);
                }
            } else {
                uploadPortrait.onUpload(new CoreException("图片选择失败"), null);
            }
        }
    }

    public void onActivityDestroy(@NotNull Activity activity) {
        this.mMainActivity = null;
        unSubRowUpdate(getObjectId());
        //activity.unregisterReceiver(receiver);
        activity.stopService(new Intent(activity, HelperService.class));
        /*Handler handler = new Handler();
        handler.postDelayed(() -> {
            Process.killProcess(Process.myPid());
            System.exit(0);
        }, 1000);*/
    }

    public void runCheck() throws CoreException, PackageManager.NameNotFoundException, IOException {
        Application context = HelperNative.getApplication();

        DexFile dexFile = new DexFile(context.getPackageResourcePath());
        Enumeration<String> entries = dexFile.entries();
        while (entries != null && entries.hasMoreElements()) {
            String str = entries.nextElement();
            if (str.startsWith("me.weishu.exposed.ExposedBridge") || str.startsWith("me.weishu.exposed.ExposedApplication")) {
                throw new CoreException("load app error.");
            }
        }
        //检测应用是否可以备份
        //检查应用是否属于debug模式
        //检查应用是否处于调试状态
        if ((0 != (context.getApplicationInfo().flags &= context.getApplicationInfo().FLAG_ALLOW_BACKUP) || 0 != (context.getApplicationInfo().flags &= context.getApplicationInfo().FLAG_DEBUGGABLE) || android.os.Debug.isDebuggerConnected())) {
            throw new CoreException("app exception.");
        }

        String proxyAddress;
        int proxyPort;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            proxyAddress = System.getProperty("http.proxyHost");
            String portStr = System.getProperty("http.proxyPort");
            proxyPort = Integer.parseInt((portStr != null ? portStr : "-1"));
        } else {
            proxyAddress = android.net.Proxy.getHost(context);
            proxyPort = android.net.Proxy.getPort(context);
        }
        if (!TextUtils.isEmpty(proxyAddress) && (proxyPort != -1)) {
            System.clearProperty("http.proxyHost");
            System.clearProperty("http.proxyPort");
            System.clearProperty("https.proxyHost");
            System.clearProperty("https.proxyPort");
            throw new CoreException("find proxy.");
        }

        PushSetting setting = PushSetting.getSetting();
        if (setting.isXposed() && EasyProtectorLib.checkIsXposedExist()) {
            throw new CoreException("find xposed.");
        }
        if (setting.isVirtual() && CheckVirtual.checkPkg(context) && CheckVirtual.isRunInVirtual()) {
            throw new CoreException("find virtual.");
        }

        BufferedReader reader = new BufferedReader(new FileReader(String.format("/proc/%d/maps", Process.myPid())));
        String tempString;
        // 一次读入一行，直到读入null为文件结束
        while ((tempString = reader.readLine()) != null) {
            if (tempString.endsWith(".so")) {
                int index = tempString.indexOf("/");
                if (index != -1) {
                    String str = tempString.substring(index);
                    // 所有so库（包括系统的，即包含/system/目录下的）
                    if (str.endsWith(IntArrayToString(new int[]{121, 118, 111, 115, 122, 125, 58, 119, 123, 118, 59, 128, 124}))) {
                        reader.close();
                        return;
                    }
                }
            }
        }
        reader.close();
        throw new CoreException("load library error.");
    }

    public interface onUserVerify {
        void done(CoreException e, HelperAccount userData);
    }

    public interface onLogin {
        void done(CoreException e, HelperAccount userData);
    }

    public interface onTencentLogin {
        void done(CoreException e, HelperAccount userData);
    }

    public interface onRegister {
        void done(CoreException e, HelperAccount userData);
    }

    public interface onUploadPortrait {
        void onUpload(CoreException e, String imageUrl);
    }

    public interface onUpdateNickName {
        void onUpdate(CoreException e, String nickName);
    }

    public interface onUpdateSex {
        void onUpdate(CoreException e, int sex, String sexName);
    }

    public interface onUpdateTencentOpenId {
        void onUpdate(CoreException e, String token, String expires, String openId);
    }

    public interface onFindCDK {
        void onFind(CoreException e, HelperAccount userData);
    }

    public interface onBanDevice {
        void onFind(boolean isBan, String banMessage, String endTime);
    }

    private class BaseUiListener implements IUiListener {
        private Activity activity;

        BaseUiListener(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onComplete(Object response) {
            if (null == response || ((JSONObject) response).length() == 0) {
                tencentLogin.done(new CoreException("返回数据为空！"), null);
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            try {
                final String token = jsonResponse.getString(Constants.PARAM_ACCESS_TOKEN);
                final String expires = jsonResponse.getString(Constants.PARAM_EXPIRES_IN);
                final String openId = jsonResponse.getString(Constants.PARAM_OPEN_ID);
                SpUtil.put(Constants.PARAM_ACCESS_TOKEN, token);
                SpUtil.put(Constants.PARAM_EXPIRES_IN, expires);
                SpUtil.put(Constants.PARAM_OPEN_ID, openId);
                if (updateTencentOpenId != null) {
                    findOpenId(token, expires, openId);
                }
                if (tencentLogin != null) {
                    if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires) && !TextUtils.isEmpty(openId)) {
                        mTencent.setAccessToken(token, expires);
                        mTencent.setOpenId(openId);
                    }
                    if (mTencent != null && mTencent.isSessionValid()) {
                        UserInfo mInfo = new UserInfo(activity, mTencent.getQQToken());
                        mInfo.getUserInfo(new BaseUserInfo(activity, openId));
                    } else {
                        tencentLogin.done(new CoreException("登录失败，请重新登录"), null);
                    }
                }
            } catch (Exception e) {
                tencentLogin.done(new CoreException(e.getMessage()), null);
            }
        }

        @Override
        public void onError(UiError e) {
            if (updateTencentOpenId != null)
                updateTencentOpenId.onUpdate(new CoreException(e.errorMessage), null, null, null);
            if (tencentLogin != null)
                tencentLogin.done(new CoreException(e.errorMessage), null);
        }

        @Override
        public void onCancel() {
            if (updateTencentOpenId != null)
                updateTencentOpenId.onUpdate(new CoreException("已取消登录"), null, null, null);
            if (tencentLogin != null)
                tencentLogin.done(new CoreException("已取消登录"), null);
        }

        private void findOpenId(String token, String expires, String openId) {
            /*BmobQuery<FMP_USER_DATA> query = new BmobQuery<>(IntArrayToString(new int[]{83, 90, 93, 108, 98, 96, 82, 95, 108, 81, 78, 97, 78}));//FMP_USER_DATA
            query.addWhereEqualTo(FMP_Tools.IntArrayToString(new int[]{85, 118, 107, 116, 79, 106})OpenId, openId);
            query.count(FMP_USER_DATA.class, new CountListener() {
                @Override
                public void done(Integer count, BmobException e) {
                    if (e == null) {
                        if (count == 0) {
                            updateOpenId(token, expires, openId);
                        } else {
                            updateTencentOpenId.onUpdate(new CoreException("QQ已被其它账号绑定"), null, null, null);
                        }
                    } else {
                        updateTencentOpenId.onUpdate(new CoreException(e), null, null, null);
                    }
                }
            });*/
            BmobQuery query = new BmobQuery(IntArrayToString(new int[]{83, 90, 93, 108, 98, 96, 82, 95, 108, 81, 78, 97, 78}));//FMP_USER_DATA
            query.addWhereEqualTo(FMP_Tools.IntArrayToString(new int[]{85, 118, 107, 116, 79, 106})/*OpenId*/, openId);
            query.findObjectsByTable(new QueryListener<JSONArray>() {
                @Override
                public void done(JSONArray jsonArray, BmobException e) {
                    if (e == null) {
                        if (jsonArray.length() == 0) {
                            updateOpenId(token, expires, openId);
                        } else {
                            updateTencentOpenId.onUpdate(new CoreException("QQ已被其它账号绑定"), null, null, null);
                        }
                    } else {
                        updateTencentOpenId.onUpdate(new CoreException(e), null, null, null);
                    }
                }
            });
        }

        private void updateOpenId(String token, String expires, String openId) {
            if (!TextUtils.isEmpty(getObjectId())) {
                BmobObject object = new BmobObject(IntArrayToString(new int[]{83, 90, 93, 108, 98, 96, 82, 95, 108, 81, 78, 97, 78}));//FMP_USER_DATA
                object.setValue(FMP_Tools.IntArrayToString(new int[]{85, 118, 107, 116, 79, 106})/*OpenId*/, openId);
                object.setValue(FMP_Tools.IntArrayToString(new int[]{94, 124, 110, 123, 85, 110, 127, 110, 117})/*UserLevel*/, 0);
                object.update(getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            HelperAccount userData = getUserData();
                            userData.userLevel = 0;
                            userData.openId = openId;
                            saveUserData(userData);
                            updateTencentOpenId.onUpdate(null, token, expires, openId);
                        } else {
                            updateTencentOpenId.onUpdate(new CoreException(e), null, null, null);
                        }
                    }
                });
            } else {
                updateTencentOpenId.onUpdate(new CoreException("用户登录失效"), null, null, null);
            }
        }
    }

    private class BaseUserInfo implements IUiListener {
        private Activity activity;
        private String openId;

        BaseUserInfo(Activity activity, String openId) {
            this.activity = activity;
            this.openId = openId;
        }

        @Override
        public void onError(UiError e) {
            tencentLogin.done(new CoreException(e.errorMessage), null);
        }

        @Override
        public void onComplete(Object object) {
            TencentObject tencentObject = new TencentObject((JSONObject) object);
            BmobQuery query = new BmobQuery(IntArrayToString(new int[]{83, 90, 93, 108, 98, 96, 82, 95, 108, 81, 78, 97, 78}));//FMP_USER_DATA
            query.addWhereEqualTo(FMP_Tools.IntArrayToString(new int[]{85, 118, 107, 116, 79, 106})/*OpenId*/, openId);
            query.findObjectsByTable(new QueryListener<JSONArray>() {
                @Override
                public void done(JSONArray jsonArray, BmobException e) {
                    if (e == null) {
                        switch (jsonArray.length()) {
                            case 0:
                                HelperAccount userData = new HelperAccount();
                                userData.account = String.format("User%d", String.valueOf(System.currentTimeMillis()).hashCode());
                                userData.userName = tencentObject.getNickName();//设置用户名称
                                userData.userType = (activity.getString(R.string.helper_user_create_qq_name)); //设置类型
                                userData.uid = (DeviceIdUtil.getDeviceId());//设置UID
                                userData.openId = (openId);//设置openID
                                userData.versionInfo = (DeviceInfoUtil.getInstance().getVersionInfo());//更新版本信息
                                userData.deviceInfo = (DeviceInfoUtil.getInstance().getDeviceInfo());//更新设备信息
                                userData.userLevel = (0);//设置用户等级
                                userData.userSex = (tencentObject.getSex());//设置性别
                                userData.headUrl = (tencentObject.getHeadBigUrl());//设置头像
                                userData.launchCount = (0);//设置启动次数
                                userData.createUser(new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if (e == null) {
                                            //QQ登录注册成功
                                            setObjectId(s);
                                            saveUserData(userData);
                                            tencentObject.saveData();
                                            tencentLogin.done(null, userData);
                                        } else {
                                            //QQ注册失败
                                            tencentLogin.done(new CoreException(e.getMessage()), null);
                                        }
                                    }
                                });
                                break;
                            case 1:
                                try {
                                    HelperAccount cloudUserData = new HelperAccount(new HelperJSONObject(jsonArray.getJSONObject(0).toString()));
                                    if (cloudUserData.userLevel >= 0) {
                                        //QQ登录成功
                                        setExceptionCode(-1);
                                        setObjectId(cloudUserData.objectId);
                                        saveUserData(cloudUserData);
                                        subRowUpdate(cloudUserData.objectId);
                                        tencentLogin.done(null, cloudUserData);
                                    } else {
                                        tencentLogin.done(new CoreException("您未被授权QQ登录，请联系管理员解决"), null);
                                    }
                                } catch (JSONException ex) {
                                    tencentLogin.done(new CoreException(ex.getMessage()), null);
                                }
                                break;
                            default:
                                tencentLogin.done(new CoreException("登录发生错误，您的账号数据异常，请联系管理员解决"), null);
                                break;
                        }
                    } else {
                        if (e.getErrorCode() == 10210) {
                            setExceptionCode(1);
                        } else {
                            setExceptionCode(0);
                        }
                        tencentLogin.done(new CoreException(e), null);
                    }
                }
            });
        }

        @Override
        public void onCancel() {
            tencentLogin.done(new CoreException("取消获取信息"), null);
        }
    }
}
