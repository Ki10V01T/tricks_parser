package ru.ki10v01t;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import ru.ki10v01t.service.ResultsManager;
import ru.ki10v01t.service.Package;


public class App 
{
    private static Path destinationFolder = null;
    private static Path payloadFilePath = null;
    private static Path configFilePath = null;
    private static final Logger log = LogManager.getLogger("AppMain");

    private static void setDestinationFolderPath(String inputDestinationFolder) throws InvalidPathException, FileNotFoundException, IOException {
        destinationFolder = Paths.get(inputDestinationFolder).toAbsolutePath();
        if (Files.notExists(destinationFolder)) {    
            System.out.println("Folder does not exist. Do you want to create it? type 'y' \n (If not, downloaded packages will store in a working directory of program)\n"
            + "> ");
            try (Scanner sc = new Scanner(System.in);){
                if (sc.nextLine() == "y") {
                    Files.createDirectories(destinationFolder);
                    return;
                } else {
                    destinationFolder = Paths.get(System.getProperty("user.dir") + "/DownloadedPackages");
                    if (Files.notExists(destinationFolder)) {
                        Files.createDirectories(destinationFolder);
                    }
                    return;
                }
            }
        }

    }

    private static void setPayloadFilePath(String inputPayloadFilePath) throws InvalidPathException{
        Path filePath = Paths.get(inputPayloadFilePath).toAbsolutePath();

        if (Files.notExists(filePath)) {
            payloadFilePath = filePath;
            return;
        }
    }

    private static void setConfigFilePath(String inputConfigFilePath) throws InvalidPathException{
        Path filePath = Paths.get(inputConfigFilePath).toAbsolutePath();
        
        if (Files.notExists(filePath)) {
            configFilePath = filePath;
            return;
        }
    }

    private static void cmdLineArgParse(String[] args) throws FileNotFoundException, IOException{
        for(int i=1; i<args.length; i++) {
            switch (args[i]){
                case ("-o"):{
                    if (args[i+1].length() > 2) {
                        setDestinationFolderPath(args[i+1]);
                        i++;
                        log.debug("Sets path to folder with downloaded packages is successful");
                        continue;
                    } else {
                        log.error("Path to folder with downloaded packages sets incorrect");
                        throw new IOException();
                    }
                }
                case ("-c"):{
                    if (args[i+1].length() > 2) {
                        setConfigFilePath(args[i+1]);
                        i++;
                        log.debug("Sets json config file path is successful");
                        continue;
                    } else {
                        log.error("Path to json config file sets incorrect");
                        throw new IOException();
                    }
                }
                case ("-p"):{
                    if (args[i+1].length() > 2) {
                        setPayloadFilePath(args[i+1]);
                        i++;
                        log.debug("Sets payload file path is successful");
                        continue;
                    } else {
                        log.error("Path to payload file path sets incorrect");
                        throw new IOException();
                    }
                }
                case ("-h"):{
                    System.out.println("Usage:\n tricks_parser [OPTION]... [PATH_TO_FOLDER]...");
                    System.out.println("Options:\n"
                    + "  -o         path to output folder. In this folder, downloaded packages will be store.\n"
                    + "  -p         overrides config file path to payload file. Uses overrided  value instead of set in json config file.\n\n"
                    + " --DEBUG     ignore other keys and uses debug values");
                    break;
                }
                default:{
                    System.out.println("Error in entered values. Use -h key for gets more help");
                    break;
                }
            }
        }
    }

    public static void main(String[] args )
    {
        if (args[1] == "--DEBUG") {

        } else {
            try {
                cmdLineArgParse(args);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
        
        Parser parser = new Parser();
        Downloader downloader = new Downloader(destinationFolder);
        parser.readConfig(configFilePath);
        parser.startProcessingPayloadFile(payloadFilePath);

        try {
            for(Package pkg : ResultsManager.getFoundedPackages()) {
                downloader.downloadPackage(pkg);
            }
        } catch (NoSuchAlgorithmException ex) {
            log.error("Set incorrect algorithm type name");
            ex.printStackTrace();
        } catch (IOException ex) {
            log.error("IO Error");
            ex.printStackTrace();
        } catch (InvalidPathException ex) {
            log.error("Set incorrect path to downloaded packages");
            ex.printStackTrace();
        }
        // try {
        //     downloader = checkDestinationFolder(args[0]);
        // } catch (FileNotFoundException ex){
        //     ex.printStackTrace();
        // } catch (IOException ex) {
        //     ex.printStackTrace();
        // }

        //System.out.print(System.getProperty("user.dir") + "/src/main/resources/config.json");
        
    }
}
