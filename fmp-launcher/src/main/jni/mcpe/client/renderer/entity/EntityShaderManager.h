#pragma once

#include "../MaterialPtr.h"

class Entity;
class Color;
class BlockSource;
class BlockPos;

// Size : 52
class EntityShaderManager
{
public:
	//void **vtable;				// 0
	

public:
	EntityShaderManager();
	virtual ~EntityShaderManager();
	virtual void getOverlayColor(Entity &, float) const;
	void _setupShaderParameters(Entity &, const Color &, const Color &, float);
	void _setupShaderParameters(Entity &, const Color &, float);
	void _setupShaderParameters(Entity &, float);
	void _setupShaderParameters(BlockSource &, const BlockPos &,float);
	void _setupShaderParameters(float, const Color &);
};
