#pragma once

#include "MaterialType.h"
#include <memory>

class Color;
// Size : 16
class Material
{
public:
	static Material *mInitialized;
	static Material *mMaterials;

public:
	char filler[25];
	
	enum Settings : int
	{
		DEFAULT
	};

public:
	Material(MaterialType, Material::Settings, float);
	~Material();
	bool isSolid() const;
	bool isLiquid() const;
	bool isType(MaterialType) const;
	bool isSuperHot() const;
	bool operator!=(Material const&) const;
	bool operator==(Material const&) const;
	bool isSolidBlocking() const;
	bool isAlwaysDestroyable() const;
	bool isReplaceable() const;
	float getTranslucency() const;
	int getBlocksMotion() const;
	int getColor() const;
	bool isFlammable() const;
	void _setMapColor(Color const&);
	void _setNotSolid();
	void _setSuperHot();
	void _setFlammable();
	void _setReplaceable();
	void _setNeverBuildable();
	void _setNotBlockingMotion();
	void _setupSurfaceMaterials();
	void _setNotAlwaysDestroyable();
	bool isNeverBuildable() const;
public:
	static Material &getMaterial(MaterialType);
	static void addMaterial(std::unique_ptr<Material, std::default_delete<Material> >);
	static void initMaterials();
	static void teardownMaterials();
};
