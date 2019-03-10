package com.dinoproblems.server.utils;

public class NounBuilder {
    private String nominative;
    private String genitive;
    private String countingForm;
    private String pluralForm;
    private GeneratorUtils.Gender gender;
    private String instrumentalForm;
    private String accusativeForm;
    private String accusativePluralForm;

    public NounBuilder nom(String nominative) {
        this.nominative = nominative;
        return this;
    }

    public NounBuilder gen(String genitive) {
        this.genitive = genitive;
        return this;
    }

    public NounBuilder counting(String countingForm) {
        this.countingForm = countingForm;
        return this;
    }

    public NounBuilder plural(String pluralForm) {
        this.pluralForm = pluralForm;
        return this;
    }

    public NounBuilder gender(GeneratorUtils.Gender gender) {
        this.gender = gender;
        return this;
    }

    public NounBuilder instr(String instrumentalForm) {
        this.instrumentalForm = instrumentalForm;
        return this;
    }

    public NounBuilder acc(String accusativeForm) {
        this.accusativeForm = accusativeForm;
        return this;
    }

    public NounBuilder accPlural(String accusativeForm) {
        this.accusativePluralForm = accusativeForm;
        return this;
    }

    public Noun createNoun() {
        return new Noun(gender, nominative, genitive, countingForm, pluralForm, instrumentalForm, accusativeForm, accusativePluralForm);
    }
}