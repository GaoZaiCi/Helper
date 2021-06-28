#pragma once

#include "LevelListener.h"
#include "BlockSourceListener.h"

class BlockSource;
class BlockPos;
class LevelEvent;
class Dimension;
class Vec3;

class Weather : public BlockSourceListener , public LevelListener
{
public:
	//char filler_Weather[UNKNOW_SIZE];
public:
	virtual ~Weather();
	virtual void levelEvent(LevelEvent, Vec3 const&, int);
public:
	Weather(Dimension&);
	void setRainLevel(float);
	float calcRainDuration();
	float calcRainCycleTime();
	void setLightningLevel(float);
	float calcLightningCycleTime();
	void stop();
	bool isRaining() const;
	bool isRainingAt(BlockSource&, BlockPos const&) const;
	void setSkyFlashTime(int);
	bool isLightning() const;
	void calcSnowBlockDepth(BlockSource&, BlockPos const&, int);
	bool isSnowingAt(BlockSource&, BlockPos const&) const;
	void tryToPlaceTopSnow(BlockSource&, BlockPos const&, bool, bool);
	float getRainLevel(float) const;
	float getLightningLevel(float) const;
	void tick();
	float getFogLevel() const;
	float getSkyFlashTime() const;
	bool canPlaceTopSnow(BlockSource&, BlockPos const&, bool, bool, int*);
	void rebuildTopSnowToDepth(BlockSource&, BlockPos const&, int);
	void serverTick();
	void setFogLevel(float);
	void _prepareWeather();
};
