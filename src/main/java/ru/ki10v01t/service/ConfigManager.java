package ru.ki10v01t.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;

import com.fasterxml.jackson.databind.ObjectMapper;


public class ConfigManager {
    private static Config currentConfig = null;
    private static ConfigMaintainer maintainer = null;
    private static ObjectMapper objectMapper = null;


    public static void createConfig (File configFD) throws IOException {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }        
        
        currentConfig = objectMapper.readValue(configFD, Config.class);
        maintainer = new ConfigMaintainer();
        //currentConfig = objectMapper.readValue(file, new TypeReference<>(){});
        //setDeclaredInnerValuesFields(currentConfig.getRegexps().get(0));
        maintainer.makePatterns();
    }

    public static String getPayloadInfo() {
        return currentConfig.getPayloadFilePath();
    }

    public static void prepareToParse(StringBuilder fileData) {
        maintainer.makeMatchers(fileData);
    }
    
    public static ArrayList<InnerValuesForRegexps<Matcher>> getPrepatedTargets() {
        return maintainer.getMatchers();
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
            currentConfig.toString();
        }
}
