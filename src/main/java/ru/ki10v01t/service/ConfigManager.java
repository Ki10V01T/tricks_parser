package ru.ki10v01t.service;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConfigManager {
    private static Config currentConfig = null;
    private static ObjectMapper objectMapper = null;

    public static void editConfig(File file) {
        createConfig(file);
    }

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

    }

    public static Config getConfig() {
        return currentConfig;
    }

    public static Boolean checkingForConfigExistence() {
        if (getConfig() == null) {
            return false;
        }
        else {return true;}
    }

    public static void printConfig() {
        for (Config.InnerValuesForRegexps el : currentConfig.getRegexps()) {
            System.out.printf("\n Method name: %s,\n Method Name and Body:  %s,\n SearchTargets: %s,\n Payload file path: %s\n", 
            el.getMethodName(),
            el.getMethodNameAndBody(),
            el.printSearchTargetsWithArgs(),
            currentConfig.getPayloadFilePath());
        }
        
    }
}
