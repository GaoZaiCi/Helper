//
// Created by Gao在此 on 2020/2/18.
//

#pragma once

#include "ItemStackBase.h"
#include "ItemInstance.h"
#include "BlockLegacy.h"

class ItemStack {
public:
    // this is actually the same layout as ItemInstance
    ItemStack();
    ItemStack(ItemInstance const&);
   /* ItemStack(int id, int count, int data) : ItemStack() {
        init(id, count, data);
        _setItem(id);
        *//*bool isBlock = itemIdIsBlock(id);
        // for a block, init it with a BlockAndData
        if (isBlock) {
            BlockLegacy* block = getBlockForItemId(id);
            if (!block) return;
            BlockAndData* blockAndData = block->getStateFromLegacyData(data);
            if (!blockAndData) return; // should never happen, but...
            setBlock(blockAndData);
        }*//*
    }*/
    virtual ~ItemStack();
};
