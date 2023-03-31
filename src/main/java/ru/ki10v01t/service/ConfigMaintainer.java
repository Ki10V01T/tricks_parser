package ru.ki10v01t.service;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigMaintainer {
    private ArrayList<InnerValuesForRegexps<Pattern>> patterns = new ArrayList<>();
    private ArrayList<InnerValuesForRegexps<Matcher>> matchers = new ArrayList<>();


    ConfigMaintainer() {
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
            //p.setQuotationExtractor(Pattern.compile(el.getQuotationExtractor(), Pattern.MULTILINE));
            p.setLinkExtractor(Pattern.compile(el.getLinkExtractor(), Pattern.MULTILINE));
            p.setHashExtractor(Pattern.compile(el.getHashExtractor(), Pattern.MULTILINE));

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
            //m.setQuotationExtractor(el.getQuotationExtractor().matcher(fileData));
            m.setLinkExtractor(el.getLinkExtractor().matcher(fileData));
            m.setHashExtractor(el.getHashExtractor().matcher(fileData));

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
