//
// Created by Gao在此 on 2020/2/17.
//

#include <jni.h>
#include <string>
#include <unistd.h>
#include <math.h>
#include <pthread.h>
#include "substrate/Substrate.h"
#include "android/android_log.h"
#include "fmp-launcher.h"
#include "netease-support.h"
#include "minecraftpe/minecraftpe.h"
#include "netease/MobileClient.h"
#include "netease/serverlevel.h"
#include "netease/serverModuleUtil.h"
#include "netease/ReactNativeMgr.h"
#include "netease/Manifest.h"

using namespace std;

static bool launcherEnable = true;
const char *hookWYChecks;
JavaVM *jvm;
void *handle;

pthread_t modTickThread;
static bool modTickEnable = true;
static bool attackThread = true;

ClientInstance *mcpeClientInstance;
MinecraftScreenModel *mcpeMinecraftScreenModel;
MinecraftGame *mcpeMinecraftGame;
ClientInstanceScreenModel *mcpeClientInstanceScreenModel;
Level *mcpeLevel;
GameMode *mcpeModTick;
Player *mcpePlayer;

EntityUniqueID attackEntity = 0;
static bool killingMode = false;
static bool clickKillMode = false;
static bool addItemToOffhand = true;
static bool carriedItemToOffhand = false;
static bool allItemAllowOffhand = false;
static bool isOperator = false;
static bool destroyModeTen = false;
static bool destroyModeClick = false;
static bool fastBuild = false;

__attribute__((section (".helper"))) JNIEnv *getJNIEnv() {
    if (jvm != nullptr) {
        JNIEnv *env;
        jvm->AttachCurrentThread(&env, NULL);
        if (env != nullptr) {
            return env;
        }
    }
    return nullptr;
}

bool (*old_Manifest_versionEquals)(Manifest *, std::string const &);

void (*old_rnJscallCpp)(ReactNativeMgr *, std::string const &);

void (*old_rnJscallPython)(ReactNativeMgr *, JNIEnv *, _jobject *, _jstring *, _jobject *);

void (*old_serverlevel_kick_player)(_serverlevel *, _jobject *);

void (*old_Minecraft_startLeaveGame)(Minecraft *, bool);

void (*old_MinecraftGame_startLocalServer)(MinecraftGame *, string, string, ContentIdentity &, LevelSettings, StartIntent, unsigned short, unsigned short);

void (*old_MinecraftGame_update)(MinecraftGame *);

void (*old_Actor_setCarriedItem)(Actor *, ItemStack &);

int (*old_Player_isOperator)(Player *);

bool (*old_Player_canUseOperatorBlocks)(Player *);

void (*old_SurvivalMode_attack)(SurvivalMode *, Actor &);

void (*old_GameMode_attack)(GameMode *, Actor &);

void (*old_GameMode_tick)(GameMode *);

void (*old_GameMode_interact)(GameMode *, Actor &, Vec3 const &);

void (*old_GameMode_useItemOn)(GameMode *, ItemStack &, BlockPos const &, unsigned char, Vec3 const &, Block const *);

void (*old_GameMode_buildBlock)(GameMode *, BlockPos const &, unsigned char);

bool (*old_Abilities_isOperator)(Abilities *);

bool (*old_MinecraftScreenModel_isOperator)(MinecraftScreenModel *);

CommandPermissionLevel (*old_MinecraftScreenModel_getPlayerCommandPermissionLevel)(MinecraftScreenModel *);

bool (*old_PlayerInventoryProxy_addItemStack)(PlayerInventoryProxy *, ItemStack &, bool);

bool (*old_Level_addEntity)(Level *, BlockSource &, std::unique_ptr<Actor>);

bool (*old_Level_addPlayer)(Level *, std::unique_ptr<Player>);

bool (*old_Level_removeEntity)(Level *, std::unique_ptr<Actor> &&, bool);

bool (*old_Item_allowOffhand)(Item *);

void (*old_Player_setCarriedItem)(Player *, ItemStack &);

void (*old_GameMode_destroyBlock)(GameMode *, const BlockPos, char);

bool (*old_ClientInstanceScreenModel_shouldDisplayPlayerPosition)(ClientInstanceScreenModel *);

__attribute__((section (".helper"))) bool hook_Manifest_versionEquals(Manifest *manifest, string version) {
    LOGI("Manifest versionEquals %s", version.c_str());
    return old_Manifest_versionEquals(manifest, version);
}

