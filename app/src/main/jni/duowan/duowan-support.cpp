//
// Created by Gao在此 on 2019/7/21.
//

#include <jni.h>
#include "../android/android_log.h"


void setLaunchedFromDuowan(JNIEnv *env) {
    jclass jc_MainActivity = env->FindClass("com/mojang/minecraftpe/MainActivity");
    jmethodID jm_MainActivity_getInstance = env->GetStaticMethodID(jc_MainActivity, "getInstance",
                                                                   "()Lcom/mojang/minecraftpe/MainActivity;");
    jfieldID jf_launchedFromDuowan = env->GetFieldID(jc_MainActivity, "launchedFromDuowan", "Z");

    jobject MainActivity = env->CallStaticObjectMethod(jc_MainActivity,
                                                       jm_MainActivity_getInstance);

    env->SetBooleanField(MainActivity, jf_launchedFromDuowan, JNI_TRUE);

    /*jclass jc_Class = env->FindClass("java/lang/Class");
    jmethodID jm_forName = env->GetStaticMethodID(jc_Class, "forName",
                                                  "(Ljava/lang/String;)Ljava/lang/Class;");
    jmethodID jm_getDeclaredField = env->GetMethodID(jc_Class, "getDeclaredField",
                                                     "(Ljava/lang/String;)Ljava/lang/reflect/Field;");

    jclass jc_Field = env->FindClass("java/lang/reflect/Field");
    jmethodID jm_Field_setAccessible = env->GetMethodID(jc_Field, "setAccessible", "(Z)V");
    jmethodID jm_Field_set = env->GetMethodID(jc_Field, "set",
                                              "(Ljava/lang/Object;Ljava/lang/Object;)V");

    jobject clazz = env->CallStaticObjectMethod(jc_Class, jm_forName, env->NewStringUTF(
            "com.mojang.minecraftpe.MainActivity"));

    jobject Field = env->CallObjectMethod(clazz, jm_getDeclaredField,
                                          env->NewStringUTF("launchedFromDuowan"));
    env->CallVoidMethod(Field, jm_Field_setAccessible, JNI_TRUE);
    env->CallVoidMethod(Field, jm_Field_set, MainActivity, JNI_TRUE);*/

    if (env->ExceptionCheck()) {
        env->ExceptionDescribe();
        env->ExceptionClear();
        LOGD("ExceptionClear");
    }

    env->DeleteLocalRef(jc_MainActivity);
    /*env->DeleteLocalRef(jc_Class);
    env->DeleteLocalRef(jc_Field);*/
}

void initCurrentMainActivity(JNIEnv *env) {
    jclass jc_MainActivity = env->FindClass("com/mojang/minecraftpe/MainActivity");
    jfieldID jf_currentMainActivity = env->GetStaticFieldID(jc_MainActivity, "currentMainActivity",
                                                            "Ljava/lang/ref/WeakReference;");
    jmethodID jm_MainActivity_getInstance = env->GetStaticMethodID(jc_MainActivity, "getInstance",
                                                                   "()Lcom/mojang/minecraftpe/MainActivity;");

    jclass jc_WeakReference = env->FindClass("java/lang/ref/WeakReference");
    jmethodID jm_WeakReference_init = env->GetMethodID(jc_WeakReference, "<init>",
                                                       "(Ljava/lang/Object;)V");

    jobject MainActivity = env->CallStaticObjectMethod(jc_MainActivity,
                                                       jm_MainActivity_getInstance);
    jobject WeakReference = env->NewObject(jc_WeakReference, jm_WeakReference_init, MainActivity);

    env->SetStaticObjectField(jc_MainActivity, jf_currentMainActivity, WeakReference);

    env->DeleteLocalRef(jc_MainActivity);
    env->DeleteLocalRef(jc_WeakReference);
}

