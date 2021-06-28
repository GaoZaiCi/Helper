#pragma once

#include "GuiElement.h"
#include <memory>

namespace glm
{
	namespace detail
	{
		template <class T>
		class tvec2
		{
			public:
			T x,y;
			tvec2(T xx,T yy)
			{
				x=xx;
				y=yy;
			}
		};
	}
}

struct UIControl
{
	char filler[25];
	UIControl();
	void setEnabled(bool);
	void setVisible(bool);
	void setHover(bool);
	void setName(std::string const&);
	void setSize(glm::detail::tvec2<float>const&);
	void setPosition(glm::detail::tvec2<float>const&);
};

struct ToggleComponent : public GuiElement
{
	char filler[50];
	void setChecked(bool);
	void _setVisible(std::weak_ptr<UIControl>&,bool);
	void setDefaultState(bool) ;
	void setToggleNameId(short) ;
	void resetDefaultState(void) ;
	ToggleComponent(std::shared_ptr<UIControl>&);
	~ToggleComponent();
};