__attribute__((section (".helper"))) void hook_rnJscallCpp(ReactNativeMgr *reactNativeMgr, string cpp) {
    LOGI("callCpp ====> %s", cpp.c_str());
    if (cpp.find("cpp.testCrash", 0) != string::npos || cpp.find("cpp.quitapp", 0) != string::npos) {
        return;
    }
    old_rnJscallCpp(reactNativeMgr, cpp);
}

__attribute__((section (".helper"))) void hook_rnJscallPython(ReactNativeMgr *reactNativeMgr, _JNIEnv *env, _jobject *obj, _jstring *msg, _jobject *callBack) {
    const char *chs = env->GetStringUTFChars(msg, 0);
    string str = string(chs);
    LOGI("callPython ====> %s", chs);
#if 1 //反检测
    if (str.find("checkJsClass", 0) != string::npos) {
        jclass jc_RNCallPythonRetObj = env->GetObjectClass(callBack);
        jfieldID jf_mPromise = env->GetFieldID(jc_RNCallPythonRetObj, "mPromise", "Lcom/facebook/react/bridge/Promise;");
        jobject Promise = env->GetObjectField(callBack, jf_mPromise);
        jclass jc_Promise = env->GetObjectClass(Promise);
        jmethodID jm_resolve = env->GetMethodID(jc_Promise, "resolve", "(Ljava/lang/Object;)V");
        env->CallVoidMethod(Promise, jm_resolve, 0);
        env->DeleteLocalRef(jc_RNCallPythonRetObj);
        env->DeleteLocalRef(jc_Promise);
        return;
    }
#endif

#if 1
    //网络游戏和租赁服
    if (str.find("join_network_game", 0) != string::npos || str.find("join_rental_game", 0) != string::npos) {
        joinNetworkGame(msg);
    }
#endif
#if 0
    if (str.find("skin_change2D",0)!=string::npos){
        jstring json=replaceSkinPath(msg);
        old_rnJscallPython(reactNativeMgr, env, obj, json, callBack);
        return;
    }
#endif
    old_rnJscallPython(reactNativeMgr, env, obj, msg, callBack);
}

__attribute__((section (".helper"))) void hook_serverlevel_kick_player(_serverlevel *serverlevel, _jobject *obj) {
    LOGD("kick_player");
    old_serverlevel_kick_player(serverlevel, obj);
}

__attribute__((section (".helper"))) void hook_Minecraft_startLeaveGame(Minecraft *minecraft, bool localServer) {
    old_Minecraft_startLeaveGame(minecraft, localServer);
    LOGI("Minecraft startLeaveGame");
    //Support::clearAllEntity(getJNIEnv());
    Support::leaveGame();
}

__attribute__((section (".helper"))) void hook_MinecraftGame_startLocalServer(MinecraftGame *minecraftGame, string worldDir, string worldName, ContentIdentity &contentIdentity, LevelSettings levelSettings, StartIntent startIntent, unsigned short arg1, unsigned short arg2) {
    LOGI("MinecraftGame startLocalServer");
    /*const char *dir=worldDir.c_str();
    const char *name=worldName.c_str();
    LOGI("worldDir %s", dir);
    LOGI("worldName %s", name);*/
    mcpeMinecraftGame = minecraftGame;
    mcpeLevel = minecraftGame->getLocalServerLevel();
    old_MinecraftGame_startLocalServer(minecraftGame, worldDir, worldName, contentIdentity, levelSettings, startIntent, arg1, arg2);
    Support::enterGame();
}

__attribute__((section (".helper"))) void hook_MinecraftGame_update(MinecraftGame *minecraftGame) {
    mcpeMinecraftGame = minecraftGame;
    mcpeClientInstance = minecraftGame->getPrimaryClientInstance();
    old_MinecraftGame_update(minecraftGame);
}

__attribute__((section (".helper"))) void hook_Actor_setCarriedItem(Actor *actor, ItemStack &itemStack) {
    if (mcpePlayer && carriedItemToOffhand) {
        mcpePlayer->setOffhandSlot(itemStack);
        ((ServerPlayer *) mcpePlayer)->sendInventory(true);
        return;
    }
    old_Actor_setCarriedItem(actor, itemStack);
}

