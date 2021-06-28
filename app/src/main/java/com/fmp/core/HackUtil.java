package com.fmp.core;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.fmp.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

import static android.content.pm.PackageInfo.INSTALL_LOCATION_AUTO;

public class HackUtil {
    private static String rootDir;
    private static String dexDir;
    private static String dexOatDir;
    private static String libDir;
    private static String resDir;

    private static List<File> loadedDex = new ArrayList<>();
    private static List<File> loadedLib = new ArrayList<>();

    static {
        loadedDex.clear();
        loadedLib.clear();
        rootDir = getContext().getFilesDir().getAbsolutePath() + "/.fmp";
        dexDir = rootDir + "/dex";
        dexOatDir = rootDir + "/dex/oat";
        libDir = rootDir + "/lib";
        resDir = rootDir + "/res";
    }

    private static boolean mkdirsDir(String dir) {
        File File = new File(dir);
        if (!File.exists()) return File.mkdirs();
        return true;
    }

    /**
     * 删除文件
     *
     * @param filePath 文件地址
     * @return
     */
    public static boolean deleteFiles(String filePath) {
        List<File> files = getFile(new File(filePath));
        if (files == null) {
            return false;
        }
        if (files.size() != 0) {
            for (int i = 0; i < files.size(); i++) {
                File file = files.get(i);
                if (file.exists()) {
                    /**  如果是文件则删除  如果都删除可不必判断  */
                    if (file.isFile()) {
                        file.delete();
                    }
                }
            }
        } else {
            return false;
        }
        return true;
    }

    /**
     * 遍历文件夹下的文件
     *
     * @param file 地址
     */
    public static List<File> getFile(File file) {
        List<File> list = new ArrayList<>();
        File[] fileArray = file.listFiles();
        if (fileArray == null) {
            return null;
        } else {
            for (File f : fileArray) {
                if (f.isFile()) {
                    list.add(0, f);
                } else {
                    getFile(f);
                }
            }
        }
        return list;
    }

    public static void init() throws IOException {
        mkdirsDir(rootDir);
        mkdirsDir(dexDir);
        mkdirsDir(dexOatDir);
        mkdirsDir(libDir);
        mkdirsDir(resDir);

        File dexDirFile = new File(dexDir);
        File[] fileList = dexDirFile.listFiles();
        boolean mainDexExist = false;
        if (fileList != null) {
            for (File file : fileList) {
                //如果找到主要dex文件-返回真
                if (file.getName().equals("main.dex")) mainDexExist = true;
            }
        }
        if (!mainDexExist) {
            File dexFile = new File(dexDir + File.separator + "main.dex");
            //主要dex文件不存在-读取assets的dex
            if (!dexFile.exists()) {
                dexFile.createNewFile();
                InputStream mIs = getContext().getAssets().open("main"); // 读取流
                byte[] mByte = new byte[1024];
                int bt = 0;
                FileOutputStream fos = new FileOutputStream(dexFile); // 写入流
                while ((bt = mIs.read(mByte)) != -1) { // assets为文件,从文件中读取流
                    fos.write(mByte, 0, bt);// 写入流到文件中
                }
                fos.flush();// 刷新缓冲区
                mIs.close();// 关闭读取流
                fos.close();// 关闭写入流
            }
        }
        try {
            initVersion();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //添加dex
        addAllDexLib();
        //注入
        doDexInject();
    }

    private static void initVersion() throws IOException, PackageManager.NameNotFoundException {
        @SuppressLint("WrongConstant") PackageInfo MyPackageInfo = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), INSTALL_LOCATION_AUTO);
        int code = MyPackageInfo.versionCode;

