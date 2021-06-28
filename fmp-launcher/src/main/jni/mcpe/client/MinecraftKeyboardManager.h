#pragma once

#include <string>

class Vec2;

class MinecraftKeyboardManager
{
public:
	virtual ~MinecraftKeyboardManager();
	virtual void enableKeyboard(std::string const&, int, bool, bool, bool, Vec2 const&);
	virtual bool isFullScreenKeyboard() const;
	virtual void disableKeyboard();
	virtual void canActivateKeyboard();
	virtual bool isKeyboardEnabled() const;
	virtual bool isKeyboardActive() const;
	virtual int getKeyboardHeight() const;
public:
	MinecraftKeyboardManager();
	void setForcedHeight(float);
};