__attribute__((section (".helper"))) void hook_Player_setCarriedItem(Player *player, ItemStack &itemStack) {
    if (mcpePlayer && carriedItemToOffhand) {
        mcpePlayer->setOffhandSlot(itemStack);
        ((ServerPlayer *) mcpePlayer)->sendInventory(true);
        return;
    }
    old_Player_setCarriedItem(player, itemStack);
}

__attribute__((section (".helper"))) int hook_Player_isOperator(Player *player) {
    //hook返回值闪退？？？
    //LOGI("Player isOperator %d", isOperator);
    return isOperator;
}

__attribute__((section (".helper"))) bool hook_Player_canUseOperatorBlocks(Player *player) {
    if (isOperator) {
        return true;
    }
    return old_Player_canUseOperatorBlocks(player);
}

__attribute__((section (".helper"))) bool hook_Abilities_isOperator(Abilities *abilities) {
    if (isOperator) {
        return true;
    }
    //hook返回值闪退？？？
    return old_Abilities_isOperator(abilities);
}

__attribute__((section (".helper"))) bool hook_MinecraftScreenModel_isOperator(MinecraftScreenModel *model) {
    mcpeMinecraftScreenModel = model;
    if (isOperator) {
        return true;
    }
    bool b = old_MinecraftScreenModel_isOperator(model);
    return b;
}

__attribute__((section (".helper"))) CommandPermissionLevel hook_MinecraftScreenModel_getPlayerCommandPermissionLevel(MinecraftScreenModel *model) {
    mcpeMinecraftScreenModel = model;
    if (isOperator) {
        return CommandPermissionLevel::CommandPermissionLevelOperator;
    }
    CommandPermissionLevel level = old_MinecraftScreenModel_getPlayerCommandPermissionLevel(model);
    LOGI("PlayerCommandPermissionLevel %d", (int) level);
    return level;
}

__attribute__((section (".helper"))) void hook_GameMode_attack(GameMode *gameMode, Actor &actor) {
    attackEntity = actor.getUniqueID();
    /*LOGI("attack pos:%f %f %f",actor.x,actor.y,actor.z);
    LOGI("attack motion:%f %f %f",actor.motionX,actor.motionY,actor.motionZ);
    LOGI("attack yaw:%f pitch:%f prevPitch:%f prevYaw:%f",actor.yaw,actor.pitch,actor.prevPitch,actor.prevYaw);
    Vec3 vec3=actor.getAABBShapeComponent();
    BlockPos pos=BlockPos(vec3);
    LOGI("player pos:%d %d %d",pos.x,pos.y,pos.z);*/
    //Support::addEntity(getJNIEnv(),actor.getUniqueID());
    old_GameMode_attack(gameMode, actor);
    /*char buff[100];
    snprintf(buff, sizeof(buff), "GameModeAttack Health %d EntityTypeId %d UniqueID %lld", actor.getHealth(),actor.getEntityTypeId(), (long long) actor.getUniqueID());
    LOGI("%s", buff);*/
}

