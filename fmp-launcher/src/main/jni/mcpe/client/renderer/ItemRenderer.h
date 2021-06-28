#pragma once

#include "entity/EntityRenderer.h"
#include "ItemRenderChunkType.h"

class Textures;
class ItemInstance;
class TextureUVCoordinateSet;
class Tessellator;

class ItemRenderer : public EntityRenderer
{
public:
	static ItemRenderer *instance;
public:
	ItemRenderer();
	virtual ~ItemRenderer();
	virtual void render(Entity &, const Vec3 &, float, float);
	void renderGuiItemCorrect(Font *, Textures *, const ItemInstance &, int, int);
	void renderGuiItemDecorations(ItemInstance const&,float,float); 
	void renderGuiItemInChunk(ItemRenderChunkType, const ItemInstance &, float, float, float, float, float,int,bool);
	void renderGuiItemNew(ItemInstance const&,int,float,float,float,float,float,bool); 
	void blit(float, float, float, float, float, float);
	void iconBlit(float, float, const TextureUVCoordinateSet &, float, float, float, float, int, float);
	void fillRect(Tessellator &, float, float, float, float, int);
	int getAtlasPos(const ItemInstance &);
	static ItemRenderer *getInstance();
};
