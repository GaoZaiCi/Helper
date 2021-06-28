#pragma once

#include <map>
#include <string>
#include <memory>

#include "../util/DimensionId.h"
#include "GeneratorType.h"

class Weather;
class CompoundTag;
class BlockSource;
class BlockPos;
class LevelChunk;
class Player;
class FullBlock;
class CircuitSystem;
class CompoundTag;
class Packet;
class Level;
class Entity;
class Biome;
class ChunkSource;
typedef int EntityUniqueID;

class Dimension
{
public:
	//char filler_Dimension[UNKNOW_SIZE];
public:
	virtual ~Dimension();
	virtual void onBlockChanged(BlockSource&, BlockPos const&, FullBlock, FullBlock, int, Entity*);
	virtual void onBlockEvent(BlockSource&, int, int, int, int, int);
	virtual void onNewChunkFor(Player&, LevelChunk&);
	virtual void init();
	virtual void tick();
	virtual void tickRedstone();
	virtual void updateLightRamp();
	virtual bool isNaturalDimension() const;
	virtual bool isValidSpawn(int, int) const;
	virtual Color getFogColor(float) const;
	virtual float getFogDistanceScale() const;
	virtual bool isFoggyAt(int, int) const;
	virtual int getCloudHeight() const;
	virtual bool mayRespawn() const;
	virtual bool hasGround() const;
	virtual BlockPos getSpawnPos() const;
	virtual int getSpawnYPosition() const;
	virtual bool hasBedrockFog();
	virtual Color getClearColorScale();
	virtual std::string getName() const=0;
	virtual bool showSky() const;
	virtual bool isDay() const;
	virtual BlockPos translatePosAcrossDimension(Vec3 const&, DimensionId) const;
	virtual void load(CompoundTag const&);
	virtual void save(CompoundTag&);
	virtual void sendBroadcast(Packet const&, Player*);
	virtual long getTimeOfDay(int, float) const;
public:
	Dimension(Level&, DimensionId, short);
	long getTimeOfDay(float) const;
	void sendPacketForEntity(Entity const&, Packet const&, Player const*);
	ChunkSource& getChunkSource() const;
	BlockSource& getBlockSourceDEPRECATEDUSEPLAYERREGIONINSTEAD() const;
	std::map<EntityUniqueID,int> getEntityIdMap();
	DimensionId getId() const;
	Weather* getWeather() const;
	CircuitSystem* getCircuitSystem();
	bool hasCeiling() const;
	void removeWither(EntityUniqueID const&);
	void addWither(EntityUniqueID const&);
	float getSkyDarken() const;
	float getMoonBrightness() const;
	bool isUltraWarm() const;
	float getSunAngle(float);
	bool isRedstoneTick();
	void transferEntity(Vec3 const&, std::unique_ptr<CompoundTag, std::default_delete<CompoundTag> >);
	Level const& getLevelConst() const;
	float getBrightnessRamp() const;
	void getSeasons();
	void setCeiling(bool);
	Entity& fetchEntity(EntityUniqueID, bool);
	Color getSkyColor(BlockSource&, BlockPos const&, float);
	Color getSkyColor(Entity const&, float);
	void setUltraWarm(bool);
	Color getCloudColor(float);
	float getOldSkyDarken(float);
	float getSunIntensity(float, Entity const&, float);
	void _createGenerator(GeneratorType);
	float getStarBrightness(float);
	void setBrightnessRamp(unsigned int, float);
	Vec3 getSunlightDirection(float);
	void sendPacketForPosition(BlockPos const&, Packet const&, Player const*);
	void _completeEntityTransfer(BlockSource&, std::unique_ptr<Entity, std::default_delete<Entity> >);
	static std::unique_ptr<Dimension> createNew(DimensionId, Level&);
	std::map<Biome*,int> getBiomes();
	float getMoonPhase() const;
	Color getSkyDarken(float) const;
	Color getSunriseColor(float) const;
	std::map<EntityUniqueID,int> const getEntityIdMapConst() const;
	Level& getLevel() const;
	int getPopCap(int, bool) const;
public:
	static float MOON_BRIGHTNESS_PER_PHASE;
};
