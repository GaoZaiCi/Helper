//
// Created by Gao在此 on 2020/2/15.
//
#include "jni.h"
#include "fmp-launcher.h"
#ifndef HELPER_HELPER_H
#define HELPER_HELPER_H

class Helper {
    int userLevel;
    const char* userUid;
    int loadMode;
    bool loadEnd;
    bool duoWanFloat;
    bool gameFloat;
    bool removeCacheMode;
    const char *pluginArray;
    const char* pluginIdArray;
    const char *skinPath;
    const char *textureArray;
public:
    bool init(JNIEnv *env, jobject intent);

    void setLoadEnd(bool load);

    bool isLoadEnd();

    bool showDuoWanFloat();

    bool showGameFloat();

    bool isRemoveCacheMode();

    int getLoadMode();

    int getLevel();

    jstring getUserUid(JNIEnv *env);

    jstring getPluginIdArray(JNIEnv *env);

    jstring getPluginArray(JNIEnv *env);

    jstring getSkinPath(JNIEnv *env);

    jstring getTextureArray(JNIEnv *env);

};


#endif //HELPER_HELPER_H

jstring charArrToJstring(JNIEnv *env, const char *arr);
const char *jstringToCharArr(JNIEnv *env, jstring str);
bool checkHelper(JNIEnv *env,jstring apkPath);
Launcher launcher;

bool Helper::init(JNIEnv *env, jobject intent) {
    LOGI("init intent");
    if (intent != nullptr) {
        jclass jc_Intent = env->FindClass("android/content/Intent");
        jmethodID getIntExtra = env->GetMethodID(jc_Intent, "getIntExtra",
                                                 "(Ljava/lang/String;I)I");
        jmethodID getBooleanExtra = env->GetMethodID(jc_Intent, "getBooleanExtra",
                                                     "(Ljava/lang/String;Z)Z");
        jmethodID getStringExtra = env->GetMethodID(jc_Intent, "getStringExtra",
                                                    "(Ljava/lang/String;)Ljava/lang/String;");
        jmethodID getStringArrayExtra = env->GetMethodID(jc_Intent, "getStringArrayExtra",
                                                         "(Ljava/lang/String;)[Ljava/lang/String;");

        userLevel = env->CallIntMethod(intent, getIntExtra, env->NewStringUTF("userLevel"), -1);
        loadMode = env->CallIntMethod(intent, getIntExtra, env->NewStringUTF("loadMode"), 0);
        loadEnd = false;
        duoWanFloat = env->CallBooleanMethod(intent, getBooleanExtra,
                                             env->NewStringUTF("duoWanFloat"), JNI_FALSE);
        gameFloat = env->CallBooleanMethod(intent, getBooleanExtra, env->NewStringUTF("gameFloat"),
                                           JNI_FALSE);
        removeCacheMode = env->CallBooleanMethod(intent, getBooleanExtra,
                                                 env->NewStringUTF("removeCacheMode"), JNI_FALSE);
        userUid = jstringToCharArr(env, (jstring) env->CallObjectMethod(intent, getStringExtra,
                                                                        env->NewStringUTF(
                                                                                "userUid")));
        pluginArray = jstringToCharArr(env, (jstring) env->CallObjectMethod(intent, getStringExtra,
                                                                            env->NewStringUTF(
                                                                                    "pluginArray")));
        pluginIdArray = jstringToCharArr(env,
                                         (jstring) env->CallObjectMethod(intent, getStringExtra,
                                                                         env->NewStringUTF(
                                                                                 "pluginIdArray")));
        skinPath = jstringToCharArr(env, (jstring) env->CallObjectMethod(intent, getStringExtra,
                                                                         env->NewStringUTF(
                                                                                 "skinPath")));
        textureArray = jstringToCharArr(env, (jstring) env->CallObjectMethod(intent, getStringExtra,
                                                                             env->NewStringUTF(
                                                                                     "textureArray")));
        jstring hookChecks = (jstring) env->CallObjectMethod(intent, getStringExtra,
                                                             env->NewStringUTF("hookChecks"));

        launcher.setHookChecks(jstringToCharArr(env, hookChecks));

        jstring apkPath=(jstring) env->CallObjectMethod(intent, getStringExtra,
                                                        env->NewStringUTF(
                                                                "apkPath"));
        if (!checkHelper(env,apkPath)){
            exit(1);
            return false;
        }

        if (userLevel >= 2) {
            launcher.setLauncherEnable(true);
        }
        env->DeleteLocalRef(jc_Intent);
        LOGI("init intent success");
        return true;
    } else {
        return false;
    }
}

void Helper::setLoadEnd(bool load) {
    loadEnd = load;
}

bool Helper::isLoadEnd() {
    return loadEnd;
}

bool Helper::showDuoWanFloat() {
    return duoWanFloat;
}

bool Helper::showGameFloat() {
    return gameFloat;
}

bool Helper::isRemoveCacheMode() {
    return removeCacheMode;
}

int Helper::getLoadMode() {
    return loadMode;
}

int Helper::getLevel() {
    return userLevel;
}

jstring Helper::getUserUid(JNIEnv *env) {
    if (userUid != nullptr)
        return charArrToJstring(env, userUid);
    return nullptr;
}

jstring Helper::getPluginIdArray(JNIEnv *env) {
    if (pluginIdArray != nullptr)
        return charArrToJstring(env, pluginIdArray);
    return nullptr;
}

jstring Helper::getPluginArray(JNIEnv *env) {
    if (pluginArray != nullptr)
        return charArrToJstring(env, pluginArray);
    return nullptr;
}

jstring Helper::getSkinPath(JNIEnv *env) {
    if (skinPath != nullptr)
        return charArrToJstring(env, skinPath);
    return nullptr;
}

jstring Helper::getTextureArray(JNIEnv *env) {
    if (textureArray != nullptr)
        return charArrToJstring(env, textureArray);
    return nullptr;
}

