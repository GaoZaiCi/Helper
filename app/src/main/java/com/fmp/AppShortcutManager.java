package com.fmp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppShortcutManager {
    private Context mContext;
    private ShortcutManager mShortcutManager;

    public AppShortcutManager(Context context) {
        mContext = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            mShortcutManager = context.getSystemService(ShortcutManager.class);
        }
    }

    public void addItem(String shortName, String longName, int iconRes, Class<?> cls) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1) {
            List<ShortcutInfo> infos = new ArrayList<>();
            for (int i = 0; i < mShortcutManager.getMaxShortcutCountPerActivity(); i++) {
                //传递的内容
                Intent intent = new Intent(mContext, cls);
                intent.setAction(Intent.ACTION_VIEW);

                ShortcutInfo info = new ShortcutInfo.Builder(mContext, "id" + i)
                        .setShortLabel(shortName)
                        .setLongLabel(longName)
                        .setIcon(Icon.createWithResource(mContext, iconRes))
                        .setIntent(intent)
                        .build();
                infos.add(info);
            }
            mShortcutManager.setDynamicShortcuts(infos);
        }
    }

    public void removeItem(int index, String disabledMessage) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1) {
            List<ShortcutInfo> infos = mShortcutManager.getPinnedShortcuts();
            for (ShortcutInfo info : infos) {
                if (info.getId().equals("id" + index)) {
                    mShortcutManager.disableShortcuts(Arrays.asList(info.getId()), disabledMessage);
                }
            }
            mShortcutManager.removeDynamicShortcuts(Arrays.asList("id" + index));
        }
    }

    public void updateItem(int index, String shortName, String longName, int iconRes, Class<?> cls) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1) {
            Intent intent = new Intent(mContext, cls);
            intent.setAction(Intent.ACTION_VIEW);

            ShortcutInfo info = new ShortcutInfo.Builder(mContext, "id" + index)
                    .setShortLabel(shortName)
                    .setLongLabel(longName)
                    .setIcon(Icon.createWithResource(mContext, iconRes))
                    .setIntent(intent)
                    .build();

            mShortcutManager.updateShortcuts(Arrays.asList(info));
        }
    }

}
