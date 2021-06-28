//
// Created by Gao在此 on 2020/2/18.
//

#pragma once

class ItemEnchants;
class TextureUVCoordinateSet;
class UseAnimation;
class CompoundTag;
class Block;

class ItemStackBase {
public:
    char filler1[16-4]; // 4
    short damage; // 16
    unsigned char count; //18
    char filler2[84-19]; // 19

    virtual ~ItemStackBase();
    ItemEnchants getEnchantsFromUserData() const;
    bool hasCustomHoverName() const;
    std::string getCustomName() const;
    void setCustomName(std::string const&);
    void init(int, int, int);
    int getId() const;
    TextureUVCoordinateSet* getIcon(int, bool) const;
    std::string getName() const;
    int getMaxStackSize() const;
    void remove(int);
    bool isArmorItem() const;
    UseAnimation getUseAnimation() const;
    void _setItem(int);
    int getDamageValue() const;
    void setBlock(Block const*);
    void setUserData(std::unique_ptr<CompoundTag> tag);
}; // see ItemInstance::fromTag for size
// or just use the shared_ptr constructor
// or look at ItemInstance::EMPTY_ITEM