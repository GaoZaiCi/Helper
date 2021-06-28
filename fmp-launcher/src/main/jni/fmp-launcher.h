//
// Created by Gao在此 on 2020/2/27.
//

#pragma once
class Launcher{
public:
    static void setLauncherEnable(bool valve);
    static bool getLauncherEnable();
    static void setHookChecks(const char* hookChecks);
    static void setKillingMode(bool value);//自动攻击模式开关
    static void setClickKillMode(bool value);//点击攻击模式开关
    static void setAddItemToOffhand(bool value);//设置添加物品到副手
    static void setCarriedItemToOffhand(bool value);//设置手持物品到副手
    static void setAllItemAllowOffhand(bool value);//设置所有物品可放副手
    static void PlayerSetOperator(bool value);//设置玩家操作员权限
    static jboolean PlayerAttack(jlong ent);//攻击指定实体
    static void PlayerAttacks(JNIEnv *env,jlongArray ents);//攻击多个指定实体
    static jlongArray LevelGetAllPlayer(JNIEnv *env);//获取全部玩家
    static void ItemSetAllowOffhand(short id,bool value);//设置指定物品是否可以放在副手
    static void setDestroyModeTen(bool value);//十字破坏
    static void setDestroyModeClick(bool value);//点击破坏
    static void setFastBuild(bool value);//自动搭路
};


__attribute__((section (".helper"))) void joinNetworkGame(jstring json);
__attribute__((section (".helper"))) jstring replaceSkinPath(jstring json);
