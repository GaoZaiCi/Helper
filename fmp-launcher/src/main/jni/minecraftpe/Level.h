//
// Created by Gao在此 on 2020/2/17.
//
#pragma once

#include <memory>
#include <unordered_map>
#include "EntityUniqueID.h"
#include "LevelData.h"
#include "Actor.h"

class BlockSource;
class Spawner;
class HitResult;
class Abilities;
class Recipes;
class BlockPalette;

enum ParticleType {
};

class Level {
public:
    void **vtable; // 0
    char filler[44 - 4]; // 4
    //std::vector<Player *> allPlayers; // 44

    Actor *fetchEntity(EntityUniqueID, bool) const;
    void addEntity(BlockSource &, std::unique_ptr <Actor>);
    void addGlobalEntity(BlockSource &, std::unique_ptr <Actor>);
    void explode(BlockSource &, Actor *, Vec3 const &, float, bool, bool, float, bool);
    void setNightMode(bool);
    void setTime(int);
    int getTime() const;
    LevelData *getLevelData();
    void playSound(Vec3 const &, std::string const &, float, float);
    bool isClientSide() const;
    HitResult const &getHitResult();
    int getDifficulty() const;
    Spawner *getSpawner() const;
    Player *getPlayer(EntityUniqueID) const;
    Player *getPrimaryLocalPlayer() const;
    void addParticle(ParticleType, Vec3 const &, Vec3 const &, int, CompoundTag const *, bool);
    Abilities *getPlayerAbilities(EntityUniqueID const &);
    Recipes *getRecipes() const;
    BlockPalette *getGlobalBlockPalette() const;
};