#pragma once

class Brightness
{
public:
	static Brightness MAX;
	static Brightness MIN;

public:
	Brightness(unsigned char v):value(v){}
	float value;
};
