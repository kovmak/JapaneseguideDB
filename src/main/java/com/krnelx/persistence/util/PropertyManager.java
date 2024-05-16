package com.krnelx.persistence.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.springframework.stereotype.Component;

@Component
public final class PropertyManager {

    private static final Properties PROPERTIES = new Properties();

    public PropertyManager() {
        loadProperties();
    }

    public static String get(String key){
        return PROPERTIES.getProperty(key);
    }

    private void loadProperties(){
        try (InputStream applicationProperties = PropertyManager.class.getClassLoader()
            .getResourceAsStream("application.properties")) {
            PROPERTIES.load(applicationProperties);
        } catch (IOException e){
            //LOGGER.error("failed to read properties. %s".formatted(e));
            throw new RuntimeException(e);
        }
    }

    public PropertyManager(InputStream applicationProperties) {
        try (applicationProperties) {
            PROPERTIES.load(applicationProperties);
        } catch (IOException e) {
            // LOGGER.error("failed to read properties. %s".formatted(e));
            throw new RuntimeException(e);
        }
    }
}
