//
// Created by Gao在此 on 2020/2/29.
//
#pragma once

class IClientInstance;

class MinecraftGame;

class MobileClient {
public:
    void closeConnection();

    void setLoginScreen();

    void getH5ServerUrl();

    void setLogoScreen();

    void kickOutUser(uint);

    void getH5VerUrl();

    void tearDown();

    void closeRN();

    void setNetworkGameStoreOpen(bool);

    //void onGameEventNotification(ui::GameEventNotification);
    void GetHideCustomArmorModel();

    void notifyLoginToNews();

    void setAuthServerUrl(std::string const &);

    void notifyNaviToNews();

    void set_player_name(char const *);

    void setWebServerUrl(std::string const &);

    void setUserNickName(std::string const &);

    void initAntiPlugSdk();

    void setH5ServerUrl(std::string const &);

    void setCurGameType(int);

    void startLauncher(MinecraftGame *);

    void quitLocalGame(int);

    void onGameLeaving();

    void setMomentUrl(std::string const &);

    void setCurGameId(std::string const &);

    void onAppResumed();

    void setLoginUid(std::string const &);

    void setLoginAid(std::string const &);

    void setH5VerUrl(std::string const &);

    void quitApp();

    void getEncrypedLoginToken();

    void getLoginAccount();

    void getMomentUrl();

    void getLoginSead();

    void getLoginAid();

    void SetShowSettingExperimentalGameplay(bool);

    void setNetworkGameStoreEnabled(int);

    void getNetworkGameStoreEnabled();

    void SetHideViewDistanceOption(bool);

    void SetDisableHealthAndHunger(bool);

    void SetHideCustomArmorModel(bool);

    void setEnableMagicSniffer(bool);

    void getEnableMagicSniffer();

    void setUnisdkUserInfo(std::string const &);

    void getNetworkEnabled(bool);

    void SetHidePlayerName(bool);

    void setLoginAccount(std::string const &);

    void setLoginToken(std::string const &);

    void setLoginSead(std::string const &);

    void onCloseRN();

    void unMuteMinecraftSound();

    void muteMinecraftSound();

    void getWebServerUrl();

    //void changeOrient(eScreenOrientation);

    //void postChangeOrientation(eScreenOrientation);

    void getAntiPlugResult();

    void isAnitPlugInited();

    void setAntiPlugInfo(std::string const &, std::string const &);

    void setExportRuntimeId(bool);

    void getExportRuntimeId();

    void getMincraftGame();

    void singleton();

    void getLoginUid();

    void initialize();

    void onTickUpdate();

    void checkStopEngine();

    void setPatchVersion(std::string const &);

    void getLoginToken();

    void getPatchVersion();

    void getEngineVersion();

    void getCurGameId();

    void waitStopEngine();

    void startStopEngine();

    void onGameStarted();

    void checkLeaveGame();

    bool isMobileEnable();

    void onConfigChange();

    void setMinecraftGame(MinecraftGame *);

    void checkOrientChange();

    void getResourceLoadMode();

    //void setResourceLoadMode(Netease::ResourceLoadMode);

    void waitingLeaveGame();

    void getOptions();

    void getAuthServerUrl();

    void GetHidePlayerName();

    void getNetworkGameStoreOpen();

    void getCurGameType();

    void GetHideViewDistanceOption();

    void GetShowSettingExperimentalGameplay();

    void GetDisableHealthAndHunger();

    void getExportItemId();

    void setExportItemId(bool);

};