package com.fmp.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

import com.fmp.Logger;

import org.jetbrains.annotations.Nullable;
import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ReflectPermission;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FMP_Util {

    /*//获取顶部Activity信息
    public static String[] getActivityManager(Context Context) {
        ActivityManager activityManager = (ActivityManager) Context.getSystemService(android.content.Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = activityManager.getRunningTasks(1);
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        return new String[]{componentInfo.getPackageName(), componentInfo.getClassName(), componentInfo.getShortClassName()};
    }*/

    //获取QQ列表
    public static String getQQList() {
        List<String> QQList = new ArrayList<>();
        List<String> Paths = new ArrayList<>();
        Paths.add(new File(com.fmp.core.HelperCore.getHelperDirectory(), "Tencent/MobileQQ").getAbsolutePath());
        Paths.add(new File(com.fmp.core.HelperCore.getHelperDirectory(), "Tencent/MobileQQ/rijmmkv").getAbsolutePath());
        Paths.add(new File(com.fmp.core.HelperCore.getHelperDirectory(), "Tencent/Tim").getAbsolutePath());
        Paths.add(new File(com.fmp.core.HelperCore.getHelperDirectory(), "Android/data/com.tencent.mobileqq/Tencent/MobileQQ").getAbsolutePath());
        Paths.add(new File(com.fmp.core.HelperCore.getHelperDirectory(), "Android/data/com.tencent.mobileqq/Tencent/MobileQQ/rijmmkv").getAbsolutePath());

        for (String path : Paths) {
            File[] files = new File(path).listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    if (file.isDirectory() && file.getName().matches("^[0-9]+$") && !QQList.contains(file.getName())) {
                        QQList.add(file.getName());
                    }
                }
            }
        }

        if (QQList.size() == 0) return "无";

        return QQList.toString();
    }

    public static String getQQFileIMEI() throws IOException {
        FileInputStream inputStream = new FileInputStream(new File(com.fmp.core.HelperCore.getHelperDirectory(), "Tencent/imei"));
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }


        return sb.toString().substring(6);

