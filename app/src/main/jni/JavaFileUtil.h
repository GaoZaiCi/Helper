//
// Created by Gao在此 on 2020/3/28.
//
#pragma once
typedef jobject jfile;

#ifndef HELPER_JAVAFILEUTIL_H
#define HELPER_JAVAFILEUTIL_H

jfile newFile(JNIEnv *env, jstring path);

jfile newFile(JNIEnv *env, const char *path);

jfile newFile(JNIEnv *env, jfile fileDir, jstring file);

jfile newFile(JNIEnv *env, jfile fileDir, const char *file);

jfile newFile(JNIEnv *env, jstring filePath, jstring file);

jfile newFile(JNIEnv *env, const char *filePath, const char *file);

jboolean fileDelete(JNIEnv *env, jfile file);

jboolean fileDelete(JNIEnv *env, jstring filePath);

jboolean fileDelete(JNIEnv *env, const char *filePath);

jboolean fileExists(JNIEnv *env, jfile file);

jboolean fileExists(JNIEnv *env, jstring filePath);

jboolean fileExists(JNIEnv *env, const char *filePath);

jstring fileGetName(JNIEnv *env, jfile file);

jstring fileGetName(JNIEnv *env, jstring filePath);

jstring fileGetName(JNIEnv *env, const char *filePath);

jboolean folderMkdirs(JNIEnv *env, jfile folder);

jboolean folderMkdirs(JNIEnv *env, jstring folderPath);

jboolean folderMkdirs(JNIEnv *env, const char *folderPath);

jstring fileGetAbsolutePath(JNIEnv *env,jfile file);

jfile getExternalStorageDirectory(JNIEnv *env);

jfile getDownloadCacheDirectory(JNIEnv *env);

jfile getHelperDirectory(JNIEnv *env);

#endif //HELPER_JAVAFILEUTIL_H
