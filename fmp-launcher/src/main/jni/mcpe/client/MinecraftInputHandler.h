#pragma once

#include <string>
#include <vector>

class Config;
class MinecraftGame;
class MobEffectsLayout;
class InputBindingMode;
class VoiceCommandData;
class Player;
class RectangleArea;
class GamePlayInputMode;
class HoloUIInputMode;

class MinecraftInputHandler
{
public:
	virtual ~MinecraftInputHandler();
	virtual void onConfigChanged(Config const&);
public:
	MinecraftInputHandler(MinecraftGame&);
	void getCurrentInputMode() const;
	void popInputMapping(bool);
	void setDisableInput(bool);
	void setSuspendInput(bool);
	void pushInputMapping(bool);
	void resetPlayerState();
	void getHoloUIInputMode();
	void onMobEffectsChanged(MobEffectsLayout const&);
	void resetPlayerMovement();
	void updateHoloUIInputMode(HoloUIInputMode, bool);
	void clearInputDeviceQueues();
	void onControllerTypeChanged(Config const&);
	void updateInteractActiveState(bool);
	void tick();
	bool allowPicking() const;
	bool allowInteract() const;
	bool isMoving() const;
	std::string getNameId(std::string const&) const;
	void useFreeformPickDirection() const;
	void getNameRegistry();
	void setInputBindingMode(InputBindingMode);
	bool canInteract() const;
	void showBoatExit() const;
	void onToastChanged(RectangleArea const&);
	void updateInputMode(GamePlayInputMode);
	void getMoveTurnInput();
	void getBindingFactory();
	void updatePlayerState(Player const&);
	void _loadVoiceMappings(std::vector<VoiceCommandData, std::allocator<VoiceCommandData> >&);
	void getInputEventQueue();
	void _registerMenuButton(std::string const&, bool, bool);
	void _registerInputHandlers();
	void _registerVoiceMappings();
	void _registerButtonMappings();
	void _registerGamepadSpecificMappings();
	void render();
	bool isSneaking() const;
	void getCursorPos(float&, float&) const;
	bool hasMobEffects() const;
	bool isCreativeMode() const;
	void showJumpButton() const;
	std::string getBoatExitText() const;
	std::string getInteractText() const;
	void getLastGameMode() const;
	bool isMovingForward() const;
	void showSneakButton() const;
	void _interactWithItem() const;
	void getLastHoloUIMode() const;
	void _interactWithEntity() const;
	void getCurrentInputMapping() const;
	void hasToast() const;
	void canPaddle() const;
};
