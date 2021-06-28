package com.fmp.core;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;

import androidx.annotation.NonNull;

import com.fmp.FMP_Tools;
import com.fmp.accessibility.AccessibilityOperator;
import com.fmp.activity.LaunchActivity;
import com.fmp.library.EasyProtectorLib;
import com.fmp.util.SpUtil;
import com.fmp.util.VeDate;

import net.fmp.helper.R;

import java.io.File;
import java.io.FileWriter;

import es.dmoral.toasty.Toasty;

public class HelperApplication extends Application implements Thread.UncaughtExceptionHandler {

    @Override
    public void onCreate() {
        try {
            System.loadLibrary(FMP_Tools.IntArrayToString(new int[]{109, 116, 119, 52, 113, 117, 112}));//fmp-jni
            super.onCreate();
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
            System.exit(1);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        EasyProtectorLib.checkXposedExistAndDisableIt();
        //异常检查
        Thread.setDefaultUncaughtExceptionHandler(this);
        //辅助功能初始化
        AccessibilityOperator.getInstance().init(this);
        /*if (new File(com.fmp.core.HelperCore.getHelperDirectory(), ".android/.fmp").exists()) {
            registerActivityLifecycleCallbacks(new ActivityCallbacks());
            startActivity(new Intent(this, NoBackActivity.class));
        }*/
        HelperCore helperCore = new HelperCore();
        helperCore.checkUserVerify((e, userData) -> {
            if (e == null) {
                if (userData != null) {
                    helperCore.subRowUpdate(userData.objectId);
                    Toasty.normal(this, "欢迎回来", getDrawable(R.drawable.ic_person_white_24dp)).show();
                }
            } else {
                Toasty.normal(this, "用户登录失效").show();
            }
        });
    }

    /**
     * Method invoked when the given thread terminates due to the
     * given uncaught exception.
     * <p>Any exception thrown by this method will be ignored by the
     * Java Virtual Machine.
     *
     * @param t the thread
     * @param e the exception
     */
    @SuppressLint("DefaultLocale")
    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
        StackTraceElement[] stackTrace = e.getStackTrace();

        StringBuilder builder = new StringBuilder();
        for (StackTraceElement stackTraceElement : stackTrace) {
            builder.append(String.format("at %s.%s(%s:%d)\n", stackTraceElement.getClassName(), stackTraceElement.getMethodName(), stackTraceElement.getFileName(), stackTraceElement.getLineNumber()));
        }
        try {
            FileWriter writer = new FileWriter(new File(getExternalFilesDir("LogInfo"), "HelperCrashHandler.log"), true);
            writer.write(String.format("%s错误时间:%s\n错误原因:%s\n", builder.toString(), VeDate.getStringDate(), e.getMessage()));
            writer.close();
            SpUtil.put(LaunchActivity.START_EXCEPTION, false);//清除异常
            Intent intent = new Intent(this, com.fmp.activity.ErrorActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("ERROR_CAUSE", e.getMessage());
            intent.putExtra("ERROR_MSG", builder.toString());
            startActivity(intent);
        } catch (Throwable e2) {
            Log.e("FMP Helper", "发生严重异常", e.getCause());
            System.exit(1);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    @Override
    public Resources getResources() {
        //获取到resources对象
        Resources res = super.getResources();
        //修改configuration的fontScale属性
        res.getConfiguration().fontScale = 1;
        //将修改后的值更新到metrics.scaledDensity属性上
        res.updateConfiguration(null,null);
        return res;
    }
}
