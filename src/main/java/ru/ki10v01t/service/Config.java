package ru.ki10v01t.service;

import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class Config {    
    private ArrayList<InnerValuesForRegexps> regexps;
    private String payloadFilePath;

    public Config() {
        this.regexps = new ArrayList<InnerValuesForRegexps>();
    }

    public class InnerValuesForRegexps {
        private String methodName, methodNameAndBody;
        private ArrayList<String> searchTargets;

        public InnerValuesForRegexps() {
            this.searchTargets = new ArrayList<String>();
        }

        @JsonSetter("methodName")
        public void setMethodName (String input) {
            this.methodName = input;
        }

        @JsonSetter("methodNameAndBody")
        public void setMethodNameAndBody (String input) {
            this.methodNameAndBody = input;
        }

        @JsonSetter("searchTargetsWithArgs")
        public void setSearchTarget (ArrayList<String> input) {
            this.searchTargets = input;
        }

        @JsonGetter("methodName")
        public String getMethodName() {
            return this.methodName;
        }

        @JsonGetter("methodNameAndBody")
        public String getMethodNameAndBody() {
            return this.methodNameAndBody;
        }

        @JsonGetter("searchTargetsWithArgs")
        public ArrayList<String> getSearchTarget() {
            return this.searchTargets;
        }

        public String getSearchTargetByIndex(Integer index) {
            if (index >= this.searchTargets.size()) {
                return searchTargets.get(index);
            }
            else {
                try {
                    throw new ArrayIndexOutOfBoundsException("Выход за границы массива");
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                    e.getMessage();
                } finally {
                    return "";
                }
            }
        }

        public String printSearchTargetsWithArgs () {
            return String.join(", ", this.searchTargets);
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
