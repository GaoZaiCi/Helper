#pragma once

#include <vector>
#include <memory>
#include "GuiElement.h"

// Size : 68
class GuiElementContainer : public GuiElement
{
public:
	char filler1[20];		// 48

public:
	GuiElementContainer(bool, bool, int, int, int, int);
	virtual ~GuiElementContainer();
	virtual void tick(MinecraftGame *);
	virtual void render(MinecraftGame *, int, int);
	virtual void topRender(MinecraftGame *, int, int);
	virtual void setupPositions();
	virtual void pointerPressed(MinecraftGame *, int, int);
	virtual void pointerReleased(MinecraftGame *, int, int);
	virtual void focusedMouseClicked(MinecraftGame *, int, int);
	virtual void focusedMouseReleased(MinecraftGame *, int, int);
	virtual void handleButtonPress(MinecraftGame *, short);
	virtual void handleButtonRelease(MinecraftGame *, short);
	virtual void handleTextChar(MinecraftGame *, std::string const &, bool);
	virtual void backPressed(MinecraftGame *, bool);
	virtual void setKeyboardHeight(MinecraftGame *, float, Vec2 const &);
	virtual void suppressOtherGUI();
	virtual void setTextboxText(std::string const &);
	virtual void hasChildren() const;
	virtual void addChild(std::shared_ptr<GuiElement>);
	virtual void removeChild(std::shared_ptr<GuiElement>);
	virtual void clearAll();
	const std::vector<std::shared_ptr<GuiElement>> &getChildren();
};
