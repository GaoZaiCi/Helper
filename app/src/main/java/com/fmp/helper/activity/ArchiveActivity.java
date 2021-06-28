package com.fmp.helper.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.billy.android.swipe.SmartSwipe;
import com.billy.android.swipe.SmartSwipeRefresh;
import com.billy.android.swipe.consumer.ActivitySlidingBackConsumer;
import com.fmp.core.GameLauncher;
import com.fmp.helper.adapter.ArchiveRecycleViewAdapter;
import com.fmp.helper.item.ArchiveItem;

import net.fmp.helper.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ArchiveActivity extends AppCompatActivity implements SmartSwipeRefresh.SmartSwipeRefreshDataLoader {
    private ArchiveRecycleViewAdapter archiveRecycleViewAdapter;
    private List<ArchiveItem> items = new ArrayList<>();
    private SmartSwipeRefresh smartSwipeRefresh;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_archive);
        Toolbar toolbar = findViewById(R.id.toolbar_archive);
        toolbar.setTitle("存档管理");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        NestedScrollView nestedView = findViewById(R.id.archive_nestedview);
        SmartSwipeRefresh.behindMode(nestedView, false).setDataLoader(this);
        RecyclerView recyclerView = findViewById(R.id.archive_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        archiveRecycleViewAdapter = new ArchiveRecycleViewAdapter(this, items);
        recyclerView.setAdapter(archiveRecycleViewAdapter);

        relativeLayout = findViewById(R.id.not_item_layout);


        //activity侧滑返回
        SmartSwipe.wrap(this)
                .addConsumer(new ActivitySlidingBackConsumer(this))
                //设置联动系数
                .setRelativeMoveFactor(0.5F)
                //指定可侧滑返回的方向，如：enableLeft() 仅左侧可侧滑返回
                .enableLeft()
                .enableRight();


        loadWorlds();
    }

    public void loadWorlds() {
        String currentVersion = GameLauncher.getInstance().getCurrentVersion().getPackageName();
        File worldDir = new File(com.fmp.core.HelperCore.getHelperDirectory(), String.format("Android/data/%s/files/minecraftWorlds", currentVersion));
        File[] worlds = worldDir.listFiles();
        if (worlds != null) {
            //移除数据
            items.clear();
            //遍历世界目录
            for (final File world : worlds) {
                ArchiveItem item = new ArchiveItem();
                item.setWorldPath(world.getAbsolutePath());
                File[] worldData = world.listFiles();
                if (worldData != null) {
                    for (final File data : worldData) {
                        if (data.getName().equals("levelname.txt") && data.isFile()) {
                            try {
                                item.setName(new BufferedReader(new FileReader(data)).readLine());
                            } catch (java.io.IOException e) {
                                item.setName("我的世界");
                            }
                        }
                        if (data.getName().equals("world_icon.jpeg") && data.isFile()) {
                            try {
                                item.setIcon(BitmapFactory.decodeStream(new FileInputStream(data)));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                        if (data.getName().equals("db") && data.isDirectory()) {
                            item.setWorldSize(getFileDirSize(data));
                        }
                        if (data.getName().equals("behavior_packs") && data.isDirectory()) {
                            File[] behaviorPacks = data.listFiles();
                            if (behaviorPacks != null) {
                                item.setBehaviorCount(behaviorPacks.length);
                                item.setBehaviorSize(getFileDirSize(data));
                            }
                        }
                        if (data.getName().equals("resource_packs") && data.isDirectory()) {
                            File[] resourcePacks = data.listFiles();
                            if (resourcePacks != null) {
                                item.setResourceCount(resourcePacks.length);
                                item.setResourceSize(getFileDirSize(data));
                            }
                        }
                    }
                    items.add(item);
                }
            }
            relativeLayout.setVisibility(items.size() == 0 ? View.VISIBLE : View.GONE);
            if (smartSwipeRefresh != null)
                smartSwipeRefresh.finished(true);
            archiveRecycleViewAdapter.notifyDataSetChanged();
        } else {
            relativeLayout.setVisibility(View.VISIBLE);
            if (smartSwipeRefresh != null)
                smartSwipeRefresh.finished(false);
        }

    }

    private long getFileDirSize(final File fileDir) {
        //判断文件是否存在
        if (fileDir.exists()) {
            //如果是目录则递归计算其内容的总大小
            if (fileDir.isDirectory()) {
                File[] files = fileDir.listFiles();
                long size = 0;
                if (files != null) {
                    for (File file : files)
                        size += getFileDirSize(file);
                }
                return size;
            } else {//如果是文件则直接返回其大小,以“兆”为单位
                return fileDir.length();
            }
        } else {
            return 0;
        }
    }


    @Override
    public void onRefresh(SmartSwipeRefresh ssr) {
        smartSwipeRefresh = ssr;
        loadWorlds();
    }

    @Override
    public void onLoadMore(SmartSwipeRefresh ssr) {
        smartSwipeRefresh = ssr;
        loadWorlds();
    }
}