__attribute__((section (".helper"))) void hook_GameMode_tick(GameMode *gameMode) {
    mcpeModTick = gameMode;
    mcpeLevel = mcpeMinecraftGame->getLocalServerLevel();
    if (mcpeLevel) {
        mcpePlayer = mcpeLevel->getPrimaryLocalPlayer();
    }

#if 0
    if (launcherEnable && killingMode){
        JNIEnv *env=getJNIEnv();
        jlongArray array=Support::getAllEntity(env);
        jsize length=env->GetArrayLength(array);
        for(int i=0;length>i;i++){
            jlong *ent=env->GetLongArrayElements(array,0);
            LOGI("%lld",ent[i]);
            Actor *actor = mcpeLevel->fetchEntity(ent[i], false);//获取到这个玩家
            if (actor != nullptr) {
                float distance = sqrt((localPlayer->motionX - actor->motionX) * (localPlayer->motionX - actor->motionX) + (localPlayer->motionY - actor->motionY) * (localPlayer->motionY - actor->motionY));//距离
                if (localPlayer->getUniqueID() != actor->getUniqueID() && !actor->isRemoved() && actor->getHealth() > 0 && distance <= 10) {
                    gameMode->attack(*actor);
                }
            }
        }
    }
#endif
    if (launcherEnable && killingMode) {
        auto const &players = mcpeLevel->allPlayers;
        for (unsigned int i = 0; i < players.size(); i++) {
            Actor *actor = mcpeLevel->fetchEntity(players[i]->getUniqueID(), false);//获取到这个玩家
            if (actor != nullptr) {
                float distance = sqrt((mcpePlayer->motionX - actor->motionX) * (mcpePlayer->motionX - actor->motionX) + (mcpePlayer->motionY - actor->motionY) * (mcpePlayer->motionY - actor->motionY));//距离
                if (mcpePlayer->getUniqueID() != actor->getUniqueID() && !actor->isRemoved() && actor->getHealth() > 0 && distance <= 10) {
                    gameMode->attack(*actor);
                }
            }
        }
    }
    if (launcherEnable && attackEntity != 0 && clickKillMode) {
        Actor *actor = mcpeLevel->fetchEntity(attackEntity, false);//获取到这个实体
        if (actor != nullptr && !actor->isRemoved()) {
            //对于自动攻击的判断
            if (killingMode) {
                //判断是否为玩家
                if (actor->getEntityTypeId() != EntityType::PLAYER)
                    gameMode->attack(*actor);
            } else {
                gameMode->attack(*actor);
            }
        } else {
            attackEntity = 0;
        }
    }
    if (launcherEnable && destroyModeTen) {
        HitResult const &objectMouseOver = mcpeLevel->getHitResult();

        float x = mcpePlayer->motionX, y = mcpePlayer->motionY, z = mcpePlayer->motionZ;
        for (int i = 0; i < 10; i++) {
            gameMode->destroyBlock(BlockPos(y, z + 5 - i, x), objectMouseOver.side);
            gameMode->destroyBlock(BlockPos(y + 1, z + 5 - i, x), objectMouseOver.side);
            gameMode->destroyBlock(BlockPos(y - 1, z + 5 - i, x), objectMouseOver.side);
            gameMode->destroyBlock(BlockPos(y, z + 5 - i, x + 1), objectMouseOver.side);
            gameMode->destroyBlock(BlockPos(y, z + 5 - i, x - 1), objectMouseOver.side);
        }
    }
    if (launcherEnable && fastBuild) {
        ItemStack *itemStack = mcpePlayer->getCarriedItem();
        if (itemStack->getId() != 0 && itemStack->isBlock()) {
            HitResult const &objectMouseOver = mcpeLevel->getHitResult();
            float x = mcpePlayer->motionX, y = mcpePlayer->motionY, z = mcpePlayer->motionZ;
            gameMode->startBuildBlock(BlockPos(y, z - 1, x), objectMouseOver.side);
            //gameMode->stopBuildBlock();
        }
    }
    old_GameMode_tick(gameMode);
}

__attribute__((section (".helper"))) void hook_GameMode_interact(GameMode *gameMode, Actor &actor, Vec3 &vec3) {
    LOGI("GameMode interact ent:%lld x:%d y:%d z:%d", actor.getUniqueID(), vec3.x, vec3.y, vec3.z);
    old_GameMode_interact(gameMode, actor, vec3);
}

__attribute__((section (".helper"))) void hook_GameMode_useItemOn(GameMode *gameMode, ItemStack &itemStack, BlockPos &blockPos, unsigned char side, Vec3 &vec3, Block const *block) {
    //LOGI("GameMode useItemOn id:%d x:%d y:%d z:%d side:%d",itemStack.getId(),blockPos.x,blockPos.y,blockPos.z,(int)side);
    old_GameMode_useItemOn(gameMode, itemStack, blockPos, side, vec3, block);
    if (launcherEnable && destroyModeClick) {
        int x = blockPos.x, y = blockPos.y, z = blockPos.z;
        for (int i = 0; i < 10; i++) {
            gameMode->destroyBlock(BlockPos(x, y + 5 - i, z), side);
            gameMode->destroyBlock(BlockPos(x + 1, y + 5 - i, z), side);
            gameMode->destroyBlock(BlockPos(x - 1, y + 5 - i, z), side);
            gameMode->destroyBlock(BlockPos(x, y + 5 - i, z + 1), side);
            gameMode->destroyBlock(BlockPos(x, y + 5 - i, z - 1), side);
        }
    }
}

__attribute__((section (".helper"))) void hook_GameMode_buildBlock(GameMode *gameMode, BlockPos const &blockPos, unsigned char side) {
    //LOGI("GameMode buildBlock pos:%d %d %d side:%d",blockPos.x,blockPos.y,blockPos.z,(int)side);
    old_GameMode_buildBlock(gameMode, blockPos, side);
}

