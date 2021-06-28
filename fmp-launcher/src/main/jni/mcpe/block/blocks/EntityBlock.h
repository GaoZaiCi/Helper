#pragma once

#include "../Block.h"

class EntityBlock : public Block
{
public:
	virtual void neighborChanged(BlockSource&, BlockPos const&, BlockPos const&) const;
	virtual void triggerEvent(BlockSource&, BlockPos const&, int, int) const;
public:
	EntityBlock(std::string const&, int, Material const&);
	void newBlockEntity(BlockPos const&, BlockID) const;
};
