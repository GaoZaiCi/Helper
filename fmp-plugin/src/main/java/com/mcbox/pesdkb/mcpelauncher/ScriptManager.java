//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.mcbox.pesdkb.mcpelauncher;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.mcbox.pesdk.archive.util.SDHelperUtil;
import com.mcbox.pesdk.launcher.LauncherEventDispatcher;
import com.mcbox.pesdk.launcher.LauncherManager;
import com.mcbox.pesdk.launcher.LauncherRuntime;
import com.mcbox.pesdk.mcfloat.func.DtContextHelper;
import com.mcbox.pesdk.mcfloat.func.DtEntityBloodFunc;
import com.mcbox.pesdk.mcfloat.func.ScriptManagerEx;
import com.mcbox.pesdk.security.SandboxContextFactory;
import com.mcbox.pesdk.util.LauncherMiscUtil;
import com.mcbox.pesdkb.mcpelauncher.api.modpe.ArmorType;
import com.mcbox.pesdkb.mcpelauncher.api.modpe.BlockFace;
import com.mcbox.pesdkb.mcpelauncher.api.modpe.BlockRenderLayer;
import com.mcbox.pesdkb.mcpelauncher.api.modpe.CallbackName;
import com.mcbox.pesdkb.mcpelauncher.api.modpe.DimensionId;
import com.mcbox.pesdkb.mcpelauncher.api.modpe.EnchantType;
import com.mcbox.pesdkb.mcpelauncher.api.modpe.Enchantment;
import com.mcbox.pesdkb.mcpelauncher.api.modpe.EntityRenderType;
import com.mcbox.pesdkb.mcpelauncher.api.modpe.EntityType;
import com.mcbox.pesdkb.mcpelauncher.api.modpe.MobEffect;
import com.mcbox.pesdkb.mcpelauncher.api.modpe.OldEntityTextureFilenameMapping;
import com.mcbox.pesdkb.mcpelauncher.api.modpe.ParticleType;
import com.mcbox.pesdkb.mcpelauncher.api.modpe.RendererManager;
import com.mcbox.pesdkb.mcpelauncher.api.modpe.UseAnimation;
import com.mcbox.pesdkb.mcpelauncher.async.ParseThread;
import com.mcbox.pesdkb.mcpelauncher.async.SkinLoader;
import com.mcbox.pesdkb.mcpelauncher.jsapi.BlockHostObject;
import com.mcbox.pesdkb.mcpelauncher.jsapi.NativeBlockApi;
import com.mcbox.pesdkb.mcpelauncher.jsapi.NativeBuildApi;
import com.mcbox.pesdkb.mcpelauncher.jsapi.NativeEntityApi;
import com.mcbox.pesdkb.mcpelauncher.jsapi.NativeItemApi;
import com.mcbox.pesdkb.mcpelauncher.jsapi.NativeLevelApi;
import com.mcbox.pesdkb.mcpelauncher.jsapi.NativeModPEApi;
import com.mcbox.pesdkb.mcpelauncher.jsapi.NativePlayerApi;
import com.mcbox.pesdkb.mcpelauncher.jsapi.NativeServerApi;
import com.mcbox.pesdkb.mcpelauncher.jsapi.ScriptState;
import com.mcbox.pesdkb.mcpelauncher.texture.AtlasMeta;
import com.mcbox.pesdkb.mcpelauncher.texture.AtlasProvider;
import com.mcbox.pesdkb.mcpelauncher.texture.ClientBlocksJsonProvider;
import com.mcbox.pesdkb.mcpelauncher.texture.ModPkgTexturePack;
import com.mcbox.pesdkb.mcpelauncher.texture.SkinPack;
import com.mcbox.pesdkb.mcpelauncher.texture.TextureListProvider;
import com.mcbox.pesdkd.mcpelauncher.ScriptManagerD;
import com.mozilla.javascript.ContextFactory;
import com.mozilla.javascript.Function;
import com.mozilla.javascript.NativeArray;
import com.mozilla.javascript.NativeJavaObject;
import com.mozilla.javascript.NativeObject;
import com.mozilla.javascript.Script;
import com.mozilla.javascript.Scriptable;
import com.mozilla.javascript.ScriptableObject;
import com.mozilla.javascript.Undefined;
import com.mozilla.javascript.annotations.JSFunction;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@SuppressWarnings("JavaJniMissingFunction")
public class ScriptManager {
    private static final int AMOUNT = 2;
    public static final int ARCH_ARM = 0;
    public static final int ARCH_I386 = 1;
    public static final int AXIS_X = 0;
    public static final int AXIS_Y = 1;
    public static final int AXIS_Z = 2;
    public static final int DAMAGE = 1;
    public static final String ENTITY_KEY_IMMOBILE_BORAD = "mcpe.master.im";
    public static final String ENTITY_KEY_IMMOBILE_DOMESTIC = "mcpe.tool.im";
    public static final String ENTITY_KEY_RENDERTYPE_BORAD = "mcpe.master.rt";
    public static final String ENTITY_KEY_RENDERTYPE_DOMESTIC = "mcpe.tool.rt";
    public static final String ENTITY_KEY_SKIN_BORAD = "mcpe.master.s";
    public static final String ENTITY_KEY_SKIN_DOMESTIC = "mcpe.tool.s";
    public static final int GUI_SCALE_ENHANCE = 1;
    public static final int GUI_SCALE_NORMAL = 0;
    public static final int ITEMID = 0;
    public static int ITEM_ID_COUNT = 512;
    private static final String LOG_TAG = "ScriptManager";
    public static final int MAX_NUM_ERRORS = 5;
    public static final String SCRIPTS_DIR = "modscripts";
    public static final int SLOT_ITEM_TYPE_ARMOR = 1;
    public static final int SLOT_ITEM_TYPE_INVENTORY = 0;
    public static List<Long> allentities = new ArrayList();
    public static List<Long> allplayers = new ArrayList();
    public static Context androidContext;
    public static AtlasMeta armorAtlassMeta;
    public static ClientBlocksJsonProvider blocksJson;
    private static Class<?>[] constantsClasses = new Class[]{ChatColor.class, ItemCategory.class, ParticleType.class, EntityType.class, EntityRenderType.class, ArmorType.class, MobEffect.class, DimensionId.class, BlockFace.class, UseAnimation.class, Enchantment.class, EnchantType.class, BlockRenderLayer.class};
    public static String currentScreen;
    public static String currentScript = "Unknown";
    public static boolean enableSkinPack = false;
    public static Set<String> enabledScripts = new HashSet();
    protected static NativeArray entityList;
    protected static Map<Long, String> entityUUIDMap = new HashMap();
    public static File externalFilesDir = null;
    public static boolean hasLevel = false;
    private static Instrumentation instrumentation;
    private static ExecutorService instrumentationExecutor;
    public static boolean isAddFloatIcon = false;
    public static boolean isEncryptImageFile = false;
    public static boolean isFastChestEnable = false;
    public static boolean isGetBitmap;
    public static boolean isLanGame = false;
    private static boolean isOpenCommand;
    public static boolean isRemote = true;
    public static AtlasMeta itemsAtlasMeta;
    public static AtlasProvider itemsMeta;
    private static float lastDestroyProgress = -1.0F;
    private static int lastDestroySide = -1;
    private static int lastDestroyX = 0;
    private static int lastDestroyY = -1;
    private static int lastDestroyZ = 0;
    public static ModPkgTexturePack modPkgTexturePack = null;
    private static final ModernWrapFactory modernWrapFactory = new ModernWrapFactory();
    public static float newPlayerPitch = 0.0F;
    public static float newPlayerYaw = 0.0F;
    public static boolean nextTickCallsSetLevel = false;
    public static ScriptManager.JoinServerRequest requestJoinServer = null;
    public static boolean requestLeaveGame = false;
    public static int requestLeaveGameCounter;
    protected static boolean requestReloadAllScripts = false;
    public static boolean requestReloadExtenalScripts;
    public static boolean requestScreenshot;
    public static ScriptManager.SelectLevelRequest requestSelectLevel = null;
    public static boolean requestSelectLevelHasSetScreen;
    public static boolean request_screenshot = false;
    private static boolean requestedGraphicsReset = false;
    private static List<Runnable> runOnMainThreadList = new ArrayList();
    public static String screenshotFileName = "";
    public static boolean scriptingEnabled = true;
    protected static boolean scriptingInitialized = false;
    public static List<ScriptState> scripts = new CopyOnWriteArrayList();
    public static boolean sensorEnabled = false;
    public static String serverAddress = null;
    public static int serverPort = 0;
    public static SkinPack skinPack = null;
    public static AtlasMeta terrainAtlasMeta;
    public static AtlasProvider terrainMeta;
    public static TextureListProvider textureList;
    private static final int[] useItemSideOffsets = new int[]{0, -1, 0, 0, 1, 0, 0, 0, -1, 0, 0, 1, -1, 0, 0, 1, 0, 0};
    public static String validSkinPackPath = "";
    public static WorldData worldData = null;
    public static int worldDataSaveCounter = 40;
    public static String worldDir;
    public static String worldName;

