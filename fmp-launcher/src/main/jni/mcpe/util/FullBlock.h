#pragma once

#include "BlockID.h"

class FullBlock
{
public:
	static FullBlock AIR;

public:
	BlockID id;
	unsigned char aux;

public:
	FullBlock() : id(0), aux(0) {};
	FullBlock(BlockID tileId, unsigned char aux_) : id(tileId), aux(aux_) {}
};
