package com.fmp.textures;

import java.io.Serializable;
import java.util.List;

public class Manifest implements Serializable {
    /**
     * format_version : 1
     * header : {"description":"VI v1.0\nGoogle +Haoguangliang \nTwitter @haoguangliang","name":"Creeper Shaders PE §eFRT","uuid":"31528c1d-c65e-2b3f-7361-fcda2608861e","version":[1,0,0],"min_engine_version":[1,12,0]}
     * modules : [{"description":"CSPE 6.0","type":"resources","uuid":"96458fac-c34e-27e5-269f-a3c3f137a9ab","version":[1,0,0]}]
     */

    private int format_version;
    private HeaderBean header;
    private List<ModulesBean> modules;

    public int getFormat_version() {
        return format_version;
    }

    public void setFormat_version(int format_version) {
        this.format_version = format_version;
    }

    public HeaderBean getHeader() {
        return header;
    }

    public void setHeader(HeaderBean header) {
        this.header = header;
    }

    public List<ModulesBean> getModules() {
        return modules;
    }

    public void setModules(List<ModulesBean> modules) {
        this.modules = modules;
    }

    public class HeaderBean implements Serializable {
        /**
         * description : VI v1.0
         * Google +Haoguangliang
         * Twitter @haoguangliang
         * name : Creeper Shaders PE §eFRT
         * uuid : 31528c1d-c65e-2b3f-7361-fcda2608861e
         * version : [1,0,0]
         * min_engine_version : [1,12,0]
         */

        private String description;
        private String name;
        private String uuid;
        private List<Integer> version;
        private List<Integer> min_engine_version;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public List<Integer> getVersion() {
            return version;
        }

        public void setVersion(List<Integer> version) {
            this.version = version;
        }

        public List<Integer> getMin_engine_version() {
            return min_engine_version;
        }

        public void setMin_engine_version(List<Integer> min_engine_version) {
            this.min_engine_version = min_engine_version;
        }
    }

    public class ModulesBean implements Serializable {
        /**
         * description : CSPE 6.0
         * type : resources
         * uuid : 96458fac-c34e-27e5-269f-a3c3f137a9ab
         * version : [1,0,0]
         */

        private String description;
        private String type;
        private String uuid;
        private List<Integer> version;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public List<Integer> getVersion() {
            return version;
        }

        public void setVersion(List<Integer> version) {
            this.version = version;
        }
    }
}
