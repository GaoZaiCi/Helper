//
// Created by Gao在此 on 2020/2/22.
//

#pragma once

#include <math.h>
#include <vector>
#include <atomic>
#include <string>

class BlockSource;
class SynchedEntityData;
class ActorDamageSource;
class ItemStack;
class Attribute;
class AttributeInstance;
class MobEffectInstance;
class Actor;
class TagMemoryChunk;
class ListTag;
class LevelData;
class Spawner;
class Player;
class ServerPlayer;
class LocalPlayer;
class PlayerInventoryProxy;
class Abilities;
class Recipes;
class BlockPalette;
class BlockLegacy;
class IDataOutput;
class PrintStream;
class IDataInput;
class Timer;
class Mob;
class Options;
class AbstractScreen;
class ScreenChooser;
class GuiData;
class MinecraftCommands;
class LevelRenderer;
class ResourcePackManager;
class EntityRenderDispatcher;
class ItemRenderer;
class UIProfanityContext;
class ServerNetworkHandler;
class SceneStack;
class SceneFactory;
class ClientInstance;
class ItemInstance;
class Entity;
class IContainerManager;
class Tick;
class InventoryOptions;
class Item;
class ItemStackBase;
class Block;
class UseAnimation;
class BlockEntity;
class ChalkboardBlockEntity;
class TextureUVCoordinateSet;
class UseDuration;
class Color;
class ItemEnchants;
class Minecraft;
class BlockAndData;
class AABB;
class Command;
class ActorInfoRegistry;
class BlockDefinitionGroup;
class BaseGameVersion;
class ContentIdentity;
class LevelSettings;
class IMinecraftGame;

enum class EntityType : int
{
    ITEM = 64,
    PRIMED_TNT,
    FALLING_BLOCK,
    EXPERIENCE_POTION = 68,
    EXPERIENCE_ORB,
    FISHINGHOOK = 77,
    ARROW = 80,
    SNOWBALL,
    THROWNEGG,
    PAINTING,
    LARGE_FIREBALL = 85,
    THROWN_POTION,
    LEASH_FENCE_KNOT = 88,
    BOAT = 90,
    LIGHTNING_BOLT = 93,
    SAMLL_FIREBALL,
    TRIPOD_CAMERA = 318,
    PLAYER=1,
    IRON_GOLEM = 788,
    SOWN_GOLEM,
    VILLAGER = 1807,
    CREEPER = 2849,
    SLIME = 2853,
    ENDERMAN,
    GHAST = 2857,
    LAVA_SLIME,
    BLAZE,
    WITCH = 2861,
    CHICKEN = 5898,
    COW ,
    PIG,
    SHEEP,
    MUSHROOM_COW = 5904,
    RABBIT = 5906,
    SQUID = 10001,
    WOLF = 22286,
    OCELOT = 22294,
    BAT = 33043,
    PIG_ZOMBIE = 68388,
    ZOMBIE = 199456,
    ZOMBIE_VILLAGER = 199468,
    SPIDER = 264995,
    SILVERFISH = 264999,
    CAVE_SPIDER,
    MINECART_RIDEABLE = 524372,
    MINECART_HOPPER = 524384,
    MINECART_MINECART_TNT,
    MINECART_CHEST,
    SKELETON = 1116962,
    WITHER_SKELETON = 1116974,
    STRAY = 1116976,
    HORSE = 2119447,
    DONKEY,
    MULE,
    SKELETON_HORSE,
    ZOMBIE_HORSE
};

enum ActorFlags {
    ActorFlagsImmobile = 16,
};
enum ParticleType {
};
enum AbilitiesIndex {
};
enum ContainerID : unsigned char {
    ContainerIDInventory = 0,
};
enum CreativeItemCategory {
};
enum GameType {
};
enum ArmorSlot {
};
enum CommandPermissionLevel {
    CommandPermissionLevelDefault = 0,
    CommandPermissionLevelHigher = 1,
    CommandPermissionLevelOperator = 2,
    CommandPermissionLevelHost = 3
};
enum CommandFlag{};
class ActorUniqueID {
public:
    long long id;

    ActorUniqueID(long long id) : id(id) {
    }

    operator long long() const {
        return this->id;
    }
};

typedef ActorUniqueID EntityUniqueID;

namespace mce {
    class TextureGroup;
    class Texture;
} // namespace mce
namespace Json {
    class Value;
};
struct Vec2{
public:
    float x;	// 0
    float y;	// 4
public:
    Vec2(float x,float y):x(x),y(y){}
    Vec2()=default;
};

