package ru.ki10v01t;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import ru.ki10v01t.service.ConfigManager;
import ru.ki10v01t.service.Method;
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
 * @param filepath - the absolute path to file in filesystem. If selected mode is CONFIG, checks: whether the value is non-zero  
 * @param mode
 */
    public void readConfig(String configFilePath) {
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
            ConfigManager.createConfig(configFile);
            ConfigManager.printConfig();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

            parseFile(readFromFileToString(payloadFile)); 
            //parseFile(readFromFileToStringFIN(payloadFile)); 
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            e.getMessage();
        }
    }

    private String readFromFileToStringFIN(File payloadFile) {
        String fileContents = null;
        try(//FileReader payloadFr = new FileReader(payloadFile);
        FileInputStream payloadFr=new FileInputStream(ConfigManager.getConfig().getPayloadFilePath());
            //BufferedReader bufReader = new BufferedReader(payloadFr);
            ) {
            int i = -1; 
            //fileContents = payloadFr.read();
            while ((i=payloadFr.read())!=-1) {
                //line = bufReader.readLine();
                fileContents += (char)i;    
                //fileContents += line;    
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContents;
    }

    private String readFromFileToString(File payloadFile) {
        String fileContents = null;
        String line = null;
        try(FileReader payloadFr = new FileReader(payloadFile);
            BufferedReader bufReader = new BufferedReader(payloadFr);) {
            fileContents = line = (bufReader.readLine() + "\n");   
            //fileContents = line = (bufReader.readLine());  
            while (line != null) {
                line = bufReader.readLine();
                fileContents += (line + "\n");    
                //fileContents += line;    
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContents;
    }

    private HashMap<String, ArrayList<Pattern>> createPatternsFromConfig() {
        HashMap<String, ArrayList<Pattern>> allPatterns = new HashMap<>();

        allPatterns.put("MethodName", (ArrayList<Pattern>)Arrays.asList(
            Pattern.compile(
                ConfigManager.getConfig()
                .getRegexps()
                .get(0)
                .getMethodName(), Pattern.MULTILINE)
        ));

        allPatterns.put("MethodNameAndBody", (ArrayList<Pattern>)Arrays.asList(
            Pattern.compile(
                ConfigManager.getConfig()
                .getRegexps()
                .get(0)
                .getMethodNameAndBody(), Pattern.MULTILINE)
        ));

        allPatterns.put("SearchTargets", new ArrayList<Pattern>());
        for (String el : ConfigManager
                                    .getConfig()
                                    .getRegexps()
                                    .get(0)
                                    .getSearchTarget()) {
            allPatterns.get("SearchTargets").add( 
                Pattern.compile(
                ConfigManager.getConfig()
                .getRegexps()
                .get(0)
                .getMethodNameAndBody(),Pattern.MULTILINE)
            );
        } 
        
        return allPatterns;
    }

    private HashMap<String, ArrayList<Matcher>> createMatchers(ArrayList<Pattern> patterns, String fileContents) {
        HashMap<String, ArrayList<Matcher>> methodMatchers = new HashMap<String, ArrayList<Matcher>>();
        ArrayList<Matcher> matchers = new ArrayList<>(); 
        methodMatchers.put("MethodName", (ArrayList<Matcher>)Arrays.asList(patterns.get(0).matcher(fileContents)));
        methodMatchers.put("MethodNameAndBody", (ArrayList<Matcher>)Arrays.asList(patterns.get(1).matcher(fileContents)));
        for (int i=2; i<patterns.size(); i++) {
            matchers.add(patterns.get(i).matcher())
            methodMatchers.put("SearchTargets", )
        }

       
    }

    private void parseFile(String fileContents) throws ParserConfigurationException {
        if (fileContents.equals("") || fileContents.equals(null)) {
            throw new ParserConfigurationException("Файл для парсинга пуст");
        }

        HashMap<String, ArrayList<Pattern>> patterns = createPatternsFromConfig(); 

        // Matcher for searching method names and bodies  
        Matcher fileMatcher = patterns.get(0).matcher(fileContents);          
        
        HashMap<String, ArrayList<Matcher>> methodMatchers = createMatchers(patterns);
        
        methodMatchers.put("SearchTargets", (ArrayList<Matcher>)Arrays.asList(patterns.get(0).matcher(fileContents)))
        ArrayList<Method> methods = new ArrayList<Method>();
        ArrayList<Package> packages = new ArrayList<Package>();
        Integer inj=0;

        // Fills arrays by method names and bodies
        while (fileMatcher.find()) {
            Method method = new Method();
            method.setMethodNameAndBody(fileContents.substring(fileMatcher.start(), fileMatcher.end()));
            methods.add(method);
            methodMatchers.add(Methodpatterns.get(0).matcher(method.getMethodNameAndBody()));
        }
        
        for (Matcher methodMatcher : methodMatchers)



                matchers.add(createMatcherFromPattern(patterns.get(0), fileContents));                    
            break;
            case 1:
                while (m.find()) {
                    System.out.println(fileContents.substring(m.start(), m.end()));
                }
            case 2:
                    
        }
        inj++;            
    }
}
