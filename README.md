# Tricks parser

This is a utility for parsing winetricks file in order to find and easily download Windows components and drivers using links from a file\
To work, you will need to install Java 17.

    -o path to output folder. In this folder, downloaded packages will be store.
    -p overrides config file path to payload file. Uses overrided  value instead of set in json config file.
    --DEBUG ignore other keys and uses debug values (not implemented yet)

If no options are specified, the default values (winetricks stored in /src/resources/winetricks) will be used.

Usage example:
1. Enter next command
```bash
java -jar tricks_parser.jar -o /home/user/downloads/output-folder -p /home/user/documents/winetricks
```
2. The application will display a list of all found packages and prompt you to enter the package number for further download.
3. After you make a choice, the application will try to download the Windows package using the link from winetricks.