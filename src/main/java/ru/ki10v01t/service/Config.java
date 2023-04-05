package ru.ki10v01t.service;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonSetter;

public class Config {    
    private ArrayList<InnerValuesForRegexps<String>> regexps = new ArrayList<>();
    private String payloadFilePath;


    @JsonSetter("regexps")
    public void addRegexp(InnerValuesForRegexps<String> value) {
        this.regexps.add(value);
    }

    @JsonSetter("payloadFilePath")
    public void setPayloadFilePath(String input) {
        this.payloadFilePath = input;
    }  

    public ArrayList<InnerValuesForRegexps<String>> getRegexps() {
        return this.regexps;
    }    

    public String getPayloadFilePath() {
        return this.payloadFilePath;
    }

    @Override
    public String toString() {
        for (InnerValuesForRegexps<String> el : regexps) {
            System.out.printf("\n Method name: %s,\n Method Name and Body:  %s,\n SearchTargets: %s,\n, Payload file path: %s\n", 
            el.getMethodName(),
            el.getMethodNameAndBody(),
            el.printSearchTargetsWithArgs(),
            getPayloadFilePath());
        }
        return null;
    }
}
