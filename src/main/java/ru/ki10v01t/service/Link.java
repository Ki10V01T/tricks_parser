package ru.ki10v01t.service;

public class Link extends LineNumber {
    private String link;
    private String cookie;
    
    public void setLink(String input) {
        this.link = input;
    }        

    public String getLink() {
        return this.link;
    }

    public void setCookie(String input) {
        this.cookie = input;
    }        

    public String getCookie() {
        return this.cookie;
    }
} 
