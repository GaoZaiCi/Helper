package com.fmp.util;

import android.content.Context;

/**
 * Created by L_Alden on 2016/11/10.
 * px与dp相互转换工具类
 */
public class PxUtil {
    public static float dpToPx(Context context, int dp) {
        //获取屏蔽的像素密度系数
        float density = context.getResources().getDisplayMetrics().density;
        return dp * density;
    }

    public static float pxTodp(Context context, int px) {
        //获取屏蔽的像素密度系数
        float density = context.getResources().getDisplayMetrics().density;
        return px / density;
    }
}
