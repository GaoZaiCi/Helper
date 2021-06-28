#pragma once

class BlockState
{
public:
	BlockState();
	bool getBool(unsigned char const&) const;
	void getMask() const;
	bool isInitialized() const;
	void initState(unsigned int&, unsigned int);
public:
	enum class BlockStates : int
	{
		
	};
};
