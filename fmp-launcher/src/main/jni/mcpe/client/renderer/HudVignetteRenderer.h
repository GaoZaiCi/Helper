#pragma once

#include <memory>

class ClientInstance;
class UIControl;
class HudVignetteRenderer
{
public:
	void _renderVignette(ClientInstance&,float,int,int);
	void render(ClientInstance&,UIControl&,int,RectangleArea&);
};
