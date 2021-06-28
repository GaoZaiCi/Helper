#pragma once
#include <unordered_set>
#include "LevelStorage.h"
#include "ParticleType.h"
#include "../util/BlockPos.h"
#include "BlockSourceListener.h"
#include "GeneratorType.h"
#include "storage/LevelData.h"
#include "../entity/Entity.h"
#include "../client/AppPlatformListener.h"
#include "../util/Random.h"
#include "../entity/player/LocalPlayer.h"
#include "../gamemode/GameType.h"

class Player;
class Random;
class MobFactory;
class SoundPlayer;
class ChangeDimensionRequest;
class BlockSource;
class FullBlock;
class EntityDamageSource;
class LightLayer;
class LevelChunk;
class Vec3;
class AABB;
class Material;
class NetEventCallback;
class LevelListener;
class LevelEvent;
class ResourcePackManager;
class LevelSettings;
class MapItemSavedData;
class GameRules;
class StructureManager;
class BossEventListener;
class SavedData;
class _TickPtr;
class MinecraftEventing;
class TextureData;
class BossEventUpdateType;
typedef int StorageVersion;
typedef int EntityUniqueID;
typedef int EntityRuntimeID;

// Size : 2996
class Level : public BlockSourceListener, public AppPlatformListener
{
public:
	char filler1[4];						// 8
	bool isRemote;							// 12
	char filler2[80];						// 16
	Random random;						// 96 + (SIZE??)
	char filler3[2664];						// 96
	LevelData worldInfo;					// 2760
	char filler4[32];						// 2868
	MobFactory *mobFactory;					// 2900
	char filler5[4];						// 2904
	BlockSource *tileSource;				// 2908
	char filler6[84];						// 2912
public:
	virtual ~Level();
	virtual void onSourceCreated(BlockSource&);
	virtual void onSourceDestroyed(BlockSource&);
	virtual void addEntity(BlockSource&, std::unique_ptr<Entity, std::default_delete<Entity> >);
	virtual void addGlobalEntity(BlockSource&, std::unique_ptr<Entity, std::default_delete<Entity> >);
	virtual void addAutonomousEntity(BlockSource&, std::unique_ptr<Entity, std::default_delete<Entity> >);
	virtual void addPlayer(std::unique_ptr<Player, std::default_delete<Player> >);
	virtual Entity& takeEntity(EntityUniqueID);
	virtual Entity& borrowEntity(EntityUniqueID, LevelChunk*);
	virtual void onPlayerDeath(Player&, EntityDamageSource const&);
	virtual void tick();
	virtual void directTickEntities(BlockSource&);
	virtual void updateSleepingPlayerList();
	virtual void setDifficulty(Difficulty);
	virtual void setCommandsEnabled(bool);
	virtual void runLightUpdates(BlockSource&, LightLayer const&, BlockPos const&, BlockPos const&);
	virtual void onNewChunkFor(Player&, LevelChunk&);
	virtual void onChunkLoaded(LevelChunk&);
	virtual void queueEntityRemoval(std::unique_ptr<Entity, std::default_delete<Entity> >&&, bool);
	virtual void removeEntityReferences(Entity&, bool);
	virtual void onAppSuspended();
	virtual ResourcePackManager& getClientResourcePackManager() const;
	virtual ResourcePackManager& getServerResourcePackManager() const;
public:
	Level(SoundPlayer&, std::unique_ptr<LevelStorage, std::default_delete<LevelStorage> >, std::string const&, LevelSettings const&, bool, MinecraftEventing&, ResourcePackManager&, StructureManager&);
	LevelData* getLevelData();
	LevelStorage* getLevelStorage();
	bool isDayCycleActive();
	Dimension& getDimension(DimensionId) const;
	bool isClientSide() const;
	Difficulty getDifficulty() const;
	int getPlayerColor(Player const&) const;
	bool hasLevelStorage() const;
	MapItemSavedData& getMapSavedData(EntityUniqueID);
	void addListener(LevelListener&);
	MinecraftEventing& getEventing();
	bool doesMapExist(EntityUniqueID);
	GameRules& getGameRules();
	void resumePlayer(Player&);
	std::vector<std::unique_ptr<Player> > getPlayerList();
	void extinguishFire(BlockSource&, BlockPos const&, signed char);
	void removeListener(LevelListener&);
	std::unique_ptr<Dimension> createDimension(DimensionId);
	EntityRuntimeID getNextRuntimeID();
	Inventory& getFixedInventory();
	void broadcastLevelEvent(LevelEvent, Vec3 const&, int, Player*);
	void broadcastSoundEvent(BlockSource&, LevelSoundEvent, Vec3 const&, int, EntityType, bool, bool);
	void getAdventureSettings();
	void broadcastDimensionEvent(BlockSource&, LevelEvent, Vec3 const&, int, Player*);
	void forceFlushRemovedPlayers();
	void requestPlayerChangeDimension(Player&, std::unique_ptr<ChangeDimensionRequest, std::default_delete<ChangeDimensionRequest> >);
	void getUsers();
	void getSpawner() const;
	Entity& fetchEntity(EntityUniqueID, bool) const;
	Entity& getRuntimeEntity(EntityRuntimeID, bool) const;
	bool hasCommandsEnabled() const;
	int getTime() const;
	Player& getPlayer(mce::UUID const&) const;
	void moveAutonomousEntityTo(Vec3 const&, EntityUniqueID, BlockSource&);
	void getPacketSender() const;
	Player& getPlayer(EntityUniqueID) const;
	void forEachPlayer(std::function<bool (Player&)>);
	Random& getRandom() const;
	void destroyBlock(BlockSource&, BlockPos const&, bool);
	void setFixedInventorySlot(int, ItemInstance*);
	int getFixedInventorySlotCount();
	void setFixedInventorySlotCount(int);
	void sendFixedInventoryUpdatePacket();
	void setTime(int);
	int getUserCount() const;
	void getVillages();
	void findPlayer(std::function<bool (Player const&)>) const;
	int getActivePlayerCount() const;
	void suspendPlayer(Player&);
	bool isPlayerSuspended(Player&) const;
	void broadcastEntityEvent(Entity*, EntityEvent);
	std::vector<Entity*>& getEntities(DimensionId, EntityType, AABB const&, std::vector<Entity*, std::allocator<Entity*> >&, bool);
	Player& getNearestPlayer(Entity&, float);
	void registerTemporaryPointer(_TickPtr&);
	void unregisterTemporaryPointer(_TickPtr&);
	Player& getNearestAttackablePlayer(Entity&, float);
	void addParticle(ParticleType, Vec3 const&, Vec3 const&, int);
	Player& getNearestAttackablePlayer(BlockPos, float, Entity*);
	void getSpecialMultiplier(DimensionId);
	void findPath(Entity&, Entity&, float, bool, bool, bool, bool, bool);
	void findPath(Entity&, int, int, int, float, bool, bool, bool, bool, bool);
	LocalPlayer* getLocalPlayer() const;
	void explode(BlockSource&, Entity*, Vec3 const&, float, bool, bool, float);
	void forEachPlayer(std::function<bool (Player const&)>) const;
	void getProjectileFactory() const;
	void broadcastBossEvent(BossEventUpdateType, Mob*);
	void potionSplash(Vec3 const&, Color const&, bool);
	EntityUniqueID getNewUniqueID();
	void entityChangeDimension(Entity&, DimensionId);
	void checkAndHandleMaterial(AABB const&, MaterialType, Entity*);
	void playSound(BlockSource&, LevelSoundEvent, Vec3 const&, int, EntityType, bool, bool);
	void getEntityDefinitions() const;
	long getCurrentTick() const;
	void _destroyEffect(BlockPos const&, FullBlock, Vec3 const&, float);
	Mob& getMob(EntityUniqueID) const;
	void playSound(LevelSoundEvent, Vec3 const&, int, EntityType, bool, bool);
	void getNearestPlayer(float, float, float, float);
	HitResult& getHitResult();
	void saveGameData();
	BlockPos getSharedSpawnPos();
	void getTearingDown() const;
	Entity& getAutonomousEntity(EntityUniqueID) const;
	void playSynchronizedSound(BlockSource&, LevelSoundEvent, Vec3 const&, int, EntityType, bool, bool);
	void updateLights();
	void setPacketSender(PacketSender*);
	void setNetEventCallback(NetEventCallback*);
	void setFinishedInitializing();
	void createMapSavedData(EntityUniqueID const&);
	void requestMapInfo(EntityUniqueID);
	int getSeed();
	void forceRemoveEntity(Entity&);
	void getStructureManager();
	void getPortalForcer();
	void denyEffect(Vec3 const&);
	void getNetEventCallback() const;
	void onChunkDiscarded(LevelChunk&);
	void getAutonomousLoadedEntities();
	void getSavedData() const;
	void loadPlayer(std::unique_ptr<Player, std::default_delete<Player> >);
	void setLevelId(std::string);
	void animateTick(Entity&);
	void savePlayers();
	void setStopTime(int);
	void takePicture(TextureData&, Entity*, Entity*, bool, std::string const&);
	void _loadMapData(EntityUniqueID const&);
	void _saveMapData(MapItemSavedData&);
	void setNightMode(bool);
	void setSavedData(std::string const&, SavedData*);
	void tickEntities();
	void _resumePlayer(mce::UUID const&);
	void checkMaterial(AABB const&, MaterialType, Entity*);
	Player& getNextPlayer(EntityUniqueID const&, bool);
	Player& getPrevPlayer(EntityUniqueID const&, bool);
	void saveBiomeData();
	void saveLevelData();
	void _suspendPlayer(mce::UUID const&);
	std::vector<std::string> getPlayerNames();
	void setIsExporting(bool);
	void _saveAllMapData();
	void _saveSomeChunks();
	Player& getRandomPlayer();
	void saveDirtyChunks();
	void setDefaultSpawn(BlockPos const&);
	void setIsClientSide(bool);
	void setLANBroadcast(bool);
	void setXBLBroadcast(bool);
	void createRandomSeed();
	void handleLevelEvent(LevelEvent, Vec3 const&, int);
	void handleSoundEvent(LevelSoundEvent, Vec3 const&, int, EntityType, bool, bool);
	void setSpawnSettings(bool);
	void _removeAllPlayers();
	std::vector<Entity*> getGlobalEntities();
	void getLightsToUpdate();
	void setDayCycleActive(bool);
	void createMapSavedData(std::vector<EntityUniqueID, std::allocator<EntityUniqueID> > const&);
	void setMultiplayerGame(bool);
	void _createMapSavedData(EntityUniqueID const&);
	void _validatePlayerName(Player&);
	void createUniqueLevelID(int);
	int getNumRemotePlayers();
	void addBossEventListener(BossEventListener*);
	void waitAsyncSuspendWork();
	void getAutonomousEntities();
	void getFixedInventorySlot(int);
	void upgradeStorageVersion(StorageVersion);
	void _backgroundTickSeasons();
	void _playerChangeDimension(Player*, ChangeDimensionRequest&);
	void _tickTemporaryPointers();
	void loadAutonomousEntities();
	void saveAutonomousEntities();
	void _handlePlayerSuspension();
	void removeBossEventListener(BossEventListener*);
	void _loadDimensionStructures(int);
	void _saveDimensionStructures();
	void removeAllNonPlayerEntities(EntityUniqueID);
	void _cleanupDisconnectedPlayers();
	void _handleChangeDimensionRequests();
	void _syncTime(int);
	int getLevelId() const;
	bool isExporting() const;
	bool isNightMode() const;
	LevelData* getLevelData() const;
	EntityUniqueID getNewPlayerId() const;
	BlockPos getDefaultSpawn() const;
	bool hasLANBroadcast() const;
	bool hasXBLBroadcast() const;
	void getRuntimePlayer(EntityRuntimeID) const;
	void getSpawnEntities() const;
	bool isUpdatingLights() const;
	bool isMultiplayerGame() const;
	std::string getScreenshotsFolder() const;
	void getAutonomousActiveEntity(EntityUniqueID) const;
	void getAutonomousInactiveEntity(EntityUniqueID) const;
	void getUsers() const;
	Player& getPlayer(std::string const&) const;
public:
	static Vec3 tickingChunksOffset;
};
