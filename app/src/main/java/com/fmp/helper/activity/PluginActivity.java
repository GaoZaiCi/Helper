package com.fmp.helper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.billy.android.swipe.SmartSwipe;
import com.billy.android.swipe.consumer.ActivitySlidingBackConsumer;
import com.fmp.helper.adapter.PluginRecycleViewAdapter;

import net.fmp.helper.R;

public class PluginActivity extends AppCompatActivity {
    private PluginRecycleViewAdapter pluginRecycleViewAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_plugin);
        Toolbar toolbar = findViewById(R.id.toolbar_plugin);
        toolbar.setTitle("插件管理");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_white_24dp);
        toolbar.setNavigationOnClickListener(v -> finish());

        RecyclerView recyclerView = findViewById(R.id.plugin_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        pluginRecycleViewAdapter = new PluginRecycleViewAdapter(this);
        recyclerView.setAdapter(pluginRecycleViewAdapter);

        //activity侧滑返回
        SmartSwipe.wrap(this)
                .addConsumer(new ActivitySlidingBackConsumer(this))
                //设置联动系数
                .setRelativeMoveFactor(0.5F)
                //指定可侧滑返回的方向，如：enableLeft() 仅左侧可侧滑返回
                .enableLeft()
                .enableRight();
    }

    @Override
    protected void onStart() {
        super.onStart();
        findViewById(R.id.not_item_layout).setVisibility(pluginRecycleViewAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        pluginRecycleViewAdapter.onActivityResult(requestCode, resultCode, data);
    }
}
