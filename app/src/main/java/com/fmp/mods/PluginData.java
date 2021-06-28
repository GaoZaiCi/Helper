package com.fmp.mods;

import com.fmp.core.GamePluginManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PluginData implements Serializable {
    private static final long serialVersionUID = -2299488770626210044L;
    private List<Item> list = new ArrayList<>();

    public static PluginData getData() {
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(GamePluginManager.getInstance().getModFilesDir(), "ModData.dat"));
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            return (PluginData) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new PluginData();
        }
    }

    public List<Item> getList() {
        return list;
    }

    public void setList(List<Item> list) {
        this.list = list;
    }

    public int getSize() {
        return list.size();
    }

    public boolean saveData() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(GamePluginManager.getInstance().getModFilesDir(), "ModData.dat"));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
            return true;
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public PluginData.Item getNewItem(){
        return new Item();
    }

    public class Item implements Serializable {
        private static final long serialVersionUID = -3000975345649760978L;
        //加载位置
        private int position;
        //名称
        private String name;
        //完整路径
        private String path;
        //大小
        private long size;
        private String md5;
        private boolean buy;
        //是否加载
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

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public String getMd5() {
            return md5;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
        }

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public boolean isBuy() {
            return buy;
        }

        public void setBuy(boolean buy) {
            this.buy = buy;
        }
    }
}
