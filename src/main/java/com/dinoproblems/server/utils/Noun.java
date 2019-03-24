package com.dinoproblems.server.utils;

import java.util.Objects;

/**
 * Created by Katushka on 27.02.2019.
 */
public class Noun implements AbstractNoun {
    private final GeneratorUtils.Gender gender;

    private final String nominative;
    private final String genitive;
    private final String countingForm;
    private final String pluralForm;
    private final String instrumentalForm; // творительный падеж
    private final String accusativeForm;
    private final String accusativePluralForm;

    Noun(String nominative, String genitive, String countingForm, String pluralForm, GeneratorUtils.Gender gender) {
        this.gender = gender;
        this.nominative = nominative;
        this.genitive = genitive;
        this.countingForm = countingForm;
        this.pluralForm = pluralForm;
        this.instrumentalForm = null;
        this.accusativeForm = null;
        this.accusativePluralForm = null;
    }

    public Noun(GeneratorUtils.Gender gender, String nominative, String genitive, String countingForm, String pluralForm, String instrumentalForm, String accusativeForm, String accusativePluralForm) {
        this.gender = gender;
        this.nominative = nominative;
        this.genitive = genitive;
        this.countingForm = countingForm;
        this.pluralForm = pluralForm;
        this.instrumentalForm = instrumentalForm;
        this.accusativeForm = accusativeForm;
        this.accusativePluralForm = accusativePluralForm;
    }

    public String getInstrumentalForm() {
        return instrumentalForm;
    }

    public String getAccusativeForm() {
        return accusativeForm;
    }

    @Override
    public String getAccusativePluralForm() {
        return accusativePluralForm != null ? accusativePluralForm : (pluralForm != null ? pluralForm : accusativeForm);
    }

    @Override
    public GeneratorUtils.Gender getGender() {
        return gender;
    }

    @Override
    public String getNominative() {
        return nominative;
    }

    @Override
    public String getGenitive() {
        return genitive;
    }

    @Override
    public String getCountingGenitive() {
        return genitive;
    }

    @Override
    public String getCountingForm() {
        return countingForm;
    }

    @Override
    public String getPluralForm() {
        return pluralForm;
    }

    @Override
    public String toString() {
        return "Noun{" +
                "gender=" + gender +
                ", nominative='" + nominative + '\'' +
                ", genitive='" + genitive + '\'' +
                ", countingForm='" + countingForm + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Noun noun = (Noun) o;

        return Objects.equals(nominative, noun.nominative);
    }

    @Override
    public int hashCode() {
        return nominative != null ? nominative.hashCode() : 0;
    }
}
