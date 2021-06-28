//
// Created by Gao在此 on 2020/2/19.
//

#pragma once
struct Vec3 {
    float x;
    float y;
    float z;

    Vec3(float x_, float y_, float z_) : x(x_), y(y_), z(z_) {
    };

    Vec3() : x(0), y(0), z(0) {
    };
};