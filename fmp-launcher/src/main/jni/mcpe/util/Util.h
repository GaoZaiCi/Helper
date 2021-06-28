#pragma once

#include <vector>
#include <string>
#include <sstream>

class Util
{
public:
	static std::string EMPTY_STRING;
	static std::string EMPTY_GUID;
public:
	static std::vector<std::string> split(const std::string &, char);
	static void splitString(std::string const &, char, std::vector<std::string> &);
	static std::string toFloatString(double value)
	{
		std::stringstream stm;
		stm<<value;
		std::string str;
		stm>>str;
		return str;
	}
	static std::string toString(float);
	static std::string toNiceString(int);
	static std::string toLower(const std::string &);
};
