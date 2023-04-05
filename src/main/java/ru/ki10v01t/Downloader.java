package ru.ki10v01t;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Downloader {
    private Path destinationFolder;

    public Downloader(Path inputDestinationFolder) {
        this.destinationFolder = inputDestinationFolder.toAbsolutePath();
    }

    public void downloadPackage(ru.ki10v01t.service.Package pkg) throws MalformedURLException, IOException {
        URL url = new URL(pkg.getLink());
        
        Path pathToFile = Paths.get(destinationFolder.toString() + "/" + pkg.getName().replaceAll("()$", "")); 
        //FileChannel outChannel = Files.
        //Files.createFile(pathToFile); 
    }

    private Boolean checkHash(String expectedHash, Path overallPath) {

        return true;
    }
}
