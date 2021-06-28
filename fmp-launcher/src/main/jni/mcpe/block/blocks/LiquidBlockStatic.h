#pragma once

#include "LiquidBlock.h"
#include "../Block.h"

class LiquidBlockStatic : public LiquidBlock
{
public:
	//virtual ~LiquidBlockStatic();
	virtual void tick(BlockSource&, BlockPos const&, Random&) const;
	virtual void neighborChanged(BlockSource&, BlockPos const&, BlockPos const&) const;
public:
	LiquidBlockStatic(std::string const&, int, BlockID, Material const&);
	bool _isFlammable(BlockSource&, BlockPos const&);
	void _setDynamic(BlockSource&, BlockPos const&) const;
};
