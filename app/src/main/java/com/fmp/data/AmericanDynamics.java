package com.fmp.data;

import com.fmp.core.HelperNative;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AmericanDynamics implements Serializable {
    private static final long serialVersionUID = 7078294045425633462L;
    private List<AmericanDynamicsItem> list = new ArrayList<>();

    public AmericanDynamicsItem getNewItem() {
        return new AmericanDynamicsItem();
    }

    public List<AmericanDynamicsItem> getList() {
        return list;
    }

    public AmericanDynamicsItem get(int position) {
        return list.get(position);
    }

    public void set(int position, AmericanDynamicsItem item) {
        list.set(position, item);
    }

    public int getCount() {
        return list.size();
    }

    public void setList(List<AmericanDynamicsItem> list) {
        this.list = list;
    }

    public static AmericanDynamics getData() {
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(HelperNative.getApplication().getCacheDir(), "ad.db"));
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            return (AmericanDynamics) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new AmericanDynamics();
        }
    }

    public void saveData() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(HelperNative.getApplication().getCacheDir(), "ad.db"));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class AmericanDynamicsItem implements Serializable {
        private static final long serialVersionUID = -7465025859389595277L;
        private int position;
        private String name;
        private String image;
        private String url;

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

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
