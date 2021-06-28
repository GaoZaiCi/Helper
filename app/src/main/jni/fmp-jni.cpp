#include <jni.h>
#include <unistd.h>
#include <dlfcn.h>
#include <stdlib.h>
#include <stdio.h>
#include <time.h>
#include <string>
#include <cstring>
#include <iostream>
#include <fstream>
#include <sstream>
#include "android/android_log.h"
#include "Base64.h"
//#include "JavaFileUtil.h"

using namespace std;

__attribute__((section (".helper"))) jobject getApplicationContext(JNIEnv *env);

__attribute__((section (".helper"))) bool stringcmp(const char *str1, const char *str2);

__attribute__((section (".helper"))) jbyteArray encrypt(JNIEnv *env, int encryptKey, jbyteArray bytes);

__attribute__((section (".helper"))) jbyteArray decrypt(JNIEnv *env, int decryptKey, jbyteArray bytes);

__attribute__((section (".helper"))) void showToast(JNIEnv *env, jstring toast);

__attribute__((section (".helper"))) void showToast(JNIEnv *env, char *str);

__attribute__((section (".helper"))) char *jbyteaArrayToChars(JNIEnv *env, jbyteArray bytearray);

__attribute__((section (".helper"))) char *jstringToCharArr(jstring str);

__attribute__((section (".helper"))) jstring charArrToJstring(char *arr);

//字符信息
const char *AppPackageName = "net.fmp.helper";
const char *BmobAPPID = "MVQCYDZVVWBjBlQ2YgMHZmJWVGZjA1o1PlNYYTlVX2c=";
const char *Signatures = "308202313082019aa0030201020204079e6e46300d06092a864886f70d0101050500305c310b300906035504061302434e310e300c06035504081305577548616e310e300c0603550407130548754265693111300f060355040a1308464d50205465616d310c300a060355040b1303464d50310c300a060355040313036465763020170d3139303732333130313933315a180f32303639303731303130313933315a305c310b300906035504061302434e310e300c06035504081305577548616e310e300c0603550407130548754265693111300f060355040a1308464d50205465616d310c300a060355040b1303464d50310c300a0603550403130364657630819f300d06092a864886f70d010101050003818d0030818902818100abf5493d7d55f6d7b8259b60279410cae7869ca3228775b441e285e5fe5bd292791c920e8e00b4189a6b756e96b9cbc7e8c228646e30bc7d3819a42edca985cdae4cf2d29aad0101a9fb655cb5566406fc2f7cd9c5c7c1f5917b95d23aef4cd3f821940ba0dbf2e5f77f5d1249cc2201103314bfbcf77d9af4a13ebd0151d1590203010001300d06092a864886f70d01010505000381810010e47c2ba93fa615ff2d55fc1c7a75f2e5f7ec8aefb5ab8eecfe97ee9550b0860ac57559cb1b43fe115413dd17224328b9e24ff5098e5f82e0071dea88cf9c6696db1f6cb0db7d7938eec093fa951abe2602b48a8a6d125c57905a33a929e4094c67045b98c3313206551e52af70f6e2d5fe1081762b7ed89144ecc006a2921c";

JNIEnv *mEnv = NULL;

jboolean initNative = JNI_FALSE;

static int userLevel = -1;

const bool isDevMode = JNI_TRUE;

