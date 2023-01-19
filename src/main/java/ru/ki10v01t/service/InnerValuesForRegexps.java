package ru.ki10v01t.service;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonSetter;

public class InnerValuesForRegexps <T> {
    private T methodName, methodNameAndBody;
    private ArrayList<T> searchTargets;

    @JsonSetter("methodName")
    public void setMethodName (T input) {
        this.methodName = input;
    }

    @JsonSetter("methodNameAndBody")
    public void setMethodNameAndBody (T input) {
        this.methodNameAndBody = input;
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
