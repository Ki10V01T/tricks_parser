package ru.ki10v01t;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import ru.ki10v01t.service.ConfigManager;
import ru.ki10v01t.service.Method;
import ru.ki10v01t.service.Package;
import ru.ki10v01t.service.Config.InnerValuesForRegexps;


public class Parser {
    
    Parser(){};

    public void readMethodNames () {
        
    }


/**
 * Parses input file, in according with selected mode
 * @param filepath - the absolute path to file in filesystem. If selected mode is CONFIG, checks: whether the value is non-zero  
 */
    public void readConfig(String configFilePath) {
        File configFile = null;
        try {
            if (configFilePath == null || configFilePath == "") {
                System.out.println("Не задан конфигурационный файл. Используется стандартный");
                configFilePath = System.getProperty("user.dir") + "/src/main/resources/config.json";
            }
            configFile = new File(configFilePath);
            if (!configFile.exists()) {
                System.out.println("Запрашиваемый конфигурационный файл не найден. (Временно) Используется стандартный");
                //TODO: DEBUG
                configFilePath = System.getProperty("user.dir") + "/src/main/resources/config.json";
            } 
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
           
        ConfigManager.createConfig(configFile);

        //TODO: DEBUG
        ConfigManager.printConfig();
    } 

    public void startProcessingPayloadFile() {
        File payloadFile = new File(ConfigManager.getConfig().getPayloadFilePath());
        
        try {
            if (!ConfigManager.checkingForConfigExistence()) {
                throw new ParserConfigurationException("Конфиг не задан");
            }   
        
            if (!payloadFile.exists()) {
                throw new ParserConfigurationException("Файл для парсинга не найден");
            }

            readFromFileToString(payloadFile);
            //parseFile(readFromFileToString(payloadFile)); 
            //parseFile(readFromFileToStringFIN(payloadFile)); 
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            e.getMessage();
        }
    }

    private String readFromFileToString(File payloadFile) {
        String fileData = null;
        String line = null;
        
        try(FileReader payloadFr = new FileReader(payloadFile);
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

    // TODO: Автоматизировать выборку полей мапы из конфига
    private void fillsPatternsFromConfig(HashMap<String, ArrayList<Pattern>> allPatterns) {



        for (Map.Entry<String, ArrayList<Pattern>> el : allPatterns.entrySet()) {
            switch (el.getKey()) {
                case "MethodName" :
                    break;
                //case ""
            }
        }
        allPatterns.get("MethodName").add(Pattern.compile(
                ConfigManager.getConfig()
                .getRegexps()
                .get(0)
                .getMethodName(), Pattern.MULTILINE)
        );

        allPatterns.put("MethodNameAndBody", (ArrayList<Pattern>)Arrays.asList(
            Pattern.compile(
                ConfigManager.getConfig()
                .getRegexps()
                .get(0)
                .getMethodNameAndBody(), Pattern.MULTILINE)
        ));

        allPatterns.put("SearchTargetsWdownload", new ArrayList<Pattern>());
        allPatterns.put("SearchTargetsWdownloadTo", new ArrayList<Pattern>());
        for (String el : ConfigManager
                                    .getConfig()
                                    .getRegexps()
                                    .get(0)
                                    .getSearchTargets()) {
            allPatterns.get("SearchTargetsWdownload").add( 
                Pattern.compile(
                ConfigManager.getConfig()
                .getRegexps()
                .get(0)
                .getMethodNameAndBody(),Pattern.MULTILINE)
            );
        }
        
        //return allPatterns;
    }

    private <T> HashMap<String, ArrayList<T>> initMap() {
        HashMap <String, ArrayList<T>> resultMap = new HashMap<>();

        for (String field : ConfigManager.getDeclaredInnerValuesFields()) {
            resultMap.put(field, new ArrayList<T>());
        }

        return resultMap;
    }

    private void parseFile(String fileData) {
        InnerValuesForRegexps patterns = ConfigManager.getConfig().getRegexps().get(0);   

        Matcher initialFileMatcher = patterns.getMethodNameAndBody().matcher(fileData);
        
        HashMap<String, ArrayList<Matcher>> matchers = new HashMap<String, ArrayList<Matcher>>();

        matchers.putAll(Map.of("SearchTargetsWdownload", new ArrayList<Matcher>(),
         "SearchTargetsWdownloadTo", new ArrayList<Matcher>()));

        ArrayList<Method> methods = new ArrayList<Method>();
        ArrayList<Package> packages = new ArrayList<Package>();
        
        String resultOfFind = null;
        Integer inj=0;

        // Fills arrays by method names and bodies from target file
        while (initialFileMatcher.find()) {
            matchers.get("SearchTargetsWdownload")
                .add(patterns.get("SearchTargetsWdownload")
                .get(0)
                    .matcher((fileData.substring(initialFileMatcher.start(), initialFileMatcher.end())))));
            
        }

       //method.getMethodNameAndBody()));
        
        /*for (Matcher methodMatcher : methodMatchers) {
                matchers.add(createMatcherFromPattern(patterns.get(0), fileContents));                    
            break;
            case 1:
                while (m.find()) {
                    System.out.println(fileContents.substring(m.start(), m.end()));
                }
            case 2:
                    
        }
        inj++;       */     
    }
}
