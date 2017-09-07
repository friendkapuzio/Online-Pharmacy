package by.bsu.machulski.database;

import java.util.Properties;
import java.util.ResourceBundle;

class PoolConfigurator {
    ResourceBundle bundle;
    private final String RESOURCE = "resources.database";
    private final String USER = "user";
    private final String PASSWORD = "password";
    private final String URL = "url";
    private final String USE_SSL = "useSSL";
    private final String POOL_CAPACITY = "poolcapacity";
    private final String ENCODING = "characterEncoding";

    PoolConfigurator() {
        bundle = ResourceBundle.getBundle(RESOURCE);
    }

    int getPoolCapacity() {
        return Integer.parseInt(bundle.getString(POOL_CAPACITY));
    }

    String getURL() {
        return bundle.getString(URL);
    }

    Properties getProperties() {
        Properties properties = new Properties();
        properties.setProperty(USER, bundle.getString(USER));
        properties.setProperty(PASSWORD, bundle.getString(PASSWORD));
        properties.setProperty(USE_SSL, bundle.getString(USE_SSL));
        properties.setProperty(ENCODING, bundle.getString(ENCODING));
        return properties;
    }
}
