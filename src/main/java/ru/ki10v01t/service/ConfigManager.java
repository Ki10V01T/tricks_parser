package ru.ki10v01t.service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;


public class ConfigManager {
    private static Config currentConfig = null;
    private static ConfigServe configServe = null;
    private static ObjectMapper objectMapper = null;
    private static ArrayList<String> configFields = null;
    
    private static void setDeclaredInnerValuesFields(InnerValuesForRegexps<String> input) {
        if (configFields ==  null) {
            configFields = new ArrayList<String>();
        }

        int lastIndex;
        Field[] tempArray = input.getClass().getDeclaredFields();
        for (int i=0; i < tempArray.length; i++) {
            lastIndex = tempArray[i].toString().lastIndexOf(".");
            configFields.add(tempArray[i].toString().substring(lastIndex+1));
        }
    }

    public static ArrayList<String> getDeclaredInnerValuesFields() {
        return configFields;
    }


    public static void createConfig (File file) {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }        

        try {
            currentConfig = objectMapper.readValue(file, Config.class);
            configServe = new ConfigServe();
            //currentConfig = objectMapper.readValue(file, new TypeReference<>(){});
            setDeclaredInnerValuesFields(currentConfig.getRegexps().get(0));
            configServe.makePatterns();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String getPayloadInfo() {
        return currentConfig.getPayloadFilePath();
    }

    public static void prepareToParse(String fileData) {
        configServe.makeMatchers(fileData);
    }
    
    public static ConfigServe getConfigServe() {
        return configServe;
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
