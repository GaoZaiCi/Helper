package com.fmp.helper;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.fmp.FMP_Toast;
import com.fmp.FMP_Tools;
import com.fmp.core.CoreException;
import com.fmp.core.GameLauncher;
import com.fmp.core.HelperCore;
import com.fmp.core.HelperNative;
import com.fmp.core.push.ClientPush;
import com.fmp.core.push.PushFunction;
import com.fmp.core.push.PushMessage;
import com.fmp.data.AmericanDynamics;
import com.fmp.data.LauncherSetting;
import com.fmp.dialog.HelperDialog;
import com.fmp.helper.activity.ArchiveActivity;
import com.fmp.helper.activity.GameLobbyActivity;
import com.fmp.helper.activity.ModMallActivity;
import com.fmp.helper.activity.SeedsActivity;
import com.fmp.util.SpUtil;
import com.fmp.view.SmartScrollView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.jian.explosion.animation.ExplosionField;
import com.lany.banner.BannerView;
import com.lany.banner.SimpleBannerAdapter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.zj.lib.noviceguide.NoviceGuide;
import com.zj.lib.noviceguide.NoviceGuideSet;
import com.zzhoujay.richtext.RichText;

import net.fmp.helper.R;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class MainFrameTab1 extends Fragment implements View.OnLongClickListener {
    //界面布局
    private View view;
    private NestedScrollView scrollView;
    private FloatingActionButton startGameButton;
    private CardView cardView_FastSetup;
    //各个按钮
    private Button Main_Tab1Item1_Tab1, Main_Tab1Item1_Tab2, Main_Tab1Item1_Tab3, Main_Tab1Item1_Tab4, Main_Tab_InfoBtn, Main_Tab_Set;
    private Switch DEBUGMODE, SAFEMODE, GAMEFLOAT, MUDWFLOAT;
    private TextView DuoWanIDText;
    private TextView GameVersionText;
    private TextView StartModeText;

    private PushMessage pushMessage;
    private int duration = 1000;

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.main_frame_tab1, null, false);

            scrollView = view.findViewById(R.id.main_tab1_nestedview);
            BannerView bannerView = view.findViewById(R.id.banner_view);

            ClientPush.getInstance().checkAmericanDynamics(list -> {
                //对banner的数据源list的数据结构不做要求，BannerItem可替换成你自己的数据结构
                bannerView.setAdapter(new SimpleBannerAdapter<AmericanDynamics.AmericanDynamicsItem>(list) {

                    @Override
                    public void bindImage(ImageView imageView, AmericanDynamics.AmericanDynamicsItem item) {
                        //这里处理图片的数据绑定，可以使用任意一种图片加载框架
                        if (!TextUtils.isEmpty(item.getImage())) {
                            Picasso.get()
                                    .load(item.getImage())
                                    .transform(new Transformation() {
                                        @Override
                                        public Bitmap transform(Bitmap source) {
                                            int widthLight = source.getWidth();
                                            int heightLight = source.getHeight();
                                            Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
                                            Canvas canvas = new Canvas(output);
                                            Paint paintColor = new Paint();
                                            paintColor.setFlags(Paint.ANTI_ALIAS_FLAG);
                                            RectF rectF = new RectF(new Rect(0, 0, widthLight, heightLight));
                                            canvas.drawRoundRect(rectF, widthLight / 40, heightLight / 40, paintColor);//这里除的数越大角度越小
                                            Paint paintImage = new Paint();
                                            paintImage.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
                                            canvas.drawBitmap(source, 0, 0, paintImage);
                                            source.recycle();
                                            return output;
                                        }

                                        @Override
                                        public String key() {
                                            return "roundcorner";
                                        }
                                    })
                                    .into(imageView);
                        }
                    }

                    @Override
                    public void bindTitle(TextView titleText, AmericanDynamics.AmericanDynamicsItem item) {
                        //这里处理标题内容的绑定。如果不需要显示标题，可不做处理
                        titleText.setText(item.getName());
                    }

                    @Override
                    public void onItemClicked(int position, AmericanDynamics.AmericanDynamicsItem item) {
                        //如果需要处理banner的点击事件，这里处理
                        try {
                            if (item.getUrl().startsWith("mqqopensdkapi")) {
                                Intent intent = new Intent();
                                intent.setData(Uri.parse(item.getUrl()));
                                // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面
                                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                FMP_Toast.Show_Toast(getContext(), "跳转中...");
                                getActivity().startActivity(intent);
                            } else if (item.getUrl().startsWith("http")) {
                                FMP_Toast.Show_Toast(getContext(), "打开网页...");
                                Uri uri = Uri.parse(item.getUrl());
                                Intent intent2 = new Intent(Intent.ACTION_VIEW, uri);
                                getActivity().startActivity(intent2);
                            }
                        } catch (Exception e) {
                            FMP_Toast.Show_Toast(getContext(), "跳转失败");
                        }
                    }
                });
            });
            //网络获取公告
            //实例化公告栏
            TextView noticeTip = view.findViewById(R.id.main_tab1_notification_tip);
            TextView noticeMessage = view.findViewById(R.id.main_tab1_notification_message);

            ClientPush.getInstance().checkMainPush(new ClientPush.onMainPushListener() {
                @Override
                public void onMessage(PushMessage push) {
                    getActivity().runOnUiThread(() -> {
                        pushMessage = push;
                        noticeTip.setText(pushMessage.getTitle());
                        RichText.fromHtml(pushMessage.getMessage()).into(noticeMessage);
                        Main_Tab_InfoBtn.setText("查看更多");
                    });
                }

                @Override
                public void onError(CoreException e) {
                    getActivity().runOnUiThread(() -> {
                        noticeTip.setText("系统公告");
                        noticeMessage.setText("公告被吃啦(*´艸`*)ヽ(´･д･｀)ﾉ");
                        Main_Tab_InfoBtn.setText("重新获取公告");
                    });
                }
            });

            startGameButton = view.findViewById(R.id.main_tab1_fab);
            startGameButton.setOnClickListener(v -> {
                //破碎
                new ExplosionField(getContext()).explode(v, new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                });
                //隐藏
                startGameButton.setVisibility(View.GONE);
                Snackbar.make(view, "正在启动游戏…", 1000 * 3).setAction("取消", v2 -> {
                    startGameButton.setVisibility(View.VISIBLE);
                    GameLauncher.getInstance().stopStartGame();
                    Snackbar.make(v2, "已取消启动", 500).show();
                }).show();
                GameLauncher.getInstance().checkStartGame((e, start, code) -> {
                    if (e == null) {
                        if (start) {
                            FMP_Toast.BM_Toast(getContext(), "启动成功，祝您游戏愉快~", true);
                        }
                    } else {
                        if (!start) {
                            Snackbar.make(view, "启动失败！" + e.getMessage(), 500).show();
                        } else {
                            FMP_Toast.BM_Toast(getContext(), e.getMessage(), false);
                        }

                    }
                    startGameButton.setVisibility(View.VISIBLE);
                });
            });

            CardView cardView_QuickSetup = view.findViewById(R.id.main_tab1_cardview_quick_setup);
            cardView_QuickSetup.setOnLongClickListener(v -> {
                //new ExplosionField(getContext()).explode(v, null);
                setSelfAndChildDisappearAndAppearOnClick(v);
                return false;
            });
            CardView cardView_AppTip = view.findViewById(R.id.main_tab1_cardview_app_tip);
            cardView_AppTip.setOnLongClickListener(v -> {
                setSelfAndChildDisappearOnClick(v);
                return false;
            });

            Main_Tab1Item1_Tab1 = view.findViewById(R.id.mainframetab1ButtonGameLobby);
            Main_Tab1Item1_Tab1.setOnClickListener(p1 -> {
                Intent intent = new Intent();
                intent.setClass(getActivity(), GameLobbyActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(intent);
                //getActivity().overridePendingTransition(android.R.anim.slide_in_left, 0);
            });

            Main_Tab1Item1_Tab2 = view.findViewById(R.id.main_frame_tab1_archive);
            Main_Tab1Item1_Tab2.setOnClickListener(p1 -> {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ArchiveActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(intent);
                //getActivity().overridePendingTransition(android.R.anim.fade_in, 0);
            });

            Main_Tab1Item1_Tab3 = view.findViewById(R.id.main_frame_tab1_mod_mall);
            Main_Tab1Item1_Tab3.setOnClickListener(p1 -> {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ModMallActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(intent);
                //getActivity().overridePendingTransition(android.R.anim.fade_in, 0);
            });

            Main_Tab1Item1_Tab4 = view.findViewById(R.id.mainframetab1ButtonSeeds);
            Main_Tab1Item1_Tab4.setOnClickListener(p1 -> {
                Intent intent = new Intent();
                intent.setClass(getActivity(), SeedsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(intent);
                //getActivity().overridePendingTransition(android.R.anim.fade_in, 0);
            });

            CardView cardView_Notification = view.findViewById(R.id.main_tab1_cardview_notification);
            cardView_Notification.setOnLongClickListener(v -> {
                //new ExplosionField(getContext()).explode(v, null);
                setSelfAndChildDisappearAndAppearOnClick(v);
                return false;
            });
            //查看公告内容
            Main_Tab_InfoBtn = view.findViewById(R.id.main_tab1_notification_button);
            Main_Tab_InfoBtn.setOnClickListener(p1 -> {
                try {
                    if (pushMessage != null) {
                        HelperDialog dialog = new HelperDialog(getContext());
                        dialog.setTitle(pushMessage.getTitle());
                        dialog.setMessage(pushMessage.getMessage());
                        RichText.fromHtml(pushMessage.getMessage()).into(dialog.getMessageView());
                        dialog.setButton3("OK", v -> {
                            dialog.dismiss();
                        });
                        if (pushMessage.isQuit()) {
                            dialog.getTitleView().setOnLongClickListener(v -> {
                                dialog.dismiss();
                                return false;
                            });
                        } else {
                            dialog.getButton3View().setVisibility(View.GONE);
                        }
                        dialog.getLayoutView().measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), 0);
                        if (dialog.getLayoutView().getMeasuredHeight() > dialog.getMessageView().getMeasuredHeight()) {
                            dialog.getButton3View().setEnabled(true);
                        } else {
                            dialog.getButton3View().setEnabled(false);
                            dialog.getScrollView().setScanScrollChangedListener(new SmartScrollView.ISmartScrollChangedListener() {
                                @Override
                                public void onScrolledToBottom() {
                                    dialog.getButton3View().setEnabled(true);
                                }

                                @Override
                                public void onScrolledToTop() {
                                    dialog.getButton3View().setEnabled(false);
                                }
                            });
                            new Handler().postDelayed(() -> {
                                dialog.getScrollView().fullScroll(ScrollView.FOCUS_DOWN);//滑到底部
                                //scrollView.fullScroll(ScrollView.FOCUS_UP);//滑到顶部
                                dialog.getButton3View().setEnabled(true);
                            }, 3000);
                        }
                        if (pushMessage.getPictures() != null) {
                            for (String picture : pushMessage.getPictures()) {
                                ImageView view = new ImageView(getContext());
                                Picasso.get()
                                        .load(picture)
                                        .into(view);
                                dialog.addView(view);
                            }
                        }
                        dialog.setCancelable(pushMessage.isQuit());
                        dialog.show();
                    } else {
                        FMP_Toast.Show_Toast(getContext(), "正在重新获取公告");
                        ClientPush.getInstance().checkMainPush(new ClientPush.onMainPushListener() {
                            @Override
                            public void onMessage(PushMessage push) {
                                getActivity().runOnUiThread(() -> {
                                    pushMessage = push;
                                    noticeTip.setText(pushMessage.getTitle());
                                    RichText.fromHtml(pushMessage.getMessage()).into(noticeMessage);
                                    Main_Tab_InfoBtn.setText("查看更多");
                                });
                            }

                            @Override
                            public void onError(CoreException e) {
                                getActivity().runOnUiThread(() -> {
                                            FMP_Toast.Show_Toast(getContext(), "获取公告失败！" + e.getMessage());
                                            Main_Tab_InfoBtn.setText("重新获取公告");
                                        }
                                );
                            }
                        });
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            });

            cardView_FastSetup = view.findViewById(R.id.main_tab1_cardview_fast_setup);
            cardView_FastSetup.setOnLongClickListener(this);

            Main_Tab_Set = view.findViewById(R.id.main_tab1_duowan_id);
            Main_Tab_Set.setOnClickListener(p1 -> {
                try {
                    View layout = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_setdwid, null, false);
                    final Dialog dialog = new Dialog(getActivity());
                    //设置弹窗显示布局
                    dialog.setContentView(layout);
                    //设置点击外部返回焦点
                    //dialog.setCancelable(false);
                    //设置点击外部不可关闭弹窗
                    //dialog.setCanceledOnTouchOutside(false);
                    //设置窗体透明背景
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    //获取弹窗的窗口样式
                    WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                    //设置窗口样式的宽
                    lp.width = getActivity().getWindowManager().getDefaultDisplay().getWidth() / 2 +
                            getActivity().getWindowManager().getDefaultDisplay().getWidth() / 3;
                    //设置弹窗应用窗口样式
                    dialog.getWindow().setAttributes(lp);
                    //显示弹窗
                    dialog.show();

                    LauncherSetting setting = LauncherSetting.getSetting();

                    final EditText editText = layout.findViewById(R.id.dialogsetdwidEditText1);
                    //获取多玩ID
                    editText.setText(setting.getMcboxId());
                    layout.findViewById(R.id.dialogsetdwidButton1).setOnClickListener(new OnClickListener() {
                        public void onClick(View p1) {
                            dialog.dismiss();
                        }
                    });
                    layout.findViewById(R.id.dialogsetdwidButton2).setOnClickListener(new OnClickListener() {
                        public void onClick(View p1) {
                            String text = editText.getText().toString();
                            if (TextUtils.isEmpty(text)) {
                                DuoWanIDText.setText("未设置");
                            } else {
                                DuoWanIDText.setText(editText.getText().toString());
                            }
                            setting.setMcboxId(editText.getText().toString());
                            setting.saveSetting();
                            dialog.dismiss();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });


            Button GameVersion = view.findViewById(R.id.main_tab1_game_version);
            GameVersion.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    try {
                        View layout = getLayoutInflater().from(getActivity()).inflate(R.layout.dialog_packgae, null, false);
                        final Dialog dialog = new Dialog(getActivity());
                        //设置弹窗显示布局
                        dialog.setContentView(layout);
                        //设置点击外部返回焦点
                        //dialog.setCancelable(false);
                        //设置点击外部不可关闭弹窗
                        //dialog.setCanceledOnTouchOutside(false);
                        //设置窗体透明背景
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        //获取弹窗的窗口样式
                        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                        //设置窗口样式的宽
                        lp.width = getActivity().getWindowManager().getDefaultDisplay().getWidth() / 2 +
                                getActivity().getWindowManager().getDefaultDisplay().getWidth() / 3;
                        //设置弹窗应用窗口样式
                        dialog.getWindow().setAttributes(lp);
                        //显示弹窗
                        dialog.show();
                        final EditText text = layout.findViewById(R.id.dialogpackgaeEditText1);
                        //获取当前版本
                        GameLauncher.ApkInfo type = GameLauncher.getInstance().getCurrentVersion();
                        if (type != null) {
                            text.setText(type.getPackageName());
                        }
                        //选择包名
                        layout.findViewById(R.id.dialogpackgaeButton1).setOnClickListener(p1 -> {
                            GameLauncher.getInstance().selectionMcVersion(getContext(), type1 -> GameVersionText.setText(type1.getChannelName()));
                            dialog.dismiss();
                        });
                        //确定按钮
                        layout.findViewById(R.id.dialogpackgaeButton2).setOnClickListener(p1 -> {
                            try {
                                GameLauncher.getInstance().setCustomVersion(text.getText().toString());
                                GameVersionText.setText(GameLauncher.getInstance().getCurrentVersion().getChannelName());
                                FMP_Toast.BM_Toast(getContext(), "设置成功", true);
                                dialog.dismiss();
                            } catch (CoreException e) {
                                FMP_Toast.BM_Toast(getContext(), e.getMessage(), false);
                            }
                        });
                        //取消按钮
                        layout.findViewById(R.id.dialogpackgaeButton3).setOnClickListener(p1 -> dialog.dismiss());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            LauncherSetting setting = LauncherSetting.getSetting();
            DuoWanIDText = view.findViewById(R.id.main_tab1_duowan_id_text);
            GameVersionText = view.findViewById(R.id.main_tab1_game_version_text);
            StartModeText = view.findViewById(R.id.main_tab1_start_mode_text);

            if (TextUtils.isEmpty(setting.getMcboxId()) || setting.getMcboxId().equals("-1")) {
                DuoWanIDText.setText("未设置");
            } else {
                DuoWanIDText.setText(setting.getMcboxId());
            }
            GameVersionText.setText(GameLauncher.getInstance().getCurrentVersion().getChannelName());
            StartModeText.setText(String.format("加载模式%s", setting.getLoadMode() == 0 ? "一" : setting.getLoadMode() == 1 ? "二" : "三"));

            Button StartMode = view.findViewById(R.id.main_tab1_start_mode);
            StartMode.setOnClickListener(v -> {
                //选择启动模式
                GameLauncher.getInstance().selectionLoadMode(getContext(), (loadModeName, message) -> {
                    if (!TextUtils.isEmpty(loadModeName))
                        StartModeText.setText(loadModeName);
                    if (!TextUtils.isEmpty(message))
                        Snackbar.make(view, message, 1000).show();
                });
            });

            DEBUGMODE = view.findViewById(R.id.main_tab1_debug_switch);
            SAFEMODE = view.findViewById(R.id.main_tab1_safe_switch);
            MUDWFLOAT = view.findViewById(R.id.main_tab1_dwfloat_switch);
            GAMEFLOAT = view.findViewById(R.id.main_tab1_gamefloat_switch);
            //加载设置
            DEBUGMODE.setChecked(setting.isDeBugMode());
            SAFEMODE.setChecked(setting.isSafeMode());
            MUDWFLOAT.setChecked(setting.onMudwFloat());
            GAMEFLOAT.setChecked(setting.onGameFloat());

            DEBUGMODE.setOnCheckedChangeListener((buttonView, isChecked) -> {
                LauncherSetting setting15 = LauncherSetting.getSetting();
                setting15.setDeBugMode(isChecked);
                setting15.saveSetting();
                CoreException.setDeBugMode(isChecked);
            });

            SAFEMODE.setOnCheckedChangeListener((buttonView, isChecked) -> {
                LauncherSetting setting13 = LauncherSetting.getSetting();
                setting13.setSafeMode(isChecked);
                setting13.saveSetting();
            });

            MUDWFLOAT.setOnCheckedChangeListener((buttonView, isChecked) -> {
                PushFunction pushFunction = PushFunction.getData();
                LauncherSetting setting12 = LauncherSetting.getSetting();
                setting12.setMudwFloat(isChecked);
                setting12.saveSetting();
                /*int level = HelperCore.getInstance().getUserAbility();
                if (pushFunction.isMudwFloat() || level >= 2) {
                    setting12.setMudwFloat(isChecked);
                    setting12.saveSetting();
                } else {
                    Snackbar.make(view, "当前功能未启用", 3000).show();
                    if (isChecked) {
                        buttonView.setChecked(false);
                        setting12.setMudwFloat(false);
                        setting12.saveSetting();
                    }
                }*/
                /*if (HelperCore.getInstance().getUserAbility() >= 1) {
                    setting12.setMudwFloat(isChecked);
                    setting12.saveSetting();
                } else {
                    if (isChecked) {
                        buttonView.setChecked(false);
                        setting12.setMudwFloat(false);
                        setting12.saveSetting();
                        //需要捐赠授权才能使用这个功能哦
                        Snackbar.make(view, FMP_Tools.IntArrayToString(new int[]{38671, 35216, 25439, 36207, 25495, 26450, 25180, 33036, 20366, 30007, 36840, 20025, 21166, 33036, 21749}), 3000).setAction(FMP_Tools.IntArrayToString(new int[]{35814, 38386, 25484, 26439}), v -> {//询问授权
                            HelperCore.getInstance().joinQQGroup(getActivity(), "ntnkXYoCGaEXhiPNcgGcwflF6ft99Zq4");
                        }).show();
                    }
                }*/
            });

            GAMEFLOAT.setOnCheckedChangeListener((buttonView, isChecked) -> {
                PushFunction pushFunction = PushFunction.getData();
                LauncherSetting setting1 = LauncherSetting.getSetting();
                setting1.setGameFloat(isChecked);
                setting1.saveSetting();
                /*int level = HelperCore.getInstance().getUserAbility();
                if (pushFunction.isGameFloat() || level >= 1) {
                    if (level >= 1) {
                        setting1.setGameFloat(isChecked);
                        setting1.saveSetting();
                    } else {
                        if (isChecked) {
                            buttonView.setChecked(false);
                            setting1.setGameFloat(false);
                            setting1.saveSetting();
                            //需要捐赠授权才能使用这个功能哦
                            Snackbar.make(view, FMP_Tools.IntArrayToString(new int[]{38671, 35216, 25439, 36207, 25495, 26450, 25180, 33036, 20366, 30007, 36840, 20025, 21166, 33036, 21749}), 3000).setAction(FMP_Tools.IntArrayToString(new int[]{35814, 38386, 25484, 26439}), v -> {//询问授权
                                HelperCore.getInstance().joinQQGroup(getActivity(), "ntnkXYoCGaEXhiPNcgGcwflF6ft99Zq4");
                            }).show();
                        }
                    }
                } else {
                    Snackbar.make(view, "当前功能未启用", 3000).show();
                    if (isChecked) {
                        buttonView.setChecked(false);
                        setting1.setGameFloat(false);
                        setting1.saveSetting();
                    }
                }*/
            });
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            int width = dm.widthPixels, height = dm.heightPixels;

            /*Animation Left = new TranslateAnimation(-width, 0, 0, 0);
            Left.setInterpolator(getActivity(), android.R.anim.anticipate_overshoot_interpolator);
            Left.setFillAfter(false);
            Animation Right = new TranslateAnimation(width, 0, 0, 0);
            Right.setInterpolator(getActivity(), android.R.anim.anticipate_overshoot_interpolator);
            Right.setFillAfter(false);
            Animation Top = new TranslateAnimation(0, 0, -height, 0);
            Top.setInterpolator(getActivity(), android.R.anim.anticipate_overshoot_interpolator);
            Top.setFillAfter(false);
            Animation Bottom = new TranslateAnimation(0, 0, height, 0);
            Bottom.setInterpolator(getActivity(), android.R.anim.anticipate_overshoot_interpolator);
            Bottom.setFillAfter(false);*/

            LinearLayout layout = view.findViewById(R.id.main_tab1_main_layout);
            for (int i = 0; layout.getChildCount() > i; i++) {
                startAnimation(layout.getChildAt(i), width);
            }

            boolean bool = (boolean) SpUtil.get("NoviceGuide", false);
            if (!bool)
                showNoviceGuide();
        }
        return view;
    }

    private void startAnimation(View view, int width) {
        Animation Left = new TranslateAnimation(-width, 0, 0, 0);
        Left.setInterpolator(getActivity(), android.R.anim.anticipate_overshoot_interpolator);
        Left.setFillAfter(false);
        Animation Right = new TranslateAnimation(width, 0, 0, 0);
        Right.setInterpolator(getActivity(), android.R.anim.anticipate_overshoot_interpolator);
        Right.setFillAfter(false);
        Random random = new Random();
        if (random.nextBoolean()) {
            Left.setDuration(duration);
            view.startAnimation(Left);
        } else {
            Right.setDuration(duration);
            view.startAnimation(Right);
        }
        duration += 500;
    }

    private void showNoviceGuide() {
        NoviceGuide archive = new NoviceGuide.Builder(getActivity())
                .focusView(view.findViewById(R.id.main_frame_tab1_archive))
                .setPadding(5, 5, 5, 5)
                .setRadius(15)
                .setRelyActivity(getActivity())
                .setLayout(R.layout.main_frame_tab1_novice_guide, null)
                .setMessage(R.id.tv_notify, "在这里查看和管理您的游戏存档")
                .setPassId(R.id.iv_know)
                .build();

        NoviceGuide modMall = new NoviceGuide.Builder(getActivity())
                .focusView(view.findViewById(R.id.main_frame_tab1_mod_mall))
                .setPadding(5, 5, 5, 5)
                .setRadius(15)
                .setRelyActivity(getActivity())
                .setLayout(R.layout.main_frame_tab1_novice_guide, null)
                .setMessage(R.id.tv_notify, "在这里获取和更新最新插件")
                .setPassId(R.id.iv_know)
                .setDismissListener(() -> {
                    //滑动到最下面
                    scrollView.scrollTo(0, cardView_FastSetup.getBottom());
                    //隐藏启动按钮
                    CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) startGameButton.getLayoutParams();
                    int bottomMargin = layoutParams.bottomMargin;
                    startGameButton.animate().translationY(startGameButton.getHeight() + bottomMargin + 200).setInterpolator(new LinearInterpolator()).start();
                })
                .build();

        NoviceGuide duoWanId = new NoviceGuide.Builder(getActivity())
                .focusView(view.findViewById(R.id.main_tab1_duowan_id))
                .setPadding(5, 5, 5, 5)
                .setRadius(15)
                .setRelyActivity(getActivity())
                .setLayout(R.layout.main_frame_tab1_novice_guide, null)
                .setMessage(R.id.tv_notify, "您可以在这里设置您的多玩盒子ID，如果您没有使用过盒子可以选择不填，如果您填写了会自动同步盒子与中国版的资源")
                .setPassId(R.id.iv_know)
                .build();

        NoviceGuide gameVersion = new NoviceGuide.Builder(getActivity())
                .focusView(view.findViewById(R.id.main_tab1_game_version))
                .setPadding(5, 5, 5, 5)
                .setRadius(15)
                .setRelyActivity(getActivity())
                .setLayout(R.layout.main_frame_tab1_novice_guide, null)
                .setMessage(R.id.tv_notify, "在这里可以选择您的游戏版本和自定义版本")
                .setPassId(R.id.iv_know)
                .build();

        NoviceGuide startMode = new NoviceGuide.Builder(getActivity())
                .focusView(view.findViewById(R.id.main_tab1_start_mode))
                .setPadding(5, 5, 5, 5)
                .setRadius(15)
                .setRelyActivity(getActivity())
                .setLayout(R.layout.main_frame_tab1_novice_guide, null)
                .setMessage(R.id.tv_notify, "在这里可以切换不同的插件加载模式，不同的模式加载出来的插件可能会不一样")
                .setPassId(R.id.iv_know)
                .build();

        NoviceGuide safeMode = new NoviceGuide.Builder(getActivity())
                .focusView(view.findViewById(R.id.main_tab1_safe_switch))
                .setPadding(10, 10, 10, 10)
                .setRadius(15)
                .setRelyActivity(getActivity())
                .setLayout(R.layout.main_frame_tab1_novice_guide, null)
                .setMessage(R.id.tv_notify, "安全模式会禁用所有的插件加载，如非必需请不要打开")
                .setPassId(R.id.iv_know)
                .build();

        NoviceGuide duoWanFloat = new NoviceGuide.Builder(getActivity())
                .focusView(view.findViewById(R.id.main_tab1_dwfloat_switch))
                .setPadding(10, 10, 10, 10)
                .setRadius(15)
                .setRelyActivity(getActivity())
                .setLayout(R.layout.main_frame_tab1_novice_guide, null)
                .setMessage(R.id.tv_notify, "此悬浮窗是多玩盒子的悬浮窗，启用可以在多人游戏显示")
                .setPassId(R.id.iv_know)
                .build();

        NoviceGuide gameFloat = new NoviceGuide.Builder(getActivity())
                .focusView(view.findViewById(R.id.main_tab1_gamefloat_switch))
                .setPadding(10, 10, 10, 10)
                .setRadius(15)
                .setRelyActivity(getActivity())
                .setLayout(R.layout.main_frame_tab1_novice_guide, null)
                .setMessage(R.id.tv_notify, "此悬浮窗是FMP助手的辅助悬浮窗，启用可以在游戏中自定义更多的功能")
                .setPassId(R.id.iv_know)
                .setDismissListener(() -> {
                    //显示启动按钮
                    startGameButton.animate().translationY(0).setInterpolator(new LinearInterpolator()).start();
                })
                .build();

        NoviceGuide startGame = new NoviceGuide.Builder(getActivity())
                .focusView(startGameButton)
                .setPadding(5, 5, 5, 5)
                .setRadius(1000)
                .setShowTime(600)
                .setRelyActivity(getActivity())
                .setLayout(R.layout.main_frame_tab1_novice_guide, null)
                .setMessage(R.id.tv_notify, "点击这里启动游戏")
                .setPassId(R.id.iv_know)
                .setDismissListener(() -> SpUtil.put("NoviceGuide", true))
                .build();

        NoviceGuideSet noviceGuideSet = new NoviceGuideSet();
        noviceGuideSet.addGuide(archive);
        noviceGuideSet.addGuide(modMall);
        noviceGuideSet.addGuide(duoWanId);
        noviceGuideSet.addGuide(gameVersion);
        noviceGuideSet.addGuide(startMode);
        noviceGuideSet.addGuide(safeMode);
        noviceGuideSet.addGuide(duoWanFloat);
        noviceGuideSet.addGuide(gameFloat);
        noviceGuideSet.addGuide(startGame);
        noviceGuideSet.show();
    }

    @Override
    public boolean onLongClick(View v) {
        //new ExplosionField(getContext()).explode(v, null);
        setSelfAndChildDisappearAndAppearOnClick(v);
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        int userAbility = HelperCore.getInstance().getUserAbility();
        /*if (userAbility <= 0) {
            LauncherSetting setting = LauncherSetting.getSetting();
            setting.setGameFloat(false);
            setting.saveSetting();
            GAMEFLOAT.setChecked(false);
        }*/
        DEBUGMODE.setVisibility(userAbility >= 100 ? View.VISIBLE : View.GONE);
    }

    /**
     * 为自己以及子View添加破碎动画，动画结束后，把View消失掉
     *
     * @param mView 可能是ViewGroup的view
     */
    private void setSelfAndChildDisappearOnClick(final View mView) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                new ExplosionField(getContext()).explode(mView,
                        new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                mView.setVisibility(View.GONE);
                            }
                        });
            }
        });

    }

    /**
     * 为自己以及子View添加破碎动画，动画结束后，View自动出现
     *
     * @param mView 可能是ViewGroup的view
     */
    private void setSelfAndChildDisappearAndAppearOnClick(final View mView) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mView.setVisibility(View.GONE);
                new ExplosionField(getContext()).explode(mView, new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mView.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

}

