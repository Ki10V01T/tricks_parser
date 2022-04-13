package ru.ki10v01t.service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConfigManager {
    private static Config currentConfig = null;
    private static ObjectMapper objectMapper = null;

    public static void createConfig(File file) {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        if (currentConfig == null) {
            currentConfig = new Config();
        }

        try {
            currentConfig = objectMapper.readValue(file, Config.class);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    };
    public static Config getConfig() {
        return currentConfig;
    }
    public static void printConfig() {
        for (Map.Entry<String, Config.InnerValuesForRegexps> el : currentConfig.getRegexps().entrySet()) {
            System.out.printf("{%1$: %2$, %3$, %4$}",   el.getKey(),
                                                        el.getValue().getMethodName(),
                                                        el.getValue().getMethodBody(),
                                                        el.getValue().getSearchTarget());
        }
        
    }
}
