package com.fmp.core;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.text.TextUtils;
import android.util.Log;

import com.fmp.data.InternetProtocol;
import com.mcbox.pesdk.launcher.impl.LauncherRuntimeImpl;
import com.mcbox.pesdk.security.SandboxClassShutter;
import com.mcbox.pesdkb.mcpelauncher.ChatColor;
import com.mcbox.pesdkb.mcpelauncher.ItemCategory;
import com.mcbox.pesdkb.mcpelauncher.PatchManager;
import com.mcbox.pesdkb.mcpelauncher.ZipTexturePack;
import com.mcbox.pesdkb.mcpelauncher.api.modpe.ArmorType;
import com.mcbox.pesdkb.mcpelauncher.api.modpe.BlockFace;
import com.mcbox.pesdkb.mcpelauncher.api.modpe.BlockRenderLayer;
import com.mcbox.pesdkb.mcpelauncher.api.modpe.DimensionId;
import com.mcbox.pesdkb.mcpelauncher.api.modpe.EnchantType;
import com.mcbox.pesdkb.mcpelauncher.api.modpe.Enchantment;
import com.mcbox.pesdkb.mcpelauncher.api.modpe.EntityRenderType;
import com.mcbox.pesdkb.mcpelauncher.api.modpe.EntityType;
import com.mcbox.pesdkb.mcpelauncher.api.modpe.MobEffect;
import com.mcbox.pesdkb.mcpelauncher.api.modpe.ParticleType;
import com.mcbox.pesdkb.mcpelauncher.api.modpe.RendererManager;
import com.mcbox.pesdkb.mcpelauncher.api.modpe.UseAnimation;
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
import com.mojang.minecraftpe.MainActivity;
import com.mozilla.javascript.NativeObject;
import com.mozilla.javascript.Script;
import com.mozilla.javascript.ScriptableObject;
import com.mozilla.javascript.annotations.JSFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipFile;

import static com.mcbox.pesdkb.mcpelauncher.ScriptManager.scripts;

@SuppressWarnings({"ResultOfMethodCallIgnored", "JavaJniMissingFunction"})
@SuppressLint({ "DefaultLocale", "UnsafeDynamicallyLoadedCode"})
public class GameManager implements DeviceInfoUtil.onInternetProtocolInfo {
    public static final int TYPE_LOAD_JS_ERR = 11;
    private static final int TYPE_LOAD_ALL_MOD = 8;
    private static final int TYPE_LOAD_ALL_END = 9;
    private static final int TYPE_LOAD_JS_TIP = 10;
    private static final int TYPE_LOAD_MODPKG_TIP = 20;
    private static final int TYPE_LOAD_MODPKG_ERR = 21;
    private static final int TYPE_APP_NETWORK_INFO = 50;
    private static GameManager gameManager;
    private List<PluginItem> pluginItems = new ArrayList<>();
    private SkinPack skinPack;
    private TexturePack texturePack;
    private LauncherRuntimeImpl launcherRuntime;
    private List<Long> worldEntityList = new ArrayList<>();

    public static native Application getApplication();

    public static native void sendBroadcast(int type, String value);

    public static native void loadScript(File file);

    public static native void loadModPkg(File file);

    public static native boolean setScriptEnabled(String path, boolean enabled);

    public static native byte[] decryptFile(int key, byte[] bytes);

    private static native void writerLog(String message);

    public static native boolean addItemInventory(int id, int count, int damage);

    public static native boolean addEnchantItemInventory(int itemId, int itemDamage, int[] enchants);

    public static native boolean addEnchantItemInventory(int itemId, int itemDamage, int[] enchants, int count);

    public static native void logout();

    protected static native int getLevel();

    public static native String getUserUUID();

    public static native String getFileMD5(String path);

    public static native HelperApi initPluginApi(String id);

