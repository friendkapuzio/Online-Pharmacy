package by.bsu.machulski.resource;

import java.util.ResourceBundle;

public class ConfigurationManager {
    private final static ResourceBundle configurationBundle = ResourceBundle.getBundle("resources.config");

    public static String getProperty(String key) {
        return configurationBundle.getString(key);
    }
}
