#pragma once

#include "BaseContainerMenu.h"

class Container;

// Size : 32
class InventoryMenu : public BaseContainerMenu
{
public:
	Container *container;	// 28

public:
	InventoryMenu(Container *);
	virtual ~InventoryMenu();
	virtual ItemInstance* getItems();
	void setSlot(int, ItemInstance const&);
	ItemInstance* getSlot(int);
};
