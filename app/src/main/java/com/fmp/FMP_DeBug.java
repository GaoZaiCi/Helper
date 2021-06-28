package com.fmp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import com.jian.explosion.animation.ExplosionField;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.Locale;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.helper.BmobNative;
import cn.bmob.v3.helper.RequestHelper;
import cn.bmob.v3.http.acknowledge;
import cn.bmob.v3.http.darkness;
import cn.bmob.v3.util.BmobContentProvider;
import cn.bmob.v3.util.Tempest;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class FMP_DeBug {
    public static Context mContext = null;

    public static void Test(Context Context) {
        if (mContext != null) return;

        mContext = Context;

        try {
            TestThrowable();
        } catch (Throwable e) {
            Logger.LogInfo("TestThrowable", e);
        }
    }

    private static void TestThrowable() throws Throwable {
        //AccessibilityOperator.getInstance().clickByText("复选框开关");
        //AccessibilityOperator.getInstance().clickById("软件包名:id/normal_sample_checkbox");

    }



    private static void start(Application application) {
        Intent intent = application.getBaseContext().getPackageManager().getLaunchIntentForPackage(application.getBaseContext().getPackageName());
        PendingIntent restartIntent = PendingIntent.getActivity(application, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) application.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 500, restartIntent); // 1秒钟后重启应用
    }


    /**
     * 为自己以及子View添加破碎动画，动画结束后，把View消失掉
     *
     * @param view 可能是ViewGroup的view
     */
    private void setSelfAndChildDisappearOnClick(final View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                setSelfAndChildDisappearOnClick(viewGroup.getChildAt(i));
            }
        } else {
            view.setOnClickListener(v ->
                    new ExplosionField(mContext).explode(view,
                            new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    view.setVisibility(View.GONE);
                                }
                            }));
        }
    }

    /**
     * 为自己以及子View添加破碎动画，动画结束后，View自动出现
     *
     * @param view 可能是ViewGroup的view
     */
    private void setSelfAndChildDisappearAndAppearOnClick(final View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                setSelfAndChildDisappearAndAppearOnClick(viewGroup.getChildAt(i));
            }
        } else {
            view.setOnClickListener(v ->
                    new ExplosionField(mContext).explode(view, null));
        }
    }


}
