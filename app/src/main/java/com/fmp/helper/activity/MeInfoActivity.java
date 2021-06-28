package com.fmp.helper.activity;

import android.animation.Animator;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.billy.android.swipe.SmartSwipe;
import com.billy.android.swipe.consumer.ActivitySlidingBackConsumer;
import com.fmp.AppConfig;
import com.fmp.FMP_Tools;
import com.fmp.core.DeviceIdUtil;
import com.fmp.core.HelperCore;
import com.fmp.core.http.bean.HelperAccount;
import com.fmp.view.CircleImageView;
import com.squareup.picasso.Picasso;

import net.fmp.helper.R;

import es.dmoral.toasty.Toasty;
import mehdi.sakout.fancybuttons.FancyButton;
import per.goweii.anylayer.AnimatorHelper;
import per.goweii.anylayer.AnyLayer;
import per.goweii.anylayer.DialogLayer;
import per.goweii.anylayer.DragLayout;
import per.goweii.anylayer.Layer;

public class MeInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private CircleImageView UserPortrait;
    private TextView NickName;
    private TextView UserSex;
    private TextView UserType;
    private TextView UserId;
    private TextView UserQQ;
    private TextView DeviceUID;
    private TextView LaunchCount;
    private TextView RegisteredTime;

    /**
     * 根据用户名的不同长度，来进行替换 ，达到保密效果
     *
     * @param userName 用户名
     * @return 替换后的用户名
     */
    private String replaceWithStar(String userName) {
        String userNameAfterReplaced;
        int nameLength = userName.length();
        if (nameLength <= 1) {
            userNameAfterReplaced = "*";
        } else if (nameLength == 2) {
            userNameAfterReplaced = replaceAction(userName, "(?<=\\d{0})\\d(?=\\d{1})");
        } else if (nameLength <= 6) {
            userNameAfterReplaced = replaceAction(userName, "(?<=\\d{1})\\d(?=\\d{1})");
        } else if (nameLength == 7) {
            userNameAfterReplaced = replaceAction(userName, "(?<=\\d{1})\\d(?=\\d{2})");
        } else if (nameLength == 8) {
            userNameAfterReplaced = replaceAction(userName, "(?<=\\d{2})\\d(?=\\d{2})");
        } else if (nameLength == 9) {
            userNameAfterReplaced = replaceAction(userName, "(?<=\\d{2})\\d(?=\\d{3})");
        } else if (nameLength == 10) {
            userNameAfterReplaced = replaceAction(userName, "(?<=\\d{3})\\d(?=\\d{3})");
        } else {
            userNameAfterReplaced = replaceAction(userName, "(?<=\\d{3})\\d(?=\\d{4})");
        }
        return userNameAfterReplaced;
    }

    /**
     * 实际替换动作
     *
     * @param username username
     * @param regular  正则
     */
    private String replaceAction(String username, String regular) {
        return username.replaceAll(regular, "*");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_me_info);
        Toolbar toolbar = findViewById(R.id.toolbar_me_info);
        toolbar.setTitle("个人信息");
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

        LinearLayout HeadImageLayout = findViewById(R.id.me_info_head_image_layout);
        LinearLayout NickNameLayout = findViewById(R.id.me_info_nick_name_layout);
        LinearLayout UserSexLayout = findViewById(R.id.me_info_sex_layout);
        LinearLayout UserTypeLayout = findViewById(R.id.me_info_type_layout);
        LinearLayout UserIdLayout = findViewById(R.id.me_info_id_layout);
        LinearLayout UserQQLayout = findViewById(R.id.me_info_qq_layout);
        LinearLayout DeviceUIDLayout = findViewById(R.id.me_info_uid_layout);
        LinearLayout LaunchCountLayout = findViewById(R.id.me_info_launch_layout);
        LinearLayout RegisteredTimeLayout = findViewById(R.id.me_info_time_layout);

        UserPortrait = findViewById(R.id.me_info_head_image);
        NickName = findViewById(R.id.me_info_nick_name);
        UserSex = findViewById(R.id.me_info_sex);
        UserType = findViewById(R.id.me_info_type);
        UserId = findViewById(R.id.me_info_id);
        UserQQ = findViewById(R.id.me_info_qq);
        DeviceUID = findViewById(R.id.me_info_uid);
        LaunchCount = findViewById(R.id.me_info_launch);
        RegisteredTime = findViewById(R.id.me_info_time);
        FancyButton Logout = findViewById(R.id.me_info_logout);
        FancyButton UseCDK = findViewById(R.id.me_info_cdk);

        HeadImageLayout.setOnClickListener(this);
        NickNameLayout.setOnClickListener(this);
        UserSexLayout.setOnClickListener(this);
        UserTypeLayout.setOnClickListener(this);
        UserIdLayout.setOnClickListener(this);
        UserQQLayout.setOnClickListener(this);
        DeviceUID.setOnClickListener(this);
        DeviceUIDLayout.setOnClickListener(this);
        LaunchCountLayout.setOnClickListener(this);
        LaunchCount.setOnClickListener(this);
        RegisteredTimeLayout.setOnClickListener(this);
        Logout.setOnClickListener(this);
        UseCDK.setOnClickListener(this);

        onRefreshData(HelperCore.getInstance().getUserData());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.me_info_head_image_layout:
                HelperCore.getInstance().uploadPortrait(this, (e, imageUrl) -> {
                    if (e == null) {
                        Picasso.get()
                                .load(imageUrl)
                                .into(UserPortrait);
                        Toasty.normal(this, "更新头像成功").show();
                    } else {
                        Toasty.error(this, "更新头像失败！" + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                    }
                });
                break;
            case R.id.me_info_nick_name_layout:
                AnyLayer.dialog(this)
                        .contentView(R.layout.dialog_edit)
                        .backgroundDimDefault()
                        .gravity(Gravity.CENTER)
                        .dragDismiss(DragLayout.DragStyle.Bottom)
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(View content) {
                                return AnimatorHelper.createBottomInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(View content) {
                                return AnimatorHelper.createBottomOutAnim(content);
                            }
                        })
                        .onVisibleChangeListener(new Layer.OnVisibleChangeListener() {
                            @Override
                            public void onShow(Layer layer) {
                                ((EditText) layer.getView(R.id.et_dialog_content)).setText(NickName.getText());
                                ((TextView) layer.getView(R.id.tv_dialog_title)).setText("修改昵称");
                                ((TextView) layer.getView(R.id.tv_dialog_no)).setText("取消");
                                ((TextView) layer.getView(R.id.tv_dialog_yes)).setText("确定");
                            }

                            @Override
                            public void onDismiss(Layer layer) {
                                DialogLayer dialogLayer = (DialogLayer) layer;
                                dialogLayer.removeSoftInput();
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_no)
                        .onClick((anyLayer, view) -> {
                            anyLayer.dismiss();
                            EditText editText = anyLayer.getView(R.id.et_dialog_content);
                            String nickName = editText.getText().toString().trim();
                            if (nickName.length() <= 20) {
                                HelperCore.getInstance().updateNickNme(nickName, (e, nickName1) -> {
                                    if (e == null) {
                                        NickName.setText(nickName1);
                                        Toasty.normal(MeInfoActivity.this, "修改昵称成功").show();
                                    } else {
                                        Toasty.error(MeInfoActivity.this, "修改昵称失败！" + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                                    }
                                });
                            } else {
                                Toasty.warning(MeInfoActivity.this, "昵称不要超过20字符哦", Toast.LENGTH_SHORT, true).show();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.me_info_sex_layout:
                AnyLayer.dialog(this)
                        .contentView(R.layout.dialog_normal_sex)
                        .backgroundDimDefault()
                        .gravity(Gravity.CENTER)
                        .dragDismiss(DragLayout.DragStyle.Bottom)
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(View content) {
                                return AnimatorHelper.createBottomInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(View content) {
                                return AnimatorHelper.createBottomOutAnim(content);
                            }
                        })
                        .onVisibleChangeListener(new Layer.OnVisibleChangeListener() {
                            @Override
                            public void onShow(Layer layer) {
                                ((TextView) layer.getView(R.id.tv_dialog_title)).setText("修改性别");
                                ((TextView) layer.getView(R.id.tv_dialog_sex_male)).setText("小哥哥");
                                ((TextView) layer.getView(R.id.tv_dialog_sex_female)).setText("小姐姐");
                            }

                            @Override
                            public void onDismiss(Layer layer) {
                                DialogLayer dialogLayer = (DialogLayer) layer;
                                dialogLayer.removeSoftInput();
                            }
                        })
                        .onClick((anyLayer, view) -> {
                            anyLayer.dismiss();
                            HelperCore.getInstance().updateSex(1, (e, sex, sexName) -> {
                                if (e == null) {
                                    UserSex.setText(sexName);
                                    Toasty.normal(MeInfoActivity.this, "修改性别成功").show();
                                } else {
                                    Toasty.error(MeInfoActivity.this, "修改性别失败！" + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                                }
                            });
                        }, R.id.tv_dialog_sex_male)
                        .onClick((anyLayer, view) -> {
                            anyLayer.dismiss();
                            HelperCore.getInstance().updateSex(0, (e, sex, sexName) -> {
                                if (e == null) {
                                    UserSex.setText(sexName);
                                    Toasty.normal(MeInfoActivity.this, "修改性别成功").show();
                                } else {
                                    Toasty.error(MeInfoActivity.this, "修改性别失败！" + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                                }
                            });
                        }, R.id.tv_dialog_sex_female)
                        .show();
                break;
            case R.id.me_info_id_layout:
                try {
                    //获取剪贴板管理器：
                    ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    // 创建普通字符型ClipData
                    ClipData mClipData = ClipData.newPlainText("Label", UserId.getText());
                    // 将ClipData内容放到系统剪贴板里。
                    cm.setPrimaryClip(mClipData);
                    Toasty.normal(MeInfoActivity.this, "已复制ID").show();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.me_info_qq_layout: {
                HelperAccount userData = HelperCore.getInstance().getUserData();
                if (userData != null) {
                    if (!TextUtils.isEmpty(userData.openId)) {
                        Toasty.normal(MeInfoActivity.this, "已绑定QQ，如需解绑请联系我们").show();
                    } else {
                        HelperCore.getInstance().updateTencentOpenId(this, (e, token, expires, openId) -> {
                            if (e == null) {
                                onRefreshData(HelperCore.getInstance().getUserData());
                                Toasty.normal(MeInfoActivity.this, "绑定QQ成功").show();
                            } else {
                                Toasty.error(MeInfoActivity.this, "绑定QQ失败！" + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                            }
                        });
                    }
                }
                break;
            }
            case R.id.me_info_uid:
            case R.id.me_info_uid_layout:
                try {
                    //获取剪贴板管理器：
                    ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    // 创建普通字符型ClipData
                    ClipData mClipData = ClipData.newPlainText("Label", DeviceIdUtil.getDeviceId());
                    // 将ClipData内容放到系统剪贴板里。
                    cm.setPrimaryClip(mClipData);
                    Toasty.normal(MeInfoActivity.this, "已复制设备标识").show();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.me_info_launch:
            case R.id.me_info_launch_layout:
                Toasty.normal(MeInfoActivity.this, String.format("真棒！您已经启动%s啦~ %s", LaunchCount.getText(), AppConfig.getEmoji())).show();
                break;
            case R.id.me_info_logout:
                if (HelperCore.getInstance().logout(this)) {
                    Toasty.normal(this, "已退出登录").show();
                }
                finish();
                break;
            case R.id.me_info_cdk:
                AnyLayer.dialog(this)
                        .contentView(R.layout.dialog_edit)
                        .backgroundDimDefault()
                        .gravity(Gravity.CENTER)
                        .dragDismiss(DragLayout.DragStyle.Bottom)
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(View content) {
                                return AnimatorHelper.createBottomInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(View content) {
                                return AnimatorHelper.createBottomOutAnim(content);
                            }
                        })
                        .onVisibleChangeListener(new Layer.OnVisibleChangeListener() {
                            @Override
                            public void onShow(Layer layer) {
                                ((TextView) layer.getView(R.id.tv_dialog_title)).setText("请输入CDK");
                                ((TextView) layer.getView(R.id.tv_dialog_no)).setText("取消");
                                ((TextView) layer.getView(R.id.tv_dialog_yes)).setText("确定");
                            }

                            @Override
                            public void onDismiss(Layer layer) {
                                DialogLayer dialogLayer = (DialogLayer) layer;
                                dialogLayer.removeSoftInput();
                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_no)
                        .onClick((anyLayer, view) -> {
                            EditText editText = anyLayer.getView(R.id.et_dialog_content);
                            String cdk = editText.getText().toString().trim();
                            if (TextUtils.isEmpty(cdk)) {
                                Toasty.info(MeInfoActivity.this, "您输入的内容为空", Toast.LENGTH_SHORT, true).show();
                            } else {
                                HelperCore.getInstance().findCDK(cdk, (e, userData) -> {
                                    if (e == null) {
                                        anyLayer.dismiss();
                                        onRefreshData(userData);
                                        Toasty.normal(MeInfoActivity.this, "激活成功").show();
                                    } else {
                                        Toasty.error(MeInfoActivity.this, "激活失败！" + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                                    }
                                });
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
        }
    }

    private void onRefreshData(HelperAccount userData) {
        if (userData != null) {
            if (!TextUtils.isEmpty(userData.headUrl))
                Picasso.get()
                        .load(userData.headUrl)
                        .into(UserPortrait);
            NickName.setText(userData.userName);
            UserSex.setText(FMP_Tools.getSexString(userData.userSex));
            if (userData.id != 0) {
                UserId.setText(String.valueOf(userData.id));
            } else {
                UserId.setText("无");
            }
            UserType.setText(userData.userType);
            if (!TextUtils.isEmpty(userData.openId)) {
                if (!TextUtils.isEmpty(userData.qq)) {
                    UserQQ.setText(String.format("已绑定%s", replaceWithStar(String.valueOf(userData.qq))));
                } else {
                    UserQQ.setText("已绑定");
                }
            } else {
                UserQQ.setText("未绑定");
            }
            DeviceUID.setText(DeviceIdUtil.getDeviceId());
            LaunchCount.setText(String.format("%d次", userData.launchCount));
            RegisteredTime.setText(userData.createdAt);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        HelperCore.getInstance().onActivityResult(requestCode, resultCode, data);
    }
}
