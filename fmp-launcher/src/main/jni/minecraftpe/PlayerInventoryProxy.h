//
// Created by Gao在此 on 2020/2/17.
//

#pragma once

#include "ItemStack.h"

class PlayerInventorySlot;

enum ContainerID : unsigned char {
    ContainerIDInventory = 0,
};

class PlayerInventoryProxy {
public:
    bool add(ItemStack&, bool);
    void removeResource(ItemStack const&, bool, bool, int);
    void clearSlot(int, ContainerID id=ContainerIDInventory);
    ItemStack* getItem(int, ContainerID id=ContainerIDInventory) const;
    void setItem(int, ItemStack const&, ContainerID=ContainerIDInventory);
    PlayerInventorySlot getSelectedSlot() const;
    void selectSlot(int, ContainerID=ContainerIDInventory);
};