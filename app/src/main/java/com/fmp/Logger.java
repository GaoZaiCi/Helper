package com.fmp;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.text.Spanned;

import com.fmp.core.CoreException;
import com.fmp.core.HelperCore;
import com.fmp.core.HelperNative;
import com.fmp.util.VeDate;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import cn.bmob.v3.exception.BmobException;

import static com.fmp.FMP_Tools.HtmlText;

public class Logger extends AppConfig {
    /*
     Verbose 的调试颜色为黑色的，任何消息都会输出，这里的v代表verbose啰嗦的意思，平时使用就是Log.v(“”,”“);
     Debug 的输出颜色是蓝色的，仅输出debug调试的意思，但他会输出上层的信息，过滤起来可以通过DDMS的Logcat标签来选择.
     Info 的输出为绿色，一般提示性的消息information，它不会输出Log.v和Log.d的信息，但会显示i、w和e的信息
     Warn 的意思为橙色，可以看作为warning警告，一般需要我们注意优化Android代码，同时选择它后还会输出Log.e的信息。
     Error 为红色，可以想到error错误，这里仅显示红色的错误信息，这些错误就需要我们认真的分析，查看栈的信息了。
     Assert 表示断言失败后的错误消息，这类错误原本是不可能出现的错误，现在却出现了，是极其严重的错误类型。
     Debug属于调试日志

     其他五类Log的重要程度排序如下。
     Assert > Error > Warn > Info > Verbose

     推荐颜色：
     Verbose：#000000
     Debug ：#0000FF
     Info：#008700
     Warn：#878700
     Error：#FF0000
     Assert：#8F0005
     */
    public static final int ASSERT = 7;
    public static final int DEBUG = 3;
    public static final int ERROR = 6;
    public static final int INFO = 4;
    public static final int VERBOSE = 2;
    public static final int WARN = 5;
    private static final String TAG = "FMP Logger";
    private static final String LOG_FILE = "Helper.log";

    static {
        File logDir = HelperNative.getApplication().getExternalFilesDir("log");
        if (!logDir.exists()) {
            logDir.mkdirs();
        }
    }

    public static void all(String title, Object obj) {
        if (obj instanceof Activity) {
            writeLogInfo(title, ((Activity) obj).getClass().getName());
            showLogInfo(HtmlText("#B500FF", ((Activity) obj).getClass().getName()));
        } else if (obj instanceof Context) {
            writeLogInfo(title, ((Context) obj).getClass().getName());
            showLogInfo(HtmlText("#B500FF", ((Context) obj).getClass().getName()));
        } else {
            writeLogInfo(title, obj.toString());
            showLogInfo(HtmlText("#B500FF", obj.toString()));
        }
    }

    public static void v(String msg) {
        writeLogInfo(null, msg);
        showLogInfo(HtmlText("#000000", msg));
    }

    public static void v(String title, String msg) {
        writeLogInfo(title, msg);
        showLogInfo(HtmlText("#000000", msg));
    }

    public static void d(String msg) {
        writeLogInfo(null, msg);
        showLogInfo(HtmlText("#0000FF", msg));
    }

    public static void d(String title, String msg) {
        writeLogInfo(title, msg);
        showLogInfo(HtmlText("#0000FF", msg));
    }

    public static void i(String msg) {
        writeLogInfo(null, msg);
        showLogInfo(HtmlText("#008700", msg));
    }

    public static void i(String title, String msg) {
        writeLogInfo(title, msg);
        showLogInfo(HtmlText("#008700", msg));
    }

    public static void w(String msg) {
        writeLogInfo(null, msg);
        showLogInfo(HtmlText("#878700", msg));
    }

    public static void w(String title, String msg) {
        writeLogInfo(title, msg);
        showLogInfo(HtmlText("#878700", msg));
    }

    public static void e(String msg) {
        writeLogInfo(null, msg);
        showLogInfo(HtmlText("#FF0000", msg));
    }

    public static void e(String title, String msg) {
        writeLogInfo(title, msg);
        showLogInfo(HtmlText("#FF0000", msg));
    }

    public static void a(String msg) {
        writeLogInfo(null, msg);
        showLogInfo(HtmlText("#8F0005", msg));
    }

    public static void a(String title, String msg) {
        writeLogInfo(title, msg);
        showLogInfo(HtmlText("#8F0005", msg));
    }

    private static void showLogInfo(Spanned msg) {
        try {
            //outPutLog(msg);
        } catch (Exception e) {
            e.printStackTrace();
            writeLogInfo("showLogInfo", e.getMessage());
        }
    }


    //输出日志
    private static void writeLogInfo(String LogTag, String LogMsg) {
        File logFile = new File(HelperNative.getApplication().getExternalFilesDir("log"), LOG_FILE);
        if (!logFile.exists()) {
            File folder = logFile.getParentFile();
            if (!folder.exists()) {
                folder.mkdirs();
            }
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (logFile.exists() && logFile.isFile() && logFile.length() >= 2048000) {
                logFile.delete();
                try {
                    logFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append(Newline);
        sb.append("[");
        sb.append(VeDate.getStringDate());
        sb.append("] ");
        if (LogTag != null) {
            sb.append(LogTag);
        }
        sb.append("——");
        sb.append(LogMsg);
        try {
            RandomAccessFile raf = new RandomAccessFile(logFile, "rw");
            raf.seek(logFile.length());
            raf.write(sb.toString().getBytes());
            raf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void toString(String Tag, Throwable e) {
        StackTraceElement[] stackTrace = e.getStackTrace();
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement stackTraceElement : stackTrace) {
            sb.append("at " + stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName());
            sb.append("(");
            sb.append(stackTraceElement.getFileName() + ":" + stackTraceElement.getLineNumber());
            sb.append(")");
            sb.append(Newline);
        }
        writeLogInfo(Tag, e.getMessage() + Newline + sb.toString());
    }

    public static void toString(Throwable e) {
        StackTraceElement[] stackTrace = e.getStackTrace();
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement stackTraceElement : stackTrace) {
            sb.append("at " + stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName());
            sb.append("(");
            sb.append(stackTraceElement.getFileName() + ":" + stackTraceElement.getLineNumber());
            sb.append(")");
            sb.append(Newline);
        }
        writeLogInfo("输出异常", e.getMessage() + Newline + sb.toString());
    }

    public static void LogInfo(Object Msg) {
        if (Msg == null) Msg = "null";
        i(Msg.toString());
    }

    public static void LogInfo(String TAG, Object Msg) {
        if (Msg == null) Msg = "null";
        d(TAG + " " + Msg);
    }

    public static void LogInfo(Context Context, String TAG, int Code, String Msg) {
        e(Context.getClass().getName(), "日志名称:" + TAG + ">>>错误:" + CoreException.getErrorMsg(Code) + ">>>错误信息:" + Msg);
    }

    public static void LogInfo(String TAG, BmobException e) {
        e(TAG, "发生错误:" + CoreException.getErrorMsg(e.getErrorCode()) + ">>>错误信息:" + e.getMessage());
    }
}
