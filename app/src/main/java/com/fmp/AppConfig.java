package com.fmp;

import java.util.Random;

public class AppConfig {
    public static final String EMP = "ElectroMagneticPulse";
    public static final String FMP = "FriedMomPlatform";
    public static final String KeyListener = "0123456789abcdefghigklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String KeyListener_Number = "0123456789";
    public static final String KeyListener_PackageName = "0123456789abcdefghigklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_.";
    public static final String gameWorlds = "games/com.mojang/minecraftWorlds";
    public static final String games = "games/com.mojang/minecraftpe";
    //字符串
    public static final String GouYa = "苟牙";
    public static final String GouYaQQ = "http://qm.qq.com/cgi-bin/qm/qr?k=BvgfWW6S4_-4GQLSCkGjKP5y_uZboZTC";
    public static final String ChiLong = "赤龙";
    public static final String XiaoWu = "小污";
    public static final String LangZi = "浪子";

    //public static String[] HonorUserInfo = {"懒易", "幻神", "小熙", "赤龙", "C_ke7", "GA-18", "oscura", "wsdcc", "tian_yu", "老生", "Taokeli1184", "老赵", "余触", "魅儿", "GraceAffectionYoung", "老白", "雨沐"};
    //
    //public static List<String> HonorUserList = new ArrayList<String>();
    public static String Newline = "\n";
    public static String Html_Newline = "<br>";
    public static String Html_Big = "<big>";
    public static String[] res =new String[]{
            "fmp_float_dialog_plugin",
            "fmp_float_dialog_menu_plugin_item",
            "fmp_float_dialog_menu",
            "fmp_float_dialog_function",
            "fmp_float_dialog_browser",
            "ic_highlight_off_black_24dp",
            "ic_lock_outline_black_24dp",
            "ic_lock_open_black_24dp"};

   /* static {
        if (!FMP_Tools.Equals("fmp", "fmp")) Helper.onError(1);

        String[] qu = new String[128], qv;
        for (int i = 0; i <= 31; i++) {
            qu[i] = String.format("\\u%04x", i);
        }
        qu[34] = "\\\"";
        qu[92] = "\\\\";
        qu[9] = "\\t";
        qu[8] = "\\b";
        qu[10] = "\\n";
        qu[13] = "\\r";
        qu[12] = "\\f";
        String[] strArr = qu.clone();
        qv = strArr;
        strArr[60] = "\\u003c";
        qv[62] = "\\u003e";
        qv[38] = "\\u0026";
        qv[61] = "\\u003d";
        qv[39] = "\\u0027";
        //添加本地荣誉用户
        //Collections.addAll(HonorUserList, HonorUserInfo);
    }*/

    /*
    ////已经写为jni方法
    //加密
    public static byte[] encrypt(byte[] bytes) {
        if (bytes == null) return null;
        int key = KEY;
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (bytes[i] ^ key);
            key = bytes[i];
        }
        return bytes;
    }
    //解密
    public static byte[] decrypt(byte[] bytes) {
        if (bytes == null) return null;
        int key = KEY;
        for (int i = bytes.length - 1; i > 0; i--) {
            bytes[i] = (byte) (bytes[i] ^ bytes[i - 1]);
        }
        bytes[0] = (byte) (bytes[0] ^ key);
        return bytes;
    }
    */

    /*public static String HtmlText(String Color, String Text) {
        return "<font color='" + Color + "'>" + Text + "</font>";
    }*/

    public static String getEmoji() {
        Random random = new Random();
        String[] Emoji = {
                "|•ω•`)", "(๑´•ω•)", "(〃'▽'〃)",
                "(๑•́ ₃ •̀๑)", "(๑•̀ω•́๑)", "(๑`^´๑)",
                "(๑•ี_เ•ี๑)", "(๑•̀ㅁ•́ฅ)", "(๑`灬´๑)",
                "(๑′°︿°๑)", "( • ̀ω•́ )✧", "(,,•́ . •̀,,)",
                "(=￣ω￣=)", "٩(ˊᗜˋ*)و ", "<(￣︶￣)/",
                "(灬°ω°灬)", "ヾ(•ω•`。)", "(=°ω°)ノ", "🌝", "🌚"};
        return Emoji[random.nextInt(Emoji.length)];
    }

}
