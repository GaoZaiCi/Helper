#pragma once

#include "Block.h"

class CropBlock : public Block
{
public:
	virtual bool mayPlaceOn(Block const&);
};
