package com.fmp.textures;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.fmp.core.HelperNative;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class TextureManager {
    private static TextureManager textureManager;
    private File fileDir = HelperNative.getApplication().getExternalFilesDir("Textures");
    private File datFile = new File(fileDir, ".data.dat");
    private List<Item> list = new ArrayList<>();

    public static TextureManager getInstance() {
        if (textureManager == null)
            textureManager = new TextureManager();
        return textureManager;
    }

    public static File getTexturesFileDir() {
        return HelperNative.getApplication().getExternalFilesDir("Textures");
    }


    private TexturesData getData() {
        try {
            FileInputStream fileInputStream = new FileInputStream(datFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            return (TexturesData) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveData() {
        try {
            TexturesData data = new TexturesData();
            List<TextureItem> items = new ArrayList<>();
            for (Item item : list) {
                TextureItem mItem = new TextureItem();
                mItem.setPosition(item.getPosition());
                mItem.setEnable(item.isEnable());
                mItem.setName(item.getName());
                mItem.setPath(item.getPath());
                mItem.setSize(item.getSize());
                mItem.setVersion(item.getVersion());
                items.add(mItem);
            }
            data.setItemData(items);
            FileOutputStream fileOutputStream = new FileOutputStream(datFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(data);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean removeItem(Item item) {
        try {
            int i = 0;
            for (Item mItem : list) {
                if (mItem.getName().equals(item.getName())) {
                    list.remove(i);
                    File file = new File(item.getPath());
                    saveData();
                    file.delete();
                }
                i++;
            }
            return i != 0;
        } catch (NullPointerException e) {
            return false;
        }
        /*TexturesData data = getData();
        if (data != null) {
            List<TextureItem> itemList = data.getItemData();
            int i=0;
            for (TextureItem textureItem : itemList) {
                if (item.getName().equals(textureItem.getName())){
                    itemList.remove(i);

                }
                i++;
            }
        }*/
    }

    public boolean setEnable(Item item, boolean enable) {
        try {
            int i = 0;
            for (Item mItem : list) {
                if (mItem.getName().equals(item.getName())) {
                    mItem.setEnable(enable);
                    saveData();
                }
                i++;
            }
            return i != 0;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public void onRefresh() {
        List<Item> items = new ArrayList<>();
        TexturesData data = getData();
        if (data != null) {
            List<TextureItem> itemList = data.getItemData();
            for (TextureItem item : itemList) {
                Item mItem = new Item();
                mItem.setEnable(item.isEnable());
                mItem.setName(item.getName());
                mItem.setPath(item.getPath());
                mItem.setSize(item.getSize());
                mItem.setVersion(item.getVersion());
                items.add(mItem);
            }
        }
        File[] files = fileDir.listFiles();
        if (files == null) {
            items.clear();//数据为空说明没有资源包 移除所有数据
        } else {
            listLoop:
            for (File file : files) {
                if (file.getName().endsWith(".mcpack")) {
                    //是资源包
                    //没有缓存数据
                    if (items.size() == 0) {
                        try {
                            Item item = getItem(file.getAbsolutePath());
                            item.setPosition(0);
                            items.add(item);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {//有缓存数据
                        try {
                            Item item = getItem(file.getAbsolutePath());
                            for (int j = 0; items.size() > j; j++) {
                                Item listItem = items.get(j);
                                if (listItem.getName().equals(item.getName())) {
                                    //更新数据
                                    item.setEnable(listItem.isEnable());
                                    item.setPosition(listItem.getPosition());
                                    items.set(j, item);
                                    //继续下一个循环
                                    continue listLoop;
                                }
                            }
                            //没找到对应的数据
                            item.setPosition(items.size() + 1);//设置位置
                            items.add(item);//添加
                        } catch (IOException | NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        list.clear();
        list.addAll(items);
        saveData();
    }

    public List<Item> getTexturesList() {
        return list;
    }

    private Item getItem(String filePath) throws IOException {
        File file = new File(filePath);
        Item item = new Item();
        item.setName(file.getName());//名称
        item.setSize(file.length());
        item.setPath(filePath);
        item.setEnable(false);
        ZipFile zipFile = new ZipFile(filePath);
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(filePath));
        ZipEntry zipEntry;
        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            if (zipEntry.getName().equals("pack_icon.png")) {
                item.setIcon(BitmapFactory.decodeStream(zipFile.getInputStream(zipEntry)));
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
                try {
                    Manifest manifest = TextureJsonTools.getManifestClass(result.toString(StandardCharsets.UTF_8.name()));
                    item.setExplain(manifest.getHeader().getDescription());//介绍
                    StringBuilder builder = new StringBuilder();
                    List<Integer> list = manifest.getHeader().getMin_engine_version();
                    for (int i = 0; list.size() > i; i++) {
                        builder.append(list.get(i));
                        if (i != list.size() - 1) {
                            builder.append(".");
                        }
                    }
                    item.setVersion(builder.toString());//适用于MC版本
                }catch (Exception e){
                    item.setExplain("");
                    item.setVersion("");
                }


            }
        }
        zipInputStream.closeEntry();
        return item;
    }

    public class Item {
        private int position;
        private String name;
        private String path;
        private String explain;
        private String version;
        private Bitmap icon;
        private long size;
        private boolean enable;

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getExplain() {
            return explain;
        }

        public void setExplain(String explain) {
            this.explain = explain;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public Bitmap getIcon() {
            return icon;
        }

        public void setIcon(Bitmap icon) {
            this.icon = icon;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }
    }
}
