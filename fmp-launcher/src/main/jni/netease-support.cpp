//
// Created by Gao在此 on 2019/6/16.
//
#include <jni.h>
#include <unistd.h>
#include <stdio.h>
#include <dirent.h>
#include <dlfcn.h>
#include <string.h>
#include <sys/stat.h>
#include <stdlib.h>
#include <string>
#include "substrate/Substrate.h"
#include "android/android_log.h"
#include "JavaFileUtil.h"
#include "Helper.h"
#include "fmp-launcher.h"
#include "netease-support.h"
#include "md5/md5file.h"

using namespace std;

static int registerNativeMethods(JNIEnv *env);

static int registerNativeMethods(JNIEnv *env, const char *className, JNINativeMethod *gMethods,
                                 int numMethods);

const char *jstringToCharArr(JNIEnv *env, jstring str);

jstring charArrToJstring(JNIEnv *env, const char *arr);

void loadScript(JNIEnv *env, jobject file);

void loadModPkg(JNIEnv *env, jobject file);

bool checkHelper(JNIEnv *env, jstring apkPath);

void initMsHook(JavaVM *vm);

static jint TYPE_ENTER_HOME = 5;
static jint TYPE_ENTER_GAME = 6;
static jint TYPE_LEAVE_GAME = 7;
static jint TYPE_LOAD_ALL_MOD = 8;
static jint TYPE_LOAD_ALL_END = 9;
static jint TYPE_LOAD_JS_TIP = 10;
static jint TYPE_LOAD_JS_ERR = 11;
static jint TYPE_LOAD_MODPKG_TIP = 20;
static jint TYPE_LOAD_MODPKG_ERR = 21;
static jint TYPE_LOAD_TEXTURES = 25;

jclass class_gameManager;

Helper helperClient;

jobject getApplicationContext(JNIEnv *env) {
    //获取Activity Thread的实例对象
    jclass activityThread = env->FindClass("android/app/ActivityThread");
    jmethodID currentActivityThread = env->GetStaticMethodID(activityThread,
                                                             "currentActivityThread",
                                                             "()Landroid/app/ActivityThread;");
    jobject at = env->CallStaticObjectMethod(activityThread, currentActivityThread);
    //获取Application，也就是全局的Context
    jmethodID getApplication = env->GetMethodID(activityThread, "getApplication",
                                                "()Landroid/app/Application;");
    jobject context = env->CallObjectMethod(at, getApplication);
    return context;
}

bool stringcmp(const char *str1, const char *str2) {
    return strcmp(str1, str2) == 0;
}

//输出Toast信息
void showToast(JNIEnv *env, jstring toast) {
    jclass jc_Toast = env->FindClass("android/widget/Toast");
    jmethodID jm_makeText = env->GetStaticMethodID(jc_Toast, "makeText",
                                                   "(Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;");
    jobject jo_Toast = env->CallStaticObjectMethod(jc_Toast, jm_makeText,
                                                   getApplicationContext(env), toast, 0);
    jmethodID jm_Show = env->GetMethodID(jc_Toast, "show", "()V");
    env->CallVoidMethod(jo_Toast, jm_Show);
}

const char *jstringToCharArr(JNIEnv *env, jstring str) {
    return env->GetStringUTFChars(str, 0);
}

jstring charArrToJstring(JNIEnv *env, const char *arr) {
    return env->NewStringUTF(arr);
}

//发送广播
void sendBroadcast(JNIEnv *env, jint type, jstring value) {
    jobject Context = getApplicationContext(env);

    jclass jc_VERSION = env->FindClass("android/os/Build$VERSION");
    jfieldID jf_SDK_INT = env->GetStaticFieldID(jc_VERSION, "SDK_INT", "I");
    jint SDK_INT = env->GetStaticIntField(jc_VERSION, jf_SDK_INT);

    jclass jc_Context = env->FindClass("android/content/Context");
    jmethodID jm_sendBroadcast = env->GetMethodID(jc_Context, "sendBroadcast",
                                                  "(Landroid/content/Intent;)V");
    jclass jc_Intent = env->FindClass("android/content/Intent");
    jmethodID jm_Intent_init = env->GetMethodID(jc_Intent, "<init>", "(Ljava/lang/String;)V");

    jmethodID jm_Intent_setComponent = NULL;
    if (SDK_INT > 22) {
        jm_Intent_setComponent = env->GetMethodID(jc_Intent, "setComponent",
                                                  "(Landroid/content/ComponentName;)Landroid/content/Intent;");
        if (env->ExceptionCheck()) {
            env->ExceptionDescribe();
            env->ExceptionClear();
        }
    }

    jmethodID jm_Intent_putExtraString = env->GetMethodID(jc_Intent, "putExtra",
                                                          "(Ljava/lang/String;Ljava/lang/String;)Landroid/content/Intent;");
    jmethodID jm_Intent_putExtraInteger = env->GetMethodID(jc_Intent, "putExtra",
                                                           "(Ljava/lang/String;I)Landroid/content/Intent;");

    jobject Intent = env->NewObject(jc_Intent, jm_Intent_init, env->NewStringUTF("HelperClient"));

    if (SDK_INT > 22) {
        jclass jc_ComponentName = env->FindClass("android/content/ComponentName");
        jmethodID jm_ComponentName_Init = env->GetMethodID(jc_ComponentName, "<init>",
                                                           "(Ljava/lang/String;Ljava/lang/String;)V");
        if (env->ExceptionCheck()) {
            env->ExceptionDescribe();
            env->ExceptionClear();
        } else {
            jobject ComponentName = env->NewObject(jc_ComponentName, jm_ComponentName_Init,
                                                   env->NewStringUTF("net.fmp.helper"),
                                                   env->NewStringUTF(
                                                           "com.fmp.receiver.LoaderReceiver"));
            env->CallObjectMethod(Intent, jm_Intent_setComponent, ComponentName);
            if (env->ExceptionCheck()) {
                env->ExceptionDescribe();
                env->ExceptionClear();
            }
        }

    }
    env->DeleteLocalRef(jc_VERSION);


    env->CallObjectMethod(Intent, jm_Intent_putExtraInteger, env->NewStringUTF("TYPE"), type);
    if (value != NULL) {
        env->CallObjectMethod(Intent, jm_Intent_putExtraString, env->NewStringUTF("VALUE"), value);
    }
    env->CallVoidMethod(Context, jm_sendBroadcast, Intent);

    env->DeleteLocalRef(Context);
    env->DeleteLocalRef(jc_Context);
    env->DeleteLocalRef(jc_Intent);
    env->DeleteLocalRef(Intent);
}

JNIEXPORT void JNICALL jniNullVoid(JNIEnv *env, jclass type) {
}

JNIEXPORT void JNICALL jniNullString(JNIEnv *env, jclass type, jstring str) {
}

JNIEXPORT void JNICALL jniNullInteger(JNIEnv *env, jclass type, jint i, jboolean b) {
}

bool checkXuxiqwq(JNIEnv *env) {
    jclass jc_hook = env->FindClass("com/xuxiqwq/x19/hook");
    if (env->ExceptionCheck()) {
        env->ExceptionClear();
        return false;
    }
    if (jc_hook != NULL) {
        LOGD("check xuxiqwq hook!");
        JNINativeMethod hookMethods[] = {{"startHook", "()V",                   (void *) jniNullVoid},
                                         {"sngh",      "()V",                   (void *) jniNullVoid},
                                         {"setFille",  "(Ljava/lang/String;)V", (void *) jniNullString},
                                         {"setAb",     "(IZ)V",                 (void *) jniNullInteger},
                                         {"WriteLog",  "(Ljava/lang/String;)V", (void *) jniNullString}};
        return registerNativeMethods(env, "com/xuxiqwq/x19/hook", hookMethods,
                                     sizeof(hookMethods) / sizeof(hookMethods[0])) != 0;
    }
    return false;
}

