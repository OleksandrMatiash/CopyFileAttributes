package com.vortex.fileattributes.domain;

public class HostConfig {

    private String alias;
    private String host;
    private String iconsDbName;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getIconsDbName() {
        return iconsDbName;
    }

    public void setIconsDbName(String iconsDbName) {
        this.iconsDbName = iconsDbName;
    }

    @Override
    public String toString() {
        return "HostConfig{" +
                "alias='" + alias + '\'' +
                ", host='" + host + '\'' +
                ", iconsDbName='" + iconsDbName + '\'' +
                '}';
    }
}
