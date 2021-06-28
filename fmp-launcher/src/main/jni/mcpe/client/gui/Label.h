#pragma once

#include <string>

#include "GuiElement.h"

class MinecraftGame;
class Color;

class Label : public GuiElement
{
public:
	char filler[50];
public:
	Label(MinecraftGame&,std::string const&,Color const&,int,int,int,bool,bool);
	void setWidth(int);
	void setCentered(bool);
};
