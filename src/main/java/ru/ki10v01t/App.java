package ru.ki10v01t;

public class App 
{

    private void configFilePathChecker() {

    }

    public static void main( String[] args )
    {
        Parser parser = new Parser();
        parser.readConfig("");
        //System.out.print(System.getProperty("user.dir") + "/src/main/resources/config.json");
        parser.startProcessingPayloadFile(); 
    }
}
