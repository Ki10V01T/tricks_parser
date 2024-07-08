package ru.ki10v01t;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
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

    public class DownloaderResult {
        private final DOWNLOADER_RESULT_CODE code;
        private final String packageName;

        public DownloaderResult(DOWNLOADER_RESULT_CODE inputCode, String inputName) {
            this.code = inputCode;
            this.packageName = inputName;
        }

        public DOWNLOADER_RESULT_CODE getResultCode() {
            return this.code;
        }

        public String getResult() {
            String resultMessage;
            
            if (packageName.equals("")) {
                resultMessage = "Downloading package error. Remote host doesn't returned nothing";
            }

            
            switch (code) {
                case D_COMPLETE -> resultMessage = "Download complete. Filename is: " + packageName;
                case D_ALREADY_DOWNLOADED -> resultMessage = "File is already downloaded. Filename is: " + packageName;
                case D_ERROR -> resultMessage = "Download error. Filename is: " + packageName;
                default -> resultMessage = "FATAL ERROR";
            }

            return resultMessage;
        }
    }

    private void checkConnection(URL url) throws IOException, SocketTimeoutException {
        URLConnection connection = url.openConnection();

        System.out.println("Remote host connection attempting...");
        connection.setReadTimeout(15000);
        connection.connect();

        System.out.println("Connection successfull. Continue downloading...");
    }

    /**
     * Downloads a one package from url that was parsed from specific package from file and returns of download's result code.
     * @param pkg
     * @return result code of download
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws InvalidPathException
     */
    public DownloaderResult downloadPackage(Package pkg) throws NoSuchAlgorithmException, IOException, InvalidPathException, SocketTimeoutException {
        URL url = new URL(pkg.getLink());
        
        checkConnection(url);

        String[] fileNameBits = url.getFile().split("/");
        
        String downloadedFileName = fileNameBits[fileNameBits.length-1];

        Path pathToFile = Paths.get(destinationFolder.toString() + "/" + downloadedFileName); 

        if (Files.exists(pathToFile) && checkHash(pkg.getHash(), pathToFile)) {
            return new DownloaderResult(DOWNLOADER_RESULT_CODE.D_ALREADY_DOWNLOADED, downloadedFileName);
        }

        // try (ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        //     FileOutputStream fos = new FileOutputStream(pathToFile.toFile())) {
            
        //     ByteBuffer buf = ByteBuffer.allocate(8192);
        //     while(rbc.read(buf) != -1) {
        //         buf.flip();
        //         fos.getChannel().write(buf);
        //         buf.compact();
        //     } 

        //     if (checkHash(pkg.getHash(), pathToFile)) {
        //     return new DownloaderResult(DOWNLOADER_RESULT_CODE.D_COMPLETE, downloadedFileName);
        // }
        //     return new DownloaderResult(DOWNLOADER_RESULT_CODE.D_ERROR, downloadedFileName);
        // }

        try (ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream(pathToFile.toFile());
            FileChannel fileChannel = fos.getChannel()) {
                fileChannel.transferFrom(rbc, 0, Long.MAX_VALUE);
            if (checkHash(pkg.getHash(), pathToFile)) {
            return new DownloaderResult(DOWNLOADER_RESULT_CODE.D_COMPLETE, downloadedFileName);
        }
            return new DownloaderResult(DOWNLOADER_RESULT_CODE.D_ERROR, downloadedFileName);
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