    static {
        hasLevel = false;
        requestLeaveGameCounter = 0;
        requestScreenshot = false;
        requestSelectLevelHasSetScreen = false;
    }

    public static String getPlayerNameFromConfs() throws IOException {
        File file=new File(androidContext.getFilesDir(), "games/com.netease/minecraftpe/userinfo.data");
        FileInputStream inputStream = new FileInputStream(file);
        byte[] strByte = new byte[inputStream.available()];
        inputStream.read(strByte);
        String data = new String(strByte);
        inputStream.close();
        return data.trim();
    }

    public static void OnBoxEventNotify(int var0, int var1) {
        if (isFastChestEnable) {
            LauncherEventDispatcher.getInstance().onChestStateChanged(var0, var1);
        }

    }

    public static void OnMinecraftQuit() {
        Log.i("mydebug", " Minecraft Quit is Pressed");
    }

    private static boolean assetFileExists(String var0) {
        boolean var1 = false;
        LauncherRuntime var2 = LauncherManager.getInstance().getLauncherRuntime();
        if (var2 != null) {
            InputStream var4;
            if (var0.startsWith("resource_packs/") && !var0.startsWith("resource_packs/vanilla") || var0.startsWith("resourcepacks") && !var0.startsWith("resourcepacks/vanilla")) {
                var4 = var2.getLocalInputStreamForAsset(var0);
            } else {
                var4 = var2.getInputStreamForAsset(var0);
            }

            if (var4 != null) {
                try {
                    var4.close();
                } catch (IOException var3) {
                }
            }

            if (var4 != null) {
                var1 = true;
            }
        }

        return var1;
    }

    public static void attackCallback(long var0, long var2) {
        callScriptMethod("attackHook", var0, var2);
        if (scriptingEnabled) {
            ScriptManagerEx.attackCallback(var0, var2);
        }

    }

    public static void blockEventCallback(int var0, int var1, int var2, int var3, int var4) {
        callScriptMethod("blockEventHook", var0, var1, var2, var3, var4);
    }

    public static void callScriptMethod(String var0, Object... var1) {
        if (scriptingEnabled && !isRemote) {
            com.mozilla.javascript.Context var2 = com.mozilla.javascript.Context.enter();
            setupContext(var2);
            Iterator var3 = scripts.iterator();

            while(var3.hasNext()) {
                ScriptState var4 = (ScriptState)var3.next();
                if (var4.errors < 5) {
                    currentScript = var4.name;
                    Scriptable var5 = var4.scope;
                    Object var6 = var5.get(var0, var5);
                    if (var6 != null && var6 instanceof Function) {
                        try {
                            ((Function)var6).call(var2, var5, var5, var1);
                        } catch (Exception var7) {
                            var7.printStackTrace();
                            reportScriptError(var4, var7);
                        }
                    }
                }
            }
        }

    }

    public static void chatCallback(String var0) {
        if (var0 != null && var0.length() >= 1) {
            callScriptMethod("chatHook", var0);
            if (var0.charAt(0) == '/') {
                callScriptMethod("procCmd", var0.substring(1));
            }
        }

    }

    public static ScriptableObject classConstantsToJSObject(Class<?> var0) {
        NativeObject var1 = new NativeObject();
        Field[] var7 = var0.getFields();
        int var2 = var7.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            Field var4 = var7[var3];
            int var5 = var4.getModifiers();
            if (Modifier.isStatic(var5) && Modifier.isPublic(var5)) {
                try {
                    var1.putConst(var4.getName(), var1, var4.get((Object)null));
                } catch (Exception var6) {
                    var6.printStackTrace();
                }
            }
        }

