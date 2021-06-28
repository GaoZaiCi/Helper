#pragma once

#include <string>
#include <functional>
#include <vector>
#include "../AppPlatformListener.h"

class Options;
class Color;
class Textures;
namespace mce
{
	class MaterialPtr;
	class TextureGroup;
}
// Size : 2304
class Font : public AppPlatformListener
{
public:
	class GlyphQuad;
public:
	virtual ~Font();
	virtual void onAppSuspended();
public:
	Font(std::string const&, mce::TextureGroup*);
	void clearCache();
	void getGlyphLocations();
	void init();
	int getLineLength(std::string const&, float, bool) const;
	void drawWordWrap(std::string const&, float, float, float, Color const&, bool, bool, bool, mce::MaterialPtr*);
	int getTextHeight(std::string const&);
	int getTextHeight(std::string const&, int, bool);
	void draw(std::string const&, float, float, Color const&, bool, mce::MaterialPtr*, int);
	void drawTransformed(std::string const&, float, float, Color const&, float, float, bool, float);
	void drawCached(std::string const&, float, float, Color const&, bool, bool, mce::MaterialPtr*, int);
	void drawShadow(std::string const&, float, float, Color const&, bool, mce::MaterialPtr*);
	void _chopString(std::string&, float&, unsigned int&, float, bool, bool, std::function<bool (std::string const&, float, unsigned int&)>);
	void resetFormat(Color const&);
	void _drawWordWrap(std::string const&, float, float, float, Color const&, unsigned int, bool, bool, bool, mce::MaterialPtr*);
	void setCaretColor(Color const&);
	void _makeTextObject(std::string const&, Color const&, bool, int);
	void _drawTextSegment(std::string const&, float, float, Color const&, bool, bool, bool, mce::MaterialPtr*);
	void onLanguageChanged(std::string const&);
	void processLinesInBox(std::string const&, float, bool, std::function<bool (std::string const&, float, unsigned int&)>);
	void _processHeightWrap(std::string const&, float, float, float, unsigned int, bool, bool, std::function<bool (std::string const&, float, unsigned int&)>);
	void getFormattingCodes(std::string const&);
	void drawWordWrapMaxLine(std::string const&, float, float, float, Color const&, int, bool, bool, bool, mce::MaterialPtr*);
	void _getStringChopAmount(std::string const&, bool, float);
	void _findNextWordPosition(std::string const&, unsigned int, char, unsigned int&, unsigned int&);
	void _willTextFitOnNextLine(std::string const&, bool, float);
	void tickObfuscatedTextIndex(float);
	void _containsObfuscatedFormatting(std::string const&);
	void _draw(std::string const&, float, float, Color const&, bool, bool, mce::MaterialPtr*, int);
	void _buildChar(std::vector<Font::GlyphQuad, std::allocator<Font::GlyphQuad> >&, int, Color const&, bool, float, float, bool) const;
	void _getCharWidth(int, bool) const;
	void _containsWideChar(std::string const&) const;
	void hasFormattingCodes(std::string const&) const;
	void _getTextureForSheet(int, bool) const;
	void _containsUnicodeChar(std::string const&) const;
	void _scanUnicodeCharacterWidth(int, int, int, bool) const;
};
