//
// Created by Gao在此 on 2020/2/17.
//

#pragma
#include <string>

class MinecraftGame;
class Minecraft;
class Player;
class Entity;

enum TextureLocation {
    TEXTURE_LOCATION_INTERNAL, // 0
};
namespace mce {
    class TextureGroup;
    class TextureGroupBase;
    class TextureDescription;
    class ResourceLocation;
    class Texture {
    public:
        bool isLoaded() const;
        TextureDescription* getDescription() const;
    };
    class TexturePtr {
    public:
        char filler[0x4]; // 0
        Texture* texture; // 4
        void* something8; // 8
        std::string textureName; // 12
        char filler2[32-16]; // 16
        TexturePtr();
        TexturePtr(TextureGroupBase&, ResourceLocation const&);
        TexturePtr(TexturePtr&&);
        ~TexturePtr();
        TexturePtr& operator=(TexturePtr&&);
        void onGroupReloaded();
        TexturePtr clone() const;
        Texture* get() const;
        static TexturePtr NONE;
    };
// TexturePtr::NONE
    class TextureGroupBase {
    public:
    };
    class TextureGroup : public TextureGroupBase {
    public:
        TexturePtr getTexture(ResourceLocation, bool);
        void loadTexture(ResourceLocation const&, bool);
        static bool mCanLoadTextures;
    };
}; // namespace mce

class Options;
class AbstractScreen;
class ScreenChooser;
class GuiData;
class LevelRenderer;
class Level;
class EntityRenderDispatcher;
class ItemRenderer;
class UIProfanityContext;
class SceneStack;
class SceneFactory;


class ClientInstance {
public:
    MinecraftGame* getMinecraftGame() const;
    Minecraft* getServerData();
    Player* getLocalPlayer();
    mce::TextureGroup& getTextures() const;
    void setCameraEntity(Entity*);
    void setCameraTargetEntity(Entity*);
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
    Level* getLocalServerLevel();
};