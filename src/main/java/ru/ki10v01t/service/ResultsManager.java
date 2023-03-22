package ru.ki10v01t.service;

import java.util.ArrayList;

public class ResultsManager {
    private static ArrayList<Package> foundedPackages;
    
    ResultsManager() {
        foundedPackages = new ArrayList<>();
    }
    
    public static void addPackage(Package inputPackage) {
        foundedPackages.add(inputPackage);
    }
}
