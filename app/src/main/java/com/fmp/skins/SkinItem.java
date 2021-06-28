package com.fmp.skins;

import android.graphics.Bitmap;

import java.io.Serializable;

public class SkinItem implements Serializable {
    private static final long serialVersionUID = 1740803558633987632L;

    private String name;
    private String path;
    private long size;
    private Bitmap alex;
    private Bitmap steve;
    private Bitmap cape;
    private Bitmap capeTwo;
    private String geometry;
    private String skins;
    private String manifest;

    public Bitmap getCape() {
        return cape;
    }

    public void setCape(Bitmap cape) {
        this.cape = cape;
    }

    public Bitmap getCapeTwo() {
        return capeTwo;
    }

    public void setCapeTwo(Bitmap capeTwo) {
        this.capeTwo = capeTwo;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Bitmap getAlex() {
        return alex;
    }

    public void setAlex(Bitmap alex) {
        this.alex = alex;
    }

    public Bitmap getSteve() {
        return steve;
    }

    public void setSteve(Bitmap steve) {
        this.steve = steve;
    }

    public String getGeometry() {
        return geometry;
    }

    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }

    public String getSkins() {
        return skins;
    }

    public void setSkins(String skins) {
        this.skins = skins;
    }

    public String getManifest() {
        return manifest;
    }

    public void setManifest(String manifest) {
        this.manifest = manifest;
    }
}