//获取MC Activity
jobject getMainActivity(JNIEnv *env) {
    jclass jc_MainActivity = env->FindClass("com/mojang/minecraftpe/MainActivity");
    jmethodID jm_MainActivity_getInstance = env->GetStaticMethodID(jc_MainActivity, "getInstance",
                                                                   "()Lcom/mojang/minecraftpe/MainActivity;");
    jobject MainActivity = env->CallStaticObjectMethod(jc_MainActivity,
                                                       jm_MainActivity_getInstance);
    env->DeleteLocalRef(jc_MainActivity);
    return MainActivity;
}

//移除多玩启动痕迹
void clearDuowanIntentData(JNIEnv *env) {
    jclass jc_MainActivity = env->FindClass("com/mojang/minecraftpe/MainActivity");
    jmethodID jm_clearDuowanIntentData = env->GetMethodID(jc_MainActivity, "clearDuowanIntentData",
                                                          "()V");
    env->CallVoidMethod(getMainActivity(env), jm_clearDuowanIntentData);
    env->DeleteLocalRef(jc_MainActivity);
}

//设置屏幕方向
void setRequestedOrientation(JNIEnv *env, jint i) {
    jclass jc_MainActivity = env->FindClass("com/mojang/minecraftpe/MainActivity");
    jmethodID jm_setRequestedOrientation = env->GetMethodID(jc_MainActivity,
                                                            "setRequestedOrientation", "(I)V");
    env->CallVoidMethod(getMainActivity(env), jm_setRequestedOrientation, i);
    env->DeleteLocalRef(jc_MainActivity);
}

//强制退出
void setLeaveServer(JNIEnv *env) {
    jclass jc_MainActivity = env->FindClass("com/mojang/minecraftpe/MainActivity");
    jmethodID jm_setLeaveServer = env->GetMethodID(jc_MainActivity, "setLeaveServer", "()V");
    env->CallVoidMethod(getMainActivity(env), jm_setLeaveServer);
    env->DeleteLocalRef(jc_MainActivity);
}

//退出登录
void logout(JNIEnv *env) {
    jclass jc_MainActivity = env->FindClass("com/mojang/minecraftpe/MainActivity");
    jmethodID jm_logout = env->GetMethodID(jc_MainActivity, "logout", "()V");
    env->CallVoidMethod(getMainActivity(env), jm_logout);
    env->DeleteLocalRef(jc_MainActivity);
}

//停止网易推送服务
void stopService(JNIEnv *env) {
    jclass jc_PushManagerImpl = env->FindClass("com/netease/pushclient/PushManagerImpl");
    jmethodID jm_stopService = env->GetStaticMethodID(jc_PushManagerImpl, "stopService", "()V");
    env->CallStaticVoidMethod(jc_PushManagerImpl, jm_stopService);

    env->DeleteLocalRef(jc_PushManagerImpl);
}

//加载modpkg
void loadModPkgScripts(JNIEnv *env) {
    jclass jc_ScriptManager = env->FindClass("com/mcbox/pesdkb/mcpelauncher/ScriptManager");
    jmethodID jm_loadModPkgScripts = env->GetStaticMethodID(jc_ScriptManager, "loadModPkgScripts",
                                                            "()V");
    env->CallStaticVoidMethod(jc_ScriptManager, jm_loadModPkgScripts);
    env->DeleteLocalRef(jc_ScriptManager);
}

//初始化调用
void frameCallback(JNIEnv *env) {
    jclass jc_ScriptManager = env->FindClass("com/mcbox/pesdkb/mcpelauncher/ScriptManager");
    jmethodID jm_frameCallback = env->GetStaticMethodID(jc_ScriptManager, "frameCallback", "()V");
    env->CallStaticVoidMethod(jc_ScriptManager, jm_frameCallback);
    env->DeleteLocalRef(jc_ScriptManager);
}

//初始化药水效果
void MobEffect_initIds(JNIEnv *env) {
    jclass jc_MobEffect = env->FindClass("com/mcbox/pesdkb/mcpelauncher/api/modpe/MobEffect");
    jmethodID jm_initIds = env->GetStaticMethodID(jc_MobEffect, "initIds", "()V");
    env->CallStaticVoidMethod(jc_MobEffect, jm_initIds);

    env->DeleteLocalRef(jc_MobEffect);
}

//获取是否多人模式
jboolean isRemote(JNIEnv *env) {
    jclass jc_ScriptManager = env->FindClass("com/mcbox/pesdkb/mcpelauncher/ScriptManager");
    jmethodID jm_LevelIsRemote = env->GetStaticMethodID(jc_ScriptManager, "nativeLevelIsRemote",
                                                        "()Z");
    return env->CallStaticBooleanMethod(jc_ScriptManager, jm_LevelIsRemote);
}

//强制设置多人游戏
void setRemote(JNIEnv *env, jboolean isRemote) {
    jclass jc_ScriptManager = env->FindClass("com/mcbox/pesdkb/mcpelauncher/ScriptManager");
    jfieldID jf_scriptingEnabled = env->GetStaticFieldID(jc_ScriptManager, "isRemote", "Z");
    env->SetStaticBooleanField(jc_ScriptManager, jf_scriptingEnabled, isRemote);

    env->DeleteLocalRef(jc_ScriptManager);
}

//设置局域网游戏
void setLanGame(JNIEnv *env, jboolean isLanGame) {
    jclass jc_ScriptManager = env->FindClass("com/mcbox/pesdkb/mcpelauncher/ScriptManager");
    jfieldID jf_scriptingEnabled = env->GetStaticFieldID(jc_ScriptManager, "isLanGame", "Z");
    env->SetStaticBooleanField(jc_ScriptManager, jf_scriptingEnabled, isLanGame);
    env->DeleteLocalRef(jc_ScriptManager);
}

//设置存档
void setHasLevel(JNIEnv *env) {
    jclass jc_ScriptManager = env->FindClass("com/mcbox/pesdkb/mcpelauncher/ScriptManager");
    jfieldID jf_scriptingEnabled = env->GetStaticFieldID(jc_ScriptManager, "hasLevel", "Z");
    env->SetStaticBooleanField(jc_ScriptManager, jf_scriptingEnabled, JNI_TRUE);

    env->DeleteLocalRef(jc_ScriptManager);
}

//设置输出名称
void setCurrentScript(JNIEnv *env) {
    jclass jc_ScriptManager = env->FindClass("com/mcbox/pesdkb/mcpelauncher/ScriptManager");
    jfieldID jf_scriptingEnabled = env->GetStaticFieldID(jc_ScriptManager, "currentScript",
                                                         "Ljava/lang/String;");
    env->SetStaticObjectField(jc_ScriptManager, jf_scriptingEnabled,
                              env->NewStringUTF("FMP-Launcher"));
    env->DeleteLocalRef(jc_ScriptManager);
}

//强制设置多人游戏mod加载
void setScriptingEnabled(JNIEnv *env, jboolean isEnabled) {
    jclass jc_ScriptManager = env->FindClass("com/mcbox/pesdkb/mcpelauncher/ScriptManager");
    jfieldID jf_scriptingEnabled = env->GetStaticFieldID(jc_ScriptManager, "scriptingEnabled", "Z");
    env->SetStaticBooleanField(jc_ScriptManager, jf_scriptingEnabled, isEnabled);

    env->DeleteLocalRef(jc_ScriptManager);
}

//显示游戏辅助悬浮窗
void showGameFloat(JNIEnv *env) {
    jclass jc_GameFloatWindow = env->FindClass("com/fmp/core/GameFloatWindow");
    jmethodID jm_getInstance = env->GetStaticMethodID(jc_GameFloatWindow, "getInstance",
                                                      "()Lcom/fmp/core/GameFloatWindow;");
    jmethodID jm_init = env->GetMethodID(jc_GameFloatWindow, "init", "(Landroid/app/Activity;)V");
    jobject GameManager = env->CallStaticObjectMethod(jc_GameFloatWindow, jm_getInstance);
    env->CallVoidMethod(GameManager, jm_init, getMainActivity(env));
    env->DeleteLocalRef(jc_GameFloatWindow);
}

