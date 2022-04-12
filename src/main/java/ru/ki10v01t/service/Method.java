package ru.ki10v01t.service;

import java.util.ArrayList;

public class Method extends LineNumber {
    private String methodName;
    private Integer position;
    private ArrayList<String> methodBody;
    private String[] inputArgs;

    public Method() {
        methodBody = new ArrayList<>();
        inputArgs = new String[4];
    }

    public void setMethodBody(ArrayList<String> input) {
        this.methodBody = input;
    }

    public ArrayList<String> getRefArrayMethodBody() {
        return this.methodBody;
    }

    public String getMethodBodyLineByIndex (Integer index) {
        if (index < 0 || index >= this.methodBody.size()) {
            System.out.println("<ERROR> <Method.setInputArgs>: Индекс выходит за границы массива");
            return "";
        }
        return this.methodBody.get(index);
    }

    public void setMethodName(String input) {
        this.methodName = input;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setPosition(Integer input) {
        this.position = input;
    }

    public Integer getPosition() {
        return this.position;
    }              

    public void setInputArgs(String[] input) {
        if (input.length > inputArgs.length) {
            System.out.println("<ERROR> <Method.setInputArgs>: Количество параметров превысило 4");
            return;
        }
        
        for (int i = 0; i < inputArgs.length; i++) {
            inputArgs[i] = input[i];
        }            
    }     
    
    public String[] getInputArgs() {
        return this.inputArgs;
    }

    public String getInputArgByIndex(Integer index) {
        if (index < 0) {
            System.out.println("<ERROR> <Method.getInputArgByIndex>: Индекс не может быть меньше нуля");
            return "";
        }
        return this.inputArgs[index];
    }
}
