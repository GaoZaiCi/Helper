#pragma once

#include "BlockEntityType.h"

#include <memory>

class BlockID;
class BlockEntity;
class BlockPos;

class BlockEntityFactory
{
public:
	static std::unique_ptr<BlockEntity> createBlockEntity(BlockEntityType, BlockPos const&, BlockID);
};