//保存玩家名称
void savePlayerName(JNIEnv *env) {
    jclass jc_File = env->FindClass("java/io/File");
    jmethodID jm_File_Init = env->GetMethodID(jc_File, "<init>",
                                              "(Ljava/io/File;Ljava/lang/String;)V");
    jmethodID jm_getAbsolutePath = env->GetMethodID(jc_File, "getAbsolutePath",
                                                    "()Ljava/lang/String;");
    jmethodID jm_exists = env->GetMethodID(jc_File, "exists", "()Z");

    jclass jc_Context = env->GetObjectClass(getApplicationContext(env));
    jmethodID jm_getFilesDir = env->GetMethodID(jc_Context, "getFilesDir", "()Ljava/io/File;");

    jobject dataFile = env->NewObject(jc_File, jm_File_Init,
                                      env->CallObjectMethod(getApplicationContext(env),
                                                            jm_getFilesDir), env->NewStringUTF(
                    "games/com.netease/minecraftpe/userinfo.data"));
    jboolean isExists = env->CallBooleanMethod(dataFile, jm_exists);

    if (isExists) {
        FILE *fp = fopen(jstringToCharArr(env, (jstring) env->CallObjectMethod(dataFile,
                                                                               jm_getAbsolutePath)),
                         "r");
        char buff[255];
        fgets(buff, 255, fp);
        fclose(fp);
        //发送广播
        sendBroadcast(env, 30, charArrToJstring(env, buff));

        jobject helperFile = env->NewObject(jc_File, jm_File_Init, getHelperDirectory(env),
                                            env->NewStringUTF(".PlayerName"));

        FILE *fp2 = fopen(jstringToCharArr(env, (jstring) env->CallObjectMethod(helperFile,
                                                                                jm_getAbsolutePath)),
                          "w");
        fprintf(fp2, "%s", buff);
        fclose(fp2);
        if (env->ExceptionCheck()) {
            env->ExceptionDescribe();
            env->ExceptionClear();
        }
        env->DeleteLocalRef(helperFile);
    }
    env->DeleteLocalRef(dataFile);

}

//加载JavaScript
void loadScript(JNIEnv *env, jobject file) {
    jclass jc_FileReader = env->FindClass("java/io/FileReader");
    jmethodID jm_FileReader_init = env->GetMethodID(jc_FileReader, "<init>", "(Ljava/io/File;)V");
    jmethodID jm_close = env->GetMethodID(jc_FileReader, "close", "()V");

    jclass jc_GameManager = env->FindClass("com/fmp/core/GameManager");
    jmethodID jm_getInstance = env->GetStaticMethodID(jc_GameManager, "getInstance",
                                                      "()Lcom/fmp/core/GameManager;");
    jmethodID jm_printException = env->GetStaticMethodID(jc_GameManager, "printException",
                                                         "(Ljava/lang/Throwable;)V");
    jobject GameManager = env->CallStaticObjectMethod(jc_GameManager, jm_getInstance);
    jmethodID jm_loadScript = env->GetMethodID(jc_GameManager, "loadScript",
                                               "(Ljava/io/Reader;Ljava/lang/String;)V");

    jclass jc_File = env->GetObjectClass(file);
    jmethodID jm_getName = env->GetMethodID(jc_File, "getName", "()Ljava/lang/String;");
    jstring name = (jstring) env->CallObjectMethod(file, jm_getName);

    jobject FileReader = env->NewObject(jc_FileReader, jm_FileReader_init, file);
    env->CallVoidMethod(GameManager, jm_loadScript, FileReader, name);
    if (env->ExceptionCheck()) {
        env->CallStaticVoidMethod(jc_GameManager, jm_printException, env->ExceptionOccurred());
        env->ExceptionClear();
        sendBroadcast(env, TYPE_LOAD_JS_ERR, name);
    }
    env->CallVoidMethod(FileReader, jm_close);
    if (env->ExceptionCheck()) {
        env->ExceptionClear();
    }
    env->DeleteLocalRef(jc_FileReader);
    env->DeleteLocalRef(jc_GameManager);
    env->DeleteLocalRef(jc_File);
}

//加载Modpkg
void loadModPkg(JNIEnv *env, jobject file) {
    jclass jc_ScriptManager = env->FindClass("com/mcbox/pesdkb/mcpelauncher/ScriptManager");
    jfieldID jf_modPkgTexturePack = env->GetStaticFieldID(jc_ScriptManager, "modPkgTexturePack",
                                                          "Lcom/mcbox/pesdkb/mcpelauncher/texture/ModPkgTexturePack;");
    jobject modPkgTexturePack = env->GetStaticObjectField(jc_ScriptManager, jf_modPkgTexturePack);

    jclass jc_ModPkgTexturePack = env->FindClass(
            "com/mcbox/pesdkb/mcpelauncher/texture/ModPkgTexturePack");
    jmethodID jm_addPackage = env->GetMethodID(jc_ModPkgTexturePack, "addPackage",
                                               "(Ljava/io/File;)V");
    env->CallVoidMethod(modPkgTexturePack, jm_addPackage, file);

    env->DeleteLocalRef(jc_ScriptManager);
    env->DeleteLocalRef(jc_ModPkgTexturePack);
}

//加载fmod
void loadMod(JNIEnv *env, jobject file, jint key) {
    jclass jc_GameManager = env->FindClass("com/fmp/core/GameManager");
    jmethodID jm_getInstance = env->GetStaticMethodID(jc_GameManager, "getInstance",
                                                      "()Lcom/fmp/core/GameManager;");
    jobject GameManager = env->CallStaticObjectMethod(jc_GameManager, jm_getInstance);
    jmethodID jm_loadMod = env->GetMethodID(jc_GameManager, "loadMod", "(Ljava/io/File;I)Z");
    jboolean load = env->CallBooleanMethod(GameManager, jm_loadMod, file, key);

    if (!load) {
        jclass jc_File = env->FindClass("java/io/File");
        jmethodID jm_getName = env->GetMethodID(jc_File, "getName", "()Ljava/lang/String;");
        jstring modName = (jstring) env->CallObjectMethod(file, jm_getName);
        sendBroadcast(env, TYPE_LOAD_JS_ERR, modName);
    }
    env->DeleteLocalRef(jc_GameManager);
}

//加载资源包
void loadTexture(JNIEnv *env, jstring texturePath) {
    jclass jc_GameManager = env->FindClass("com/fmp/core/GameManager");
    jmethodID jm_getInstance = env->GetStaticMethodID(jc_GameManager, "getInstance",
                                                      "()Lcom/fmp/core/GameManager;");
    jmethodID jm_loadInit = env->GetMethodID(jc_GameManager, "loadTexture",
                                             "(Ljava/lang/String;)V");
    jobject GameManager = env->CallStaticObjectMethod(jc_GameManager, jm_getInstance);

    env->CallVoidMethod(GameManager, jm_loadInit, texturePath);

    env->DeleteLocalRef(jc_GameManager);
}

//初始化插件信息
void initModsData(JNIEnv *env, jstring modsData) {
    jclass jc_GameManager = env->FindClass("com/fmp/core/GameManager");
    jmethodID jm_getInstance = env->GetStaticMethodID(jc_GameManager, "getInstance",
                                                      "()Lcom/fmp/core/GameManager;");
    jmethodID jm_loadInit = env->GetMethodID(jc_GameManager, "initModsData",
                                             "(Ljava/lang/String;)V");
    jobject GameManager = env->CallStaticObjectMethod(jc_GameManager, jm_getInstance);

    env->CallVoidMethod(GameManager, jm_loadInit, modsData);
    env->DeleteLocalRef(jc_GameManager);
}

//删除游戏错误文件
void deleteGameErrorFile(JNIEnv *env) {
    jclass jc_File = env->FindClass("java/io/File");
    jmethodID jm_File_Init = env->GetMethodID(jc_File, "<init>",
                                              "(Ljava/io/File;Ljava/lang/String;)V");
    jmethodID jm_delete = env->GetMethodID(jc_File, "delete", "()Z");
    jobject file = env->NewObject(jc_File, jm_File_Init, getHelperDirectory(env),
                                  env->NewStringUTF("Mod/.StartGame"));
    if (env->ExceptionCheck()) {
        env->ExceptionDescribe();
        env->ExceptionClear();
        return;
    }
    env->CallBooleanMethod(file, jm_delete);

    env->DeleteLocalRef(jc_File);
    env->DeleteLocalRef(file);

}

