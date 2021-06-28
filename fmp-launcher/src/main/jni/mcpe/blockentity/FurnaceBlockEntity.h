#pragma once

#include <string>
#include "BlockEntity.h"

class ItemInstance;
class CompoundTag;
class BlockSource;
class BlockPos;
class Player;

class FurnaceBlockEntity : public BlockEntity
{
public:
	virtual ~FurnaceBlockEntity();
	virtual void load(CompoundTag const&);
	virtual void save(CompoundTag&);
	virtual void tick(BlockSource&);
	virtual bool isFinished();
	virtual void onMove();
	virtual void onNeighborChanged(BlockSource&, BlockPos const&);
	virtual void getItem(int) const;
	virtual void setItem(int, ItemInstance const*);
	virtual void removeItem(int, int);
	virtual std::string getName() const;
	virtual void getMaxStackSize() const;
	virtual void getContainerSize() const;
	virtual void startOpen(Player&);
	virtual void stopOpen(Player&);
	virtual void canPushInItem(BlockSource&, int, int, ItemInstance*);
	virtual void canPullOutItem(BlockSource&, int, int, ItemInstance*);
public:
	FurnaceBlockEntity(BlockPos const&);
	void setLitTime(int);
	void setTickCount(int);
	int getLitProgress(int);
	void setLitDuration(int);
	int getBurnProgress(int);
	void getLastFuelSource();
	int getLitTime() const;
	int getTickCount() const;
	float getLitDuration() const;
	bool isSlotDirty(int);
	bool isSlotEmpty(int);
	void resetSlotsDirty();
	void resetBurnProgress();
	void _getPositionOfNeighbor(int);
	void checkForSmeltEverythingAchievement(BlockSource&);
	void burn();
	bool isLit();
	bool canBurn();
public:
	static bool isFuel(ItemInstance const*);
	static bool isIngredient(ItemInstance const*);
	static int getBurnDuration(ItemInstance const*);
};
