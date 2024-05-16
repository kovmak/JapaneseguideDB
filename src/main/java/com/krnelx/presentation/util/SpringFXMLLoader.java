package com.krnelx.presentation.util;

import javafx.fxml.FXMLLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import java.io.IOException;
import java.net.URL;

public class SpringFXMLLoader {
    private final ApplicationContext context;
    private static final Logger logger = LoggerFactory.getLogger(SpringFXMLLoader.class);
    public SpringFXMLLoader(ApplicationContext context) {
        this.context = context;
    }

    public Object load(URL url) throws IOException {
        FXMLLoader loader = new FXMLLoader(url);
        loader.setControllerFactory(context::getBean);

        try {
            return loader.load();
        } catch (IOException e) {
            logger.error("Error loading FXML from URL: {}", url, e);
            throw e;
        }
    }
}
