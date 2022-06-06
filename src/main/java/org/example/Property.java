package org.example;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Property {

    private static Property property;
    private static final Properties properties = new Properties();

    private Property() {
        try {
            properties.load(new FileReader("src/main/resources/configuration.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Property getInstance() {
        if (property == null) {
            property = new Property();
        }
        return property;
    }

    public Properties getProperties() {
        return properties;
    }
}
