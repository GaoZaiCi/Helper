package com.fmp.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

public class ActivityCallbacks implements Application.ActivityLifecycleCallbacks {
    private int count = 0;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        count++;
    }

    @Override
    public void onActivityStopped(Activity activity) {
        count--;
        if (count == 0) {
            activity.startActivity(new Intent(activity.getApplicationContext(), NoBackActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            ActivityManager activityManager = (ActivityManager) activity.getSystemService(android.content.Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningTaskInfo runningTaskInfo : activityManager.getRunningTasks(100)) {
                if (runningTaskInfo.topActivity.getPackageName().equals(activity.getPackageName())) {
                    activityManager.moveTaskToFront(runningTaskInfo.id, 0);
                    return;
                }
            }
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