struct Vec3 {
    float x;
    float y;
    float z;

    Vec3(float x_, float y_, float z_) : x(x_), y(y_), z(z_) {
    };

    Vec3() : x(0), y(0), z(0) {
    };
};

struct BlockPos {
    int x;
    int y;
    int z;
    BlockPos(BlockPos const& other) : x(other.x), y(other.y), z(other.z) {
    }
    BlockPos(): x(0), y(0), z(0) {
    }
    BlockPos(int x_, int y_, int z_): x(x_), y(y_), z(z_) {
    }
    BlockPos(Vec3 const&vec3):x(floorf(vec3.x)),y(floorf(vec3.y)),z(floorf(vec3.z)){

    };
};
struct PlayerInventorySlot {
    ContainerID containerID;
    int slot;
};
typedef struct {
    char filler0[24-0];
    int type; //24
    unsigned char side; //28
    int x; //32
    int y; //36
    int z; //40
    Vec3 hitVec; //44
    Entity* entity; //56
    unsigned char filler1[89-60]; //60
} HitResult;
class Tag{
public:
};

class CompoundTag: public Tag {
public:
    void write(IDataOutput &) const;
    bool equals(Tag const &) const;
    std::string toString() const;
    void print(std::string const &, PrintStream &) const;
    CompoundTag();
    CompoundTag(std::string const &);
    CompoundTag(CompoundTag &&);
    Tag *get(std::string const &) const;
    bool contains(std::string const &) const;
    bool contains(std::string const &, int) const;
    int8_t getByte(std::string const &) const;
    int16_t getShort(std::string const &) const;
    int getInt(std::string const &) const;
    int64_t getInt64(std::string const &) const;
    float getFloat(std::string const &) const;
    double getDouble(std::string const &) const;
    std::string getString(std::string const &) const;
    TagMemoryChunk getByteArray(std::string const &) const;
    TagMemoryChunk getIntArray(std::string const &) const;
    CompoundTag *getCompound(std::string const &) const;
    ListTag *getList(std::string const &) const;
    bool getBoolean(std::string const &) const;
    bool isEmpty() const;
    void *rawView() const;
    ~CompoundTag();
    void getAllTags(std::vector<Tag *, std::allocator<Tag *> > &) const;
    void remove(std::string const &);
    void load(IDataInput &);
    void put(std::string const &, std::unique_ptr<Tag, std::default_delete<Tag> >);
    CompoundTag *copy() const;
    CompoundTag *clone() const;
    std::unique_ptr<CompoundTag> uniqueClone() const;
    void putByte(std::string const &, char);
    void putBoolean(std::string const &, bool);
    void putShort(std::string const &, short);
    void putInt(std::string const &, int);
    void putInt64(std::string const &, long long);
    void putFloat(std::string const &, float);
    void putDouble(std::string const &, float);
    void putString(std::string const &, std::string const &);
    void putByteArray(std::string const &, TagMemoryChunk);
    void putCompound(std::string const &,std::unique_ptr<CompoundTag, std::default_delete<CompoundTag> >);
};

class PermissionsHandler{
  public:
    int getPlayerPermissions(void)const;
};

class Level {
public:
    void **vtable; // 0
    char filler[44 - 4]; // 4
    std::vector<Player *> allPlayers; // 44

    void queueEntityRemoval(std::unique_ptr<Actor, std::default_delete<Actor>> &&, bool);
    Actor *fetchEntity(EntityUniqueID, bool) const;
    bool addEntity(BlockSource &, std::unique_ptr <Actor>);
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
    Player *getPlayer(ActorUniqueID)const;
    Player *getPrimaryLocalPlayer() const;
    void addParticle(ParticleType, Vec3 const &, Vec3 const &, int, CompoundTag const *, bool);
    Abilities *getPlayerAbilities(EntityUniqueID const &);
    Recipes *getRecipes() const;
    BlockPalette *getGlobalBlockPalette() const;
    bool IsDisableOpenContainers();
};
class ServerLevel : public Level {
public:
    MinecraftCommands* getCommands();
};
class MultiPlayerLevel : public Level{
  public:
    void tick();
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
    Vec3 getPos()const;
    Vec3 getAABBShapeComponent()const;
    void setPos(Vec3 const&);//设置坐标
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
    void setArmor(ArmorSlot, ItemStack const&);
    AttributeInstance* getAttribute(Attribute const&) const;
    void setChanged();
    void addEffect(MobEffectInstance const&);//添加药水效果
    void removeEffect(int);//移除指定药水效果
    void removeAllEffects();//移除所有药水效果
    bool canFly();//是否可以飞
    void setCanFly(bool);//设置是否可以飞行
    void attack(Actor&);//攻击实体
    bool isRemoved()const;
    EntityType getEntityTypeId()const;//获取实体类型
    void setCarriedItem(ItemStack const&);//设置手持物品
};
class Mob :public Actor{
public:
    void sendInventory(bool);
};

