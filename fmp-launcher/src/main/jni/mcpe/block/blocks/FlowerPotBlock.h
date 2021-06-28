#pragma once

#include "../Block.h"

class FlowerPotBlock : public Block
{
public:
	bool isSupportedBlock(Block const*, int) const;
};
