package ru.ki10v01t;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class App 
{
    private static Downloader downloader;
    private static Parser parser;

    private static Downloader checkDestinationFolder(String inputDestinationFolder) throws FileNotFoundException, IOException {
        Path destinationFolder = Paths.get(inputDestinationFolder).toAbsolutePath();
        if (Files.notExists(destinationFolder)) {    
            System.out.println("Folder does not exist. Do you want to create it? type 'y' \n (If not, downloaded packages will store in a working directory of program)");
            Scanner sc = new Scanner(System.in);
            if (sc.nextLine() == "y") {
                Files.createDirectories(destinationFolder);
            } else {
                destinationFolder = Paths.get(System.getProperty("user.dir"));
            }
            sc.close();
        }
        return new Downloader(destinationFolder);
    }

    public static void main( String[] args )
    {
        parser = new Parser();
        parser.readConfig("");
        
        // try {
        //     downloader = checkDestinationFolder(args[0]);
        // } catch (FileNotFoundException ex){
        //     ex.printStackTrace();
        // } catch (IOException ex) {
        //     ex.printStackTrace();
        // }

        //System.out.print(System.getProperty("user.dir") + "/src/main/resources/config.json");
        parser.startProcessingPayloadFile(); 
    }
}