//        File file=new File(com.fmp.core.HelperCore.getHelperDirectory(), "Tencent/imei");
//        if (file.exists()) {
//            byte[] fileBytes = new byte[((int) file.length())];
//            FileInputStream fis = new FileInputStream(file);
//            fis.read(fileBytes);
//            fis.close();
//            for (String s : new String(fileBytes, "UTF-8").split("\n")) {
//                if (s.startsWith("imei=")) {
//                    return "Tencent/imei";
//                }
//            }
//        }
//        return "";
    }

    public static Application getApplication() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, IllegalArgumentException, NoSuchMethodException, SecurityException, InvocationTargetException {
        //获取全局Context对象,任何时候，任何地方，任何逻辑都可以获取
        Class<?> clazz = Class.forName("android.app.ActivityThread");
        Field field = clazz.getDeclaredField("sCurrentActivityThread");
        field.setAccessible(true);
        //得到ActivityThread的对象，虽然是隐藏的，但已经指向了内存的堆地址
        Object object = field.get(null);
        Method method = clazz.getDeclaredMethod("getApplication");
        method.setAccessible(true);
        Application application = (Application) method.invoke(object);
        return application;
    }

    //获取assets的xml
    public static XmlPullParser getLayoutXmlPullParser(Context mContext, String AssetsXmlName) {
        AssetManager assetManager = mContext.getAssets();
        try {
            return assetManager.openXmlResourceParser(AssetsXmlName);
        } catch (IOException e) {
            Logger.LogInfo("getLayoutXmlPullParser", e.toString());
            return null;
        }
    }

    //获取assets的xml
    public static View getAssetsLayout(Context mContext, String Path) throws IOException {
        AssetManager assetManager = mContext.getAssets();
        XmlResourceParser xmlPullParser = assetManager.openXmlResourceParser(Path);
        return LayoutInflater.from(mContext).inflate(xmlPullParser, null);
    }

    //获取assets的图片
    public static Drawable getAssetsImage(Context Context, String Path) throws IOException {
        AssetManager assetManager = Context.getAssets();
        InputStream in = assetManager.open(Path);
        Bitmap bmp = BitmapFactory.decodeStream(in);
        return new BitmapDrawable(bmp);
    }

    //获取assets的特殊图片
    @Nullable
    public static NinePatchDrawable getAssetsNinePatchDrawable(Context Context, String Path) {
        try {
            InputStream stream = Context.getAssets().open(Path);
            Bitmap bitmap = BitmapFactory.decodeStream(stream);
            byte[] chunk = bitmap.getNinePatchChunk();
            //boolean bResult = NinePatch.isNinePatchChunk(chunk);
            return new NinePatchDrawable(bitmap, chunk, new Rect(), null);
        } catch (IOException e) {
            Logger.LogInfo("getAssetsNinePatchDrawable", e);
            return null;
        }
    }

    @NonNull
    public static NinePatchDrawable getBase64NinePatchDrawable(String base64Arr) {
        byte[] bytes = android.util.Base64.decode(base64Arr.getBytes(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        byte[] chunk = bitmap.getNinePatchChunk();
        return new NinePatchDrawable(bitmap, chunk, new Rect(), null);
    }

    /**
     * MD5加密
     *
     * @param byteStr 需要加密的内容
     * @return 返回 byteStr的md5值
     */
    public static String encryptionMD5(byte[] byteStr) {
        MessageDigest messageDigest = null;
        StringBuffer md5StrBuff = new StringBuffer();
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(byteStr);
            byte[] byteArray = messageDigest.digest();
//            return Base64.encodeToString(byteArray,Base64.NO_WRAP);
            for (int i = 0; i < byteArray.length; i++) {
                if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                    md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
                } else {
                    md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5StrBuff.toString();
    }


    public static String getSignMd5Str(Context Context) {
        try {
            PackageInfo packageInfo = Context.getPackageManager().getPackageInfo(
                    Context.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];
            String signStr = encryptionMD5(sign.toByteArray());
            return signStr;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getSignature() {
        Context ctx = null;
        try {
            ctx = getApplication();
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
        try {
            /** 通过包管理器获得指定包名包含签名的包信息 **/
            PackageInfo packageInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            /******* 通过返回的包信息获得签名数组 *******/
            Signature[] signatures = packageInfo.signatures;
            /******* 循环遍历签名数组拼接应用签名 *******/
            StringBuilder builder = new StringBuilder();
            for (Signature signature : signatures) {
                builder.append(signature.toCharsString());
            }
            /************** 得到应用签名 **************/
            return builder.toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    @SuppressLint("WrongConstant")
    public static int getNumberSignature(String PackageName) throws PackageManager.NameNotFoundException {
        try {
            return getApplication().getPackageManager().getPackageInfo(PackageName, 64).signatures[0].hashCode();
        } catch (Throwable e) {
            return 0;
        }
    }

    public static Method[] getClassMethods(Class<?> cls) {
        Map<String, Method> uniqueMethods = new HashMap<String, Method>();
        Class<?> currentClass = cls;
        while (currentClass != null && currentClass != Object.class) {
            addUniqueMethods(uniqueMethods, currentClass.getDeclaredMethods());

            //获取接口中的所有方法
            Class<?>[] interfaces = currentClass.getInterfaces();
            for (Class<?> anInterface : interfaces) {
                addUniqueMethods(uniqueMethods, anInterface.getMethods());
            }
            //获取父类，继续while循环
            currentClass = currentClass.getSuperclass();
        }

        Collection<Method> methods = uniqueMethods.values();

        return methods.toArray(new Method[methods.size()]);
    }

    private static void addUniqueMethods(Map<String, Method> uniqueMethods, Method[] methods) {
        for (Method currentMethod : methods) {
            if (!currentMethod.isBridge()) {
                //获取方法的签名，格式是：返回值类型#方法名称:参数类型列表
                String signature = getSignature(currentMethod);
                //检查是否在子类中已经添加过该方法，如果在子类中已经添加过，则表示子类覆盖了该方法，无须再向uniqueMethods集合中添加该方法了
                if (!uniqueMethods.containsKey(signature)) {
                    if (canControlMemberAccessible()) {
                        try {
                            currentMethod.setAccessible(true);
                        } catch (Exception e) {
                            // Ignored. This is only a final precaution, nothing we can do.
                        }
                    }
                    uniqueMethods.put(signature, currentMethod);
                }
            }
        }
    }

    private static String getSignature(Method method) {
        StringBuilder sb = new StringBuilder();
        Class<?> returnType = method.getReturnType();
        if (returnType != null) {
            sb.append(returnType.getName()).append('#');
        }
        sb.append(method.getName());
        Class<?>[] parameters = method.getParameterTypes();
        for (int i = 0; i < parameters.length; i++) {
            if (i == 0) {
                sb.append(':');
            } else {
                sb.append(',');
            }
            sb.append(parameters[i].getName());
        }
        return sb.toString();
    }

    /**
     * Checks whether can control member accessible.
     *
     * @return If can control member accessible, it return {@literal true}
     * @since 3.5.0
     */
    public static boolean canControlMemberAccessible() {
        try {
            SecurityManager securityManager = System.getSecurityManager();
            if (null != securityManager) {
                securityManager.checkPermission(new ReflectPermission("suppressAccessChecks"));
            }
        } catch (SecurityException e) {
            return false;
        }
        return true;
    }

    /**
     * 获取单个文件的MD5值！
     *
     * @param file
     * @return
     */
    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest;
        FileInputStream in;
        byte[] buffer = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }

    public static boolean CheckNetwork(Context Context) {
        ConnectivityManager cManager = (ConnectivityManager) Context.getSystemService(android.content.Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cManager.getActiveNetworkInfo();
        return info != null && info.isAvailable();
    }

    public static void JiaQQ(Context Context, String QQ) {
        String qqUrl = "mqqwpa://im/chat?chat_type=wpa&uin=" + QQ + "&version=1";
        try {
            Context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qqUrl)));
        } catch (Exception e) {
            Logger.LogInfo("JiaQQ", e.toString());
        }
    }

    public static Date getNetTime() {
        String webUrl = "http://www.ntsc.ac.cn";//中国科学院国家授时中心
        try {
            URL url = new URL(webUrl);
            URLConnection uc = url.openConnection();
            uc.setReadTimeout(5000);
            uc.setConnectTimeout(5000);
            uc.connect();
            long correctTime = uc.getDate();
            Date date = new Date(correctTime);
            return date;
        } catch (Exception e) {
            return new Date();
        }
    }


    //读取assets的文件并放到指定目录
    public static void AssetsLoadFile(Context Context, String Path, String FileName) {
        new File(com.fmp.core.HelperCore.getHelperDirectory(), Path).mkdirs();
        File file = new File(com.fmp.core.HelperCore.getHelperDirectory(), Path + "/" + FileName);
        if (!file.exists()) {
            try {
                if (!file.isFile()) {
                    try {
                        OutputStream fileOutputStream = new FileOutputStream(file);
                        InputStream open = Context.getAssets().open(FileName);
                        byte[] bArr = new byte[1024];
                        for (int read = open.read(bArr); read > 0; read = open.read(bArr)) {
                            fileOutputStream.write(bArr, 0, read);
                        }
                        fileOutputStream.flush();
                        open.close();
                        fileOutputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    /**
     * 添加当活动为启动项
     *
     * @param cx
     * @param name 快捷方式名称
     */
    public static void addShortcut(Activity cx, String name) {
        // TODO: 2017/6/25  创建快捷方式的intent广播
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        // TODO: 2017/6/25 添加快捷名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
        //  快捷图标是允许重复
        shortcut.putExtra("duplicate", false);
        // 快捷图标
        Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(cx, cx.getResources().getIdentifier("ic_launcher", "mipmap", cx.getPackageName()));
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
        // TODO: 2017/6/25 我们下次启动要用的Intent信息
        Intent carryIntent = new Intent(Intent.ACTION_MAIN);
        carryIntent.putExtra("name", name);
        carryIntent.setClassName(cx.getPackageName(), cx.getClass().getName());
        carryIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //添加携带的Intent
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, carryIntent);
        // TODO: 2017/6/25  发送广播
        cx.sendBroadcast(shortcut);
    }

    /**
     * 去市场下载页面
     */
    public void goToMarket(Context context, String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
        }
    }

   /* private static void getApkInfo(ContextWrapper context, String packageName) throws PackageManager.NameNotFoundException {
        //首先获取packageManager
        PackageManager packageManager = context.getPackageManager();
        //获取packageInfo信息
        PackageInfo packageInfo1 = packageManager.getPackageInfo(packageName, 0);
        //获取应用版本号
        int versionCode = packageInfo1.versionCode;
        //获取应用版本名
        String versionName = packageInfo1.versionName;
        //获取应用名称
        String appName = (String) packageInfo1.applicationInfo.loadLabel(packageManager);
        //获取应用图标
        Drawable appIcon = packageInfo1.applicationInfo.loadIcon(packageManager);
        //<application/>标签下metadata获取
        ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
        Bundle metadata = applicationInfo.metaData;//然后通过改bundle获取相应值，用法见bundle
        //<activity/>标签下metadata获取(其他service\receiver\provider同理，切换flag即可)

        //入口信息获取
        PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES | PackageManager.GET_META_DATA);
        ActivityInfo[] activityInfos = packageInfo.activities;
        for (ActivityInfo activityInfo : activityInfos) {
            Bundle metadata1 = activityInfo.metaData;//然后通过改bundle获取相应值，用法见bundle
            String activityClassName = activityInfo.name;
        }
    }*/
}
