#pragma once

#include <memory>

class MinecraftGame;
class UIControl;
class NameTagRenderer
{
public:
	void render(MinecraftGame&,std::shared_ptr<UIControl>&,int,RectangleArea&);
};
