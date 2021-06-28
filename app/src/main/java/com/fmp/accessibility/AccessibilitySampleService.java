package com.fmp.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.fmp.FMP_Toast;
import com.fmp.Logger;

import java.util.List;


@TargetApi(16)
public class AccessibilitySampleService extends AccessibilityService {


    public static void jumpToSettingPage(Context context) {
        try {
            Intent intent = new Intent(context, AccessibilityOpenHelperActivity.class);
            intent.putExtra("action", "action_start_accessibility_setting");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri.parse("package:" + context.getPackageName());
            context.startActivity(intent);
            FMP_Toast.BM_Toast(context, "找到“辅助功能”打开即可", true);
        } catch (Exception e) {
            Logger.LogInfo(e.toString());
        }
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        FMP_Toast.BM_Toast(this, "辅助功能已开启", true);
        //服务启动时调用
        // 通过代码可以动态配置，但是可配置项少一点
//        AccessibilityServiceInfo accessibilityServiceInfo = new AccessibilityServiceInfo();
//        accessibilityServiceInfo.eventTypes = AccessibilityEvent.TYPE_WINDOWS_CHANGED
//                | AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
//                | AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
//                | AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
//        accessibilityServiceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;
//        accessibilityServiceInfo.notificationTimeout = 0;
//        accessibilityServiceInfo.flags = AccessibilityServiceInfo.DEFAULT;
//        setServiceInfo(accessibilityServiceInfo);
    }

    //在系统将要关闭这个AccessibilityService会被调用。在这个方法中进行一些释放资源的工作。
    @Override
    public boolean onUnbind(Intent intent) {
        FMP_Toast.BM_Toast(this, "辅助功能已关闭", true);
        return super.onUnbind(intent);
    }


    private void qqClickZan(AccessibilityEvent event) {
        if (event.getClassName() != null && "com.tencent.mobileqq.activity.VisitorsActivity".equals(event.getClassName().toString())) {
            AccessibilityNodeInfo root = getRootInActiveWindow();
            if (root != null) {
                try {
                    List<AccessibilityNodeInfo> nodes = root.findAccessibilityNodeInfosByText("赞");
                    for (AccessibilityNodeInfo node : nodes) {
                        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
                        if (nodeInfo != null) {
                            List<AccessibilityNodeInfo> views = nodeInfo.findAccessibilityNodeInfosByText("取消");
                            for (AccessibilityNodeInfo view : views) {
                                if (view.getClassName().equals("android.widget.TextView")) {
                                    view.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                    Thread.sleep(100);
                                }
                            }
                        }

                        if (node.getClassName().equals("android.widget.ImageView")) {
                            for (int i = 0; 20 > i; i++) {
                                node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            }
                        }
                    }
                    FMP_Toast.BM_Toast(this, "自动点击结束", true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        qqClickZan(event);
        AccessibilityOperator.getInstance().updateEvent(this, event);
    }

    @Override
    public void onInterrupt() {

    }
}
