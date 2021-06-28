package com.fmp.util;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class TimeUtil {
    private static String URL = "http://www.360.cn";

    public static long getCurTimeMills() {
        MyTask myTask = new MyTask();
        try {
            Date d = myTask.execute(URL).get();
            //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //Log.e("服务器Time", formatter.format(d));
            return d.getTime();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return new Date(getCurTimeMills()).getTime();
    }

    public static Date getNetCurTimeMills() {
        MyTask myTask = new MyTask();
        try {
            return myTask.execute(URL).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return getNetCurTimeMills();
    }

    static class MyTask extends AsyncTask<String, Void, Date> {
        @Override
        protected Date doInBackground(String... params) {
            URL url;
            try {
                url = new URL(params[0]);
                URLConnection uc = url.openConnection();
                uc.connect();
                long ld = uc.getDate();
                return new Date(ld);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new Date();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Date s) {
        }
    }

    public static long date2Milliseconds(Date time) {
        return time.getTime();
    }

    public static long getTime(String timeString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = sdf.parse(timeString);
        return d.getTime();
    }
}

