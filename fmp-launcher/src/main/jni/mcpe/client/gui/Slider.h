#pragma once

#include <vector>
#include <memory>
#include "GuiElement.h"
#include "../settings/Options.h"

//Size : 164
class Slider : public GuiElement
{
public:
	char filler1[56];

public:
	Slider(MinecraftGame &, const Options::Option *, float, float);
	Slider(MinecraftGame &, const Options::Option *, std::vector<int,std::allocator<int>>const&);
	virtual ~Slider();
	virtual void tick(MinecraftGame *);
	virtual void render(MinecraftGame *, int, int);
	virtual void mouseClicked(MinecraftGame *, int, int, int);
	virtual void mouseRelased(MinecraftGame *, int, int, int);
	virtual void setOption(MinecraftGame *);
	void updateStepPercentage();
	void processControllerInput(MinecraftGame *, int);
};
