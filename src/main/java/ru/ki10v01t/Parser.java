package ru.ki10v01t;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import ru.ki10v01t.service.ConfigManager;
import ru.ki10v01t.service.Package;

/**
 * Working mode for Parser
 * CONFIG - In this mode, the program parses json config file
 * PAYLOAD - In this mode, the program parses input source code file
 */
enum MODE {
    CONFIG,
    PAYLOAD
}

public class Parser {
    
    Parser(){};

    public void readMethodNames () {
        
    }

/**
 * Parses input file, in according with selected mode
 * @see MODE
 * @param filepath - the absolute path to file in filesystem. If selected mode is CONFIG, checks: whether the value is non-zero  
 * @param mode
 */
    public static void readFile(String configFilePath, MODE mode) {
        File configFile = null;
        try {
            if (configFilePath == null || configFilePath == "") {
                configFilePath = "/home/dimidrol/Documents/Workspace/git/tricks_parser/src/main/resources/config.json";
            }
            configFile = new File(configFilePath);
            if (!configFile.exists()) {
                System.out.println("Запрашиваемый конфигурационный файл не найден. Используется стандартный");
                configFilePath = "/home/dimidrol/Documents/Workspace/git/tricks_parser/src/main/resources/config.json";
            } 
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        try (FileReader fr = new FileReader(configFile);   
            BufferedReader bufReader = new BufferedReader(fr);) 
        {
            switch (mode) {
                case CONFIG:
                    ConfigManager.createConfig(configFile);
                    ConfigManager.printConfig();
                    break;
                case PAYLOAD:
                    doPayloadProcessing(bufReader);
                    break;
                default:
                    throw new ParserConfigurationException("Задан неправильный режим работы парсера");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            e.getMessage();
        }
    }

    private static void doPayloadProcessing(BufferedReader bufReader) throws IOException, ParserConfigurationException {
        if (ConfigManager.getConfig() == null) {
            throw new ParserConfigurationException("Конфиг не задан");
        }
        String line = null;
        File payloadFile = null;
        int lineNumber=0;
        ArrayList<Package> pkgArray = new ArrayList<Package>();
        while (line != null) {
            
            //pkgArray.add(e)
            line = bufReader.readLine();   
            lineNumber++;         
        }
    }
}
