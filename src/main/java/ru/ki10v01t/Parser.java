package ru.ki10v01t;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.FilenameUtils;
import ru.ki10v01t.service.ConfigManager;
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
 * @see MODE
 * @param filepath - the absolute path to file in filesystem. If selected mode is CONFIG, checks: whether the value is non-zero  
 * @param mode
 */
    public static void readFile(String filepath, MODE mode) {
        File file = null;
        try {
            if (filepath == null || filepath == "") {
                filepath = "/home/dimidrol/Documents/Workspace/git/tricks_parser/src/main/resources/config.json";
            }
            file = new File(filepath);
            if (!file.exists()) {
                System.out.println("Запрашиваемый конфигурационный файл не найден. Используется стандартный");
                filepath = "/home/dimidrol/Documents/Workspace/git/tricks_parser/src/main/resources/config.json";
            } 
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        try (FileReader fr = new FileReader(file);   
            BufferedReader bufReader = new BufferedReader(fr);) 
        {             
            String line = bufReader.readLine();

            switch (mode) {
                case CONFIG:
                    ConfigManager.createConfig(file);
                    ConfigManager.getConfig();
                    break;
                case PAYLOAD:
                    doPayloadProcessing(line, bufReader);
                    break;
                default:
                    throw new ParserConfigurationException("Задан неправильный режим работы парсера");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            e.getMessage();
        }
    }

    private static void doPayloadProcessing(String line, BufferedReader bufReader) throws IOException, ParserConfigurationException {
        if (ConfigManager.getConfig() == null) {
            throw new ParserConfigurationException("Конфиг не задан");
        }
        int lineNumber=0;
        ArrayList<Package> pkgArray = new ArrayList<Package>();
        while (line != null) {

            //pkgArray.add(e)
            line = bufReader.readLine();   
            lineNumber++;         
        }
    }
}
