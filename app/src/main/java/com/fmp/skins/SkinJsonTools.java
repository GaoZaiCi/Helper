package com.fmp.skins;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;

public class SkinJsonTools {

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

    public static Skins getSkinsClass(String json) {
        return new Gson().fromJson(json, Skins.class);
    }

    public static String getSkinsJson(Skins skins) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        return gson.toJson(skins);
    }

    /*修复1.16版本*/
    public static Manifest fixManifest(Manifest manifest) {
        manifest.setFormat_version(1);
        manifest.getHeader().setVersion(Arrays.asList(1, 0, 0));
        manifest.getModules().get(0).setVersion(Arrays.asList(1, 0, 0));
        return manifest;
    }

    /*修复1.16版本*/
    public static Skins fixSkins(Skins skins) {
        for (Skins.SkinsBean bean : skins.getSkins()) {
            /*if (bean.getLocalization_name().equals("steve")) {
                bean.setTexture("skin_steve.png");
                bean.setCape("cape.png");
            }*/
            if (bean.getLocalization_name().equals("Steve")) {
                bean.setLocalization_name("steve");
                bean.setTexture("skin_steve.png");
                bean.setCape("cape.png");
            }
            /*if (bean.getLocalization_name().equals("alex")) {
                bean.setTexture("skin_alex.png");
                bean.setCape("capeTwo.png");
            }*/
            if (bean.getLocalization_name().equals("Alex")) {
                bean.setLocalization_name("alex");
                bean.setTexture("skin_alex.png");
                bean.setCape("capeTwo.png");
            }
            /*if (bean.getLocalization_name().equals("dummy")) {
                bean.setTexture("dummy.png");
                bean.setCape("cape.png");
            }*/
            if (bean.getLocalization_name().equals("Dummy")) {
                bean.setLocalization_name("dummy");
                bean.setTexture("dummy.png");
                bean.setCape("cape.png");
            }
        }
        return skins;
    }
}