__attribute__((section (".helper"))) bool hook_PlayerInventoryProxy_addItemStack(PlayerInventoryProxy *proxy, ItemStack &itemStack, bool b) {
    if (addItemToOffhand) {
        mcpePlayer->setOffhandSlot(itemStack);
        /*localPlayer->setArmor((ArmorSlot)0,itemStack);
        localPlayer->setArmor((ArmorSlot)1,itemStack);
        localPlayer->setArmor((ArmorSlot)2,itemStack);
        localPlayer->setArmor((ArmorSlot)3,itemStack);*/
    } else {
        old_PlayerInventoryProxy_addItemStack(proxy, itemStack, b);
    }
    ((ServerPlayer *) mcpePlayer)->sendInventory(true);
    return true;
}

__attribute__((section (".helper"))) bool hook_Level_addEntity(Level *level, BlockSource &blockSource, std::unique_ptr<Actor> entityPtr) {
    EntityUniqueID entityId = entityPtr->getUniqueID();
    LOGI("Level addEntity %lld", entityId);
    Support::addEntity(getJNIEnv(), entityId);
    bool value = old_Level_addEntity(level, blockSource, std::move(entityPtr));
    return value;
}

__attribute__((section (".helper"))) bool hook_Level_addPlayer(Level *level, std::unique_ptr<Player> entity) {
    EntityUniqueID entityId = entity->getUniqueID();
    LOGI("Level addPlayer %lld", entityId);
    Support::addEntity(getJNIEnv(), entityId);
    bool value = old_Level_addPlayer(level, std::move(entity));
    return value;
}

__attribute__((section (".helper"))) bool hook_Level_removeEntity(Level *level, std::unique_ptr<Actor> &&entity, bool arg2) {
    EntityUniqueID entityId = entity->getUniqueID();
    LOGI("Level removeEntity %lld", entityId);
    Support::removeEntity(getJNIEnv(), entityId);
    bool value = old_Level_removeEntity(level, std::move(entity), arg2);
    return value;
}

__attribute__((section (".helper"))) bool hook_Item_allowOffhand(Item *item) {
    if (launcherEnable && allItemAllowOffhand) {
        return true;
    }
    return old_Item_allowOffhand(item);
}

__attribute__((section (".helper"))) void hook_GameMode_destroyBlock(GameMode *gameMode, BlockPos blockPos, char side) {
    /*int x=blockPos.x,y=blockPos.y,z=blockPos.z;
    LOGI("destroyBlock:%d %d %d",x,y,z);*/
    old_GameMode_destroyBlock(gameMode, blockPos, side);
}

__attribute__((section (".helper"))) bool hook_ClientInstanceScreenModel_shouldDisplayPlayerPosition(ClientInstanceScreenModel *model) {
    mcpeClientInstanceScreenModel = model;
    bool isShow = old_ClientInstanceScreenModel_shouldDisplayPlayerPosition(model);
    //LOGI("shouldDisplayPlayerPosition %d",isShow);
    Vec3 vec3 = model->getPlayerPosition();
    LOGI("player pos:%f %f %f", vec3.x, vec3.y, vec3.z);
    BlockPos blockPos = BlockPos(vec3);
    LOGI("player block pos:%d %d %d", blockPos.x, blockPos.y, blockPos.z);
    return isShow;
}

void Launcher::setLauncherEnable(bool valve) {
    launcherEnable = valve;
}

bool Launcher::getLauncherEnable() {
    return launcherEnable;
}

void Launcher::setHookChecks(const char *hookChecks) {
    hookWYChecks = hookChecks;
}

void Launcher::setAddItemToOffhand(bool value) {
    addItemToOffhand = value;
}

void Launcher::setCarriedItemToOffhand(bool value) {
    carriedItemToOffhand = true;
}

void Launcher::setAllItemAllowOffhand(bool value) {
    allItemAllowOffhand = value;
    /*int maxId=ItemRegistry::getMaxItemID();
    if (maxId==0){
        Support::toast(getJNIEnv(),"请进入游戏后使用");
    } else{
        for (int i = 1; i < maxId; i++) {
            Item* item=ItemRegistry::getItem((short)i).get();
            if (item){
                item->setAllowOffhand(value);
            }
        }
    }*/
}