void initLibrary(JNIEnv *env) {
    jclass jc_MainActivity = env->FindClass("com/mojang/minecraftpe/MainActivity");
    jfieldID jf_MC_NATIVE_LIBRARY_LOCATION = env->GetStaticFieldID(jc_MainActivity,
                                                                   "MC_NATIVE_LIBRARY_LOCATION",
                                                                   "Ljava/lang/String;");
    jmethodID jm_MainActivity_getInstance = env->GetStaticMethodID(jc_MainActivity, "getInstance",
                                                                   "()Lcom/mojang/minecraftpe/MainActivity;");

    jobject MainActivity = env->CallStaticObjectMethod(jc_MainActivity,
                                                       jm_MainActivity_getInstance);

    env->DeleteLocalRef(jc_MainActivity);
}

void initDuoWanLoader(JNIEnv *env) {
    jclass jc_Intent = env->FindClass("android/content/Intent");
    jmethodID jm_Intent_init = env->GetMethodID(jc_Intent, "<init>", "(Ljava/lang/String;)V");
    jfieldID jf_Intent_Action = env->GetStaticFieldID(jc_Intent, "ACTION_VIEW",
                                                      "Ljava/lang/String;");

    jmethodID jm_Intent_putExtraBoolean = env->GetMethodID(jc_Intent, "putExtra",
                                                           "(Ljava/lang/String;Z)Landroid/content/Intent;");


    jclass jc_MainActivity = env->FindClass("com/mojang/minecraftpe/MainActivity");
    jmethodID jm_MainActivity_getInstance = env->GetStaticMethodID(jc_MainActivity, "getInstance",
                                                                   "()Lcom/mojang/minecraftpe/MainActivity;");

    jclass jc_FloatLauncherManager = env->FindClass(
            "com/huya/mcfloat/launcher/FloatLauncherManager");
    jmethodID jm_FloatLauncherManager_getInstance = env->GetStaticMethodID(jc_FloatLauncherManager,
                                                                           "getInstance",
                                                                           "()Lcom/huya/mcfloat/launcher/FloatLauncherManager;");
    jmethodID jm_FloatLauncherManager_init = env->GetMethodID(jc_FloatLauncherManager, "init",
                                                              "(Landroid/app/Activity;[Ljava/lang/String;)V");
    jobject MainActivity = env->CallStaticObjectMethod(jc_MainActivity,
                                                       jm_MainActivity_getInstance);
    jclass jc_Activity = env->GetObjectClass(MainActivity);

    jmethodID jm_MainActivity_setIntent = env->GetMethodID(jc_Activity, "setIntent",
                                                           "(Landroid/content/Intent;)V");

    jstring Action = (jstring) env->GetStaticObjectField(jc_Intent, jf_Intent_Action);

    jobject MainActivity_Intent = env->NewObject(jc_Intent, jm_Intent_init,
                                                 Action);

    env->CallObjectMethod(MainActivity_Intent, jm_Intent_putExtraBoolean,
                          env->NewStringUTF("hymcfloatSupport"), JNI_TRUE);
    env->CallVoidMethod(MainActivity, jm_MainActivity_setIntent, MainActivity_Intent);

    jobject FloatLauncherManager = env->CallStaticObjectMethod(jc_FloatLauncherManager,
                                                               jm_FloatLauncherManager_getInstance);
    env->CallVoidMethod(FloatLauncherManager, jm_FloatLauncherManager_init, MainActivity, NULL);

    if (env->ExceptionCheck()) {
        env->ExceptionDescribe();
        env->ExceptionClear();
        LOGD("ExceptionClear");
    }

    env->DeleteLocalRef(jc_Intent);
    env->DeleteLocalRef(jc_MainActivity);
    env->DeleteLocalRef(jc_FloatLauncherManager);
}


void JNI_OnUnload(JavaVM *vm, void *reserved) {
    LOGD("DuoWan JNI unload...");
}


JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    LOGD("DuoWan JNI load...");
//定义JNIEnv为NULL
    JNIEnv *env = NULL;
//判断JavaVM获取的JNIEnv是否成功
    if (vm->_JavaVM::GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK)
        return JNI_ERR;
    LOGI("set..");
    setLaunchedFromDuowan(env);
    LOGI("init..");
    initCurrentMainActivity(env);
    LOGI("init....");
    initDuoWanLoader(env);
    LOGD("start.");
    return JNI_VERSION_1_6;
}