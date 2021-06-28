#pragma once

class BlockSource;
class Entity;

class Explosion
{
public:
	Explosion(BlockSource &, Entity *, float, float, float, float);
	static void explode();
	void finalizeExplosion();
};
