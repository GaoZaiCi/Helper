//
// Created by Gao在此 on 2020/2/17.
//
#pragma once

enum AbilitiesIndex {
};

class Abilities {
public:
    bool isOperator();
    bool getBool(AbilitiesIndex) const;
    void setAbility(AbilitiesIndex, bool);
    static AbilitiesIndex nameToAbilityIndex(std::string const&);
};


