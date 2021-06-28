#pragma once

#include <vector>
#include <memory>

#include "../../util/DimensionId.h"
#include "../../util/ChunkBlockPos.h"
#include "../../level/biome/Biome.h"

class Dimension;
class IDataOutput;
class Entity;
class BlockPos;
class ChunkSource;
class ChunkPos;
class BlockSource;
class Tick;
class ChunkBlockPos;
class EntityLink;
class LightLayer;
class Brightness;
class ChunkState;
class NibblePair;
class IDataInput;
class BlockEntity;
class ChunkTerrainDataState;
class KeyValueInput;
template <typename Type>
class buffer_span;

class LevelChunk
{
public:
	//char filler_LevelChunk[UNKNOW_SIZE];
public:
	enum class Finalization : int
	{
		
	};
public:
	LevelChunk(Level&, Dimension&, ChunkPos const&, bool);
	~LevelChunk();
	ChunkPos const& getPosition() const;
	DimensionId getDimensionId() const;
	std::vector<Entity*, std::allocator<Entity*> >& getEntities() const;
	void serialize2DMaps(IDataOutput&) const;
	void serializeBorderBlocks(IDataOutput&) const;
	void serializeBlockEntities(IDataOutput&) const;
	void serializeBlockExtraData(IDataOutput&) const;
	void removeEntity(Entity&);
	void addEntity(std::unique_ptr<Entity, std::default_delete<Entity> >);
	void tickBlockEntities(BlockSource&);
	void tick(Player*, Tick const&);
	bool wasTickedThisTick(Tick const&) const;
	ChunkState getState() const;
	BlockPos getTopRainBlockPos(ChunkBlockPos const&);
	int getHeightmap(ChunkBlockPos const&) const;
	void setBiome(Biome const&, ChunkBlockPos const&);
	BlockEntity* getBlockEntity(ChunkBlockPos const&);
	std::vector<Entity*, std::allocator<Entity*> >& getEntities(Entity*, AABB const&, std::vector<Entity*, std::allocator<Entity*> >&);
	Brightness getBrightness(LightLayer const&, ChunkBlockPos const&);
	int getGrassColor(ChunkBlockPos const&);
	void setBrightness(LightLayer const&, ChunkBlockPos const&, Brightness);
	void setGrassColor(int, ChunkBlockPos const&);
	void setBlockAndData(ChunkBlockPos const&, FullBlock, BlockSource*, std::unique_ptr<BlockEntity, std::default_delete<BlockEntity> >);
	Brightness getRawBrightness(ChunkBlockPos const&, Brightness);
	int getBlockExtraData(ChunkBlockPos const&);
	void removeBlockEntity(BlockPos const&);
	void setBlockExtraData(ChunkBlockPos const&, unsigned short);
	void onBlockEntityChanged();
	Block* getAboveTopSolidBlock(ChunkBlockPos const&, bool, bool, bool);
	void isSkyLit(ChunkBlockPos const&);
	void setBorder(ChunkBlockPos const&, bool);
	bool isReadOnly() const;
	Biome& getBiome(ChunkBlockPos const&) const;
	bool getBorder(ChunkBlockPos const&) const;
	void operator delete(void*);
	void changeState(ChunkState, ChunkState);
	void tryChangeState(ChunkState, ChunkState);
	ChunkSource* getGenerator() const;
	int getMax() const;
	int getMin() const;
	void setFinalized(LevelChunk::Finalization);
	void setSaved();
	void* operator new(unsigned int);
	void _lightGaps(BlockSource&, ChunkBlockPos const&);
	void setUnsaved();
	void tickBlocks(Player*);
	void _lightLayer(LightLayer const&, ChunkBlockPos const&);
	void deserialize(KeyValueInput&);
	void _recalcHeight(ChunkBlockPos const&, BlockSource*);
	void _setGenerator(ChunkSource*);
	bool hasBlockEntity(ChunkBlockPos const&);
	void setAllBlockIDs(buffer_span<BlockID>, short);
	void setAllSkyLight(buffer_span<NibblePair>, short);
	void trimMemoryPool();
	void _createSubChunk(unsigned int, bool);
	void _placeCallbacks(ChunkBlockPos const&, BlockID, BlockID, BlockSource*, std::unique_ptr<BlockEntity, std::default_delete<BlockEntity> >);
	void recalcHeightmap();
	void setAllBlockData(buffer_span<NibblePair>, short);
	void _removeCallbacks(ChunkBlockPos const&, BlockID, BlockID, BlockSource*);
	void checkBiomeStates();
	void deserializeTicks(IDataInput&);
	void moveLightSources();
	void setAllBlockLight(buffer_span<NibblePair>, short);
	void updateCachedData(BlockSource&, bool);
	void _placeBlockEntity(std::unique_ptr<BlockEntity, std::default_delete<BlockEntity> >);
	void deferLightEmitter(BlockPos const&);
	void deserialize2DMaps(IDataInput&);
	void recalcBlockLights();
	void tickEntitiesDirty();
	void _createBlockEntity(BlockPos const&, BlockSource*, BlockID, BlockID);
	void _deserializeEntity(BlockSource&, IDataInput&, std::vector<EntityLink, std::allocator<EntityLink> >&);
	void setPendingEntities(std::string&);
	void tickRedstoneBlocks(BlockSource&);
	void deserializeSubChunk(unsigned char, IDataInput&);
	void findLightningTarget(BlockPos const&, BlockSource&);
	void deserializeBiomeStates(IDataInput&);
	void updateLightsAndHeights(BlockSource&);
	void _changeTerrainDataState(ChunkTerrainDataState, ChunkTerrainDataState);
	void applySeasonsPostProcess(BlockSource&);
	void deserialize2DMapsLegacy(IDataInput&);
	void deserializeBorderBlocks(IDataInput&);
	void deserializeFinalization(IDataInput&);
	void deserializeBlockEntities(IDataInput&);
	void legacyDeserializeTerrain(IDataInput&);
	void deserializeBlockExtraData(IDataInput&);
	void checkSeasonsPostProcessDirty();
	void onLoaded(BlockSource&);
	void _lightGap(BlockSource&, BlockPos const&);
	void _setLight(ChunkBlockPos const&, Brightness);
	bool hasEntity(Entity&);
	void getEntities(EntityType, AABB const&, std::vector<Entity*, std::allocator<Entity*> >&, bool) const;
	void getLastTick() const;
	bool needsSaving(int, int) const;
	Dimension& getDimension() const;
	bool isSubChunkKey(std::string const&) const;
	bool isBlockInChunk(BlockPos const&) const;
	void serializeTicks(IDataOutput&) const;
	void getLoadedFormat() const;
	void needsUpgradeFix() const;
	std::vector<BlockEntity*>& getBlockEntities() const;
	bool hasAnyBiomeStates() const;
	void serializeEntities(IDataOutput&) const;
	bool isAABBFullyInChunk(BlockPos const&, BlockPos const&) const;
	void _getTerrainDataState() const;
	bool hasAnyBlockExtraData() const;
	void serializeBiomeStates(IDataOutput&) const;
	void serializeFinalization(IDataOutput&) const;
	bool isAABBOverlappingChunk(BlockPos const&, BlockPos const&) const;
	void countSubChunksAfterPrune() const;
	void key() const;
	bool isDirty() const;
public:
	static int UPDATE_MAP_BIT_SHIFT;
};