//删除加载错误文件
void deleteModErrorFile(JNIEnv *env) {
    jclass jc_File = env->FindClass("java/io/File");
    jmethodID jm_File_Init = env->GetMethodID(jc_File, "<init>",
                                              "(Ljava/io/File;Ljava/lang/String;)V");
    jmethodID jm_delete = env->GetMethodID(jc_File, "delete", "()Z");
    jobject file = env->NewObject(jc_File, jm_File_Init, getHelperDirectory(env),
                                  env->NewStringUTF("Mod/.LoadMod"));
    if (env->ExceptionCheck()) {
        env->ExceptionDescribe();
        env->ExceptionClear();
        return;
    }
    env->CallBooleanMethod(file, jm_delete);

    env->DeleteLocalRef(jc_File);
    env->DeleteLocalRef(file);
}

//删除核心文件
void deleteDatFile(JNIEnv *env) {
    jclass jc_File = env->FindClass("java/io/File");
    jmethodID jm_File_Init = env->GetMethodID(jc_File, "<init>",
                                              "(Ljava/io/File;Ljava/lang/String;)V");
    jmethodID jm_delete = env->GetMethodID(jc_File, "delete", "()Z");


    jobject dat = env->NewObject(jc_File, jm_File_Init, getExternalStorageDirectory(env),
                                 env->NewStringUTF("Android/.js/p.dat"));
    env->CallBooleanMethod(dat, jm_delete);
    if (env->ExceptionCheck()) {
        env->ExceptionClear();
    }
    env->DeleteLocalRef(jc_File);
    env->DeleteLocalRef(dat);
}

//移除缓存数据
void deleteCache(JNIEnv *env) {
    jclass jc_GameManager = env->FindClass("com/fmp/core/GameManager");
    jmethodID jm_getInstance = env->GetStaticMethodID(jc_GameManager, "getInstance",
                                                      "()Lcom/fmp/core/GameManager;");
    jobject GameManager = env->CallStaticObjectMethod(jc_GameManager, jm_getInstance);
    jmethodID jm_deleteCache = env->GetMethodID(jc_GameManager, "deleteCache",
                                                "(Landroid/app/Application;)V");
    env->CallVoidMethod(GameManager, jm_deleteCache, getApplicationContext(env));
    env->DeleteLocalRef(jc_GameManager);
}

//解析加载MOD
void analysisModData(JNIEnv *env, jstring modsData) {
    if (modsData == nullptr) {
        LOGI("mods data is null");
        helperClient.setLoadEnd(true);
        deleteModErrorFile(env);
        return;
    }

    setScriptingEnabled(env, JNI_TRUE);

    initModsData(env, modsData);

    jclass jc_String = env->FindClass("java/lang/String");
    jmethodID jm_split = env->GetMethodID(jc_String, "split",
                                          "(Ljava/lang/String;)[Ljava/lang/String;");
    jmethodID jm_endsWith = env->GetMethodID(jc_String, "endsWith", "(Ljava/lang/String;)Z");
    jmethodID jm_contains = env->GetMethodID(jc_String, "contains", "(Ljava/lang/CharSequence;)Z");
    jmethodID jm_substring = env->GetMethodID(jc_String, "substring", "(II)Ljava/lang/String;");
    jmethodID jm_indexOf = env->GetMethodID(jc_String, "indexOf", "(Ljava/lang/String;)I");
    jmethodID jm_length = env->GetMethodID(jc_String, "length", "()I");
    jmethodID jm_isEmpty = env->GetMethodID(jc_String, "isEmpty", "()Z");
    jclass jc_Integer = env->FindClass("java/lang/Integer");
    jmethodID jm_parseInt = env->GetStaticMethodID(jc_Integer, "parseInt", "(Ljava/lang/String;)I");
    jclass jc_File = env->FindClass("java/io/File");
    jmethodID jm_File_Init = env->GetMethodID(jc_File, "<init>", "(Ljava/lang/String;)V");
    jmethodID jm_getName = env->GetMethodID(jc_File, "getName", "()Ljava/lang/String;");
    jmethodID jm_exists = env->GetMethodID(jc_File, "exists", "()Z");

    jboolean isEmpty = env->CallBooleanMethod(modsData, jm_isEmpty);
    if (isEmpty) {
        helperClient.setLoadEnd(true);
        deleteModErrorFile(env);
        env->DeleteLocalRef(jc_File);
        env->DeleteLocalRef(jc_Integer);
        env->DeleteLocalRef(jc_String);
        return;
    }

    sendBroadcast(env, TYPE_LOAD_ALL_MOD, NULL);

    jobjectArray mods = (jobjectArray) env->CallObjectMethod(modsData, jm_split,
                                                             env->NewStringUTF(","));
    if (env->ExceptionCheck()) {
        env->ExceptionClear();

        sendBroadcast(env, TYPE_LOAD_ALL_END, NULL);
        helperClient.setLoadEnd(true);

        env->DeleteLocalRef(jc_File);
        env->DeleteLocalRef(jc_Integer);
        env->DeleteLocalRef(jc_String);
        env->DeleteLocalRef(mods);
        return;
    }
    if (mods != NULL) {
        //获取所有MOD的个数
        int modsLength = env->GetArrayLength(mods);
        for (int i = 0; i < modsLength; i++) {
            //获取单个MOD的数据
            jstring modPath = (jstring) env->GetObjectArrayElement(mods, i);
            jboolean isJs = env->CallBooleanMethod(modPath, jm_endsWith, env->NewStringUTF(".js"));
            jboolean isModpkg = env->CallBooleanMethod(modPath, jm_endsWith,
                                                       env->NewStringUTF(".modpkg"));

            jboolean isFmod = env->CallBooleanMethod(modPath, jm_endsWith,
                                                     env->NewStringUTF(".fmod"));
            jboolean existKey = env->CallBooleanMethod(modPath, jm_contains,
                                                       env->NewStringUTF("'"));

            jint key = 50;

            if (existKey) {
                //获取key标志的位置
                jint keyFlag = env->CallIntMethod(modPath, jm_indexOf, env->NewStringUTF("'"));
                //获取key字符串
                jstring keyStr = (jstring) env->CallObjectMethod(modPath, jm_substring, 0, keyFlag);
                //解密key
                key = env->CallStaticIntMethod(jc_Integer, jm_parseInt, keyStr);
                //获取字符串长度
                jint length = env->CallIntMethod(modPath, jm_length);
                //获取MOD地址
                modPath = (jstring) env->CallObjectMethod(modPath, jm_substring, keyFlag + 1,
                                                          length);
                LOGD("exist key %d", key);
            }
            if (isJs) {
                jobject modFile = env->NewObject(jc_File, jm_File_Init, modPath);
                jstring modName = (jstring) env->CallObjectMethod(modFile, jm_getName);
                LOGD("load js %s", jstringToCharArr(env, modName));
                sendBroadcast(env, TYPE_LOAD_JS_TIP, modName);
                loadScript(env, modFile);
                if (env->ExceptionCheck()) {
                    env->ExceptionClear();
                    sendBroadcast(env, TYPE_LOAD_JS_ERR, modName);
                }
                sleep(2);
                continue;
            }
            if (isModpkg) {
                jobject modFile = env->NewObject(jc_File, jm_File_Init, modPath);
                jstring modName = (jstring) env->CallObjectMethod(modFile, jm_getName);
                LOGD("load modpkg %s", jstringToCharArr(env, modName));
                sendBroadcast(env, TYPE_LOAD_MODPKG_TIP, modName);
                loadModPkg(env, modFile);
                if (env->ExceptionCheck()) {
                    env->ExceptionClear();
                    sendBroadcast(env, TYPE_LOAD_MODPKG_ERR, modName);
                }
                continue;
            }
            if (isFmod) {
                jobject modFile = env->NewObject(jc_File, jm_File_Init, modPath);
                jstring modName = (jstring) env->CallObjectMethod(modFile, jm_getName);
                LOGD("load fmod %s", jstringToCharArr(env, modName));
                sendBroadcast(env, TYPE_LOAD_JS_TIP, modName);
                loadMod(env, modFile, key);
                if (env->ExceptionCheck()) {
                    env->ExceptionClear();
                    sendBroadcast(env, TYPE_LOAD_JS_ERR, modName);
                }
                sleep(2);
                continue;
            }
        }
        loadModPkgScripts(env);

        if (modsLength != 0)
            sendBroadcast(env, TYPE_LOAD_ALL_END, NULL);
    }
    deleteModErrorFile(env);
    helperClient.setLoadEnd(true);

    env->DeleteLocalRef(jc_File);
    env->DeleteLocalRef(jc_Integer);
    env->DeleteLocalRef(jc_String);
    env->DeleteLocalRef(mods);
}

