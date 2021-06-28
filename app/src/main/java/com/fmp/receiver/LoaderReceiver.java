package com.fmp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.fmp.FMP_Toast;
import com.fmp.core.DeviceInfoUtil;
import com.fmp.core.GameLauncher;
import com.fmp.core.HelperJSONObject;
import com.fmp.core.HelperSetting;
import com.fmp.data.InternetProtocol;
import com.fmp.util.SpUtil;

import org.json.JSONException;

public class LoaderReceiver extends BroadcastReceiver {
    private static final int TYPE_NULL = -1;
    private static final int TYPE_ENTER_HOME = 5;
    private static final int TYPE_ENTER_GAME = 6;
    private static final int TYPE_LEAVE_GAME = 7;
    private static final int TYPE_LOAD_ALL_MOD = 8;
    private static final int TYPE_LOAD_ALL_END = 9;
    private static final int TYPE_LOAD_JS_TIP = 10;
    private static final int TYPE_LOAD_JS_ERR = 11;
    private static final int TYPE_LOAD_MODPKG_TIP = 20;
    private static final int TYPE_LOAD_MODPKG_ERR = 21;
    private static final int TYPE_LOAD_TEXTURES = 25;
    private static final int TYPE_PLAYER_NAME = 30;
    private static final int TYPE_APP_NETWORK_INFO=50;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            HelperSetting setting = HelperSetting.getInstance().getSetting();
            if (setting.isLoadMessage()) {
                String Value = intent.getStringExtra("VALUE");
                switch (intent.getIntExtra("TYPE", TYPE_NULL)) {
                    case TYPE_NULL:
                        break;
                    case TYPE_ENTER_HOME:
                        FMP_Toast.BM_Toast(context, "您当前进入主界面啦~", true);
                        break;
                    case TYPE_ENTER_GAME:
                        FMP_Toast.BM_Toast(context, "您已经进入一个世界~", true);
                        break;
                    case TYPE_LEAVE_GAME:
                        FMP_Toast.BM_Toast(context, "您已退出此世界~", true);
                        break;
                    case TYPE_LOAD_ALL_MOD:
                        FMP_Toast.BM_Toast(context, "FMP助手正在加载插件~", true);
                        break;
                    case TYPE_LOAD_ALL_END:
                        FMP_Toast.BM_Toast(context, "插件加载完成，祝您游戏愉快~", true);
                        break;
                    case TYPE_LOAD_JS_TIP:
                    case TYPE_LOAD_MODPKG_TIP:
                        FMP_Toast.BM_Toast(context, "FMP助手正在加载" + Value, true);
                        SpUtil.put(GameLauncher.LOAD_MOD_NAME, Value);
                        break;
                    case TYPE_LOAD_JS_ERR:
                    case TYPE_LOAD_MODPKG_ERR:
                        FMP_Toast.BM_Toast(context, Value + "加载失败", false);
                        SpUtil.put(GameLauncher.LOAD_MOD_NAME, Value);
                        break;
                    case TYPE_LOAD_TEXTURES:
                        FMP_Toast.BM_Toast(context, "正在加载资源...", true);
                    case TYPE_PLAYER_NAME:
                        SpUtil.put(GameLauncher.NETEASE_PLAYER_NAME, Value);
                        break;
                    case TYPE_APP_NETWORK_INFO:
                        try {
                            DeviceInfoUtil.getInstance().saveInternetProtocol(new InternetProtocol(new HelperJSONObject(Value)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        FMP_Toast.BM_Toast(context, Value, true);
                        break;
                }
            }
        }
    }
}
