package com.fmp.helper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.billy.android.swipe.SmartSwipe;
import com.billy.android.swipe.consumer.ActivitySlidingBackConsumer;
import com.fmp.core.GamePluginManager;
import com.fmp.core.HelperCore;
import com.fmp.core.LocalPluginManager;
import com.fmp.helper.adapter.ModFileRecycleViewAdapter;
import com.fmp.mods.PluginData;
import com.fmp.util.FileUtil;
import com.fmp.util.SpUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;

import net.fmp.helper.R;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class ModFileActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_CHOOSE = 2000;
    private List<PluginData.Item> items = new ArrayList<>();
    private ModFileRecycleViewAdapter modFileRecycleViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mod_files);
        Toolbar toolbar = findViewById(R.id.toolbar_mod_script_files);
        toolbar.setTitle("插件管理");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_white_24dp);
        toolbar.setNavigationOnClickListener(v -> finish());
        //activity侧滑返回
        SmartSwipe.wrap(this)
                .addConsumer(new ActivitySlidingBackConsumer(this))
                //设置联动系数
                .setRelativeMoveFactor(0.5F)
                //指定可侧滑返回的方向，如：enableLeft() 仅左侧可侧滑返回
                .enableLeft();
        //.enableRight();

        NestedScrollView nestedView = findViewById(R.id.mod_script_nestedview);
        //SmartSwipeRefresh.behindMode(nestedView, false).setDataLoader(this);
        FloatingActionButton fab = findViewById(R.id.mod_script_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LFilePicker()
                        .withActivity(ModFileActivity.this)
                        .withRequestCode(REQUEST_CODE_CHOOSE)
                        .withTitle("文件选择")
                        .withIconStyle(Constant.ICON_STYLE_BLUE)
                        .withBackIcon(Constant.BACKICON_STYLETWO)
                        .withMutilyMode(true)
                        .withMaxNum(50)
                        .withStartPath((String) SpUtil.get("FilePickerStartPath", HelperCore.getHelperDirectory().getAbsolutePath()))//指定初始显示路径
                        .withEndPath(HelperCore.getHelperDirectory().getAbsolutePath())
                        .withNotFoundBooks("至少选择一个文件")
                        .withChooseMode(true)//文件夹选择模式
                        .withFileFilter(new String[]{".js", ".modpkg", ".fmod", ".nmod"})
                        .start();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.mod_script_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        modFileRecycleViewAdapter = new ModFileRecycleViewAdapter(this, items);
        recyclerView.setAdapter(modFileRecycleViewAdapter);

        Button gotoModMall = findViewById(R.id.goto_mod_mall);
        gotoModMall.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(ModFileActivity.this, ModMallActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        //为RecycleView绑定触摸事件
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NotNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder) {
                //首先回调的方法 返回int表示是否监听该方向
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;//拖拽
                int swipeFlags = ItemTouchHelper.RIGHT;//侧滑删除
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NotNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder, @NotNull RecyclerView.ViewHolder target) {
                //数据交换
                //items.get(viewHolder.getAdapterPosition()).setPosition(target.getAdapterPosition());
                //items.get(target.getAdapterPosition()).setPosition(viewHolder.getAdapterPosition());
                //滑动事件
                Collections.swap(items, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                //更新数据
                LocalPluginManager.getInstance().updatePluginPosition(items.get(viewHolder.getAdapterPosition()), items.get(target.getAdapterPosition()));

                modFileRecycleViewAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return false;
            }

            @Override
            public void onSwiped(@NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                //侧滑事件
                items.remove(viewHolder.getAdapterPosition());
                modFileRecycleViewAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }

            @Override
            public boolean isLongPressDragEnabled() {
                //是否可拖拽
                return true;
            }
        });
        helper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CHOOSE) {
            List<String> list = data.getStringArrayListExtra(Constant.RESULT_INFO);
            if (list != null && list.size() > 0) {
                SpUtil.put("FilePickerStartPath", new File(list.get(0)).getParent());
                for (String str : list) {
                    File fromFile = new File(str);
                    File toFile = new File(GamePluginManager.getInstance().getModFilesDir(), fromFile.getName());
                    if (fromFile.exists() && fromFile.isFile()) {
                        if (toFile.exists()) {
                            Toasty.error(this, String.format("插件%s重复", fromFile.getName()), Toast.LENGTH_SHORT, true).show();
                        } else {
                            if (FileUtil.copyFile(fromFile.getAbsolutePath(), toFile.getAbsolutePath())) {
                                Toasty.success(this, String.format("添加%s成功", fromFile.getName()), Toast.LENGTH_SHORT, true).show();
                            } else {
                                Toasty.error(this, String.format("插件%s添加失败", fromFile.getName()), Toast.LENGTH_SHORT, true).show();
                            }
                        }
                    } else {
                        Toasty.error(this, String.format("插件%s添加失败", fromFile.getName()), Toast.LENGTH_SHORT, true).show();
                    }
                }
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        items.clear();
        items.addAll(LocalPluginManager.getInstance().refreshPlugin());
        modFileRecycleViewAdapter.notifyDataSetChanged();
        findViewById(R.id.not_item_layout).setVisibility(items.size() == 0 ? View.VISIBLE : View.GONE);
    }
}