class Player: public Mob {
public:
    int getScore();
    void addExperience(int);
    void addLevels(int);
    PlayerInventoryProxy* getSupplies() const;
    CommandPermissionLevel getPlayerPermissionLevel();
    void setPermissions(CommandPermissionLevel);
    bool canUseOperatorBlocks();
    bool isOperator();
    void attack(Actor&);//攻击实体
    std::string getName();
    void sendInventory(bool);//发送背包
    int getEntityTypeId();
    ItemStack *getCarriedItem()const;
};

class PlayerInventoryProxy{
public:
    bool add(ItemStack&, bool);
    bool canAdd(ItemStack &);
    void removeResource(ItemStack const&, bool, bool, int);
    void clearSlot(int, ContainerID id=ContainerIDInventory);
    ItemStack* getItem(int, ContainerID id=ContainerIDInventory) const;
    void setItem(int, ItemStack const&, ContainerID=ContainerIDInventory);
    PlayerInventorySlot getSelectedSlot() const;
    void selectSlot(int, ContainerID=ContainerIDInventory);
    void save();
};

class SurvivalMode{
public:
    void tick();
    void attack(Actor*);
};

class GameMode{
public:
    Player* player; // 4
    void attack(Actor&);
    void useItem(ItemStack &);
    void useItemOn(ItemStack &, BlockPos const&, unsigned char, Vec3 const&, Block const*);
    void interact(Actor &, Vec3 const&);
    void buildBlock(BlockPos const&, unsigned char);
    void startBuildBlock(BlockPos const&,unsigned char);
    void continueBuildBlock(BlockPos const&,unsigned char);
    void stopBuildBlock();
    void tick();
    float getDestroyProgress();
    void destroyBlock(BlockPos const&, unsigned char);
    void _destroyBlockInternal(BlockPos const&, unsigned char);
    bool _canDestroy(BlockPos const&,unsigned char);
    bool _canUseBlock(Block const&);
    void startDestroyBlock(BlockPos const&,unsigned char,bool &);
    void continueDestroyBlock(BlockPos const&,unsigned char,bool &);
    void _creativeDestroyBlock(BlockPos const&,unsigned char);
    float getOldDestroyProgress(void);
    float getDestroyRate(Block const&);
};

class Abilities {
public:
    bool isOperator();
    bool getBool(AbilitiesIndex) const;
    void setAbility(AbilitiesIndex, bool);
    static AbilitiesIndex nameToAbilityIndex(std::string const&);
};

class ServerPlayer : public Player{
public:
    void sendInventory(bool);
};

class Minecraft {
public:
    Level* getLevel() const;
    Timer* getTimer();
    MinecraftCommands* getCommands();
    ResourcePackManager* getResourceLoader();
    void setGameModeReal(GameType);
};

class MinecraftGame{
public:
    ClientInstance* getPrimaryClientInstance();
    LocalPlayer* getPrimaryLocalPlayer();
    Level* getLocalServerLevel() const;
};
class IClientInstance {
};

class ClientInstance : public IClientInstance {
public:
    MinecraftGame* getMinecraftGame() const;
    Minecraft* getServerData();
    Player* getLocalPlayer();
    mce::TextureGroup& getTextures() const;
    void setCameraEntity(Entity*);
    void setCameraTargetEntity(Entity*);
    Options* getOptions();
    AbstractScreen* getScreen();
    ScreenChooser& getScreenChooser() const;
    GuiData* getGuiData();
    void onResourcesLoaded();
    LevelRenderer* getLevelRenderer() const;
    void play(std::string const&, Vec3 const&, float, float);
    void _startLeaveGame();
    Level* getLevel();
    EntityRenderDispatcher& getEntityRenderDispatcher();
    ItemRenderer* getItemRenderer();
    UIProfanityContext const& getUIProfanityContext() const;
    mce::Texture const& getUITexture();
    SceneStack& getClientSceneStack() const;
    SceneFactory& getSceneFactory() const;
    Level* getLocalServerLevel();
};
class CommandOrigin {
public:
    CommandOrigin();
    virtual ~CommandOrigin();
};

