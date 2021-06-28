#pragma once

#include <functional>
#include <memory>
#include <vector>
#include "App.h"
#include "../gamemode/GameType.h"
#include "../util/Vec3.h"
#include "Realms.h"
#include "../level/BlockSourceListener.h"
#include "../level/LevelListener.h"
#include "AppPlatformListener.h"
#include "MinecraftKeyboardManager.h"

class LevelSummary;
class AbstractScreen;
class Font;
class GameRenderer;
class GuiData;
class Screen;
class ScreenChooser;
class Minecraft;
class ClientInstance;
class Music;
class BuildActionIntention;
class Level;
class Mob;
class Dimension;
class Timer;
class Options;
class VoiceCommand;
class DirectionId;
class InputMode;
class Side;
class HolographicPlatform;
class VoiceSystem;
class Player;
class LocalPlayer;
class LevelSettings;
class SoundEngine;
class TextureAtlas;
class GameStore;
class GeometryGroup;
class SkinRepository;
class MinecraftTelemetry;
class ExternalServerFile;
class UIDefRepository;
class MobEffectsLayout;
class LevelRenderer;
class HolosceneRenderer;
class ParticleEngine;
class MinecraftInputHandler;
class MusicManager;
class LevelArchiver;
class SnoopClient;
class Entity;
class LevelRenderer;
class FocusImpact;
class TextureData;
class ResourcePacksInfoData;
class HoloUIInputMode;
class ResourcePackResponse;
class PushNotificationMessage;
class HoloGameMode;
class FilePathManager;
class Server;
class ResourcePackManager;
class ScreenshotOptions;

namespace mce
{ 
	class TextureGroup;
	class ImageBuffer;
	
}
namespace ui { class GameEventNotification; }
namespace Social 
{ 
	class UserManager; 
	class Multiplayer;
	class XboxLiveGameInfo;
	class GameConnectionInfo;
	namespace Telemetry 
	{
		class TelemetryManager;
	}
}

