package ru.ki10v01t;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import ru.ki10v01t.Downloader.DownloaderResult;
import ru.ki10v01t.service.ResultsManager;


public class App 
{
    private static Path outputFolder = null;
    private static Path payloadFilePath = null;
    private static Path configFilePath = null;
    private static final Logger log = LogManager.getLogger("AppMain");

    private static void setOutputFolderPath(String newOutputFolder) throws InvalidPathException, FileNotFoundException, IOException, SecurityException, FileAlreadyExistsException {
        outputFolder = Paths.get(newOutputFolder).toAbsolutePath();
        if (Files.notExists(outputFolder)) {    
            System.out.print("Folder does not exist. Do you want to create it? type 'y' \n (If not, downloaded packages will store in a working directory of program)\n"
            + "> ");
            try (Scanner sc = new Scanner(System.in);){
                if (sc.nextLine().equals("y")) {
                    Files.createDirectories(outputFolder);
                    return;
                } else {
                    outputFolder = Paths.get(System.getProperty("user.dir") + "/DownloadedPackages");
                    if (Files.notExists(outputFolder)) {
                        Files.createDirectories(outputFolder);
                    }
                    return;
                }
            }
        }

    }

    private static void setPayloadFilePath(String inputPayloadFilePath) throws InvalidPathException, FileNotFoundException{
        Path filePath = Paths.get(inputPayloadFilePath).toAbsolutePath();

        if (Files.exists(filePath)) {
            payloadFilePath = filePath;
            return;
        }
        throw new FileNotFoundException("File with payload not found");
    }

    private static void setConfigFilePath(String inputConfigFilePath) throws InvalidPathException, FileNotFoundException{
        Path filePath = Paths.get(inputConfigFilePath).toAbsolutePath();
        
        if (Files.exists(filePath)) {
            configFilePath = filePath;
            return;
        }
        throw new FileNotFoundException("File with config json not found");
    }

    private static void cmdLineArgParse(String[] args) throws FileNotFoundException, IOException, SecurityException, FileAlreadyExistsException{
        for(int i=0; i<args.length; i++) {
            switch (args[i]){
                case ("-o"):{
                    if (args[i+1].length() > 2) {
                        setOutputFolderPath(args[i+1]);
                        i++;
                        log.debug("Sets path to folder with downloaded packages is successful");
                        continue;
                    } else {
                        throw new IOException("Path to folder with downloaded packages sets incorrect");
                    }
                }
                case ("-c"):{
                    if (args[i+1].length() > 2) {
                        setConfigFilePath(args[i+1]);
                        i++;
                        log.debug("Sets json config file path is successful");
                        continue;
                    } else {
                        throw new IOException("Path to json config file sets incorrect");
                    }
                }
                case ("-p"):{
                    if (args[i+1].length() > 2) {
                        setPayloadFilePath(args[i+1]);
                        i++;
                        log.debug("Sets payload file path is successful");
                        continue;
                    } else {
                        throw new IOException("Path to payload file path sets incorrect");
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
        if (args[0] == "--DEBUG") {

        } else {
            try {
                cmdLineArgParse(args);
            } catch (FileNotFoundException fnfe) {
                log.error(fnfe.getMessage(), fnfe);
                System.exit(1);
            } catch (IOException ioe) {
                log.error(ioe.getMessage(), ioe);
            }
        }
        
        
        Downloader downloader = new Downloader(outputFolder);
        
        try (Scanner sc = new Scanner (System.in)) {
            Parser parser = new Parser(configFilePath);
            parser.startProcessingPayloadFile(payloadFilePath);
            
            //TODO:
            System.out.println("Enter number of package: ");
            Integer pkgNumber = Integer.parseInt(sc.nextLine()); 

            Integer totalPackagesNumber = ResultsManager.getFoundedPackages().size()-1;
            if (pkgNumber < 0 || pkgNumber > totalPackagesNumber) {
                throw new NumberFormatException();
            }

            
            DownloaderResult downloadResult = downloader.downloadPackage(ResultsManager.getFoundedPackageById(pkgNumber));

            switch (downloadResult.getResultCode()) {
                case D_COMPLETE: {
                    System.out.println("Download complete. Filename is: " + downloadResult.getPackageName());
                    break;
                }
                case D_ALREADY_DOWNLOADED: {
                    System.out.println("File is already downloaded. Filename is: " + downloadResult.getPackageName());
                    break;
                }
                case D_ERROR:{
                    System.out.println("Download error. Filename is: " + downloadResult.getPackageName());
                    break;
                }
            }
        
        } catch (NoSuchAlgorithmException nsae) {
            log.error("Set incorrect algorithm type name", nsae);
            System.exit(1);
            //log.error("Set incorrect path to downloaded packages", nsae);
        } catch (NumberFormatException nfe) {
            log.error("Invalid number of package was entered", nfe);
            System.exit(1);
        } catch (InvalidPathException ipe) {
            log.error("Invalid output path was entered", ipe);
            System.exit(1);
        } catch (SecurityException se) {
            log.error("Read/Write operation by this path is not allowed", se);
            System.exit(1);
        } catch (FileNotFoundException fnfe) {
            log.error(fnfe.getMessage(), fnfe);
            System.exit(1);
        } catch (IOException ioe) {
            log.error("IO Error", ioe);
            System.exit(1);
        } catch (ExecutionException ee) {
            log.error(ee.getMessage(), ee);
            System.exit(1);
        } catch (ParserConfigurationException pce) {
            log.error(pce.getMessage(), pce);
            System.exit(1);
        } catch (InterruptedException ie) {
            log.error(ie.getMessage(), ie);
            System.exit(1);
        }
    }
}
