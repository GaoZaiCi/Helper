#pragma once

#include "LiquidBlock.h"
#include "../Block.h"

class LiquidBlockDynamic : public LiquidBlock
{
public:
	//virtual ~LiquidBlockDynamic();
	virtual void tick(BlockSource&, BlockPos const&, Random&) const;
	virtual void onPlace(BlockSource&, BlockPos const&) const;
public:
	LiquidBlockDynamic(std::string const&, int, Material const&);
	void _getSpread(BlockSource&, BlockPos const&) const;
	void _setStatic(BlockSource&, BlockPos const&) const;
	void _getHighest(BlockSource&, BlockPos const&, int, int&) const;
	bool _canSpreadTo(BlockSource&, BlockPos const&) const;
	void _trySpreadTo(BlockSource&, BlockPos const&, int) const;
	bool _isWaterBlocking(BlockSource&, BlockPos const&) const;
	void _getSlopeDistance(BlockSource&, BlockPos const&, int, int) const;
	void _spread(BlockSource&, BlockPos const&, int) const;
};
