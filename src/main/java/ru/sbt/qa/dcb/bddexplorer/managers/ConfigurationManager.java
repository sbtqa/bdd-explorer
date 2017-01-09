package ru.sbt.qa.dcb.bddexplorer.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import ru.sbt.qa.dcb.bddexplorer.units.Configuration;
import ru.sbt.qa.dcb.bddexplorer.utils.XmlConverter;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

/**
 * Created by SBT-Razuvaev-SV on 27.12.2016.
 */
@Component
@DependsOn("xmlConverter")
public class ConfigurationManager {
    private static final Logger log = LoggerFactory.getLogger(ImageManager.class);

    private static final String CONFIG_FILE = "bddexplorer.conf";

    @Autowired private XmlConverter xmlConverter;

    private Configuration configuration;

    @PostConstruct
    public void init() {
        loadConfiguration();
    }

    @PreDestroy
    public void close() {
        savePreferences();
    }

    /**
     * Load configuration from configuration file
     */
    public void loadConfiguration() {
        try {
            configuration = (Configuration) xmlConverter.xmlToObject(CONFIG_FILE);
            log.debug("Configuration imported successfully");
        } catch (Exception e) {
            log.error("Configuration import error", e);
            log.debug("Set default values");
            configuration = new Configuration();
        }
        log.debug("Configuration loading... complete");
    }

    /**
     * Save configuration to configuration file
     */
    public void savePreferences() {
        try {
            xmlConverter.objectToXML(CONFIG_FILE, configuration);
        } catch (IOException e) {
            log.error("Configuration not saved", e);
        }
    }

    public String getGuiProperty(String key) {
        return configuration.getGuiProperty(key);
    }

    public String getProperty(String key) {
        return configuration.getProperty(key);
    }

    public void setGuiProperty(String key, String value) {
        if (key != null) {
            configuration.setGuiProperty(key, value);
        }
    }

    public void setProperty(String key, String value) {
        if (key != null) {
            configuration.setProperty(key, value);
        }
    }

}
