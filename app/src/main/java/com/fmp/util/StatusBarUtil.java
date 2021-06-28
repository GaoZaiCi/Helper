package com.fmp.util;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import net.fmp.helper.R;


public class StatusBarUtil {
    public static void setStatusBarLayoutStyle(Context context, boolean isChange) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            ((AppCompatActivity) context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            ((AppCompatActivity) context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemBarTintManager tintManager = new SystemBarTintManager(((AppCompatActivity) context));
            // 激活状态栏
            tintManager.setStatusBarTintEnabled(true);
            //判断是否需要更改状态栏颜色
            if (isChange) {
                tintManager.setStatusBarTintResource(R.color.colorPrimaryDark);
            } else {
                tintManager.setStatusBarTintResource(android.R.color.white);
            }
            ViewGroup mContentView = ((AppCompatActivity) context).getWindow().findViewById(((AppCompatActivity) context).getWindow().ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 预留出系统 View 的空间.
                mChildView.setFitsSystemWindows(true);
            }
        }
    }
}
