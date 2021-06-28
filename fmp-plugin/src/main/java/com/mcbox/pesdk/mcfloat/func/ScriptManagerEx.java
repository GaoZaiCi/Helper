//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.mcbox.pesdk.mcfloat.func;

import android.util.Log;

import com.mcbox.pesdk.launcher.LauncherEventDispatcher;
import com.mcbox.pesdk.launcher.impl.LauncherRuntimeImpl;
import com.mcbox.pesdk.launcher.script.ScriptTemplate;
import com.mcbox.pesdk.mcfloat.func.DtItemInventory.DtItem;
import com.mcbox.pesdk.mcfloat.model.BagItem;
import com.mcbox.pesdk.mcfloat.model.RolePosition;
import com.mcbox.pesdkb.mcpelauncher.ScriptManager;
import com.mcbox.pesdkd.mcpelauncher.ScriptManagerD;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SuppressWarnings("JavaJniMissingFunction")
public class ScriptManagerEx {
    private static String[] INTERNAL_SCRIPT_CLASSES = new String[]{"com.mcbox.pesdk.launcher.script.impl.SprintRunScript", "com.mcbox.pesdk.launcher.script.impl.DeathNoDropScript", "com.mcbox.pesdk.launcher.script.impl.PotionAttackScript", "com.mcbox.pesdk.launcher.script.impl.McFloatFuncScript"};
    private static String SCRIPT_DISABLE_FUNC_NAME = "disable_script_func";
    private static String SCRIPT_ENABLE_FUNC_NAME = "enable_script_func";
    private static String SCRIPT_RESET_FUNC_NAME = "reset";
    public static int bagReadSize = 45;
    private static boolean bagRefreshFlag = false;
    private static Map<String, ScriptTemplate> internalScripts = new HashMap();
    public static ArrayList<BagItem> mcGameBagList = new ArrayList(36);
    public static ArrayList<BagItem> mcUserBagList = new ArrayList(36);
    public static boolean needUpdatGameBag = true;
    public static int num = 0;
    public static int updateGameBagTick = 0;

