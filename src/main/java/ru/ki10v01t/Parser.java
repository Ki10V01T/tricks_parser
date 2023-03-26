package ru.ki10v01t;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;

import javax.xml.parsers.ParserConfigurationException;

import ru.ki10v01t.service.Package;
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

        int beginningFoundStartingRegion;
        int endingFoundStartingRegion;
        String findedMethodName = "";
        ArrayList<Package> findedPackagesList = new ArrayList<>();

        for(InnerValuesForRegexps<Matcher> el : ConfigManager.getPrepatedTargets()) {
            while(el.getMethodNameAndBody().find()) {
                beginningFoundStartingRegion=el.getMethodNameAndBody().start();
                endingFoundStartingRegion=el.getMethodNameAndBody().end();                
                // System.out.println(fileData.substring(beginningFoundRegion, endingFoundRegion));
                for(Matcher target : el.getSearchTargets())
                {
                    //target = target.region(beginningFoundStartingRegion, endingFoundStartingRegion);
                    while(target.region(beginningFoundStartingRegion, endingFoundStartingRegion).find()) {
                        //Matcher r = el.getMethodName().region(beginningFoundStartingRegion, endingFoundStartingRegion);
                        while(el.getMethodName().region(beginningFoundStartingRegion, endingFoundStartingRegion).find()) {
                            findedMethodName = fileData.substring(el.getMethodName().start(), el.getMethodName().end());
                        }
                        
                        //TODO: посмотреть момент с кавычками. Их может и не быть. Уточнить логику.
                        // Если кавычки найдены, возвращаемся в начало региона и достаём всю инфу из них.
                        if(el.getQuotationExtractor().region(target.start(), target.end()).find()) {
                            el.getQuotationExtractor().region(target.start(), target.end()).reset();

                            while(el.getQuotationExtractor().find()) {
                                findedPackagesList.add(new Package(
                                                                    linkExtraction(el, fileData),
                                                                    hashExtraction(el, fileData),
                                                                    findedMethodName));
                                
                                //fileData.substring(el.getQuotationExtractor().start(), el.getQuotationExtractor().end());
                            }
                        }
                        else {
                            findedPackagesList.add(new Package(
                                                                linkExtraction(el, fileData),
                                                                hashExtraction(el, fileData),
                                                                findedMethodName));
                        }
                        
                    }

                    //fileData.substring(beginningFoundRegion, endingFoundRegion);
                }
            }
            

        }
        findedPackagesList.toString();
    }

    private String hashExtraction(InnerValuesForRegexps<Matcher> el, String fileData) {
        String parsedHash = "";
        while(el.getHashExtractor().find()) {
            parsedHash = fileData.substring(el.getHashExtractor().start(), el.getHashExtractor().end());
        }
        return parsedHash;
    }

    private String linkExtraction(InnerValuesForRegexps<Matcher> el, String fileData) {
        String parsedLink = "";
        while(el.getLinkExtractor().find()) {
            parsedLink = fileData.substring(el.getLinkExtractor().start(), el.getLinkExtractor().end());
        }
        return parsedLink;
    }
}
