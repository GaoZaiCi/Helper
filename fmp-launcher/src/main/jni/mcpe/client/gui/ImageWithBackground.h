#pragma once

#include "LargeImageButton.h"

class Textures;

// Size : 192
class ImageWithBackground : public LargeImageButton
{
public:
	char filler1[12];		// 180

public:
	ImageWithBackground(int);
	virtual ~ImageWithBackground();
	virtual void render(MinecraftGame *, int, int);
	virtual void renderBg(MinecraftGame *, int, int);
	void init(TextureGroup *, int, int, IntRectangle, IntRectangle, int, int, std::string const &);
	void setSize(float, float);
};
