package com.fmp.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.WindowManager;


public class SizeUtil {
    public static float dp2px(Context context, float f) {
        return TypedValue.applyDimension(1, f, context.getResources().getDisplayMetrics());
    }

    public static float px2dp(Context context, float f) {
        return TypedValue.applyDimension(0, f, context.getResources().getDisplayMetrics());
    }

    public static float sp2px(Context context, float f) {
        return TypedValue.applyDimension(2, f, context.getResources().getDisplayMetrics());
    }

    public static float px2sp(Context context, float f) {
        return TypedValue.applyDimension(0, f, context.getResources().getDisplayMetrics());
    }

    public static int[] forceGetViewSize(View var0) {
        int var1 = MeasureSpec.makeMeasureSpec(0, 0);
        int var2 = MeasureSpec.makeMeasureSpec(0, 0);
        var0.measure(var1, var2);
        return new int[]{var1, var2};
    }

    public static int getDeviceWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static int getDeviceHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static int getStatusBarHeight(Context context) {
        int identifier = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            return context.getResources().getDimensionPixelSize(identifier);
        }
        return 0;
    }

    /*public static int getTopBarHeight(Activity activity) {
        return getStatusBarHeight(activity) + activity.getResources().getDimensionPixelOffset(R.dimen.actionBarHeight);
    }*/
}
