package com.fmp.skins;

import java.io.Serializable;
import java.util.List;

public class Manifest implements Serializable {
    /**
     * format_version : 1
     * header : {"name":"Standard","uuid":"c18e65aa-7b21-4637-9b63-8ad63622ef01","version":[1,0,0]}
     * modules : [{"type":"skin_pack","uuid":"d0823c95-37e9-4ade-8663-6cd39996fac5","version":[1,0,0]}]
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
         * name : Standard
         * uuid : c18e65aa-7b21-4637-9b63-8ad63622ef01
         * version : [1,0,0]
         */

        private String name;
        private String uuid;
        private List<Integer> version;

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
    }

    public class ModulesBean implements Serializable {
        /**
         * type : skin_pack
         * uuid : d0823c95-37e9-4ade-8663-6cd39996fac5
         * version : [1,0,0]
         */

        private String type;
        private String uuid;
        private List<Integer> version;

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
