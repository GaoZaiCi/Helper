package com.fmp.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.fmp.core.DeviceIdUtil;
import com.fmp.core.HelperCore;
import com.fmp.util.SpUtil;
import com.fmp.view.CircleImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import net.fmp.helper.R;

import es.dmoral.toasty.Toasty;
import mehdi.sakout.fancybuttons.FancyButton;

public class LogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_layout_login, null);
        setContentView(view);

        CircleImageView HeadIcon = view.findViewWithTag("icon");
        TextView TextTip = view.findViewWithTag("tip");
        TextInputLayout AccountInputLayout = view.findViewWithTag("acc_put");
        TextInputEditText AccountInputEditText = view.findViewWithTag("acc_edt");
        TextInputLayout PasswordInputLayout = view.findViewWithTag("pwd_put");
        TextInputEditText PasswordInputEditText = view.findViewWithTag("pwd_edt");
        FancyButton LogInButton = view.findViewWithTag("login");
        FancyButton RegisteredButton = view.findViewWithTag("registered");
        ImageView QQLogIn = view.findViewWithTag("qq");
        TextView Retrieve = view.findViewWithTag("retrieve");

        Retrieve.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        Retrieve.getPaint().setAntiAlias(true);//抗锯齿;

        PasswordInputEditText.setText(DeviceIdUtil.getDeviceId());
        PasswordInputEditText.setEnabled(false);

        AccountInputEditText.setText((CharSequence) SpUtil.get("Account", ""));
        AccountInputEditText.setKeyListener(DigitsKeyListener.getInstance("0123456789abcdefghigklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_."));
        AccountInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (start < 5) {
                    AccountInputLayout.setError("最低输入6个字符");
                    AccountInputLayout.setErrorEnabled(true);
                } else if (start > 19) {
                    AccountInputLayout.setError("超过20个字符");
                    AccountInputLayout.setErrorEnabled(true);
                } else {
                    AccountInputLayout.setError("");
                    AccountInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        LogInButton.setOnClickListener(v -> {
            Editable editable = AccountInputEditText.getText();
            if (editable != null && !TextUtils.isEmpty(editable.toString())) {
                if (editable.toString().length() < 6) {
                    Toasty.info(this, "最低输入6个字符", Toast.LENGTH_SHORT, true).show();
                } else if (editable.toString().length() > 20) {
                    Toasty.info(this, "超过20个字符", Toast.LENGTH_SHORT, true).show();
                } else {
                    LogInButton.setEnabled(false);
                    RegisteredButton.setEnabled(false);
                    QQLogIn.setEnabled(false);
                    TextTip.setText("登录中…");
                    HelperCore.getInstance().login(editable.toString(), (e, userData) -> {
                        if (e == null) {
                            //Toasty.success(this, "登录成功！用户名" + userData.getUserName(), Toast.LENGTH_SHORT, true).show();
                            if (!TextUtils.isEmpty(userData.headUrl)) {
                                Picasso.get()
                                        .load(userData.headUrl)
                                        .into(HeadIcon);
                            }
                            HelperCore.getInstance().updateUserData(this);
                            SpUtil.put("Account", editable.toString());
                            Intent intent = new Intent();
                            intent.putExtra("login", true);
                            intent.putExtra("objectId", userData.objectId);
                            LogInActivity.this.setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            Toasty.error(this, "登录失败！" + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                        }
                        LogInButton.setEnabled(true);
                        RegisteredButton.setEnabled(true);
                        QQLogIn.setEnabled(true);
                        TextTip.setText("FMP用户您好");
                    });
                }
            } else {
                Toasty.info(this, "请输入账号", Toast.LENGTH_SHORT, true).show();
            }
        });
        RegisteredButton.setOnClickListener(v -> {
            Editable editable = AccountInputEditText.getText();
            if (editable != null || !TextUtils.isEmpty(editable.toString())) {
                if (editable.toString().length() < 6) {
                    Toasty.info(this, "最低输入6个字符", Toast.LENGTH_SHORT, true).show();
                } else if (editable.toString().length() > 20) {
                    Toasty.info(this, "超过20个字符", Toast.LENGTH_SHORT, true).show();
                } else {
                    v.setEnabled(false);
                    TextTip.setText("注册中…");
                    HelperCore.getInstance().register(editable.toString(), (e, userData) -> {
                        if (e == null) {
                            Toasty.success(this, "注册成功！请登录", Toast.LENGTH_SHORT, true).show();
                            SpUtil.put("Account", editable.toString());
                        } else {
                            Toasty.error(this, "注册失败！" + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                        }
                        v.setEnabled(true);
                        TextTip.setText("FMP用户您好");
                    });
                }
            } else {
                Toasty.info(this, "请输入账号", Toast.LENGTH_SHORT, true).show();
            }
        });
        QQLogIn.setOnClickListener(v -> {
            LogInButton.setEnabled(false);
            RegisteredButton.setEnabled(false);
            QQLogIn.setEnabled(false);
            TextTip.setText("登录中…");
            HelperCore.getInstance().login(this, (e, userData) -> {
                if (e == null) {
                    //Toasty.success(this, "登录成功！用户名" + userData.getUserName(), Toast.LENGTH_SHORT, true).show();
                    if (!TextUtils.isEmpty(userData.headUrl)) {
                        Picasso.get()
                                .load(userData.headUrl)
                                .into(HeadIcon);
                    }
                    HelperCore.getInstance().updateUserData(this);
                    Intent intent = new Intent();
                    intent.putExtra("login", true);
                    intent.putExtra("objectId", userData.objectId);
                    LogInActivity.this.setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toasty.error(this, "登录失败！" + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                }
                LogInButton.setEnabled(true);
                RegisteredButton.setEnabled(true);
                QQLogIn.setEnabled(true);
                TextTip.setText("FMP用户您好");
            });
        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("您确定要退出登录嘛？");
            builder.setNegativeButton("确定", (dialog, which) -> {
                Intent intent = new Intent();
                intent.putExtra("login", false);
                LogInActivity.this.setResult(RESULT_CANCELED, intent);
                finish();
            });
            builder.setPositiveButton("取消", null);
            builder.show();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        HelperCore.getInstance().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
