package com.fmp.core.push;

import com.fmp.core.HelperNative;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

public class PushHookCheck implements Serializable {
    private static final long serialVersionUID = 8768733429332621312L;
    private List<String> callPython;
    private List<String> callCpp;

    public List<String> getCallPython() {
        return callPython;
    }

    public void setCallPython(List<String> callPython) {
        this.callPython = callPython;
    }

    public List<String> getCallCpp() {
        return callCpp;
    }

    public void setCallCpp(List<String> callCpp) {
        this.callCpp = callCpp;
    }

    public static PushHookCheck getData() {
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(HelperNative.getApplication().getFilesDir(), "pushHookCheck.db"));
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            return (PushHookCheck) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new PushHookCheck();
        }
    }

    void saveData() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(HelperNative.getApplication().getFilesDir(), "pushHookCheck.db"));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
