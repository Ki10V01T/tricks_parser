package ru.ki10v01t;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;

import javax.xml.parsers.ParserConfigurationException;

import ru.ki10v01t.service.ConfigManager;
import ru.ki10v01t.service.InnerValuesForRegexps;


public class Parser {
    
    Parser(){};

    public void readMethodNames () {
        
    }


/**
 * Parses input file, in according with selected mode
 * @param configFilePath - the absolute path to file in filesystem. If selected mode is CONFIG, checks: whether the value is non-zero  
 */
    public void readConfig(String configFilePath) {
        File configFD = null;
        try {
            if (configFilePath == null || configFilePath == "") {
                System.out.println("Не задан конфигурационный файл. Используется стандартный");
                configFilePath = System.getProperty("user.dir") + "/src/main/resources/config.json";
            }
            configFD = new File(configFilePath);
            if (!configFD.exists()) {
                System.out.println("Запрашиваемый конфигурационный файл не найден. (Временно) Используется стандартный");
                //TODO: DEBUG
                configFilePath = System.getProperty("user.dir") + "/src/main/resources/config.json";
            } 
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
           
        ConfigManager.createConfig(configFD);

        for (String s : ConfigManager.getDeclaredInnerValuesFields())
        {
            System.out.println(s);
        }
        //TODO: DEBUG
        ConfigManager.printConfig();
    } 

    public void startProcessingPayloadFile() {
        File payloadFile = new File(ConfigManager.getPayloadInfo());
        
        try {
            if (!ConfigManager.checkingForConfigExistence()) {
                throw new ParserConfigurationException("Конфиг не задан");
            }   
        
            if (!payloadFile.exists()) {
                throw new ParserConfigurationException("Файл для парсинга не найден");
            }

            //readFromFileToString(payloadFile);
            parseFile(readFromFileToString(payloadFile)); 
            //parseFile(readFromFileToStringFIN(payloadFile)); 
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            e.getMessage();
        }
    }

    private String readFromFileToString(File payloadFD) {
        String fileData = null;
        String line = null;
        
        try(FileReader payloadFr = new FileReader(payloadFD);
            BufferedReader bufReader = new BufferedReader(payloadFr);) {
            line = (bufReader.readLine() + "\n");   
            while (line != null) {
                fileData += (line + "\n");
                line = bufReader.readLine();
            }

            if (fileData.equals("") || fileData.equals(null)) {
                throw new ParserConfigurationException("Файл для парсинга пуст");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.getMessage();
        }
        

        return fileData;
    }

    private void parseFile(String fileData) {

        ConfigManager.prepareToParse(fileData);

        
        for(InnerValuesForRegexps<Matcher> el : ConfigManager.getPrepatedTargets()) {
            int beginningFoundRegion;
            int endingFoundRegion;
            
            while(el.getMethodNameAndBody().find()) {
                beginningFoundRegion=el.getMethodNameAndBody().start();
                endingFoundRegion=el.getMethodNameAndBody().end();
                
                // System.out.println(fileData.substring(beginningFoundRegion, endingFoundRegion));
                for(Matcher target : el.getSearchTargets())
                {
                    while(target.find()) {

                    }
                    fileData.substring(beginningFoundRegion, endingFoundRegion);
                }
            }


        }
    }
}
