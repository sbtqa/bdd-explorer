package ru.sbt.qa.dcb.bddexplorer.units;

import javax.xml.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SBT-Razuvaev-SV on 28.12.2016.
 */
@XmlRootElement(name = "configuration")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "guiConfigMap", "configMap" })
public class Configuration {

    @XmlElement(name = "guiConfigurationMap")
    private Map<String, String> guiConfigMap = new HashMap<>();

    @XmlElement(name = "configurationMap")
    private Map<String, String> configMap = new HashMap<>();

    public void setGuiProperty(String key, String value) {
        guiConfigMap.put(key, value);
    }

    public String getGuiProperty(String key) {
        return guiConfigMap.get(key);
    }

    public void setProperty(String key, String value) {
        configMap.put(key, value);
    }

    public String getProperty(String key) {
        return configMap.get(key);
    }

}
