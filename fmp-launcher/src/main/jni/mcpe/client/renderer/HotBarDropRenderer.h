#pragma once

#include <memory>

class ClientInstance;
class UIControl;
class RectangleArea;
class HotBarDropRenderer
{
public:
	void render(ClientInstance&,UIControl&,int,RectangleArea&);
};
