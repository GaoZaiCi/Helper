#pragma once

#include "Item.h"
#include "ArmorSlot.h"

// Size : 88
class ArmorItem : public Item
{
public:
	// Size : 20
	class ArmorMaterial
	{
	public:
		int i1;		// 0
		int defense;// 4
		int i3;		// 8
		int i4;		// 12
		int i5;		// 16
		int i6;		
	public:
		ArmorMaterial(int,int,int,int,int,int);
		int getDefenseForSlot(int);
		int getHealthForSlot(int);
	};

public:
	static ArmorMaterial CHAIN;
	static ArmorMaterial CLOTH;
	static ArmorMaterial DIAMOND;
	static ArmorMaterial GOLD;
	static ArmorMaterial IRON;
	static int healthPerSlot[4];

public:
	int i1;                            // 72
	int defence;                      // 76
	int i2;                            // 80
	ArmorMaterial *armorMaterial;    // 84

public:
	virtual bool isArmor() const;
	virtual std::string appendFormattedHovertext(ItemInstance const&, Level&, std::string&, bool) const;
	virtual bool isValidRepairItem(ItemInstance const&, ItemInstance const&)const;
	virtual int getEnchantSlot() const;
	virtual int getEnchantValue() const;
	virtual int getDamageChance(int) const;
	virtual int getColor(ItemInstance const&) const;
	virtual bool use(ItemInstance&, Player&) const;
	virtual void dispense(BlockSource&, Container&, int, Vec3 const&, signed char)const;
	virtual void hurtEnemy(ItemInstance&, Mob*, Mob*)const;
	virtual void mineBlock(ItemInstance&, BlockID, int, int, int, Entity*)const;
	virtual TextureUVCoordinateSet const& getIcon(int, int, bool) const;
public:
	ArmorItem(std::string const&, int, ArmorItem::ArmorMaterial const&, int, ArmorSlot);
	bool isFlyEnabled(ItemInstance const&);
	ArmorSlot getSlotForItem(ItemInstance const&);
	bool isDamageable(ItemInstance const&);
	void clearColor(ItemInstance&);
	bool isElytraBroken(int);
	ArmorItem* getArmorForSlot(ArmorSlot, int);
	bool isElytra(ItemInstance const&);
	void setColor(ItemInstance&, Color const&);
	int getTierItem() const;
	bool hasCustomColor(ItemInstance const&) const;
	bool isElytra() const;
};
