#pragma once

#include "../Block.h"

class LiquidBlock : public Block
{
public:
	//virtual ~LiquidBlock();
	virtual AABB const &getAABB(BlockSource&, BlockPos const&, AABB&, int, bool, int) const;
	virtual bool checkIsPathable(Entity&, BlockPos const&, BlockPos const&) const;
	virtual void onPlace(BlockSource&, BlockPos const&) const;
	virtual bool mayPick(BlockSource&, int, bool) const;
	virtual void neighborChanged(BlockSource&, BlockPos const&, BlockPos const&) const;
	virtual int getResource(Random&, int, int) const;
	virtual int getResourceCount(Random&, int, int) const;
	virtual void handleEntityInside(BlockSource&, BlockPos const&, Entity*, Vec3&) const;
	virtual void getMobToSpawn(BlockSource&, BlockPos const&) const;
	virtual Color getMapColor(BlockSource&, BlockPos const&) const;
	virtual bool shouldRenderFace(BlockSource&, BlockPos const&, signed char, AABB const&) const;
	virtual int getColor(BlockSource&, BlockPos const&) const;
	virtual void animateTick(BlockSource&, BlockPos const&, Random&) const;
public:
	LiquidBlock(std::string const&, int, Material const&);
	float getHeightFromData(int);
	float getBrightness(BlockSource&, BlockPos const&);
	float getSlopeAngle(BlockSource&, BlockPos const&, Material const&);
	int getTickDelay(BlockSource&) const;
	void trySpreadFire(BlockSource&, BlockPos const&, Random&) const;
	void emitFizzParticle(BlockSource&, BlockPos const&) const;
	void getRenderedDepth(BlockSource&, BlockPos const&) const;
	int _getFlow(BlockSource&, BlockPos const&) const;
	float getDepth(BlockSource&, BlockPos const&) const;
	void solidify(BlockSource&, BlockPos const&, BlockPos const&) const;
};
