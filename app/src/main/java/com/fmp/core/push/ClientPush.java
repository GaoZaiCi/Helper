package com.fmp.core.push;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.os.Handler;
import android.os.Process;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.fmp.Logger;
import com.fmp.cloud.FMP_APP_AD;
import com.fmp.core.CoreException;
import com.fmp.core.HelperJSONObject;
import com.fmp.core.HelperNative;
import com.fmp.core.HotFixManager;
import com.fmp.core.UpdateManager;
import com.fmp.core.http.bean.HelperPush;
import com.fmp.data.AmericanDynamics;
import com.fmp.dialog.HelperDialog;
import com.fmp.library.EasyProtectorLib;
import com.fmp.util.CheckVirtual;
import com.fmp.util.TimeUtil;
import com.fmp.util.UnicodeUtil;
import com.fmp.util.VeDate;
import com.fmp.view.SmartScrollView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.zzhoujay.richtext.RichText;

import net.fmp.helper.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import per.goweii.anylayer.AnimatorHelper;
import per.goweii.anylayer.AnyLayer;
import per.goweii.anylayer.Layer;

import static com.fmp.core.HelperNative.getApplication;

public class ClientPush {
    public static final String TYPE_ALL = "all";
    public static final String TYPE_HOTUPDATE = "app_hot_update";
    public static final String TYPE_UPDATE = "app_update";
    public static final String TYPE_MESSAGE = "app_message";
    public static final String TYPE_SETTINGS = "app_settings";
    public static final String TYPE_HOTFIX = "app_hotfix";
    public static final String TYPE_FUNCTION = "app_function";
    public static final String TYPE_PLUGIN = "app_plugin";
    public static final String TYPE_HOOKCHECK = "hook_check";
    public static ClientPush clientPush;

    public static ClientPush getInstance() {
        if (clientPush == null)
            clientPush = new ClientPush();
        return clientPush;
    }

