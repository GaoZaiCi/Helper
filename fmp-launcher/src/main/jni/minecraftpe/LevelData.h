//
// Created by Gao在此 on 2020/2/19.
//

#pragma once

#include "Minecraft.h"
#include "LevelSettings.h"
#include "Level.h"
#include "GeneratorType.h"
#include "BlockPos.h"
#include "Difficulty.h"
#include "CompoundTag.h"

class GameRules;

typedef int StorageVersion;

class Tick;

class LevelData
{
public:
    LevelData();
    LevelData(LevelSettings const&, std::string const&, GeneratorType, BlockPos const&, bool, bool, float, float);
    LevelData(LevelData&&);
    LevelData(CompoundTag const&);
    ~LevelData();
    GameType getGameType() const;
    bool achievementsWillBeDisabledOnLoad() const;
    long getSeed() const;
    bool isEduLevel() const;
    std::string getLevelName() const;
    float getRainLevel() const;
    GameType getForceGameType() const;
    float getLightningLevel() const;
    bool isTexturepacksRequired() const;
    void setRainTime(int);
    void setRainLevel(float);
    void setLightningTime(int);
    void setLightningLevel(float);
    void setSpawn(BlockPos const&);
    void setTime(int);
    Difficulty getGameDifficulty() const;
    bool hasCommandsEnabled() const;
    int getTime() const;
    void getFixedInventory();
    GeneratorType getGenerator() const;
    int getStopTime() const;
    BlockPos getWorldCenter() const;
    int getStorageVersion() const;
    void setGameType(GameType);
    void setStopTime(int);
    GameRules& getGameRules();
    void setLevelName(std::string const&);
    void incrementTick();
    void recordStartUp();
    void setLANBroadcast(bool);
    void setXBLBroadcast(bool);
    void setForceGameType(bool);
    void setGameDifficulty(Difficulty);
    void setStorageVersion(StorageVersion);
    void setCommandsEnabled(bool);
    void setMultiplayerGame(bool);
    void disableAchievements();
    void clearLoadedPlayerTag();
    void getAdventureSettings();
    void operator=(LevelData&&);
    bool getSpawnMobs() const;
    int getCurrentTick() const;
    bool hasLANBroadcast() const;
    bool hasXBLBroadcast() const;
    bool isMultiplayerGame() const;
    int getWorldStartCount() const;
    BlockPos getSpawn() const;
    void setTexturepacksRequired(bool);
    void hasAchievementsDisabled() const;
    CompoundTag& getLoadedPlayerTag();
    CompoundTag& getTagData(CompoundTag const&);
    //CompoundTag& v1_read(RakNet::BitStream&, StorageVersion);
    CompoundTag& createTag() const;
    int getLastPlayed() const;
    int getNetworkVersion() const;
    void setGenerator(GeneratorType);
    void setOfferName(std::string const&);
    void setSpawnMobs(bool);
    void setIsEduLevel(bool);
    void setCurrentTick(Tick&);
    void setNetworkVersion(int);
    void loadFixedInventoryData(CompoundTag&);
    void getLoadedFixedInventory();
    void setSeed(unsigned int);
    void setTagData(CompoundTag&) const;
    float getRainTime() const;
    bool isLightning() const;
    std::string getOfferName() const;
    float getLightningTime() const;
    bool isRaining() const;
};