#pragma once

#include <string>
#include <vector>
#include "GuiComponent.h"
#include "../settings/IConfigListener.h"
#include "../AppPlatformListener.h"

class MobEffectsLayout;
class MinecraftGame;
class Config;
class ItemInstance;
class Tessellator;
class RectangleArea;
class GuiMessage;
typedef int ContainerID;

// Size : 3040
class GuiData : public GuiComponent, public IConfigListener, public AppPlatformListener
{
public:
	static float DropTicks;
	static float GuiScale;
	static float InvGuiScale;
	static float BUTTONS_TRANSPARENCY;

public:
	//void **vtable;					// 64
	char filler1[2520];					// 68
	MinecraftGame *mc;// 2588
    int numSlots;

public:
	GuiData(MinecraftGame &);
	virtual ~GuiData();
	virtual void onAppSuspended();
	virtual void onConfigChanged(Config const &);
	void getGuiScale();
	void addMessage(std::string const&, std::string const&, int, bool, bool);
	void handleClick();
	void setGuiScale(float);
	void clearMessages();
	void ceilToGuiScale(int);
	void getInvGuiScale();
	void onLevelGenerated();
	void clearAllTTSMessages();
	void displaySystemMessage(std::string const&);
	void updatePointerLocation(short, short);
	void setTextToSpeechEnabled(bool);
	void displayLocalizableMessage(std::string const&, std::vector<std::string, std::allocator<std::string> > const&, bool);
	void tick();
	int getNumSlots() const;
	void wasToolbarClicked() const;
	void renderSlotText(ItemInstance const*, float, float, bool, bool, bool, bool);
	void showPopupNotice(std::string const&, std::string const&);
	void floorAlignToScreenPixel(float);
	int getScreenWidth() const;
	int getScreenHeight() const;
	void getCurrentDropSlot() const;
	void getCurrentDropTicks() const;
	void getIconTex();
	void getGuiTex();
	void calcNewAlpha(float, float);
	void getInvFillMat();
	void getRcFeedbackInner();
	void getRcFeedbackOuter();
	int getPointerX() const;
	int getPointerY() const;
	float getShowProgress() const;
	void getVignette();
	void getSlotPos(int, int&, int&);
	void getAtlasTex();
	void getCursorMat();
	void _tickItemDrop();
	void _tickMessages();
	void addTTSMessage(std::string const&);
	void forceMuteChat();
	void getTipMessage(std::string&);
	void itemCountItoa(char*, int);
	void setIsChatting(bool);
	void setNowPlaying(std::string const&);
	void cubeSmoothStep(float, float, float);
	void getMessageList();
	void showTipMessage(std::string const&);
	void toggleMuteChat();
	void getCrosshairMat();
	void setShowProgress(bool);
	void _insertGuiMessage(GuiMessage&);
	std::string getNewPopupNotice(std::string&, std::string&);
	void displayChatMessage(std::string const&, std::string const&);
	std::vector<std::string> getNewChatMessages();
	void setLastSelectedSlot(int, ContainerID);
	void setTouchToolbarArea(RectangleArea const&);
	void displayClientMessage(std::string const&);
	void displayWhisperMessage(std::string const&, std::string const&);
	void setToolbarWasRendered(bool);
	void ceilAlignToScreenPixel(float);
	void getNewChatScreenMessages();
	void displayAnnouncementMessage(std::string const&, std::string);
	void flashSlot(int);
	void postError(int);
	bool isMuteChat() const;
	void getSlotIdAt(int, int) const;
	int getFlashSlotId() const;
	std::string getLastPopupText() const;
	int getLastSelectedSlot() const;
	int getFlashSlotStartTime() const;
	bool getToolbarWasRendered() const;
	int getItemNameOverlayTime() const;
	std::string getLastPopupSubtitleText() const;
	void _touchEnabledOrHolographic() const;
	bool isTouchEnabledOrHolographic() const;
};
