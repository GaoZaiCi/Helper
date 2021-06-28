#pragma once

#include "BlockEntity.h"

class Color;

class CauldronBlockEntity : public BlockEntity
{
public:
	Color getCustomColor() const;
	Color getPotionColor() const;
	Color getColor() const;
};
