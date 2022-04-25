package ru.ki10v01t.service;

import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class Config {    
    private ArrayList<InnerValuesForRegexps> regexps;
    private String payloadFilePath;

    public class InnerValuesForRegexps extends Accessor <T>{
        private String methodName, methodNameAndBody;
        private ArrayList<String> searchTargetsWdownload, searchTargetsWdownloadTo;

        @JsonSetter("methodName")
        public void setMethodName (String input) {
            this.methodName = input;
        }

        @JsonSetter("methodNameAndBody")
        public void setMethodNameAndBody (String input) {
            this.methodNameAndBody = input;
        }

        @JsonSetter("searchTargetsWdownload")
        public void setSearchTargetsWdownload (ArrayList<String> input) {
            this.searchTargetsWdownload = input;
        }

        @JsonSetter("searchTargetsWdownloadTo")
        public void setSearchTargetsWdownloadTo (ArrayList<String> input) {
            this.searchTargetsWdownloadTo = input;
        }

        @JsonGetter("methodName")
        public String getMethodName() {
            return this.methodName;
        }

        @JsonGetter("methodNameAndBody")
        public String getMethodNameAndBody() {
            return this.methodNameAndBody;
        }

        @JsonGetter("searchTargetsWdownload")
        public ArrayList<String> getSearchTargetsWdownload() {
            return this.searchTargetsWdownload;
        }

        @JsonGetter("searchTargetsWdownloadTo")
        public ArrayList<String> getSearchTargetsWdownloadTo() {
            return this.searchTargetsWdownloadTo;
        }

        public String getSearchTargetByIndex(ArrayList<String> inputArray, Integer index) throws ArrayIndexOutOfBoundsException {
            if (index >= inputArray.size()) {
                return inputArray.get(index);
            }
            else {
                throw new ArrayIndexOutOfBoundsException("Выход за границы массива");
            }
        }

        public String printSearchTargetsWithArgs (ArrayList<String> inputArray) {
            return String.join(", ", inputArray);
        }
    }

    @JsonSetter("regexps")
    public void setRegexps(InnerValuesForRegexps value) {
        this.regexps.add(value);
    }

    @JsonSetter("payloadFilePath")
    public void setPayloadFilePath(String input) {
        this.payloadFilePath = input;
    }

    @JsonGetter("regexps")
    public ArrayList<InnerValuesForRegexps> getRegexps() {
        return this.regexps;
    }    

    @JsonGetter("payloadFilePath")
    public String getPayloadFilePath() {
        return this.payloadFilePath;
    }
}
