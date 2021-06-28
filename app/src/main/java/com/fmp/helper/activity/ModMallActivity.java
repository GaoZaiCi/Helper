package com.fmp.helper.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.billy.android.swipe.SmartSwipe;
import com.billy.android.swipe.SmartSwipeRefresh;
import com.billy.android.swipe.consumer.ActivitySlidingBackConsumer;
import com.fmp.AppConfig;
import com.fmp.core.GamePluginManager;
import com.fmp.dialog.HelperDialog;
import com.fmp.helper.adapter.ModMallRecycleViewAdapter;
import com.fmp.util.SpUtil;

import net.fmp.helper.R;

import es.dmoral.toasty.Toasty;


public class ModMallActivity extends AppCompatActivity implements SmartSwipeRefresh.SmartSwipeRefreshDataLoader {
    private static final String DATA_MOD_MALL_REMIND = "MOD_MALL_REMIND";
    private ModMallRecycleViewAdapter modMallRecycleViewAdapter;
    private RelativeLayout relativeLayout;
    private long refreshTime = 0;

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_modmall);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_mod_mall);
        toolbar.setTitle("插件中心");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_white_24dp);
        toolbar.setNavigationOnClickListener(v -> finish());

        remindDialog();

        NestedScrollView nestedView = findViewById(R.id.mod_mall_nestedview);
        SmartSwipeRefresh.behindMode(nestedView, false).setDataLoader(this);
        RecyclerView recyclerView = findViewById(R.id.mod_mall_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        modMallRecycleViewAdapter = new ModMallRecycleViewAdapter(this);
        recyclerView.setAdapter(modMallRecycleViewAdapter);

        relativeLayout = findViewById(R.id.not_item_layout);

        //activity侧滑返回
        SmartSwipe.wrap(this)
                .addConsumer(new ActivitySlidingBackConsumer(this))
                //设置联动系数
                .setRelativeMoveFactor(0.5F)
                //指定可侧滑返回的方向，如：enableLeft() 仅左侧可侧滑返回
                .enableLeft()
                .enableRight();
    }

    private void remindDialog() {
        boolean remind = (boolean) SpUtil.get(DATA_MOD_MALL_REMIND, true);
        if (remind) {
            HelperDialog dialog = new HelperDialog(this);
            dialog.setTitle("提示");
            dialog.setMessage("上架MOD由各大JS作者制作投稿，与FMP助手无关\n付费MOD为第三方提供，服务由第三方提供\n上架MOD只为拓展玩家游戏乐趣和增加游戏体验，若出现破坏游戏平衡现象我们会参与调查并实施处罚，如果有违规信息您可以向我们举报\n请您不要恶意举报");
            dialog.setButton1("同意", v -> {
                SpUtil.put(DATA_MOD_MALL_REMIND, false);
                dialog.dismiss();
            });
            dialog.setButton3("不同意", v -> finish());
        }
    }


    @Override
    public void onRefresh(SmartSwipeRefresh ssr) {
        //下滑刷新
        if (refreshTime != 0 && System.currentTimeMillis() - refreshTime < 1000 * 3) {
            ssr.finished(false);
            Toasty.warning(this, "您刷新的太快啦，休息一会儿吧~", Toast.LENGTH_SHORT, true).show();
        } else {
            refreshTime = System.currentTimeMillis();
            GamePluginManager.getInstance().refreshAllPlugin((e, modList) -> {
                if (e == null) {
                    ssr.finished(true);
                    modMallRecycleViewAdapter.notifyDataSetChanged();
                    relativeLayout.setVisibility(modList.size() == 0 ? View.VISIBLE : View.GONE);
                    Toasty.success(ModMallActivity.this, "刷新列表成功", Toast.LENGTH_SHORT, true).show();
                } else {
                    ssr.finished(false);
                    Toasty.error(ModMallActivity.this, "刷新列表失败！" + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                }
            });
        }
    }

    @Override
    public void onLoadMore(SmartSwipeRefresh ssr) {
        //上滑加载
        if (refreshTime != 0 && System.currentTimeMillis() - refreshTime < 1000 * 3) {
            ssr.finished(false);
            Toasty.warning(this, "您获取的太快啦，休息一会儿吧~", Toast.LENGTH_SHORT, true).show();
        } else {
            refreshTime = System.currentTimeMillis();
            GamePluginManager.getInstance().refreshAllPlugin((e, modList) -> {
                if (e == null) {
                    ssr.finished(true);
                    modMallRecycleViewAdapter.notifyDataSetChanged();
                    relativeLayout.setVisibility(modList.size() == 0 ? View.VISIBLE : View.GONE);
                    Toasty.success(ModMallActivity.this, "加载列表成功", Toast.LENGTH_SHORT, true).show();
                } else {
                    ssr.finished(false);
                    Toasty.error(ModMallActivity.this, "加载列表失败！" + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                }
            });
        }
    }


    //右上角菜单的操作
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mod_mall, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        TextView textView = new TextView(ModMallActivity.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(ModMallActivity.this);
        //builder.setTitle("提示");
        builder.setPositiveButton("知道了", null);
        switch (item.getItemId()) {
            case R.id.menu_mod_mall_submission:
                //投稿
                textView.setTextIsSelectable(true);
                textView.setText(String.format("投稿请发送邮件到helper@gaozaici.cn%s在内容中写入MOD名称 MOD版本 MOD图标(附件) MOD介绍 MOD文件(附件)%s如果您需要定制更多请联系我们", AppConfig.Newline, AppConfig.Newline));
                builder.setView(textView);
                builder.show();
                break;
            case R.id.menu_mod_mall_report:
                //举报
                textView.setTextIsSelectable(true);
                textView.setText(String.format("举报请发送邮件到helper@gaozaici.cn%s在内容中写入您要举报的MOD名称以及举报原因", AppConfig.Newline));
                builder.setView(textView);
                builder.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}

