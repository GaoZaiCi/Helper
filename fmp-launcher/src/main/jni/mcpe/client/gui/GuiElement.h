#pragma once

#include <string>

#include "GuiComponent.h"
#include "../../util/Color.h"

class StickDirection;
class MinecraftGame;
class NinePatchLayer;
class Vec2;

// Size : 48
class GuiElement : public GuiComponent
{
public:
	bool enabled;						// 4
	bool visible;						// 5
	int xPosition;						// 8
	int yPosition;						// 12
	int width;							// 16
	int height;							// 20
	Color backgroundColor;				// 24
	NinePatchLayer *backgroundLayer;	// 40
	bool selected;						// 44
	bool focused;						// 45

public:
	GuiElement(bool, bool, int, int, int, int);

	virtual ~GuiElement();
	virtual void tick();
	virtual void tick(MinecraftGame*);
	virtual void render(MinecraftGame*, int, int);
	virtual void topRender(MinecraftGame*, int, int);
	virtual void setupPositions();
	virtual void pointerPressed(MinecraftGame*, int, int);
	virtual void pointerReleased(MinecraftGame*, int, int);
	virtual void focusedMouseClicked(MinecraftGame*, int, int);
	virtual void focusedMouseReleased(MinecraftGame*, int, int);
	virtual void handleButtonPress(MinecraftGame*, short);
	virtual void handleButtonRelease(MinecraftGame*, short);
	virtual void handleTextChar(MinecraftGame*, std::string const&, bool);
	virtual void handleCaretLocation(int);
	virtual void backPressed(MinecraftGame*, bool);
	virtual void setKeyboardHeight(MinecraftGame*, float, Vec2 const&);
	virtual void pointInside(int, int);
	virtual void suppressOtherGUI();
	virtual void setTextboxText(std::string const&);
	virtual bool hasFocus() const;
	virtual void setFocus(bool);
	virtual void handleControllerDirectionHeld(int, StickDirection);
	virtual void drawSelected(int);
	virtual void drawPressed(int);
	virtual void drawSliderSelected();
	virtual void onSelectedChanged();
	virtual bool hasChildren() const;
	void clearBackground();
	bool isSelected();
	void setBackground(const Color&);
	void setBackground(MinecraftGame*, const std::string&, const IntRectangle&, int, int);
	void setVisible(bool);
	void setSelected(bool);
	void setActiveAndVisibility(bool, bool);
	void setActiveAndVisibility(bool);
};
