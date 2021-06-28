#pragma once

#include "Button.h"
#include "ImageDef.h"

class ImageDef;

// Size : 180
class ImageButton : public Button
{
public:
	char filler1[72];		// 108

public:
	ImageButton(int, const std::string &);
	ImageButton(int, const std::string &, ImageDef );
	virtual ~ImageButton();
	virtual void render(MinecraftGame *, int, int);
	virtual void renderBg(MinecraftGame *, int, int);
	virtual void setYOffset(int);
	virtual void setupDefault();
	virtual void isSecondImage(bool);
	void setImageDef(ImageDef const &, bool);
};
