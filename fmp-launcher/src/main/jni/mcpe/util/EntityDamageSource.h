#pragma once

#include <string>

class Entity;
enum class EntityDamageCause
{
	NONE=5
};
class EntityDamageSource
{
char filler[50];
public:
	EntityDamageSource(EntityDamageCause);
	virtual ~EntityDamageSource();
	virtual bool isEntitySource();
	virtual bool isTileSource();
	virtual std::string getDeathMessage(std::string, Entity *);
};
