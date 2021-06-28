#pragma once

#include "GuiElementContainer.h"

class ScrollingPane;

// Size : 112
class PackedScrollContainer : public GuiElementContainer
{
public:
	char filler1[44];		// 68
public:
	PackedScrollContainer(bool, bool);
	virtual ~PackedScrollContainer();
	virtual void tick(MinecraftGame *);
	virtual void render(MinecraftGame *, int, int);
	virtual void setupPositions();
	virtual void pointerPressed(MinecraftGame *, int, int);
	virtual void setTextboxText(std::string const &);
};
