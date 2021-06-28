#pragma once

#include <memory>
#include <vector>
#include "../GuiComponent.h"
#include "BaseScreen.h"

namespace Touch { class TButton; };


class StickDirection;
class MinecraftGame;
class Button;
class TextBox;
class ClientInstance;
class GuiElement;
class MCOEvent;
class MojangConnectionStatus;
class ControllerButtonRenderer;
class ItemInstance;

// Size : 160
class Screen : public BaseScreen, public GuiComponent
{
public:
	//void* vtable; // 8
	int width; // 12
	int height; // 16
	MinecraftGame* mcGame; // 20
	ClientInstance* clientInstance; //24
	std::vector<std::shared_ptr<Button>> buttonList; // 36
	std::vector<std::shared_ptr<TextBox>> textboxList; // 48
	std::vector<std::shared_ptr<Button>> tabButtonList; // 60
	std::vector<std::shared_ptr<GuiElement>> tabElementList; // 72
	std::vector<std::shared_ptr<GuiElement>> guiElementList; // 84
	int tabButtonIndex; // 96
	int tabElementIndex; // 100
	std::unique_ptr<ControllerButtonRenderer> buttonRenderer; // 104
	Font* font; // 108
	Button* buttonClicked; // 112
	char screen_vars6[32]; // 114
public:
    virtual ~Screen();
	virtual void _init(int, int);
	virtual void setSize(int, int);
	virtual void onSetKeyboardHeight(float);
	virtual void setAssociatedBlockPos(BlockPos const&);
	virtual void onInternetUpdate();
	virtual void onFocusGained();
	virtual void onFocusLost();
	virtual void terminate();
	virtual void tick();
	virtual void tick(int, int);
	virtual void updateEvents();
	virtual void render(ScreenContext&);
	virtual void handleInputModeChanged(InputMode);
	virtual void handleHoloInputModeChanged(HoloUIInputMode);
	virtual void handleButtonPress(short, FocusImpact);
	virtual void handleButtonRelease(short, FocusImpact);
	virtual void handleRawInputEvent(int, RawInputType, ButtonState, bool);
	virtual void handlePointerLocation(PointerLocationEventData const&, FocusImpact);
	virtual void handlePointerPressed(bool);
	virtual void handleDirection(DirectionId, float, float, FocusImpact);
	virtual void handleBackEvent(bool);
	virtual void handleTextChar(std::string const&, bool, FocusImpact);
	virtual void handleCaretLocation(int, FocusImpact);
	virtual void setTextboxText(std::string const&);
	virtual void handleLicenseChanged();
	virtual void handleIdentityGained();
	virtual void handleIdentityLost();
	virtual void handleGazeGestureInput(short, float, float, float, FocusImpact);
	virtual void handleCommandEvent(VoiceCommand const&);
	virtual void renderGameBehind() const;
	virtual void absorbsInput() const;
	virtual bool closeOnPlayerHurt() const;
	virtual int getWidth();
	virtual int getHeight();
	virtual std::string getScreenName() const;
	virtual std::string getScreenNameW() const;
	virtual void toGUICoordinate(int, int, int&, int&);
	virtual void render(int, int, float);
	virtual void init();
	virtual void setupPositions();
	virtual void checkForPointerEvent();
	virtual void controllerEvent();
	virtual void renderBackground(int);
	virtual void renderDirtBackground();
	virtual void renderMenuBackground(float);
	virtual void confirmResult(bool, int);
	virtual void supppressedBySubWindow();
	virtual void onTextBoxUpdated(int);
	virtual void onMojangConnectorStatus(MojangConnectionStatus);
	virtual void handleButtonPress(short);
	virtual void handleButtonRelease(short);
	virtual void handleTextChar(std::string const&, bool);
	virtual void handleCaretLocation(int);
	virtual void handlePointerLocation(short, short);
	virtual void handleDirection(DirectionId, float, float);
	virtual void handleScrollWheel(float);
	virtual void handleGazeGestureInput(short, float, float, float);
	virtual void _handlePointerAction(int, int, bool);
	virtual void _updateTabButtonSelection();
	virtual void _buttonClicked(Button&);
	virtual void _guiElementClicked(GuiElement&);
	virtual void _pointerPressed(int, int);
	virtual void _pointerReleased(int, int);
	virtual void _controllerDirectionChanged(int, StickDirection);
	virtual void _controllerDirectionHeld(int, StickDirection);
public:
	Screen(MinecraftGame&, ClientInstance&);
	void renderToolBar(float, float, bool);
	void renderOnSelectItemNameText(int, Font*, int);
	void _tabNext();
	void _renderSlot(int, int, int, float);
	void renderSlotText(ItemInstance const&, float, float, bool, bool, bool, bool);
	void setButtonIndex(int);
	void _getCursorMoveThrottle();
	void _processControllerDirection(int);
	void _tabPrev();
	void renderProgressBar(float) const;
};
