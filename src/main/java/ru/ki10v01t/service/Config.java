package ru.ki10v01t.service;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;


public class Config {    
    private HashMap <String, InnerValuesForRegexps> regexps;
    private String payloadFilePath;

    class InnerValuesForRegexps {
        private String methodName, methodBody, searchTarget;

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
    public void setRegexps(String key, InnerValuesForRegexps value) {
        this.regexps.put(key, value);
    }

    @JsonSetter("payloadFilePath")
    public void setPayloadFilePath(String input) {
        this.payloadFilePath = input;
    }

    @JsonSetter("regexps")
    public HashMap<String, InnerValuesForRegexps> getRegexps() {
        return this.regexps;
    }

    @JsonGetter("payloadFilePath")
    public String getPayloadFilePath() {
        return this.payloadFilePath;
    }
}
