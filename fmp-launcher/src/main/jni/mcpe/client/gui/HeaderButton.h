#pragma once

#include "TButton.h"

namespace Touch
{
// Size : 128
class HeaderButton : public TButton
{
public:
	bool isRightSided;		// 116
	int screenWidth;		// 120
	MinecraftGame* mc;	// 124

public:
	HeaderButton(int, MinecraftGame &, std::string const &, bool, bool);
	virtual ~HeaderButton();
	virtual void setMsg(std::string const &);
};
};
