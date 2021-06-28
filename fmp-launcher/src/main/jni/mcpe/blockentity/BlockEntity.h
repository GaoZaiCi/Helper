#pragma once

#include "BlockEntityType.h"
#include "../inventory/FillingContainer.h"

#include <unordered_map>

class Vec3;
class Packet;
class BlockPos;
class BlockSource;
class CompoundTag;
class AABB;
typedef int BlockEntityRendererId;

class BlockEntity
{
public:
	char filler[112];
public:
	BlockEntity(BlockEntityType, const BlockPos &, const std::string &);
	virtual ~BlockEntity();
	virtual void load(CompoundTag const&);
	virtual void save(CompoundTag&) const;
	virtual void saveItemInstanceData(CompoundTag&);
	virtual void saveBlockData(CompoundTag&, BlockSource&) const;
	virtual void loadBlockData(CompoundTag const&, BlockSource&);
	virtual void tick(BlockSource&);
	virtual bool isFinished();
	virtual void onChanged(BlockSource&);
	virtual bool isMovable();
	virtual bool isCustomNameSaved();
	virtual void getUpdatePacket(BlockSource&);
	virtual void onPlace(BlockSource&);
	virtual void onUpdatePacket(CompoundTag const&, BlockSource&);
	virtual void onMove();
	virtual void onRemoved(BlockSource&);
	virtual void triggerEvent(int, int);
	virtual void clearCache();
	virtual void onNeighborChanged(BlockSource&, BlockPos const&);
	virtual float getShadowRadius(BlockSource&) const;
	virtual bool hasAlphaLayer() const;
	virtual void getCrackEntity(BlockSource&, BlockPos const&);
	virtual std::string getDebugText(std::vector<std::string, std::allocator<std::string> >&);
	virtual std::string getCustomName() const;
	virtual std::string getName() const;
public:
	BlockPos const& getPosition() const;
	void setChanged();
	BlockEntityType getType() const;
	bool isType(BlockEntityType);
	BlockEntityRendererId getRendererId() const;
	AABB getAABB() const;
	bool isInWorld() const;
	int getData() const;
	bool canRenderCustomName() const;
	void distanceToSqr(Vec3 const&);
	std::string getDisplayName() const;
	Block const* getBlock();
	void _resetAABB();
	void loadStatic(CompoundTag const&);
	void setMovable(bool);
	void stopDestroy();
	void setRunningId(int);
	void setCustomName(std::string const&);
	void setRendererId(BlockEntityRendererId);
	void setClientSideOnly(bool);
	void _destructionWobble(float&, float&, float&);
	void setCustomNameSaved(bool);
	void setBB(AABB);
	void moveTo(BlockPos const&);
	void setData(int);
	int getRunningId() const;
	bool isClientSideOnly() const;
public:
	static bool isType(BlockEntity&, BlockEntityType);
	static void setId(BlockEntityType, std::string const&);
	static void initBlockEntities();
	static int _runningId;
	static std::unordered_map<BlockEntityType,std::string> classIdMap;
	static std::unordered_map<BlockEntityType,std::string> idClassMap;
};
