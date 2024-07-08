package ru.ki10v01t.service;

import java.util.concurrent.Callable;
import java.util.regex.Matcher;

public class ParserWorker implements Callable<Boolean> {
    private int beginningFoundStartingRegion;
    private int endingFoundStartingRegion;
    private InnerValuesForRegexps<Matcher> el;
    private StringBuilder fileData;

    public ParserWorker(InnerValuesForRegexps<Matcher> el, StringBuilder fileData) {
        this.beginningFoundStartingRegion = el.getMethodNameAndBody().start();
        this.endingFoundStartingRegion = el.getMethodNameAndBody().end();
        this.el = el;
        this.fileData = fileData;
    }

    private void multithreadParsing() {
        for(Matcher target : el.getSearchTargets())
        {
            target = target.region(beginningFoundStartingRegion, endingFoundStartingRegion);
            while(target.find()) {
                //thread1
                String findedMethodName = "";
                Matcher targetName = el.getMethodName().region(beginningFoundStartingRegion, endingFoundStartingRegion);
                while(targetName.find()) {
                    findedMethodName = fileData.substring(targetName.start(), targetName.end());
                }

                ResultsManager.addFoundedPackage(new Package(
                                                    linkExtraction(el, target.start(), target.end(), fileData),
                                                    hashExtraction(el, target.start(), target.end(), fileData),
                                                    findedMethodName));
            }
        }
    } 

    private StringBuilder hashExtraction(InnerValuesForRegexps<Matcher> el, int regionStartId, int regionEndId, StringBuilder fileData) {
        StringBuilder parsedHash = new StringBuilder(15);
        //String parsedHash = "";
        Matcher targetHash = el.getHashExtractor().region(regionStartId, regionEndId);
        while(targetHash.find()) {
            parsedHash.append(fileData.substring(targetHash.start(), targetHash.end()));
            //parsedHash = fileData.substring(targetHash.start(), targetHash.end());
        }
        parsedHash.trimToSize();
        return parsedHash;
    }

    private StringBuilder linkExtraction(InnerValuesForRegexps<Matcher> el, int regionStartId, int regionEndId, StringBuilder fileData) {
        StringBuilder parsedLink = new StringBuilder(30);
        //String parsedLink = "";
        Matcher targetLink = el.getLinkExtractor().region(regionStartId, regionEndId);
        while(targetLink.find()) {
            parsedLink.append(fileData.substring(targetLink.start(), targetLink.end()));
            //parsedLink = fileData.substring(targetLink.start(), targetLink.end());
        }
        parsedLink.trimToSize();
        return parsedLink;
    }

    // @Override
    // public void run() {
    //     multithreadParsing();
    // }

    @Override
    public Boolean call() throws Exception {
        multithreadParsing();
        return true;
    }

}
