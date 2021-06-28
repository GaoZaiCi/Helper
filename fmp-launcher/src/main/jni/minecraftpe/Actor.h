//
// Created by Gao在此 on 2020/2/17.
//
#pragma once

#include <memory>
#include <unordered_map>
#include "EntityUniqueID.h"
#include "Vec2.h"
#include "Vec3.h"
#include "Level.h"

class BlockSource;
class SynchedEntityData;
class ActorDamageSource;
class ItemStack;
class Attribute;
class ArmorSlot;
class AttributeInstance;
class MobEffectInstance;

enum ActorFlags {
    ActorFlagsImmobile = 16,
};

class Actor {
public:
    char filler2[176-4]; // 4
    float pitch; //176 Entity::setRot
    float yaw; //180
    float prevPitch; //184
    float prevYaw; //188

    char filler4[584-192]; //192
    //std::vector<Entity*> riders; // 584

    char filler3[780-596]; // 596
    float x; //780 - Entity::setPos(Vec3 const&) or Actor.getPos
    float y; //784
    float z; //788
    // Actor::lerpMotion
    char filler5[804-792];
    float motionX; // 804 - Actor::push or PushableComponent::push
    float motionY; // 808
    float motionZ; // 812

    virtual ~Actor();
    BlockSource* getRegion() const;
    void setRot(Vec2 const&);//设置视角
    EntityUniqueID const& getUniqueID() const;//获取当前ent
    void setNameTag(std::string const&);
    SynchedEntityData* getEntityData();
    EntityUniqueID const& getTargetId();
    void setTarget(Actor*);
    void setStatusFlag(ActorFlags, bool);
    Level* getLevel();//获取当前存档
    void hurt(ActorDamageSource const&, int, bool, bool);
    Actor* getRide() const;
    void setNameTagVisible(bool);
    void sendMotionPacketIfNeeded();
    void sendMotionToServer(bool);
    bool isAutoSendEnabled() const;
    void enableAutoSendPosRot(bool);
    void teleportTo(Vec3 const&, int, int);
    bool hasTeleported() const;
    ItemStack* getOffhandSlot() const;//获取副手物品
    void setOffhandSlot(ItemStack const&);//设置副手物品
    int getHealth() const;//获取生命值
    ItemStack* getArmor(ArmorSlot) const;
    AttributeInstance* getAttribute(Attribute const&) const;
    void setChanged();
    void addEffect(MobEffectInstance const&);//添加药水效果
    void removeEffect(int);//移除指定药水效果
    void removeAllEffects();//移除所有药水效果
    bool canFly();//是否可以飞
    void setCanFly(bool);//设置是否可以飞行
    void attack(Actor&);//攻击实体
};


