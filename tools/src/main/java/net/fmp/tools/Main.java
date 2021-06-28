package net.fmp.tools;


public class Main {
    public static void main(String[] args) throws Throwable {

        System.out.println(IntArrayToStringArray(StringToIntArray("enable")));


    }



    public static String IntArrayToStringArray(int[] in) {
        StringBuilder str = new StringBuilder("FMP_Tools.IntArrayToString(new int[]{");
        for (int i = 0; in.length > i; i++) {
            str.append(in[i]);
            if (i != in.length - 1)
                str.append(",");
        }
        str.append("})/*").append(IntArrayToString(in)).append("*/");
        return str.toString();
    }

    public static int[] StringToIntArray(String str) {
        char[] cha = str.toCharArray();
        int[] in = new int[cha.length];
        for (int i = 0; cha.length > i; i++) {
            in[i] = cha[i] + in.length;
        }
        return in;
    }

    public static String IntArrayToString(int[] in) {
        StringBuilder Return = new StringBuilder();
        for (int value : in) {
            Return.append((char) (value - in.length));
        }
        return Return.toString();
    }
}
