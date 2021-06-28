package com.fmp.data;

import android.text.TextUtils;

import com.fmp.Logger;
import com.fmp.core.HelperNative;
import com.fmp.core.StringArray;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.fmp.core.StringArray.get;

public class TencentObject implements Serializable {
    private static final long serialVersionUID = -2927840164246771155L;
    private List<Throwable> errorList = new ArrayList<>();
    private String nickName = "";
    private int sex = 1;
    private int year;
    private String headUrl = "";
    private String headBigUrl = "";

    public TencentObject() {

    }

    public TencentObject(JSONObject object) {
        if (object != null) {
            if (object.has("nickname")) {
                try {
                    nickName = object.getString("nickname");
                } catch (JSONException e) {
                    errorList.add(e);
                    nickName = get(StringArray.DEFAULT_USER_NAME);
                }
            }
            if (object.has("gender")) {
                try {
                    String sexString = object.getString("gender");
                    sex = !TextUtils.isEmpty(sexString) && sexString.equals("女") ? 0 : 1;
                } catch (JSONException e) {
                    errorList.add(e);
                    sex = 1;
                }
            }
            if (object.has("year")) {
                try {
                    year = object.getInt("year");
                } catch (JSONException e) {
                    errorList.add(e);
                    year = 2000;
                }
            }
            if (object.has("figureurl")) {
                try {
                    headUrl = object.getString("figureurl");
                } catch (JSONException e) {
                    errorList.add(e);
                    headUrl = "https://i.loli.net/2019/10/30/1NJXL6DcofrbECi.png";
                }
            }
            if (object.has("figureurl_qq_2")) {
                try {
                    headBigUrl = object.getString("figureurl_qq_2");
                } catch (JSONException e) {
                    errorList.add(e);
                    headBigUrl = "https://i.loli.net/2019/10/30/1NJXL6DcofrbECi.png";
                }
            }
            for (Throwable throwable : errorList) {
                Logger.toString("Tencent", throwable);
            }
        }
    }

    public String getNickName() {
        return nickName;
    }

    public int getSex() {
        return sex;
    }

    public int getYear() {
        return year;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public String getHeadBigUrl() {
        return headBigUrl;
    }

    public List<Throwable> getCause() {
        return errorList;
    }

    public TencentObject getData() {
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(HelperNative.getApplication().getFilesDir(), "tencent.db"));
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            TencentObject object = (TencentObject) objectInputStream.readObject();
            nickName = object.getNickName();
            sex = object.getSex();
            year = object.getYear();
            headUrl = object.getHeadUrl();
            headBigUrl = object.headBigUrl;
            return object;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveData() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(HelperNative.getApplication().getFilesDir(), "tencent.db"));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
