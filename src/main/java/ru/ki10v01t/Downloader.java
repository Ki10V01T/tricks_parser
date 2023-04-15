package ru.ki10v01t;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import ru.ki10v01t.service.Package;

enum DOWNLOADER_RESULT_CODE {
    D_COMPLETE,
    D_ERROR,
    D_ALREADY_DOWNLOADED
}

public class Downloader {
    private Path destinationFolder;

    /**
     * 
     * @param inputDestinationFolder
     */
    public Downloader(Path inputDestinationFolder) {
        destinationFolder = inputDestinationFolder.toAbsolutePath();
    }

    /**
     * Downloads a one package from url that was parsed from specific package from file and returns of download's result code.
     * @param pkg
     * @return result code of download
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws MalformedURLException
     * @throws InvalidPathException
     */
    public DOWNLOADER_RESULT_CODE downloadPackage(Package pkg) throws NoSuchAlgorithmException, IOException, InvalidPathException {
        URL url = new URL(pkg.getLink());
        String downloadedFileName = url.getFile();

        Path pathToFile = Paths.get(destinationFolder.toString() + "/" + downloadedFileName); 

        if (Files.exists(pathToFile)
        && checkHash(pkg.getHash(), pathToFile)) {
                return DOWNLOADER_RESULT_CODE.D_ALREADY_DOWNLOADED;
        } 

        try (ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream(pathToFile.toFile())) {
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

            if (checkHash(pkg.getHash(), pathToFile)) {
            return DOWNLOADER_RESULT_CODE.D_COMPLETE;
        }
            return DOWNLOADER_RESULT_CODE.D_ERROR;
        }
    }

    /**
     * Checks a sha-256 hashcode between a parsed package hash and real downloaded file hash.
     * @param expectedHash a hashcode, that was parsed from input file for specific package.
     * @param overallPath path to file in filesystem.
     * @return the result of checking. True - if hashcode is match, false otherwise.
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    private Boolean checkHash(String expectedHash, Path pathToFile) throws NoSuchAlgorithmException, IOException {
        byte[] data = Files.readAllBytes(pathToFile);
        byte[] hash = MessageDigest.getInstance("SHA-256").digest(data);
        String checksum = new BigInteger(1, hash).toString(16);

        if (checksum.equals(expectedHash)) {
            return true;
        }
        return false;        
    }
}
