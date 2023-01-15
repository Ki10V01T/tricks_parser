package ru.ki10v01t;

public class App 
{
    public static void main( String[] args )
    {
        Parser parser = new Parser();
        parser.readConfig("");
        //System.out.print(System.getProperty("user.dir") + "/src/main/resources/config.json");
        /* Parser parser = new Parser();
        parser.readConfig("");
        parser.startProcessingPayloadFile(); */
    }
}