        File file = new File(rootDir, ".version");
        if (!file.exists()) {
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file, false);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.append(String.valueOf(code));
            writer.flush();
            writer.close();
        } else {
            FileInputStream inputStream = new FileInputStream(file);
            byte[] strByte = new byte[inputStream.available()];
            inputStream.read(strByte);
            int data = Integer.parseInt(new String(strByte));
            if (code > data) {
                deleteFiles(dexDir);
                deleteFiles(dexOatDir);
                deleteFiles(libDir);
                deleteFiles(resDir);
                file.delete();
                file.createNewFile();
                FileWriter fileWriter = new FileWriter(file, false);
                BufferedWriter writer = new BufferedWriter(fileWriter);
                writer.append(String.valueOf(code));
                writer.flush();
                writer.close();
            }
        }
    }

    public static void addJar(String jarPath) throws Exception {
        File jarFile = new File(jarPath);
        if (!jarFile.exists()) throw new FileNotFoundException("file not found");
        String fileName = jarFile.getName();
        if (!isFile(fileName)) throw new Exception("file type error");
        ZipInputStream inZip = new ZipInputStream(new FileInputStream(new File(jarPath)));
        ZipEntry zipEntry;
        while ((zipEntry = inZip.getNextEntry()) != null) {
            //获取文件名称
            String szName = zipEntry.getName();
            //如果名称包含非法字符-跳转到下一循环
            if (szName.contains("../")) continue;
            //是否不为目录--名称不包含非法字符
            if (!zipEntry.isDirectory()) {
                String outDir;
                //文件后缀名是否为.dex
                if (szName.endsWith(".dex")) {
                    outDir = dexDir;
                    //文件后缀名是否为.so
                } else if (szName.startsWith("lib/") && szName.endsWith(".so")) {
                    outDir = rootDir;
                } else {
                    //都不是-继续下一循环
                    continue;
                }
                //new 文件对象
                File file = new File(outDir + File.separator + szName);
                //文件不存在
                if (!file.exists()) {
                    //获取父目录--创建目录
                    file.getParentFile().mkdirs();
                    //创建新文件
                    file.createNewFile();
                } else {
                    file.delete();
                    file.createNewFile();
                }
                // 获取文件的输出流
                FileOutputStream out = new FileOutputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                // 读取字节到缓冲区
                while ((len = inZip.read(buffer)) != -1) {
                    // 从缓冲区位置写入字节
                    out.write(buffer, 0, len);
                    //刷新缓冲区
                    out.flush();
                }
                //关闭输出流
                out.close();
            }
        }
        //关闭zip流
        inZip.close();
        //删除文件
        jarFile.delete();
    }

    private static Context getContext() {
        try {
            Class<?> clazz = Class.forName("android.app.ActivityThread");
            Field field = clazz.getDeclaredField("sCurrentActivityThread");
            field.setAccessible(true);
            Object object = field.get(null);
            Method method = clazz.getDeclaredMethod("getApplication");
            method.setAccessible(true);
            Application application = (Application) method.invoke(object);
            return application.getApplicationContext();
        } catch (Throwable e) {
            Logger.e(e.toString());
            return HelperNative.getApplication();
        }
    }

