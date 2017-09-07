package by.bsu.machulski.resource;

import java.util.Locale;
import java.util.ResourceBundle;

public class MessageManager {
    private final static String PATH = "resources.messages";

    public static String getProperty(String key, String locale) {
        ResourceBundle bundle = ResourceBundle.getBundle(PATH, Locale.forLanguageTag(locale));
        return bundle.getString(key);
    }
}
