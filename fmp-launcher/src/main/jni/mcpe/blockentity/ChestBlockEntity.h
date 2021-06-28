#pragma once

#include "BlockEntityType.h"

class ChestBlockEntity : public BlockEntity
{
public:
	ChestBlockEntity();
	~ChestBlockEntity();
	
	ItemInstance*getItem(int)const;
	void setItem(int,ItemInstance*);
};
