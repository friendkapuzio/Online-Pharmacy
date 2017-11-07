package by.bsu.machulski.resource;

import java.util.ResourceBundle;

public class ConfigurationManager {
    private static final ResourceBundle configurationBundle = ResourceBundle.getBundle("resources.config");

    public static String getPath(String key) {
        return configurationBundle.getString(key);
    }

    public static String addParameter(String path, String parameterName, String parameterValue) {
        return path + (path.contains("?") ? "&" : "?") + parameterName + "=" + parameterValue;
    }
}
