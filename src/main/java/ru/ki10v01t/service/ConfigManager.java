package ru.ki10v01t.service;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

enum CREATION_FLAG {
    CREATE,
    UPDATE
}

public class ConfigManager {
    private static Config currentConfig = null;
    private static ObjectMapper objectMapper = null;

    public static void manageConfig (String json, CREATION_FLAG flag) {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }

        switch (flag) {
            case CREATE:
                currentConfig = objectMapper.readValue(json, Config.class);
                break;
            case UPDATE:
                break;
            default:
                break;
        }
        
    }

    public void printConfig() {
        for (Map.Entry<String, Config.InnerValuesForRegexps> el : currentConfig.getRegexps().entrySet()) {
            System.out.printf("{%1$: %2$, %3$, %4$}",   el.getKey(),
                                                        el.getValue().getMethodName(),
                                                        el.getValue().getMethodBody(),
                                                        el.getValue().getSearchTarget());
        }
        
    }
}
