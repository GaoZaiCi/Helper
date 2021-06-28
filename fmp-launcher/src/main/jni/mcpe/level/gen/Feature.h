#pragma once

class Entity;
class BlockSource;
class BlockPos;
class Random;

class Feature
{
public:
	bool manuallyPlaced;
	unsigned char updateNotify;
	Entity *entity;
public:
	Feature(Entity*);
public:
	virtual ~Feature();
	virtual bool place(BlockSource&, const BlockPos&, Random&) const = 0;
};