    private void refreshAllData(onRefreshPushListener refreshPushListener) {
        BmobQuery query = new BmobQuery(HelperPush.CLASS_TABLE_NAME);
        query.addWhereEqualTo("name", HelperNative.getApplication().getPackageName());
        query.findObjectsByTable(new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray jsonArray, BmobException e) {
                if (e == null) {
                    for (int i=0;jsonArray.length()>i;i++){
                        try {
                            HelperPush push=new HelperPush(new HelperJSONObject(jsonArray.getJSONObject(i).toString()));
                            if (push.push) {//判断推送状态
                                switch (push.type) {//判断推送类型
                                    case TYPE_HOTUPDATE:
                                        PushHotUpdate pushHotUpdate = new Gson().fromJson(push.data, PushHotUpdate.class);
                                        refreshPushListener.onHotUpdate(pushHotUpdate, push.tag, push.startTime, push.endTime);
                                        break;
                                    case TYPE_UPDATE:
                                        PushUpdate pushUpdate = new Gson().fromJson(push.data, PushUpdate.class);
                                        refreshPushListener.onUpdate(pushUpdate, push.tag, push.startTime, push.endTime);
                                        break;
                                    case TYPE_MESSAGE:
                                        PushMessage pushMessage = new Gson().fromJson(push.data, PushMessage.class);
                                        refreshPushListener.onMessage(pushMessage, push.tag, push.startTime, push.endTime);
                                        break;
                                    case TYPE_SETTINGS:
                                        PushSetting pushSetting = new Gson().fromJson(push.data, PushSetting.class);
                                        refreshPushListener.onSetting(pushSetting, push.tag, push.startTime, push.endTime);
                                        break;
                                    case TYPE_HOTFIX:
                                        PushHotFix pushHotFix = new Gson().fromJson(push.data, PushHotFix.class);
                                        refreshPushListener.onHotFix(pushHotFix, push.tag, push.startTime, push.endTime);
                                        break;
                                    case TYPE_FUNCTION:
                                        PushFunction pushFunction = new Gson().fromJson(push.data, PushFunction.class);
                                        refreshPushListener.onFunction(pushFunction, push.tag, push.startTime, push.endTime);
                                        break;
                                    case TYPE_PLUGIN:
                                        PushPlugin pushPlugin = new Gson().fromJson(push.data, PushPlugin.class);
                                        refreshPushListener.onPlugin(pushPlugin, push.tag, push.startTime, push.endTime);
                                        break;
                                    case TYPE_HOOKCHECK:
                                        PushHookCheck pushHookCheck = new Gson().fromJson(push.data, PushHookCheck.class);
                                        refreshPushListener.onHookCheck(pushHookCheck, push.tag, push.startTime, push.endTime);
                                        break;
                                }
                            }
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                            refreshPushListener.onError(new CoreException(ex.getMessage()));
                        }
                    }
                } else {
                    refreshPushListener.onError(new CoreException(e));
                }
            }
        });
    }

    public void checkAllPush(Context context, String pushType, boolean clickMode) {
        refreshAllData(new onRefreshPushListener() {
            @Override
            public void onHotUpdate(PushHotUpdate push, String tag, String startTime, String endTime) {
                if (pushType != null && (pushType.equals(TYPE_HOTUPDATE) || pushType.equals(TYPE_ALL)))
                    if (checkTime(tag, startTime, endTime, push.isForced())) {
                        HelperDialog dialog = new HelperDialog(context);
                        dialog.setTitle(push.getTitle());
                        RichText.fromHtml(push.getMessage()).into(dialog.getMessageView());
                        //Linkify.addLinks(dialog.getMessageView(), Linkify.ALL);
                        dialog.setButton1("下载修复包", v -> new HotFixManager(context, push.getUrl(), push.getCode(), tag));
                        dialog.setButton2("不再提示", v -> {
                            put(tag, VeDate.strToDateLong(endTime).getTime());
                            dialog.dismiss();
                        });
                        dialog.setButton3("取消下载", v -> dialog.dismiss());
                        //判断下载链接不为空--空的就隐藏
                        if (TextUtils.isEmpty(push.getUrl())) {
                            dialog.getButton1View().setVisibility(View.GONE);
                            dialog.getButton1View().setEnabled(false);
                        }
                        //判断是否可以改变窗口或者是强制更新--false就隐藏
                        if (!push.isQuit() || push.isForced()) {
                            dialog.getButton2View().setVisibility(View.GONE);
                            dialog.getButton3View().setVisibility(View.GONE);
                            dialog.getButton2View().setEnabled(false);
                            dialog.getButton3View().setEnabled(false);
                        }
                        dialog.setCancelable(push.isQuit());
                        dialog.show();
                    } else if (clickMode) {
                        toast("当前木有新的补丁~");
                    }
            }

            @Override
            public void onUpdate(PushUpdate push, String tag, String startTime, String endTime) {
                if (pushType != null && (pushType.equals(TYPE_UPDATE) || pushType.equals(TYPE_ALL)))
                    if (checkTime(tag, startTime, endTime, push.isForced())) {
                        try {
                            @SuppressLint("WrongConstant") PackageInfo info = getApplication().getPackageManager().getPackageInfo(context.getPackageName(), 64);
                            if (push.getCode() > info.versionCode || (push.isCover() && push.getCode() == info.versionCode)) {
                                HelperDialog dialog = new HelperDialog(context);
                                dialog.setTitle(push.getTitle());
                                dialog.setMessage(push.getMessage());
                                RichText.fromHtml(push.getMessage()).into(dialog.getMessageView());
                                //Linkify.addLinks(dialog.getMessageView(), Linkify.ALL);
                                dialog.setButton1("下载更新", v -> new UpdateManager(context, push.getUrl()));
                                dialog.setButton2("不再提示", v -> {
                                    put(tag, VeDate.strToDateLong(endTime).getTime());
                                    dialog.dismiss();
                                });
                                dialog.setButton3("取消更新", v -> dialog.dismiss());
                                //判断下载链接不为空--空的就隐藏
                                if (TextUtils.isEmpty(push.getUrl())) {
                                    dialog.getButton1View().setVisibility(View.GONE);
                                    dialog.getButton1View().setEnabled(false);
                                }
                                //判断是否可以改变窗口或者是强制更新--false就隐藏
                                if (!push.isQuit() || push.isForced()) {
                                    dialog.getButton2View().setVisibility(View.GONE);
                                    dialog.getButton3View().setVisibility(View.GONE);
                                    dialog.getButton2View().setEnabled(false);
                                    dialog.getButton3View().setEnabled(false);
                                }
                                if (push.getPictures() != null) {
                                    for (String picture : push.getPictures()) {
                                        ImageView view = new ImageView(context);
                                        Picasso.get()
                                                .load(picture)
                                                .into(view);
                                        dialog.addView(view);
                                    }
                                }
                                dialog.setCancelable(push.isQuit());
                                dialog.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (clickMode) {
                        toast("当前木有新版本~");
                    }
            }

            @Override
            public void onMessage(PushMessage push, String tag, String startTime, String endTime) {
                if (pushType != null && (pushType.equals(TYPE_MESSAGE) || pushType.equals(TYPE_ALL)))
                    if (checkTime(tag, startTime, endTime, push.isForced())) {
                        HelperDialog dialog = new HelperDialog(context);
                        dialog.setTitle(push.getTitle());
                        dialog.setMessage(push.getMessage());
                        RichText.fromHtml(push.getMessage()).into(dialog.getMessageView());
                        //Linkify.addLinks(dialog.getMessageView(), Linkify.ALL);
                        dialog.setButton3("OK", v -> {
                            put(tag, VeDate.strToDateLong(endTime).getTime());
                            dialog.dismiss();
                        });
                        if (push.isQuit()) {
                            dialog.getTitleView().setOnLongClickListener(v -> {
                                dialog.dismiss();
                                return false;
                            });
                        } else {
                            dialog.getButton3View().setVisibility(View.GONE);
                        }
                        dialog.getLayoutView().measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), 0);
                        if (dialog.getLayoutView().getMeasuredHeight() > dialog.getMessageView().getMeasuredHeight()) {
                            dialog.getButton3View().setEnabled(true);
                        } else {
                            dialog.getButton3View().setEnabled(false);
                            dialog.getScrollView().setScanScrollChangedListener(new SmartScrollView.ISmartScrollChangedListener() {
                                @Override
                                public void onScrolledToBottom() {
                                    dialog.getButton3View().setEnabled(true);
                                }

                                @Override
                                public void onScrolledToTop() {
                                    dialog.getButton3View().setEnabled(false);
                                }
                            });
                            new Handler().postDelayed(() -> {
                                dialog.getScrollView().fullScroll(ScrollView.FOCUS_DOWN);//滑到底部
                                //scrollView.fullScroll(ScrollView.FOCUS_UP);//滑到顶部
                                dialog.getButton3View().setEnabled(true);
                            }, 3000);
                        }
                        if (push.getPictures() != null) {
                            for (String picture : push.getPictures()) {
                                ImageView view = new ImageView(context);
                                Picasso.get()
                                        .load(picture)
                                        .into(view);
                                dialog.addView(view);
                            }
                        }
                        dialog.setCancelable(push.isQuit());
                        dialog.show();
                    } else if (clickMode) {
                        toast("当前木有新公告~");
                    }
            }

            @Override
            public void onSetting(PushSetting push, String tag, String startTime, String endTime) {
                if (pushType != null && (pushType.equals(TYPE_SETTINGS) || pushType.equals(TYPE_ALL))) {
                    if (push.isXposed() && EasyProtectorLib.checkIsXposedExist() ) {
                        Logger.i("find xposed");
                        HelperNative.onError();
                        Process.killProcess(Process.myPid());
                        System.exit(1);
                    }
                    if (push.isVirtual() && CheckVirtual.checkPkg(context) && CheckVirtual.isRunInVirtual()) {
                        Logger.i("find virtual");
                        HelperNative.onError();
                        Process.killProcess(Process.myPid());
                        System.exit(1);
                    }
                    push.saveSetting();
                }
            }

            @Override
            public void onHotFix(PushHotFix push, String tag, String startTime, String endTime) {
                if (pushType != null && (pushType.equals(TYPE_HOTFIX) || pushType.equals(TYPE_ALL)))
                    if (checkTime(tag, startTime, endTime, push.isForced())) {
                        new HotFixManager(push.getUrl(), push.getCode(), tag);
                    }
            }

            @Override
            public void onFunction(PushFunction push, String tag, String startTime, String endTime) {
                if (pushType != null && (pushType.equals(TYPE_FUNCTION) || pushType.equals(TYPE_ALL))) {
                    push.saveData();
                }
            }

            @Override
            public void onPlugin(PushPlugin push, String tag, String startTime, String endTime) {

            }

            @Override
            public void onHookCheck(PushHookCheck push, String tag, String startTime, String endTime) {
                if (pushType != null && (pushType.equals(TYPE_HOOKCHECK) || pushType.equals(TYPE_ALL))) {
                    push.saveData();
                }
            }

            @Override
            public void onError(CoreException e) {
                if (clickMode)
                    toast("发生错误:" + e.getMessage());
                if (CoreException.deBugMode){
                    Logger.toString(e);
                }
            }

            private boolean checkTime(String tag, String startTime, String endTime, boolean isForced) {
                long cacheTime = get(tag);
                long networkTime = TimeUtil.getCurTimeMills();
                Date start = VeDate.strToDateLong(startTime);
                Date end = VeDate.strToDateLong(endTime);
                return end.getTime() >= networkTime && start.getTime() <= networkTime && (cacheTime != end.getTime() || isForced || clickMode);
            }
        });
    }

    public void checkRequestCore(onRequestCoreListener listener){
        BmobQuery query = new BmobQuery(HelperPush.CLASS_TABLE_NAME);
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);// 先从缓存获取数据，如果没有，再从网络获取。
        query.addWhereEqualTo("type", "core");
        query.setLimit(1);
        query.findObjectsByTable(new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray jsonArray, BmobException e) {
                if (e==null){
                    try {
                        HelperPush push=new HelperPush(new HelperJSONObject(jsonArray.getJSONObject(0).toString()));
                        listener.done(null, push.data);
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                        listener.done(new CoreException(ex.getMessage()), null);
                    }
                }else {
                    listener.done(new CoreException(e), null);
                }
            }
        });
    }

    public void checkMainPush(onMainPushListener listener) {
        new Thread(() -> {
            try {
                PushSetting setting = PushSetting.getSetting();
                URL infoUrl = new URL(setting.getSharechain());
                URLConnection connection = infoUrl.openConnection();
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                int responseCode = httpConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = httpConnection.getInputStream();
                    ByteArrayOutputStream result = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) != -1) {
                        result.write(buffer, 0, length);
                    }
                    String str = result.toString(StandardCharsets.UTF_8.name());
                    inputStream.close();
                    result.close();
                    httpConnection.disconnect();
                    str = UnicodeUtil.toString(str).replaceAll("\\\\", "");
                    str = str.substring(str.indexOf("<span>@Start@") + 13
                            , str.indexOf("@End@"))
                            .replaceAll("</div>", "")
                            .replaceAll("<div>", "")
                            .replaceAll("</span>", "")
                            .replaceAll("&nbsp;", " ")
                            .replaceAll("&lt;", "<")
                            .replaceAll("&gt;", ">")
                            .replaceAll("&amp;", "&")
                            .replaceAll(" <img src=\"", "")
                            .replaceAll("\" {2}/>", "")
                    ;
                    str = str.substring(str.indexOf("{"), str.indexOf("}") + 1);
                    listener.onMessage(new Gson().fromJson(str, PushMessage.class));
                } else {
                    listener.onError(new CoreException("请求失败"));
                }
            } catch (Throwable e) {
                listener.onError(new CoreException(e.getMessage()));
            }
        }).start();
    }

    public void checkAmericanDynamics(onAmericanDynamicsListener listener) {
        BmobQuery<FMP_APP_AD> bmobQuery = new BmobQuery<>();
        bmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);   // 先从网络读取数据，如果没有，再从缓存中获取。
        bmobQuery.findObjects(new FindListener<FMP_APP_AD>() {
            @Override
            public void done(List<FMP_APP_AD> list, BmobException e) {
                AmericanDynamics americanDynamics = AmericanDynamics.getData();
                List<AmericanDynamics.AmericanDynamicsItem> items = americanDynamics.getList();
                if (e == null) {
                    listSort(list);
                    items.clear();
                    for (FMP_APP_AD ad : list) {
                        AmericanDynamics.AmericanDynamicsItem item = americanDynamics.getNewItem();
                        item.setName(ad.getName());
                        item.setPosition(ad.getPosition());
                        item.setImage(ad.getImage());
                        item.setUrl(ad.getUrl());
                        items.add(item);
                    }
                    listener.onList(items);
                    americanDynamics.saveData();
                } else {
                    if (americanDynamics.getCount() > 0) {
                        listener.onList(americanDynamics.getList());
                    } else {
                        String[] urls = new String[]{
                                "https://i.loli.net/2019/10/06/cepzbmHDwylS3XB.jpg",
                                "https://i.loli.net/2019/10/06/pZ7Mg4morPXHcsd.jpg",
                                "https://i.loli.net/2019/10/06/LJ6mZl43EMC7OFr.jpg",
                                "https://i.loli.net/2019/10/06/EgWGeqjRDVZnuzo.jpg",
                                "https://i.loli.net/2019/10/06/n2dR5CSmDiuxBH9.jpg",
                                "https://i.loli.net/2019/10/06/xE52CipVSW3u8eL.jpg",
                                "https://i.loli.net/2019/10/06/AaHU2hVRwjfFzOq.jpg",
                                "https://i.loli.net/2019/10/06/FZkCV5sv4Y2adND.jpg",
                                "https://i.loli.net/2019/10/06/PqFJmdI1AnQBUv4.jpg",
                                "https://i.loli.net/2019/10/06/X5esEL1hWHMu9Uj.jpg",
                                "https://i.loli.net/2019/10/06/df63BsIWbTNqwGZ.jpg",
                                "https://i.loli.net/2019/10/06/WLnesmKUONFx5v1.png",
                                "https://i.loli.net/2019/10/06/RPgFXjKvcE5sGxw.jpg"
                        };
                        int i = 0;
                        for (String str : urls) {
                            AmericanDynamics.AmericanDynamicsItem item = americanDynamics.getNewItem();
                            item.setName(String.valueOf(str.hashCode()));
                            item.setPosition(i++);
                            item.setImage(str);
                            item.setUrl(str);
                            items.add(item);
                        }
                        listener.onList(items);
                    }
                }
            }

            private void listSort(List<FMP_APP_AD> list) {
                Collections.sort(list, (item1, item2) -> {
                    try {
                        int id1 = item1.getPosition();
                        int id2 = item2.getPosition();
                        if (id1 > id2) {
                            return 1;
                        } else {
                            return -1;
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    return 0;
                });
            }
        });
    }

    private long get(String tag) {
        SharedPreferences sharedPreferences = HelperNative.getApplication().getSharedPreferences("push", Context.MODE_PRIVATE);
        return sharedPreferences.getLong(tag, 0);
    }

    public void put(String tag, long date) {
        SharedPreferences sharedPreferences = HelperNative.getApplication().getSharedPreferences("push", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (date == 0) {
            editor.remove(tag);
        } else {
            editor.putLong(tag, date);
        }
        editor.apply();
    }

    private void toast(String msg) {
        Random mRandom = new Random();
        AnyLayer.toast()
                .duration(3000)
                .icon(R.drawable.ic_info_outline_white_18dp)
                .message(msg)
                .alpha(mRandom.nextFloat())
                .backgroundColorInt(Color.argb(mRandom.nextInt(255), mRandom.nextInt(255), mRandom.nextInt(255), mRandom.nextInt(255)))
                .gravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL)
                .animator(new Layer.AnimatorCreator() {
                    @Override
                    public Animator createInAnimator(View target) {
                        return AnimatorHelper.createZoomAlphaInAnim(target);
                    }

                    @Override
                    public Animator createOutAnimator(View target) {
                        return AnimatorHelper.createZoomAlphaOutAnim(target);
                    }
                })
                .show();
    }

    public interface onAmericanDynamicsListener {
        void onList(List<AmericanDynamics.AmericanDynamicsItem> list);
    }

    public interface onMainPushListener {
        void onMessage(PushMessage push);

        void onError(CoreException e);
    }

    public interface onRequestCoreListener{
        void done(CoreException e,String coreUrl);
    }

    public interface onRefreshPushListener {
        void onHotUpdate(PushHotUpdate push, String tag, String startTime, String endTime);

        void onUpdate(PushUpdate push, String tag, String startTime, String endTime);

        void onMessage(PushMessage push, String tag, String startTime, String endTime);

        void onSetting(PushSetting push, String tag, String startTime, String endTime);

        void onHotFix(PushHotFix push, String tag, String startTime, String endTime);

        void onFunction(PushFunction push, String tag, String startTime, String endTime);

        void onPlugin(PushPlugin push, String tag, String startTime, String endTime);

        void onHookCheck(PushHookCheck push, String tag, String startTime, String endTime);

        void onError(CoreException e);
    }

}