class ServerCommandOrigin : public CommandOrigin {
public:
    ServerCommandOrigin(std::string const&, ServerLevel &, CommandPermissionLevel);
    virtual ~ServerCommandOrigin();
    char filler[40-4]; // 4 from ServerCommandOrigin::clone()
};

class DevConsoleCommandOrigin : public CommandOrigin {
public:
    DevConsoleCommandOrigin(Player&);
    virtual ~DevConsoleCommandOrigin();
    char filler[200-4]; // 4 from DevConsoleCommandOrigin::clone()
};

class CommandVersion {
public:
    static int CurrentVersion;
};
struct MCRESULT {

};

class CommandRegistry {
public:
    Command* findCommand(std::string const&) const;
    void checkOriginCommandFlags(CommandOrigin const&, CommandFlag, CommandPermissionLevel)const;
};

class MinecraftCommands {
public:
    CommandRegistry* getRegistry();
    MCRESULT requestCommandExecution(std::unique_ptr<CommandOrigin,std::default_delete<CommandOrigin>>,std::string const&,int,bool);
};
class MinecraftScreenModel{
public:
    void _startLocalServer(std::string const&,std::string const&,ContentIdentity const&,LevelSettings const&);
    bool isCreative();
    bool isOperator();
    Player* getLocalPlayer();
    CommandPermissionLevel getPlayerCommandPermissionLevel();
    void devConsoleExecuteCommand(std::string const&);
    MinecraftCommands* getCommands();
};
class ClientInstanceScreenModel{
  public:
    ClientInstanceScreenModel(IMinecraftGame &, IClientInstance &, SceneStack &, SceneFactory &);
    Vec3 getPlayerPosition()const;
    float getPlayerExp();
    int getPlayerLevel();
    bool getNeteaseStoreVisible();
    ItemStack* getOffhandSlot();
    bool requestDisconnectPlayer(std::string const&);
    bool shouldDisplayPlayerPosition();
};
class ContentIdentity{
public:
};
class LevelSettings
{
public:
    unsigned int randomSeed;	// 0
    int theGameType;			// 4
    // .
    // .
    // .
    // ?
};
class StartIntent{
public:
};
class IContainerListener{
public:
};
class LocalPlayer : public Player, public IContainerListener
{
public:
    //void **vtable;				// 3444
    char filler1[16];				// 3480
    ClientInstance *clientInstance;			// 3492
    //ItemInstance itemInstance[5];	// 3596
    char filler2[4];				// 3660
public:
    enum class MovementAxis:int
    {

    };
    class RegionListener
    {

    };
public:
    static float const GLIDE_STOP_DELAY;
};

class EnchantedBookItem{
  public:
    EnchantedBookItem(std::string const&,int,bool);
    bool isGlint(ItemStackBase const&);
    short getEnchantValue()const;
    int getEnchantSlot()const;
};

class Enchant {
public:
    void* vtable; // 0
    int id; // 4
    char filler[24-8]; // 8
    std::string description; // 24
    static std::vector<Enchant*> mEnchants;
    enum Type {
    };
};

class EnchantUtils {
public:
    static bool overrideEnchant(ItemStack &,Enchant::Type,int);
    static bool applyEnchant(ItemInstance&, Enchant::Type, int, bool);
    static bool applyEnchant(ItemStack&, Enchant::Type, int, bool);
};

class EnchantmentInstance {
public:
    int type;
    int level;

    EnchantmentInstance();
    EnchantmentInstance(Enchant::Type t, int l):EnchantmentInstance(){
        setEnchantType(t);
        setEnchantLevel(l);
    };
    Enchant::Type getEnchantType();
    int getEnchantLevel();
    void setEnchantType(Enchant::Type);
    void setEnchantLevel(int);
};

class ItemEnchants {
public:
    char filler[40]; // ItemEnchants::ItemEnchants
    int count() const;
    std::vector<EnchantmentInstance> getAllEnchants() const;
    bool hasEnchant(Enchant::Type)const;
    bool isEmpty()const;
    bool canEnchant(EnchantmentInstance,bool);
    bool addEnchant(EnchantmentInstance, bool);
    bool addEnchants(ItemEnchants const&,bool);
    bool overrideEnchant(Enchant::Type,int);
};

class ItemStackBase {
public:
    char filler1[16-4]; // 4
    short damage; // 16
    unsigned char count; //18
    char filler2[84-19]; // 19

