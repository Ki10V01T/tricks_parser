package ru.ki10v01t.service;

import java.util.ArrayList;

public class ResultsManager {
    private static ArrayList<Package> foundedPackages = new ArrayList<>();
    
    ResultsManager() {
        //foundedPackages = new ArrayList<>();
    }


    
    public static void addPackage(Package inputPackage) {
        foundedPackages.add(inputPackage);
    }

    public static ArrayList<Package> getFoundedPackages() {
        return foundedPackages;
    }

    public static Package getFoundedPackageById(Integer index) {
        return foundedPackages.get(index);
    }

    public static void printFoundedPackages() {
        System.out.printf("%s|    %s    |    %s\n", "№", "Name", "Link");
        for (int i=0; i<foundedPackages.size(); i++) {
            Package pkg = foundedPackages.get(i);
            System.out.printf("\n%s|    %s    |    %s\n", i , pkg.getMethodName(), pkg.getLink());
        }
    }
}
