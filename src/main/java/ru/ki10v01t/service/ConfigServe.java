package ru.ki10v01t.service;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigServe {
    private ArrayList<InnerValuesForRegexps<Pattern>> patterns = new ArrayList<>();
    private ArrayList<InnerValuesForRegexps<Matcher>> matchers = new ArrayList<>();


    ConfigServe() {
        patterns = new ArrayList<>();
        matchers = new ArrayList<>();
    }

    public void addMatcher(InnerValuesForRegexps<Matcher> value) {
        this.matchers.add(value);
    }

    public void makePatterns() {

        InnerValuesForRegexps<Pattern> p;

        for (InnerValuesForRegexps<String> el : ConfigManager.getConfig().getRegexps()) {
            p = new InnerValuesForRegexps<>();
            p.setMethodName(Pattern.compile(el.getMethodName(), Pattern.MULTILINE));
            p.setMethodNameAndBody(Pattern.compile(el.getMethodNameAndBody(), Pattern.MULTILINE));

            for (String target : el.getSearchTargets()) {
                p.addSearchTarget(Pattern.compile(target, Pattern.MULTILINE));
            }

            patterns.add(p);
        }
    }

    public void makeMatchers(String fileData) {
        InnerValuesForRegexps<Matcher> m;

        for (InnerValuesForRegexps<Pattern> el : patterns) {
            m = new InnerValuesForRegexps<>();
            m.setMethodName(el.getMethodName().matcher(fileData));
            m.setMethodNameAndBody(el.getMethodNameAndBody().matcher(fileData));

            for (Pattern target : el.getSearchTargets()) {
                m.addSearchTarget(target.matcher(fileData));
            }

            matchers.add(m);
        }
    }

    public ArrayList<InnerValuesForRegexps<Pattern>> getPatterns() {
        return this.patterns;
    }

    public ArrayList<InnerValuesForRegexps<Matcher>> getMatchers() {
        return this.matchers;
    }
}
