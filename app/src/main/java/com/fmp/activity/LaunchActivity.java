package com.fmp.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.fmp.AppConfig;
import com.fmp.FMP_Toast;
import com.fmp.Logger;
import com.fmp.core.HelperCore;
import com.fmp.helper.MainActivity;
import com.fmp.util.SpUtil;

import net.fmp.helper.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_NEUTRAL;
import static android.content.DialogInterface.BUTTON_POSITIVE;

public class LaunchActivity extends AppCompatActivity implements DialogInterface.OnClickListener, OnClickListener, OnLongClickListener, Runnable {
    public static final String START_EXCEPTION = "startException";
    private static final int PERMISSION_REQUESTCODE = 100;
    private AlertDialog SubDialog;
    private int logoClickCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (HelperCore.getInstance().getMainActivity() != null) {
            finish();
            return;
        }
        //禁止截图
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        //设置效果
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        //透明度alpha在0.0f到1.0f之间
        lp.dimAmount = 0.4f;
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        //模糊度dimAmount在0.0f和1.0f之间
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        initView();
        initExceptionDialog();
        //提示授权
        requestRunPermisssions(new String[]{
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
        });
    }

    @Override
    public void finish() {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.finish();
    }


    @Override
    public boolean onLongClick(View view) {
        if (logoClickCount >= 2) {
            FMP_Toast.Show_Toast(this, "哎嘿，居然被你发现这个彩蛋了~" + AppConfig.getEmoji());
        }
        logoClickCount++;
        return false;
    }

    private void initView() {
        RelativeLayout layout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.activity_launch, null);
        TextView tv_FixClient = layout.findViewById(R.id.tv_launch_fixclient);
        TextView tv_Open = layout.findViewById(R.id.tv_launch_open);
        TextView tv_Version = layout.findViewById(R.id.tv_launch_version);
        ImageView img_logo = layout.findViewById(R.id.img_launch_logo);
        setContentView(layout);
        try {
            PackageInfo MyPackageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
            tv_Version.setText(MyPackageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        tv_Open.setVisibility(View.GONE);

        tv_FixClient.setOnClickListener(this);
        tv_Open.setOnClickListener(this);
        img_logo.setOnLongClickListener(this);
    }

    private void openSetting() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("package:" + getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivity(intent);
    }

    private void initExceptionDialog() {
        boolean exception = (boolean) SpUtil.get(START_EXCEPTION, false);
        if (exception) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("异常提示");
            builder.setMessage("Helper上一次启动发生异常，可能是闪退或者突然关闭导致的");
            builder.setNegativeButton("退出软件", this);
            builder.setPositiveButton("继续进入", this);
            builder.setCancelable(false);
            builder.show();
        } else {
            SpUtil.put(START_EXCEPTION, true);//放置异常
        }
    }


    private void initDialog() {
        RelativeLayout layout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.fmp_alert_dialog, null);
        TextView tv_Title = layout.findViewById(R.id.tv_alert_title);
        TextView tv_Message = layout.findViewById(R.id.tv_alert_message);
        Button btn_Button1 = layout.findViewById(R.id.btn_alert_button1);
        Button btn_Button2 = layout.findViewById(R.id.btn_alert_button2);
        Button btn_Button3 = layout.findViewById(R.id.btn_alert_button3);

        tv_Title.setText("权限提示");
        tv_Message.setText("Helper助手需要您打开软件权限继续操作。主要权限：存储权限与获取设备信息的权限");

        btn_Button1.setText("退出软件");
        btn_Button2.setText("跳转设置");
        btn_Button3.setText("继续进入");

        btn_Button1.setOnClickListener(this);
        btn_Button2.setOnClickListener(this);
        btn_Button3.setOnClickListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout);
        builder.setCancelable(false);
        SubDialog = builder.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_launch_fixclient:
                break;
            case R.id.tv_launch_open:
                startMainActivity();
                break;
            case R.id.btn_alert_button1:
                finish();
                System.exit(0);
                break;
            case R.id.btn_alert_button2:
                openSetting();
                break;
            case R.id.btn_alert_button3:
                FMP_Toast.BM_Toast(this, "您可以在需要的时候再打开权限", false);
                SubDialog.dismiss();
                startMainActivity();
                break;
        }
    }

    private void startMainActivity() {
        new Handler().postDelayed(this, 4000);
    }

    @Override
    public void onClick(DialogInterface dialog, int i) {
        switch (i) {
            case BUTTON_POSITIVE:
                dialog.dismiss();
                break;
            case BUTTON_NEGATIVE:
                finish();
                System.exit(0);
                break;
            case BUTTON_NEUTRAL:
                break;
        }
    }

    @Override
    public void run() {
        startActivity(new Intent(LaunchActivity.this, MainActivity.class));
        finish();
    }

    public void requestRunPermisssions(String[] permissions) {
        List<String> permissionLists = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionLists.add(permission);
            }
        }
        if (!permissionLists.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionLists.toArray(new String[permissionLists.size()]), PERMISSION_REQUESTCODE);
        } else {
            startMainActivity();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUESTCODE) {
            if (grantResults.length > 0) {
                //存放没授权的权限
                List<String> deniedPermissions = new ArrayList<>();
                for (int i = 0; i < grantResults.length; i++) {
                    int grantResult = grantResults[i];
                    String permission = permissions[i];
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        deniedPermissions.add(permission);
                    }
                }
                if (deniedPermissions.isEmpty()) {
                    startMainActivity();
                } else {
                    initDialog();
                    for (String permission : deniedPermissions) {
                        Logger.i("被拒绝的权限", permission);
                    }
                }
            }
        }
    }


}
