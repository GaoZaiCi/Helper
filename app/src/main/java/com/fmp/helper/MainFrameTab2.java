package com.fmp.helper;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.fmp.Logger;
import com.fmp.core.GameLauncher;
import com.fmp.core.GamePluginManager;
import com.fmp.helper.activity.ArchiveActivity;
import com.fmp.helper.activity.ModFileActivity;
import com.fmp.helper.activity.ModMallActivity;
import com.fmp.helper.activity.SkinsActivity;
import com.fmp.helper.activity.TexturesActivity;
import com.fmp.skins.SkinUtil;
import com.fmp.textures.TextureManager;
import com.fmp.util.FileSizeUtil;

import net.fmp.helper.R;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class MainFrameTab2 extends Fragment implements OnClickListener {
    private View view;
    private int width, height;
    private CardView main_world, main_mod, main_skin, main_texture;
    //地图
    private TextView world_count, world_size;
    //插件
    private TextView mod_count, mod_size;
    //皮肤
    private TextView skin_cur, skin_size;
    //材质光影
    private TextView texture_cur, texture_size;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.main_frame_tab2, null, false);
        }
        width = getActivity().getWindow().getWindowManager().getDefaultDisplay().getWidth();
        height = getActivity().getWindow().getWindowManager().getDefaultDisplay().getHeight();
        //卡片布局
        main_world = view.findViewById(R.id.main_tab2_world_cardview);
        main_mod = view.findViewById(R.id.main_tab2_mod_cardview);
        main_skin = view.findViewById(R.id.main_tab2_skin_cardview);
        main_texture = view.findViewById(R.id.main_tab2_texture_cardview);
        //设置点击事件
        main_world.setOnClickListener(this::onClick);
        main_mod.setOnClickListener(this::onClick);
        main_skin.setOnClickListener(this::onClick);
        main_texture.setOnClickListener(this::onClick);
        //地图的显示控件
        world_count = main_world.findViewById(R.id.main_tab2_world_count);
        world_size = main_world.findViewById(R.id.main_tab2_world_size);
        //插件的显示控件
        mod_count = main_mod.findViewById(R.id.main_tab2_mod_count);
        mod_size = main_mod.findViewById(R.id.main_tab2_mod_size);
        //皮肤的显示控件
        skin_cur = main_skin.findViewById(R.id.main_tab2_skin_cur_name);
        skin_size = main_skin.findViewById(R.id.main_tab2_skin_size);
        //材质光影的显示控件
        texture_cur = main_texture.findViewById(R.id.main_tab2_texture_cur_name);
        texture_size = main_texture.findViewById(R.id.main_tab2_texture_size);

        //前往MOD商城
        view.findViewById(R.id.main_tab2_goto_mod_mall).setOnClickListener(new OnClickListener() {
            public void onClick(View p1) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ModMallActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(intent);
            }
        });

        statrAnimation();
        return view;
    }


    private void statrAnimation() {
        Animation Left = new TranslateAnimation(-width, 0, 0, 0);
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
        Bottom.setFillAfter(false);

        Left.setDuration(600);
        main_world.startAnimation(Left);

        Right.setDuration(600);
        main_mod.startAnimation(Right);

        Left.setDuration(600);
        main_skin.startAnimation(Left);

        Right.setDuration(600);
        main_texture.startAnimation(Right);
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.main_tab2_world_cardview:
                try {
                    //跳转到存档管理
                    intent.setClass(getActivity(), ArchiveActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getActivity().startActivity(intent);
                } catch (Exception e) {
                    Logger.toString(e);
                    Toasty.error(getContext(), "错误:" + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                }
                break;
            case R.id.main_tab2_mod_cardview:
                try {
                    //跳转到插件管理
                    intent.setClass(getActivity(), ModFileActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getActivity().startActivity(intent);
                } catch (Exception e) {
                    Logger.toString(e);
                    Toasty.error(getContext(), "错误:" + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                }
                break;
            case R.id.main_tab2_skin_cardview:
                try {
                    //跳转到皮肤管理
                    intent.setClass(getActivity(), SkinsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getActivity().startActivity(intent);
                } catch (Exception e) {
                    Logger.toString(e);
                    Toasty.error(getContext(), "错误:" + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                }
                break;
            case R.id.main_tab2_texture_cardview:
                try {
                    //跳转到材质光影管理
                    intent.setClass(getActivity(), TexturesActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getActivity().startActivity(intent);
                } catch (Exception e) {
                    Logger.toString(e);
                    Toasty.error(getContext(), "错误:" + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                }
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        /*世界类*/
        String currentVersion = GameLauncher.getInstance().getCurrentVersion().getPackageName();
        File worldDir = new File(com.fmp.core.HelperCore.getHelperDirectory(), String.format("Android/data/%s/files/minecraftWorlds", currentVersion));
        File[] worlds = worldDir.listFiles();
        if (worlds != null) {
            world_size.setText(FileSizeUtil.FormetFileSize(getFileDirSize(worldDir)));
            world_count.setText(String.format("您有%d个世界", worlds.length));
        } else {
            world_size.setText("无文件");
            world_count.setText("您没有世界资源");
        }
        /*插件类*/
        File modDir = GamePluginManager.getInstance().getModFilesDir();
        File[] mods = modDir.listFiles();
        if (mods != null) {
            long length = 0, count = 0;
            for (File mod : mods) {
                if (mod.isFile()) {
                    if (mod.getName().endsWith(".js") || mod.getName().endsWith(".modpkg") || mod.getName().endsWith(".fmod")) {
                        length += mod.length();
                        count++;
                    }
                }
            }
            mod_size.setText(FileSizeUtil.FormetFileSize(length));
            mod_count.setText(String.format("您有%d个插件", count));
        } else {
            mod_size.setText("无文件");
            mod_count.setText("您没有插件资源");
        }
        /*皮肤类*/
        String curSkinName = SkinUtil.getCurSkinName();
        if (curSkinName != null) {
            //skin_cur.setSingleLine(true);
            //skin_cur.setText("正在使用"+curSkinName.replaceAll(".mcskin", Helper.EMPTY_STRING));
            skin_cur.setText("自定义皮肤");
        } else {
            skin_cur.setText("默认皮肤");
        }
        File[] skins = SkinUtil.getSkinsDir().listFiles();
        if (skins != null) {
            int count = 0;
            for (File skin : skins) {
                if (skin.getName().endsWith(".mcskin")) count++;
            }
            if (count == 0) {
                skin_size.setText("您没有皮肤资源");
            } else {
                skin_size.setText(String.format("您有%d个皮肤", count));
            }
        } else {
            skin_size.setText("您没有皮肤资源");
        }
        /*材质光影类*/
        try {
            int count = 0;
            List<TextureManager.Item> items = TextureManager.getInstance().getTexturesList();
            for (int i = 0; items.size() > i; i++) {
                //判断是否加载
                if (items.get(i).isEnable()) {
                    count++;
                }
            }
            if (count == 0) {
                texture_cur.setText("默认材质光影");
            } else {
                texture_cur.setText("自定义材质");
            }
        } catch (Exception e) {
            //FMP_Logger.toString(StringArray.get(StringArray.TAG), e);
            texture_cur.setText("默认材质光影");
        }
        try {
            File textureDir = TextureManager.getTexturesFileDir();
            File[] textures = textureDir.listFiles();
            if (textures != null) {
                long length = 0;
                for (File texture : textures) {
                    if (texture.isFile()) {
                        if (texture.getName().endsWith(".mcpack")) {
                            length += texture.length();
                        }
                    }
                }
                if (length == 0) {
                    texture_size.setText("当前没有资源");
                } else {
                    texture_size.setText(FileSizeUtil.FormetFileSize(length));
                }
            } else {
                texture_size.setText("0KB");
            }
        } catch (Exception e) {
            //FMP_Logger.toString(StringArray.get(StringArray.TAG), e);
            texture_size.setText("0KB");
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


}