void Launcher::setKillingMode(bool value) {
    killingMode = value;
}

void Launcher::setClickKillMode(bool value) {
    clickKillMode = value;
}

void Launcher::PlayerSetOperator(bool value) {
    isOperator = value;
    if (mcpePlayer) {
        if (value) {
            mcpePlayer->setPermissions(CommandPermissionLevelOperator);
        } else {
            mcpePlayer->setPermissions(CommandPermissionLevelDefault);
        }
    }
}

jboolean Launcher::PlayerAttack(jlong ent) {
    if (mcpeModTick != nullptr && mcpeLevel != nullptr && attackThread) {
        Actor *actor = mcpeLevel->fetchEntity(ent, false);//获取到这个玩家
        if (actor) {
            mcpeModTick->attack(*actor);
            attackThread = false;
            return JNI_TRUE;
        } else {
            return JNI_FALSE;
        }
    } else {
        return JNI_FALSE;
    }
}

void Launcher::PlayerAttacks(JNIEnv *env, jlongArray ents) {
    if (mcpeModTick != nullptr && mcpeLevel != nullptr && attackThread) {
        int length = env->GetArrayLength(ents);
        for (int i = 0; length > i; i++) {
            jlong ent = (jlong) env->GetObjectArrayElement((jobjectArray) ents, i);
            Actor *actor = mcpeLevel->fetchEntity(ent, false);//获取到这个玩家
            if (actor != nullptr) {
                mcpeModTick->attack(*actor);
                attackThread = false;
            }
        }
    }
}

jlongArray Launcher::LevelGetAllPlayer(JNIEnv *env) {
    if (mcpeLevel == nullptr) {
        return env->NewLongArray(0);
    }
    auto const &players = mcpeLevel->allPlayers;
    jlong ids[players.size()];
    for (unsigned int i = 0; i < players.size(); i++) {
        ids[i] = (long long) players[i]->getUniqueID();
    }
    jlongArray ret = env->NewLongArray(players.size());
    env->SetLongArrayRegion(ret, 0, players.size(), ids);
    return ret;
}

void Launcher::ItemSetAllowOffhand(short id, bool value) {
    Item *item = ItemRegistry::getItem(id).get();
    if (item) {
        item->setAllowOffhand(value);
    }
}

void Launcher::setDestroyModeTen(bool value) {
    destroyModeTen = value;
}

void Launcher::setDestroyModeClick(bool value) {
    destroyModeClick = value;
}

void Launcher::setFastBuild(bool value) {
    fastBuild = value;
}

__attribute__((section (".helper"))) void joinNetworkGame(jstring json) {
    JNIEnv *env = getJNIEnv();
    jclass jc_JSONObject = env->FindClass("org/json/JSONObject");
    jmethodID jm_JSONObject_init = env->GetMethodID(jc_JSONObject, "<init>", "(Ljava/lang/String;)V");
    jmethodID jm_JSONObject_getString = env->GetMethodID(jc_JSONObject, "getString", "(Ljava/lang/String;)Ljava/lang/String;");
    jmethodID jm_JSONObject_getJSONArray = env->GetMethodID(jc_JSONObject, "getJSONArray", "(Ljava/lang/String;)Lorg/json/JSONArray;");

    jobject object = env->NewObject(jc_JSONObject, jm_JSONObject_init, json);

    jclass jc_JSONArray = env->FindClass("org/json/JSONArray");
    jmethodID jm_JSONArray_getString = env->GetMethodID(jc_JSONArray, "getString", "(I)Ljava/lang/String;");
    jmethodID jm_JSONArray_getInt = env->GetMethodID(jc_JSONArray, "getInt", "(I)I");

    jobject array = env->CallObjectMethod(object, jm_JSONObject_getJSONArray, env->NewStringUTF("args"));
    jstring ip = (jstring) env->CallObjectMethod(array, jm_JSONArray_getString, 0);
    jint port = env->CallIntMethod(array, jm_JSONArray_getInt, 1);
    jstring id = (jstring) env->CallObjectMethod(array, jm_JSONArray_getString, 2);
    jstring serverName = (jstring) env->CallObjectMethod(array, jm_JSONArray_getString, 3);

    char buff[200];
    snprintf(buff, sizeof(buff), "正在加入 %s 服务器IP %s 端口 %d ID %s", env->GetStringUTFChars(serverName, 0), env->GetStringUTFChars(ip, 0), port, env->GetStringUTFChars(id, 0));
    Support::toast(env, buff);

    env->DeleteLocalRef(jc_JSONObject);
    env->DeleteLocalRef(jc_JSONArray);
    env->DeleteLocalRef(object);
    env->DeleteLocalRef(array);
    env->DeleteLocalRef(ip);
    env->DeleteLocalRef(id);
    env->DeleteLocalRef(serverName);
}

