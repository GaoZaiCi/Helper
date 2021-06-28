//
// Created by Gao在此 on 2020/2/18.
//

#pragma once
class BlockAndData;
class AABB;
enum CreativeItemCategory {
};

class BlockLegacy {
public:
    void** vtable; //0
    std::string nameId; // 4
    std::string mappingId; // 8
    char filler1[24-12]; // 12
    int renderLayer; //24
    char filler2[96-28]; // 28
    float destroyTime; //96
    float explosionResistance; //100
    char filler3[120-104]; // 104
    unsigned char lightOpacity; // 120 from BlockLegacy::setLightBlock
    unsigned char lightEmission; // 121 from BlockLegacy::setLightEmission
    char filler4[136-122]; // 122
    unsigned short id; // 136
    char filler5[3192-138]; // 138

    float getDestroySpeed() const;
    float getFriction() const;
    void setFriction(float);
    void setSolid(bool);
    void setCategory(CreativeItemCategory);
    std::string const& getDescriptionId() const;
    int getRenderLayer() const;
    void* getMaterial() const;
    BlockAndData* getStateFromLegacyData(unsigned short) const;
    AABB& getVisualShape(BlockAndData const&, AABB&, bool) const;
};