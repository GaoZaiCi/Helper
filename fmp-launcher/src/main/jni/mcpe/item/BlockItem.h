#pragma once

#include "Item.h"

class BlockItem : public Item {
public:
	char filler_tileitem[50];

	BlockItem(std::string const&, int);
};
