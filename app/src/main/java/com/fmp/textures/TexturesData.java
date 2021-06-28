package com.fmp.textures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TexturesData implements Serializable {
    private static final long serialVersionUID = -566307762078538093L;
    private int position;
    private List<TextureItem> itemData = new ArrayList<>();

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<TextureItem> getItemData() {
        return itemData;
    }

    public void setItemData(List<TextureItem> itemData) {
        this.itemData = itemData;
    }

}
