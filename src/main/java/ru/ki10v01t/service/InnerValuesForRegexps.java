package ru.ki10v01t.service;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonSetter;

public class InnerValuesForRegexps <T> {
    private T methodName, methodNameAndBody, quotationExtractor, linkExtractor, hashExtractor;
    private ArrayList<T> searchTargets;

    @JsonSetter("methodName")
    public void setMethodName (T input) {
        this.methodName = input;
    }

    @JsonSetter("methodNameAndBody")
    public void setMethodNameAndBody (T input) {
        this.methodNameAndBody = input;
    }

    @JsonSetter("quotationExtractor")
    public void setQuotationExtractor(T input) {
        this.quotationExtractor = input;
    }

    @JsonSetter("linkExtractor")
    public void setLinkExtractor(T input) {
        this.linkExtractor = input;
    }

    @JsonSetter("hashExtractor")
    public void setHashExtractor(T input) {
        this.hashExtractor = input;
    }

    @JsonSetter("searchTargets")
    public void setSearchTargets (ArrayList<T> input) {
        this.searchTargets = input;
    }

    public void addSearchTarget (T input) {
        if (this.searchTargets == null) {
            this.searchTargets = new ArrayList<>();
        }
        this.searchTargets.add(input);
    }

    public T getMethodName() {
        return this.methodName;
    }

    public T getMethodNameAndBody() {
        return this.methodNameAndBody;
    }

    public T getQuotationExtractor() {
        return this.quotationExtractor;
    }

    public T getLinkExtractor() {
        return this.linkExtractor;
    }

    public T getHashExtractor() {
        return this.hashExtractor;
    }

    public ArrayList<T> getSearchTargets() {
        return this.searchTargets;
    }

    public String getSearchTargetByIndex(ArrayList<String> inputArray, Integer index) throws ArrayIndexOutOfBoundsException {
        //Проверить!!!
        if (index >= inputArray.size()) {
            return inputArray.get(index);
        }
        else {
            throw new ArrayIndexOutOfBoundsException("Выход за границы массива");
        }
    }

    public String printSearchTargetsWithArgs () {
        return String.join(", ", searchTargets.toString());
    }
}
