package ru.ki10v01t;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import com.fasterxml.jackson.databind.deser.DataFormatReaders.Match;

import ru.ki10v01t.service.ConfigManager;
import ru.ki10v01t.service.Config;

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
        String line = null;
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

    private void parseFile(String fileContents) throws ParserConfigurationException {
        if (fileContents.equals("") || fileContents.equals(null)) {
            throw new ParserConfigurationException("Файл для парсинга пуст");
        }

        //System.out.println(fileContents);
        //System.exit(0);

        Pattern methodNamePattern = Pattern.compile(ConfigManager
        .getConfig()
        .getRegexps()
        .get(0)
        .getSearchTarget());
        /*Pattern methodNamePattern = Pattern.compile(ConfigManager
                                                                .getConfig()
                                                                .getRegexps()
                                                                .get(0)
                                                                .getMethodName());*/
        Pattern methodBodyPattern = Pattern.compile(ConfigManager
                                                                .getConfig()
                                                                .getRegexps()
                                                                .get(0)
                                                                .getMethodBody());
        Pattern searchTargetPattern = Pattern.compile(ConfigManager
                                                                .getConfig()
                                                                .getRegexps()
                                                                .get(0)
                                                                .getSearchTarget());                  
        
         
        /*ArrayList<Matcher> matchers = new ArrayList<Matcher>(Arrays.asList(
                                                            methodNamePattern.matcher(fileContents),
                                                            methodBodyPattern.matcher(fileContents),
                                                            searchTargetPattern.matcher(fileContents)));
        
        */
        Matcher mNameMatcher = methodNamePattern.matcher(fileContents);
        Matcher mBodyMatcher = methodBodyPattern.matcher(fileContents);
        Matcher mSearchMatcher = searchTargetPattern.matcher(fileContents);
        Integer inj=0;

        while (mNameMatcher.find()) {
            //inj++;
            String res = fileContents.substring(mNameMatcher.start(), mNameMatcher.end());
            System.out.println(res);
        } 

        /*while (mBodyMatcher.find()) {
            System.out.println(fileContents.substring(mBodyMatcher.start(), mBodyMatcher.end()));
        }

        while (mSearchMatcher.find()) {
            System.out.println(fileContents.substring(mSearchMatcher.start(), mSearchMatcher.end()));
        }*/


        /*for (Matcher m : matchers) {
            inj=0;
            while (m.find()) {
                inj++;
                System.out.println(fileContents.substring(m.start(), m.end()));
            }
        }*/
    }
}