//    public static void addDexFile(String addDexPath, String dexName) throws FileNotFoundException, IOException, Exception {
//        if (dexName.endsWith(".apk") || dexName.endsWith(".jar") || dexName.endsWith(".zip")) {
//            addJar(addDexPath + File.separator + dexName);
//            return;
//        } else if (!dexName.endsWith(".dex")) {
//            throw new Exception("File Type Error");
//        }
//        //dex路径
//        String filePath = dexDir + File.separator + dexName;
//        //dex文件对象
//        File dexFile = new File(filePath);
//        //如果dex存在-删除
//        if (dexFile.exists()) dexFile.delete();
//        //写入dex文件
//        InputStream in = new FileInputStream(addDexPath + File.separator + dexName);
//        FileOutputStream out = new FileOutputStream(filePath);
//        byte[] buffer = new byte[1024];
//        for (int i=0;(i = in.read(buffer)) != -1;i++) {
//            //写入
//            out.write(buffer, 0, i);
//        }
//        //如果dex文件不存在-抛出异常
//        if (!new File(filePath).exists()) throw new Exception("Dex File Not Found");
//        //缓存文件对象
//        File cacheDexFile=new File(addDexPath + File.separator + dexName);
//        //删除缓存
//        cacheDexFile.delete();
//    }

    private static void addAllDexLib() {
        //dex文件夹对象
        File dexFile = new File(dexDir);
        File[] dexListFiles = dexFile.listFiles();
        if (dexListFiles != null) {
            //循环添加
            for (File file : dexListFiles) {
                //判断是否为目录
                if (file.isDirectory()) {
                    File[] ListFiles = file.listFiles();
                    for (File file2 : ListFiles) {
                        if (file2.getName().endsWith(".dex")) {
                            loadedDex.add(file2);
                        }
                    }
                    //不是目录-判断后缀名
                } else if (file.getName().endsWith(".dex")) {
                    loadedDex.add(file);
                }
            }
        }
        //so文件夹对象
        File libFile = new File(libDir);
        File[] libListFiles = libFile.listFiles();
        if (libListFiles != null) {
            //循环添加
            for (File file : libListFiles) {
                if (file.isDirectory()) {
                    File[] ListFiles = file.listFiles();
                    for (File file2 : ListFiles) {
                        if (file2.getName().endsWith(".so")) {
                            loadedLib.add(file2);
                        }
                    }
                }
            }
        }
    }

    private static void doDexInject() {
        //dex文件需要被写入的目录
        File fileOat = new File(dexOatDir);
        //获得加载应用程序dex的PathClassLoader
        PathClassLoader pathClassLoader = (PathClassLoader) getContext().getClassLoader();
        for (File dex : loadedDex) {
            loadPathDexSo(pathClassLoader, dex.getAbsolutePath(), fileOat.getAbsolutePath(), null);
        }
        for (File so : loadedLib) {
            try {
                System.load(so.getAbsolutePath());
            } catch (Throwable e) {
                e.printStackTrace();
            }

        }
    }

    private static void loadPathDexSo(PathClassLoader classLoader, String dexPath, String dexOatPath, String soPath) {
        //获得加载指定路径下dex的DexClassLoader
        try {
            DexClassLoader dexClassLoader = new DexClassLoader(
                    dexPath,
                    dexOatPath,
                    soPath,
                    classLoader);
            //合并dex
            Object dexObj = getPathList(dexClassLoader);
            Object pathObj = getPathList(classLoader);
            Object fixDexElements = getDexElements(dexObj);
            Object pathDexElements = getDexElements(pathObj);
            //合并两个数组
            Object newDexElements = combineArray(fixDexElements, pathDexElements);
            //重新赋值给PathClassLoader 中的dexElements数组
            Object pathList = getPathList(classLoader);
            Field localField = pathList.getClass().getDeclaredField("dexElements");
            localField.setAccessible(true);
            localField.set(pathList, newDexElements);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.toString("DexElements", e);
        }
    }

    private static Object getPathList(Object baseDexClassLoader) throws Exception {
        return getField(baseDexClassLoader, Class.forName("dalvik.system.BaseDexClassLoader"), "pathList");
    }

    private static Object getDexElements(Object obj) throws Exception {
        return getField(obj, obj.getClass(), "dexElements");
    }

    /**
     * 通过反射获得对应类
     *
     * @param obj   Object类对象
     * @param cl    class对象
     * @param field 获得类的字符串名称
     * @return obj
     */
    private static Object getField(Object obj, Class cl, String field) throws NoSuchFieldException, IllegalAccessException {
        Field localField = cl.getDeclaredField(field);
        localField.setAccessible(true);
        return localField.get(obj);
    }

    /**
     * 两个数组合并
     *
     * @param arrayLhs 修复
     * @param arrayRhs 原
     * @return obj
     */
    private static Object combineArray(Object arrayLhs, Object arrayRhs) {
        Class<?> localClass = arrayLhs.getClass().getComponentType();
        int i = Array.getLength(arrayLhs);
        int j = i + Array.getLength(arrayRhs);
        Object result = Array.newInstance(localClass, j);
        for (int k = 0; k < j; ++k) {
            if (k < i) {
                Array.set(result, k, Array.get(arrayLhs, k));
            } else {
                Array.set(result, k, Array.get(arrayRhs, k - i));
            }
        }
        return result;
    }

    private static boolean isFile(String fileName) {
        return (fileName.endsWith(".jar") || fileName.endsWith(".apk") || fileName.endsWith(".zip"));
    }
}
