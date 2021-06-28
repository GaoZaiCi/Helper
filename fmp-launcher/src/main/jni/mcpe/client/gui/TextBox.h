#pragma once

#include "GuiElement.h"
#include "../settings/Options.h"

namespace Touch
{
	class TButton;
};

class Screen;

// Size : 100
class TextBox : public GuiElement
{
public:
	static const char *extendedASCII;
	static const char *identifierChars;
	static const char *numberChars;

public:
	char filler1[52];	// 48

public:
	TextBox(MinecraftGame &, const Options::Option *, const std::string &, const std::string &);
	TextBox(MinecraftGame &, const std::string &, int, const std::string &, Screen *, void (Screen::*)(int), int, bool);
	virtual ~TextBox();
	virtual void tick(MinecraftGame *);
	virtual void render(MinecraftGame *, int, int);
	virtual void topRender(MinecraftGame *, int, int);
	virtual void pointerPressed(MinecraftGame *, int, int);
	virtual void pointerReleased(MinecraftGame *, int, int);
	virtual void focusedMouseClicked(MinecraftGame *, int, int);
	virtual void focusedMouseReleased(MinecraftGame *, int, int);
	virtual void handleButtonRelease(MinecraftGame *, short);
	virtual void handleTextChar(MinecraftGame *, std::string const &, bool);
	virtual void backPressed(MinecraftGame *, bool);
	virtual void suppressOtherGUI();
	virtual void setTextboxText(std::string const &);
	virtual void hasFocus() const;
	virtual void setFocus(MinecraftGame *);
	virtual void loseFocus(MinecraftGame *);
	int getKey();
	std::string &getText() const;
	void setText(const std::string &);
	void setValidChars(const std::string &);
};
