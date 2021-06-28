package com.fmp.core.plugin.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends Activity {
    Intent intent;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         intent= getIntent();
        if (intent != null) {
            String packageNmae = intent.getStringExtra("packageName");
            String classNmae = intent.getStringExtra("className");
            intent.setClassName(packageNmae, classNmae);
            startActivity(intent);

            //start();
        }
        finish();
    }

    private void start(){
        Uri parse = Uri.parse(String.format("duowan://home?mcbox_uid=%s&mcbox_id=%s&timestamp=%s","-1","-1", System.currentTimeMillis()));
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(parse);
        intent.putExtra("pluginArray", "");
        intent.putExtra("pluginIdArray", "");
        intent.putExtra("userUid", "");
        intent.putExtra("hookChecks", "");
        intent.putExtra("js_opened", "");
        intent.putExtra("hymcfloatSupport", true);
        intent.putExtra("userLevel", 100);
        intent.putExtra("loadMode", 1);
        intent.putExtra("duoWanFloat", true);
        intent.putExtra("gameFloat", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
