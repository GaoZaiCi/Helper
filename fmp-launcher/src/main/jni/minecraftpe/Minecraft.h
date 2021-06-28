//
// Created by Gao在此 on 2020/2/18.
//

#pragma once

#include "Player.h"
#include "LocalPlayer.h"

class Level;
class Actor;

class Timer;
namespace mce {
    class TextureGroup;
    class Texture;
} // namespace mce
class Mob;
class Options;
class AbstractScreen;
class ScreenChooser;
class GuiData;
class MinecraftCommands;
class LevelRenderer;
class ResourcePackManager;
class EntityRenderDispatcher;
class ItemRenderer;
class UIProfanityContext;
class ServerNetworkHandler;
class SceneStack;
class SceneFactory;

enum GameType {
};

class Minecraft {
public:
    Level* getLevel() const;
    Timer* getTimer();
    MinecraftCommands* getCommands();
    ResourcePackManager* getResourceLoader();
    void setGameModeReal(GameType);
};

// actually an App::App subclass. Technically equivalent to MinecraftClient <= 1.0, but most functions moved to ClientInstance
class IMinecraftGame {
};


class IClientInstance {
};

class MinecraftGame{
public:
    LocalPlayer* getPrimaryLocalPlayer();

};
/*
class ClientInstance : public IClientInstance {
public:
    MinecraftGame* getMinecraftGame() const;
    Minecraft* getServerData();
    Player* getLocalPlayer();
    mce::TextureGroup& getTextures() const;
    void setCameraEntity(Actor*);
    void setCameraTargetEntity(Actor*);
    Options* getOptions();
    AbstractScreen* getScreen();
    ScreenChooser& getScreenChooser() const;
    GuiData* getGuiData();
    void onResourcesLoaded();
    LevelRenderer* getLevelRenderer() const;
    void play(std::string const&, Vec3 const&, float, float);
    void _startLeaveGame();
    Level* getLevel();
    EntityRenderDispatcher& getEntityRenderDispatcher();
    ItemRenderer* getItemRenderer();
    UIProfanityContext const& getUIProfanityContext() const;
    mce::Texture const& getUITexture();
    SceneStack& getClientSceneStack() const;
    SceneFactory& getSceneFactory() const;
};
*/
