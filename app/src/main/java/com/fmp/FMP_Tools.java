package com.fmp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.util.Base64;

import com.fmp.core.HelperNative;
import com.fmp.util.FMP_Util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class FMP_Tools {
    private static Context mContext;

    static {
        try {
            mContext = FMP_Util.getApplication();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static Integer isNullInteger(Integer value) {
        if (value == null) {
            return 0;
        }
        return value;
    }

    public static boolean Equals(String str1, String str2) {
        if (str1 == null && str2 == null) return true;
        if (str1 == null || str2 == null) return false;
        return str1.equals(str2) && str1.hashCode() == str2.hashCode() && str1.toCharArray()[1] == str2.toCharArray()[1] && str1.charAt(1) == str2.charAt(1);
    }

    public static boolean isEmpty(String str) {
        if (str == null) return true;
        return str.equals("");
    }

    @SuppressLint("DefaultLocale")
    public static String encryptFileByApi(int key, String filePath) throws Exception {
        File file1 = new File(filePath);
        File file2 = new File(file1.getAbsolutePath() + "-encrypt.js");
        // 第一步文件的加密
        // 先用字节缓冲流读取文件
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file1));
        // 再用字节数组输出流将文件写到一个字节数组内
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //调用writeFile2方法写到一个字节数组内
        writeInFile(baos, bis);
        // 将字节数组输出流内的内容转换成一个字节数组
        byte[] bytes = Base64.encode(HelperNative.encrypt(key, baos.toByteArray()), Base64.NO_WRAP);
        byte[] newBytes = String.format("Helper.loadMod(\"%s\",%d);", new String(bytes), key).getBytes();

        ByteArrayInputStream bais = new ByteArrayInputStream(newBytes);
        // 用字节缓冲输出流将数组内容写到具体的位置
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file2));
        writeOutFile(bais, bos);
        //file1.delete();
        //file2.renameTo(file1);
        return file2.getAbsolutePath();
    }

    public static String encryptFile(int key, String filePath) throws Exception {
        File file1 = new File(filePath);
        File file2 = new File(file1.getAbsolutePath() + ".fmod");
        // 第一步文件的加密
        // 先用字节缓冲流读取文件
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file1));
        // 再用字节数组输出流将文件写到一个字节数组内
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //调用writeFile2方法写到一个字节数组内
        writeInFile(baos, bis);
        // 将字节数组输出流内的内容转换成一个字节数组
        byte[] bytes = HelperNative.encrypt(key, baos.toByteArray());

        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        // 用字节缓冲输出流将数组内容写到具体的位置
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file2));
        writeOutFile(bais, bos);
        //file1.delete();
        //file2.renameTo(file1);
        return file2.getAbsolutePath();
    }

    public static void decryptFile(int key, String filePath) throws Exception {
        File file1 = new File(filePath);
        File file2 = new File(file1.getAbsolutePath() + ".js");
        // 第一步文件的加密
        // 先用字节缓冲流读取文件
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file1));
        // 再用字节数组输出流将文件写到一个字节数组内
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //调用writeFile2方法写到一个字节数组内
        writeInFile(baos, bis);
        // 将字节数组输出流内的内容转换成一个字节数组
        byte[] bytes = HelperNative.decrypt(key, baos.toByteArray());

        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        // 用字节缓冲输出流将数组内容写到具体的位置
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file2));
        writeOutFile(bais, bos);
        //file1.delete();
        //file2.renameTo(file1);
    }

    private static void writeInFile(ByteArrayOutputStream baos, BufferedInputStream bis) throws Exception {
        byte[] bytes = new byte[1024];
        int len;
        while ((len = bis.read(bytes)) != -1) {
            baos.write(bytes, 0, len);
        }
        bis.close();
    }

    private static void writeOutFile(ByteArrayInputStream bais, BufferedOutputStream bos) throws Exception {
        byte[] bytes = new byte[1024];
        int len;
        while ((len = bais.read(bytes)) != -1) {
            bos.write(bytes, 0, len);
        }
        bos.close();
    }




    /*public static String ShowTime(long Time) {
        if (Time == 0)
            return "";
        long getTime = TimeUtis.getCurTimeMills();
        if (getTime <= 0)
            return "";
        long SurplusTime = Time - getTime;
        long Year = SurplusTime / 1000 / 60 / 60 / 24 / 365;
        long Month = SurplusTime / 1000 / 60 / 60 / 24 / 30 % 365;
        long Day = SurplusTime / 1000 / 60 / 60 / 24 % 30;
        long Hour = SurplusTime / 1000 / 60 / 60 % 24;
        long Minute = SurplusTime / 1000 / 60 % 60;
        long Second = SurplusTime / 1000 % 60;
        return ((Year == 0 ? "" : Year + "年") +
                (Month == 0 ? "" : Month + "月") +
                (Day == 0 ? "" : Day + "日") +
                (Hour == 0 ? "" : Hour + "时") +
                (Minute == 0 ? "" : Minute + "分") +
                (Second == 0 ? "" : Second + "秒"));
    }*/

    public static Spanned HtmlText(String color, String msg) {
        return Html.fromHtml("<font color='" + color + "'>" + msg + "</font>");
    }

