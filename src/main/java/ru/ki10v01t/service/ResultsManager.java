package ru.ki10v01t.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ResultsManager {
    private static ArrayList<Package> foundedPackages = new ArrayList<>();
    
    ResultsManager() {
        //foundedPackages = new ArrayList<>();
    }


    
    public synchronized static void addFoundedPackage(Package inputPackage) {
        foundedPackages.add(inputPackage);
    }

    public static ArrayList<Package> getFoundedPackages() {
        return foundedPackages;
    }

    public static Package getFoundedPackageById(Integer index) {
        return foundedPackages.get(index);
    }

    private static void cleaningPackagesWithEmptyAddresses() {
        ArrayList<Integer> idsForDeleting = new ArrayList<>(10);

        for (int i = 0; i < foundedPackages.size(); i++) {
            if (foundedPackages.get(i).getLink().length() < 5) {
                idsForDeleting.add(i);
            }
        }

        Collections.reverse(idsForDeleting);

        for (Integer id : idsForDeleting) {
            foundedPackages.remove(id.intValue());
        }

        foundedPackages.trimToSize();
    }

    public static void printFoundedPackages() {
        System.out.println("");
        System.out.printf("%-4s| %-5s%-5s\n%-4s| %s\n", "№", "Name", "", "", "Link");

        cleaningPackagesWithEmptyAddresses();

        for (int i = 0; i < foundedPackages.size(); i++) {
            Package pkg = foundedPackages.get(i);

            System.out.printf("\n%-4s| %-5s%-4s| %s\n", i, pkg.getMethodName(), "", pkg.getLink());
        }
        System.out.println("");
    }
}
