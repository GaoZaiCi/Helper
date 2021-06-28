//
// Created by Gao在此 on 2020/2/27.
//

#pragma once
class Support{
public:
    static void sendBroadcastHelper(JNIEnv *env, jint type, jstring value);
    static void enterGame();
    static void leaveGame();
    static void toast(JNIEnv *env,const char *message);
    static void addEntity(JNIEnv *env,jlong ent);
    static void removeEntity(JNIEnv *env,jlong ent);
    static jlongArray getAllEntity(JNIEnv *env);
    static void clearAllEntity(JNIEnv *env);
};