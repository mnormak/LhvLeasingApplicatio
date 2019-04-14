package utils;

import java.io.InputStream;
import java.util.Properties;

public class ConfigUtil {

    private Properties properties;

    public ConfigUtil() {
        properties = new Properties();
        String propertiesFileName = "config.properties";
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertiesFileName);

        try {
            properties.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPropertyValue(String property) {
        return properties.getProperty(property);
    }
}
