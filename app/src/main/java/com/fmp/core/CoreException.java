package com.fmp.core;

import android.annotation.SuppressLint;

import com.fmp.Logger;
import com.fmp.util.TimeUtil;

import java.text.SimpleDateFormat;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class CoreException extends Throwable {
    private static final String TAG = "CoreException";
    private static final long serialVersionUID = -7057957153741098640L;
    public static boolean deBugMode = false;

    public CoreException() {
        super();
    }

    public CoreException(String message) {
        super(message);
        if (deBugMode)
            Logger.e(TAG, message);
    }

    public CoreException(String message, Throwable cause) {
        super(message, cause);
        if (deBugMode)
            Logger.toString(message, cause);
    }

    public CoreException(Throwable cause) {
        super(cause);
        if (deBugMode)
            Logger.toString(TAG, cause);
    }

    public CoreException(BmobException e) {
        super(getErrorMsg(e.getErrorCode()), e);
        if (deBugMode)
            Logger.LogInfo(TAG, e);
    }

    public static void setDeBugMode(boolean mode) {
        deBugMode = mode;
    }

    public static String getErrorMsg(int code) {
        switch (code) {
            case 1:
                return "内部错误";
            case 138:
                return "无权限更新";
            case 101:
                return "用户数据不存在";
            case 111:
                return "数据类型错误";
            case 141:
                return "SDK时间错误";
            case 304:
                return "objectId为空";
            case 400:
                return "App未初始化";
            case 9001:
                return "Application Id为空";
            case 9002:
                return "解析返回数据出错";
            case 9003:
                return "上传文件出错";
            case 9004:
                return "文件上传失败";
            case 9005:
                return "批量操作只支持最多50条";
            case 9006:
                return "objectId为空";
            case 9007:
                return "文件大小超过10MB";
            case 9008:
                return "上传文件不存在";
            case 9009:
                return "没有缓存数据";
            case 9010:
                return "网络超时，检查您的网络";
            case 9011:
                return "用户类表不支持批量操作";
            case 9012:
                return "Context上下文为空";
            case 9013:
                return "数据表名称格式不正确";
            case 9014:
                return "第三方授权失败";
            case 9015:
                return "内部错误";
            case 9016:
                return "没有网络连接";
            case 9017:
                return "第三方登录错误";
            case 9018:
                return "参数不能为空";
            case 9019:
                return "格式不正确";
            case 9020:
                return "保存CDN信息失败";
            case 9021:
                return "文件上传缺少权限";
            case 9022:
                return "文件上传失败";
            case 9023:
                return "SDK没有初始化";
            case 10007:
                return "服务器未启用";
            case 10210:
                getTime((time, timeStr) -> HelperNative.Toast(String.format("服务器爆满，请等待%s", timeStr)));
                return "哎呀，服务器太累了，休息一会儿吧~";
            default:
                return "未定义错误" + code;
        }
    }

    @SuppressLint({"DefaultLocale", "SimpleDateFormat"})
    public static void getTime(onTimeCallBack callBack) {
        Bmob.getServerTime(new QueryListener<Long>() {
            @Override
            public void done(Long aLong, BmobException e) {
                if (e == null) {
                    callBack.onCallBack(aLong * 1000L, getTimeStr(aLong * 1000L));
                } else {
                    long curTime = TimeUtil.getCurTimeMills();
                    callBack.onCallBack(curTime, getTimeStr(curTime));
                }
            }

            private String getTimeStr(long curTime) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = formatter.format(curTime);
                String min = dateString.substring(14, 16);
                int mm = Integer.parseInt(min);
                if (mm >= 1 && mm <= 30) {
                    return String.format("%d分钟", 30 - mm);
                } else {
                    return String.format("%d分钟", 60 - mm);
                }
            }
        });

    }

    public interface onTimeCallBack {
        void onCallBack(long time, String timeStr);
    }
}