__attribute__((section (".helper"))) jstring replaceSkinPath(jstring json) {
    JNIEnv *env = getJNIEnv();
    jclass jc_JSONObject = env->FindClass("org/json/JSONObject");
    jmethodID jm_JSONObject_init = env->GetMethodID(jc_JSONObject, "<init>", "(Ljava/lang/String;)V");
    jmethodID jm_JSONObject_getString = env->GetMethodID(jc_JSONObject, "getString", "(Ljava/lang/String;)Ljava/lang/String;");
    jmethodID jm_JSONObject_getJSONArray = env->GetMethodID(jc_JSONObject, "getJSONArray", "(Ljava/lang/String;)Lorg/json/JSONArray;");
    jmethodID jm_JSONObject_toString = env->GetMethodID(jc_JSONObject, "toString", "()Ljava/lang/String;");

    jobject object = env->NewObject(jc_JSONObject, jm_JSONObject_init, json);

    jclass jc_JSONArray = env->FindClass("org/json/JSONArray");
    jmethodID jm_JSONArray_getString = env->GetMethodID(jc_JSONArray, "getString", "(I)Ljava/lang/String;");
    jmethodID jm_JSONArray_putObject = env->GetMethodID(jc_JSONArray, "put", "(ILjava/lang/Object;)Lorg/json/JSONArray;");

    jobject array = env->CallObjectMethod(object, jm_JSONObject_getJSONArray, env->NewStringUTF("args"));
    env->CallObjectMethod(array, jm_JSONArray_putObject, 2, env->NewStringUTF("/storage/emulated/0/Android_GaoZaiCi/网易我的世界/皮肤/6eafea30bf4ff3cc.png"));

    env->DeleteLocalRef(jc_JSONObject);
    env->DeleteLocalRef(jc_JSONArray);
    return (jstring) env->CallObjectMethod(object, jm_JSONObject_toString);
}