    public static void addScript(String path, String name) {
        Reader reader = null;
        try {
            File file = new File(path);
            if (file.exists()) {
                reader = new InputStreamReader(new FileInputStream(file));
            }
            ScriptManager.loadScript(reader, name, name);
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        } catch (IOException e4) {
            e4.printStackTrace();
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e5) {
                    e5.printStackTrace();
                }
            }
        } catch (Throwable th) {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e6) {
                    e6.printStackTrace();
                }
            }
            throw th;
        }
    }


    public static void attackCallback(long var0, long var2) {
        Iterator var4 = internalScripts.values().iterator();

        while(var4.hasNext()) {
            ScriptTemplate var5 = (ScriptTemplate)var4.next();

            try {
                if (var5.isEnabled()) {
                    var5.attackHook(var0, var2);
                }
            } catch (Exception var6) {
                var6.printStackTrace();
            }
        }

    }

    public static void destroyBlockCallback(int var0, int var1, int var2, int var3) {
        Iterator var4 = internalScripts.values().iterator();

        while(var4.hasNext()) {
            ScriptTemplate var5 = (ScriptTemplate)var4.next();

            try {
                if (var5.isEnabled()) {
                    var5.destroyBlock(var0, var1, var2, var3);
                }
            } catch (Exception var6) {
                var6.printStackTrace();
            }
        }

    }

    public static void disableScript(String var0) {
        ScriptManager.invokeScriptMethod(var0, SCRIPT_DISABLE_FUNC_NAME, new Object[0]);
    }

    private static void dtAdjustArmorSlot() {
    }

    private static void dtAdjustCanFly() {
        if (McFloatSettings.bUpdateFlyMode) {
            if (dtGetCanFly() != McFloatSettings.bOptionCanFly) {
                dtSetCanFly(McFloatSettings.bOptionCanFly);
            }

            McFloatSettings.bUpdateFlyMode = false;
        }

    }

    private static void dtAdjustExternJS() {
    }

    private static void dtAdjustGameMode() {
        if (dtGetGameMode() != McFloatSettings.nOptionGameMode) {
            dtSetGameMode(McFloatSettings.nOptionGameMode, true);
            McFloatSettings.bUpdateFlyMode = true;
        }

    }

    private static void dtAdjustGameTime() {
        if (McFloatSettings.bUpdateGameTime && DtContextHelper.getNetWorldMode() == 0) {
            if (dtGetGameTime() != McFloatSettings.nOptionGameTime) {
                dtSetGameTime(McFloatSettings.nOptionGameTime);
            }

            McFloatSettings.bUpdateGameTime = false;
        }

    }

    private static void dtAdjustItemInventory() {
        int var0 = DtItemInventory.getAddItemCnt();
        if (var0 > 0) {
            int var1;
            int var2;
            DtItem var3;
            if (dtGetGameMode() == 1) {
                var1 = 0;

                for(var2 = 0; var2 < var0; ++var2) {
                    var3 = (DtItem)DtItemInventory.DTItemInventory_AddArray.get(var2);
                    if (var3.flag == 1 && var3.type == 0) {
                        var3.flag = 0;
                    } else {
                        ++var1;
                    }
                }

                if (var1 == 0) {
                    DtItemInventory.setAddItemCnt(0);
                    return;
                }

                DtItemInventory.setAddItemCnt(var1);
            }

            var0 = DtItemInventory.getAddItemCnt();
            DtItemInventory.lock();

            for(var2 = 0; var2 < var0; ++var2) {
                var3 = (DtItem)DtItemInventory.DTItemInventory_AddArray.get(var2);
                if (var3.flag == 1) {
                    var3.flag = 0;
                }

                if (var3.type == 0) {
                    needUpdatGameBag = true;
                    ScriptManagerD.nativeAddItemInventory(var3.addId, var3.addCount, var3.addDmg, (String)null);
                } else if (var3.type == 1) {
                    for(var1 = 0; var1 < var3.addCount; ++var1) {
                        ScriptManager.nativeSpawnEntity(ScriptManager.nativeGetPlayerLoc(0), ScriptManager.nativeGetPlayerLoc(1), ScriptManager.nativeGetPlayerLoc(2), var3.addId, (String)null);
                    }
                }
            }

            DtItemInventory.setAddItemCnt(0);
            DtItemInventory.unlock();
        }

    }

    private static void dtAdjustLoadScript() {
    }

    private static void dtAdjustPlayerHealth() {
        if (McFloatSettings.bOptionPlayerInvincible) {
            dtSetPlayerHealth();
        }

    }

    private static void dtAdjustPlayerSpawnPoints() {
    }

    private static void dtAdjustRolePos() {
        RolePosition var0 = LauncherRuntimeImpl.shareRolePosition();
        if (var0.updateFlag) {
            float var1 = var0.dest_x;
            float var2 = var0.dest_y;
            float var3 = var0.dest_z;

            try {
                ScriptManager.nativeSetPosition(ScriptManager.nativeGetPlayerEnt(), var1, var2, var3);
            } catch (Throwable var7) {
            } finally {
                var0.updateFlag = false;
            }

            dtResetSprintRunInternal();
        }

    }

    private static void dtAdjustThirdPersionAngle() {
    }

    public static void dtChangeGUIStatus() {
        try {
            ScriptManagerD.nativeChangeGuiHideStatus();
        } catch (Exception var1) {
            var1.printStackTrace();
        }

    }

    public static void dtEnableFlyButton(boolean var0) {
        try {
            ScriptManagerD.nativeEnableFlyButton(var0);
        } catch (Throwable var2) {
            var2.printStackTrace();
        }

    }

    public static boolean dtGetCanFly() {
        boolean var0;
        try {
            var0 = ScriptManager.nativePlayerCanFly();
        } catch (Exception var2) {
            Log.e("mydebug", "nativeGetTime fail!");
            var0 = false;
        }

        return var0;
    }

    public static boolean dtGetGUIStatus() {
        boolean var0;
        try {
            var0 = ScriptManagerD.nativeGetGuiHideStatus();
        } catch (Exception var2) {
            var2.printStackTrace();
            var0 = false;
        }

        return var0;
    }

    public static int dtGetGameMode() {
        int var0;
        try {
            var0 = ScriptManager.nativeGetGameType();
        } catch (Exception var2) {
            var0 = -1;
        }

        return var0;
    }

    public static int dtGetGameTime() {
        long var0;
        long var2;
        byte var4;
        try {
            var0 = ScriptManager.nativeGetTime();
            var2 = var0 / 19200L;
        } catch (Exception var6) {
            Log.e("mydebug", "nativeGetTime fail!");
            var4 = -1;
            return var4;
        }

        if (var0 < 9600L + 19200L * var2) {
            var4 = 0;
        } else {
            var4 = 1;
        }

        return var4;
    }

    public static int dtGetGuiScale() {
        byte var0;
        if (DtLocalStore.getKeyIntVar("gui_scale") != 0) {
            var0 = 1;
        } else {
            var0 = 0;
        }

        return var0;
    }

    public static boolean dtGetPlayInvinciable() {
        if (!McFloatSettings.bInitPlayInvinciable) {
            McFloatSettings.bOptionPlayerInvincible = DtLocalStore.getKeyBoolVar(DtContextHelper.getGameWorldName(), "invinciable");
            McFloatSettings.bInitPlayInvinciable = true;
        }

        return McFloatSettings.bOptionPlayerInvincible;
    }

    public static int dtGetPlayerLevel() {
        int var0 = 0;

        int var1;
        try {
            var1 = ScriptManager.nativePlayerGetLevel();
        } catch (Throwable var3) {
            return var0;
        }

        var0 = var1;
        return var0;
    }

    public static boolean dtGetThirdPersionAngle() {
        return false;
    }

    public static void dtKillMyself() {
        try {
            ScriptManager.nativeHurtTo(0);
        } catch (Exception var1) {
            var1.printStackTrace();
        }

    }

    public static void dtPlayerJump() {
    }

    public static void dtResetSprintRunInternal() {
        ScriptTemplate var0 = getInternalScript("SprintRunScript");
        if (var0 != null) {
            var0.reset();
        }

    }

    public static void dtSetCanFly(boolean var0) {
        try {
            ScriptManagerD.nativePlayerSetCanFly(var0);
            ScriptManagerD.nativePlayerSetFlying(var0);
        } catch (Throwable var2) {
        }

    }

    public static void dtSetFallWithNoDamage(boolean var0) {
    }

    public static void dtSetFov(float var0, boolean var1) {
        try {
            ScriptManager.nativeSetFov(var0, var1);
        } catch (Exception var3) {
        }

    }

    public static void dtSetGameMode(int var0, boolean var1) {
        if (var0 == 1) {
            if (var1 && DtContextHelper.getNetWorldMode() == 0) {
                for(var0 = 9; var0 < 45; ++var0) {
                    DtBagManager.savePlayerBagSlot(var0, ScriptManager.nativeGetSlotInventory(var0, 0), ScriptManager.nativeGetSlotInventory(var0, 1), ScriptManager.nativeGetSlotInventory(var0, 2), ScriptManagerD.nativeGetSlotInventoryEnchants(0, var0));
                }
            }

            ScriptManager.nativeSetGameType(1);
        } else if (var0 == 0) {
            ScriptManager.nativeSetGameType(0);
            if (var1 && DtContextHelper.getNetWorldMode() == 0) {
                for(var0 = 9; var0 < 45; ++var0) {
                    BagItem var2 = DtBagManager.getPlagerBagSlot(var0);
                    if (var2 != null && var2.getId() > 0) {
                        int var3 = Math.min(var2.getCount(), 64);
                        int[] var4 = var2.getEnchantData();
                        if (var4 != null && var4.length > 0) {
                            ScriptManagerD.nativeAddEnchantItemInventory(var2.getId(), var3, var2.getData(), var4, var4.length, (String)null);
                        } else {
                            ScriptManagerD.nativeAddItemInventory(var2.getId(), var3, var2.getData(), (String)null);
                        }
                    }
                }

                DtBagManager.clearSavedItems();
            }
        }

    }

    public static void dtSetGameTime(int var0) {
        boolean var10001;
        if (var0 == 0) {
            try {
                ScriptManager.nativeSetTime(23000L);
            } catch (Exception var2) {
                var10001 = false;
            }
        } else if (var0 == 1) {
            try {
                ScriptManager.nativeSetTime(6000L);
            } catch (Exception var3) {
                var10001 = false;
            }
        } else if (var0 == 2) {
            try {
                ScriptManager.nativeSetTime(12500L);
            } catch (Exception var4) {
                var10001 = false;
            }
        } else if (var0 == 3) {
            try {
                ScriptManager.nativeSetTime(18000L);
            } catch (Exception var5) {
                var10001 = false;
            }
        }

    }

    public static void dtSetGameTimeDirectly(long var0) {
        try {
            ScriptManager.nativeSetTime(Math.min(30000L, Math.max(0L, var0)));
        } catch (Exception var3) {
        }

    }

    public static void dtSetGuiScale(int var0) {
        if (var0 > 0) {
            DtLocalStore.setKeyIntVar("gui_scale", 1);
        } else {
            DtLocalStore.setKeyIntVar("gui_scale", 0);
        }

    }

    public static void dtSetNoclip(boolean var0) {
        Throwable var10000;
        label35: {
            long var1;
            boolean var10001;
            try {
                var1 = ScriptManager.nativeGetPlayerEnt();
            } catch (Throwable var7) {
                var10000 = var7;
                var10001 = false;
                break label35;
            }

            if (var0) {
                try {
                    ScriptManager.nativeEntitySetSize(var1, -1.0F, -1.0F);
                } catch (Throwable var6) {
                    var10000 = var6;
                    var10001 = false;
                    break label35;
                }
            } else {
                try {
                    ScriptManager.nativeEntitySetSize(var1, 0.6F, 1.8F);
                } catch (Throwable var5) {
                    var10000 = var5;
                    var10001 = false;
                    break label35;
                }
            }

            try {
                ScriptManager.nativePlayerSetFlying(var0);
                return;
            } catch (Throwable var4) {
                var10000 = var4;
                var10001 = false;
            }
        }

        Throwable var3 = var10000;
        var3.printStackTrace();
    }

    public static void dtSetPlayerHealth() {
        try {
            ScriptManager.nativeHurtTo(20);
        } catch (Exception var1) {
        }

    }

    public static void dtSetPlayerLevel(int var0) {
        try {
            ScriptManager.nativePlayerSetLevel(var0);
        } catch (Throwable var2) {
        }

    }

    public static void dtSetPlayerSpawnPoints() {
    }

    public static void dtSetThirdPersionAngle(int var0) {
    }

    private static void dtSyncUserBagList() {
        if (bagRefreshFlag) {
            int var0;
            for(var0 = 0; var0 < 45; ++var0) {
                ScriptManager.nativeClearSlotInventory(var0);
            }

            for(var0 = 0; var0 < mcUserBagList.size(); ++var0) {
                BagItem var1 = (BagItem)mcUserBagList.get(var0);
                if (var1.getEnchantData() == null) {
                    ScriptManagerD.nativeAddItemInventory(var1.getId(), var1.getCount(), var1.getData(), var1.getName());
                } else {
                    ScriptManagerD.nativeAddEnchantItemInventory(var1.getId(), var1.getCount(), 0, var1.getEnchantData(), var1.getEnchantData().length, var1.getName());
                }
            }

            needUpdatGameBag = true;
            setBagRefreshFlag(false);
        }

    }

    public static void dtTickThreadFunc() {
        firstSyncGameParam();
        dtAdjustItemInventory();
        dtAdjustRolePos();
        dtSyncUserBagList();
        if (updateGameBagTick > 60) {
            updateGameBagTick = 0;
            dtUpdateBagList();
        } else {
            ++updateGameBagTick;
        }

        DtBuildingFunc.buildMusicBoxTick();
    }

    private static void dtUpdateBagList() {
        if (dtGetGameMode() != 1 && needUpdatGameBag) {
            mcGameBagList.clear();

            for(int var0 = 0; var0 < bagReadSize; ++var0) {
                int var1 = ScriptManager.nativeGetSlotInventory(var0, 0);
                int var2 = ScriptManager.nativeGetSlotInventory(var0, 2);
                int var3 = ScriptManager.nativeGetSlotInventory(var0, 1);
                String var4 = ScriptManagerD.nativeGetInventoryCustomName(var0);
                boolean var5;
                if (var1 >= 0) {
                    var5 = true;
                } else {
                    var5 = false;
                }

                boolean var6;
                if (var2 > 0) {
                    var6 = true;
                } else {
                    var6 = false;
                }

                if (var6 & var5) {
                    BagItem var7 = new BagItem(var1, var3, var2, var0);
                    if (var4 != null) {
                        var7.setName(var4);
                    }

                    int[] var8 = ScriptManagerD.nativeGetSlotInventoryEnchants(0, var0);
                    if (var8 != null) {
                        var7.setEnchantData(var8);
                    }

                    mcGameBagList.add(var7);
                }
            }
        }

    }

    public static void enableInternalScript(String var0, boolean var1) {
        ScriptTemplate var2 = (ScriptTemplate)internalScripts.get(var0);
        if (var2 != null) {
            var2.setEnabled(var1);
        }

    }

    public static void enableLoadScript(boolean var0) {
        McFloatSettings.bAllowLoadScript = var0;
    }

    public static void enableScript(String var0) {
        ScriptManager.invokeScriptMethod(var0, SCRIPT_ENABLE_FUNC_NAME, new Object[0]);
    }

    public static void entityAddedCallback(long var0) {
        Iterator var2 = internalScripts.values().iterator();

        while(var2.hasNext()) {
            ScriptTemplate var3 = (ScriptTemplate)var2.next();

            try {
                if (var3.isEnabled()) {
                    var3.entityAddedHook(var0);
                }
            } catch (Exception var4) {
                var4.printStackTrace();
            }
        }

    }

    public static void explodeCallback(long var0, float var2, float var3, float var4, float var5, boolean var6) {
        Iterator var7 = internalScripts.values().iterator();

        while(var7.hasNext()) {
            ScriptTemplate var8 = (ScriptTemplate)var7.next();

            try {
                if (var8.isEnabled()) {
                    var8.explodeHook(var0, var2, var3, var4, var5, var6);
                }
            } catch (Exception var9) {
                var9.printStackTrace();
            }
        }

    }

    private static native void enterGame();

    public static void firstSyncGameParam() {
        if (DtContextHelper.getFirstSyncGameParam() == 0) {
            McFloatSettings.nOptionGameMode = dtGetGameMode();
            McFloatSettings.nOptionGameTime = dtGetGameTime();
            McFloatSettings.bOptionCanFly = dtGetCanFly();
            DtBagManager.init();
            if (DtContextHelper.getEnterMapFlag() != 2) {
                DtContextHelper.setEnterMapFlag(2);
                Log.i("nadiee", "ScriptManagerEx-firstSyncGameParam-ScriptManager.isRemote:" + ScriptManager.isRemote);
                enterGame();
                if (ScriptManager.isRemote) {
                    DtContextHelper.setGameWorldName("NetWorldName");
                    DtContextHelper.setGameWorldDir("NetWorldDirName");
                    DtContextHelper.setNetWorldMode(1);
                    LauncherEventDispatcher.getInstance().loadNetMap();
                } else {
                    DtContextHelper.setNetWorldMode(0);
                    LauncherEventDispatcher.getInstance().loadMap(DtContextHelper.getGameWorldName());
                }
            }

            updateGameBagTick = 0;
            dtUpdateBagList();
            long var0 = ScriptManager.nativeGetPlayerEnt();
            Log.d("Mc/Launcher", "[ScriptManagerEx] enter map, local playerId = " + var0);
            DtContextHelper.levelContext.playerId = ScriptManager.nativeGetPlayerEnt();
            DtContextHelper.setFirstSyncGameParam(1);
        }

    }

    private static void firstSyncLocalGoods() {
        int var0 = DtContextHelper.getNetWorldMode();
        int var1 = dtGetGameMode();
        if (var0 == 1 && var1 == 0) {
        }

    }

    public static ArrayList<BagItem> getGameBagList() {
        return mcGameBagList;
    }

    public static ScriptTemplate getInternalScript(String var0) {
        return (ScriptTemplate)internalScripts.get(var0);
    }

    public static boolean isInternalScriptEnabled(String var0) {
        ScriptTemplate var2 = (ScriptTemplate)internalScripts.get(var0);
        boolean var1;
        if (var2 != null) {
            var1 = var2.isEnabled();
        } else {
            var1 = false;
        }

        return var1;
    }

    public static boolean isLoadScriptEnabled() {
        return McFloatSettings.bAllowLoadScript;
    }

    public static void leaveGameCallback() {
        Iterator var0 = internalScripts.values().iterator();

        while(var0.hasNext()) {
            ScriptTemplate var1 = (ScriptTemplate)var0.next();

            try {
                if (var1.isEnabled()) {
                    var1.leaveGame();
                }
            } catch (Exception var2) {
                var2.printStackTrace();
            }
        }

    }

    public static void loadInternalScripts() {
        internalScripts.clear();
        String[] var0 = INTERNAL_SCRIPT_CLASSES;
        int var1 = var0.length;

        for(int var2 = 0; var2 < var1; ++var2) {
            String var3 = var0[var2];

            try {
                ScriptTemplate var5 = (ScriptTemplate)Class.forName(var3).newInstance();
                var5.init();
                internalScripts.put(var5.getName(), var5);
            } catch (Exception var4) {
                var4.printStackTrace();
            }
        }

    }

    public static void loadScript(String var0) throws IOException {
    }

    public static void loadScript(String var0, String var1) throws IOException {
    }

    public static void loadScriptWithAssetsDir(String var0, int var1) throws IOException {
    }

    public static void mobDieCallback(int var0, int var1) {
        Iterator var2 = internalScripts.values().iterator();

        while(var2.hasNext()) {
            ScriptTemplate var3 = (ScriptTemplate)var2.next();

            try {
                if (var3.isEnabled()) {
                    var3.deathHook(var0, var1);
                }
            } catch (Exception var4) {
                var4.printStackTrace();
            }
        }

    }

    public static void mobDieCallback(long var0, long var2) {
        Iterator var4 = internalScripts.values().iterator();

        while(var4.hasNext()) {
            ScriptTemplate var5 = (ScriptTemplate)var4.next();

            try {
                if (var5.isEnabled()) {
                    var5.deathHook(var0, var2);
                }
            } catch (Exception var6) {
                var6.printStackTrace();
            }
        }

    }

    public static void resetInternalScripts() {
        Iterator var0 = internalScripts.values().iterator();

        while(var0.hasNext()) {
            ((ScriptTemplate)var0.next()).reset();
        }

    }

    public static void resetScript(String var0) {
        ScriptManager.invokeScriptMethod(var0, SCRIPT_RESET_FUNC_NAME, new Object[0]);
    }

    public static void setBagRefreshFlag(boolean var0) {
        bagRefreshFlag = var0;
    }

    public static void setEnabled(File var0, boolean var1) throws Exception {
        ScriptManager.setEnabled(var0.getName(), var1);
    }

    public static void setLevelFakeCallback(boolean var0) {
        Iterator var1 = internalScripts.values().iterator();

        while(var1.hasNext()) {
            ScriptTemplate var2 = (ScriptTemplate)var1.next();

            try {
                if (var2.isEnabled()) {
                    var2.newLevel(var0);
                }
            } catch (Exception var3) {
                var3.printStackTrace();
            }
        }

    }

    public static void setUserBagList(List<BagItem> var0) {
        if (var0 != null) {
            mcUserBagList.clear();
            mcUserBagList.addAll(var0);
        }

    }

    public static void tickCallback() {
        Iterator var0 = internalScripts.values().iterator();

        while(var0.hasNext()) {
            ScriptTemplate var1 = (ScriptTemplate)var0.next();

            try {
                if (var1.isEnabled()) {
                    var1.mobTick();
                }
            } catch (Exception var2) {
                var2.printStackTrace();
            }
        }

    }

    public static void useItemOnCallback(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
        Iterator var8 = internalScripts.values().iterator();

        while(var8.hasNext()) {
            ScriptTemplate var9 = (ScriptTemplate)var8.next();

            try {
                if (var9.isEnabled()) {
                    var9.useItem(var0, var1, var2, var3, var4, var5, var6, var7);
                }
            } catch (Exception var10) {
                var10.printStackTrace();
            }
        }

    }
}