    protected static native void nativeAddItemToOffhand(boolean b);
    protected static native void nativeCarriedItemToOffhand(boolean b);
    protected static native void nativeAllItemAllowOffhand(boolean b);
    protected static native void nativeKillingMode(boolean mode);
    protected static native void nativeClickKillMode(boolean mode);
    protected static native void nativePlayerSetOperator(String id, boolean mode);
    public static native boolean nativePlayerAttack(String id, long ent);
    public static native void nativePlayerAttacks(String id, long[] ents);
    public static native long[] nativeLevelGetAllPlayer();
    public static native void nativeItemSetAllowOffhand(short id, boolean b);
    protected static native void nativeDestroyModeTen(boolean mode);
    protected static native void nativeDestroyModeClick(boolean mode);
    protected static native void nativeFastBuild(boolean mode);

    public static GameManager getInstance() {
        if (gameManager == null) {
            gameManager = new GameManager();
        }
        return gameManager;
    }

    public static void printException(Throwable msg) {
        StackTraceElement[] stackTrace = msg.getStackTrace();
        StringBuilder builder = new StringBuilder(String.format("\n%s\n", msg.getMessage()));
        for (StackTraceElement stackTraceElement : stackTrace) {
            if (!stackTraceElement.getClassName().contains("fmp"))
                builder.append(String.format("at %s.%s(%s:%d)\n", stackTraceElement.getClassName(), stackTraceElement.getMethodName(), stackTraceElement.getFileName(), stackTraceElement.getLineNumber()));
        }
        try {
            writerLog(builder.toString());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void init(LauncherRuntimeImpl launcherRuntime, Activity activity) {
        this.launcherRuntime = launcherRuntime;
        try {
            ZipFile zipFile = new ZipFile(activity.getIntent().getStringExtra("apkPath"));
            File soFile = new File(activity.getFilesDir(), "libnetease-support.so");
            writeFile(zipFile.getInputStream(zipFile.getEntry("lib/armeabi-v7a/libnetease-support.so")), soFile);
            writeFile(zipFile.getInputStream(zipFile.getEntry("assets/263df0f15ecab8d9.js")), new File(activity.getDir("modscripts", Context.MODE_PRIVATE), "263df0f15ecab8d9.js"));
            writeFile(zipFile.getInputStream(zipFile.getEntry("assets/b392920865578da2.js")), new File(activity.getDir("modscripts", Context.MODE_PRIVATE), "b392920865578da2.js"));
            System.load(soFile.getAbsolutePath());
            //writeFile(zipFile.getInputStream(zipFile.getEntry("assets/index.bundle")), new File(activity.getFilesDir(), "games/com.netease/rn/index.bundle"));
            //reUpdateManifest();
            DeviceInfoUtil.getInstance().getInternetProtocolInfo(this);
        } catch (Throwable e) {
            Log.e("FMP_Helper", "init", e);
        }
    }

    private void writeFile(InputStream inputStream, File pathFile) throws IOException {
        File parentFile = pathFile.getParentFile();
        if (parentFile != null && !parentFile.exists()) {
            parentFile.mkdirs();
        } else if (pathFile.exists()) {
            pathFile.delete();
        }
        pathFile.createNewFile();
        // 获取文件的输出流
        FileOutputStream out = new FileOutputStream(pathFile);
        int len;
        byte[] buffer = new byte[1024];
        // 读取字节到缓冲区
        while ((len = inputStream.read(buffer)) != -1) {
            // 从缓冲区位置写入字节
            out.write(buffer, 0, len);
            //刷新缓冲区
            out.flush();
        }
        //关闭输出流
        out.close();
    }

    private void reUpdateManifest() throws IOException, JSONException {
        File index=new File(getApplication().getFilesDir(), "games/com.netease/rn/index.bundle");
        File manifest= new File(getApplication().getFilesDir(), "games/com.netease/manifest");
        FileInputStream inputStream = new FileInputStream(manifest);
        byte[] strByte = new byte[inputStream.available()];
        inputStream.read(strByte);
        String json = new String(strByte);
        inputStream.close();
        JSONObject object=new JSONObject(json);
        JSONObject assetsObject=object.getJSONObject("assets");
        JSONObject indexObject=assetsObject.getJSONObject("rn/index.bundle");
        String md5=getFileMD5(index.getAbsolutePath());
        Log.i("FMP_Helper", "index md5: "+md5);
        String oldMd5=indexObject.getString("md5");
        Log.i("FMP_Helper", "index oldMd5: "+oldMd5);
        indexObject.put("md5", md5);
        indexObject.put("md5check", md5);
        indexObject.put("size", index.length());
        String version=object.getString("version");
        Log.i("FMP_Helper", "version: "+version);
        Log.i("FMP_Helper", "size: "+index.length());
        manifest.delete();
        FileWriter fileWriter = new FileWriter(manifest, false);
        BufferedWriter writer = new BufferedWriter(fileWriter);
        writer.append(object.toString());
        writer.flush();
        writer.close();
        fileWriter.close();
        if (!md5.equals(oldMd5)){
            try {
                MainActivity mainActivity=MainActivity.getInstance();
                Method method=mainActivity.getClass().getMethod("nativeJsCallCpp", String.class);
                //method.setAccessible(true);
                method.invoke(mainActivity,"cpp.quitapp");
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                Log.e("FMP_Helper", "invoke", e);
            }
        }
    }

    @Override
    public void onBack(boolean bool, InternetProtocol internetProtocol) {
        if (internetProtocol != null) {
            try {
                sendBroadcast(TYPE_APP_NETWORK_INFO, internetProtocol.toJson());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void addEntity(long ent) {
        if (!worldEntityList.contains(ent)) {
            worldEntityList.add(ent);
        }
    }

    public void removeEntity(long ent) {
        if (worldEntityList.contains(ent)) {
            worldEntityList.remove(ent);
        }
    }

    public long[] getAllEntity() {
        int size = worldEntityList.size(), i = 0;
        long[] array = new long[size];
        for (Long l : worldEntityList) {
            array[i] = l.longValue();
            i++;
        }
        return array;
    }

    public void clearAllEntity() {
        worldEntityList.clear();
    }


    List<PluginItem> getPluginItems() {
        return pluginItems;
    }

    public void deleteCache(Application application) {
        deleteFile(application.getCacheDir());
        deleteFile(application.getCodeCacheDir());
        deleteFile(application.getFilesDir());
    }

    private void deleteFile(File file) {
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null)
                for (File f : files) {
                    deleteFile(f);
                }
        }
        file.delete();
    }

    public void initModsData(String modsData) {
        if (!TextUtils.isEmpty(modsData)) {
            pluginItems.clear();
            int i = 0;
            for (String mod : modsData.split(",")) {
                PluginItem item = new PluginItem();
                item.setPosition(i++);
                item.setEnable(true);
                if (mod.contains("'")) {
                    File file = new File(mod.substring(mod.indexOf("'") + 1));
                    item.setName(file.getName());
                    item.setPath(file.getAbsolutePath());
                    item.setSize(file.length());
                    item.setKey(Integer.parseInt(mod.substring(0, mod.indexOf("'"))));
                } else {
                    File file = new File(mod);
                    item.setName(file.getName());
                    item.setPath(file.getAbsolutePath());
                    item.setSize(file.length());
                    item.setKey(50);
                }
                pluginItems.add(item);
            }
        }
    }

    public boolean loadMod(File file, int key) {
        File file2 = new File(getApplication().getCacheDir(), file.getName());
        try {
            // 第一步文件的加密
            // 先用字节缓冲流读取文件
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            // 再用字节数组输出流将文件写到一个字节数组内
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            {
                byte[] bytes = new byte[1024];
                int len;
                while ((len = bufferedInputStream.read(bytes)) != -1) {
                    byteArrayOutputStream.write(bytes, 0, len);
                }
                bufferedInputStream.close();
            }
            // 将字节数组输出流内的内容转换成一个字节数组
            byte[] byteArr = decryptFile(key, byteArrayOutputStream.toByteArray());
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArr);
            // 用字节缓冲输出流将数组内容写到具体的位置
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file2));
            {
                byte[] bytes = new byte[1024];
                int len;
                while ((len = byteArrayInputStream.read(bytes)) != -1) {
                    bufferedOutputStream.write(bytes, 0, len);
                }
                bufferedOutputStream.close();
            }
            loadScript(file2);
            return true;
        } catch (Exception e) {
            file2.delete();
            return false;
        } finally {
            file2.delete();
        }
    }

