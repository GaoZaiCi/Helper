#pragma once

#include <string>
#include "GuiElementContainer.h"
#include "../settings/Options.h"

class OptionsGroup : public GuiElementContainer
{
public:
	OptionsGroup(std::string, bool);
	virtual ~OptionsGroup();
	virtual void render(MinecraftGame *, int, int);
	virtual void setupPositions();
	virtual void addOptionItem(Options::Option const &, MinecraftGame &);
	virtual void addLimitedTextBoxOptionItem(Options::Option const &, MinecraftGame &, std::string const &);
	virtual void optionalAddOptionItem(bool, Options::Option const &, MinecraftGame &);
	virtual void createToggle(Options::Option const &, MinecraftGame &);
	virtual void createProgressSlider(Options::Option const &, MinecraftGame &);
	virtual void createStepSlider(Options::Option const &, MinecraftGame &);
	void createTextBox(const Options::Option &, MinecraftGame &, const std::string &);
};