//解析加载资源
void analysisTexturesData(JNIEnv *env, jstring texturesData) {
    jclass jc_String = env->FindClass("java/lang/String");
    jmethodID jm_isEmpty = env->GetMethodID(jc_String, "isEmpty", "()Z");
    jmethodID jm_split = env->GetMethodID(jc_String, "split",
                                          "(Ljava/lang/String;)[Ljava/lang/String;");

    jboolean isEmpty = env->CallBooleanMethod(texturesData, jm_isEmpty);
    if (!isEmpty) {
        jobjectArray textures = (jobjectArray) env->CallObjectMethod(texturesData, jm_split,
                                                                     env->NewStringUTF(","));
        if (env->ExceptionCheck()) {
            env->ExceptionClear();
            return;
        }
        if (textures != NULL) {
            sendBroadcast(env, TYPE_LOAD_TEXTURES, NULL);
            //获取所有资源的个数
            int texturesLength = env->GetArrayLength(textures);
            for (int i = 0; i < texturesLength; i++) {
                //获取单个资源的数据
                jstring texture = (jstring) env->GetObjectArrayElement(textures, i);
                loadTexture(env, texture);
            }
        }
        if (env->ExceptionCheck()) {
            env->ExceptionClear();
        }
    }

}

//添加物品到背包
void addItemInventory(JNIEnv *env, jint id, jint count, jint damage) {
    jclass jc_ScriptManager = env->FindClass("com/mcbox/pesdkb/mcpelauncher/ScriptManager");
    jmethodID jm_SetCarriedItem = env->GetStaticMethodID(jc_ScriptManager, "nativeSetCarriedItem",
                                                         "(JIII)V");
    jmethodID jm_GetPlayerEnt = env->GetStaticMethodID(jc_ScriptManager, "nativeGetPlayerEnt",
                                                       "()J");
    jlong playerEnt = env->CallStaticLongMethod(jc_ScriptManager, jm_GetPlayerEnt);
    env->CallStaticVoidMethod(jc_ScriptManager, jm_SetCarriedItem, playerEnt, id, count, damage);

    env->DeleteLocalRef(jc_ScriptManager);
}

//附魔手持物品
void enchantItemInventory(JNIEnv *env, jintArray enchants) {
    //jclass jc_ScriptManager = env->FindClass("com/mcbox/pesdkb/mcpelauncher/ScriptManager");
    jclass jc_ScriptManager = env->FindClass("com/mcbox/pesdkd/mcpelauncher/ScriptManagerD");
    jmethodID jm_PlayerEnchant = env->GetStaticMethodID(jc_ScriptManager, "nativePlayerEnchant",
                                                        "(III)Z");
    jmethodID jm_GetSelectedSlotId = env->GetStaticMethodID(jc_ScriptManager,
                                                            "nativeGetSelectedSlotId", "()I");
    jint id = env->CallStaticIntMethod(jc_ScriptManager, jm_GetSelectedSlotId);
    jint *enchant = env->GetIntArrayElements(enchants, JNI_FALSE);
    int length = env->GetArrayLength(enchants);
    for (int i = 0; length > i; i += 2) {
        env->CallStaticBooleanMethod(jc_ScriptManager, jm_PlayerEnchant, id, enchant[i],
                                     -65336 - enchant[i + 1]);
    }
    env->DeleteLocalRef(jc_ScriptManager);
}

//加密
jbyteArray encrypt(JNIEnv *env, int encryptKey, jbyteArray bytes) {
    if (bytes == NULL)return NULL;
    jbyte *jb_Array = env->GetByteArrayElements(bytes, 0);
    int length = env->GetArrayLength(bytes);
    int key = encryptKey;
    jbyteArray jb_result = env->NewByteArray(length);
    jbyte jb_buf[length];
    for (int i = 0; i < length; i++) {
        jb_buf[i] = (jbyte) (jb_Array[i] ^ key);
        key = jb_buf[i];
    }
    env->ReleaseByteArrayElements(bytes, jb_Array, 0);
    env->SetByteArrayRegion(jb_result, 0, length, jb_buf);
    return jb_result;
}

//解密
jbyteArray decrypt(JNIEnv *env, int decryptKey, jbyteArray bytes) {
    if (bytes == NULL)return NULL;
    jbyte *jb_Array = env->GetByteArrayElements(bytes, 0);
    int length = env->GetArrayLength(bytes);
    int key = decryptKey;
    jbyteArray jb_result = env->NewByteArray(length);
    jbyte jb_buf[length];
    for (int i = length; i > 0; i--) {
        jb_buf[i] = (jbyte) (jb_Array[i] ^ jb_Array[i - 1]);
    }
    jb_buf[0] = (jbyte) (jb_Array[0] ^ key);
    env->ReleaseByteArrayElements(bytes, jb_Array, 0);
    env->SetByteArrayRegion(jb_result, 0, length, jb_buf);
    return jb_result;
}

bool checkPluginId(JNIEnv *env, jstring id) {
    jobject pluginIdArray = helperClient.getPluginIdArray(env);
    if (pluginIdArray != nullptr) {
        jclass jc_String = env->GetObjectClass(pluginIdArray);
        jmethodID jm_split = env->GetMethodID(jc_String, "split",
                                              "(Ljava/lang/String;)[Ljava/lang/String;");

        jobjectArray array = (jobjectArray) env->CallObjectMethod(pluginIdArray, jm_split,
                                                                  env->NewStringUTF(","));
        if (array != NULL) {
            int length = env->GetArrayLength(array);
            const char *chId = jstringToCharArr(env, id);
            for (int i = 0; i < length; i++) {
                const char *arrayId = jstringToCharArr(env,
                                                       (jstring) env->GetObjectArrayElement(array,
                                                                                            i));
                if (stringcmp(arrayId, chId)) {
                    return true;
                }
            }
        }
    }
    return false;
}

