package com.fmp.core;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;

import com.mcbox.pesdk.security.SandboxNativeJavaObject;
import com.mcbox.pesdkb.mcpelauncher.ScriptManager;
import com.mcbox.pesdkd.mcpelauncher.ScriptManagerD;

import com.mozilla.javascript.NativeJavaObject;
import com.mozilla.javascript.Scriptable;
import com.mozilla.javascript.ScriptableObject;
import com.mozilla.javascript.annotations.JSStaticFunction;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;

public class HelperApi extends ScriptableObject {
    private static final long serialVersionUID = 6806477747348824645L;

    /*//js初始化插件方法
    @JSStaticFunction
    public static HelperApi init(String id) {
        return GameManager.initPluginApi(id);
    }

    @JSStaticFunction
    public static Integer getApiVersion() {
        return 2;
    }

    @JSStaticFunction
    public static void PlayerSetOperator(String id, boolean operator) {
        GameManager.nativePlayerSetOperator(id, operator);
    }

    @JSStaticFunction
    public static boolean PlayerAttack(String id, Object ent) {
        return GameManager.nativePlayerAttack(id, ScriptManager.getEntityId(ent));
    }

    @JSStaticFunction
    public static void PlayerAttacks(String id, Scriptable ents) {
        GameManager.nativePlayerAttacks(id,scriptableToLongArray(ents));
    }

    @JSStaticFunction
    public static long[] LevelGetAllPlayer() {
        return GameManager.nativeLevelGetAllPlayer();
    }

    @JSStaticFunction
    public static void ItemSetAllowOffhand(int id, boolean b) {
        GameManager.nativeItemSetAllowOffhand((short) id, b);
    }*/

    @JSStaticFunction
    public static void loadJS(String path) {
        File file = new File(path);
        if (file.exists())
            GameManager.loadScript(file);
    }

    @JSStaticFunction
    public static String getUserUUID() {
        return GameManager.getUserUUID();
    }