        return var1;
    }

    public static void clearAllEnableScripts() {
        Editor var0 = Utils.getPrefs(1).edit();
        var0.putString("enabledScripts", "");
        var0.apply();
    }

    public static void clearTextureOverride(String var0) {
        File var1 = getTextureOverrideFile(var0);
        if (var1 != null && var1.exists()) {
            var1.delete();
        }

        requestedGraphicsReset = true;
    }

    public static void clearTextureOverrides() {
        if (androidContext != null) {
            Utils.clearDirectory(new File(androidContext.getExternalFilesDir((String)null), "textures"));
            requestedGraphicsReset = true;
        }

    }

    public static void commandBlockPlaceCallBack() {
    }

    @CallbackName(
            args = {"x", "y", "z", "side", "progress"},
            name = "continueDestroyBlock"
    )
    public static void continueDestroyBlockCallback(int var0, int var1, int var2, int var3, float var4) {
        boolean var5;
        if (var0 == lastDestroyX && var1 == lastDestroyY && var2 == lastDestroyZ && var3 == lastDestroySide) {
            var5 = true;
        } else {
            var5 = false;
        }

        if (var4 == 0.0F && (var4 != lastDestroyProgress || !var5)) {
            callScriptMethod("startDestroyBlock", var0, var1, var2, var3);
        }

        lastDestroyProgress = var4;
        lastDestroyX = var0;
        lastDestroyY = var1;
        lastDestroyZ = var2;
        lastDestroySide = var3;
        callScriptMethod("continueDestroyBlock", var0, var1, var2, var3, var4);
    }

    private static void debugDumpItems() throws IOException {
        PrintWriter var0 = new PrintWriter(new File(Environment.getExternalStorageDirectory(), "/items.csv"));
        float[] var1 = new float[6];
        int[][] var2 = new int[][]{{1, 1, 6}, {12, 1, 1}, {38, 0, 8}, {159, 0, 15}, {171, 0, 15}, {175, 0, 5}, {349, 1, 3}, {350, 1, 1}, {383, 10, 63}};

        int var3;
        for(var3 = 0; var3 < ITEM_ID_COUNT; ++var3) {
            String var4 = nativeGetItemName(var3, 0, true);
            if (var4 != null) {
                nativeGetTextureCoordinatesForItem(var3, 0, var1);
                var0.println(var3 + "," + var4 + "," + Arrays.toString(var1).replace("[", "").replace("]", "").replace(",", "|"));
            }
        }

        int var5 = var2.length;

        for(var3 = 0; var3 < var5; ++var3) {
            int[] var9 = var2[var3];
            int var6 = var9[0];

            for(int var7 = var9[1]; var7 <= var9[2]; ++var7) {
                String var8 = nativeGetItemName(var6, var7, true);
                if (var8 != null) {
                    nativeGetTextureCoordinatesForItem(var6, var7, var1);
                    var0.println(var6 + ":" + var7 + "," + var8 + "," + Arrays.toString(var1).replace("[", "").replace("]", "").replace(",", "|"));
                }
            }
        }

        var0.close();
    }

    public static void destroy() {
        scriptingInitialized = false;
        androidContext = null;
        scripts.clear();
        runOnMainThreadList.clear();
    }

    public static void destroyBlockCallback(int var0, int var1, int var2, int var3) {
        callScriptMethod("destroyBlock", var0, var1, var2, var3);
        if (scriptingEnabled) {
            ScriptManagerEx.destroyBlockCallback(var0, var1, var2, var3);
        }

    }

    public static void doUseItemOurselves(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
        var0 += useItemSideOffsets[var5 * 3];
        var1 += useItemSideOffsets[var5 * 3 + 1];
        var2 += useItemSideOffsets[var5 * 3 + 2];
        if (NativeLevelApi.getTile(var0, var1, var2) == 0) {
            NativeLevelApi.setTile(var0, var1, var2, var3, var6);
        }

    }

    @CallbackName(
            args = {"projectile", "targetEntity"},
            name = "projectileHitEntityHook"
    )
    public static void dummyThrowableHitEntityCallback() {
    }

    public static void eatCallback(int var0, float var1) {
        callScriptMethod("eatHook", var0, var1);
    }

    @CallbackName(name="entityAddedHook", args={"entity"})
    public static void entityAddedCallback(long entity) {
        if (NativePlayerApi.isPlayer(entity) && !isRemote) {
            Log.d("Mc/Launcher", "[ScriptManager] player is added, playerId = " + entity);
            playerAddedHandler(entity);
        }
        synchronized (allentities) {
            allentities.add(entity);
        }
        callScriptMethod("entityAddedHook", entity);
        if (scriptingEnabled) {
            ScriptManagerEx.entityAddedCallback(entity);
        }
        DtEntityBloodFunc.setEntityMaxHeight(entity);
    }


    @CallbackName(
            args = {"attacker", "victim", "halfhearts"},
            name = "entityHurtHook",
            prevent = true
    )
    public static void entityHurtCallback(long var0, long var2, int var4) {
        callScriptMethod("entityHurtHook", var0, var2, var4);
    }

    public static void entityRemovedCallback(long paramLong) {
        if (NativePlayerApi.isPlayer(Long.valueOf(paramLong))) {
            Log.d("Mc/Launcher", "[ScriptManager] player is removed, playerId = " + paramLong);
            playerRemovedHandler(paramLong);
        }
        synchronized (allentities) {
            int i = allentities.indexOf(Long.valueOf(paramLong));
            if (i >= 0) {
                allentities.remove(i);
            }
        }
        callScriptMethod("entityRemovedHook", Long.valueOf(paramLong));
    }


    public static int[] expandColorsArray(Scriptable var0) {
        int var1 = ((Number)ScriptableObject.getProperty(var0, "length")).intValue();
        int[] var2 = new int[16];

        for(int var3 = 0; var3 < var2.length; ++var3) {
            if (var3 < var1) {
                var2[var3] = (int)((Number)ScriptableObject.getProperty(var0, var3)).longValue();
            } else {
                var2[var3] = (int)((Number)ScriptableObject.getProperty(var0, 0)).longValue();
            }
        }

        return var2;
    }

    public static int[] expandShapelessRecipe(Scriptable var0) {
        int var1 = ((Number)ScriptableObject.getProperty(var0, "length")).intValue();
        if (!(ScriptableObject.getProperty(var0, 0) instanceof Number)) {
            throw new IllegalArgumentException("Method takes in an array of [itemid, itemCount, itemdamage, ...]");
        } else if (var1 % 3 != 0) {
            throw new IllegalArgumentException("Array length must be multiple of 3 (this was changed in 1.6.8): [itemid, itemCount, itemdamage, ...]");
        } else {
            int[] var2 = new int[var1];

            for(var1 = 0; var1 < var2.length; ++var1) {
                var2[var1] = ((Number)ScriptableObject.getProperty(var0, var1)).intValue();
            }

            return var2;
        }
    }

    public static ScriptManager.TextureRequests expandTexturesArray(Object var0) {
        int[] var1 = new int[96];
        String[] var2 = new String[96];
        ScriptManager.TextureRequests var3 = new ScriptManager.TextureRequests();
        var3.coords = var1;
        var3.names = var2;
        if (!(var0 instanceof String)) {
            Scriptable var4 = (Scriptable)var0;
            int var5 = ((Number)ScriptableObject.getProperty(var4, "length")).intValue();
            byte var6;
            if (var5 % 6 == 0) {
                var6 = 6;
            } else {
                var6 = 1;
            }

            var0 = ScriptableObject.getProperty(var4, 0);
            if ((var5 == 1 || var5 == 2) && var0 instanceof String) {
                Arrays.fill(var2, (String)var0);
                if (var5 == 2) {
                    Arrays.fill(var1, ((Number)ScriptableObject.getProperty(var4, 1)).intValue());
                }
            } else {
                for(int var7 = 0; var7 < var1.length; ++var7) {
                    if (var7 < var5) {
                        var0 = ScriptableObject.getProperty(var4, var7);
                    } else {
                        var0 = ScriptableObject.getProperty(var4, var7 % var6);
                    }

                    Scriptable var8 = (Scriptable)var0;
                    String var10 = (String)ScriptableObject.getProperty(var8, 0);
                    int var9 = 0;
                    if (((Number)ScriptableObject.getProperty(var8, "length")).intValue() > 1) {
                        var9 = ((Number)ScriptableObject.getProperty(var8, 1)).intValue();
                    }

                    var1[var7] = var9;
                    var2[var7] = var10;
                }
            }
        } else {
            Arrays.fill(var2, (String)var0);
        }

        return var3;
    }

    public static void explodeCallback(long var0, float var2, float var3, float var4, float var5, boolean var6) {
        callScriptMethod("explodeHook", var0, var2, var3, var4, var5, var6);
        if (scriptingEnabled) {
            ScriptManagerEx.explodeCallback(var0, var2, var3, var4, var5, var6);
        }

    }

    public static void frameCallback() {
        if (isRemote) {
            nativeRequestFrameCallback();
        } else {
            if (requestReloadAllScripts) {
                if (!nativeIsValidItem(256)) {
                    nativeRequestFrameCallback();
                    return;
                }

                requestReloadAllScripts = false;

                try {
                    MobEffect.initIds();
                    SandboxContextFactory var0 = new SandboxContextFactory();
                    ContextFactory.initGlobal(var0);
                    loadEnabledScripts();
                    ScriptManagerEx.loadInternalScripts();
                    loadModPkgScripts();
                    nativeArmorAddQueuedTextures();
                } catch (Throwable var2) {
                    var2.printStackTrace();
                    reportScriptError((ScriptState)null, var2);
                }
            }

            if (requestSelectLevel != null && !requestLeaveGame) {
                if (requestSelectLevelHasSetScreen) {
                    nativeSelectLevel(requestSelectLevel.dir, requestSelectLevel.name);
                    requestSelectLevel = null;
                    requestSelectLevelHasSetScreen = false;
                } else {
                    nativeShowProgressScreen();
                    requestSelectLevelHasSetScreen = true;
                    nativeRequestFrameCallback();
                }
            }

            if (request_screenshot) {
                if (isAddFloatIcon) {
                    int var1 = LauncherManager.getInstance().getLauncherExtend().getScreenShotWatermarkResId();
                    ScreenshotHelper.setWaterMarkFileRes(androidContext.getResources(), var1);
                    isAddFloatIcon = false;
                } else {
                    ScreenshotHelper.setWaterMarkFileRes((Resources)null, 0);
                }

                ScreenshotHelper.takeScreenshot(screenshotFileName, isGetBitmap);
                request_screenshot = false;
            }
        }

    }

    public static String getAllApiMethodsDescriptions() {
        return "";
    }

    private static String[] getAllJsFunctions(Class<? extends ScriptableObject> clazz) {
        List<String> allList = new ArrayList<>();
        for (Method met : clazz.getMethods()) {
            if (met.getAnnotation(JSFunction.class) != null) {
                allList.add(met.getName());
            }
        }
        return allList.toArray(PatchManager.blankArray);
    }

    public static Set<String> getEnabledScripts() {
        return enabledScripts;
    }

    public static long getEntityId(Object var0) {
        long var1;
        if (var0 == null) {
            var1 = -1L;
        } else if (var0 instanceof NativeJavaObject) {
            var1 = (Long)((NativeJavaObject)var0).unwrap();
        } else if (var0 instanceof Number) {
            var1 = ((Number)var0).longValue();
        } else {
            if (!(var0 instanceof Undefined)) {
                throw new RuntimeException("Not an entity: " + var0 + " (" + var0.getClass().toString() + ")");
            }

            var1 = 0L;
        }

        return var1;
    }

    public static String getEntityUUID(long var0) {
        return Long.toString(var0);
    }

    public static File getOriginalFile(File var0) {
        String var2 = getOriginalLocations().optString(var0.getName(), (String)null);
        if (var2 == null) {
            var0 = null;
        } else {
            File var1 = new File(var2);
            var0 = var1;
            if (!var1.exists()) {
                var0 = null;
            }
        }

        return var0;
    }

    public static JSONObject getOriginalLocations() {
        JSONObject var0;
        try {
            var0 = new JSONObject(Utils.getPrefs(1).getString("scriptOriginalLocations", "{}"));
        } catch (JSONException var1) {
            var0 = new JSONObject();
        }

        return var0;
    }

    public static File getScriptFile(String var0) {
        return new File(androidContext.getDir("modscripts", 0), var0);
    }

    public static File getTextureOverrideFile(String var0) {
        Object var1 = null;
        File var2;
        if (androidContext == null) {
            var2 = (File)var1;
        } else {
            var2 = new File((new File(androidContext.getExternalFilesDir((String)null), "textures")).getAbsolutePath(), var0.replace("..", ""));
        }

        return var2;
    }

    public static String getWorldPath() {
        String var0;
        if (worldDir == null) {
            var0 = "";
        } else {
            var0 = worldDir;
        }

        return var0;
    }

    protected static ModernWrapFactory getWrapFactory() {
        return modernWrapFactory;
    }

    public static void handleChatPacketCallback(String var0) {
        if (var0 != null && var0.length() >= 1) {
            callScriptMethod("serverMessageReceiveHook", var0);
        }

    }

    public static void handleMessagePacketCallback(String var0, String var1) {
        if (var1 != null && var1.length() >= 1) {
            if (var0.length() == 0 && var1.equals("§0MCPELauncher, enable scripts")) {
                scriptingEnabled = true;
                nativePreventDefault();
                LauncherManager.getInstance().getLauncherRuntime().scriptPrintCallback("Scripts have been re-enabled", "");
            }

            callScriptMethod("chatReceiveHook", var1, var0);
        }

    }

    public static void init(Context var0) throws Throwable {
        init(var0, true);
    }

    public static void init(Context var0, boolean var1) throws Throwable {
        Log.d("Mc/Launcher", "[ScriptManager] init ...");
        SDHelperUtil.setContext(var0);
        scriptingInitialized = true;
        androidContext = var0.getApplicationContext();
        modPkgTexturePack = new ModPkgTexturePack("resource_packs/vanilla/");
        int var2 = 0;

        label13: {
            int var3;
            try {
                var3 = var0.getPackageManager().getPackageInfo(var0.getPackageName(), 0).versionCode;
            } catch (NameNotFoundException var4) {
                break label13;
            }

            var2 = var3;
        }

        nativeSetupHooks(var2);
        ITEM_ID_COUNT = nativeGetItemIdCount();
        Log.d("Mc/Launcher", "[ScriptManager] [init] ITEM_ID_COUNT=" + ITEM_ID_COUNT);
        scripts.clear();
        entityList = new NativeArray(0L);
        requestReloadAllScripts = var1;
        nativeRequestFrameCallback();
        prepareEnabledScripts();
    }

    public static void initJustLoadedScript(com.mozilla.javascript.Context ctx, Script script, String sourceName) {
        ScriptableObject initStandardObjects = ctx.initStandardObjects(new BlockHostObject(), false);
        ScriptState state = new ScriptState(script, initStandardObjects, sourceName);
        initStandardObjects.defineFunctionProperties(getAllJsFunctions(BlockHostObject.class), BlockHostObject.class, 0);
        try {
            ScriptableObject.defineClass(initStandardObjects, NativePlayerApi.class);
            ScriptableObject.defineClass(initStandardObjects, NativeLevelApi.class);
            ScriptableObject.defineClass(initStandardObjects, NativeEntityApi.class);
            ScriptableObject.defineClass(initStandardObjects, NativeModPEApi.class);
            ScriptableObject.defineClass(initStandardObjects, NativeItemApi.class);
            ScriptableObject.defineClass(initStandardObjects, NativeBlockApi.class);
            ScriptableObject.defineClass(initStandardObjects, NativeServerApi.class);
            ScriptableObject.defineClass(initStandardObjects, NativeBuildApi.class);
            RendererManager.defineClasses(initStandardObjects);
            for (Class clazz : constantsClasses) {
                ScriptableObject.putProperty(initStandardObjects, clazz.getSimpleName(), classConstantsToJSObject(clazz));
            }
        } catch (Exception e) {
            e.printStackTrace();
            reportScriptError(state, e);
        }
        script.exec(ctx, initStandardObjects);
        scripts.add(state);
    }

    private static void injectKeyEvent(final int var0, final int var1) {
        if (instrumentation == null) {
            instrumentation = new Instrumentation();
            instrumentationExecutor = Executors.newSingleThreadExecutor();
        }

        instrumentationExecutor.execute(new Runnable() {
            public void run() {
                Instrumentation var1x = ScriptManager.instrumentation;
                byte var2;
                if (var1 != 0) {
                    var2 = 0;
                } else {
                    var2 = 1;
                }

                var1x.sendKeySync(new KeyEvent(var2, var0));
            }
        });
    }

    public static boolean invalidTexName(String var0) {
        boolean var1;
        if (var0 != null && !var0.equals("undefined") && !var0.equals("null")) {
            var1 = false;
        } else {
            var1 = true;
        }

        return var1;
    }

    public static void invokeScriptMethod(String var0, String var1, Object[] var2) {
        if (scriptingEnabled && !isRemote && var0 != null) {
            com.mozilla.javascript.Context var3 = com.mozilla.javascript.Context.enter();
            setupContext(var3);
            Iterator var4 = scripts.iterator();

            while(var4.hasNext()) {
                ScriptState var5 = (ScriptState)var4.next();
                if (var0.equalsIgnoreCase(var5.name) && var5.errors < 5) {
                    currentScript = var5.name;
                    Scriptable var6 = var5.scope;
                    Object var7 = var6.get(var1, var6);
                    if (var7 != null && var7 instanceof Function) {
                        try {
                            ((Function)var7).call(var3, var6, var6, var2);
                        } catch (Exception var8) {
                            var8.printStackTrace();
                            reportScriptError(var5, var8);
                        }
                    }
                }
            }
        }

    }

    private static boolean isClassGenMode() {
        return false;
    }

    public static boolean isEnabled(File var0) {
        return isEnabled(var0.getName());
    }

    private static boolean isEnabled(String var0) {
        return enabledScripts.contains(var0);
    }

    public static boolean isLocalAddress(String var0) {
        boolean var1 = false;

        label19: {
            boolean var2;
            try {
                InetAddress var4 = InetAddress.getByName(var0);
                if (var4.isLoopbackAddress()) {
                    break label19;
                }

                var2 = var4.isLinkLocalAddress();
            } catch (UnknownHostException var3) {
                var3.printStackTrace();
                return var1;
            }

            if (!var2) {
                return var1;
            }
        }

        var1 = true;
        return var1;
    }

    private static boolean isPackagedScript(File var0) {
        return isPackagedScript(var0.getName());
    }

    private static boolean isPackagedScript(String var0) {
        return var0.toLowerCase().endsWith(".modpkg");
    }

    public static boolean isRealLocalAddress(String hostname) {
        try {
            InetAddress address = InetAddress.getByName(hostname);
            if (address.isLoopbackAddress() || address.isLinkLocalAddress() || address.isSiteLocalAddress() || LauncherMiscUtil.isLocalAddress(address)) {
                return true;
            }
            return false;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        }
    }

    protected static boolean isScriptingEnabled() {
        return scriptingEnabled;
    }

    public static boolean isSkinNameNormalized() {
        return true;
    }

    public static boolean isValidStringParameter(String var0) {
        boolean var1;
        if (!invalidTexName(var0)) {
            var1 = true;
        } else {
            var1 = false;
        }

        return var1;
    }

    private static native void leaveGame();

    public static void leaveGameCallback(boolean var0) {
        Log.i("nadiee", "ScriptManager-leaveGameCallback:");
        leaveGame();
        DtContextHelper.setFirstSyncGameParam(0);
        DtContextHelper.setNetWorldMode(0);
        isLanGame = false;
        scriptingEnabled = true;
        hasLevel = false;
        if (scriptingInitialized) {
            callScriptMethod("leaveGame");
            if (scriptingEnabled) {
                ScriptManagerEx.leaveGameCallback();
            }
        }

        if (worldData != null) {
            try {
                worldData.save();
            } catch (IOException var3) {
                var3.printStackTrace();
            }

            worldData = null;
        }

        serverAddress = null;
        serverPort = 0;

        try {
            LauncherEventDispatcher.getInstance().freeMap(DtContextHelper.getGameWorldName());
        } catch (Exception var2) {
        }

        DtContextHelper.setEnterMapFlag(0);
        if (!isRemote) {
            DtEntityBloodFunc.saveEntityToFile(true);
        }

        isRemote = true;
    }

    public static void levelEventCallback(int var0, int var1, int var2, int var3, int var4, int var5) {
        callScriptMethod("levelEventHook", var0, var1, var2, var3, var4, var5);
    }

    public static void loadEnabledScripts() throws Exception {
        Log.i("hymcbox", "ScriptManager-loadEnabledScripts ");
        loadEnabledScriptsNames(androidContext);
        Iterator var0 = enabledScripts.iterator();

        while(true) {
            while(var0.hasNext()) {
                File var1 = getScriptFile((String)var0.next());
                if (var1.exists() && var1.isFile()) {
                    loadScript(var1);
                } else {
                    Log.i("Mc/Launcher", "ModPE script " + var1.toString() + " doesn't exist");
                }
            }

            Log.i("hymcbox", "ScriptManager-loadEnabledScripts region:" + LauncherManager.getInstance().getConfig().region + ", requestReloadExtenalScripts:" + requestReloadExtenalScripts);
            if (1 == LauncherManager.getInstance().getConfig().region) {
                LauncherManager.getInstance().getLauncherExtend().loadExternalScripts();
            } else if (requestReloadExtenalScripts) {
                LauncherManager.getInstance().getLauncherExtend().loadExternalScripts();
            }

            return;
        }
    }

    public static void loadEnabledScriptsNames(Context var0) {
        enabledScripts = new HashSet<String>() {
            {
                this.add("263df0f15ecab8d9.js");
                this.add("b392920865578da2.js");
            }
        };
    }

    public static void loadModPkgScripts() throws IOException {
        Iterator var0 = modPkgTexturePack.listAllPacks().iterator();

        while(var0.hasNext()) {
            ZipTexturePack var1 = (ZipTexturePack)var0.next();

            try {
                loadPackagedScript(var1.getFile(), var1.getZipFile(), true);
            } catch (Exception var3) {
                var3.printStackTrace();
                modPkgTexturePack.removePackage(var1.getZipName());
            }
        }

    }

    private static void loadPackagedScript(File pkgFile, ZipFile zipFile, boolean firstLoad) throws Exception {
        Reader reader;
        MpepInfo info = null;
        boolean scrambled = false;
        try {
            info = MpepInfo.fromZip(zipFile);
            scrambled = info != null && info.scrambleCode.length() > 0;
        } catch (JSONException jsonEx) {
            jsonEx.printStackTrace();
        }
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            String name = entry.getName();
            Log.d("mydebug", "[loadPackagedScript] script name=" + name);
            String zipName = zipFile.getName();
            if (zipName != null && zipName.contains("/")) {
                zipName = zipName.substring(zipName.lastIndexOf("/") + 1);
            }
            if (name.startsWith("script/") && name.toLowerCase().endsWith(".js")) {
                if (scrambled) {
                    InputStream is = zipFile.getInputStream(entry);
                    byte[] scrambleBytes = new byte[((int) entry.getSize())];
                    is.read(scrambleBytes);
                    is.close();
                    reader = Scrambler.scramble(scrambleBytes, info);
                } else {
                    reader = new InputStreamReader(zipFile.getInputStream(entry));
                }
                Log.d("mydebug", "[loadPackagedScript] script name=" + name);
                loadScript(reader, pkgFile == null ? "unknown.modpkg" : pkgFile.getName(), zipName);
                if (reader != null) {
                    reader.close();
                }
            }
        }
    }


    public static void loadScript(File var0) throws Exception {
        Log.i("hymcbox", "ScriptManager-loadScript : " + var0.getName());
        if (isClassGenMode()) {
            if (scriptingInitialized) {
                /*if (!scriptingEnabled) {
                    throw new RuntimeException("Not available in multiplayer");
                }*/

                loadScriptFromInstance(ScriptTranslationCache.get(androidContext, var0), var0.getName());
            }
        } else if (isPackagedScript(var0)) {
            loadPackagedScript(var0, new ZipFile(var0),true);
        } else {
            FileReader var1 = new FileReader(var0);
            loadScript(var1, var0.getName(), var0.getName());
            var1.close();
        }

    }

    public static void loadScript(Reader var0, String var1, String var2) throws IOException {
        Log.d("mydebug", "start load loadScript :: " + var1);
        if (scriptingInitialized) {
            /*if (!scriptingEnabled) {
                throw new RuntimeException("Not available in multiplayer");
            }*/

            ParseThread var4 = new ParseThread(var0, var1, var2);
            Thread var6 = new Thread(Thread.currentThread().getThreadGroup(), var4, "mctools parse thread", 262144L);
            var6.start();

            try {
                var6.join();
            } catch (InterruptedException var3) {
            }

            if (var4.error != null) {
                if (var4.error instanceof IOException) {
                    IOException var5 = (IOException)var4.error;
                    throw var5;
                } else {
                    throw new IOException(var4.error);
                }
            }
        }

    }

    private static void loadScriptFromInstance(Script var0, String var1) {
        com.mozilla.javascript.Context var2 = com.mozilla.javascript.Context.enter();
        setupContext(var2);
        initJustLoadedScript(var2, var0, var1);
        com.mozilla.javascript.Context.exit();
    }

    public static ScriptManager.TextureRequests mapTextureNames(ScriptManager.TextureRequests var0) {
        for(int var1 = 0; var1 < var0.coords.length; ++var1) {
            String var2 = var0.names[var1];
            if (var2.equals("stonecutter")) {
                String[] var4 = var0.names;
                int var3 = var0.coords[var1];
                var4[var1] = (new String[]{"stonecutter_side", "stonecutter_other_side", "stonecutter_top", "stonecutter_bottom"})[var3 % 4];
                var0.coords[var1] = 0;
            } else if (var2.equals("piston_inner")) {
                var0.names[var1] = "piston_top";
            }
        }

        return var0;
    }

    @CallbackName(
            args = {"attacker", "victim"},
            name = "deathHook"
    )
    public static void mobDieCallback(long var0, long var2) {
        callScriptMethod("deathHook", var0, var2);
        if (scriptingEnabled) {
            ScriptManagerEx.mobDieCallback(var0, var2);
        }

        if (worldData != null) {
            worldData.clearEntityData(var2);
        }

        DtEntityBloodFunc.entityDie(var2);
    }

    private static void nameAndShame(String var0) {
    }

    public static native void nativeAddFurnaceRecipe(int var0, int var1, int var2);

    public static native void nativeAddItemChest(int var0, int var1, int var2, int var3, int var4, int var5, int var6);

    public static native void nativeAddItemCreativeInv(int var0, int var1, int var2);

    public static native void nativeAddItemFurnace(int var0, int var1, int var2, int var3, int var4, int var5, int var6);

    public static native void nativeAddItemInventory(int var0, int var1, int var2);

    public static native void nativeAddShapedRecipe(int var0, int var1, int var2, String[] var3, int[] var4);

    public static native void nativeArmorAddQueuedTextures();

    public static native String nativeBiomeIdToName(int var0);

    public static native float nativeBlockGetDestroyTime(int var0, int var1);

    public static native float nativeBlockGetFriction(int var0);

    public static native int nativeBlockGetSecondPart(int var0, int var1, int var2, int var3);

    public static native void nativeBlockSetCollisionEnabled(int var0, boolean var1);

    public static native void nativeBlockSetColor(int var0, int[] var1);

    public static native void nativeBlockSetDestroyTime(int var0, float var1);

    public static native void nativeBlockSetExplosionResistance(int var0, float var1);

    public static native void nativeBlockSetFriction(int var0, float var1);

    public static native void nativeBlockSetLightLevel(int var0, int var1);

    public static native void nativeBlockSetLightOpacity(int var0, int var1);

    public static native void nativeBlockSetRedstoneConsumer(int var0, boolean var1);

    public static native void nativeBlockSetRenderLayer(int var0, int var1);

    public static native void nativeBlockSetShape(int var0, float var1, float var2, float var3, float var4, float var5, float var6, int var7);

    public static native void nativeBlockSetStepSound(int var0, int var1);

    public static native void nativeClearCapes();

    public static native void nativeClearSlotInventory(int var0);

    public static native void nativeClientMessage(String var0);

    public static native void nativeCloseScreen();

    public static native void nativeDefineArmor(int var0, String var1, int var2, String var3, String var4, int var5, int var6, int var7);

    public static native void nativeDefineBlock(int var0, String var1, String[] var2, int[] var3, int var4, boolean var5, int var6);

    public static native void nativeDefineBlock(int var0, String var1, String[] var2, int[] var3, int var4, boolean var5, int var6, int var7);

    public static native void nativeDefineFoodItem(int var0, String var1, int var2, int var3, String var4, int var5);

    public static native void nativeDefineItem(int var0, String var1, int var2, String var3, int var4);

    public static native void nativeDefinePlaceholderBlocks();

    public static native void nativeDefineSnowballItem(int var0, String var1, int var2, String var3, int var4);

    public static native void nativeDestroyBlock(int var0, int var1, int var2);

    public static native long nativeDropItem(float var0, float var1, float var2, float var3, int var4, int var5, int var6);

    public static native void nativeDumpVtable(String var0, int var1);

    public static native int nativeEntityGetCarriedItem(long var0, int var2);

    public static native String nativeEntityGetMobSkin(long var0);

    public static native String nativeEntityGetNameTag(long var0);

    public static native int nativeEntityGetRenderType(long var0);

    public static native int nativeEntityGetRider(long var0);

    public static native int nativeEntityGetRiding(long var0);

    public static native long nativeEntityGetTarget(long var0);

    public static native long[] nativeEntityGetUUID(long var0);

    public static native boolean nativeEntityHasCustomSkin(long var0);

    public static native void nativeEntitySetImmobile(long var0, boolean var2);

    public static native void nativeEntitySetNameTag(long var0, String var2);

    public static native void nativeEntitySetSize(long var0, float var2, float var3);

    public static native void nativeEntitySetTarget(long var0, long var2);

    public static native void nativeExplode(float var0, float var1, float var2, float var3, boolean var4);

    public static native void nativeExplode(float var0, float var1, float var2, float var3, boolean var4, boolean var5, float var6);

    public static native void nativeExtinguishFire(int var0, int var1, int var2, int var3);

    public static native void nativeForceCrash();

    public static native void nativeGetAllEntities();

    public static native int nativeGetAnimalAge(long var0);

    public static native int nativeGetArch();

    public static native int nativeGetBlockRenderShape(int var0);

    public static native int nativeGetBrightness(int var0, int var1, int var2);

    public static native int nativeGetCarriedItem(int var0);

    public static native int nativeGetData(int var0, int var1, int var2);

    public static native float nativeGetEntityLoc(long var0, int var2);

    public static native int nativeGetEntityTypeId(long var0);

    public static native float nativeGetEntityVel(long var0, int var2);

    public static native int nativeGetGameType();

    public static native String nativeGetI18NString(String var0);

    public static native int nativeGetItemChest(int var0, int var1, int var2, int var3);

    public static native int nativeGetItemCountChest(int var0, int var1, int var2, int var3);

    public static native int nativeGetItemCountFurnace(int var0, int var1, int var2, int var3);

    public static native int nativeGetItemDataChest(int var0, int var1, int var2, int var3);

    public static native int nativeGetItemDataFurnace(int var0, int var1, int var2, int var3);

    public static native int nativeGetItemEntityItem(long var0, int var2);

    public static native int nativeGetItemFurnace(int var0, int var1, int var2, int var3);

    public static native int nativeGetItemIdCount();

    public static native int nativeGetItemMaxDamage(int var0);

    public static native String nativeGetItemName(int var0, int var1, boolean var2);

    public static native String nativeGetItemNameChest(int var0, int var1, int var2, int var3);

    public static native String nativeGetLanguageName();

    public static native long nativeGetLevel();

    public static native int nativeGetMobHealth(long var0);

    public static native int nativeGetMobMaxHealth(long var0);

    public static native float nativeGetPitch(long var0);

    public static native long nativeGetPlayerEnt();

    public static native float nativeGetPlayerLoc(int var0);

    public static native String nativeGetPlayerName(long var0);

    public static native int nativeGetSelectedSlotId();

    public static native String nativeGetSignText(int var0, int var1, int var2, int var3);

    public static native int nativeGetSlotArmor(int var0, int var1);

    public static native int nativeGetSlotInventory(int var0, int var1);

    public static native boolean nativeGetTextureCoordinatesForBlock(int var0, int var1, int var2, float[] var3);

    public static native boolean nativeGetTextureCoordinatesForItem(int var0, int var1, float[] var2);

    public static native int nativeGetTile(int var0, int var1, int var2);

    public static int nativeGetTileWrap(int var0, int var1, int var2) {
        int var3 = nativeGetTile(var0, var1, var2);
        if (var3 == 245) {
            var0 = nativeLevelGetExtraData(var0, var1, var2);
            if (var0 != 0) {
                return var0;
            }
        }

        var0 = var3;
        return var0;
    }

    public static native long nativeGetTime();

    public static native float nativeGetYaw(long var0);

    public static native boolean nativeHasPreventedDefault();

    public static native void nativeHurtTo(int var0);

    public static native boolean nativeIsSneaking(long var0);

    public static native boolean nativeIsValidCommand(String var0);

    public static native boolean nativeIsValidItem(int var0);

    public static native int nativeItemGetMaxStackSize(int var0);

    public static native int nativeItemGetUseAnimation(int var0);

    public static native boolean nativeItemIsExtendedBlock(int var0);

    public static native boolean nativeItemSetProperties(int var0, String var1);

    public static native void nativeItemSetStackedByData(int var0, boolean var1);

    public static native void nativeItemSetUseAnimation(int var0, int var1);

    public static native void nativeJoinServer(String var0, int var1);

    public static native void nativeLeaveGame(boolean var0);

    public static native void nativeLevelAddParticle(int var0, float var1, float var2, float var3, float var4, float var5, float var6, int var7);

    public static native void nativeLevelAddParticle(String var0, float var1, float var2, float var3, float var4, float var5, float var6, int var7);

    public static native boolean nativeLevelCanSeeSky(int var0, int var1, int var2);

    public static native int nativeLevelGetBiome(int var0, int var1);

    public static native String nativeLevelGetBiomeName(int var0, int var1);

    public static native int nativeLevelGetDifficulty();

    public static native int nativeLevelGetExtraData(int var0, int var1, int var2);

    public static native int nativeLevelGetGrassColor(int var0, int var1);

    public static native float nativeLevelGetLightningLevel();

    public static native float nativeLevelGetRainLevel();

    public static native boolean nativeLevelIsRemote();

    public static native void nativeLevelSetBiome(int var0, int var1, int var2);

    public static native void nativeLevelSetDifficulty(int var0);

    public static native void nativeLevelSetExtraData(int var0, int var1, int var2, int var3);

    public static native void nativeLevelSetGrassColor(int var0, int var1, int var2);

    public static native void nativeLevelSetLightningLevel(float var0);

    public static native void nativeLevelSetRainLevel(float var0);

    public static native void nativeMobAddEffect(long var0, int var2, int var3, int var4, boolean var5, boolean var6);

    public static native int nativeMobGetArmor(long var0, int var2, int var3);

    public static native String nativeMobGetArmorCustomName(long var0, int var2);

    public static native void nativeMobRemoveAllEffects(long var0);

    public static native void nativeMobRemoveEffect(long var0, int var2);

    public static native void nativeMobSetArmor(long var0, int var2, int var3, int var4);

    public static native void nativeMobSetArmorCustomName(long var0, int var2, String var3);

    public static native void nativeModPESetDesktopGui(boolean var0);

    public static native void nativeModPESetRenderDebug(boolean var0);

    public static native void nativeOnGraphicsReset();

    public static native void nativePlaySound(float var0, float var1, float var2, String var3, float var4, float var5);

    public static native void nativePlayerAddExperience(int var0);

    public static native boolean nativePlayerCanFly();

    public static native boolean nativePlayerEnchant(int var0, int var1, int var2);

    public static native int nativePlayerGetDimension();

    public static native int[] nativePlayerGetEnchantments(int var0);

    public static native float nativePlayerGetExhaustion();

    public static native float nativePlayerGetExperience();

    public static native float nativePlayerGetHunger(long var0);

    public static native String nativePlayerGetItemCustomName(int var0);

    public static native int nativePlayerGetLevel();

    public static native int nativePlayerGetPointedBlock(int var0);

    public static native long nativePlayerGetPointedEntity();

    public static native float nativePlayerGetPointedVec(int var0);

    public static native float nativePlayerGetSaturation();

    public static native int nativePlayerGetScore();

    public static native boolean nativePlayerIsFlying();

    public static native void nativePlayerSetCanFly(boolean var0);

    public static native void nativePlayerSetExhaustion(float var0);

    public static native void nativePlayerSetExperience(float var0);

    public static native void nativePlayerSetFlying(boolean var0);

    public static native void nativePlayerSetHunger(long var0, float var2);

    public static native void nativePlayerSetItemCustomName(int var0, String var1);

    public static native void nativePlayerSetLevel(int var0);

    public static native void nativePlayerSetSaturation(float var0);

    public static native void nativePrePatch();

    public static native void nativePreventDefault();

    public static native void nativeRecipeSetAnyAuxValue(int var0, boolean var1);

    public static native void nativeRemoveEntity(long var0);

    public static native void nativeRemoveItemBackground();

    public static native void nativeRequestFrameCallback();

    public static native void nativeRideAnimal(long var0, long var2);

    public static native void nativeScreenChooserSetScreen(int var0);

    public static native void nativeSelectLevel(String var0, String var1);

    public static native void nativeSendChat(String var0);

    public static native void nativeSetAllowEnchantments(int var0, int var1, int var2);

    public static native void nativeSetAnimalAge(long var0, int var2);

    public static native void nativeSetArmorSlot(int var0, int var1, int var2);

    public static native void nativeSetBlockRenderShape(int var0, int var1);

    public static native void nativeSetCameraEntity(long var0);

    public static native void nativeSetCape(long var0, String var2);

    public static native void nativeSetCarriedItem(long var0, int var2, int var3, int var4);

    public static native boolean nativeSetEntityRenderType(long var0, int var2);

    public static native void nativeSetExitEnabled(boolean var0);

    public static native void nativeSetFov(float var0, boolean var1);

    public static native void nativeSetGameSpeed(float var0);

    public static native void nativeSetGameType(int var0);

    public static native void nativeSetHandEquipped(int var0, boolean var1);

    public static native void nativeSetI18NString(String var0, String var1);

    public static native void nativeSetInventorySlot(int var0, int var1, int var2, int var3);

    public static native void nativeSetIsRecording(boolean var0);

    public static native void nativeSetItemCategory(int var0, int var1, int var2);

    public static native void nativeSetItemMaxDamage(int var0, int var1);

    public static native void nativeSetItemNameChest(int var0, int var1, int var2, int var3, String var4);

    public static native void nativeSetMobHealth(long var0, int var2);

    public static native void nativeSetMobMaxHealth(long var0, int var2);

    public static native void nativeSetMobSkin(long var0, String var2);

    public static native void nativeSetNightMode(boolean var0);

    public static native void nativeSetOnFire(long var0, int var2);

    public static native void nativeSetPosition(long var0, float var2, float var3, float var4);

    public static native void nativeSetPositionRelative(long var0, float var2, float var3, float var4);

    public static native void nativeSetRot(long var0, float var2, float var3);

    public static native void nativeSetSelectedSlotId(int var0);

    public static native void nativeSetSignText(int var0, int var1, int var2, int var3, String var4);

    public static native void nativeSetSneaking(long var0, boolean var2);

    public static native void nativeSetSpawn(int var0, int var1, int var2);

    public static native void nativeSetStonecutterItem(int var0, int var1);

    public static native void nativeSetTextParseColorCodes(boolean var0);

    public static native void nativeSetTile(int var0, int var1, int var2, int var3, int var4);

    public static native void nativeSetTime(long var0);

    public static native void nativeSetUseController(boolean var0);

    public static native void nativeSetVel(long var0, float var2, int var3);

    public static native void nativeSetupHooks(int var0);

    public static native void nativeShowProgressScreen();

    public static native void nativeShowTipMessage(String var0);

    public static native long nativeSpawnEntity(float var0, float var1, float var2, int var3, String var4);

    public static native int nativeSpawnerGetEntityType(int var0, int var1, int var2);

    public static native void nativeSpawnerSetEntityType(int var0, int var1, int var2, int var3);

    public static native boolean nativeZombieIsBaby(long var0);

    public static native void nativeZombieSetBaby(long var0, boolean var2);

    public static void overrideTexture(String var0, String var1) {
        if (androidContext != null) {
            if (!var1.contains("terrain-atlas.tga") && !var1.contains("items-opaque.png")) {
                if (var0.equals("")) {
                    clearTextureOverride(var1);
                } else {
                    try {
                        URL var4 = new URL(var0);
                        ScriptTextureDownloader var3 = new ScriptTextureDownloader(var4, getTextureOverrideFile(var1));
                        Thread var2 = new Thread(var3);
                        var2.start();
                    } catch (Exception var5) {
                        throw new RuntimeException(var5);
                    }
                }
            } else {
                scriptPrint("cannot override " + var1);
            }
        }

    }

    @CallbackName(
            args = {"player", "experienceAdded"},
            name = "playerAddExpHook",
            prevent = true
    )
    public static void playerAddExperienceCallback(long var0, int var2) {
        callScriptMethod("playerAddExpHook", var0, var2);
    }

    @CallbackName(
            args = {"player", "levelsAdded"},
            name = "playerExpLevelChangeHook",
            prevent = true
    )
    public static void playerAddLevelsCallback(long var0, int var2) {
        callScriptMethod("playerExpLevelChangeHook", var0, var2);
    }

    private static void playerAddedHandler(long var0) {
        if (!allplayers.contains(var0)) {
            allplayers.add(var0);
            if (shouldLoadSkin()) {
                runOnMainThread(new SkinLoader(var0));
            }
        }

    }

    private static void playerRemovedHandler(long var0) {
        int var2 = allplayers.indexOf(var0);
        if (var2 >= 0) {
            allplayers.remove(var2);
        }

    }

    protected static void prepareEnabledScripts() throws Exception {
        loadEnabledScriptsNames(androidContext);
        boolean var0 = Utils.getPrefs(0).getBoolean("zz_reimport_scripts", false);
        StringBuilder var1 = new StringBuilder();
        Iterator var2 = enabledScripts.iterator();

        while(true) {
            while(var2.hasNext()) {
                File var3 = getScriptFile((String)var2.next());
                if (var3.exists() && var3.isFile()) {
                    if (var0) {
                        try {
                            if (reimportIfPossible(var3)) {
                                var1.append(var3.getName()).append(' ');
                            }
                        } catch (Exception var5) {
                            var5.printStackTrace();
                        }
                    }

                    prepareScript(var3);
                } else {
                    Log.i("Mc/Launcher", "ModPE script " + var3.toString() + " doesn't exist");
                }
            }

            return;
        }
    }

    private static void prepareScript(File var0) throws Exception {
        if (isPackagedScript(var0)) {
            modPkgTexturePack.addPackage(var0);
        }

    }

    public static void processDebugCommand(String var0) {
        try {
            if (var0.equals("dumpitems")) {
                debugDumpItems();
            }
        } catch (Exception var1) {
            var1.printStackTrace();
        }

    }

    public static void rakNetConnectCallback(String hostname, int port) {
        isLanGame = isRealLocalAddress(hostname);
        scriptingEnabled = isLocalAddress(hostname);
        serverAddress = hostname;
        serverPort = port;
        isRemote = true;
        synchronized (allentities) {
            allentities.clear();
        }
        allplayers.clear();
        LauncherEventDispatcher.getInstance().netConnect();
    }


    @CallbackName(
            args = {"x", "y", "z", "newCurrent", "someBooleanIDontKnow", "blockId", "blockData"},
            name = "redstoneUpdateHook"
    )
    public static void redstoneUpdateCallback(int var0, int var1, int var2, int var3, boolean var4, int var5, int var6) {
        callScriptMethod("redstoneUpdateHook", var0, var1, var2, var3, var4, var5, var6);
    }

    public static boolean reimportIfPossible(File var0) throws IOException {
        File var1 = getOriginalFile(var0);
        boolean var2;
        if (var1 != null && var1.lastModified() > var0.lastModified()) {
            var2 = true;
        } else {
            var2 = false;
        }

        return var2;
    }

    public static void reloadScript(File var0) throws Exception {
        removeScript(var0.getName());
        loadScript(var0);
    }

    public static void removeDeadEntries(Collection<String> var0) {
        enabledScripts.retainAll(var0);
        saveEnabledScripts();
    }

    public static void removeScript(String var0) {
        for(int var1 = scripts.size() - 1; var1 >= 0; --var1) {
            if (((ScriptState)scripts.get(var1)).name.equals(var0)) {
                scripts.remove(var1);
                break;
            }
        }

        if (isPackagedScript(var0)) {
            try {
                modPkgTexturePack.removePackage(var0);
            } catch (IOException var2) {
                var2.printStackTrace();
            }
        }

    }

    public static void reportScriptError(ScriptState state, Throwable t) {
        if (state != null) {
            state.errors++;
        }
        LauncherRuntime lr = LauncherManager.getInstance().getLauncherRuntime();
        if (lr == null) {
            return;
        }
        if (state == null) {
            lr.scriptErrorCallback("JS错误", t);
            return;
        }
        lr.scriptErrorCallback(state.name, t);
        if (state != null && state.errors >= 5) {
            lr.scriptTooManyErrorsCallback(state.name);
        }
    }


    public static void requestGraphicsReset() {
        requestedGraphicsReset = true;
    }

    public static void runOnMainThread(Runnable paramRunnable) {
        synchronized (runOnMainThreadList) {
            runOnMainThreadList.add(paramRunnable);
        }
    }


    protected static void saveEnabledScripts() {
        Editor var0 = Utils.getPrefs(1).edit();
        var0.putString("enabledScripts", PatchManager.join((String[])enabledScripts.toArray(PatchManager.blankArray), ";"));
        var0.putInt("scriptManagerVersion", 1);
        var0.apply();
    }

    public static void saveEnabledScripts(Set<String> var0) {
        Editor var1 = Utils.getPrefs(1).edit();
        var1.putString("enabledScripts", PatchManager.join((String[])var0.toArray(PatchManager.blankArray), ";"));
        var1.putInt("scriptManagerVersion", 1);
        var1.apply();
    }

    @CallbackName(
            args = {"screenName"},
            name = "screenChangeHook"
    )
    public static void screenChangeCallback(String var0, String var1, String var2) {
        currentScreen = var0;
        callScriptMethod("screenChangeHook", var0);
    }

    public static void scriptPrint(String var0) {
        try {
            LauncherManager.getInstance().getLauncherRuntime().scriptPrintCallback(var0, currentScript);
        } catch (Exception var1) {
            var1.printStackTrace();
        }

    }

    public static void selectLevelCallback(String var0, String var1) {
        Log.d("ScriptManager", "selectLevelCallback ------map dir:" + SDHelperUtil.getMapStorePath() + ",name:" + var0 + ",path:" + var1);
        scriptingEnabled = true;
        isRemote = true;
        isLanGame = false;
        nextTickCallsSetLevel = true;
        worldName = var0;
        worldDir = var1;
        if (worldData != null) {
            try {
                worldData.save();
            } catch (IOException var5) {
                var5.printStackTrace();
            }

            worldData = null;
        }

        try {
            File var3 = new File(SDHelperUtil.getMapStorePath(), worldDir);
            WorldData var2 = new WorldData(var3);
            worldData = var2;
        } catch (IOException var4) {
            var4.printStackTrace();
        }

        callScriptMethod("selectLevelHook");
        DtContextHelper.setFirstSyncGameParam(0);
        DtContextHelper.setGameWorldName(var0);
        DtContextHelper.setGameWorldDir(var1);
        DtContextHelper.setEnterMapFlag(1);
        Log.d("mydebug", "come into selectLevelCallback");
        nextTickCallsSetLevel = true;
    }

    public static void setCustomedSkinPack(boolean var0, String var1) {
        enableSkinPack = var0;
        validSkinPackPath = var1;
    }

    public static void setEnabled(File var0, boolean var1) throws Exception {
        setEnabled(var0.getName(), var1);
    }

    public static void setEnabled(String var0, boolean var1) throws Exception {
        if (var1) {
            reloadScript(getScriptFile(var0));
            enabledScripts.add(var0);
        } else {
            enabledScripts.remove(var0);
            removeScript(var0);
        }

        saveEnabledScripts();
    }

    public static void setEnabled(File[] var0, boolean var1) throws Exception {
        int var2 = var0.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            String var4 = var0[var3].getAbsolutePath();
            if (var4 != null && var4.length() <= 0) {
            }

            if (var1) {
                reloadScript(getScriptFile(var4));
                enabledScripts.add(var4);
            } else {
                enabledScripts.remove(var4);
                removeScript(var4);
            }
        }

        saveEnabledScripts();
    }

    public static void setEnabledWithoutLoad(File var0, boolean var1) throws IOException {
        setEnabledWithoutLoad(var0.getName(), var1);
    }

    private static void setEnabledWithoutLoad(String var0, boolean var1) throws IOException {
        Log.i("mydebug", "ScriptManager-[setEnabledWithoutLoad]-name:" + var0 + ", state:" + var1);
        if (var1) {
            enabledScripts.add(var0);
        } else {
            enabledScripts.remove(var0);
        }

        saveEnabledScripts();
    }

    public static void setLevelCallback(boolean var0, boolean var1) {
    }

    @CallbackName(name = "newLevel")
    public static void setLevelFakeCallback(boolean hasLevel2, boolean isRemote2) {
        boolean z = true;
        ScriptManagerD.nativeItemSetIconTextures();
        nextTickCallsSetLevel = false;
        if (!isRemote2) {
            scriptingEnabled = true;
        }
        nativeSetGameSpeed(20.0f);
        synchronized (allentities) {
            allentities.clear();
        }
        allplayers.clear();
        entityUUIDMap.clear();
        entityAddedCallback(nativeGetPlayerEnt());
        if (!isRemote2) {
            runOnMainThreadList.add(new Runnable() {
                @Override
                public void run() {
                    NativeItemApi.reregisterRecipes();
                }
            });
        }
        callScriptMethod("newLevel", true);
        if (scriptingEnabled) {
            ScriptManagerEx.setLevelFakeCallback(true);
        }
        LauncherRuntime launcherRuntime = LauncherManager.getInstance().getLauncherRuntime();
        if (scriptingEnabled) {
            z = false;
        }
        launcherRuntime.setLevelCallback(z);
    }


    public static void setOriginalLocation(File var0, File var1) throws IOException {
        Editor var2 = Utils.getPrefs(1).edit();
        JSONObject var3 = getOriginalLocations();

        try {
            var3.put(var1.getName(), var0.getAbsolutePath());
            var2.putString("scriptOriginalLocations", var3.toString());
            var2.apply();
        } catch (JSONException var4) {
            throw new RuntimeException("Setting original location failed", var4);
        }
    }

    public static void setRequestLeaveGame() {
        nativeCloseScreen();
        requestLeaveGame = true;
        requestLeaveGameCounter = 10;
    }

    public static void setupContext(com.mozilla.javascript.Context var0) {
        var0.setOptimizationLevel(-1);
        var0.setLanguageVersion(200);
    }

    protected static boolean shouldLoadSkin() {
        return false;
    }

    private static long spawnEntityImpl(float var0, float var1, float var2, int var3, String var4) {
        if (var3 <= 0) {
            throw new RuntimeException("Invalid entity type: " + var3);
        } else {
            String var5 = var4;
            if (var4 != null) {
                label31: {
                    var5 = (String)OldEntityTextureFilenameMapping.m.get(var4);
                    if (var5 != null) {
                        var4 = var5;
                    }

                    if (!var4.endsWith(".png")) {
                        var5 = var4;
                        if (!var4.endsWith(".tga")) {
                            break label31;
                        }
                    }

                    var5 = var4.substring(0, var4.length() - 4);
                }
            }

            long var6 = nativeSpawnEntity(var0, var1, var2, var3, var5);
            if (nativeEntityHasCustomSkin(var6)) {
                if (1 == LauncherManager.getInstance().getConfig().region) {
                    NativeEntityApi.setExtraData(var6, "mcpe.tool.s", var5);
                } else {
                    NativeEntityApi.setExtraData(var6, "mcpe.master.s", var5);
                }
            }

            return var6;
        }
    }

    public static void startDestroyBlockCallback(int var0, int var1, int var2, int var3) {
        callScriptMethod("startDestroyBlock", var0, var1, var2, var3);
    }

    public static void takeScreenshot(String var0) {
        request_screenshot = true;
        screenshotFileName = var0.replace("/", "").replace("\\", "");
        isGetBitmap = false;
        isAddFloatIcon = false;
        nativeRequestFrameCallback();
    }

    public static void takeScreenshotWithCallBack(String var0, boolean var1) {
        request_screenshot = true;
        screenshotFileName = var0;
        isGetBitmap = var1;
        isAddFloatIcon = false;
        nativeRequestFrameCallback();
    }

    public static void takeScreenshotWithWM(String var0, boolean var1, boolean var2) {
        request_screenshot = true;
        screenshotFileName = var0.replace("/", "").replace("\\", "");
        isGetBitmap = var2;
        isAddFloatIcon = var1;
        nativeRequestFrameCallback();
    }

    @CallbackName(
            args = {"projectile", "blockX", "blockY", "blockZ", "side"},
            name = "projectileHitBlockHook"
    )
    public static void throwableHitCallback(long var0, int var2, int var3, int var4, int var5, int var6, float var7, float var8, float var9, long var10) {
        Integer var12;
        if (nativeGetEntityTypeId(var0) == 81) {
            var12 = (Integer)NativeItemApi.itemIdToRendererId.get(nativeEntityGetRenderType(var0));
        } else {
            var12 = null;
        }

        if (var2 == 0) {
            if (var12 != null) {
                callScriptMethod("customThrowableHitBlockHook", var0, var12, var4, var5, var6, var3);
            }

            callScriptMethod("projectileHitBlockHook", var0, var4, var5, var6, var3);
        } else if (var2 == 1) {
            if (var12 != null) {
                callScriptMethod("customThrowableHitEntityHook", var0, var12, var10);
            }

            callScriptMethod("projectileHitEntityHook", var0, var10);
        }

    }

    public static void tickCallback() {
        ScriptManagerEx.dtTickThreadFunc();
        if (nextTickCallsSetLevel) {
            setLevelFakeCallback(true, nativeLevelIsRemote());
        }
        callScriptMethod("modTick", new Object[0]);
        if (scriptingEnabled) {
            ScriptManagerEx.tickCallback();
        }
        if (requestedGraphicsReset) {
            nativeOnGraphicsReset();
            requestedGraphicsReset = false;
        }
        if (sensorEnabled) {
            updatePlayerOrientation();
        }
        if (requestLeaveGame) {
            int i = requestLeaveGameCounter;
            requestLeaveGameCounter = i - 1;
            if (i <= 0) {
                nativeScreenChooserSetScreen(1);
                nativeLeaveGame(false);
                requestLeaveGame = false;
                if (LauncherManager.getInstance().getLauncherRuntime() != null) {
                    LauncherManager.getInstance().getLauncherRuntime().hideKeyboardView();
                }
                nativeRequestFrameCallback();
            }
        }
        if (requestJoinServer != null && !requestLeaveGame) {
            nativeJoinServer(requestJoinServer.serverAddress, requestJoinServer.serverPort);
            requestJoinServer = null;
        }
        if (runOnMainThreadList.size() > 0) {
            synchronized (runOnMainThreadList) {
                for (Runnable r : runOnMainThreadList) {
                    r.run();
                }
                runOnMainThreadList.clear();
            }
        }
        if (ScriptManagerD.isSavingMap) {
            ScriptManagerD.nativeSaveGameData();
            ScriptManagerD.isSavingMap = false;
        }
        if (worldData != null) {
            worldDataSaveCounter--;
            if (worldDataSaveCounter <= 0) {
                try {
                    worldData.save();
                } catch (IOException ie) {
                    ie.printStackTrace();
                }
                worldDataSaveCounter = 200;
            }
        }
    }

    private static void toast(final String var0) {
        if (!TextUtils.isEmpty(var0)) {
            try {
                Activity var1 = LauncherManager.getInstance().getLauncherRuntime().getMcActivity();
                var1.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(LauncherManager.getInstance().getLauncherRuntime().getMcActivity().getApplicationContext(), var0, Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception var3) {
                var3.printStackTrace();
            }
        }

    }

    private static void updatePlayerOrientation() {
        nativeSetRot(nativeGetPlayerEnt(), newPlayerYaw, newPlayerPitch);
    }

    @CallbackName(
            args = {"x", "y", "z", "itemid", "blockid", "side", "itemDamage", "blockDamage"},
            name = "useItem"
    )
    public static void useItemOnCallback(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
        callScriptMethod("useItem", var0, var1, var2, var3, var4, var5, var6, var7);
        if (var3 >= 256 && nativeItemIsExtendedBlock(var3) && !nativeHasPreventedDefault()) {
            nativePreventDefault();
            doUseItemOurselves(var0, var1, var2, var3, var4, var5, var6, var7);
        }

        if (scriptingEnabled) {
            ScriptManagerEx.useItemOnCallback(var0, var1, var2, var3, var4, var5, var6, var7);
        }

    }

    public static void verifyBlockTextures(ScriptManager.TextureRequests var0) {
        if (terrainMeta != null) {
            for(int var1 = 0; var1 < var0.names.length; ++var1) {
                if (!terrainMeta.hasIcon(var0.names[var1], var0.coords[var1])) {
                    throw new MissingTextureException("The requested block texture " + var0.names[var1] + ":" + var0.coords[var1] + " does not exist");
                }
            }
        }

    }

    public static void wordWrapClientMessage(String var0) {
        String[] var1 = var0.split("\n");

        for(int var2 = 0; var2 < var1.length; ++var2) {
            String var3 = var1[var2];
            var0 = var3;
            if (var3.contains("§")) {
                nativeClientMessage(var3);
            } else {
                while(var0.length() > 40) {
                    var3 = var0.substring(0, 40);
                    nativeClientMessage(var3);
                    var0 = var0.substring(var3.length());
                }

                if (var0.length() > 0) {
                    nativeClientMessage(var0);
                }
            }
        }

    }

    public static class EnchantmentInstance {
        public final int level;
        public final int type;

        public EnchantmentInstance(int var1, int var2) {
            this.type = var1;
            this.level = var2;
        }

        public String toString() {
            return "EnchantmentInstance[type=" + this.type + ",level=" + this.level + "]";
        }
    }

    public static class JoinServerRequest {
        public String serverAddress;
        public int serverPort;
    }

    public static class SelectLevelRequest {
        public String dir;
        public int gameMode = 0;
        public String name;
        public String seed;
    }

    public static class TextureRequests {
        public int[] coords;
        public String[] names;
    }
}
