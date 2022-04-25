package ru.ki10v01t.service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;

import ru.ki10v01t.service.Config.InnerValuesForRegexps;

public class ConfigManager {
    private static Config currentConfig = null;
    private static ObjectMapper objectMapper = null;
    private static ArrayList<String> configVars = null;

    public static void editConfig(File file) {
        createConfig(file);
    }
    
    private static void setDeclaredInnerValuesFields(InnerValuesForRegexps input) {
        if (configVars ==  null) {
            configVars = new ArrayList<String>();
        }

        Field[] tempArray = input.getClass().getDeclaredFields();
        for (int i=0; i < tempArray.length; i++) {
            configVars.add(tempArray[i].toString());
        }
    }

    public static ArrayList<String> getDeclaredInnerValuesFields() {
        return configVars;
    }

    public static void createConfig(File file) {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        if (currentConfig == null) {
            currentConfig = new Config();
            setDeclaredInnerValuesFields(currentConfig.getRegexps().get(0));
        }

        try {
            currentConfig = objectMapper.readValue(file, Config.class);
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
            System.out.printf("\n Method name: %s,\n Method Name and Body:  %s,\n Wdownload: %s,\n, WdownloadTo: %s,\n Payload file path: %s\n", 
            el.getMethodName(),
            el.getMethodNameAndBody(),
            el.printSearchTargetsWithArgs(el.getSearchTargetsWdownload()),
            el.printSearchTargetsWithArgs(el.getSearchTargetsWdownloadTo()),
            currentConfig.getPayloadFilePath());
        }
        
    }
}
