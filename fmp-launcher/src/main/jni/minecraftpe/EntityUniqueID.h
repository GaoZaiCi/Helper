//
// Created by Gao在此 on 2020/2/19.
//
#pragma once

class ActorUniqueID {
public:
    long long id;

    ActorUniqueID(long long id) : id(id) {
    }

    operator long long() const {
        return this->id;
    }
};

typedef ActorUniqueID EntityUniqueID;
