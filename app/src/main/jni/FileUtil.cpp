//
// Created by Gao在此 on 2019/8/17.
//

#include<jni.h>
#include "JavaFileUtil.h"

const char *FileClass = "java/io/File";

/*获取文件对象*/
jfile newFile(JNIEnv *env, jstring path) {
    jclass jc_File = env->FindClass(FileClass);
    jmethodID jm_File_Init = env->GetMethodID(jc_File, "<init>", "(Ljava/lang/String;)V");
    return env->NewObject(jc_File, jm_File_Init, path);
}

jfile newFile(JNIEnv *env, const char *path) {
    jclass jc_File = env->FindClass(FileClass);
    jmethodID jm_File_Init = env->GetMethodID(jc_File, "<init>", "(Ljava/lang/String;)V");
    return env->NewObject(jc_File, jm_File_Init, env->NewStringUTF(path));
}

jfile newFile(JNIEnv *env, jfile fileDir, jstring file) {
    jclass jc_File = env->FindClass(FileClass);
    jmethodID jm_File_Init = env->GetMethodID(jc_File, "<init>",
                                              "(Ljava/io/File;Ljava/lang/String;)V");
    return env->NewObject(jc_File, jm_File_Init, fileDir, file);
}

jfile newFile(JNIEnv *env, jfile fileDir, const char *file) {
    jclass jc_File = env->FindClass(FileClass);
    jmethodID jm_File_Init = env->GetMethodID(jc_File, "<init>",
                                              "(Ljava/io/File;Ljava/lang/String;)V");
    return env->NewObject(jc_File, jm_File_Init, fileDir, env->NewStringUTF(file));
}

jfile newFile(JNIEnv *env, jstring filePath, jstring file) {
    jclass jc_File = env->FindClass(FileClass);
    jmethodID jm_File_Init = env->GetMethodID(jc_File, "<init>",
                                              "(Ljava/lang/String;Ljava/lang/String;)V");
    return env->NewObject(jc_File, jm_File_Init, filePath, file);
}

jfile newFile(JNIEnv *env, const char *filePath, const char *file) {
    jclass jc_File = env->FindClass(FileClass);
    jmethodID jm_File_Init = env->GetMethodID(jc_File, "<init>",
                                              "(Ljava/lang/String;Ljava/lang/String;)V");
    return env->NewObject(jc_File, jm_File_Init, env->NewStringUTF(filePath),
                          env->NewStringUTF(file));
}

/*文件删除*/
jboolean fileDelete(JNIEnv *env, jfile file) {
    jclass jc_File = env->FindClass(FileClass);
    jmethodID jm_delete = env->GetMethodID(jc_File, "delete", "()Z");
    return env->CallBooleanMethod(file, jm_delete, file);
}

jboolean fileDelete(JNIEnv *env, jstring filePath) {
    jfile file = newFile(env, filePath);
    jclass jc_File = env->FindClass(FileClass);
    jmethodID jm_delete = env->GetMethodID(jc_File, "delete", "()Z");
    return env->CallBooleanMethod(file, jm_delete, file);
}

jboolean fileDelete(JNIEnv *env, const char *filePath) {
    jfile file = newFile(env, filePath);
    jclass jc_File = env->FindClass(FileClass);
    jmethodID jm_delete = env->GetMethodID(jc_File, "delete", "()Z");
    return env->CallBooleanMethod(file, jm_delete, file);
}

/*判断存在*/
jboolean fileExists(JNIEnv *env, jfile file) {
    jclass jc_File = env->FindClass(FileClass);
    jmethodID jm_exists = env->GetMethodID(jc_File, "exists", "()Z");
    return env->CallBooleanMethod(file, jm_exists);
}

jboolean fileExists(JNIEnv *env, jstring filePath) {
    jfile file = newFile(env, filePath);
    jclass jc_File = env->FindClass(FileClass);
    jmethodID jm_exists = env->GetMethodID(jc_File, "exists", "()Z");
    return env->CallBooleanMethod(file, jm_exists);
}

