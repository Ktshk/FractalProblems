package com.dinoproblems.server.generators;

/**
 * Created by Katushka on 27.02.2019.
 */
public class Noun implements AbstractNoun {
    private final GeneratorUtils.Gender gender;

    private final String nominative;
    private final String genitive;
    private final String countingForm;

    public Noun(String nominative, String genitive, String countingForm, GeneratorUtils.Gender gender) {
        this.gender = gender;
        this.nominative = nominative;
        this.genitive = genitive;
        this.countingForm = countingForm;
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
    public String getCountingForm() {
        return countingForm;
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

        return nominative != null ? nominative.equals(noun.nominative) : noun.nominative == null;
    }

    @Override
    public int hashCode() {
        return nominative != null ? nominative.hashCode() : 0;
    }
}
