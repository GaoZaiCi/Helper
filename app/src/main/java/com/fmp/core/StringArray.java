package com.fmp.core;

public class StringArray {
    public final static int TENCENT_APPID = 0;
    public final static int DATA_START_ERROR = 1;
    public final static int DATA_NETWORK_TIME = 2;
    //public final static int DATA_USER_OBJID = 3;
    public final static int DATA_USER_NAME = 4;
    public final static int DATA_USER_SEX = 5;
    public final static int DATA_USER_TYPE = 6;
    public final static int DATA_USER_QQ = 7;
    public final static int DATA_USER_ID = 8;
    public final static int DATA_OPEN_ID = 9;
    public final static int DATA_TOKEN = 10;
    public final static int DATA_QQ_NAME = 11;
    public final static int DATA_EXPIRES = 12;
    public final static int DATA_IMAGE_URL = 13;
    public final static int DATA_USER_UID = 14;
    public final static int DATA_QQ_LOGIN = 15;
    public final static int DATA_BAN_REASON = 16;
    public final static int DATA_BAN_TIME = 17;
    public final static int DEFAULT_USER_NAME = 18;
    public final static int TAG = 19;
    public final static int HIDE_FILE = 20;
    public final static int DATA_NIGHT_MODE = 21;

    private static final String[] dataArr = new String[]{
            "101730033",/*0*/
            "startError",/*1*/
            "NetworkTime",/*2*/
            "objectId",/*3*/
            "nickName",/*4*/
            "userSex",/*5*/
            "userType",/*6*/
            "qq",/*7*/
            "userId",/*8*/
            "openId",/*9*/
            "token",/*10*/
            "QQ_NickName",/*11*/
            "expires",/*12*/
            "imageUrl",/*13*/
            "UID",/*14*/
            "QQ_LOGIN",/*15*/
            "BAN_REASON",/*16*/
            "BAN_TIME",/*17*/
            "FMP用户",/*18*/
            "Helper-SDK",/*19*/
            "Android/.hide",/*20*/
            "NIGHT_MODE",/*21*/
            "",/*22*/
            "",/*23*/
            "",/*24*/
            "",/*25*/
            "",/*26*/
    };

    public static String get(int dataFlag) {
        if (dataFlag > dataArr.length) return "";
        return dataArr[dataFlag];
    }


}