bool checkHelper(JNIEnv *env, jstring apkPath) {
    jobject Context = getApplicationContext(env);
    jclass jc_Context = env->GetObjectClass(Context);

    jclass jc_ZipFile = env->FindClass("java/util/zip/ZipFile");
    jmethodID jm_ZipFile_Init = env->GetMethodID(jc_ZipFile, "<init>", "(Ljava/lang/String;)V");
    jmethodID jm_getEntry = env->GetMethodID(jc_ZipFile, "getEntry",
                                             "(Ljava/lang/String;)Ljava/util/zip/ZipEntry;");
    jmethodID jm_getInputStream = env->GetMethodID(jc_ZipFile, "getInputStream",
                                                   "(Ljava/util/zip/ZipEntry;)Ljava/io/InputStream;");

    jclass jc_InputStream = env->FindClass("java/io/InputStream");
    jmethodID jm_read = env->GetMethodID(jc_InputStream, "read", "([B)I");

    jclass jc_ByteArrayOutputStream = env->FindClass("java/io/ByteArrayOutputStream");
    jmethodID jm_ByteArrayOutputStream_init = env->GetMethodID(jc_ByteArrayOutputStream, "<init>",
                                                               "()V");
    jmethodID jm_toString = env->GetMethodID(jc_ByteArrayOutputStream, "toString",
                                             "(Ljava/lang/String;)Ljava/lang/String;");
    jmethodID jm_write = env->GetMethodID(jc_ByteArrayOutputStream, "write", "([BII)V");
    jmethodID jm_close = env->GetMethodID(jc_ByteArrayOutputStream, "close", "()V");

    jclass jc_String = env->FindClass("java/lang/String");
    jmethodID jm_valueOf = env->GetStaticMethodID(jc_String, "valueOf", "(I)Ljava/lang/String;");
    jmethodID jm_contains = env->GetMethodID(jc_String, "contains", "(Ljava/lang/CharSequence;)Z");

    jobject zipFile = env->NewObject(jc_ZipFile, jm_ZipFile_Init, apkPath);
    jobject zipEntry = env->CallObjectMethod(zipFile, jm_getEntry,
                                             env->NewStringUTF("META-INF/CERT.RSA"));
    if (zipEntry == NULL) {
        LOGE("RSA not found");
        exit(1);
        return false;
    }
    jobject mtHookEntry = env->CallObjectMethod(zipFile, jm_getEntry,
                                                env->NewStringUTF("mthook/hook.dex"));
    if (mtHookEntry != NULL) {
        LOGE("mt hook");
        exit(1);
        return false;
    }
    jobject inputStream = env->CallObjectMethod(zipFile, jm_getInputStream, zipEntry);
    jobject result = env->NewObject(jc_ByteArrayOutputStream, jm_ByteArrayOutputStream_init);
    jbyteArray buffer = env->NewByteArray(1024);
    for (int length = 0; length != -1; length = env->CallIntMethod(inputStream, jm_read, buffer)) {
        env->CallVoidMethod(result, jm_write, buffer, 0, length);
    }
    jstring str = (jstring) env->CallObjectMethod(result, jm_toString, env->NewStringUTF("UTF-8"));
    env->CallVoidMethod(result, jm_close);
    if (env->CallBooleanMethod(str, jm_contains,
                               env->NewStringUTF("190723101931")) && env->CallBooleanMethod(str,
                                                                                            jm_contains,
                                                                                            env->NewStringUTF(
                                                                                                    "20690710101931"))) {
        const char *chs = env->GetStringUTFChars(str, 0);
        string cstr = string(chs);
        delete chs;
        if (cstr.find("190723101931", 0) != string::npos && cstr.find("20690710101931",
                                                                      0) != string::npos) {
            env->DeleteLocalRef(jc_Context);
            env->DeleteLocalRef(apkPath);
            env->DeleteLocalRef(jc_ZipFile);
            env->DeleteLocalRef(jc_InputStream);
            env->DeleteLocalRef(jc_ByteArrayOutputStream);
            env->DeleteLocalRef(jc_String);
            env->DeleteLocalRef(zipFile);
            env->DeleteLocalRef(zipEntry);
            env->DeleteLocalRef(inputStream);
            env->DeleteLocalRef(result);
            env->DeleteLocalRef(str);
            env->DeleteLocalRef(buffer);
            return true;
        }
    }
    exit(1);
    return false;
}


JNIEXPORT jobject JNICALL jniGetApplication(JNIEnv *env, jclass clazz) {
    return getApplicationContext(env);
}

JNIEXPORT void JNICALL jniSendBroadcast(JNIEnv *env, jclass clazz, jint type, jstring value) {
    sendBroadcast(env, type, value);
}

JNIEXPORT void JNICALL jniLoadScript(JNIEnv *env, jclass type, jobject file) {
    setScriptingEnabled(env, JNI_TRUE);
    loadScript(env, file);
}

JNIEXPORT void JNICALL jniLoadModPkg(JNIEnv *env, jclass type, jobject file) {
    setScriptingEnabled(env, JNI_TRUE);
    loadModPkg(env, file);
}

JNIEXPORT jboolean JNICALL
jniSetScriptEnabled(JNIEnv *env, jclass type, jstring path, jboolean enabled) {
    jclass jc_ScriptManager = env->FindClass("com/mcbox/pesdkb/mcpelauncher/ScriptManager");
    jmethodID jm_removeScript = env->GetStaticMethodID(jc_ScriptManager, "removeScript",
                                                       "(Ljava/lang/String;)V");
    jclass jc_File = env->FindClass("java/io/File");
    jmethodID jm_File_Init = env->GetMethodID(jc_File, "<init>", "(Ljava/lang/String;)V");
    jmethodID jm_getName = env->GetMethodID(jc_File, "getName", "()Ljava/lang/String;");

    jclass jc_ScriptManagerEx = env->FindClass("com/mcbox/pesdk/mcfloat/func/ScriptManagerEx");
    jmethodID jm_enableInternalScript = env->GetStaticMethodID(jc_ScriptManagerEx,
                                                               "enableInternalScript",
                                                               "(Ljava/lang/String;Z)V");

    jobject File = env->NewObject(jc_File, jm_File_Init, path);

    env->CallStaticVoidMethod(jc_ScriptManagerEx, jm_enableInternalScript,
                              env->CallObjectMethod(File, jm_getName), enabled);

    if (env->ExceptionCheck()) {
        env->ExceptionClear();
        env->DeleteLocalRef(jc_ScriptManager);
        return JNI_FALSE;
    }
    env->DeleteLocalRef(jc_ScriptManager);
    env->DeleteLocalRef(jc_File);
    return JNI_TRUE;
}

JNIEXPORT jbyteArray JNICALL jniDecryptFile(JNIEnv *env, jclass clazz, jint key, jbyteArray bytes) {
    return decrypt(env, key, bytes);
}

//进入主界面调用返回
JNIEXPORT void JNICALL enterHome(JNIEnv *env, jclass type) {
    LOGD("enter home.");
    sendBroadcast(env, TYPE_ENTER_HOME, NULL);
    savePlayerName(env);
    jobject activity = getMainActivity(env);
    jclass jc_Activity = env->GetObjectClass(activity);
    jmethodID getIntent = env->GetMethodID(jc_Activity, "getIntent", "()Landroid/content/Intent;");
    jmethodID setIntent = env->GetMethodID(jc_Activity, "setIntent", "(Landroid/content/Intent;)V");
    env->DeleteLocalRef(jc_Activity);
    if (helperClient.init(env, env->CallObjectMethod(activity, getIntent))) {//初始化
        env->CallVoidMethod(activity, setIntent, NULL);//移除启动痕迹
        LOGD("load mode = %d", helperClient.getLoadMode());
        setCurrentScript(env);
        deleteGameErrorFile(env);
        clearDuowanIntentData(env);
        setScriptingEnabled(env, JNI_TRUE);

        if (helperClient.getPluginIdArray(env) == nullptr) {
            deleteModErrorFile(env);
        }

        if (helperClient.getSkinPath(env) != nullptr) {
            //加载皮肤
            LOGD("load skin..");
            //LOGI("%s", jstringToCharArr(env, helperClient.getSkinPath(env)));
            loadTexture(env, helperClient.getSkinPath(env));
        }
        if (helperClient.getTextureArray(env) != nullptr) {
            //加载资源包
            LOGD("load textures..");
            //LOGI("%s", jstringToCharArr(env, helperClient.getTextureArray(env)));
            analysisTexturesData(env, helperClient.getTextureArray(env));
        }
        if (helperClient.getLoadMode() == 0 && !helperClient.isLoadEnd()) {
            LOGD("load mod by home");
            //LOGI("%s", jstringToCharArr(env, helperClient.getPluginArray(env)));
            frameCallback(env);
            MobEffect_initIds(env);
            analysisModData(env, helperClient.getPluginArray(env));
        }
        stopService(env);
        if (helperClient.showGameFloat()) {
            LOGD("show game float window");
            //显示辅助悬浮窗
            showGameFloat(env);
        }
    }
}

