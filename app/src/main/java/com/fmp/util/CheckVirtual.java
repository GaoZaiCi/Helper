package com.fmp.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by zaratustra on 2017/9/14.
 */

public class CheckVirtual {

    public static boolean checkPkg(Context context) {
        if (!context.getFilesDir().getAbsolutePath().startsWith(String.format("/data/data/%s", context.getPackageName())))
            return true;
        try {
            int count = 0;
            String packageName = context.getPackageName();
            PackageManager pm = context.getPackageManager();
            List<PackageInfo> pkgs = pm.getInstalledPackages(0);
            for (PackageInfo info : pkgs) {
                if (packageName.equals(info.packageName)) {
                    count++;
                }
            }
            return count > 1;
        } catch (Exception ignore) {}
        return false;
    }


    public static boolean isRunInVirtual() {

        String filter = getUidStrFormat();

        String result = exec("ps");
        if (result == null || result.isEmpty()) {
            return false;
        }

        String[] lines = result.split("\n");
        if (lines.length <= 0) {
            return false;
        }

        if (filter==null)return false;

        int exitDirCount = 0;

        for (String line : lines) {
            if (line.contains(filter)) {
                int pkgStartIndex = line.lastIndexOf(" ");
                String processName = line.substring(pkgStartIndex <= 0
                        ? 0 : pkgStartIndex + 1);
                File dataFile = new File(String.format("/data/data/%s",
                        processName, Locale.CHINA));
                if (dataFile.exists()) {
                    exitDirCount++;
                }
            }
        }

        return exitDirCount > 1;
    }


    private static String exec(String command) {
        BufferedOutputStream bufferedOutputStream = null;
        BufferedInputStream bufferedInputStream = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("sh");
            bufferedOutputStream = new BufferedOutputStream(process.getOutputStream());

            bufferedInputStream = new BufferedInputStream(process.getInputStream());
            bufferedOutputStream.write(command.getBytes());
            bufferedOutputStream.write('\n');
            bufferedOutputStream.flush();
            bufferedOutputStream.close();

            process.waitFor();

            return getStrFromBufferInputSteam(bufferedInputStream);
        } catch (Exception e) {
            return null;
        } finally {
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (process != null) {
                process.destroy();
            }
        }
    }

    private static String getStrFromBufferInputSteam(BufferedInputStream bufferedInputStream) {
        if (null == bufferedInputStream) {
            return "";
        }
        int BUFFER_SIZE = 512;
        byte[] buffer = new byte[BUFFER_SIZE];
        StringBuilder result = new StringBuilder();
        try {
            while (true) {
                int read = bufferedInputStream.read(buffer);
                if (read > 0) {
                    result.append(new String(buffer, 0, read));
                }
                if (read < BUFFER_SIZE) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    private static String getUidStrFormat() {
        String filter = exec("cat /proc/self/cgroup");
        if (filter == null || filter.length() == 0) {
            return null;
        }

        int uidStartIndex = filter.lastIndexOf("uid");
        int uidEndIndex = filter.lastIndexOf("/pid");
        if (uidStartIndex < 0) {
            return null;
        }
        if (uidEndIndex <= 0) {
            uidEndIndex = filter.length();
        }

        filter = filter.substring(uidStartIndex + 4, uidEndIndex);
        try {
            String strUid = filter.replaceAll("\n", "");
            if (isNumber(strUid)) {
                int uid = Integer.valueOf(strUid);
                filter = String.format("u0_a%d", uid - 10000);
                return filter;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static boolean isNumber(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}