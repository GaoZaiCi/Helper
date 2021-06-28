#pragma once

#include "Player.h"
#include "../../inventory/IContainerListener.h"
#include "../../inventory/InventoryMenu.h"

class User;
class Boat;
class Certificate;
class Tick;
class ChalkboardBlockEntity;
class InventoryOptions;
class IContainerManager;
class NetworkIdentifier;
class ClientInstance;
class Side;
class HitResult;
namespace ui
{
	class GameEventNotification;
}
// Size : 3664
class LocalPlayer : public Player, public IContainerListener
{
public:
	//void **vtable;				// 3444
	char filler1[16];				// 3480
	ClientInstance *clientInstance;			// 3492
	ItemInstance itemInstance[5];	// 3596
	char filler2[4];				// 3660
public:
	enum class MovementAxis:int
	{
		
	};
	class RegionListener
	{
		
	};
public:
	virtual ~LocalPlayer();
	virtual void initializeComponents(Entity::InitializationMethod, VariantParameterList const&);
	virtual void setPos(Vec3 const&);
	virtual void move(Vec3 const&);
	virtual void normalTick();
	virtual void rideTick();
	virtual void startRiding(Entity&);
	virtual void handleEntityEvent(EntityEvent, int);
	virtual void handleInsidePortal(BlockPos const&);
	virtual void changeDimension(DimensionId);
	virtual void changeDimension(ChangeDimensionPacket const&);
	virtual void getControllingPlayer() const;
	virtual void checkFallDamage(float, bool);
	virtual void causeFallDamage(float);
	virtual void playSynchronizedSound(LevelSoundEvent, Vec3 const&, int, bool);
	virtual void stopRiding(bool, bool);
	virtual void drop(ItemInstance const&, bool);
	virtual bool outOfWorld();
	virtual void _hurt(EntityDamageSource const&, int, bool, bool);
	virtual void readAdditionalSaveData(CompoundTag const&);
	virtual void addAdditionalSaveData(CompoundTag&);
	virtual void die(EntityDamageSource const&);
	virtual void setSneaking(bool);
	virtual void setSprinting(bool);
	virtual void actuallyHurt(int, EntityDamageSource const*, bool);
	virtual void travel(float, float);
	virtual void applyFinalFriction(float);
	virtual void aiStep();
	virtual void swing();
	virtual void setArmor(ArmorSlot, ItemInstance const*);
	virtual void setItemSlot(EquipmentSlot, ItemInstance&);
	virtual void setOffhandSlot(ItemInstance const&);
	virtual void updateAi();
	virtual void destroyRegion();
	virtual void onPrepChangeDimension();
	virtual void onDimensionChanged();
	virtual void changeDimensionWithCredits(DimensionId);
	virtual void tickWorld(Tick const&);
	virtual void checkMovementStats(Vec3 const&);
	virtual void getCurrentStructureFeature() const;
	virtual void respawn();
	virtual void resetPos(bool);
	virtual bool isInTrialMode();
	virtual void startCrafting(BlockPos const&, bool);
	virtual void openContainer(int, BlockPos const&);
	virtual void openContainer(int, EntityUniqueID const&);
	virtual void openFurnace(int, BlockPos const&);
	virtual void openEnchanter(int, BlockPos const&);
	virtual void openAnvil(int, BlockPos const&);
	virtual void openBrewingStand(int, BlockPos const&);
	virtual void openHopper(int, BlockPos const&);
	virtual void openHopper(int, EntityUniqueID const&);
	virtual void openDispenser(int, BlockPos const&, bool);
	virtual void openBeacon(int, BlockPos const&);
	virtual void openPortfolio();
	virtual void openCommandBlock(BlockPos const&);
	virtual void openCommandBlockMinecart(EntityUniqueID const&);
	virtual void openHorseInventory(int, EntityUniqueID const&);
	virtual void openTrading(EntityUniqueID const&);
	virtual void openChalkboard(ChalkboardBlockEntity&);
	virtual void openNpcInteractScreen(Entity&);
	virtual void openInventory();
	virtual void openStructureEditor(BlockPos const&);
	virtual void displayClientMessage(std::string const&);
	virtual void displayLocalizableMessage(std::string const&, std::vector<std::string, std::allocator<std::string> > const&, bool);
	virtual void displayWhisperMessage(std::string const&, std::string const&);
	virtual void startSleepInBed(BlockPos const&);
	virtual void stopSleepInBed(bool, bool);
	virtual bool canStartSleepInBed();
	virtual void openSign(BlockPos const&);
	virtual bool isLocalPlayer() const;
	virtual void stopLoading();
	virtual void setPlayerGameType(GameType);
	virtual void _crit(Entity&);
	virtual void getEventing() const;
	virtual void addExperience(int);
	virtual void addLevels(int);
	virtual void setContainerData(IContainerManager&, int, int);
	virtual void slotChanged(IContainerManager&, int, ItemInstance const&, bool);
	virtual void refreshContainer(IContainerManager&, std::vector<ItemInstance, std::allocator<ItemInstance> > const&);
	virtual void deleteContainerManager();
	virtual bool isEntityRelevant(Entity const&);
	virtual bool isTeacher() const;
public:
	LocalPlayer(ClientInstance&, Level&, std::string const&, GameType, NetworkIdentifier const&, mce::UUID, std::unique_ptr<Certificate, std::default_delete<Certificate> >);
	void registerFramewiseActionOrStop();
	void setTeacher();
	void clearMovementState();
	void requestChunkRadius(int);
	void getLoadingState() const;
	void setInventoryOptions(InventoryOptions const&);
	void getPreloadingProgress();
	void getInventoryOptions() const;
	bool isAutoJumpEnabled() const;
	bool hasBossRegistered(EntityUniqueID) const;
	void closeScreen();
	InventoryMenu* getInventoryMenu();
	void hurtTo(int);
	void sendPosition();
	void stopPaddling(Side);
	void startPaddling(Side);
	void _updateAutoJump(float, float, float, float);
	void addItemCreative(ItemInstance const&);
	void localPlayerTurn(Vec2 const&);
	void setLeavingLevel(bool);
	void _calculateFlight(float, float, float);
	void _setRideInputFor(Entity&);
	void addRegionListener(LocalPlayer::RegionListener*);
	void pickBlockCreative(HitResult const&, bool);
	void resetLastHmdAngle();
	float getJumpRidingScale();
	void _computeBowOverride(Vec3&);
	void setPortalEffectTime(float);
	void _RotationVec2Modulus(Vec2&);
	void _updateArmorTypeHash();
	void removeRegionListener(LocalPlayer::RegionListener*);
	void setOPortalEffectTime(float);
	void getFieldOfViewModifier();
	void onGameEventNotification(ui::GameEventNotification);
	void _modifyDeltaByHMDPosition(Vec3&, float);
	void checkIfAchievedSuperSonic(float);
	void localPlayerRenderFrequencyMove(float);
	void _PostJumpFrameOfReferenceAdjustCheck(bool);
	void chat(std::string const&);
	void _input();
	void sendInput();
	void checkSuperSonic(BlockPos const&, LocalPlayer::MovementAxis) const;
	ClientInstance* getClientInstance() const;
	std::string getGameModeString() const;
	float getViewBlockRadius() const;
	float getPortalEffectTime() const;
	float getOPortalEffectTime() const;
	void getBob() const;
	bool isFlying() const;
	void _isPaddle(ItemInstance const&) const;
	void getBobOld() const;
public:
	static float const GLIDE_STOP_DELAY;
};
