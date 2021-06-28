#pragma once

#include <memory>
#include <map>
#include <vector>
#include <string>

#include "../../level/ParticleType.h"
#include "../../entity/EntityType.h"
#include "../../level/BlockSourceListener.h"
#include "../../client/AppPlatformListener.h"

class MinecraftGame;
class FrustumCuller;
class Entity;
class Block;
class BlockPos;
class BlockSource;
class FullBlock;
class GeometryLayer;
class Player;
class LevelChunk;
class TextureData;
class LevelSoundEvent;
class Vec3;
class LevelEvent;
class Matrix;
class TerrainLayer;
class Mob;
class BlockRenderer;
class BlockRenderLayer;
class Pos;
class RenderChunkRenderParameters;
class RenderChunk;
class ScreenCuller;
class EntityLocation;
class TextureAtlas;
class AABB;
class BlockID;
class BlockEntity;
class BlockEntity;

typedef int EntityRendererId;
template <typename Type>
class Boxed{};

class LevelRenderer : public BlockSourceListener , public AppPlatformListener
{
public:
	virtual ~LevelRenderer();
	virtual void onAreaChanged(BlockSource&, BlockPos const&, BlockPos const&);
	virtual void onBlockChanged(BlockSource&, BlockPos const&, FullBlock, FullBlock, int, Entity*);
	virtual void onBrightnessChanged(BlockSource&, BlockPos const&);
	virtual void allChanged();
	virtual void addParticle(ParticleType, Vec3 const&, Vec3 const&, int);
	virtual void onNewChunkFor(Player&, LevelChunk&);
	virtual void levelEvent(LevelEvent, Vec3 const&, int);
	virtual void levelSoundEvent(LevelSoundEvent, Vec3 const&, int, EntityType, bool, bool);
	virtual void takePicture(TextureData&, Entity*, Entity*, bool, std::string const&);
	virtual void onAppSuspended();
	virtual void onAppResumed();
	virtual void onLowMemory();
public:
	LevelRenderer(MinecraftGame*, std::shared_ptr<TextureAtlas>);
	void setDimension(Dimension*, bool, bool);
	void onStereoOptionChanged();
	void initializeEndcapLookupTexture(bool);
	void setDeferRenderingUntilChunksReady();
	void tick();
	void setLevel(Level*);
	float getAmbientBrightness() const;
	void renderLevel(Entity&, FrustumCuller&, FrustumCuller&, float, float);
	void setupCamera(float, int);
	void renderHitSelect(BlockSource&, BlockPos const&, float, bool);
	Vec3 computeCameraPos(float);
	void setDestroyProgress(BlockPos const&, float);
	float getFov(float, bool);
	void bobHurt(Matrix&, float);
	void bobView(Matrix&, float);
	void playSound(Entity const&, EntityLocation, std::string const&, float, float);
	void setupClearColor(float);
	AABB getCutawayBounds(Vec3&, Vec3&);
	GeometryLayer terrainRenderLayerToGeometryLayer(TerrainLayer);
	Vec3 getCameraPosFromEntity(float, Entity const&) const;
	float getNightVisionScale(Mob const&, float);
	void preRender(float);
	void startFrame(FrustumCuller&, FrustumCuller&, float, float);
	void _queueChunk(RenderChunk&, float, bool);
	void _tryRebuild(Boxed<RenderChunk>&, Vec3 const&);
	void _renderStars(float);
	void renderChunks(TerrainLayer, float, bool, bool);
	void renderClouds(float);
	void renderCracks(BlockSource&, BlockPos const&, float, float);
	void _buildSkyMesh();
	void _renderShadow(float, Vec3 const&, float);
	void renderEndCaps();
	void renderShadows(std::multimap<EntityRendererId, Entity*, std::less<EntityRendererId>, std::allocator<std::pair<EntityRendererId const, Entity*> > > const&, std::vector<BlockEntity*, std::allocator<BlockEntity*> > const&, float);
	void renderWeather(float);
	void _initResources();
	void renderEntities(float);
	void renderNameTags(float);
	void updateViewArea(Entity&, float);
	void _buildImmediate(Boxed<RenderChunk>&);
	void _buildStarsMesh();
	void _cullerIsVisible(AABB const&);
	void _renderSunOrMoon(float, bool);
	BlockRenderer* _getBlockRenderer();
	void _renderChunkQueue(RenderChunkRenderParameters const&);
	void _updateEndCapMesh();
	void playDeferredSound(std::string const&, Vec3 const&, float, float);
	void _addMaxXEndcapMesh(Vec3&, Vec3&, Vec3&);
	void _addMaxZEndcapMesh(Vec3&, Vec3&, Vec3&);
	void _addMinXEndcapMesh(Vec3&, Vec3&, Vec3&);
	void _addMinZEndcapMesh(Vec3&, Vec3&, Vec3&);
	void _buildShadowVolume();
	void _scheduleChunkSort(Boxed<RenderChunk>&);
	void moveCameraToPlayer(Matrix&, float);
	void _buildShadowOverlay();
	void _getOrCreateChunkAt(BlockPos const&, bool);
	void _renderBlockOverlay(BlockSource&, float, Block const*, BlockPos const&);
	void _renderEntityShadow(Entity const&, float);
	void _scheduleChunkBuild(Boxed<RenderChunk>&, bool);
	void _skyDarkeningFactor();
	void onViewRadiusChanged(bool, bool);
	void renderBlockEntities(bool, float);
	void renderEntityEffects(float);
	void _affectsTessellation(BlockID);
	void _blockCanHaveOverlay(Block const&);
	void _buildSunAndMoonQuad(bool, int);
	void _computeEyePositions();
	void _finishBuildingChunk(RenderChunk&);
	void _renderCracksOverlay(BlockSource&, Block const&, BlockPos const&, float);
	void _computeCutawayBounds();
	void _recreateTessellators();
	void _buildSunAndMoonMeshes();
	void _chooseOverlayMaterial(BlockRenderLayer);
	void _updateColumnVisibility(BlockPos const&);
	void updateFarChunksDistance();
	void _canHaveHighlightOverlay(Block const&);
	void _finishSortingChunkFaces(RenderChunk&);
	void _renderBlockEntityShadow(BlockEntity const&, float);
	void _cutawayVisibilityCulling(Vec3 const&, bool);
	void _frustumVisibilityCulling(Vec3 const&, bool, bool);
	void _getProjectionAspectRatio();
	void updateChunksFaceSortState(BlockPos const&, BlockPos const&, Vec3 const&);
	void _advancedVisibilityCulling(Vec3 const&, bool, ScreenCuller*);
	void _blockCanHaveCracksOverlay(Block const&, BlockSource&);
	void _determineUnderwaterStatus(Entity&);
	void _queueRenderEntitiesClient(BlockSource&, ChunkPos const&, float);
	void _queueRenderEntitiesServer(BlockSource&, ChunkPos const&, float);
	void _recalculateRenderDistance();
	void _getCameraDeltaToUpdateArea();
	void _blockCanHaveHighlightOverlay(Block const&);
	void _checkAndRenderRedHitFlashForVR(float);
	void _renderStencilShadowOverlayCube(float);
	void _advancedCutawayVisibilityCulling(Vec3 const&, bool);
	bool _IsRealityModeAndInFrontOfAperture(Vec3);
	void tickFov();
	void tickRain();
	void _setDirty(Pos const&, bool, bool);
	void _setDirty(BlockPos const&, BlockPos const&, bool, bool);
	void _setDirty(BlockPos const&, bool, bool);
	void _setupFog(Entity const&, int, float);
	void playSound(std::string const&, Vec3 const&, float, float);
	void renderSky(Entity&, float);
	bool isAABBVisible(AABB const&) const;
	bool isBlockVisible(BlockPos const&) const;
	bool _isChunkAllEmpty(RenderChunk&) const;
	float getFogBrightness(float) const;
	void _renderWaterHoles(float) const;
	int _getDimensionHeight() const;
	bool _checkPosForOneToOneClip(Vec3 const&) const;
	void _closeEnoughForImmediateRebuild(RenderChunk&, Vec3 const&) const;
public:
	static float Z_NEAR;
	static float sEndCapMaterial;
	static float Z_FAR_MIN;
};
