package ru.ki10v01t.service;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class Config {    
    private ArrayList<InnerValuesForRegexps> regexps =  new ArrayList<InnerValuesForRegexps>();
    private String payloadFilePath;

    public class InnerValuesForRegexps {
        private String methodName, methodBody, searchTarget;

        public InnerValuesForRegexps(){
        }

        @JsonSetter("methodName")
        public void setMethodName (String input) {
            this.methodName = input;
        }

        @JsonSetter("methodBody")
        public void setMethodBody (String input) {
            this.methodBody = input;
        }

        @JsonSetter("searchTarget")
        public void setSearchTarget (String input) {
            this.searchTarget = input;
        }

        @JsonGetter("methodName")
        public String getMethodName() {
            return this.methodName;
        }

        @JsonGetter("methodBody")
        public String getMethodBody() {
            return this.methodBody;
        }

        @JsonGetter("searchTarget")
        public String getSearchTarget() {
            return this.searchTarget;
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
