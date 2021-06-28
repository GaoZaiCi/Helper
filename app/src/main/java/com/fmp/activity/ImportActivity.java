package com.fmp.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fmp.Logger;
import com.fmp.core.GamePluginManager;
import com.fmp.skins.SkinUtil;
import com.fmp.textures.TextureManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import es.dmoral.toasty.Toasty;

public class ImportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Uri uri = intent.getData();
        if (uri != null) {
            String ModPath = uri.getPath();
            if (ModPath == null) {
                Toasty.warning(this, "未成功选择文件", Toast.LENGTH_SHORT, true).show();
                return;
            }
            if (intent.getScheme() != null && intent.getScheme().equals("content")) {
                ModPath = uri.getPath().replaceAll("/external_files", "");
            }
            File ModFile = new File(ModPath);
            try {
                String fileName = ModFile.getName();
                if (fileName.endsWith(".js") || (fileName.endsWith(".modpkg") || (fileName.endsWith(".fmod")))) {
                    File file = new File(GamePluginManager.getInstance().getModFilesDir(), fileName);
                    if (!file.exists()) {
                        copyFile(ModFile.getAbsolutePath(), file.getAbsolutePath());
                        Toasty.success(this, "添加" + ModFile.getName() + "成功", Toast.LENGTH_SHORT, true).show();
                    } else {
                        Toasty.error(this, "不可添加重复名称的插件", Toast.LENGTH_SHORT, true).show();
                    }
                }
                if (fileName.endsWith(".mcskin")) {
                    File file = new File(SkinUtil.getSkinsDir(), fileName);
                    if (!file.exists()) {
                        copyFile(ModFile.getAbsolutePath(), file.getAbsolutePath());
                        Toasty.success(this, "添加" + ModFile.getName() + "成功", Toast.LENGTH_SHORT, true).show();
                    } else {
                        Toasty.error(this, "不可添加重复名称的皮肤包", Toast.LENGTH_SHORT, true).show();
                    }
                }
                if (fileName.endsWith(".mcpack")) {
                    File file = new File(TextureManager.getTexturesFileDir(), fileName);
                    if (!file.exists()) {
                        copyFile(ModFile.getAbsolutePath(), file.getAbsolutePath());
                        Toasty.success(this, "添加" + ModFile.getName() + "成功", Toast.LENGTH_SHORT, true).show();
                        //boolean bool = TextureUtil.removeErrorFile(new File(TextureUtil.getTexturesFileDir(this), fileName).getAbsolutePath());
                        //Toasty.success(this, "添加" + ModFile.getName() + "成功，检查资源包" + (bool ? "成功" : "失败"), Toast.LENGTH_SHORT, true).show();
                    } else {
                        Toasty.error(this, "不可添加重复名称的材质/光影", Toast.LENGTH_SHORT, true).show();
                    }
                }
            } catch (Exception e) {
                Logger.toString(e);
                Toasty.error(this, "导入发生错误", Toast.LENGTH_SHORT, true).show();
            }
        }
        finish();
    }

    private void copyFile(String fromFile, String toFile) throws IOException {
        InputStream fosfrom = new FileInputStream(fromFile);
        OutputStream fosto = new FileOutputStream(toFile);
        byte[] bt = new byte[1024];
        int c;
        while ((c = fosfrom.read(bt)) > 0) {
            fosto.write(bt, 0, c);
        }
        fosfrom.close();
        fosto.close();
    }
}