    @JSStaticFunction
    public static String getIMEI() {
        Context ctx = ScriptManager.androidContext;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            try {
                TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ctx.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    if (tm != null) {
                        return tm.getDeviceId();
                    }
                }
            } catch (Exception e) {
                return "no IMEI";
            }
        } else {
            return "this is android 10";
        }
        return "no permission";
    }

    @JSStaticFunction
    public static boolean loadMod(String modString, int key) {
        for (GameManager.PluginItem item : GameManager.getInstance().getPluginItems()) {
            if (item.getKey() == key && item.isEnable()) {
                File file = new File(ScriptManager.androidContext.getCacheDir(), String.valueOf(modString.length()));
                try {
                    // 将字节数组输出流内的内容转换成一个字节数组
                    byte[] byteArr = GameManager.decryptFile(key, Base64.decode(modString, Base64.NO_WRAP));
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArr);
                    // 用字节缓冲输出流将数组内容写到具体的位置
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
                    {
                        byte[] bytes = new byte[1024];
                        int len;
                        while ((len = byteArrayInputStream.read(bytes)) != -1) {
                            bufferedOutputStream.write(bytes, 0, len);
                        }
                        bufferedOutputStream.close();
                    }
                    GameManager.loadScript(file);
                } catch (Exception e) {
                    GameManager.printException(e);
                    GameManager.sendBroadcast(GameManager.TYPE_LOAD_JS_ERR, "插件");
                    return false;
                }
                return file.delete();
            }
        }
        GameManager.sendBroadcast(GameManager.TYPE_LOAD_JS_ERR, "插件");
        return false;
    }

    @JSStaticFunction
    public static void setOffhandSlot(String ent, Integer id, Integer count, Integer damage) {

    }

    @JSStaticFunction
    public static void AddEnchantItemInventory(Integer itemId, Integer count, Integer itemDamage, Scriptable enchants, Integer enchantsLength, String itemName) {
        ScriptManagerD.nativeAddEnchantItemInventory(itemId, count, itemDamage, scriptableToIntArray(enchants), enchantsLength, itemName);
    }

    @JSStaticFunction
    public static void AddFurnaceRecipe(int i, int i2, int i3) {
        ScriptManagerD.nativeAddFurnaceRecipe(i, i2, i3);
    }

    @JSStaticFunction
    public static void AddItemChest(int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        ScriptManagerD.nativeAddItemChest(i, i2, i3, i4, i5, i6, i7);
    }

    @JSStaticFunction
    public static void AddItemCreativeInv(int i, int i2, int i3) {
        ScriptManagerD.nativeAddItemCreativeInv(i, i2, i3);
    }

    @JSStaticFunction
    public static void AddItemFurnace(int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        ScriptManagerD.nativeAddItemFurnace(i, i2, i3, i4, i5, i6, i7);
    }

    @JSStaticFunction
    public static void AddItemInventory(int i, int i2, int i3) {
        ScriptManagerD.nativeAddItemInventory(i, i2, i3);
    }

    @JSStaticFunction
    public static void AddItemInventoryName(int i, int i2, int i3, String str) {
        ScriptManagerD.nativeAddItemInventory(i, i2, i3, str);
    }

    @JSStaticFunction
    public static void AddShapedRecipe(int ii, int i2, int i3, Scriptable strArr, Scriptable iArr) {
        ScriptManagerD.nativeAddShapedRecipe(ii, i2, i3, scriptableToStringArray(strArr), scriptableToIntArray(iArr));
    }

    @JSStaticFunction
    public static void ArmorAddQueuedTextures() {
        ScriptManagerD.nativeArmorAddQueuedTextures();
    }

    @JSStaticFunction
    public static String BiomeIdToName(int i) {
        return ScriptManagerD.nativeBiomeIdToName(i);
    }

    @JSStaticFunction
    public static double BlockGetDestroyTime(int i, int i2) {
        return ScriptManagerD.nativeBlockGetDestroyTime(i, i2);
    }

    @JSStaticFunction
    public static double BlockGetFriction(int i) {
        return ScriptManagerD.nativeBlockGetFriction(i);
    }

    @JSStaticFunction
    public static int BlockGetSecondPart(int i, int i2, int i3, int i4) {
        return ScriptManagerD.nativeBlockGetSecondPart(i, i2, i3, i4);
    }

    @JSStaticFunction
    public static void BlockSetCollisionEnabled(int i, boolean z) {
        ScriptManagerD.nativeBlockSetCollisionEnabled(i, z);
    }

    @JSStaticFunction
    public static void BlockSetColor(int ii, Scriptable iArr) {
        ScriptManagerD.nativeBlockSetColor(ii, scriptableToIntArray(iArr));
    }

    @JSStaticFunction
    public static void BlockSetDestroyTime(int i, double f) {
        ScriptManagerD.nativeBlockSetDestroyTime(i, (float) f);
    }

    @JSStaticFunction
    public static void BlockSetExplosionResistance(int i, double f) {
        ScriptManagerD.nativeBlockSetExplosionResistance(i, (float) f);
    }

    @JSStaticFunction
    public static void BlockSetFriction(int i, double f) {
        ScriptManagerD.nativeBlockSetFriction(i, (float) f);
    }

    @JSStaticFunction
    public static void BlockSetLightLevel(int i, int i2) {
        ScriptManagerD.nativeBlockSetLightLevel(i, i2);
    }

    @JSStaticFunction
    public static void BlockSetLightOpacity(int i, int i2) {
        ScriptManagerD.nativeBlockSetLightOpacity(i, i2);
    }

    @JSStaticFunction
    public static void BlockSetRedstoneConsumer(int i, boolean z) {
        ScriptManagerD.nativeBlockSetRedstoneConsumer(i, z);
    }

    @JSStaticFunction
    public static void BlockSetRenderLayer(int i, int i2) {
        ScriptManagerD.nativeBlockSetRenderLayer(i, i2);
    }

    @JSStaticFunction
    public static void BlockSetShape(int i, double f, double f2, double f3, double f4, double f5, double f6) {
        ScriptManagerD.nativeBlockSetShape(i, (float) f, (float) f2, (float) f3, (float) f4, (float) f5, (float) f6);
    }

    @JSStaticFunction
    public static void BlockSetShapeCount(int i, double f, double f2, double f3, double f4, double f5, double f6, int i2) {
        ScriptManagerD.nativeBlockSetShape(i, (float) f, (float) f2, (float) f3, (float) f4, (float) f5, (float) f6, i2);
    }

    @JSStaticFunction
    public static void BlockSetStepSound(int i, int i2) {
        ScriptManagerD.nativeBlockSetStepSound(i, i2);
    }

    @JSStaticFunction
    public static void ChangeGuiHideStatus() {
        ScriptManagerD.nativeChangeGuiHideStatus();
    }

    @JSStaticFunction
    public static void ClearCapes() {
        ScriptManagerD.nativeClearCapes();
    }

    @JSStaticFunction
    public static void ClearSlotInventory(int i) {
        ScriptManagerD.nativeClearSlotInventory(i);
    }

    @JSStaticFunction
    public static void ClientMessage(String str) {
        ScriptManagerD.nativeClientMessage(str);
    }

    @JSStaticFunction
    public static void CloseScreen() {
        ScriptManagerD.nativeCloseScreen();
    }

    @JSStaticFunction
    public static void DefineArmor(int i, String str, int i2, String str2, String str3, int i3, int i4, int i5) {
        ScriptManagerD.nativeDefineArmor(i, str, i2, str2, str3, i3, i4, i5);
    }

    @JSStaticFunction
    public static void DefineBlock(int i, String str, Scriptable strArr, Scriptable iArr, int i2, boolean z, int i3) {
        ScriptManagerD.nativeDefineBlock(i, str, scriptableToStringArray(strArr), scriptableToIntArray(iArr), i2, z, i3);
    }

    @JSStaticFunction
    public static void DefineBlockCount(int i, String str, Scriptable strArr, Scriptable iArr, int i2, boolean z, int i3, int i4) {
        ScriptManagerD.nativeDefineBlock(i, str, scriptableToStringArray(strArr), scriptableToIntArray(iArr), i2, z, i3, i4);
    }

    @JSStaticFunction
    public static void DefineFoodItem(int i, String str, int i2, int i3, String str2, int i4) {
        ScriptManagerD.nativeDefineFoodItem(i, str, i2, i3, str2, i4);
    }

    @JSStaticFunction
    public static void DefineItem(int i, String str, int i2, String str2, int i3) {
        ScriptManagerD.nativeDefineItem(i, str, i2, str2, i3);
    }

    @JSStaticFunction
    public static void DefinePlaceholderBlocks() {
        ScriptManagerD.nativeDefinePlaceholderBlocks();
    }

    @JSStaticFunction
    public static void DestroyBlock(int i, int i2, int i3) {
        ScriptManagerD.nativeDestroyBlock(i, i2, i3);
    }

    @JSStaticFunction
    public static long DropItem(double f, double f2, double f3, double f4, int i, int i2, int i3) {
        return ScriptManagerD.nativeDropItem((float) f, (float) f2, (float) f3, (float) f4, i, i2, i3);
    }

    @JSStaticFunction
    public static void DumpVtable(String str, int i) {
        ScriptManagerD.nativeDumpVtable(str, i);
    }

    @JSStaticFunction
    public static void EnableFlyButton(boolean z) {
        ScriptManagerD.nativeEnableFlyButton(z);
    }

    @JSStaticFunction
    public static String EntityGetMobSkin(int i) {
        return ScriptManagerD.nativeEntityGetMobSkin(i);
    }

    @JSStaticFunction
    public static String EntityGetMobSkinEnt(Object j) {
        return ScriptManagerD.nativeEntityGetMobSkin((long) j);
    }

    @JSStaticFunction
    public static String EntityGetNameTag(int i) {
        return ScriptManagerD.nativeEntityGetNameTag(i);
    }

    @JSStaticFunction
    public static String EntityGetNameTagEnt(Object j) {
        return ScriptManagerD.nativeEntityGetNameTag((long) j);
    }

    @JSStaticFunction
    public static int EntityGetRenderType(int i) {
        return ScriptManagerD.nativeEntityGetRenderType(i);
    }

    @JSStaticFunction
    public static int EntityGetRenderTypeEnt(Object j) {
        return ScriptManagerD.nativeEntityGetRenderType((long) j);
    }

    @JSStaticFunction
    public static int EntityGetRider(int i) {
        return ScriptManagerD.nativeEntityGetRider(i);
    }

    @JSStaticFunction
    public static int EntityGetRiderEnt(Object j) {
        return ScriptManagerD.nativeEntityGetRider((long) j);
    }

    @JSStaticFunction
    public static int EntityGetRiding(int i) {
        return ScriptManagerD.nativeEntityGetRiding(i);
    }

    @JSStaticFunction
    public static int EntityGetRidingEnt(Object j) {
        return ScriptManagerD.nativeEntityGetRiding((long) j);
    }

    @JSStaticFunction
    public static long[] EntityGetUUID(int i) {
        return ScriptManagerD.nativeEntityGetUUID(i);
    }

    @JSStaticFunction
    public static long[] EntityGetUUIDEnt(Object j) {
        return ScriptManagerD.nativeEntityGetUUID((long) j);
    }

    @JSStaticFunction
    public static void EntitySetNameTag(int i, String str) {
        ScriptManagerD.nativeEntitySetNameTag(i, str);
    }

    @JSStaticFunction
    public static void EntitySetNameTagEnt(Object j, String str) {
        ScriptManagerD.nativeEntitySetNameTag((long) j, str);
    }

    @JSStaticFunction
    public static void EntitySetSize(int i, double f, double f2) {
        ScriptManagerD.nativeEntitySetSize(i, (float) f, (float) f2);
    }

    @JSStaticFunction
    public static void EntitySetSizeEnt(Object j, double f, double f2) {
        ScriptManagerD.nativeEntitySetSize((long) j, (float) f, (float) f2);
    }

    @JSStaticFunction
    public static void Explode(double f, double f2, double f3, double f4) {
        ScriptManagerD.nativeExplode((float) f, (float) f2, (float) f3, (float) f4);
    }

    @JSStaticFunction
    public static void IsExplode(double f, double f2, double f3, double f4, boolean z) {
        ScriptManagerD.nativeExplode((float) f, (float) f2, (float) f3, (float) f4, z);
    }

    @JSStaticFunction
    public static void ExtinguishFire(int i, int i2, int i3, int i4) {
        ScriptManagerD.nativeExtinguishFire(i, i2, i3, i4);
    }

    @JSStaticFunction
    public static void ForbidBlockUpdate(boolean z) {
        ScriptManagerD.nativeForbidBlockUpdate(z);
    }

    @JSStaticFunction
    public static void ForceCrash() {
        ScriptManagerD.nativeForceCrash();
    }

    @JSStaticFunction
    public static void GatherMob(double f) {
        ScriptManagerD.nativeGatherMob((float) f);
    }

    @JSStaticFunction
    public static void GetAllEntities() {
        ScriptManagerD.nativeGetAllEntities();
    }

    @JSStaticFunction
    public static int GetAnimalAge(int i) {
        return ScriptManagerD.nativeGetAnimalAge(i);
    }

    @JSStaticFunction
    public static int GetAnimalAgeEnt(Object j) {
        return ScriptManagerD.nativeGetAnimalAge((long) j);
    }

    @JSStaticFunction
    public static int GetArch() {
        return ScriptManagerD.nativeGetArch();
    }

    @JSStaticFunction
    public static int GetBlockRenderShape(int i) {
        return ScriptManagerD.nativeGetBlockRenderShape(i);
    }

    @JSStaticFunction
    public static int GetBrightness(int i, int i2, int i3) {
        return ScriptManagerD.nativeGetBrightness(i, i2, i3);
    }

    @JSStaticFunction
    public static int GetCarriedItem(int i) {
        return ScriptManagerD.nativeGetCarriedItem(i);
    }

    @JSStaticFunction
    public static String[] GetCommandBlockConfig() {
        return ScriptManagerD.nativeGetCommandBlockConfig();
    }

    @JSStaticFunction
    public static int GetData(int i, int i2, int i3) {
        return ScriptManagerD.nativeGetData(i, i2, i3);
    }

    @JSStaticFunction
    public static int GetEnableDieFall() {
        return ScriptManagerD.nativeGetEnableDieFall();
    }

    @JSStaticFunction
    public static double GetEntityLoc(int i, int i2) {
        return ScriptManagerD.nativeGetEntityLoc(i, i2);
    }

    @JSStaticFunction
    public static double GetEntityLocEnt(Object j, int i) {
        return ScriptManagerD.nativeGetEntityLoc((long) j, i);
    }

    @JSStaticFunction
    public static int GetEntityTypeId(int i) {
        return ScriptManagerD.nativeGetEntityTypeId(i);
    }

    @JSStaticFunction
    public static int GetEntityTypeIdEnt(Object j) {
        return ScriptManagerD.nativeGetEntityTypeId((long) j);
    }

    @JSStaticFunction
    public static double GetEntityVel(int i, int i2) {
        return ScriptManagerD.nativeGetEntityVel(i, i2);
    }

    @JSStaticFunction
    public static double GetEntityVelEnt(Object j, int i) {
        return ScriptManagerD.nativeGetEntityVel((long) j, i);
    }

    @JSStaticFunction
    public static double GetGUIscale() {
        return ScriptManagerD.nativeGetGUIscale();
    }

    @JSStaticFunction
    public static int GetGameType() {
        return ScriptManagerD.nativeGetGameType();
    }

    @JSStaticFunction
    public static boolean GetGuiHideStatus() {
        return ScriptManagerD.nativeGetGuiHideStatus();
    }

    @JSStaticFunction
    public static String GetI18NString(String str) {
        return ScriptManagerD.nativeGetI18NString(str);
    }

    @JSStaticFunction
    public static String GetInventoryCustomName(int i) {
        return ScriptManagerD.nativeGetInventoryCustomName(i);
    }

    @JSStaticFunction
    public static int GetItemChest(int i, int i2, int i3, int i4) {
        return ScriptManagerD.nativeGetItemChest(i, i2, i3, i4);
    }

    @JSStaticFunction
    public static int GetItemCountChest(int i, int i2, int i3, int i4) {
        return ScriptManagerD.nativeGetItemCountChest(i, i2, i3, i4);
    }

    @JSStaticFunction
    public static int GetItemCountFurnace(int i, int i2, int i3, int i4) {
        return ScriptManagerD.nativeGetItemCountFurnace(i, i2, i3, i4);
    }

    @JSStaticFunction
    public static int GetItemDataChest(int i, int i2, int i3, int i4) {
        return ScriptManagerD.nativeGetItemDataChest(i, i2, i3, i4);
    }

    @JSStaticFunction
    public static int GetItemDataFurnace(int i, int i2, int i3, int i4) {
        return ScriptManagerD.nativeGetItemDataFurnace(i, i2, i3, i4);
    }

    @JSStaticFunction
    public static int GetItemEntityItem(Object j, int i) {
        return ScriptManagerD.nativeGetItemEntityItem((long) j, i);
    }

    @JSStaticFunction
    public static int GetItemFurnace(int i, int i2, int i3, int i4) {
        return ScriptManagerD.nativeGetItemFurnace(i, i2, i3, i4);
    }

    @JSStaticFunction
    public static int GetItemIdCount() {
        return ScriptManagerD.nativeGetItemIdCount();
    }

    @JSStaticFunction
    public static String GetItemName(int i, int i2, boolean z) {
        return ScriptManagerD.nativeGetItemName(i, i2, z);
    }

    @JSStaticFunction
    public static String GetLanguageName() {
        return ScriptManagerD.nativeGetLanguageName();
    }

    @JSStaticFunction

    public static long GetLevel() {
        return ScriptManagerD.nativeGetLevel();
    }

    @JSStaticFunction
    public static int GetMobHealth(int i) {
        return ScriptManagerD.nativeGetMobHealth(i);
    }

    @JSStaticFunction
    public static int GetMobHealthEnt(Object j) {
        return ScriptManagerD.nativeGetMobHealth((long) j);
    }

    @JSStaticFunction
    public static int GetMobMaxHealth(Object j) {
        return ScriptManagerD.nativeGetMobMaxHealth((long) j);
    }

    @JSStaticFunction
    public static double GetPitch(int i) {
        return ScriptManagerD.nativeGetPitch(i);
    }

    @JSStaticFunction
    public static double GetPitchEnt(Object j) {
        return ScriptManagerD.nativeGetPitch((long) j);
    }

    @JSStaticFunction
    public static long GetPlayerEnt() {
        return ScriptManagerD.nativeGetPlayerEnt();
    }

    @JSStaticFunction
    public static String[] GetPlayerList() {
        return ScriptManagerD.nativeGetPlayerList();
    }

    @JSStaticFunction
    public static double GetPlayerLoc(int i) {
        return ScriptManagerD.nativeGetPlayerLoc(i);
    }

    @JSStaticFunction
    public static String GetPlayerName(int i) {
        return ScriptManagerD.nativeGetPlayerName(i);
    }

    @JSStaticFunction
    public static String GetPlayerNameEnt(Object j) {
        return ScriptManagerD.nativeGetPlayerName((long) j);
    }

    @JSStaticFunction
    public static int GetReSpawnPos(int i) {
        return ScriptManagerD.nativeGetReSpawnPos(i);
    }

    @JSStaticFunction
    public static int GetSelectedSlotId() {
        return ScriptManagerD.nativeGetSelectedSlotId();
    }

    @JSStaticFunction
    public static int GetServerMaxConnection() {
        return ScriptManagerD.nativeGetServerMaxConnection();
    }

    @JSStaticFunction
    public static String GetSignText(int i, int i2, int i3, int i4) {
        return ScriptManagerD.nativeGetSignText(i, i2, i3, i4);
    }

    @JSStaticFunction
    public static int GetSlotArmor(int i, int i2) {
        return ScriptManagerD.nativeGetSlotArmor(i, i2);
    }

    @JSStaticFunction
    public static int GetSlotInventory(int i, int i2) {
        return ScriptManagerD.nativeGetSlotInventory(i, i2);
    }

    @JSStaticFunction
    public static int[] GetSlotInventoryEnchants(int i, int i2) {
        return ScriptManagerD.nativeGetSlotInventoryEnchants(i, i2);
    }

    @JSStaticFunction
    public static boolean GetTextureCoordinatesForItem(int i, int i2, Scriptable fArr) {
        return ScriptManagerD.nativeGetTextureCoordinatesForItem(i, i2, scriptableToFloatArray(fArr));
    }

    @JSStaticFunction
    public static int GetTile(int i, int i2, int i3) {
        return ScriptManagerD.nativeGetTile(i, i2, i3);
    }

    @JSStaticFunction
    public static long GetTime() {
        return ScriptManagerD.nativeGetTime();
    }

    @JSStaticFunction
    public static int GetTimeStop() {
        return ScriptManagerD.nativeGetTimeStop();
    }

    @JSStaticFunction
    public static double GetYaw(int i) {
        return ScriptManagerD.nativeGetYaw(i);
    }

    @JSStaticFunction
    public static double GetYawEnt(Object j) {
        return ScriptManagerD.nativeGetYaw((long) j);
    }

    @JSStaticFunction
    public static String Getsvnversion() {
        return ScriptManagerD.nativeGetsvnversion();
    }

    @JSStaticFunction
    public static String Gettransmute(String str, int i, int i2) {
        return ScriptManagerD.nativeGettransmute(str, i, i2);
    }

    @JSStaticFunction
    public static void HurtTo(int i) {
        ScriptManagerD.nativeHurtTo(i);
    }

    @JSStaticFunction
    public static boolean IsSneaking(Object j) {
        return ScriptManagerD.nativeIsSneaking((long) j);
    }

    @JSStaticFunction
    public static boolean IsValidItem(int i) {
        return ScriptManagerD.nativeIsValidItem(i);
    }

    @JSStaticFunction
    public static int ItemGetUseAnimation(int i) {
        return ScriptManagerD.nativeItemGetUseAnimation(i);
    }

    @JSStaticFunction
    public static void ItemSetIconTextures() {
        ScriptManagerD.nativeItemSetIconTextures();
    }

    @JSStaticFunction
    public static boolean ItemSetProperties(int i, String str) {
        return ScriptManagerD.nativeItemSetProperties(i, str);
    }

    @JSStaticFunction
    public static void ItemSetStackedByData(int i, boolean z) {
        ScriptManagerD.nativeItemSetStackedByData(i, z);
    }

    @JSStaticFunction
    public static void ItemSetUseAnimation(int i, int i2) {
        ScriptManagerD.nativeItemSetUseAnimation(i, i2);
    }

    @JSStaticFunction
    public static void JoinServer(String str, int i) {
        ScriptManagerD.nativeJoinServer(str, i);
    }

    @JSStaticFunction
    public static void LeaveGame(boolean z) {
        ScriptManagerD.nativeLeaveGame(z);
    }

    @JSStaticFunction
    public static void LevelAddParticle(int i, double f, double f2, double f3, double f4, double f5, double f6, int i2) {
        ScriptManagerD.nativeLevelAddParticle(i, (float) f, (float) f2, (float) f3, (float) f4, (float) f5, (float) f6, i2);
    }

    @JSStaticFunction
    public static boolean LevelCanSeeSky(int i, int i2, int i3) {
        return ScriptManagerD.nativeLevelCanSeeSky(i, i2, i3);
    }

    @JSStaticFunction
    public static int LevelGetBiome(int i, int i2) {
        return ScriptManagerD.nativeLevelGetBiome(i, i2);
    }

    @JSStaticFunction
    public static String LevelGetBiomeName(int i, int i2) {
        return ScriptManagerD.nativeLevelGetBiomeName(i, i2);
    }

    @JSStaticFunction
    public static int LevelGetDifficulty() {
        return ScriptManagerD.nativeLevelGetDifficulty();
    }

    @JSStaticFunction
    public static int LevelGetGrassColor(int i, int i2) {
        return ScriptManagerD.nativeLevelGetGrassColor(i, i2);
    }

    @JSStaticFunction
    public static double LevelGetLightningLevel() {
        return ScriptManagerD.nativeLevelGetLightningLevel();
    }

    @JSStaticFunction
    public static double LevelGetRainLevel() {
        return ScriptManagerD.nativeLevelGetRainLevel();
    }

    @JSStaticFunction
    public static boolean LevelIsRemote() {
        return ScriptManagerD.nativeLevelIsRemote();
    }

    @JSStaticFunction
    public static void LevelSetBiome(int i, int i2, int i3) {
        ScriptManagerD.nativeLevelSetBiome(i, i2, i3);
    }

    @JSStaticFunction
    public static void LevelSetDifficulty(int i) {
        ScriptManagerD.nativeLevelSetDifficulty(i);
    }

    @JSStaticFunction
    public static void LevelSetGrassColor(int i, int i2, int i3) {
        ScriptManagerD.nativeLevelSetGrassColor(i, i2, i3);
    }

    @JSStaticFunction
    public static void LevelSetLightningLevel(double f) {
        ScriptManagerD.nativeLevelSetLightningLevel((float) f);
    }

    @JSStaticFunction
    public static void LevelSetRainLevel(double f) {
        ScriptManagerD.nativeLevelSetRainLevel((float) f);
    }

    @JSStaticFunction
    public static void MobAddEffect(Object j, int i, int i2, int i3, boolean z, boolean z2) {
        ScriptManagerD.nativeMobAddEffect((long) j, i, i2, i3, z, z2);
    }

    @JSStaticFunction
    public static int MobGetArmor(Object j, int i, int i2) {
        return ScriptManagerD.nativeMobGetArmor((long) j, i, i2);
    }

    @JSStaticFunction
    public static void MobRemoveAllEffects(Object j) {
        ScriptManagerD.nativeMobRemoveAllEffects((long) j);
    }

    @JSStaticFunction
    public static void MobRemoveEffect(Object j, int i) {
        ScriptManagerD.nativeMobRemoveEffect((long) j, i);
    }

    @JSStaticFunction
    public static void MobSetArmor(Object j, int i, int i2, int i3) {
        ScriptManagerD.nativeMobSetArmor((long) j, i, i2, i3);
    }

    @JSStaticFunction
    public static void NeeddecryptMapData(Scriptable bArr, int i) {
        ScriptManagerD.nativeNeeddecryptMapData(scriptableToByteArray(bArr), i);
    }

    @JSStaticFunction
    public static void OnGraphicsReset() {
        ScriptManagerD.nativeOnGraphicsReset();
    }

    @JSStaticFunction
    public static void PlayNoteBlock(double f) {
        ScriptManagerD.nativePlayNoteBlock((float) f);
    }

    @JSStaticFunction
    public static void PlaySound(double f, double f2, double f3, String str, double f4, double f5) {
        ScriptManagerD.nativePlaySound((float) f, (float) f2, (float) f3, str, (float) f4, (float) f5);
    }

    @JSStaticFunction
    public static void PlayerAddExperience(int i) {
        ScriptManagerD.nativePlayerAddExperience(i);
    }

    @JSStaticFunction
    public static boolean PlayerCanFly() {
        return ScriptManagerD.nativePlayerCanFly();
    }

    @JSStaticFunction
    public static boolean PlayerEnchant(int i, int i2, int i3) {
        return ScriptManagerD.nativePlayerEnchant(i, i2, i3);
    }

    @JSStaticFunction
    public static int PlayerGetDimension() {
        return ScriptManagerD.nativePlayerGetDimension();
    }

    @JSStaticFunction
    public static int[] PlayerGetEnchantments(int i) {
        return ScriptManagerD.nativePlayerGetEnchantments(i);
    }

    @JSStaticFunction
    public static double PlayerGetExhaustion() {
        return ScriptManagerD.nativePlayerGetExhaustion();
    }

    @JSStaticFunction
    public static double PlayerGetExperience() {
        return ScriptManagerD.nativePlayerGetExperience();
    }

    @JSStaticFunction
    public static double PlayerGetHunger(Object j) {
        return ScriptManagerD.nativePlayerGetHunger((long) j);
    }

    @JSStaticFunction
    public static String PlayerGetItemCustomName(int i) {
        return ScriptManagerD.nativePlayerGetItemCustomName(i);
    }

    @JSStaticFunction
    public static int PlayerGetLevel() {
        return ScriptManagerD.nativePlayerGetLevel();
    }

    @JSStaticFunction
    public static int PlayerGetPointedBlock(int i) {
        return ScriptManagerD.nativePlayerGetPointedBlock(i);
    }

    @JSStaticFunction
    public static long PlayerGetPointedEntity() {
        return ScriptManagerD.nativePlayerGetPointedEntity();
    }

    @JSStaticFunction
    public static double PlayerGetPointedVec(int i) {
        return ScriptManagerD.nativePlayerGetPointedVec(i);
    }

    @JSStaticFunction
    public static double PlayerGetSaturation() {
        return ScriptManagerD.nativePlayerGetSaturation();
    }

    @JSStaticFunction
    public static boolean PlayerIsFlying() {
        return ScriptManagerD.nativePlayerIsFlying();
    }

    @JSStaticFunction
    public static void PlayerSetCanFly(boolean z) {
        ScriptManagerD.nativePlayerSetCanFly(z);
    }

    @JSStaticFunction
    public static void PlayerSetExhaustion(double f) {
        ScriptManagerD.nativePlayerSetExhaustion((float) f);
    }

    @JSStaticFunction
    public static void PlayerSetExperience(double f) {
        ScriptManagerD.nativePlayerSetExperience((float) f);
    }

    @JSStaticFunction
    public static void PlayerSetFlying(boolean z) {
        ScriptManagerD.nativePlayerSetFlying(z);
    }

    @JSStaticFunction
    public static void PlayerSetHunger(Object j, double f) {
        ScriptManagerD.nativePlayerSetHunger((long) j, (float) f);
    }

    @JSStaticFunction
    public static void PlayerSetItemCustomName(int i, String str) {
        ScriptManagerD.nativePlayerSetItemCustomName(i, str);
    }

    @JSStaticFunction
    public static void PlayerSetLevel(int i) {
        ScriptManagerD.nativePlayerSetLevel(i);
    }

    @JSStaticFunction
    public static void PlayerSetSaturation(double f) {
        ScriptManagerD.nativePlayerSetSaturation((float) f);
    }

    @JSStaticFunction
    public static void PrePatch() {
        ScriptManagerD.nativePrePatch();
    }

    @JSStaticFunction
    public static void PreventDefault() {
        ScriptManagerD.nativePreventDefault();
    }

    @JSStaticFunction
    public static void RecipeSetAnyAuxValue(int i, boolean z) {
        ScriptManagerD.nativeRecipeSetAnyAuxValue(i, z);
    }

    @JSStaticFunction
    public static void RemoveEntity(int i) {
        ScriptManagerD.nativeRemoveEntity(i);
    }

    @JSStaticFunction
    public static void RemoveEntityEnt(Object j) {
        ScriptManagerD.nativeRemoveEntity((long) j);
    }

    @JSStaticFunction
    public static void RemoveItemBackground() {
        ScriptManagerD.nativeRemoveItemBackground();
    }

    @JSStaticFunction
    public static void RequestFrameCallback() {
        ScriptManagerD.nativeRequestFrameCallback();
    }

    @JSStaticFunction
    public static void RideAnimal(int i, int i2) {
        ScriptManagerD.nativeRideAnimal(i, i2);
    }

    @JSStaticFunction
    public static void RideAnimalEnt(Object j, Object j2) {
        ScriptManagerD.nativeRideAnimal((long) j, (long) j2);
    }

    @JSStaticFunction
    public static void SaveGameData() {
        ScriptManagerD.nativeSaveGameData();
    }

    @JSStaticFunction
    public static void ScreenChooserSetScreen(int i) {
        ScriptManagerD.nativeScreenChooserSetScreen(i);
    }

    @JSStaticFunction
    public static void SelectLevel(String str) {
        ScriptManagerD.nativeSelectLevel(str);
    }

    @JSStaticFunction
    public static void SelectLevelString(String str, String str2) {
        ScriptManagerD.nativeSelectLevel(str, str2);
    }

    @JSStaticFunction
    public static void SendChat(String str) {
        ScriptManagerD.nativeSendChat(str);
    }

    @JSStaticFunction
    public static void SetAllowEnchantments(int i, int i2, int i3) {
        ScriptManagerD.nativeSetAllowEnchantments(i, i2, i3);
    }

    @JSStaticFunction
    public static void SetAnimalAge(int i, int i2) {
        ScriptManagerD.nativeSetAnimalAge(i, i2);
    }

    @JSStaticFunction
    public static void SetAnimalAgeEnt(Object j, int i) {
        ScriptManagerD.nativeSetAnimalAge((long) j, i);
    }

    @JSStaticFunction
    public static void SetArmorSlot(int i, int i2, int i3) {
        ScriptManagerD.nativeSetArmorSlot(i, i2, i3);
    }

    @JSStaticFunction
    public static void SetArmorSlotEnchants(int i, int i2, int i3, Scriptable iArr, int i4) {
        ScriptManagerD.nativeSetArmorSlotEnchants(i, i2, i3, scriptableToIntArray(iArr), i4);
    }

    @JSStaticFunction
    public static void SetAutoFlee(int i) {
        ScriptManagerD.nativeSetAutoFlee(i);
    }

    @JSStaticFunction
    public static void SetAutoWalk(int i) {
        ScriptManagerD.nativeSetAutoWalk(i);
    }

    @JSStaticFunction
    public static void SetBlockRenderShape(int i, int i2) {
        ScriptManagerD.nativeSetBlockRenderShape(i, i2);
    }

    @JSStaticFunction
    public static void SetCameraEntity(int i) {
        ScriptManagerD.nativeSetCameraEntity(i);
    }

    @JSStaticFunction
    public static void SetCameraEntityEnt(Object j) {
        ScriptManagerD.nativeSetCameraEntity((long) j);
    }

    @JSStaticFunction
    public static void SetCape(Object j, String str) {
        ScriptManagerD.nativeSetCape((long) j, str);
    }

    @JSStaticFunction
    public static void SetCarriedItem(int i, int i2, int i3, int i4) {
        ScriptManagerD.nativeSetCarriedItem(i, i2, i3, i4);
    }

    @JSStaticFunction
    public static void SetCarriedItemEnt(Object j, int i, int i2, int i3) {
        ScriptManagerD.nativeSetCarriedItem((long) j, i, i2, i3);
    }

    @JSStaticFunction
    public static int SetCommandBlockConfig(String str) {
        return ScriptManagerD.nativeSetCommandBlockConfig(str);
    }

    @JSStaticFunction
    public static int SetConfig(Scriptable iArr) {
        return ScriptManagerD.nativeSetConfig(scriptableToIntArray(iArr));
    }

    @JSStaticFunction
    public static void SetEnableDieFall(int i) {
        ScriptManagerD.nativeSetEnableDieFall(i);
    }

    @JSStaticFunction
    public static boolean SetEntityRenderType(Object j, int i) {
        return ScriptManagerD.nativeSetEntityRenderType((long) j, i);
    }

    @JSStaticFunction
    public static void SetExitEnabled(boolean z) {
        ScriptManagerD.nativeSetExitEnabled(z);
    }

    @JSStaticFunction
    public static void SetFastAddItem(boolean z) {
        ScriptManagerD.nativeSetFastAddItem(z);
    }

    @JSStaticFunction
    public static void SetFov(double f, boolean z) {
        ScriptManagerD.nativeSetFov((float) f, z);
    }

    @JSStaticFunction
    public static void SetGameSpeed(double f) {
        ScriptManagerD.nativeSetGameSpeed((float) f);
    }

    @JSStaticFunction
    public static void SetGameType(int i) {
        ScriptManagerD.nativeSetGameType(i);
    }

    @JSStaticFunction
    public static void SetGuiScale(int i) {
        ScriptManagerD.nativeSetGuiScale(i);
    }

    @JSStaticFunction
    public static void SetHandEquipped(int i, boolean z) {
        ScriptManagerD.nativeSetHandEquipped(i, z);
    }

    @JSStaticFunction
    public static void SetI18NString(String str, String str2) {
        ScriptManagerD.nativeSetI18NString(str, str2);
    }

    @JSStaticFunction
    public static void SetInventoryCustomName(int i, String str) {
        ScriptManagerD.nativeSetInventoryCustomName(i, str);
    }

    @JSStaticFunction
    public static void SetInventorySlot(int i, int i2, int i3, int i4) {
        ScriptManagerD.nativeSetInventorySlot(i, i2, i3, i4);
    }

    @JSStaticFunction
    public static void SetInvinciable(boolean z) {
        ScriptManagerD.nativeSetInvinciable(z);
    }

    @JSStaticFunction
    public static void SetIsRecording(boolean z) {
        ScriptManagerD.nativeSetIsRecording(z);
    }

    @JSStaticFunction
    public static void SetItemCategory(int i, int i2, int i3) {
        ScriptManagerD.nativeSetItemCategory(i, i2, i3);
    }

    @JSStaticFunction
    public static void SetItemMaxDamage(int i, int i2) {
        ScriptManagerD.nativeSetItemMaxDamage(i, i2);
    }

    @JSStaticFunction
    public static void SetKillaura(int i) {
        ScriptManagerD.nativeSetKillaura(i);
    }

    @JSStaticFunction
    public static void SetMCVersion(int i, int i2, int i3, int i4) {
        ScriptManagerD.nativeSetMCVersion(i, i2, i3, i4);
    }

    @JSStaticFunction
    public static void SetMaxConnection(int i) {
        ScriptManagerD.nativeSetMaxConnection(i);
    }

    @JSStaticFunction
    public static void SetMobHealth(int i, int i2) {
        ScriptManagerD.nativeSetMobHealth(i, i2);
    }

    @JSStaticFunction
    public static void SetMobHealthEnt(Object j, int i) {
        ScriptManagerD.nativeSetMobHealth((long) j, i);
    }

    @JSStaticFunction
    public static void SetMobMaxHealth(Object j, int i) {
        ScriptManagerD.nativeSetMobMaxHealth((long) j, i);
    }

    @JSStaticFunction
    public static void SetMobSkin(int i, String str) {
        ScriptManagerD.nativeSetMobSkin(i, str);
    }

    @JSStaticFunction
    public static void SetMobSkinEnt(Object j, String str) {
        ScriptManagerD.nativeSetMobSkin((long) j, str);
    }

    @JSStaticFunction
    public static void SetNightMode(boolean z) {
        ScriptManagerD.nativeSetNightMode(z);
    }

    @JSStaticFunction
    public static int SetNoteBlock(int i, int i2, int i3, int i4) {
        return ScriptManagerD.nativeSetNoteBlock(i, i2, i3, i4);
    }

    @JSStaticFunction
    public static void SetOnFire(int i, int i2) {
        ScriptManagerD.nativeSetOnFire(i, i2);
    }

    @JSStaticFunction
    public static void SetOnFireEnt(Object j, int i) {
        ScriptManagerD.nativeSetOnFire((long) j, i);
    }

    @JSStaticFunction
    public static void SetOversea(boolean z) {
        ScriptManagerD.nativeSetOversea(z);
    }

    @JSStaticFunction
    public static void SetPlayerSkin(Object j, String str) {
        ScriptManagerD.nativeSetPlayerSkin((long) j, str);
    }

    @JSStaticFunction
    public static void SetPosition(int i, double f, double f2, double f3) {
        ScriptManagerD.nativeSetPosition(i, (float) f, (float) f2, (float) f3);
    }

    @JSStaticFunction
    public static void SetPositionEnt(Object j, double f, double f2, double f3) {
        ScriptManagerD.nativeSetPosition((long) j, (float) f, (float) f2, (float) f3);
    }

    @JSStaticFunction
    public static void SetPositionRelative(int i, double f, double f2, double f3) {
        ScriptManagerD.nativeSetPositionRelative(i, (float) f, (float) f2, (float) f3);
    }

    @JSStaticFunction
    public static void SetPositionRelativeEnt(Object j, double f, double f2, double f3) {
        ScriptManagerD.nativeSetPositionRelative((long) j, (float) f, (float) f2, (float) f3);
    }

    @JSStaticFunction
    public static void SetReSpawnPos(double f, double f2, double f3) {
        ScriptManagerD.nativeSetReSpawnPos((float) f, (float) f2, (float) f3);
    }

    @JSStaticFunction
    public static void SetRot(int i, double f, double f2) {
        ScriptManagerD.nativeSetRot(i, (float) f, (float) f2);
    }

    @JSStaticFunction
    public static void SetRotEnt(Object j, double f, double f2) {
        ScriptManagerD.nativeSetRot((long) j, (float) f, (float) f2);
    }

    @JSStaticFunction
    public static void SetSelectedSlotId(int i) {
        ScriptManagerD.nativeSetSelectedSlotId(i);
    }

    @JSStaticFunction
    public static void SetSignText(int i, int i2, int i3, int i4, String str) {
        ScriptManagerD.nativeSetSignText(i, i2, i3, i4, str);
    }

    @JSStaticFunction
    public static void SetSkin(String str, String str2, String str3, Scriptable strArr) {
        ScriptManagerD.nativeSetSkin(str, str2, str3, scriptableToStringArray(strArr));
    }

    @JSStaticFunction
    public static void SetSneaking(int i, boolean z) {
        ScriptManagerD.nativeSetSneaking(i, z);
    }

    @JSStaticFunction
    public static void SetSneakingEnt(Object j, boolean z) {
        ScriptManagerD.nativeSetSneaking((long) j, z);
    }

    @JSStaticFunction
    public static void SetSpawn(int i, int i2, int i3) {
        ScriptManagerD.nativeSetSpawn(i, i2, i3);
    }

    @JSStaticFunction
    public static void SetStonecutterItem(int i, int i2) {
        ScriptManagerD.nativeSetStonecutterItem(i, i2);
    }

    @JSStaticFunction
    public static void SetTextParseColorCodes(boolean z) {
        ScriptManagerD.nativeSetTextParseColorCodes(z);
    }

    @JSStaticFunction
    public static void SetTextureOverrideDir(String str) {
        ScriptManagerD.nativeSetTextureOverrideDir(str);
    }

    @JSStaticFunction
    public static void SetTexturePackFileName(Scriptable strArr) {
        ScriptManagerD.nativeSetTexturePackFileName(scriptableToStringArray(strArr));
    }

    @JSStaticFunction
    public static void SetThroughWall(boolean z) {
        ScriptManagerD.nativeSetThroughWall(z);
    }

    @JSStaticFunction
    public static void SetTile(int i, int i2, int i3, int i4, int i5) {
        ScriptManagerD.nativeSetTile(i, i2, i3, i4, i5);
    }

    @JSStaticFunction
    public static void SetTime(int i) {
        ScriptManagerD.nativeSetTime(i);
    }

    @JSStaticFunction
    public static void SetTimeEnt(Object j) {
        ScriptManagerD.nativeSetTime((long) j);
    }

    @JSStaticFunction
    public static void SetTimeStop(int i) {
        ScriptManagerD.nativeSetTimeStop(i);
    }

    @JSStaticFunction
    public static void SetUseController(boolean z) {
        ScriptManagerD.nativeSetUseController(z);
    }

    @JSStaticFunction
    public static void SetVel(int i, double f, int i2) {
        ScriptManagerD.nativeSetVel(i, (float) f, i2);
    }

    @JSStaticFunction
    public static void SetVelEnt(Object j, double f, int i) {
        ScriptManagerD.nativeSetVel((long) j, (float) f, i);
    }

    @JSStaticFunction
    public static void SetWeather(int i, double f) {
        ScriptManagerD.nativeSetWeather(i, (float) f);
    }

    @JSStaticFunction
    public static void SetWin10GUI(boolean z) {
        ScriptManagerD.nativeSetWin10GUI(z);
    }

    @JSStaticFunction
    public static void SetmapDamaged() {
        ScriptManagerD.nativeSetmapDamaged();
    }

    @JSStaticFunction
    public static void SetupHooks(int i) {
        ScriptManagerD.nativeSetupHooks(i);
    }

    @JSStaticFunction
    public static void ShowProgressScreen() {
        ScriptManagerD.nativeShowProgressScreen();
    }

    @JSStaticFunction
    public static void ShowTipMessage(String str) {
        ScriptManagerD.nativeShowTipMessage(str);
    }

    @JSStaticFunction
    public static void SkinUnLock() {
        ScriptManagerD.nativeSkinUnLock();
    }

    @JSStaticFunction
    public static long SpawnEntity(double f, double f2, double f3, int i, String str) {
        return ScriptManagerD.nativeSpawnEntity((float) f, (float) f2, (float) f3, i, str);
    }

    @JSStaticFunction
    public static long SpawnEntityWithProperties(double f, double f2, double f3, int i, String str, String str2, int i2, Scriptable iArr, Scriptable iArr2, int i3, int i4, int i5) {
        return ScriptManagerD.nativeSpawnEntityWithProperties((float) f, (float) f2, (float) f3, i, str, str2, i2, scriptableToIntArray(iArr), scriptableToIntArray(iArr2), i3, i4, i5);
    }

    @JSStaticFunction
    public static long SpawnEntityWithPropertiesArr(Scriptable objArr, Scriptable iArr, Scriptable objArr2, Scriptable iArr2, Scriptable iArr3) {
        return ScriptManagerD.nativeSpawnEntityWithProperties(scriptableToObjectArray(objArr), scriptableToIntArray(iArr), scriptableToObjectArray(objArr2), scriptableToIntArray(iArr2), scriptableToIntArray(iArr3));
    }

    @JSStaticFunction
    public static long SpawnPoweredCreeper(double f, double f2, double f3, int i, String str) {
        return ScriptManagerD.nativeSpawnPoweredCreeper((float) f, (float) f2, (float) f3, i, str);
    }

    @JSStaticFunction
    public static int SpawnerGetEntityType(int i, int i2, int i3) {
        return ScriptManagerD.nativeSpawnerGetEntityType(i, i2, i3);
    }

    @JSStaticFunction
    public static void SpawnerSetEntityType(int i, int i2, int i3, int i4) {
        ScriptManagerD.nativeSpawnerSetEntityType(i, i2, i3, i4);
    }

    @JSStaticFunction
    public static boolean ZombieIsBaby(Object j) {
        return ScriptManagerD.nativeZombieIsBaby((long) j);
    }

    @JSStaticFunction
    public static void ZombieSetBaby(Object j, boolean z) {
        ScriptManagerD.nativeZombieSetBaby((long) j, z);
    }

    @JSStaticFunction
    public static void test(double f, double f2, double f3, int i, String str) {
        ScriptManagerD.test((float) f, (float) f2, (float) f3, i, str);
    }

    private static Object[] scriptableToObjectArray(Scriptable scriptable) {
        int length = ((Number) ScriptableObject.getProperty(scriptable, "length")).intValue();
        Object[] array = new Object[length];
        for (int j = 0; j < length; j++) {
            array[j] = ScriptableObject.getProperty(scriptable, j);
        }
        return array;
    }

    private static String[] scriptableToStringArray(Scriptable scriptable) {
        int length = ((Number) ScriptableObject.getProperty(scriptable, "length")).intValue();
        String[] array = new String[length];
        for (int j = 0; j < length; j++) {
            array[j] = ScriptableObject.getProperty(scriptable, j).toString();
        }
        return array;
    }

    private static int[] scriptableToIntArray(Scriptable scriptable) {
        int length = ((Number) ScriptableObject.getProperty(scriptable, "length")).intValue();
        int[] array = new int[length];
        for (int j = 0; j < length; j++) {
            array[j] = (int) ScriptableObject.getProperty(scriptable, j);
        }
        return array;
    }

    private static float[] scriptableToFloatArray(Scriptable scriptable) {
        int length = ((Number) ScriptableObject.getProperty(scriptable, "length")).intValue();
        float[] array = new float[length];
        for (int j = 0; j < length; j++) {
            array[j] = (float) ScriptableObject.getProperty(scriptable, j);
        }
        return array;
    }

    private static long[] scriptableToLongArray(Scriptable scriptable) {
        int length = ((Number) ScriptableObject.getProperty(scriptable, "length")).intValue();
        long[] array = new long[length];
        for (int j = 0; j < length; j++) {
            array[j] = (long) ScriptableObject.getProperty(scriptable, j);
        }
        return array;
    }

    private static byte[] scriptableToByteArray(Scriptable scriptable) {
        int length = ((Number) ScriptableObject.getProperty(scriptable, "length")).intValue();
        byte[] array = new byte[length];
        for (int j = 0; j < length; j++) {
            array[j] = (byte) ScriptableObject.getProperty(scriptable, j);
        }
        return array;
    }


    @Override
    public String getClassName() {
        return "Helper";
    }
}
