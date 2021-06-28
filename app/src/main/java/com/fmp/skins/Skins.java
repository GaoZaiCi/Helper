package com.fmp.skins;

import java.io.Serializable;
import java.util.List;

public class Skins implements Serializable {
    /**
     * skins : [{"localization_name":"steve","geometry":"geometry.humanoid.custom","texture":"skin_steve.png","cape":"cape.png","type":"free"},{"localization_name":"alex","geometry":"geometry.humanoid.customSlim","texture":"skin_alex.png","cape":"capeTwo.png","type":"free"},{"localization_name":"dummy","geometry":"geometry.humanoid.custom","texture":"dummy.png","cape":"cape.png","type":"custom"}]
     * serialize_name : Standard
     * localization_name : Standard
     */

    private String serialize_name;
    private String localization_name;
    private List<SkinsBean> skins;

    public String getSerialize_name() {
        return serialize_name;
    }

    public void setSerialize_name(String serialize_name) {
        this.serialize_name = serialize_name;
    }

    public String getLocalization_name() {
        return localization_name;
    }

    public void setLocalization_name(String localization_name) {
        this.localization_name = localization_name;
    }

    public List<SkinsBean> getSkins() {
        return skins;
    }

    public void setSkins(List<SkinsBean> skins) {
        this.skins = skins;
    }

    public class SkinsBean implements Serializable {
        /**
         * localization_name : steve
         * geometry : geometry.humanoid.custom
         * texture : skin_steve.png
         * cape : cape.png
         * type : free
         */

        private String localization_name;
        private String geometry;
        private String texture;
        private String cape;
        private String type;

        public String getLocalization_name() {
            return localization_name;
        }

        public void setLocalization_name(String localization_name) {
            this.localization_name = localization_name;
        }

        public String getGeometry() {
            return geometry;
        }

        public void setGeometry(String geometry) {
            this.geometry = geometry;
        }

        public String getTexture() {
            return texture;
        }

        public void setTexture(String texture) {
            this.texture = texture;
        }

        public String getCape() {
            return cape;
        }

        public void setCape(String cape) {
            this.cape = cape;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
