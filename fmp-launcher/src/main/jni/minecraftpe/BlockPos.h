//
// Created by Gao在此 on 2020/2/19.
//

#pragma once
struct BlockPos {
    int x;
    int y;
    int z;
    BlockPos(BlockPos const& other) : x(other.x), y(other.y), z(other.z) {
    }
    BlockPos(): x(0), y(0), z(0) {
    }
    BlockPos(int x_, int y_, int z_): x(x_), y(y_), z(z_) {
    }
};
