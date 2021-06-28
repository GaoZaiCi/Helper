package com.fmp.helper;

import android.animation.Animator;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.fmp.FMP_Toast;
import com.fmp.FMP_Tools;
import com.fmp.core.GameLauncher;
import com.fmp.core.HelperCore;
import com.fmp.core.push.ClientPush;

import net.fmp.helper.R;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    //划屏操作的ViewPager
    private ViewPager viewPager;
    //底部按钮的容器
    private RadioGroup radioGroup;
    //底部按钮
    private RadioButton TabBtn1, TabBtn2, TabBtn3, TabBtn4;
    private TextView toolBarTitle;
    //启动游戏的判断
    private Menu tab1_menu;
    private long mExitTime = 0;
    //private Helper helper;
    //private AppPushManager pushManager;
    //private CloudSignIn signIn;

    @Override
    protected void onDestroy() {
        HelperCore.getInstance().onActivityDestroy(this);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置主页面布局
        setContentView(R.layout.activity_layout_main);
        //设置ToolBar
        LinearLayout toolBarView = findViewById(R.id.activitymainLinearLayout1);

        //TOOLBAR = getLayoutInflater().inflate(R.layout.app_activity_toolbar, null, false);
        //Toolbar toolbar = TOOLBAR.findViewById(R.id.toolbar);
        // TOOLBAR = findViewById(R.id.appbar_main_layout);
        //private AppBarLayout TOOLBAR;
        Toolbar toolbar = findViewById(R.id.toolbar_main_layout);
        toolbar.setTitle("");
        toolBarTitle = toolbar.findViewById(R.id.tv_toolbar_title);
        toolBarTitle.setText("用户首页");
        setSupportActionBar(toolbar);
        //初始化Fragment
        //各个Fragment碎片界面
        MainFrameTab1 TAB1 = new MainFrameTab1();
        MainFrameTab2 TAB2 = new MainFrameTab2();
        MainFrameTab3 TAB3 = new MainFrameTab3();
        MainFrameTab4 TAB4 = new MainFrameTab4();
        //创建一个Fragment集
        //list集
        List<Fragment> list = new ArrayList<>();
        //将Fragment增加到list中;
        list.add(TAB1);
        list.add(TAB2);
        list.add(TAB3);
        list.add(TAB4);
        //ViewPager实例化
        viewPager = findViewById(R.id.activitymainViewPager);
        //实例化底部按钮容器
        radioGroup = findViewById(R.id.activityfooterRadioGroup);
        //实例化底部按钮
        TabBtn1 = findViewById(R.id.activityfooterRadioButton1);
        TabBtn2 = findViewById(R.id.activityfooterRadioButton2);
        TabBtn3 = findViewById(R.id.activityfooterRadioButton3);
        TabBtn4 = findViewById(R.id.activityfooterRadioButton4);
        //按钮点击事件
        radioGroup.setOnCheckedChangeListener((radioGroup, id) -> {
            switch (id) {
                case R.id.activityfooterRadioButton1:
                    loadAnimation(findViewById(id));
                    viewPager.setCurrentItem(0);
                    toolBarTitle.setText("用户首页");
                    loadAnimation(toolBarTitle);
                    setToolBarItemVisible(true);
                    //Toast.makeText(MainActivity.this, "按钮A" + p2, Toast.LENGTH_SHORT).show();
                    //显示ToolBar
                   /* if (TOOLBAR != null) {
                        ToolBarView.removeAllViews();
                        ToolBarView.addView(TOOLBAR);
                    } else {
                        //ToolBarView.addView(MXBAR = TOOLBAR);
                    }*/
                    break;
                case R.id.activityfooterRadioButton2:
                    loadAnimation(findViewById(id));
                    viewPager.setCurrentItem(1);
                    toolBarTitle.setText("游戏资源");
                    loadAnimation(toolBarTitle);
                    setToolBarItemVisible(false);
                    //Toast.makeText(MainActivity.this, "按钮B" + p2, Toast.LENGTH_SHORT).show();
                    //显示ToolBar
                    /*if (TOOLBAR != null) {
                        ToolBarView.removeAllViews();
                        ToolBarView.addView(TOOLBAR);
                    } else {
                        //ToolBarView.addView(MXBAR = TOOLBAR);
                    }*/
                    break;
                case R.id.activityfooterRadioButton3:
                    loadAnimation(findViewById(id));
                    viewPager.setCurrentItem(2);
                    toolBarTitle.setText("系统通知");
                    loadAnimation(toolBarTitle);
                    setToolBarItemVisible(false);
                    //Toast.makeText(MainActivity.this, "按钮C" + p2, Toast.LENGTH_SHORT).show();
                    //显示ToolBar
                    /*if (TOOLBAR != null) {
                        ToolBarView.removeAllViews();
                        ToolBarView.addView(TOOLBAR);
                    } else {
                        //ToolBarView.addView(MXBAR = TOOLBAR);
                    }*/
                    break;
                case R.id.activityfooterRadioButton4:
                    loadAnimation(findViewById(id));
                    viewPager.setCurrentItem(3);
                    toolBarTitle.setText("我的信息");
                    loadAnimation(toolBarTitle);
                    setToolBarItemVisible(false);
                    //隐藏ToolBar
                    //ToolBarView.removeAllViews();
                    //TOOLBAR.setVisibility(View.GONE);
                    break;
            }
        });
        //为ViewPager设置adapter
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), 0, list));
        //设置显示首页
        viewPager.setCurrentItem(0);
        //viewPager滑动事件监听
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        TabBtn1.setChecked(true);
                        break;
                    case 1:
                        TabBtn2.setChecked(true);
                        break;
                    case 2:
                        TabBtn3.setChecked(true);
                        break;
                    case 3:
                        TabBtn4.setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        HelperCore.getInstance().initMainActivity(this);
        /*try {
            FMP_Tools.encryptFile(400, "/storage/emulated/0/Android/data/com.tencent.mobileqq/Tencent/QQfile_recv/FMP加密.js");
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        //GameFloatWindow.getInstance().init(this);

    }

    @Override
    public Resources getResources() {
        //获取到resources对象
        Resources res = super.getResources();
        //修改configuration的fontScale属性
        res.getConfiguration().fontScale = 1;
        //将修改后的值更新到metrics.scaledDensity属性上
        res.updateConfiguration(null, null);
        return res;
    }

    @Override
    protected void onStart() {
        super.onStart();
        HelperCore.getInstance().onActivityStart(this);
        GameLauncher.getInstance().onActivityStart(this);
    }

    //点击效果
    public void loadAnimation(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                Animator animator =
                        ViewAnimationUtils.createCircularReveal(v, v.getWidth() / 2, v.getHeight() / 2, 0, (float) Math.hypot(v.getWidth(), v.getHeight()));
                animator.setDuration(1000);
                animator.setInterpolator(new AccelerateInterpolator());
                animator.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setToolBarItemVisible(boolean visible) {
        if (tab1_menu != null) {
            for (int i = 0; tab1_menu.size() > i; i++) {
                tab1_menu.setGroupVisible(i, visible);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                FMP_Toast.BM_Toast(this, "再返回一次退出Helper~", true);
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
                HelperCore.getInstance().onActivityDestroy(this);
                /*Process.killProcess(Process.myPid());
                System.exit(0);*/
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        //menu菜单
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        tab1_menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //menu菜单item的点击事件
        switch (item.getItemId()) {
           /* case R.id.menu_signin:
                //signIn.initCloudSignIn();
                return true;*/
            case R.id.menu_help:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("帮助");
                builder.setMessage("——软件是否收费\n本软件基础功能免费，高级功能需要授权才能使用，授权可以通过Mod作者认证、捐赠和活动获取。请不要相信任何第三方的授权，以防被骗钱财，您可以在关于找到我们。\n\n——软件的功能\n本软件是“我的世界中国版”的一款辅助软件，提供MOD加载以扩展游戏的多样玩法。\n\n——无法加载MOD\n这个可能是由于多种原因造成的，建议您安装最新的“我的世界”与“多玩我的世界盒子。”\n\n——如何删除插件\n点击您需要删除的插件名称，在弹出的窗口点击删除并确认。”");
                builder.setNegativeButton("知道了", null);
                builder.show();
                break;
            case R.id.menu_checkUpdate:
                ClientPush.getInstance().checkAllPush(this, ClientPush.TYPE_UPDATE, true);
                break;
            case R.id.menu_checkMessage:
                ClientPush.getInstance().checkAllPush(this, ClientPush.TYPE_MESSAGE, true);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //helper.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }

}