//    public static String decrypt4DSkin(String json, String key) throws Throwable {
//        return new JSONObject(new String(com.mcbox.pesdk.util.LauncherMiscUtil.decode4D(new ByteArrayInputStream(json.getBytes()), new ByteArrayInputStream(key.getBytes())))).toString(2);
//    }
//    public static void decrypt () {
//        try {
//            File file = new File("/sdcard/mcpemaster/skin_temp/");
//            if (file.exists()) {
//                File[] listFiles = file.listFiles();
//                if (listFiles.length == 0) {
//                    //app("没有找到任何皮肤，请参考帮助", -16776961);
//                } else if (listFiles.length != 1) {
//                    //app("找到了多个皮肤，请参考帮助", -16776961);
//                } else {
//                    File file2 = listFiles[0];
//                    String substring = file2.getName().substring(0, file2.getName().lastIndexOf(46));
//                    File file3 = new File("/sdcard/4d皮肤解密文件夹/", substring);
//                    file3.mkdirs();
//                    //app(new StringBuffer().append("已找到皮肤 ID:").append(substring).toString());
//                    //app("正在解压皮肤…");
//                    InputStream inputStream = null;
//                    InputStream inputStream2 = null;
//                    ByteArrayOutputStream byteArrayOutputStream = null;
//                    ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(file2));
//                    while (true) {
//                        ZipEntry nextEntry = zipInputStream.getNextEntry();
//                        if (nextEntry == null) {
//                            //app("解析完成，正在解密...");
//                            String str = new String(com.mcbox.pesdk.util.LauncherMiscUtil.decode4D(inputStream2, inputStream));
//                            //app("解压完成！正在解码中");
//                            substring = new JSONObject(str).toString(2);
//                            //app("4d皮肤json解码完成！正在导出ing~");
//                            FileOutputStream fileOutputStream = new FileOutputStream(new File(file3, "skin.json"));
//                            fileOutputStream.write(substring.getBytes());
//                            fileOutputStream.close();
//                            //app(new StringBuffer().append("ojbk，4D皮肤保存在").append(file3.getPath()).toString(), -16711936);
//                            return;
//                        } else if (nextEntry.getName().equalsIgnoreCase(".blur")) {
//                            //app("已找到密钥文件.blur");
//                            byteArrayOutputStream = new ByteArrayOutputStream();
//                            copy(zipInputStream, byteArrayOutputStream);
//                            zipInputStream.closeEntry();
//                            byteArrayOutputStream.close();
//                            inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
//                        } else if (nextEntry.getName().endsWith(".json")) {
//                            //app(new StringBuffer().append("已找到皮肤Json文件:").append(nextEntry.getName()).toString());
//                            byteArrayOutputStream = new ByteArrayOutputStream();
//                            copy(zipInputStream, byteArrayOutputStream);
//                            zipInputStream.closeEntry();
//                            byteArrayOutputStream.close();
//                            inputStream2 = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
//                        } else if (nextEntry.getName().endsWith(".png")) {
//                            //app(new StringBuffer().append("已找到皮肤文件:").append(nextEntry.getName()).toString());
//                            file2 = new File(file3, "skin.png");
//                            file2.createNewFile();
//                            copy(zipInputStream, new FileOutputStream(file2));
//                        }
//                    }
//                }
//            } else {
//                //app("没有找到skin_temp文件夹，请参考帮助", -16776961);
//            }
//        } catch (Throwable e) {
//            FMP_Util.LogInfo("decrypt4DSkin",e);
//            //app(new StringBuffer().append("错误:").append(Log.getStackTraceString(th)).toString(), -65536);
//        }
//    }
//    private static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
//        byte[] bArr = new byte[256];
//        while (true) {
//            int read = inputStream.read(bArr);
//            if (read != -1) {
//                outputStream.write(bArr, 0, read);
//            } else {
//                return;
//            }
//        }
//    }

    public static int[] StringToIntArray(String str) {
        char[] cha = str.toCharArray();
        int[] in = new int[cha.length];
        for (int i = 0; cha.length > i; i++) {
            in[i] = cha[i] + in.length;
        }
        return in;
    }

    public static String IntArrayToString(int[] in) {
        StringBuilder Return = new StringBuilder();
        for (int anIn : in) {
            Return.append((char) (anIn - in.length));
        }
        return Return.toString();
    }

   /* public static boolean getDetect() {
        //检测应用是否可以备份
        //检查应用是否属于debug模式
        //检查应用是否处于调试状态
        if ((0 != (mContext.getApplicationInfo().flags &= mContext.getApplicationInfo().FLAG_ALLOW_BACKUP) || 0 != (mContext.getApplicationInfo().flags &= mContext.getApplicationInfo().FLAG_DEBUGGABLE) || android.os.Debug.isDebuggerConnected())) {
            FMP_Logger.LogInfo("onError:0");
            return true;
        }
        //正常返回null
        if (System.getProperty("http.proxyHost") != null || System.getProperty("http.proxyPort") != null) {
            FMP_Logger.LogInfo("检测到代理服务！");
        }
        return false;
    }*/

    /*public static Object[] getNetworkTime() {
        Date date = TimeUtis.getNetCurTimeMills();
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.text.SimpleDateFormat formatter2 = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String AccurateTime = formatter.format(date);
        String Date = formatter2.format(date);
        long DataTime = date.getTime();
        return new Object[]{AccurateTime, Date, DataTime};
    }*/


    /*public static HashMap<String, String> getAppConfig() {
        File file = new File(com.fmp.core.HelperCore.getHelperDirectory(), "Android_GaoZaiCi/FMP/AppConfig.txt");
        if (!file.exists()) {
            new File(com.fmp.core.HelperCore.getHelperDirectory(), "Android_GaoZaiCi/FriedMomPlatform").mkdirs();
            return null;
        }
        try {
            FileInputStream inputStream = new FileInputStream(file);
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            String data = new String(b);
            HashMap<String, String> items = new HashMap<>();
            //数据分隔字符
            String[] attrs = data.split(AppConfig.Newline);
            for (String it : attrs) {
                //数据识别字符
                String[] vs = it.split("=");
                if (vs.length != 2) continue;
                items.put(vs[0], vs[1]);
            }
            return items;
        } catch (Throwable e) {
            return null;
        }
    }*/

   /* public static void putAppConfig(String str) {
        File file = new File(com.fmp.core.HelperCore.getHelperDirectory(), "Android_GaoZaiCi/FMP/AppConfig.txt");
        if (!file.exists()) {
            new File(com.fmp.core.HelperCore.getHelperDirectory(), "Android_GaoZaiCi/FMP").mkdirs();
        }
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "rw");
            raf.write(str.getBytes());
            raf.close();
        } catch (IOException e) {
            FMP_Logger.LogInfo(e.toString());
        }
    }*/