jboolean fileExists(JNIEnv *env, const char *filePath) {
    jfile file = newFile(env, filePath);
    jclass jc_File = env->FindClass(FileClass);
    jmethodID jm_exists = env->GetMethodID(jc_File, "exists", "()Z");
    return env->CallBooleanMethod(file, jm_exists);
}

/*获取名称*/
jstring fileGetName(JNIEnv *env, jfile file) {
    jclass jc_File = env->FindClass(FileClass);
    jmethodID jm_getName = env->GetMethodID(jc_File, "getName", "()Ljava/lang/String;");
    return (jstring) env->CallObjectMethod(file, jm_getName);
}

jstring fileGetName(JNIEnv *env, jstring filePath) {
    jfile file = newFile(env, filePath);
    jclass jc_File = env->FindClass(FileClass);
    jmethodID jm_getName = env->GetMethodID(jc_File, "getName", "()Ljava/lang/String;");
    return (jstring) env->CallObjectMethod(file, jm_getName);
}

jstring fileGetName(JNIEnv *env, const char *filePath) {
    jfile file = newFile(env, filePath);
    jclass jc_File = env->FindClass(FileClass);
    jmethodID jm_getName = env->GetMethodID(jc_File, "getName", "()Ljava/lang/String;");
    return (jstring) env->CallObjectMethod(file, jm_getName);
}
/*创建文件夹*/
jboolean folderMkdirs(JNIEnv *env, jfile folder) {
    jclass jc_File = env->FindClass(FileClass);
    jmethodID jm_mkdirs = env->GetMethodID(jc_File, "mkdirs", "()Z");
    return env->CallBooleanMethod(folder, jm_mkdirs);
}

jboolean folderMkdirs(JNIEnv *env, jstring folderPath) {
    jfile folder = newFile(env, folderPath);
    jclass jc_File = env->FindClass(FileClass);
    jmethodID jm_mkdirs = env->GetMethodID(jc_File, "mkdirs", "()Z");
    return env->CallBooleanMethod(folder, jm_mkdirs);
}

jboolean folderMkdirs(JNIEnv *env, const char *folderPath) {
    jfile folder = newFile(env, folderPath);
    jclass jc_File = env->FindClass(FileClass);
    jmethodID jm_mkdirs = env->GetMethodID(jc_File, "mkdirs", "()Z");
    return env->CallBooleanMethod(folder, jm_mkdirs);
}
/*获取绝对路径*/
jstring fileGetAbsolutePath(JNIEnv *env,jfile file){
    jclass jc_File = env->FindClass(FileClass);
    jmethodID jm_getAbsolutePath=env->GetMethodID(jc_File,"getAbsolutePath","()Ljava/lang/String;");
    return (jstring)env->CallObjectMethod(file,jm_getAbsolutePath);
}

jfile getExternalStorageDirectory(JNIEnv *env) {
    jclass jc_Environment = env->FindClass("android/os/Environment");
    jmethodID jm_getEXD = env->GetStaticMethodID(jc_Environment, "getExternalStorageDirectory",
                                                 "()Ljava/io/File;");
    return env->CallStaticObjectMethod(jc_Environment, jm_getEXD);
}

jfile getDownloadCacheDirectory(JNIEnv *env) {
    jclass jc_Environment = env->FindClass("android/os/Environment");
    jmethodID jm_getEXD = env->GetStaticMethodID(jc_Environment, "getDownloadCacheDirectory",
                                                 "()Ljava/io/File;");
    return env->CallStaticObjectMethod(jc_Environment, jm_getEXD);
}

jfile getHelperDirectory(JNIEnv *env) {
    jclass jc_File = env->FindClass("java/io/File");
    jmethodID jm_File_Init = env->GetMethodID(jc_File, "<init>",
                                              "(Ljava/io/File;Ljava/lang/String;)V");
    return env->NewObject(jc_File, jm_File_Init, getExternalStorageDirectory(env),
                          env->NewStringUTF("Android/data/net.fmp.helper/files"));
}

