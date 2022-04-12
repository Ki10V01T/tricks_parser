package ru.ki10v01t;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputFilter.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import org.apache.commons.io.FilenameUtils;

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

    public static void parseConfig() {

    }

    public void readMethodNames () {
        
    }

/**
 * Parses input file, in according with selected mode
 * @see MODE
 * @param filepath - the absolute path to file in filesystem. If selected mode is CONFIG, checks: whether the value is non-zero  
 * @param mode
 */
    public void readFile(String filepath, MODE mode) {
        if (mode == MODE.CONFIG && (filepath == null || filepath == "")) {
            filepath = "/resources/config.json";
        }
        
        if (FilenameUtils.getExtension(filepath) != "json") {
            throw new Exception("Тип файла не соответсвует json");
        }

        File file = null;
        try {
            file = new File(filepath);
            
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        try (FileReader fr = new FileReader(file);   
            BufferedReader bufReader = new BufferedReader(fr);
            ) 
        {             
            String line = bufReader.readLine();
            if (mode == MODE.CONFIG) {
                String configJson = null;
                while (line != null) {
                    configJson += line;
                    line = bufReader.readLine();            
                }
                ObjectMapper objectMapper = new ObjectMapper();
                Config objectMapper.readValue(configJson, Config.class);
            }
            else {
                while (line != null) {
                //TODO: !!!
                line = bufReader.readLine();            
                }
            } 
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
