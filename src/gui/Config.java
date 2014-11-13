package gui;

import core.BBCodeLayout;
import core.HostingService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    public static final String getCover = "getCover";
    public static final String extractFolder = "extractFolder";
    public static final String currentTemplate = "currentTemplate";
    public static final String defaultRawTemplate = "defaultRaw";
    public static final String defaultEngTemplate = "defaultEng";
    public static final String customTemplate = "custom";
    public static final String host = "host";
    private static Properties currentConfig;
    private static String configFile = "config.xml";

    public static Properties getCurrentConfig() {
        return currentConfig;
    }

    public static void loadConfig() throws IOException {
        try {
            currentConfig = new Properties();
            currentConfig.loadFromXML(new FileInputStream(configFile));
        } catch (FileNotFoundException e) {
            createDefaultConfig();
            currentConfig.loadFromXML(new FileInputStream(configFile));
        }
    }

    private static void createDefaultConfig() throws IOException {
        Properties prop = new Properties();

        // set the properties value
        prop.setProperty(getCover, "true");
        prop.setProperty(extractFolder, "false");
        prop.setProperty(currentTemplate, defaultRawTemplate);
        prop.setProperty(host, "Megaupload");

        // save properties to project root folder
        prop.storeToXML(new FileOutputStream(configFile), null);
    }

    public static void setValue(String key, String value) throws IOException {
        currentConfig.setProperty(key, value);
        currentConfig.storeToXML(new FileOutputStream(configFile), null);
    }

    public static boolean getCover() {
        String getCover = currentConfig.getProperty(Config.getCover);
        return Boolean.valueOf(getCover);
    }

    public static HostingService getHost() {
        String hostName = currentConfig.getProperty(Config.host);
        return HostingService.valueOf(hostName.toUpperCase());
    }
}