__attribute__((section (".helper"))) void initMsHook(JavaVM *vm) {
    LOGD("init MSHook.");
    jvm = vm;
    handle = dlopen("libminecraftpe.so", RTLD_LAZY);
    if (handle) {
        LOGI("hook libminecraftpe.so %p", handle);

        MSHookFunction(dlsym(handle, "_ZNK8Manifest13versionEqualsERKSs"), (void *) &hook_Manifest_versionEquals, (void **) &old_Manifest_versionEquals);

        MSHookFunction(dlsym(handle, "_ZN14ReactNativeMgr11rnJscallCppERKSs"), (void *) &hook_rnJscallCpp, (void **) &old_rnJscallCpp);
        MSHookFunction(dlsym(handle, "_ZN14ReactNativeMgr14rnJscallPythonEP7_JNIEnvP8_jobjectP8_jstringS3_"), (void *) &hook_rnJscallPython, (void **) &old_rnJscallPython);
        MSHookFunction(dlsym(handle, "_ZN12_serverlevel11kick_playerEP7_object"), (void *) &hook_serverlevel_kick_player, (void **) &old_serverlevel_kick_player);

        MSHookFunction(dlsym(handle, "_ZN9Minecraft14startLeaveGameEb"), (void *) &hook_Minecraft_startLeaveGame, (void **) &old_Minecraft_startLeaveGame);

        MSHookFunction(dlsym(handle, "_ZN13MinecraftGame16startLocalServerERKSsS1_RK15ContentIdentity13LevelSettings11StartIntenttt"), (void *) &hook_MinecraftGame_startLocalServer, (void **) &old_MinecraftGame_startLocalServer);
        MSHookFunction(dlsym(handle, "_ZN13MinecraftGame6updateEv"), (void *) &hook_MinecraftGame_update, (void **) &old_MinecraftGame_update);

        MSHookFunction(dlsym(handle, "_ZN5Actor14setCarriedItemERK9ItemStack"), (void *) &hook_Actor_setCarriedItem, (void **) &old_Actor_setCarriedItem);
#if 0
        //崩溃
        MSHookFunction(dlsym(handle, "_ZNK6Player10isOperatorEv"), (void *) &hook_Player_isOperator,(void **) &old_Player_isOperator);
#endif
        MSHookFunction(dlsym(handle, "_ZNK6Player20canUseOperatorBlocksEv"), (void *) &hook_Player_canUseOperatorBlocks, (void **) &old_Player_canUseOperatorBlocks);
        MSHookFunction(dlsym(handle, "_ZN6Player14setCarriedItemERK9ItemStack"), (void *) &hook_Player_setCarriedItem, (void **) &old_Player_setCarriedItem);
#if 0
        //崩溃
        MSHookFunction(dlsym(handle, "_ZNK9Abilities10isOperatorEv"),(void *) &hook_Abilities_isOperator, (void **) &old_Abilities_isOperator);
#endif
        MSHookFunction(dlsym(handle, "_ZNK20MinecraftScreenModel10isOperatorEv"), (void *) &hook_MinecraftScreenModel_isOperator, (void **) &old_MinecraftScreenModel_isOperator);
        MSHookFunction(dlsym(handle, "_ZNK20MinecraftScreenModel31getPlayerCommandPermissionLevelEv"), (void *) &hook_MinecraftScreenModel_getPlayerCommandPermissionLevel, (void **) &old_MinecraftScreenModel_getPlayerCommandPermissionLevel);

        MSHookFunction(dlsym(handle, "_ZN8GameMode6attackER5Actor"), (void *) &hook_GameMode_attack, (void **) &old_GameMode_attack);
        MSHookFunction(dlsym(handle, "_ZN8GameMode4tickEv"), (void *) &hook_GameMode_tick, (void **) &old_GameMode_tick);
        MSHookFunction(dlsym(handle, "_ZN8GameMode8interactER5ActorRK4Vec3"), (void *) &hook_GameMode_interact, (void **) &old_GameMode_interact);
        MSHookFunction(dlsym(handle, "_ZN8GameMode9useItemOnER9ItemStackRK8BlockPoshRK4Vec3PK5Block"), (void *) &hook_GameMode_useItemOn, (void **) &old_GameMode_useItemOn);
        MSHookFunction(dlsym(handle, "_ZN8GameMode10buildBlockERK8BlockPosh"), (void *) &hook_GameMode_buildBlock, (void **) &old_GameMode_buildBlock);
        MSHookFunction(dlsym(handle, "_ZN8GameMode12destroyBlockERK8BlockPosh"), (void *) &hook_GameMode_destroyBlock, (void **) &old_GameMode_destroyBlock);

        MSHookFunction(dlsym(handle, "_ZN20PlayerInventoryProxy3addER9ItemStackb"), (void *) &hook_PlayerInventoryProxy_addItemStack, (void **) &old_PlayerInventoryProxy_addItemStack);
#if 0
        MSHookFunction(dlsym(handle, "_ZN5Level9addEntityER11BlockSourceSt10unique_ptrI5ActorSt14default_deleteIS3_EE"), (void *) &hook_Level_addEntity, (void **) &old_Level_addEntity);
        MSHookFunction(dlsym(handle, "_ZN5Level9addPlayerESt10unique_ptrI6PlayerSt14default_deleteIS1_EE"), (void *) &hook_Level_addPlayer, (void **) &old_Level_addPlayer);
        MSHookFunction(dlsym(handle, "_ZN5Level18queueEntityRemovalEOSt10unique_ptrI5ActorSt14default_deleteIS1_EEb"), (void *) &hook_Level_removeEntity, (void **) &old_Level_removeEntity);
#endif

#if 1
        void *p = dlsym(handle, "_ZNK4Item12allowOffhandEv");
        MSHookFunction((void *) ((long) p + 8), (void *) &hook_Item_allowOffhand, (void **) &old_Item_allowOffhand);
#endif

        //MSHookFunction(dlsym(handle, "_ZNK25ClientInstanceScreenModel27shouldDisplayPlayerPositionEv"), (void *) &hook_ClientInstanceScreenModel_shouldDisplayPlayerPosition, (void **) &old_ClientInstanceScreenModel_shouldDisplayPlayerPosition);



    } else {
        LOGE("hook err %s", dlerror());
    }
}

