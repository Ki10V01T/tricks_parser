package ru.ki10v01t;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.parsers.ParserConfigurationException;

import ru.ki10v01t.service.Package;
import ru.ki10v01t.service.ResultsManager;

import ru.ki10v01t.service.ConfigManager;
import ru.ki10v01t.service.InnerValuesForRegexps;


public class Parser {
    Logger log = LogManager.getLogger("Parser");
    Parser(){};

    public void readMethodNames () {
        
    }


/**
 * Parses input file, in according with selected mode
 * @param configFilePath - the absolute path to file in filesystem. If selected mode is CONFIG, checks: whether the value is non-zero  
 */
    public void readConfig(Path configFilePath) {
        try {
            if (configFilePath == null) {
                log.info("Config file is not set. A built in file was used");
                configFilePath = Paths.get(System.getProperty("user.dir") + "/src/main/resources/config.json");
            }
            if (Files.notExists(configFilePath)) {
                log.error("A built in config file is not found. Create a new or restore from backup");
                System.exit(1);
            } 
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
           
        ConfigManager.createConfig(configFilePath.toFile());

        //TODO: DEBUG
        ConfigManager.printConfig();
    } 

    private void printDeclaredConfigClassFields() {
        for (String s : ConfigManager.getDeclaredInnerValuesFields())
        {
            System.out.println(s);
        }
    }

    public void startProcessingPayloadFile(Path inputPayloadFilePath) {
        Path payloadFilePath = null;
        
        if (inputPayloadFilePath != null) {
            payloadFilePath = inputPayloadFilePath;
        } else {
            log.info("Was used a defined in config file path for payload file");
            payloadFilePath = Paths.get(ConfigManager.getPayloadInfo());
        }
        
        try {
            if (!ConfigManager.checkingForConfigExistence()) {
                throw new ParserConfigurationException("Конфиг не задан");
            }   
        
            if (Files.notExists(payloadFilePath)) {
                throw new ParserConfigurationException("Файл для парсинга не найден");
            }
            parseFile(readFromFileToString(payloadFilePath));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            e.getMessage();
        }
    }

    private String readFromFileToString(Path payloadFD) {
        String fileData = null;
        //String line = null;
        
        try(Stream<String> inputStream = Files.lines(payloadFD)) {
            fileData = inputStream.collect(Collectors.joining("\n"));

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

        for(InnerValuesForRegexps<Matcher> el : ConfigManager.getPrepatedTargets()) {
            while(el.getMethodNameAndBody().find()) {
                beginningFoundStartingRegion=el.getMethodNameAndBody().start();
                endingFoundStartingRegion=el.getMethodNameAndBody().end();

                for(Matcher target : el.getSearchTargets())
                {
                    target = target.region(beginningFoundStartingRegion, endingFoundStartingRegion);
                    while(target.find()) {
                        //thread1
                        Matcher targetName = el.getMethodName().region(beginningFoundStartingRegion, endingFoundStartingRegion);
                        while(targetName.find()) {
                            findedMethodName = fileData.substring(targetName.start(), targetName.end());
                        }

                        ResultsManager.addPackage(new Package(
                                                            linkExtraction(el, target.start(), target.end(), fileData),
                                                            hashExtraction(el, target.start(), target.end(), fileData),
                                                            findedMethodName));
                    }
                }
            }
        }
        ResultsManager.printFoundedPackages();
        //findedPackagesList.toString();
    }

    private String hashExtraction(InnerValuesForRegexps<Matcher> el, int regionStartId, int regionEndId, String fileData) {
        String parsedHash = "";
        Matcher targetHash = el.getHashExtractor().region(regionStartId, regionEndId);
        while(targetHash.find()) {
            parsedHash = fileData.substring(targetHash.start(), targetHash.end());
        }
        return parsedHash;
    }

    private String linkExtraction(InnerValuesForRegexps<Matcher> el, int regionStartId, int regionEndId, String fileData) {
        String parsedLink = "";
        Matcher targetLink = el.getLinkExtractor().region(regionStartId, regionEndId);
        while(targetLink.find()) {
            parsedLink = fileData.substring(targetLink.start(), targetLink.end());
        }
        return parsedLink;
    }
}
