package com.fmp.helper.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.billy.android.swipe.SmartSwipe;
import com.billy.android.swipe.consumer.ActivitySlidingBackConsumer;
import com.fmp.core.HelperCore;
import com.google.android.material.snackbar.Snackbar;

import net.fmp.helper.R;

public class AboutActivity extends AppCompatActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_about_me);
        Toolbar toolbar = findViewById(R.id.toolbar_about);
        toolbar.setTitle("关于我们");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_white_24dp);
        toolbar.setNavigationOnClickListener(v -> finish());

        //activity侧滑返回
        SmartSwipe.wrap(this)
                .addConsumer(new ActivitySlidingBackConsumer(this))
                //设置联动系数
                .setRelativeMoveFactor(0.5F)
                //指定可侧滑返回的方向，如：enableLeft() 仅左侧可侧滑返回
                .enableLeft()
                .enableRight();

        TextView VersionName = findViewById(R.id.about_me_version_name);
        try {
            PackageInfo MyPackageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            VersionName.setText(String.format("%s %s", getResources().getString(R.string.app_name), MyPackageInfo.versionName));
        } catch (PackageManager.NameNotFoundException e) {
            VersionName.setText(getResources().getString(R.string.app_name));
        }
        findViewById(R.id.QQGroupService).setOnClickListener(this);
        //实例化QQ加群按钮并监听点击事件
        findViewById(R.id.QQGroup).setOnClickListener(this);
        //实例化QQ加群按钮并监听点击事件
        findViewById(R.id.QQGroup2).setOnClickListener(this);
        //实例化QQ加群按钮并监听点击事件
        findViewById(R.id.QQGroup3).setOnClickListener(this);
        //实例化查看更多按钮并监听点击事件
        findViewById(R.id.AboutPeoPle).setOnClickListener(this);
        //实例化申请加入开发团队按钮并监听点击事件
        findViewById(R.id.AboutDevPeoPle).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.QQGroupService:
                HelperCore.getInstance().joinQQGroup(AboutActivity.this, "ntnkXYoCGaEXhiPNcgGcwflF6ft99Zq4");
                break;
            case R.id.QQGroup:
                HelperCore.getInstance().joinQQGroup(AboutActivity.this, "1aC9Su0RMwPxCNHE0TACJHsGKWgfvJ_7");
                break;
            case R.id.QQGroup2:
                HelperCore.getInstance().joinQQGroup(AboutActivity.this, "O5TBaqWlZmu5xDPa7R2gBdS0UBExEfK6");
                break;
            case R.id.QQGroup3:
                HelperCore.getInstance().joinQQGroup(AboutActivity.this, "Abq8HZbaIRPdPGXpiTcY4T_88Ulf8mFH");
                break;
            case R.id.AboutPeoPle:
                Snackbar.make(findViewById(R.id.about_me_layout), "很抱歉，不允许查看更多成员信息", Snackbar.LENGTH_LONG).show();
                break;
            case R.id.AboutDevPeoPle:
                Snackbar.make(findViewById(R.id.about_me_layout), "很抱歉，暂时关闭申请加入开发成员", Snackbar.LENGTH_LONG).show();
                break;
        }
    }
}
