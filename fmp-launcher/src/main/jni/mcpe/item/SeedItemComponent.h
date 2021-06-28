#pragma once

class ItemInstance;
class Player;
class BlockPos;
class Vec3;

class SeedItemComponent
{
public:
	void useOn(ItemInstance&,Player&,BlockPos const&,signed char,Vec3 const&);
};
