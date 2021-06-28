package com.fmp.receiver;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.fmp.activity.NoBackActivity;

public class HomeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
            String extra = intent.getStringExtra("reason");
            if (extra != null && extra.equals("homekey")) {
                intent = new Intent(context, NoBackActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    PendingIntent.getActivity(context, 0, intent, 0).send();
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
