#pragma once

#include "GuiElement.h"

class Button : public GuiElement
{
public:
	char filler [96 - 46]; // 46
	std::string msg; // 96
	int id; // 100
	bool flip; // 104
	bool pressed; // 105
	bool overrideScreenRendering; // 106

	Button(int, int, int, int, int, const std::string&, bool);
	Button(int, int, int, const std::string&);
	Button(int, const std::string&, bool);
	virtual ~Button();
	virtual void render(MinecraftGame*, int, int);
	virtual void pointerReleased(MinecraftGame*, int, int);
	virtual void drawPressed(int);
	virtual void clicked(MinecraftGame*, int, int);
	virtual void released(int, int);
	virtual void setPressed();
	virtual void setPressed(bool);
	virtual void setMsg(const std::string&);
	virtual void getYImage(bool);
	virtual void renderBg(MinecraftGame*, int, int);
	virtual void renderFace(MinecraftGame*, int, int);

	void hovered(MinecraftGame*, int, int);
	int _getWidth(MinecraftGame*, const std::string&, int);
	bool isInside(int, int);
	bool isPressed(int, int);
	bool isOveridingScreenRendering();
	void setOverrideScreenRendering(bool);
};