//进入游戏调用返回
JNIEXPORT void JNICALL enterGame(JNIEnv *env, jclass type) {
    LOGD("enter game.");
    sendBroadcast(env, TYPE_ENTER_GAME, NULL);
    if (helperClient.showDuoWanFloat()) {
        setRemote(env, JNI_FALSE);
        setLanGame(env, JNI_TRUE);
        setHasLevel(env);
    }
    if (helperClient.isRemoveCacheMode()) {
        deleteCache(env);
    }
    if (helperClient.getLoadMode() == 1 && !isRemote(env) && helperClient.getPluginArray(
            env) != nullptr && !helperClient.isLoadEnd()) {
        LOGI("load mod by my game");
        frameCallback(env);
        MobEffect_initIds(env);
        analysisModData(env, helperClient.getPluginArray(env));
    } else {
        deleteModErrorFile(env);
    }
    if (helperClient.getLoadMode() == 2 && helperClient.getPluginArray(
            env) != nullptr && !helperClient.isLoadEnd()) {
        LOGI("load mod by game");
        frameCallback(env);
        MobEffect_initIds(env);
        analysisModData(env, helperClient.getPluginArray(env));
        //setRequestedOrientation(env, 6);
    }
}

//退出游戏调用返回
JNIEXPORT void JNICALL leaveGame(JNIEnv *env, jclass type) {
    /* LOGD("player leave game.");
     setRemote(env, JNI_TRUE);
     sendBroadcast(env, TYPE_LEAVE_GAME, NULL);*/
}

JNIEXPORT void JNICALL jniWriterLog(JNIEnv *env, jclass type, jstring message) {
    if (message == nullptr)
        return;
    jclass jc_File = env->FindClass("java/io/File");
    jmethodID jm_File_Init = env->GetMethodID(jc_File, "<init>",
                                              "(Ljava/io/File;Ljava/lang/String;)V");
    jmethodID jm_getAbsolutePath = env->GetMethodID(jc_File, "getAbsolutePath",
                                                    "()Ljava/lang/String;");

    jobject file = env->NewObject(jc_File, jm_File_Init, getHelperDirectory(env),
                                  env->NewStringUTF("Mod/.RunLog.log"));

    FILE *fp = fopen(
            jstringToCharArr(env, (jstring) env->CallObjectMethod(file, jm_getAbsolutePath)), "w");
    fprintf(fp, "%s", jstringToCharArr(env, message));
    fclose(fp);

    env->DeleteLocalRef(file);
}

JNIEXPORT jboolean JNICALL
jniAddItemInventory(JNIEnv *env, jclass clazz, jint id, jint count, jint damage) {
    /*if (isRemote(env)) {
        //添加物品
        addItemInventory(env, id, count, damage);
        return JNI_TRUE;
    }*/
    return JNI_FALSE;
}

JNIEXPORT jboolean JNICALL
jniAddEnchantItemInventory(JNIEnv *env, jclass clazz, jint item_id, jint item_damage,
                           jintArray enchants) {
    /*if (isRemote(env)) {
        //添加物品
        addItemInventory(env, item_id, 1, item_damage);
        //附魔
        //enchantItemInventory(env, enchants);
        return JNI_TRUE;
    }*/
    return JNI_FALSE;
}

JNIEXPORT void JNICALL jniLogout(JNIEnv *env, jclass type) {
    logout(env);
}

JNIEXPORT jint JNICALL jniGetLevel(JNIEnv *env, jclass type) {
    return helperClient.getLevel();
}

JNIEXPORT jstring JNICALL jniGetUserUUID(JNIEnv *env, jclass type) {
    return helperClient.getUserUid(env);
}

JNIEXPORT jstring JNICALL jniGetFileMD5(JNIEnv *env, jclass type, jstring path) {
    string md5 = getFileMD5(string(env->GetStringUTFChars(path, 0)));
    return env->NewStringUTF(md5.c_str());
}

JNIEXPORT jobject JNICALL jniInitPluginApi(JNIEnv *env, jclass type, jstring id) {
    if (checkPluginId(env, id)) {
        jclass jc_HelperApi = env->FindClass("com/fmp/core/HelperApi");
        jmethodID jm_init = env->GetMethodID(jc_HelperApi, "<init>", "()V");
        launcher.setLauncherEnable(true);
        return env->NewObject(jc_HelperApi, jm_init);
    }
    return NULL;
}

JNIEXPORT void JNICALL jniAddItemToOffhand(JNIEnv *env, jclass type, jboolean b) {
    Launcher::setAddItemToOffhand(b);
}

JNIEXPORT void JNICALL jniCarriedItemToOffhand(JNIEnv *env, jclass type, jboolean b) {
    Launcher::setCarriedItemToOffhand(b);
}

JNIEXPORT void JNICALL jniAllItemAllowOffhand(JNIEnv *env, jclass type, jboolean b) {
    Launcher::setAllItemAllowOffhand(b);
}

JNIEXPORT void JNICALL jniKillingMode(JNIEnv *env, jclass type, jboolean mode) {
    Launcher::setKillingMode(mode);
}

JNIEXPORT void JNICALL jniClickKillMode(JNIEnv *env, jclass type, jboolean mode) {
    Launcher::setClickKillMode(mode);
}

JNIEXPORT void JNICALL
jniPlayerSetOperator(JNIEnv *env, jclass type, jstring id, jboolean isOperator) {
    if (checkPluginId(env, id) || helperClient.getLevel() >= 1)
        Launcher::PlayerSetOperator(isOperator);
}

JNIEXPORT jboolean JNICALL jniPlayerAttack(JNIEnv *env, jclass type, jstring id, jlong ent) {
    if (checkPluginId(env, id) || helperClient.getLevel() >= 2)
        return Launcher::PlayerAttack(ent);
    return JNI_FALSE;
}

JNIEXPORT void JNICALL jniPlayerAttacks(JNIEnv *env, jclass type, jstring id, jlongArray ents) {
    if (checkPluginId(env, id) || helperClient.getLevel() >= 2)
        return Launcher::PlayerAttacks(env, ents);
}

JNIEXPORT jlongArray JNICALL jniLevelGetAllPlayer(JNIEnv *env, jclass type) {
    return Launcher::LevelGetAllPlayer(env);
}

JNIEXPORT void JNICALL jniItemSetAllowOffhand(JNIEnv *env, jclass type, jshort id, jboolean b) {
    Launcher::ItemSetAllowOffhand(id, b);
}

JNIEXPORT void JNICALL jniDestroyModeTen(JNIEnv *env, jclass type, jboolean mode) {
    Launcher::setDestroyModeTen(mode);
}

JNIEXPORT void JNICALL jniDestroyModeClick(JNIEnv *env, jclass type, jboolean mode) {
    Launcher::setDestroyModeClick(mode);
}

JNIEXPORT void JNICALL jniFastBuild(JNIEnv *env, jclass type, jboolean mode) {
    Launcher::setFastBuild(mode);
}

JNIEXPORT jboolean JNICALL
jniAddMuitEnchantItemInventory(JNIEnv *env, jclass clazz, jint item_id, jint item_damage,
                               jintArray enchants, jint count) {
    if (isRemote(env)) {
        //添加物品
        addItemInventory(env, item_id, count, item_damage);
        //附魔
        //enchantItemInventory(env, enchants);
        return JNI_TRUE;
    }
    return JNI_FALSE;
}

JNIEXPORT jboolean JNICALL externalScripts(JNIEnv *env, jclass clazz) {
    return JNI_FALSE;
}

void Support::sendBroadcastHelper(JNIEnv *env, jint type, jstring value) {
    sendBroadcast(env, type, value);
}

void Support::enterGame() {
}

void Support::leaveGame() {
}

void Support::toast(JNIEnv *env, const char *message) {
    showToast(env, charArrToJstring(env, message));
}

void Support::addEntity(JNIEnv *env, jlong ent) {
    jmethodID jm_getInstance = env->GetStaticMethodID(class_gameManager, "getInstance",
                                                      "()Lcom/fmp/core/GameManager;");
    jmethodID jm_addEntity = env->GetMethodID(class_gameManager, "addEntity", "(J)V");
    jobject GameManager = env->CallStaticObjectMethod(class_gameManager, jm_getInstance);
    env->CallVoidMethod(GameManager, jm_addEntity, ent);
}

