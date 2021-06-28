#pragma once

#include <string>
#include <vector>

#include "mcpe/item/ItemInstance.h"

class FurnaceRecipes
{
public:
	FurnaceRecipes();
	void addFurnaceRecipe(int, ItemInstance const&);
	void addFurnaceRecipeAuxData(short, short, ItemInstance const&);
	bool isFurnaceItem(ItemInstance const&) const;
	ItemInstance getResult(ItemInstance const&) const;
	void clearFurnaceRecipes();
	void teardownFurnaceRecipes();
	void init();
	void _init();
public:
	static FurnaceRecipes * mInstance;
	static FurnaceRecipes * getInstance();
};
