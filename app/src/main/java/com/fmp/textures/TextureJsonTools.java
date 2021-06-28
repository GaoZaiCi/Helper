package com.fmp.textures;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TextureJsonTools {
    public static Manifest getManifestClass(String json) {
        return new Gson().fromJson(json, Manifest.class);
    }

    public static String getManifestJson(Manifest manifest) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        return gson.toJson(manifest);
    }
}
