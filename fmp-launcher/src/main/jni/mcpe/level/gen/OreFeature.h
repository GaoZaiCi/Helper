#pragma once
#include "Feature.h"
#include "mcpe/util/BlockID.h"

class BlockSource;
class BlockPos;
class Random;

class OreFeature : public Feature
{
protected:
	BlockID id;
	unsigned char data;
	int veinSize;
public:
	OreFeature(BlockID, int);
	OreFeature(BlockID, unsigned char, int);
public:
	virtual ~OreFeature();
	virtual bool place(BlockSource&, const BlockPos&, Random&) const;
};
