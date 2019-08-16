package com.vortex.fileattributes;

import com.vortex.fileattributes.domain.Config;
import com.vortex.fileattributes.domain.HostConfig;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ConfigService {

    private static List<HostConfig> hostConfigs;

    static {
        try {
            // read a file that is in the same folder as the .jar file
            InputStream iStream = new FileInputStream(new File(getExecutionPath()).getParentFile().getAbsoluteFile() + "/config.json");
            Config cfg = new ObjectMapper().readValue(iStream, Config.class);
            hostConfigs = cfg.getHostConfigs();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getExecutionPath() {
        return ConfigService.class.getProtectionDomain().getCodeSource().getLocation().getFile();
    }

    public List<HostConfig> getHosts() {
        return hostConfigs;
    }
}
