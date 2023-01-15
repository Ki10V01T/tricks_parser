package ru.ki10v01t.service;

import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonSetter;

public class Config {    
    private ArrayList<InnerValuesForRegexps> regexps = new ArrayList<>();
    private String payloadFilePath;

    public class InnerValuesForRegexps {
        private String methodName, methodNameAndBody;
        private ArrayList<String> searchTargets;

        @JsonSetter("methodName")
        public void setMethodName (String input) {
            this.methodName = input;
        }

        @JsonSetter("methodNameAndBody")
        public void setMethodNameAndBody (String input) {
            this.methodNameAndBody = input;
        }

        @JsonSetter("searchTargets")
        public void setSearchTargets (ArrayList<String> input) {
            this.searchTargets = input;
        }

        public String getMethodName() {
            return this.methodName;
        }

        public String getMethodNameAndBody() {
            return this.methodNameAndBody;
        }

        public ArrayList<String> getSearchTargets() {
            return this.searchTargets;
        }

        public String getSearchTargetByIndex(ArrayList<String> inputArray, Integer index) throws ArrayIndexOutOfBoundsException {
            if (index >= inputArray.size()) {
                return inputArray.get(index);
            }
            else {
                throw new ArrayIndexOutOfBoundsException("Выход за границы массива");
            }
        }

        public String printSearchTargetsWithArgs () {
            return String.join(", ", searchTargets);
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

    public ArrayList<InnerValuesForRegexps> getRegexps() {
        return this.regexps;
    }    

    public String getPayloadFilePath() {
        return this.payloadFilePath;
    }
}
