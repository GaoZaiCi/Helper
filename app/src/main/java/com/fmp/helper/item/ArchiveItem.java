package com.fmp.helper.item;

import android.graphics.Bitmap;

public class ArchiveItem {
    private String name;
    private Bitmap icon;
    private String worldPath;
    private long worldSize;
    private long behaviorSize;
    private long resourceSize;
    private int behaviorCount;
    private int resourceCount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public String getWorldPath() {
        return worldPath;
    }

    public void setWorldPath(String worldPath) {
        this.worldPath = worldPath;
    }

    public long getWorldSize() {
        return worldSize;
    }

    public void setWorldSize(long worldSize) {
        this.worldSize = worldSize;
    }

    public long getBehaviorSize() {
        return behaviorSize;
    }

    public void setBehaviorSize(long behaviorSize) {
        this.behaviorSize = behaviorSize;
    }

    public long getResourceSize() {
        return resourceSize;
    }

    public void setResourceSize(long resourceSize) {
        this.resourceSize = resourceSize;
    }

    public int getBehaviorCount() {
        return behaviorCount;
    }

    public void setBehaviorCount(int behaviorCount) {
        this.behaviorCount = behaviorCount;
    }

    public int getResourceCount() {
        return resourceCount;
    }

    public void setResourceCount(int resourceCount) {
        this.resourceCount = resourceCount;
    }
}
