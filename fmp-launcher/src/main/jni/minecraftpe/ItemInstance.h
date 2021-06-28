//
// Created by Gao在此 on 2020/2/18.
//

#pragma once

#include <memory>

class TextureUVCoordinateSet;
class CompoundTag;
class Item;
class Block;
class IDataInput;
class BlockEntity;
class IDataOutput;
class ItemEnchants;
class BlockSource;
class Mob;
class Entity;
class Player;
class Color;
class UseDuration;
class UseAnimation;
class BlockID;

namespace Json
{
    class Value;
}

class ItemInstance
{
public:
    unsigned char count;
    unsigned short aux;
    CompoundTag* userData;
    bool valid;
    Item* item;
    Block* block;
public:
    ItemInstance(Item const&, int);
    ItemInstance(ItemInstance const&);
    ItemInstance(Item const&);
    ItemInstance(Block const&);
    ItemInstance(int, int, int);
    ItemInstance();
    ItemInstance(Item const&, int, int);
    ItemInstance(Block const&, int);
    ItemInstance(Block const&, int, int);
    ItemInstance(Item const&, int, int, CompoundTag const*);
    ItemInstance(int, int, int, CompoundTag const*);
public:
    UseAnimation getUseAnimation() const;
    bool isNull() const;
    std::string getHoverName() const;
    std::string getEffectName() const;
    void setAuxValue(short);
    ItemInstance& operator=(ItemInstance const&);
    short getAuxValue() const;
    bool isOffhandItem() const;
    bool isWearableItem() const;
    bool sameItemAndAux(ItemInstance const&) const;
    unsigned char getMaxStackSize() const;
    bool isDamageableItem() const;
    ItemInstance clone() const;
    int getId() const;
    std::string getName() const;
    bool operator!=(ItemInstance const&) const;
    void add(int);
    bool isFullStack() const;
    void operator==(ItemInstance const&) const;
    void set(int);
    void addCustomUserData(BlockEntity&, BlockSource&);
    bool isThrowable() const;
    bool isEquivalentArmor(ItemInstance const&) const;
    void getStrippedNetworkItem() const;
    UseDuration getMaxUseDuration() const;
    bool isHorseArmorItem() const;
    bool isArmorItem() const;
    bool isGlint() const;
    bool hasCustomHoverName() const;
    TextureUVCoordinateSet const& getIcon(int, bool) const;
    Color getColor() const;
    short getMaxDamage() const;
    short getDamageValue() const;
    bool isDamaged() const;
    void setNull();
    bool isStackedByData() const;
    bool matches(ItemInstance const&) const;
    bool isStackable() const;
    short getIdAuxEnchanted() const;
    short getIdAux() const;
    bool isLiquidClipItem() const;
    std::string getFormattedHovertext(Level&, bool) const;
    void setUserData(std::unique_ptr<CompoundTag, std::default_delete<CompoundTag> >);
    void retrieveIDFromIDAux(int);
    void retrieveAuxValFromIDAux(int);
    void retrieveEnchantFromIDAux(int);
    void getEnchantsFromUserData() const;
    void onCraftedBy(Level&, Player&);
    void addComponents(Json::Value const&, std::string&);
    void remove(int);
    void fromTag(CompoundTag const&);
    bool isStackable(ItemInstance const&) const;
    void matchesItem(ItemInstance const&) const;
    void save() const;
    void setCustomName(std::string const&);
    void setRepairCost(int);
    void resetHoverName();
    bool isEnchantingBook() const;
    int getBaseRepairCost() const;
    void setJustBrewed(bool);
    bool isPotionItem() const;
    bool wasJustBrewed() const;
    bool isEnchanted() const;
    int getEnchantValue() const;
    void inventoryTick(Level&, Entity&, int, bool);
    void hurtAndBreak(int, Entity*);
    CompoundTag* getUserData() const;
    bool hasUserData() const;
    void load(CompoundTag const&);
    int getAttackDamage() const;
    void releaseUsing(Player*, int);
    void interactEnemy(Mob*, Player*);
    void useTimeDepleted(Level*, Player*);
    void hurtEnemy(Mob*, Mob*);
    float startCoolDown(Player*) const;
    bool getDestroySpeed(Block const&) const;
    void canDestroySpecial(Block const&) const;
    bool use(Player&);
    bool useOn(Entity&, int, int, int, signed char, float, float, float);
    void mineBlock(BlockID, int, int, int, Mob*);
    void componentsMatch(ItemInstance const&) const;
    bool hasSameUserData(ItemInstance const&) const;
    void saveEnchantsToUserData(ItemEnchants const&);
    void deserializeComponents(IDataInput&);
    std::string getCustomName() const;
    void serializeComponents(IDataOutput&) const;
    void _initComponents();
    void _loadComponents(CompoundTag const&);
    void updateComponent(std::string const&, Json::Value const&);
    void _cloneComponents(ItemInstance const&);
    bool isValidComponent(std::string const&);
    void setDescriptionId(std::string const&);
    void init(int, int, int);
    void snap(Player*);
    void _setItem(int);
    bool useAsFuel();
    bool canDestroy(Block const*) const;
    bool canPlaceOn(Block const*) const;
    std::string getRawNameId() const;
    bool hasComponent(std::string const&) const;
    bool _hasComponents() const;
    int getEnchantSlot() const;
    void _saveComponents(CompoundTag&) const;
    void getNetworkUserData() const;
    void _getHoverFormattingPrefix() const;
    std::string toString() const;
public:
    static ItemInstance const EMPTY_ITEM;
    static short const MAX_STACK_SIZE;
    static std::string const TAG_ENCHANTS;
    static std::string const TAG_DISPLAY;
    static std::string const TAG_CAN_DESTROY;
    static std::string const TAG_REPAIR_COST;
    static std::string const TAG_CAN_PLACE_ON;
    static std::string const TAG_DISPLAY_NAME;
    static std::string const TAG_STORE_CAN_DESTROY;
    static std::string const TAG_STORE_CAN_PLACE_ON;
};
int safeGetItemId(ItemInstance const*);
int safeGetItemAuxId(ItemInstance const*);
