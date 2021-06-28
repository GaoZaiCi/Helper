#pragma once

#include "mcpe/util/ResourceLocation.h"

typedef unsigned short ushort;

struct TextureUVCoordinateSet
{
public:
	float minU;
	float minV;
	float maxU;
	float maxV;
	unsigned short width;
	unsigned short height;
	ResourceLocation res;
	int index;
public:
	TextureUVCoordinateSet();
	TextureUVCoordinateSet(float,float,float,float,ushort,ushort,ResourceLocation);
public:
	float getMinU();
	float getMinV();
	float getMaxU();
	float getMaxV();
	void setUV(float, float, float, float);
	float getInterpolatedU(float);
	float getInterpolatedV(float);
};
