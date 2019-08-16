package com.vortex.fileattributes.domain;

import java.util.List;

public class Config {

    private List<HostConfig> hostConfigs;

    public List<HostConfig> getHostConfigs() {
        return hostConfigs;
    }

    public void setHostConfigs(List<HostConfig> hostConfigs) {
        this.hostConfigs = hostConfigs;
    }

    @Override
    public String toString() {
        return "Config{" + "hostConfigs=" + hostConfigs + '}';
    }
}