//    public static void loadNativeAddons() throws Throwable {
//        PackageManager pm = mContext.getPackageManager();
//        List<ApplicationInfo> apps = pm.getInstalledApplications(PackageManager.GET_META_DATA);
//        for (ApplicationInfo app : apps) {
//            if (app.metaData == null)
//                continue;
//            String nativeLibName = app.metaData
//				.getString("net.zhuoweizhang.mcpelauncher.api.nativelibname");
//            if (nativeLibName != null && pm.checkPermission("net.zhuoweizhang.mcpelauncher.ADDON", app.packageName) == PackageManager.PERMISSION_GRANTED)
//                try {
//                    System.load(app.nativeLibraryDir + "/lib" + nativeLibName + ".so");
//                    com.mojang.minecraftpe.MainActivity.loadedAddons.add(app.packageName);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//        }
//    }


  /*  public static String getPlayerName() {
        try {
            File f = new File(com.fmp.core.HelperCore.getHelperDirectory(), "games/com.mojang/minecraftpe/options.txt");
            if (!f.exists()) {
                return "Steve";
            }
            byte[] fileBytes = new byte[((int) f.length())];
            FileInputStream fis = new FileInputStream(f);
            fis.read(fileBytes);
            fis.close();
            for (String s : new String(fileBytes, StandardCharsets.UTF_8).split("\n")) {
                if (s.startsWith("mp_username:")) {
                    return s.substring("mp_username:".length());
                }
            }
            return "Steve";
        } catch (Exception ie) {
            ie.printStackTrace();
            return "Steve";
        }
    }*/

    /*public static void notification(Context mContext, String ContentTitle, String ContentText) {
        //获取通知管理器，用于发送通知
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder notification = new Notification.Builder(mContext); // 创建一个Notification对象
        // 设置打开该通知，该通知自动消失
        notification.setAutoCancel(true);
        // 设置通知的图标
        notification.setSmallIcon(mContext.getResources().getIdentifier("ic_launcher", "mipmap", mContext.getPackageName()));
        // 设置通知内容的标题
        notification.setContentTitle(ContentTitle);
        // 设置通知内容
        notification.setContentText(ContentText);
        //设置使用系统默认的声音、默认震动
        notification.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        //设置发送时间
        notification.setWhen(System.currentTimeMillis());
        // 创建一个启动其他Activity的Intent
        //Intent intent = new Intent(mContext, HomeFloatWindow.class);
        //PendingIntent pi = PendingIntent.getActivity(mContext, 0, intent, 0);
        //设置通知栏点击跳转
        //notification.setContentIntent(pi);
        //发送通知
        notificationManager.notify(0x123, notification.build());
    }*/


    /*public static String getUID(boolean isReal) {
        try {
            Context ctx = Helper.getApplication();
            DisplayMetrics dm = new DisplayMetrics();
            String IMEI = null;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                try {
                    TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ctx.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                        if (tm != null) {
                            IMEI = tm.getDeviceId();
                        } else {
                            throw new Exception();
                        }
                    } else {
                        throw new Exception();
                    }
                } catch (Exception e) {
                    FMP_Logger.e("IMEI is null");
                }
            } else {
                IMEI = "IMEI";
            }
            StringBuilder builder = new StringBuilder();
            builder.append(android.os.Build.BOARD.length() % 10);
            builder.append(android.os.Build.BRAND.length() % 10);
            builder.append(android.os.Build.CPU_ABI.length() % 10);
            builder.append(android.os.Build.DEVICE.length() % 10);
            builder.append(android.os.Build.DISPLAY.length() % 10);
            builder.append(android.os.Build.HOST.length() % 10);
            builder.append(android.os.Build.ID.length() % 10);
            builder.append(android.os.Build.MANUFACTURER.length() % 10);
            builder.append(android.os.Build.MODEL.length() % 10);
            builder.append(android.os.Build.PRODUCT.length() % 10);
            builder.append(android.os.Build.TAGS.length() % 10);
            builder.append(android.os.Build.TYPE.length() % 10);
            builder.append(android.os.Build.USER.length() % 10);
            builder.append(dm.heightPixels + dm.widthPixels % 10);
            builder.append(getMacAddress(ctx).hashCode() % 10);
            String UID = new UUID(builder.toString().hashCode(), IMEI.hashCode()).toString();
            if (!isReal) {
                String CacheUID = (String) FMP_Data.get("UID", Helper.EMPTY_STRING);
                if (!CacheUID.isEmpty()) {
                    if (!CacheUID.equals(UID)) {
                        return CacheUID;
                    }
                }
            }
            FMP_Data.put(StringArray.get(StringArray.DATA_USER_UID), UID);
            return UID;
        } catch (Throwable e) {
            FMP_Logger.w("get uid error." + e.toString());
            Helper.onError();
            return null;
        }

    }*/


   /* public static void readZipFile(String file) throws Exception {
        ZipFile zf = new ZipFile(file);
        InputStream in = new BufferedInputStream(new FileInputStream(file));
        ZipInputStream zin = new ZipInputStream(in);
        ZipEntry ze;
        while ((ze = zin.getNextEntry()) != null) {
            if (ze.isDirectory()) {
            } else {
                System.err.println("file - " + ze.getName() + " : " + ze.getSize() + " bytes");
                long size = ze.getSize();
                if (size > 0) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(zf.getInputStream(ze)));
                    sun.security.pkcs.PKCS7 pkcs7 = new sun.security.pkcs.PKCS7(zf.getInputStream(ze));
                    String line;
                    while ((line = br.readLine()) != null) {
                        System.out.println(line);
                    }
                    br.close();
                }
                System.out.println();
            }
        }
        zin.closeEntry();
    }*/


    /*public static String getPublicKey() {
        try {
            Context ctx = FMP_Util.getApplication();
            ApplicationInfo appInfo = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(), 0);
            String apkPath = appInfo.sourceDir;
            ZipFile zipFile = new ZipFile(apkPath);
            ZipInputStream inZip = new ZipInputStream(new FileInputStream(apkPath));
            ZipEntry zipEntry;
            while ((zipEntry = inZip.getNextEntry()) != null) {
                String szName = zipEntry.getName();
                //是签名文件
                if (szName.endsWith(".RSA")) {
                    sun.security.pkcs.PKCS7 pkcs7 = new sun.security.pkcs.PKCS7(zipFile.getInputStream(zipEntry));
                    X509Certificate publicKey = pkcs7.getCertificates()[0];
                    String str = publicKey.getPublicKey().toString();
                    inZip.close();
                    return str.substring(str.indexOf("=") + 1, str.indexOf(","));
                }
            }
        } catch (Throwable e) {
            FMP_Logger.LogInfo("getPublicKey", e);
            Helper.onError();
        }
        Helper.onError();
        return null;
    }*/

    /*public static ArrayList<Long> getFileTime() {
        try {
            ArrayList<Long> times=new ArrayList<>();
            Context ctx = FMP_Util.getApplication();
            ApplicationInfo appInfo = ctx.getPackageManager()
                    .getApplicationInfo(ctx.getPackageName(), 0);
            String apkPath = appInfo.sourceDir;
            ZipInputStream inZip = new ZipInputStream(new FileInputStream(apkPath));
            ZipEntry zipEntry;
            while ((zipEntry = inZip.getNextEntry()) != null) {
                times.add(zipEntry.getTime());
            }
            return times;
        } catch (Throwable e) {
            FMP_Logger.LogInfo("getFileTime", e);
            Helper.onError();
        }
        Helper.onError();
        return null;
    }*/

    /**
     * ***========================================================================
     * ***   方法 getClassMethodsInfo
     * ***   备注 获取类方法信息
     * ***   编写 Gao在此
     * ***========================================================================
     **/
    private static Object[][][] getClassMethodsInfo(Class<?> Class) {
        //获取所有方法数据
        Method[] AllMethod = FMP_Util.getClassMethods(Class);
        //方法信息
        Object[][][] obj = new Object[AllMethod.length][1][3], obj2;
        //方法返回值信息
        String[] MethodReturnType = {"java.lang.String", "int", "long", "float", "boolean"};
        //类型数组
        Object[] ValueType = new Object[]{"", 0, 0L, 0F, false};
        //长度
        int length = 0;
        //处理所有方法信息
        for (int i = 0; AllMethod.length > i; i++) {
            //获取方法信息
            String[] MethodInfo = getMethodInfo(AllMethod[i]);
            //循环判断返回值信息
            for (int i2 = 0; MethodReturnType.length > i2; i2++) {
                //判断是对应的类型
                if (MethodInfo[0].equals(MethodReturnType[i2])) {
                    //判断传递参数为空
                    if (MethodInfo[2].isEmpty()) {
                        //符合的方法+1
                        length++;
                        obj[i][0][0] = MethodInfo[1];
//						//判断Sp的数据并赋值
//						if (getSpData.contains(MethodInfo[1])) {
//							//重新赋值
//							obj[i][0][1] = SpUtil.get(MethodInfo[1], ValueType[i2]);
//						}
                        obj[i][0][2] = ValueType[i2];
                        //结束循环
                        break;
                    }
                }
            }
        }
        //创建新的数据
        obj2 = new Object[length][1][3];
        //把符合的数据重新处理
        for (int i3 = 0, i4 = 0; obj.length > i3; i3++) {
            if (obj[i3][0][0] != null) {
                obj2[i4][0][0] = obj[i3][0][0];
                obj2[i4][0][1] = obj[i3][0][1];
                obj2[i4][0][2] = obj[i3][0][2];
                i4++;
            }
        }
        return obj2;
    }

    private static String[] getMethodInfo(Method method) {
        String[] MethodInfo = new String[3];
        StringBuilder sb = new StringBuilder();
        Class<?> returnType = method.getReturnType();
        //返回值
        MethodInfo[0] = returnType.getName();
        //方法名称
        MethodInfo[1] = method.getName();
        Class<?>[] parameters = method.getParameterTypes();
        for (int i = 0; i < parameters.length; i++) {
            if (i != 0)
                sb.append(',');
            sb.append(parameters[i].getName());
        }
        //传递参数
        MethodInfo[2] = sb.toString();
        return MethodInfo;
    }

    public static String getSexString(Integer i) {
        if (i == null) return "未知";
        switch (i) {
            case 0:
                return "小姐姐";
            case 1:
                return "小哥哥";
            case 2:
                return "女装大佬";
            case 3:
                return "男装大佬";
            case 4:
                return "人妖";
            default:
                return "未知";
        }
    }

    public static void closeHook() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        Class XposedBridge = Class.forName("de.robv.android.xposed.XposedBridge");
        Field disableHooks = XposedBridge.getDeclaredField("disableHooks");
        Field disableResources = XposedBridge.getDeclaredField("disableResources");
        disableHooks.setAccessible(true);
        disableHooks.set(null, true);
        disableResources.setAccessible(true);
        disableResources.set(null, true);
    }


}
