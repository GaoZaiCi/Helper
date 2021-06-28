package com.fmp.skins;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.fmp.core.HelperCore;
import com.fmp.core.HelperNative;
import com.fmp.util.SpUtil;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class SkinUtil {
    private static final String SKIN_CUR_NAME = "skinName";
    private static final int SKIN_WIDTH = 64;
    private static final int SKIN_HEIGHT = 64;
    private static final int SKIN_CAPE_WIDTH = 64;
    private static final int SKIN_CAPE_HEIGHT = 32;

    public static File getSkinsDir() {
        return HelperNative.getApplication().getExternalFilesDir("Skins");
    }

    public static File getSkin4DDir() {
        return new File(com.fmp.core.HelperCore.getHelperDirectory(), "mctools/skin_temp");
    }

    public static void addNewSkinPack(File skinFile) throws IOException {
        Bitmap bitmap = BitmapFactory.decodeFile(skinFile.getAbsolutePath());
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        if ((skinFile.getName().endsWith(".jpg") || skinFile.getName().endsWith(".png")) && ((height == SKIN_HEIGHT || height == SKIN_CAPE_HEIGHT) && (width == SKIN_WIDTH || width == SKIN_CAPE_WIDTH))) {
            SkinItem item = new SkinItem();
            item.setSteve(bitmap);
            item.setManifest(getManifestJson(HelperNative.getApplication()));
            item.setSkins(getSkinsJson(HelperNative.getApplication()));
            item.setName(skinFile.getName());
            item.setSize(skinFile.length());
            saveSkinPack(new File(getSkinsDir(), skinFile.getName().replace(".jpg", ".mcskin").replace(".png", ".mcskin")).getAbsolutePath(), item);
        } else {
            throw new IOException("不支持导入此类型图片");
        }
    }

    public static SkinItem getSkinPack(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) throw new FileNotFoundException();
        SkinItem item = new SkinItem();
        //设置名称
        item.setName(file.getName());
        //设置路径
        item.setPath(filePath);
        //设置大小
        item.setSize(file.length());
        ZipFile zipFile = new ZipFile(filePath);
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(filePath));
        ZipEntry zipEntry;
        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            if (zipEntry.getName().equals("skin_alex.png")) {
                item.setAlex(BitmapFactory.decodeStream(zipFile.getInputStream(zipEntry)));
            }
            if (zipEntry.getName().equals("skin_steve.png")) {
                item.setSteve(BitmapFactory.decodeStream(zipFile.getInputStream(zipEntry)));
            }
            if (zipEntry.getName().equals("cape.png")) {
                item.setCape(BitmapFactory.decodeStream(zipFile.getInputStream(zipEntry)));
            }
            if (zipEntry.getName().equals("capeTwo.png")) {
                item.setCapeTwo(BitmapFactory.decodeStream(zipFile.getInputStream(zipEntry)));
            }
            if (zipEntry.getName().equals("skins.json")) {
                InputStream inputStream = zipFile.getInputStream(zipEntry);
                ByteArrayOutputStream result = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    result.write(buffer, 0, length);
                }
                inputStream.close();
                result.close();
                item.setSkins(result.toString(StandardCharsets.UTF_8.name()));
            }
            if (zipEntry.getName().equals("geometry.json")) {
                InputStream inputStream = zipFile.getInputStream(zipEntry);
                ByteArrayOutputStream result = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    result.write(buffer, 0, length);
                }
                inputStream.close();
                result.close();
                item.setGeometry(result.toString(StandardCharsets.UTF_8.name()));
            }
            if (zipEntry.getName().equals("manifest.json")) {
                InputStream inputStream = zipFile.getInputStream(zipEntry);
                ByteArrayOutputStream result = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    result.write(buffer, 0, length);
                }
                inputStream.close();
                result.close();
                item.setManifest(result.toString(StandardCharsets.UTF_8.name()));
            }
        }
        zipInputStream.closeEntry();
        return item;
    }

    public static void saveSkinPack(String filePath, SkinItem item) throws IOException {
        ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(filePath));
        if (item.getAlex() != null) {
            ZipEntry zipEntry = new ZipEntry("skin_alex.png");
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            item.getAlex().compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            outZip.putNextEntry(zipEntry);
            int len;
            byte[] buffer = new byte[4096];
            while ((len = inputStream.read(buffer)) != -1) {
                outZip.write(buffer, 0, len);
            }
            outZip.closeEntry();
        }
        if (item.getSteve() != null) {
            ZipEntry zipEntry = new ZipEntry("skin_steve.png");
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            item.getSteve().compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            outZip.putNextEntry(zipEntry);
            int len;
            byte[] buffer = new byte[4096];
            while ((len = inputStream.read(buffer)) != -1) {
                outZip.write(buffer, 0, len);
            }
            outZip.closeEntry();
        }
        if (item.getCape() != null) {
            ZipEntry zipEntry = new ZipEntry("cape.png");
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            item.getCape().compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            outZip.putNextEntry(zipEntry);
            int len;
            byte[] buffer = new byte[4096];
            while ((len = inputStream.read(buffer)) != -1) {
                outZip.write(buffer, 0, len);
            }
            outZip.closeEntry();
        }
        if (item.getCapeTwo() != null) {
            ZipEntry zipEntry = new ZipEntry("capeTwo.png");
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            item.getCapeTwo().compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            outZip.putNextEntry(zipEntry);
            int len;
            byte[] buffer = new byte[4096];
            while ((len = inputStream.read(buffer)) != -1) {
                outZip.write(buffer, 0, len);
            }
            outZip.closeEntry();
        }
        if (item.getSkins() != null) {
            ZipEntry zipEntry = new ZipEntry("skins.json");
            ByteArrayInputStream inputStream = new ByteArrayInputStream(item.getSkins().getBytes());
            outZip.putNextEntry(zipEntry);
            int len;
            byte[] buffer = new byte[4096];
            while ((len = inputStream.read(buffer)) != -1) {
                outZip.write(buffer, 0, len);
            }
            outZip.closeEntry();
        }
        if (item.getGeometry() != null) {
            ZipEntry zipEntry = new ZipEntry("geometry.json");
            ByteArrayInputStream inputStream = new ByteArrayInputStream(item.getGeometry().getBytes());
            outZip.putNextEntry(zipEntry);
            int len;
            byte[] buffer = new byte[4096];
            while ((len = inputStream.read(buffer)) != -1) {
                outZip.write(buffer, 0, len);
            }
            outZip.closeEntry();
        }
        if (item.getGeometry() != null) {
            ZipEntry zipEntry = new ZipEntry("manifest.json");
            ByteArrayInputStream inputStream = new ByteArrayInputStream(item.getManifest().getBytes());
            outZip.putNextEntry(zipEntry);
            int len;
            byte[] buffer = new byte[4096];
            while ((len = inputStream.read(buffer)) != -1) {
                outZip.write(buffer, 0, len);
            }
            outZip.closeEntry();
        }
        //完成和关闭
        outZip.finish();
        outZip.close();
    }

    public static String getCurSkinName() {
        return (String) SpUtil.get(SKIN_CUR_NAME, HelperCore.EMPTY_STRING);
    }

    public static void setCurSkinName(String name) {
        SpUtil.put(SKIN_CUR_NAME, name);
    }

    public static String getManifestJson(Context context) {
        try {
            InputStream inputStream = context.getAssets().open("manifest.json");
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            inputStream.close();
            result.close();
            return result.toString(StandardCharsets.UTF_8.name());
        } catch (Exception ex) {
            return null;
        }
    }

    public static String getSkinsJson(Context context) {
        try {
            InputStream inputStream = context.getAssets().open("skins.json");
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            inputStream.close();
            result.close();
            return result.toString(StandardCharsets.UTF_8.name());
        } catch (Exception ex) {
            return null;
        }
    }

    public static String getGeometryJson(Context context) {
        try {
            InputStream inputStream = context.getAssets().open("geometry.json");
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            inputStream.close();
            result.close();
            return result.toString(StandardCharsets.UTF_8.name());
        } catch (Exception ex) {
            return null;
        }
    }

    public static List<Skin4DItem> getSkin4DList() {
        List<Skin4DItem> items = new ArrayList<>();
        File file = getSkin4DDir();
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File zip : files) {
                    if (zip.getName().endsWith(".zip")) {
                        try {
                            items.add(decode4DSkin(zip));
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }
                }
            }
        }
        return items;
    }

    public static Skin4DItem decode4DSkin(File zipPath) throws Throwable {
        Skin4DItem item = new Skin4DItem();
        item.setName(zipPath.getName());
        ZipFile zipFile = new ZipFile(zipPath);
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipPath));
        ZipEntry zipEntry;
        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            if (zipEntry.getName().endsWith(".png")) {
                item.setSkin(BitmapFactory.decodeStream(zipFile.getInputStream(zipEntry)));
            }
            if (zipEntry.getName().endsWith(".blur")) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(zipFile.getInputStream(zipEntry)));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    builder.append(line);
                }
                bufferedReader.close();
                item.setBlur(builder.toString());
            }
            if (zipEntry.getName().endsWith(".json")) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(zipFile.getInputStream(zipEntry)));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    builder.append(line);
                }
                bufferedReader.close();
                item.setJson(builder.toString());
            }
        }
        zipInputStream.closeEntry();
        //item.setJson(new JSONObject(new String(LauncherMiscUtil.decode4D(new ByteArrayInputStream(item.getBlur().getBytes()), new ByteArrayInputStream(item.getJson().getBytes())))).toString(2));
        return item;
    }

}
