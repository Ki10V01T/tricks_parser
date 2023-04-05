package ru.ki10v01t.service;


public class Package {
    private String link;
    private String methodName;
    private String name;
    private String version;
    private String hash;
      

    public Package(String inputLink, String inputHash, String inputMethodName) {
        this.link = inputLink.replaceAll("^\"|\"$", "");
        this.hash = inputHash.replaceAll("^\"|\"$", "");
        this.methodName = inputMethodName.replaceAll("^\"|\"$", "");
    }

    public void setName(String input) {
        this.name = input;
    }

    public void setMethodName(String input) {
        this.methodName = input;
    }

    public void setVersion(String input) {
        this.version = input;
    }

    public void setLink(String input) {
        this.link = input;
    }

    public void setHash(String input) {
        this.hash = input;
    }

    public String getName() {
        return this.name;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public String getVersion() {
        return this.version;
    }   

    public String getLink() {
        return this.link;
    }

    public String getHash() {
        return this.hash;
    }
}