    public void loadTexture(String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (file.exists()) {
                if (filePath.endsWith(".mcskin")) {
                    //皮肤
                    try {
                        if (skinPack == null) {
                            skinPack = new SkinPack(new File(filePath));
                            launcherRuntime.getTextureOverrides().add(skinPack);
                        } else {
                            skinPack.addPackage(new File(filePath));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (filePath.endsWith(".mcpack")) {
                    //材质光影
                    try {
                        if (texturePack == null) {
                            texturePack = new TexturePack();
                            launcherRuntime.getTextureOverrides().add(texturePack);
                        }
                        if (filePath.contains(",")) {
                            String[] files = filePath.split(",");
                            for (String str : files) {
                                texturePack.addPackage(new File(str));
                            }
                        } else {
                            texturePack.addPackage(new File(filePath));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void loadScript(Reader in, String sourceName) throws InterruptedException {
        // Rhino needs lots of recursion depth to parse nested else ifs
        // dalvik vm/Thread.h specifies 256K as maximum stack size
        // default thread depth is 16K (8K on old devices, 1K on super-low-end
        // devices)
        JavaScript parseRunner = new JavaScript(in, sourceName);
        Thread thread = new Thread(Thread.currentThread().getThreadGroup(), parseRunner,
                "Helper parse thread", 256 * 1024);
        thread.start();
        thread.join(); // block on this thread
    }

    private class JavaScript implements Runnable {
        private Reader in;
        private String sourceName;

        JavaScript(Reader paramReader, String paramString) {
            this.in = paramReader;
            this.sourceName = paramString;
        }

        public void run() {
            try {
                com.mozilla.javascript.Context localContext = com.mozilla.javascript.Context.enter();
                localContext.setOptimizationLevel(-1);
                localContext.setLanguageVersion(200);
                localContext.setClassShutter(new SandboxClassShutter());
                initJustLoadedScript(localContext, localContext.compileReader(this.in, this.sourceName, 0, null), this.sourceName);
            } catch (Throwable localException) {
                GameManager.printException(localException);
            }
            com.mozilla.javascript.Context.exit();
        }

        void initJustLoadedScript(com.mozilla.javascript.Context ctx, Script script, String sourceName) {
            ScriptableObject scope = ctx.initStandardObjects(new BlockHostObject(), false);
            ScriptState state = new ScriptState(script, scope, sourceName);
            scope.defineFunctionProperties(getAllFunctions(), BlockHostObject.class, 0);
            try {
                ScriptableObject.defineClass(scope, NativePlayerApi.class);
                ScriptableObject.defineClass(scope, NativeLevelApi.class);
                ScriptableObject.defineClass(scope, NativeEntityApi.class);
                ScriptableObject.defineClass(scope, NativeModPEApi.class);
                ScriptableObject.defineClass(scope, NativeItemApi.class);
                ScriptableObject.defineClass(scope, NativeBlockApi.class);
                ScriptableObject.defineClass(scope, NativeServerApi.class);
                ScriptableObject.defineClass(scope, NativeBuildApi.class);
                ScriptableObject.defineClass(scope, HelperApi.class);
                RendererManager.defineClasses(scope);
                Class<?>[] constantsClasses = new Class[]{ChatColor.class, ItemCategory.class, ParticleType.class, EntityType.class, EntityRenderType.class, ArmorType.class, MobEffect.class, DimensionId.class, BlockFace.class, UseAnimation.class, Enchantment.class, EnchantType.class, BlockRenderLayer.class};
                for (Class clazz : constantsClasses) {
                    ScriptableObject.putProperty(scope, clazz.getSimpleName(), classConstantsToJSObject(clazz));
                }
            } catch (Exception e) {
                e.printStackTrace();
                GameManager.printException(e);
            }
            script.exec(ctx, scope);
            scripts.add(state);
        }

        private String[] getAllFunctions() {
            List<String> allList = new ArrayList<>();
            for (Method method : BlockHostObject.class.getMethods()) {
                if (method.getAnnotation(JSFunction.class) != null) {
                    allList.add(method.getName());
                }
            }
            return allList.toArray(PatchManager.blankArray);
        }

        ScriptableObject classConstantsToJSObject(Class<?> clazz) {
            ScriptableObject obj = new NativeObject();
            for (Field field : clazz.getFields()) {
                int fieldModifiers = field.getModifiers();
                if (Modifier.isStatic(fieldModifiers) && Modifier.isPublic(fieldModifiers)) {
                    try {
                        obj.putConst(field.getName(), obj, field.get(null));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return obj;
        }
    }

    public class PluginItem {
        //加载位置
        private int position;
        //名称
        private String name;
        //完整路径
        private String path;
        //大小
        private long size;
        //是否加载
        private boolean enable;
        //解密key
        private int key;

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }
    }

    public class SkinPack implements com.mcbox.pesdkb.mcpelauncher.TexturePack {
        private List<ZipTexturePack> subPacks = new ArrayList();

        public SkinPack(File file) throws IOException {
            this.subPacks.add(new ZipTexturePack(file));
        }

        public void addPackage(File file) throws IOException {
            this.subPacks.add(new ZipTexturePack(file));
        }

        public void removePackage(String fileName) throws IOException {
            for (int i = this.subPacks.size() - 1; i >= 0; i--) {
                ZipTexturePack pack = this.subPacks.get(i);
                if (pack.getZipName().equals(fileName)) {
                    pack.close();
                    this.subPacks.remove(i);
                    return;
                }
            }
        }

        public InputStream getInputStream(String fileName) throws IOException {
            String prefix = "skin_packs/vanilla/";
            if (fileName.startsWith(prefix)) {
                fileName = fileName.substring(prefix.length());
            }
            for (ZipTexturePack pack : this.subPacks) {
                InputStream is = pack.getInputStream(fileName);
                if (is != null) {
                    return is;
                }
            }
            return null;
        }

        public long getSize(String fileName) throws IOException {
            for (ZipTexturePack pack : this.subPacks) {
                long size = pack.getSize(fileName);
                if (size != -1) {
                    return size;
                }
            }
            return -1;
        }

        public void close() throws IOException {
            for (ZipTexturePack pack : this.subPacks) {
                pack.close();
            }
            this.subPacks.clear();
        }

        public List<String> listFiles() throws IOException {
            List<String> list = new ArrayList();
            for (ZipTexturePack pack : this.subPacks) {
                list.addAll(pack.listFiles());
            }
            return list;
        }

        public List<ZipTexturePack> listAllPacks() {
            return this.subPacks;
        }
    }

    public class TexturePack implements com.mcbox.pesdkb.mcpelauncher.TexturePack {
        private List<ZipTexturePack> subPacks = new ArrayList();


        public void addPackage(File file) throws IOException {
            this.subPacks.add(new ZipTexturePack(file));
        }

        public void removePackage(String fileName) throws IOException {
            for (int i = this.subPacks.size() - 1; i >= 0; i--) {
                ZipTexturePack pack = this.subPacks.get(i);
                if (pack.getZipName().equals(fileName)) {
                    pack.close();
                    this.subPacks.remove(i);
                    return;
                }
            }
        }

        public InputStream getInputStream(String fileName) throws IOException {
            if (fileName.contains("manifest.json"))
                return null;

            for (ZipTexturePack pack : this.subPacks) {
                InputStream is = pack.getInputStream(fileName);
                if (is != null) {
                    return is;
                }
            }
            return null;
        }

        public long getSize(String fileName) throws IOException {
            if (fileName.contains("manifest.json"))
                return -1;
            String prefix = "resource_packs/vanilla/";
            if (fileName.startsWith(prefix) && !fileName.startsWith("shaders")) {
                fileName = fileName.substring(prefix.length());
            }
            for (ZipTexturePack pack : this.subPacks) {
                long size = pack.getSize(fileName);
                if (size != -1) {
                    return size;
                }
            }
            return -1;
        }

        public void close() throws IOException {
            for (ZipTexturePack pack : this.subPacks) {
                pack.close();
            }
            this.subPacks.clear();
        }

        public List<String> listFiles() throws IOException {
            List<String> retval = new ArrayList();
            for (ZipTexturePack pack : this.subPacks) {
                retval.addAll(pack.listFiles());
            }
            return retval;
        }

        public List<ZipTexturePack> listAllPacks() {
            return this.subPacks;
        }
    }
}
