package ru.ki10v01t;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.parsers.ParserConfigurationException;

import ru.ki10v01t.service.Package;
import ru.ki10v01t.service.ParserWorker;
import ru.ki10v01t.service.ResultsManager;
import ru.ki10v01t.service.Config;
import ru.ki10v01t.service.ConfigManager;
import ru.ki10v01t.service.InnerValuesForRegexps;


public class Parser {
    Logger log = LogManager.getLogger("Parser");

    public void readMethodNames () {
        
    }

    private Parser() {}

    public Parser(Path configFilePath) throws NullPointerException, IOException {
        readConfig(configFilePath);
    }


/**
 * Parses input file, in according with selected mode
 * @param configFilePath - the absolute path to file in filesystem. If selected mode is CONFIG, checks: whether the value is non-zero  
 */
    private void readConfig(Path configFilePath) throws NullPointerException, IOException {
        if (configFilePath == null) {
            log.info("Config file is not set. A built in file was used");
            configFilePath = Paths.get(System.getProperty("user.dir") + "/src/main/resources/config.json");
        }
        if (Files.notExists(configFilePath)) {
            log.error("A built in config file is not found. Create a new or restore from backup");
            System.exit(1);
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

    public void startProcessingPayloadFile(Path inputPayloadFilePath) throws InterruptedException, ParserConfigurationException, ExecutionException {
        Path payloadFilePath = null;
        
        if (inputPayloadFilePath != null) {
            payloadFilePath = inputPayloadFilePath;
        } else {
            log.info("Was used a defined in config file path for payload file");
            payloadFilePath = Paths.get(ConfigManager.getPayloadInfo());
        }
        
        
        if (!ConfigManager.checkingForConfigExistence()) {
            throw new ParserConfigurationException("Config is not set");
        }   

        if (Files.notExists(payloadFilePath)) {
            throw new ParserConfigurationException("File for parse is not find");
        }
        parseFile(readFromFileToString(payloadFilePath));
    }

    private StringBuilder readFromFileToString(Path payloadFD) {
        StringBuilder fileData = new StringBuilder(200);
        //String fileData = null;
        //String line = null;
        
        try(Stream<String> inputStream = Files.lines(payloadFD)) {
            fileData.append(inputStream.collect(Collectors.joining("\n")));
            //fileData = inputStream.collect(Collectors.joining("\n"));

            if (fileData.equals(null) || fileData.isEmpty()) {
                throw new ParserConfigurationException("File for parse is empty");
            }
        } catch (IOException ioe) {
            log.error(ioe.getMessage(), ioe);
            System.exit(1);
        } catch (ParserConfigurationException pce) {
            log.error(pce.getMessage(), pce);
            System.exit(1);
        }
        
        fileData.trimToSize();
        return fileData;
    }

    // private void parseFile(StringBuilder fileData) throws InterruptedException, ExecutionException {

    //     ConfigManager.prepareToParse(fileData);

    //     for(InnerValuesForRegexps<Matcher> el : ConfigManager.getPrepatedTargets()) {
    //         while(el.getMethodNameAndBody().find()) {
    //             int beginningFoundStartingRegion = el.getMethodNameAndBody().start();
    //             int endingFoundStartingRegion = el.getMethodNameAndBody().end();

    //             for(Matcher target : el.getSearchTargets())
    //             {
    //                 target = target.region(beginningFoundStartingRegion, endingFoundStartingRegion);
    //                 while(target.find()) {
    //                     //thread1
    //                     String findedMethodName = "";
    //                     Matcher targetName = el.getMethodName().region(beginningFoundStartingRegion, endingFoundStartingRegion);
    //                     while(targetName.find()) {
    //                         findedMethodName = fileData.substring(targetName.start(), targetName.end());
    //                     }

    //                     ResultsManager.addFoundedPackage(new Package(
    //                                                         linkExtraction(el, target.start(), target.end(), fileData),
    //                                                         hashExtraction(el, target.start(), target.end(), fileData),
    //                                                         findedMethodName));
    //                 }
    //             }
    //         }
    //     }
    //     ResultsManager.printFoundedPackages();
    // }

    private void parseFile(StringBuilder fileData) throws InterruptedException, ExecutionException {

        ConfigManager.prepareToParse(fileData);

        //int beginningFoundStartingRegion;
        //int endingFoundStartingRegion;
        //String findedMethodName = "";
        Deque<CompletableFuture<Boolean>> tasksList = new ArrayDeque<>();
        ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for(InnerValuesForRegexps<Matcher> el : ConfigManager.getPrepatedTargets()) {
            while(el.getMethodNameAndBody().find()) {
                ParserWorker worker = new ParserWorker(el, fileData);
                // Runnable methodNameAndBodyTask = () -> {
                    // int beginningFoundStartingRegion = el.getMethodNameAndBody().start();
                    // int endingFoundStartingRegion = el.getMethodNameAndBody().end();

                    // for(Matcher target : el.getSearchTargets())
                    // {
                    //     target = target.region(beginningFoundStartingRegion, endingFoundStartingRegion);
                    //     while(target.find()) {
                    //         //thread1
                    //         String findedMethodName = "";
                    //         Matcher targetName = el.getMethodName().region(beginningFoundStartingRegion, endingFoundStartingRegion);
                    //         while(targetName.find()) {
                    //             findedMethodName = fileData.substring(targetName.start(), targetName.end());
                    //         }

                    //         ResultsManager.addFoundedPackage(new Package(
                    //                                             linkExtraction(el, target.start(), target.end(), fileData),
                    //                                             hashExtraction(el, target.start(), target.end(), fileData),
                    //                                             findedMethodName));
                    //     }
                    // }
                // };

                //FutureTask<Void> voidMethodNameAndBodyTask = new FutureTask<>(methodNameAndBodyTask);
                tasksList.add(CompletableFuture.supplyAsync(() -> worker, threadPool));
                //CompletableFuture.runAsync(worker, threadPool);
            }
        }
        // threadPool.shutdown();
        // threadPool.awaitTermination(45, TimeUnit.MINUTES);
        for(CompletableFuture<Void> resultElement : tasksList) {
            resultElement.get();
        }

        threadPool.shutdown();
        ResultsManager.printFoundedPackages();
        //findedPackagesList.toString();
    }

    // private StringBuilder hashExtraction(InnerValuesForRegexps<Matcher> el, int regionStartId, int regionEndId, StringBuilder fileData) {
    //     StringBuilder parsedHash = new StringBuilder(15);
    //     //String parsedHash = "";
    //     Matcher targetHash = el.getHashExtractor().region(regionStartId, regionEndId);
    //     while(targetHash.find()) {
    //         parsedHash.append(fileData.substring(targetHash.start(), targetHash.end()));
    //         //parsedHash = fileData.substring(targetHash.start(), targetHash.end());
    //     }
    //     parsedHash.trimToSize();
    //     return parsedHash;
    // }

    // private StringBuilder linkExtraction(InnerValuesForRegexps<Matcher> el, int regionStartId, int regionEndId, StringBuilder fileData) {
    //     StringBuilder parsedLink = new StringBuilder(30);
    //     //String parsedLink = "";
    //     Matcher targetLink = el.getLinkExtractor().region(regionStartId, regionEndId);
    //     while(targetLink.find()) {
    //         parsedLink.append(fileData.substring(targetLink.start(), targetLink.end()));
    //         //parsedLink = fileData.substring(targetLink.start(), targetLink.end());
    //     }
    //     parsedLink.trimToSize();
    //     return parsedLink;
    // }
}