class MinecraftGame : public AppPlatformListener , public App , public MinecraftKeyboardManager , public BlockSourceListener , public LevelListener
{
public:
	virtual ~MinecraftGame();
	virtual void onLowMemory();
	virtual void onAppPaused();
	virtual void onAppSuspended();
	virtual void onAppResumed();
	virtual void onAppFocusLost();
	virtual void onAppFocusGained();
	virtual void onPushNotificationReceived(PushNotificationMessage const&);
	virtual void audioEngineOn();
	virtual void audioEngineOff();
	virtual void muteAudio();
	virtual void unMuteAudio();
	virtual bool useTouchscreen();
	virtual void setTextboxText(std::string const&);
	virtual void update();
	virtual void setUISizeAndScale(int, int, float);
	virtual void setRenderingSize(int, int);
	virtual void init();
	virtual void handleBack(bool);
	virtual void playerListChanged();
	virtual void canActivateKeyboard();
	virtual Minecraft* getMinecraft();
	virtual void getAutomationClient() const;
	virtual bool isDedicatedServer() const;
	virtual bool isEduMode() const;
	virtual void onActiveResourcePacksChanged(ResourcePackManager&);
	virtual void onLanguageSubpacksChanged();
	virtual void onVanillaPackDownloadComplete();
public:
	//Methods
	MinecraftGame(int, char**);
	void getPrimaryClientInstance();
	GuiData* getGuiData();
	Options* getOptions();
	void releaseMouse();
	void checkForPiracy();
	void getScreenNames();
	void getScreenStack();
	void onPlayerLoaded(Player&);
	void getToastManager();
	void joinMultiplayer(Social::GameConnectionInfo, bool);
	void setDisableInput(bool);
	void dimensionChanged();
	void onClientLevelExit(ClientInstance&);
	void requestScreenshot(ScreenshotOptions const&);
	void setClientGameMode(GameType);
	void getLocalServerLevel();
	void getMobEffectsLayout();
	void initializeTrialWorld(Player*);
	void onClientStartedLevel(std::unique_ptr<Level, std::default_delete<Level> >, std::unique_ptr<LocalPlayer, std::default_delete<LocalPlayer> >);
	void getUserAuthentication();
	void onPrepChangeDimension();
	void updateScheduledScreen();
	void onGameEventNotification(ui::GameEventNotification);
	void getResourcePacksInfoData();
	void currentScreenShouldStealMouse();
	void play(std::string const&, Vec3 const&, float, float);
	void playUI(std::string const&, float, float);
	Screen* getScreen();
	void grabMouse();
	void leaveGame(bool);
	void getEventing() const;
	void getRuneFont() const;
	void getTextures() const;
	bool allowPicking() const;
	bool isInBedScreen() const;
	bool isShowingMenu() const;
	void useController() const;
	bool getMultiplayer() const;
	int getScreenWidth() const;
	void getSoundEngine() const;
	void getUnicodeFont() const;
	void getUserManager() const;
	GameRenderer* getGameRenderer() const;
	void getMouseGrabbed() const;
	int getScreenHeight() const;
	GeometryGroup* getGeometryGroup() const;
	LevelRenderer* getLevelRenderer() const;
	ScreenChooser* getScreenChooser() const;
	int getClientRandomId() const;
	void getSkinRepository() const;
	void getForceMonoscopic() const;
	void isHoloCursorNeeded() const;
	void getCurrentInputMode() const;
	void getDevConsoleLogger() const;
	void isScreenReplaceable() const;
	void getHolosceneRenderer() const;
	void getSkinGeometryGroup() const;
	ResourcePackManager* getResourcePackManager() const;
	void getResourcePackRepository() const;
	void currentInputModeIsGamePadOrMotionController() const;
	Font* getFont() const;
	void getInput() const;
	bool isInGame() const;
	void getDpadScale();
	void pushScreen(std::shared_ptr<AbstractScreen>, bool);
	void resetInput();
	void setEduMode(bool);
	void startFrame();
	void stopSounds();
	float getGuiScale(int);
	void linkToOffer(std::string const&);
	void updateStats();
	void _reloadFancy(bool);
	void handleInvite();
	void initEventing();
	void isKindleFire(int);
	void joinLiveGame(std::string const&);
	void onUserSignin();
	void refocusMouse();
	void setDpadScale(float);
	void forEachScreen(std::function<bool (std::shared_ptr<AbstractScreen>&)>, bool);
	std::string getScreenName();
	void onUserSignout();
	void reloadShaders(bool);
	void setupRenderer(HolographicPlatform&);
	void getLevelLoader();
	void joinRealmsGame(Realms::World const&, Social::GameConnectionInfo const&);
	void updateGraphics(Timer const&);
	void isServerVisible();
	void setSuspendInput(bool);
	void setupClientGame(std::function<void (bool)>, bool);
	void getGuiScaleIndex(float);
	void getLevelArchiver();
	void sendLocalMessage(std::string const&, std::string const&);
	void startLocalServer(std::string, std::string, std::string, std::string, LevelSettings);
	void teardownRenderer();
	void calculateGuiScale(int);
	void composeScreenshot(mce::ImageBuffer&, ScreenshotOptions const&);
	void getContentManager();
	void schedulePopScreen(int);
	void _initMinecraftGame();
	void addDownloadingPack(std::string, unsigned long long);
	FilePathManager* getFilePathManager();
	void handleIdentityLost();
	void onMobEffectsChange();
	void setupCommandParser();
	void validateLocalLevel(std::string, std::string, LevelSettings);
	void getSituationalMusic();
	void initOptionObservers();
	void joinRealmFromInvite(Social::XboxLiveGameInfo const&);
	void setServerToTransfer(std::string const&, short);
	void setupLevelRendering(Level&, Entity&);
	void transformResolution(int*, int*);
	void updateFoliageColors();
	void captureScreenAsImage(mce::ImageBuffer&);
	void forEachVisibleScreen(std::function<void (std::shared_ptr<AbstractScreen>&)>, bool);
	void getUIMeasureStrategy();
	void handleIdentityGained();
	void handleLicenseChanged();
	void hasNetworkPrivileges(bool);
	void registerUpsellScreen();
	void waitAsyncSuspendWork();
	void createDynamicTextures();
	LocalPlayer* getPrimaryLocalPlayer();
	void processServerTransfer();
	void setPrimaryLocalPlayer(LocalPlayer*);
	void updateDownloadingPack(std::string, unsigned long long, bool);
	void destroyDynamicTextures();
	void getHoloscreenHalfWidth();
	void getPackManifestFactory();
	void getResourceLoadManager();
	void handleShowUpsellScreen(bool);
	void runRegionalOfferChecks();
	void setHoloscreenHalfWidth(float);
	void setPrimaryCameraEntity(Entity*);
	void getResourcePackProgress();
	void getServerNetworkHandler();
	void setKeyboardForcedHeight(float);
	void _buildVanillaClientStack(bool, bool);
	void setLaunchedFromOculusApp(bool);
	void setResourcePacksInfoData(ResourcePacksInfoData const&);
	void handleReloadUIDefinitions();
	void setProcessRegistrationKey(std::string const&);
	void getActiveDirectoryIdentity();
	void repopulatePlayScreenWorlds();
	void _registerOnInitUriListeners();
	void handleControllerTypeChanged();
	void setPrimaryCameraTargetEntity(Entity*);
	void _onActiveResourcePacksChanged(ResourcePackManager&, bool, long long);
	void _unregisterOnInitUriListeners();
	void handleResourcePackConfirmation(ResourcePackResponse, bool);
	void _deserializeGlobalResourcePacks();
	void _getGuiScaleIndexForLargeScreen(int, int);
	void _getGuiScaleIndexForSmallScreen(int, int);
	void setInhibitInputDueToTextBoxMode();
	void displayActiveDirectoryLoginDialog();
	void resetInhibitInputDueToTextBoxMode();
	void getResourcePackDownloadingProgress(std::string&);
	void _loadLastGlobalResourcePacksFromFile();
	void initFoliageAndBlockTextureTessellator();
	void _shouldUseLastPointerLocationOnFocusChange();
	void returnJoinLobbyResultBasedOnPendingRealmsInvites();
	void onTick();
	void endFrame();
	Realms* getRealms();
	void stopSound(std::string const&);
	void tickInput();
	int getUIWidth() const;
	int getUIHeight() const;
	void getUIDefRepo() const;
	bool isUserSignedIn() const;
	bool isIPv4Supported() const;
	bool isIPv6Supported() const;
	void getExternalServer() const;
	void isSRPlacementMode() const;
	void getDownloadMonitor() const;
	void getOfferRepository() const;
	bool primaryLevelExists() const;
	bool hasPendingScreenPop() const;
	LocalPlayer* getPrimaryLocalPlayer() const;
	LevelSummary* getCurrentLevelSummary() const;
	Entity* getPrimaryCameraEntity() const;
	bool isLaunchedFromOculusApp() const;
	MinecraftGame* getPrimaryClientInstance() const;
	std::string getGlobalResourcePacksPath() const;
	void* getOculusPlatformMessagePump() const;
	Entity* getPrimaryCameraTargetEntity() const;
	bool isHostingLocalDedicatedServer() const;
	void* getResourcePackDownloadManager(std::string const&) const;
	void copyInternalSettingsFolderToExternalLocation() const;
	Screen* getScreen() const;
public:
	static std::string WORLD_PATH;
	static int* GUI_SCALE_VALUES;
	static bool _hasInitedStatics;
	static std::string INTERACTION_FREQ_MS;
	static std::string RESOURCE_PACKS_SAVE_FILENAME;
};
