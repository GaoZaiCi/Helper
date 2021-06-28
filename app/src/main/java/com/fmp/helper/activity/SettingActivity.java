package com.fmp.helper.activity;

import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.billy.android.swipe.SmartSwipe;
import com.billy.android.swipe.consumer.ActivitySlidingBackConsumer;
import com.fmp.core.HelperSetting;

import net.fmp.helper.R;


public class SettingActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private HelperSetting setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_settings);
        Toolbar toolbar = findViewById(R.id.toolbar_settings);
        toolbar.setTitle("软件设置");
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

        Switch Notification = findViewById(R.id.settings_app_notification);
        Switch AppStartTip = findViewById(R.id.settings_app_start_tip);

        Switch ModLoadTips = findViewById(R.id.settings_mod_load_tips);
        //Switch NetworkInfo = findViewById(R.id.settings_update_network_info);

        setting = HelperSetting.getInstance().getSetting();

        Notification.setChecked(setting.isNotification());
        AppStartTip.setChecked(setting.isStartMessage());

        ModLoadTips.setChecked(setting.isLoadMessage());
        //NetworkInfo.setChecked(setting.isNetworkUpload());

        Notification.setOnCheckedChangeListener(this);
        AppStartTip.setOnCheckedChangeListener(this);
        ModLoadTips.setOnCheckedChangeListener(this);
        //NetworkInfo.setOnCheckedChangeListener(this);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.settings_app_notification:
                setting.setNotification(isChecked);
                break;
            case R.id.settings_app_start_tip:
                setting.setStartMessage(isChecked);
                break;
            case R.id.settings_mod_load_tips:
                setting.setLoadMessage(isChecked);
                break;
            /*case R.id.settings_update_network_info:
                setting.setNetworkUpload(isChecked);
                break;*/
        }
    }
}
