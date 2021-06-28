//
// Created by Gao在此 on 2020/2/17.
//
#pragma once
class PlayerInventoryProxy;
class Actor;

class Mob{
public:
    void sendInventory(bool);
};

class Player: public Mob {
public:
    int getScore();
    void addExperience(int);
    void addLevels(int);
    PlayerInventoryProxy* getSupplies() const;
    int getHealth() ;//获取生命值
    bool getPlayerPermissionLevel();
    bool canUseOperatorBlocks();
    bool isOperator();
    void attack(Actor&) const;//攻击实体
    std::string getName() const;
    void sendInventory(bool);//发送背包
};
