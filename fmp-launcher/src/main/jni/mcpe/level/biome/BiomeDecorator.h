#pragma once

class BlockSource;
class BlockPos;
class Random;
class Feature;

#include <memory>

class BiomeDecorator 
{
public:
	void decorateDepthSpan(BlockSource*, Random&, const BlockPos&, int, std::unique_ptr<Feature, std::default_delete<Feature>>&, int, int);
	void decorateDepthAverage(BlockSource*, Random&, const BlockPos&, int, std::unique_ptr<Feature, std::default_delete<Feature>>&, int, int);
	void decorateOres(BlockSource*, Random&, const BlockPos&);
	void decorate(BlockSource*, Random&, Biome*, BlockPos const&, bool, float);
	void _placeFeature(BlockSource*, std::unique_ptr<Feature, std::default_delete<Feature> > const&, BlockPos const&, Random&);
};
