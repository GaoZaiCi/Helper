package com.fmp.util;

public class UnicodeUtil {
    /* 将字符串转化为\\u形式的字符串，如： "U字符" -> "\\u55\\u5b57\\u7b26" ;U字符为字符串中每个字符的16进制信息 */
    public static String toUnicode(String str) {
        String tmp = "";

        for (char C : str.toCharArray())
            // 获取所有字符
            tmp += "\\u" + Integer.toHexString(C);        // 将每个字符的的值，转化为16进制字符串

        return tmp;
    }

    /* 将U字符转化为其表示的字符串, 如： "\\u55\\u5b57\\u7b26" -> "U字符" ;按\\u分割，依次转化为对应字符*/
    public static String toString(String str) {
        int S = 0, E = 0;
        String C = "", Value = "";

        while (str.contains("\\u")) {
            S = str.indexOf("\\u") + "\\u".length();
            E = str.indexOf("\\u", S);
            if (E == -1) E = str.length();

            if (E > S) {
                C = str.substring(S, E);
                if (C.length() > 4) C = C.substring(0, 4);
                Value = (char) Integer.parseInt(C, 16) + "";

                str = str.replace("\\u" + C, Value);
            }
        }

        return str;
    }
}