__attribute__((section (".helper"))) jobject getApplicationContext(JNIEnv *env) {
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

__attribute__((section (".helper"))) bool stringcmp(const char *str1, const char *str2) {
    return strcmp(str1, str2) == 0;
}

//加密字符串
__attribute__((section (".helper"))) jbyteArray encrypt(JNIEnv *env, int encryptKey, jbyteArray bytes) {
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

//解密字符串
__attribute__((section (".helper"))) jbyteArray decrypt(JNIEnv *env, int decryptKey, jbyteArray bytes) {
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

//输出Toast信息
__attribute__((section (".helper"))) void showToast(JNIEnv *env, jstring toast) {
    jclass jc_Toast = env->FindClass("android/widget/Toast");
    jmethodID jm_makeText = env->GetStaticMethodID(jc_Toast, "makeText",
                                                   "(Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;");
    jobject jo_Toast = env->CallStaticObjectMethod(jc_Toast, jm_makeText,
                                                   getApplicationContext(env), toast, 0);
    jmethodID jm_Show = env->GetMethodID(jc_Toast, "show", "()V");
    env->CallVoidMethod(jo_Toast, jm_Show);
}

//输出Toast信息
__attribute__((section (".helper"))) void showToast(JNIEnv *env, char *str) {
    jclass tclss = env->FindClass("android/widget/Toast");
    jmethodID mid = env->GetStaticMethodID(tclss, "makeText",
                                           "(Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;");
    jobject job = env->CallStaticObjectMethod(tclss, mid, getApplicationContext(env),
                                              env->NewStringUTF(str), 0);
    jmethodID showId = env->GetMethodID(tclss, "show", "()V");
    env->CallVoidMethod(job, showId);
}

__attribute__((section (".helper"))) char *jbyteaArrayToChars(JNIEnv *env, jbyteArray bytearray) {
    char *chars = NULL;
    jbyte *bytes;
    bytes = env->GetByteArrayElements(bytearray, 0);
    int chars_len = env->GetArrayLength(bytearray);
    chars = new char[chars_len + 1];
    memset(chars, 0, chars_len + 1);
    memcpy(chars, bytes, chars_len);
    chars[chars_len] = 0;
    env->ReleaseByteArrayElements(bytearray, bytes, 0);
    return chars;
}

__attribute__((section (".helper"))) char *jstringToCharArr(jstring str) {
    return (char *) mEnv->GetStringUTFChars(str, 0);
}

__attribute__((section (".helper"))) jstring charArrToJstring(char *arr) {
    return mEnv->NewStringUTF(arr);
}

__attribute__((section (".helper"))) std::string jstringToStr(JNIEnv *env, jstring jstr) {
    char *rtn = NULL;
    jclass clsstring = env->FindClass("java/lang/String");
    jstring strencode = env->NewStringUTF("UTF-8");
    jmethodID mid = env->GetMethodID(clsstring, "getBytes", "(Ljava/lang/String;)[B");
    jbyteArray barr = (jbyteArray) env->CallObjectMethod(jstr, mid, strencode);
    jsize alen = env->GetArrayLength(barr);
    jbyte *ba = env->GetByteArrayElements(barr, JNI_FALSE);
    if (alen > 0) {
        rtn = (char *) malloc(alen + 1);
        memcpy(rtn, ba, alen);
        rtn[alen] = 0;
    }
    env->ReleaseByteArrayElements(barr, ba, 0);
    std::string stemp(rtn);
    free(rtn);
    return stemp;
}

__attribute__((section (".helper"))) void bmob_resetDomain(JNIEnv *env, char *domainStr) {
    jclass jc_Bmob = env->FindClass("cn/bmob/v3/Bmob");
    jmethodID jm_resetDomain = env->GetStaticMethodID(jc_Bmob, "resetDomain",
                                                      "(Ljava/lang/String;)V");
    env->CallStaticVoidMethod(jc_Bmob, jm_resetDomain, env->NewStringUTF(domainStr));

    env->DeleteLocalRef(jc_Bmob);
}

__attribute__((section (".helper"))) void bmob_init(JNIEnv *env) {
    bmob_resetDomain(env, (char *) "http://sdk.gaozaici.cn/8/");

    jstring deBase64 = charArrToJstring((char *) base64_decode((unsigned char *) BmobAPPID));
    //转byte数组
    jclass jc_String = env->FindClass("java/lang/String");
    jmethodID jm_getBytes = env->GetMethodID(jc_String, "getBytes", "()[B");
    jbyteArray toByteArray = (jbyteArray) env->CallObjectMethod(deBase64, jm_getBytes);
    //解密一次
    jbyteArray jb_decryptStr = decrypt(env, 99, toByteArray);
    //再解密一次
    jbyteArray jb_decryptStrTo = decrypt(env, 99, jb_decryptStr);
    //转C++字符串
    char *deEnd = jbyteaArrayToChars(env, jb_decryptStrTo);
    //转Java字符串
    jstring out = env->NewStringUTF(deEnd);
    //初始化Bmob
    jclass jc_Bmob = env->FindClass("cn/bmob/v3/Bmob");
    jmethodID jm_initialize = env->GetStaticMethodID(jc_Bmob, "initialize",
                                                     "(Landroid/content/Context;Ljava/lang/String;)V");
    env->CallStaticVoidMethod(jc_Bmob, jm_initialize, getApplicationContext(env), out);

    env->DeleteLocalRef(deBase64);
    env->DeleteLocalRef(jc_String);
    env->DeleteLocalRef(toByteArray);
    env->DeleteLocalRef(jb_decryptStr);
    env->DeleteLocalRef(jb_decryptStrTo);
    env->DeleteLocalRef(out);
    env->DeleteLocalRef(jc_Bmob);

}

//Jni版签名验证
__attribute__((section (".helper"))) void jniVerification(JNIEnv *env) {
    //获取context
    jobject mContext = getApplicationContext(env);
    //获得方法对象
    jclass native_class = env->GetObjectClass(mContext);
    jmethodID pm_id = env->GetMethodID(native_class, "getPackageManager",
                                       "()Landroid/content/pm/PackageManager;");
    jobject pm_obj = env->CallObjectMethod(mContext, pm_id);
    jclass pm_clazz = env->GetObjectClass(pm_obj);
    // 得到 getPackageInfo 方法的 ID
    jmethodID package_info_id = env->GetMethodID(pm_clazz, "getPackageInfo",
                                                 "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");
    jmethodID mId = env->GetMethodID(native_class, "getPackageName", "()Ljava/lang/String;");
    jstring pkg_str = static_cast<jstring>(env->CallObjectMethod(mContext, mId));
    // 获得应用包的信息
    jobject pi_obj = env->CallObjectMethod(pm_obj, package_info_id, pkg_str, 64);
    // 获得 PackageInfo 类
    jclass pi_clazz = env->GetObjectClass(pi_obj);
    // 获得签名数组属性的 ID
    jfieldID signatures_fieldId = env->GetFieldID(pi_clazz, "signatures",
                                                  "[Landroid/content/pm/Signature;");
    jobject signatures_obj = env->GetObjectField(pi_obj, signatures_fieldId);
    jobjectArray signaturesArray = (jobjectArray) signatures_obj;
    jsize size = env->GetArrayLength(signaturesArray);
    jobject signature_obj = env->GetObjectArrayElement(signaturesArray, 0);
    jclass signature_clazz = env->GetObjectClass(signature_obj);
    jmethodID string_id = env->GetMethodID(signature_clazz, "toCharsString",
                                           "()Ljava/lang/String;");
    jstring str = static_cast<jstring>(env->CallObjectMethod(signature_obj, string_id));
    const char *c_msg = env->GetStringUTFChars(str, 0);
    //对比签名
    if (strcmp(c_msg, Signatures) != 0) {
        //退出
        exit(1);
    }
    env->DeleteLocalRef(mContext);
    env->DeleteLocalRef(native_class);
    env->DeleteLocalRef(pm_obj);
    env->DeleteLocalRef(pm_clazz);
    env->DeleteLocalRef(pkg_str);
    env->DeleteLocalRef(pi_obj);
    env->DeleteLocalRef(pi_clazz);
    env->DeleteLocalRef(signature_obj);
    env->DeleteLocalRef(signaturesArray);
    env->DeleteLocalRef(signature_clazz);
    env->DeleteLocalRef(str);
}


__attribute__((section (".helper"))) bool checkHelper(JNIEnv *env) {
    jclass activityThreadClass = env->FindClass("android/app/ActivityThread");
    jmethodID currentActivityThreadMethod = env->GetStaticMethodID(activityThreadClass,"currentActivityThread","()Landroid/app/ActivityThread;");
    jobject currentActivityThread = env->CallStaticObjectMethod(activityThreadClass,currentActivityThreadMethod);
    jfieldID sPackageManagerFieldId = env->GetStaticFieldID(activityThreadClass, "sPackageManager","Landroid/content/pm/IPackageManager;");
    jobject sPackageManager = env->GetStaticObjectField(env->GetObjectClass(currentActivityThread),sPackageManagerFieldId);
    env->DeleteLocalRef(activityThreadClass);
    env->DeleteLocalRef(currentActivityThread);

    jobject cPMSO = sPackageManager;
    jclass cPMSC = env->GetObjectClass(cPMSO);
    jclass cPMSFC = env->GetSuperclass(cPMSC);
    jclass proxyClass = env->FindClass("java/lang/reflect/Proxy");
    if (env->IsAssignableFrom(cPMSFC, proxyClass)) {
        LOGI("pms hook");
        exit(1);
        return false;
    }
    env->DeleteLocalRef(cPMSO);
    env->DeleteLocalRef(cPMSC);
    env->DeleteLocalRef(cPMSFC);
    env->DeleteLocalRef(proxyClass);

    jobject Context = getApplicationContext(env);
    jclass jc_Context = env->GetObjectClass(Context);
    jmethodID jm_Context_getPackageResourcePath=env->GetMethodID(jc_Context,"getPackageResourcePath","()Ljava/lang/String;");

    jstring apkPath = (jstring)env->CallObjectMethod(Context, jm_Context_getPackageResourcePath);

    jclass jc_ZipFile = env->FindClass("java/util/zip/ZipFile");
    jmethodID jm_ZipFile_Init = env->GetMethodID(jc_ZipFile, "<init>", "(Ljava/lang/String;)V");
    jmethodID jm_getEntry=env->GetMethodID(jc_ZipFile,"getEntry","(Ljava/lang/String;)Ljava/util/zip/ZipEntry;");
    jmethodID jm_getInputStream = env->GetMethodID(jc_ZipFile, "getInputStream","(Ljava/util/zip/ZipEntry;)Ljava/io/InputStream;");

    jclass jc_InputStream=env->FindClass("java/io/InputStream");
    jmethodID jm_read=env->GetMethodID(jc_InputStream,"read","([B)I");

    jclass jc_ByteArrayOutputStream=env->FindClass("java/io/ByteArrayOutputStream");
    jmethodID jm_ByteArrayOutputStream_init=env->GetMethodID(jc_ByteArrayOutputStream,"<init>","()V");
    jmethodID jm_toString=env->GetMethodID(jc_ByteArrayOutputStream,"toString","(Ljava/lang/String;)Ljava/lang/String;");
    jmethodID jm_write=env->GetMethodID(jc_ByteArrayOutputStream,"write","([BII)V");
    jmethodID jm_close=env->GetMethodID(jc_ByteArrayOutputStream,"close","()V");

    jclass jc_String = env->FindClass("java/lang/String");
    jmethodID jm_valueOf=env->GetStaticMethodID(jc_String,"valueOf","(I)Ljava/lang/String;");
    jmethodID jm_contains=env->GetMethodID(jc_String,"contains","(Ljava/lang/CharSequence;)Z");

    jobject zipFile = env->NewObject(jc_ZipFile, jm_ZipFile_Init, apkPath);
    jobject zipEntry=env->CallObjectMethod(zipFile,jm_getEntry,env->NewStringUTF("META-INF/CERT.RSA"));
    if (zipEntry==NULL){
        LOGE("RSA not found");
        exit(1);
        return false;
    }
    jobject mtHookEntry=env->CallObjectMethod(zipFile,jm_getEntry,env->NewStringUTF("mthook/hook.dex"));
    if (mtHookEntry!=NULL){
        LOGE("mt hook");
        exit(1);
        return false;
    }
    jobject inputStream=env->CallObjectMethod(zipFile,jm_getInputStream,zipEntry);
    jobject result=env->NewObject(jc_ByteArrayOutputStream,jm_ByteArrayOutputStream_init);
    jbyteArray buffer=env->NewByteArray(1024);
    for (int length=0;length!=-1;length = env->CallIntMethod(inputStream,jm_read,buffer)){
        env->CallVoidMethod(result,jm_write,buffer,0,length);
    }
    jstring str=(jstring)env->CallObjectMethod(result,jm_toString,env->NewStringUTF("UTF-8"));
    env->CallVoidMethod(result,jm_close);
    if (env->CallBooleanMethod(str,jm_contains,env->NewStringUTF("190723101931")) && env->CallBooleanMethod(str,jm_contains,env->NewStringUTF("20690710101931"))){
        const char *chs=env->GetStringUTFChars(str,0);
        string cstr=string(chs);
        delete chs;
        if (cstr.find("190723101931",0)!=string::npos && cstr.find("20690710101931",0)!=string::npos){
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

//返回 /data/data/<包名>/files 的File对象
__attribute__((section (".helper"))) jobject getFilesDir(JNIEnv *env) {
    jclass jc_ContextWrapper = env->FindClass("android/content/ContextWrapper");
    jmethodID jm_ContextWrapperInit = env->GetMethodID(jc_ContextWrapper, "<init>",
                                                       "(Landroid/content/Context;)V");
    jobject jo_ContextWrapper = env->NewObject(jc_ContextWrapper, jm_ContextWrapperInit,
                                               getApplicationContext(env));
    jmethodID jm_getFilesDir = env->GetMethodID(jc_ContextWrapper, "getFilesDir",
                                                "()Ljava/io/File;");
    jobject jo_FilesDir = env->CallObjectMethod(jo_ContextWrapper, jm_getFilesDir);
    return jo_FilesDir;
}

__attribute__((section (".helper"))) JNIEXPORT jint JNICALL jniInitNative(JNIEnv *env, jclass type, jobject ctx) {
    if (ctx == NULL) {
        exit(0);
    }
    jclass jc_VERSION = env->FindClass("android/os/Build$VERSION");
    jfieldID jf_SDK_INT = env->GetStaticFieldID(jc_VERSION, "SDK_INT", "I");
    jint SDK_INT = env->GetStaticIntField(jc_VERSION, jf_SDK_INT);
    if (SDK_INT < 29) {
        checkHelper(env);
    }
    env->DeleteLocalRef(jc_VERSION);

    initNative = JNI_TRUE;
    return 1;
}

__attribute__((section (".helper"))) JNIEXPORT jint JNICALL
jniInitUser(JNIEnv *env, jclass type, jobject userData, jint level) {
    if (userData == NULL) {
        userLevel = -1;
        return 1;
    }
    userLevel = level;
    LOGD("user level %d", userLevel);
    return 0;
}


__attribute__((section (".helper"))) JNIEXPORT jobject JNICALL jniGetApplication(JNIEnv *env, jclass type) {
    return getApplicationContext(env);
}

__attribute__((section (".helper"))) JNIEXPORT jboolean JNICALL jniDevMode(JNIEnv *env, jclass type, jstring key_) {
    const char *key = env->GetStringUTFChars(key_, 0);
    const char *devKey = "Helper:DebugMode";
    if (strcmp(key, devKey) == 0) {
        userLevel = 100;
        return JNI_TRUE;
    } else {
        return JNI_FALSE;
    }
}

__attribute__((section (".helper"))) JNIEXPORT jbyteArray JNICALL
jniEncrypt___3B(JNIEnv *env, jclass type, jbyteArray bytes) {
    return encrypt(env, 99, bytes);
}

__attribute__((section (".helper"))) JNIEXPORT jbyteArray JNICALL
jniDecrypt___3B(JNIEnv *env, jclass type, jbyteArray bytes) {
    return decrypt(env, 99, bytes);
}

__attribute__((section (".helper"))) JNIEXPORT jbyteArray JNICALL
jniEncrypt__I_3B(JNIEnv *env, jclass clazz, jint key, jbyteArray bytes) {
    return encrypt(env, key, bytes);
}

__attribute__((section (".helper"))) JNIEXPORT jbyteArray JNICALL
jniDecrypt__I_3B(JNIEnv *env, jclass clazz, jint key, jbyteArray bytes) {
    return decrypt(env, key, bytes);
}

//退出APP
__attribute__((section (".helper"))) JNIEXPORT void JNICALL jniOnError(JNIEnv *env, jclass type) {
    jclass jc_System = env->FindClass("java/lang/System");
    jmethodID jm_exit = env->GetStaticMethodID(jc_System, "exit", "(I)V");
    env->CallStaticVoidMethod(jc_System, jm_exit, 0);
    exit(0);
    env->CallObjectMethod(type, env->GetMethodID(type, "", ""));
}

//显示Toast
__attribute__((section (".helper"))) JNIEXPORT void JNICALL jniToast(JNIEnv *env, jclass type, jstring str) {
    showToast(env, str);
}

__attribute__((section (".helper"))) JNIEXPORT void JNICALL
jniStartNetEaseMc(JNIEnv *env, jclass type, jstring packageName, jstring DwUrl, jstring jsArr,
                  jstring gameModArr, jstring uid, jint loadMode, jboolean showMuFloat,
                  jboolean showGameFloat, jstring skinPath, jstring texturesPath,
                  jboolean removeCacheMode, jstring hookChecks) {
    if (!initNative) {
        exit(0);
    }
    userLevel=1;
    LOGI("level %d", userLevel);

    jobject Context = getApplicationContext(env);
    jclass jc_Context = env->GetObjectClass(Context);
    jmethodID jm_Context_startActivity = env->GetMethodID(jc_Context, "startActivity",
                                                          "(Landroid/content/Intent;)V");
    jmethodID jm_Context_getPackageResourcePath=env->GetMethodID(jc_Context,"getPackageResourcePath","()Ljava/lang/String;");

    //Uri类
    jclass jc_Uri = env->FindClass("android/net/Uri");
    //parse方法
    jmethodID jm_Uri_parse = env->GetStaticMethodID(jc_Uri, "parse",
                                                    "(Ljava/lang/String;)Landroid/net/Uri;");


    jobject Uri = env->CallStaticObjectMethod(jc_Uri, jm_Uri_parse, DwUrl);
    //Intent类
    jclass jc_Intent = env->FindClass("android/content/Intent");
    //FLAG
    jfieldID jf_Intent_FLAG_ACTIVITY_NEW_TASK = env->GetStaticFieldID(jc_Intent,
                                                                      "FLAG_ACTIVITY_NEW_TASK",
                                                                      "I");
    jfieldID jf_Intent_FLAG_RECEIVER_FOREGROUND = env->GetStaticFieldID(jc_Intent,
                                                                        "FLAG_RECEIVER_FOREGROUND",
                                                                        "I");
    //Intent类初始化方法
    jmethodID jm_Intent_init = env->GetMethodID(jc_Intent, "<init>", "(Ljava/lang/String;)V");
    //
    jfieldID jf_Intent_Action = env->GetStaticFieldID(jc_Intent, "ACTION_VIEW",
                                                      "Ljava/lang/String;");
    jstring Action = (jstring) env->GetStaticObjectField(jc_Intent, jf_Intent_Action);
    //setClassName方法
    jmethodID jm_Intent_setClassName = env->GetMethodID(jc_Intent, "setClassName",
                                                        "(Ljava/lang/String;Ljava/lang/String;)Landroid/content/Intent;");
    //setData方法
    jmethodID jm_Intent_setData = env->GetMethodID(jc_Intent, "setData",
                                                   "(Landroid/net/Uri;)Landroid/content/Intent;");
    //putExtra方法（字符串）
    jmethodID jm_Intent_putExtraString = env->GetMethodID(jc_Intent, "putExtra",
                                                          "(Ljava/lang/String;Ljava/lang/String;)Landroid/content/Intent;");
    //putExtra方法（布尔值）
    jmethodID jm_Intent_putExtraBoolean = env->GetMethodID(jc_Intent, "putExtra",
                                                           "(Ljava/lang/String;Z)Landroid/content/Intent;");
    //putExtra方法（整数）
    jmethodID jm_Intent_putExtraInteger = env->GetMethodID(jc_Intent, "putExtra",
                                                           "(Ljava/lang/String;I)Landroid/content/Intent;");

    jmethodID jm_Intent_putStringArrayListExtra = env->GetMethodID(jc_Intent,
                                                                   "putStringArrayListExtra",
                                                                   "(Ljava/lang/String;Ljava/util/ArrayList;)Landroid/content/Intent;");
    //addFlags方法
    jmethodID jm_Intent_setFlags = env->GetMethodID(jc_Intent, "setFlags",
                                                    "(I)Landroid/content/Intent;");
    //addFlags方法
    jmethodID jm_Intent_addFlags = env->GetMethodID(jc_Intent, "addFlags",
                                                    "(I)Landroid/content/Intent;");

    jint FLAG_ACTIVITY_NEW_TASK = env->GetStaticIntField(jc_Intent,
                                                         jf_Intent_FLAG_ACTIVITY_NEW_TASK);
    jint FLAG_RECEIVER_FOREGROUND = env->GetStaticIntField(jc_Intent,
                                                           jf_Intent_FLAG_RECEIVER_FOREGROUND);

    jobject intent = env->NewObject(jc_Intent, jm_Intent_init, Action);
    const char *startActivity=stringcmp("com.netease.mc.baidu", jstringToCharArr(packageName))?"com.netease.ntunisdk.WelcomActivity":"com.mojang.minecraftpe.MainActivity";

    env->CallObjectMethod(intent, jm_Intent_setClassName, packageName,
                          env->NewStringUTF(startActivity));


    env->CallObjectMethod(intent, jm_Intent_setData, Uri);

    /*env->CallObjectMethod(intent, jm_Intent_putExtraString, env->NewStringUTF("packageName"),
                          packageName);
    env->CallObjectMethod(intent, jm_Intent_putExtraString, env->NewStringUTF("className"),
                          env->NewStringUTF(startActivity));*/

    env->CallObjectMethod(intent, jm_Intent_putExtraString, env->NewStringUTF("apkPath"),
                          env->CallObjectMethod(Context,jm_Context_getPackageResourcePath));

    if (userLevel >= 0) {
        env->CallObjectMethod(intent, jm_Intent_putExtraInteger, env->NewStringUTF("userLevel"),
                              userLevel);
        env->CallObjectMethod(intent, jm_Intent_putExtraString, env->NewStringUTF("pluginArray"),
                              jsArr);
        env->CallObjectMethod(intent, jm_Intent_putExtraString, env->NewStringUTF("pluginIdArray"),
                              gameModArr);
        env->CallObjectMethod(intent, jm_Intent_putExtraBoolean,
                              env->NewStringUTF("hymcfloatSupport"), JNI_TRUE);
        env->CallObjectMethod(intent, jm_Intent_putExtraString, env->NewStringUTF("userUid"), uid);
        env->CallObjectMethod(intent, jm_Intent_putExtraInteger, env->NewStringUTF("loadMode"),
                              loadMode);
        env->CallObjectMethod(intent, jm_Intent_putExtraBoolean, env->NewStringUTF("duoWanFloat"),
                              showMuFloat);
        if (userLevel >= 1) {
            env->CallObjectMethod(intent, jm_Intent_putExtraBoolean, env->NewStringUTF("gameFloat"),
                                  showGameFloat);
        } else {
            env->CallObjectMethod(intent, jm_Intent_putExtraBoolean, env->NewStringUTF("gameFloat"),
                                  JNI_FALSE);
        }
        env->CallObjectMethod(intent, jm_Intent_putExtraString, env->NewStringUTF("skinPath"),
                              skinPath);

        env->CallObjectMethod(intent, jm_Intent_putExtraString, env->NewStringUTF("textureArray"),
                              texturesPath);

        env->CallObjectMethod(intent, jm_Intent_putExtraBoolean,
                              env->NewStringUTF("removeCacheMode"), removeCacheMode);
    }
    env->CallObjectMethod(intent, jm_Intent_putExtraString, env->NewStringUTF("hookChecks"),
                          hookChecks);

    env->CallObjectMethod(intent, jm_Intent_setFlags, FLAG_ACTIVITY_NEW_TASK);
    env->CallObjectMethod(intent, jm_Intent_addFlags, FLAG_RECEIVER_FOREGROUND);


    env->CallVoidMethod(Context, jm_Context_startActivity, intent);

    env->DeleteLocalRef(intent);
    env->DeleteLocalRef(jc_Intent);
    env->DeleteLocalRef(jc_Uri);
    env->DeleteLocalRef(jc_Context);
}


__attribute__((section (".helper"))) static int registerNativeMethods(JNIEnv *env, const char *className, JNINativeMethod *gMethods,
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

__attribute__((section (".helper"))) static int registerNativeMethods(JNIEnv *env) {
    JNINativeMethod HelperNativeMethods[] = {{"initNative",     "(Landroid/content/Context;)I",                                                                                                                            (void *) jniInitNative},
                                             {"initUser",       "(Ljava/lang/Object;I)I",                                                                                                                                  (void *) jniInitUser},
                                             {"getApplication", "()Landroid/app/Application;",                                                                                                                             (void *) jniGetApplication},
                                             {"devMode",        "(Ljava/lang/String;)Z",                                                                                                                                   (void *) jniDevMode},
                                             {"encrypt",        "([B)[B",                                                                                                                                                  (void *) jniEncrypt___3B},
                                             {"decrypt",        "([B)[B",                                                                                                                                                  (void *) jniDecrypt___3B},
                                             {"encrypt",        "(I[B)[B",                                                                                                                                                 (void *) jniEncrypt__I_3B},
                                             {"decrypt",        "(I[B)[B",                                                                                                                                                 (void *) jniDecrypt__I_3B},
                                             {"onError",        "()V",                                                                                                                                                     (void *) jniOnError},
                                             {"Toast",          "(Ljava/lang/String;)V",                                                                                                                                   (void *) jniToast},
                                             {"startNetEaseMc", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;IZZLjava/lang/String;Ljava/lang/String;ZLjava/lang/String;)V", (void *) jniStartNetEaseMc},};

    if (!registerNativeMethods(env, "com/fmp/core/HelperNative", HelperNativeMethods,
                               sizeof(HelperNativeMethods) / sizeof(HelperNativeMethods[0]))) {
        return JNI_FALSE;
    }
    return JNI_TRUE;
}

//获取TracePid
__attribute__((section (".helper"))) int get_number_for_str(char *str) {
    if (str == NULL) {
        return -1;
    }
    char result[20];
    int count = 0;
    while (*str != '\0') {
        if (*str >= 48 && *str <= 57) {
            result[count] = *str;
            count++;
        }
        str++;
    }
    int val = atoi(result);
    return val;
}

//开启循环轮训检查TracePid字段
__attribute__((section (".helper"))) void *thread_function(void *argv) {
    int pid = getpid();
    char file_name[20] = {'\0'};
    sprintf(file_name, "/proc/%d/status", pid);
    char linestr[256];
    int i = 0, traceid;
    FILE *fp;
    while (1) {
        i = 0;
        fp = fopen(file_name, "r");
        if (fp == NULL) {
            break;
        }
        while (!feof(fp)) {
            fgets(linestr, 256, fp);
            if (i == 5) {
                traceid = get_number_for_str(linestr);
                //LOGD("traceId:%d", traceid);
                if (traceid!=pid && traceid > 1000) {
                    //LOGD("I was be traced...trace pid:%d", traceid);
                    //华为P9会主动给app附加一个进程，暂且认为小于1000的是系统的
                    exit(1);
                }
                break;
            }
            i++;
        }
        fclose(fp);
        sleep(5);
    }
    return ((void *) 0);
}

__attribute__((section (".helper"))) void create_thread_check_traceid() {
    pthread_t t_id;
    int err = pthread_create(&t_id, NULL, thread_function, NULL);
    if (err != 0) {
        LOGD("create thread fail: %s\n", strerror(err));
    }
}

void JNI_OnUnload(JavaVM *vm, void *reserved) {
    LOGD("FMP JNI unload...");
}


JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    LOGD("FMP JNI load...");
    //定义JNIEnv为NULL
    JNIEnv *env = NULL;
    //判断JavaVM获取的JNIEnv是否成功
    if (vm->_JavaVM::GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK)
        return JNI_ERR;
    mEnv = env;
    if (registerNativeMethods(env) != JNI_TRUE) {
        exit(0);
    }
    //检测自己有没有被trace
    create_thread_check_traceid();
    //方法验证
    if (!stringcmp("fmp", "fmp")) {
        env = NULL;
        AppPackageName = NULL;
        BmobAPPID = NULL;
        Signatures = NULL;
        LOGD("fmp cmp null.");
    }
    /*签名验证*/
    //Jni版
    jniVerification(env);
    //apk签名文件验证
    if (checkHelper(env)) {
        //Bmob初始化
        bmob_init(env);
    } else {
        env = NULL;
        AppPackageName = NULL;
        BmobAPPID = NULL;
        Signatures = NULL;
    }

    return JNI_VERSION_1_6;
}