void Support::removeEntity(JNIEnv *env, jlong ent) {
    jmethodID jm_getInstance = env->GetStaticMethodID(class_gameManager, "getInstance",
                                                      "()Lcom/fmp/core/GameManager;");
    jmethodID jm_removeEntity = env->GetMethodID(class_gameManager, "removeEntity", "(J)V");
    jobject GameManager = env->CallStaticObjectMethod(class_gameManager, jm_getInstance);
    env->CallVoidMethod(GameManager, jm_removeEntity, ent);
}

jlongArray Support::getAllEntity(JNIEnv *env) {
    jmethodID jm_getInstance = env->GetStaticMethodID(class_gameManager, "getInstance",
                                                      "()Lcom/fmp/core/GameManager;");
    jmethodID jm_getAllEntity = env->GetMethodID(class_gameManager, "getAllEntity", "()[J");
    jobject GameManager = env->CallStaticObjectMethod(class_gameManager, jm_getInstance);
    jlongArray arr = (jlongArray) env->CallObjectMethod(GameManager, jm_getAllEntity);
    return arr;
}

void Support::clearAllEntity(JNIEnv *env) {
    jmethodID jm_getInstance = env->GetStaticMethodID(class_gameManager, "getInstance",
                                                      "()Lcom/fmp/core/GameManager;");
    jmethodID jm_clearAllEntity = env->GetMethodID(class_gameManager, "clearAllEntity", "()V");
    jobject GameManager = env->CallStaticObjectMethod(class_gameManager, jm_getInstance);
    env->CallVoidMethod(GameManager, jm_clearAllEntity);
}

static int registerNativeMethods(JNIEnv *env, const char *className, JNINativeMethod *gMethods,
                                 int numMethods) {
    jclass clazz = env->FindClass(className);
    if (clazz == NULL) {
        return JNI_FALSE;
    }
    if (env->RegisterNatives(clazz, gMethods, numMethods) < 0) {
        return JNI_FALSE;
    }
    return JNI_TRUE;
}

static int registerNativeMethods(JNIEnv *env) {
    JNINativeMethod GameManagerMethods[] = {{"getApplication",             "()Landroid/app/Application;",                  (void *) jniGetApplication},
                                            {"sendBroadcast",              "(ILjava/lang/String;)V",                       (void *) jniSendBroadcast},
                                            {"loadScript",                 "(Ljava/io/File;)V",                            (void *) jniLoadScript},
                                            {"setScriptEnabled",           "(Ljava/lang/String;Z)Z",                       (void *) jniSetScriptEnabled},
                                            {"loadModPkg",                 "(Ljava/io/File;)V",                            (void *) jniLoadModPkg},
                                            {"decryptFile",                "(I[B)[B",                                      (void *) jniDecryptFile},
                                            {"writerLog",                  "(Ljava/lang/String;)V",                        (void *) jniWriterLog},
                                            {"addItemInventory",           "(III)Z",                                       (void *) jniAddItemInventory},
                                            {"addEnchantItemInventory",    "(II[I)Z",                                      (void *) jniAddEnchantItemInventory},
                                            {"addEnchantItemInventory",    "(II[II)Z",                                     (void *) jniAddMuitEnchantItemInventory},
                                            {"logout",                     "()V",                                          (void *) jniLogout},
                                            {"getLevel",                   "()I",                                          (void *) jniGetLevel},
                                            {"getUserUUID",                "()Ljava/lang/String;",                         (void *) jniGetUserUUID},
                                            {"getFileMD5",                 "(Ljava/lang/String;)Ljava/lang/String;",       (void *) jniGetFileMD5},
                                            {"initPluginApi",              "(Ljava/lang/String;)Lcom/fmp/core/HelperApi;", (void *) jniInitPluginApi},
                                            {"nativeAddItemToOffhand",     "(Z)V",                                         (void *) jniAddItemToOffhand},
                                            {"nativeCarriedItemToOffhand", "(Z)V",                                         (void *) jniCarriedItemToOffhand},
                                            {"nativeAllItemAllowOffhand",  "(Z)V",                                         (void *) jniAllItemAllowOffhand},
                                            {"nativeKillingMode",          "(Z)V",                                         (void *) jniKillingMode},
                                            {"nativeClickKillMode",        "(Z)V",                                         (void *) jniClickKillMode},
                                            {"nativePlayerSetOperator",    "(Ljava/lang/String;Z)V",                       (void *) jniPlayerSetOperator},
                                            {"nativePlayerAttack",         "(Ljava/lang/String;J)Z",                       (void *) jniPlayerAttack},
                                            {"nativePlayerAttacks",        "(Ljava/lang/String;[J)V",                      (void *) jniPlayerAttacks},
                                            {"nativeLevelGetAllPlayer",    "()[J",                                         (void *) jniLevelGetAllPlayer},
                                            {"nativeItemSetAllowOffhand",  "(SZ)V",                                        (void *) jniItemSetAllowOffhand},
                                            {"nativeDestroyModeTen",       "(Z)V",                                         (void *) jniDestroyModeTen},
                                            {"nativeDestroyModeClick",     "(Z)V",                                         (void *) jniDestroyModeClick},
                                            {"nativeFastBuild",            "(Z)V",                                         (void *) jniFastBuild}};
    JNINativeMethod LauncherRuntimeImplMethods[] = {{"enterHome", "()V", (void *) enterHome}};
    JNINativeMethod ScriptManagerExMethods[] = {{"enterGame", "()V", (void *) enterGame}};
    JNINativeMethod ScriptManagerMethods[] = {{"leaveGame", "()V", (void *) leaveGame}};
    JNINativeMethod LauncherManagerImplMethods[] = {{"externalScripts", "()Z", (void *) externalScripts}};
    if (!registerNativeMethods(env, "com/fmp/core/GameManager", GameManagerMethods,
                               sizeof(GameManagerMethods) / sizeof(GameManagerMethods[0]))) {
        return JNI_FALSE;
    }
    if (!registerNativeMethods(env, "com/mcbox/pesdk/launcher/impl/LauncherRuntimeImpl",
                               LauncherRuntimeImplMethods,
                               sizeof(LauncherRuntimeImplMethods) / sizeof(LauncherRuntimeImplMethods[0]))) {
        return JNI_FALSE;
    }
    if (!registerNativeMethods(env, "com/mcbox/pesdk/mcfloat/func/ScriptManagerEx",
                               ScriptManagerExMethods,
                               sizeof(ScriptManagerExMethods) / sizeof(ScriptManagerExMethods[0]))) {
        return JNI_FALSE;
    }
    if (!registerNativeMethods(env, "com/mcbox/pesdkb/mcpelauncher/ScriptManager",
                               ScriptManagerMethods,
                               sizeof(ScriptManagerMethods) / sizeof(ScriptManagerMethods[0]))) {
        return JNI_FALSE;
    }
    if (!registerNativeMethods(env, "com/mcbox/pesdk/launcher/LauncherManagerImpl",
                               LauncherManagerImplMethods,
                               sizeof(LauncherManagerImplMethods) / sizeof(LauncherManagerImplMethods[0]))) {
        return JNI_FALSE;
    }
    return JNI_TRUE;
}


void JNI_OnUnload(JavaVM *vm, void *reserved) {
    LOGD("netease JNI unload...");
}


JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    LOGD("netease JNI load...");
    JNIEnv *env;
    //判断JavaVM获取的JNIEnv是否成功
    if (vm->_JavaVM::GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK)
        return JNI_ERR;
    if (registerNativeMethods(env) != JNI_TRUE) {
        exit(0);
    }
    deleteDatFile(env);
    initMsHook(vm);
    checkXuxiqwq(env);
    //全局变量
    jclass clazz = env->FindClass("com/fmp/core/GameManager");
    class_gameManager = (jclass) env->NewGlobalRef(clazz);
    return JNI_VERSION_1_6;
}

