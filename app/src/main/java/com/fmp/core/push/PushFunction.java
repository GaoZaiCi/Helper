package com.fmp.core.push;

import com.fmp.core.HelperNative;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class PushFunction implements Serializable {
    private static final long serialVersionUID = 3279769168571199867L;
    private boolean mudwFloat;
    private boolean gameFloat;
    private boolean loadMode1;
    private boolean loadMode2;
    private boolean loadMode3;

    public boolean isMudwFloat() {
        return mudwFloat;
    }

    public void setMudwFloat(boolean mudwFloat) {
        this.mudwFloat = mudwFloat;
    }

    public boolean isGameFloat() {
        return gameFloat;
    }

    public void setGameFloat(boolean gameFloat) {
        this.gameFloat = gameFloat;
    }

    public boolean isLoadMode1() {
        return loadMode1;
    }

    public void setLoadMode1(boolean loadMode1) {
        this.loadMode1 = loadMode1;
    }

    public boolean isLoadMode2() {
        return loadMode2;
    }

    public void setLoadMode2(boolean loadMode2) {
        this.loadMode2 = loadMode2;
    }

    public boolean isLoadMode3() {
        return loadMode3;
    }

    public void setLoadMode3(boolean loadMode3) {
        this.loadMode3 = loadMode3;
    }

    public static PushFunction getData() {
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(HelperNative.getApplication().getFilesDir(), "pushFunction.db"));
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            return (PushFunction) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new PushFunction();
        }
    }

    void saveData() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(HelperNative.getApplication().getFilesDir(), "pushFunction.db"));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
