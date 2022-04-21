package ru.ki10v01t.service;


public class Package {
    private Link link;
    private Method locatedInMethod;
    private String name;
    private String version;
    private String hash;
      

    public Package(){
        link = new Link();
        locatedInMethod = new Method();
    }

    public void setName(String input) {
        this.name = input;
    }

    public String getName() {
        return this.name;
    }

    public void setVersion(String input) {
        this.version = input;
    }

    public String getVersion() {
        return this.version;
    }

    public void setHash(String input) {
        this.hash = input;
    }

    public String getHash() {
        return this.hash;
    }
}