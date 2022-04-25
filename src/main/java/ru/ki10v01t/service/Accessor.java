package ru.ki10v01t.service;

public abstract class Accessor <T> {
    T value;
    public void setVal(T input) {
        this.value = input;
    }
    
    public T getVal() {
        return this.value;
    } 
}
