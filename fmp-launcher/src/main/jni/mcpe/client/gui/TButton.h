#pragma once

#include "Button.h"

class NinePatchLayer;
class MinecraftGame;

namespace Touch
{
// Size : 116
class TButton : public Button
{
public:
	NinePatchLayer* unclickedNinePatchLayer;		// 108
	NinePatchLayer* clickedNinePatchLayer;			// 112
	MinecraftGame* mc;

public:
	TButton(int, int, int, int, int, std::string const &, MinecraftGame *, bool, int);
	TButton(int, int, int, std::string const &, MinecraftGame *, int);
	TButton(int, std::string const &, MinecraftGame *, bool, int);
	virtual ~TButton();
	virtual void renderBg(MinecraftGame *, int, int);
	void init(MinecraftGame *);
	void init(MinecraftGame *, std::string const &, IntRectangle const &, IntRectangle const &, int, int, int, int);
};
};
