package com.fmp.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import net.fmp.helper.R;

public class HelperService extends Service {
    private static final String CHANNEL = "fmp_channel";
    private static final int NOTICE_ID = 100;
    private NotificationManager notificationManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (notificationManager != null) {
            notificationManager.cancel(NOTICE_ID);
        }
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initNotification();
    }

    private void initNotification() {
        notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);

        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//判断API
            NotificationChannel mChannel = new NotificationChannel(CHANNEL, "常驻服务", NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(mChannel);
            notification = new Notification.Builder(this)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.emp_logo))
                    .setChannelId(CHANNEL)
                    .setContentTitle(getResources().getString(R.string.app_name))
                    .setContentText("常驻服务")
                    .setOngoing(true)
                    .setSmallIcon(R.drawable.emp_logo)
                    .setWhen(System.currentTimeMillis())
                    .build();
        } else {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.emp_logo))
                    .setContentTitle(getResources().getString(R.string.app_name))
                    .setContentText("常驻服务")
                    .setSmallIcon(R.drawable.emp_logo)
                    .setOngoing(true)
                    .setChannelId(CHANNEL)//无效
                    .setWhen(System.currentTimeMillis());
            notification = notificationBuilder.build();
        }
        notificationManager.notify(NOTICE_ID, notification);
        //startForeground(NOTICE_ID, notification);
    }

}