    virtual ~ItemStackBase();
    ItemEnchants* getEnchantsFromUserData() const;
    void saveEnchantsToUserData(ItemEnchants const&);
    bool hasCustomHoverName() const;
    std::string getCustomName() const;
    void setCustomName(std::string const&);
    void init(int, int, int);
    void init(Item const&,int,int,CompoundTag const*);
    void init(BlockLegacy const&,int);
    int getId() const;
    TextureUVCoordinateSet* getIcon(int, bool) const;
    std::string getName() const;
    int getMaxStackSize() const;
    void remove(int);
    bool isArmorItem() const;
    UseAnimation getUseAnimation() const;
    void _setItem(int);
    void setBlock(Block const*);
    int getDamageValue() const;
    void setUserData(std::unique_ptr<CompoundTag> tag);
    bool isEnchanted();
    bool isBlock()const;
};
class BlockLegacy {
public:
    void** vtable; //0
    std::string nameId; // 4
    std::string mappingId; // 8
    char filler1[24-12]; // 12
    int renderLayer; //24
    char filler2[96-28]; // 28
    float destroyTime; //96
    float explosionResistance; //100
    char filler3[120-104]; // 104
    unsigned char lightOpacity; // 120 from BlockLegacy::setLightBlock
    unsigned char lightEmission; // 121 from BlockLegacy::setLightEmission
    char filler4[136-122]; // 122
    unsigned short id; // 136
    char filler5[3192-138]; // 138

    float getDestroySpeed() const;
    float getFriction() const;
    void setFriction(float);
    void setSolid(bool);
    void setCategory(CreativeItemCategory);
    std::string const& getDescriptionId() const;
    int getRenderLayer() const;
    void* getMaterial() const;
    BlockAndData* getStateFromLegacyData(unsigned short) const;
    AABB& getVisualShape(BlockAndData const&, AABB&, bool) const;
};

class BlockSource{
public:
    bool checkBlockDestroyPermissions(Actor &,BlockPos const&,ItemStack const&,bool);
};

class Item{
  public:
    //void** vtable; //0
    char filler0[64-4]; //4
    short itemId; //64
    char filler1[98-66]; // 66
    short flags; // 98
    char filler3[168-100]; // 100
    virtual ~Item();

    Item(std::string const&, short);

    // this one loads textures
    void initClient(Json::Value&, Json::Value&);
    // this one doesn't
    void initServer(Json::Value&);
    void setStackedByData(bool);
    bool isStackedByData() const;
    void setMaxDamage(int);
    int getMaxDamage() const;
    void setAllowOffhand(bool);
    void setUseAnimation(UseAnimation);
    static void initCreativeItems(bool, ActorInfoRegistry*, BlockDefinitionGroup*, bool, BaseGameVersion const&, std::function<void (ActorInfoRegistry*, BlockDefinitionGroup*, bool)>);
};

class ItemStack : public ItemStackBase {
public:
    ItemStack();
    ItemStack(ItemInstance const&);
    ItemStack(Item const& item, int count, int data);
    ItemStack(Item const&, int, int, const CompoundTag *);//带命令标签物品
    ItemStack(BlockLegacy const&, int);//方块
    ItemStack(Block const&, int, const CompoundTag *);//带命令标签方块
};

class ItemInstance : public ItemStackBase{
public:
    ItemInstance();
    ItemInstance(ItemStack const&);
    ItemInstance(ItemInstance const&);
    ItemInstance(Item const&,int,int);
    ItemInstance(Item const&);
    ItemInstance(Item const&,int,int,CompoundTag const*);
    ItemInstance(Item const&,int);
    ItemInstance(BlockLegacy const&,int);
    ItemInstance(Block const&,int,CompoundTag const*);
    ItemInstance(BlockLegacy const&,int,short);
};

template<class T> class SharedPtrImpl {
public:
    T* ptr; // 0
    void* destructor; // 4
    std::atomic_uint referenceCount; // 8
};

template<class T> class SharedPtr {
public:
    SharedPtrImpl<T>* impl;
    ~SharedPtr() {
        // don't care since we're treating the SharedPtr as a weak anyways
        // this is enough to get it to pass via temporary
    }
    T* get() {
        if (!impl) return nullptr;
        return impl->ptr;
    }
    template <class... Args>
    static SharedPtr<T> make(Args&&... args);
};

class ItemRegistry {
public:
    static SharedPtr<Item> getItem(short);
    static SharedPtr<Item> getItem(std::string const&);
    static void _setItem(short, Item const*);
    static void registerItem(SharedPtr<Item>&&);
    static ItemRegistry* mItemRegistry;
    static int getMaxItemID();
    static int getItemCount();
    //static std::unordered_map<std::string, std::pair<std::string const, Item const*>> mItemLookupMap;
};
