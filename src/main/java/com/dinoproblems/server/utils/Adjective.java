package com.dinoproblems.server.utils;

import com.dinoproblems.server.utils.GeneratorUtils;

/**
 * Created by Katushka on 27.02.2019.
 */
public class Adjective {
    private final String nominativeMasculine;
    private final String nominativeFeminine;
    private final String nominativeNeuter;
    private final String countingForm;
    private final String genitiveMasculine;
    private final String genitiveFeminine;
    private final String pluralForm;


    public Adjective(String nominativeMasculine, String nominativeFeminine, String nominativeNeuter, String countingForm, String genitiveMasculine, String genitiveFeminine, String pluralForm) {
        this.nominativeMasculine = nominativeMasculine;
        this.nominativeFeminine = nominativeFeminine;
        this.nominativeNeuter = nominativeNeuter;
        this.countingForm = countingForm;
        this.genitiveMasculine = genitiveMasculine;
        this.genitiveFeminine = genitiveFeminine;
        this.pluralForm = pluralForm;
    }

    public String getNominativeMasculine() {
        return nominativeMasculine;
    }

    public String getNominativeFeminine() {
        return nominativeFeminine;
    }

    public String getNominativeNeuter() {
        return nominativeNeuter;
    }

    public String getGenitiveMasculine() {
        return genitiveMasculine;
    }

    public String getGenitiveFeminine() {
        return genitiveFeminine;
    }

    public String getCountingForm() {
        return countingForm;
    }

    public String getPluralForm() {
        return pluralForm;
    }

    public String getGenitiveForm(GeneratorUtils.Gender gender) {
        switch (gender) {
            case MASCULINE:
            case NEUTER:
                return genitiveMasculine;
            case FEMININE:
                return genitiveFeminine;
        }
        throw new IllegalArgumentException();
    }

    public String getNominative(GeneratorUtils.Gender gender) {
        switch (gender) {
            case MASCULINE:
                return getNominativeMasculine();
            case FEMININE:
                return getNominativeFeminine();
            case NEUTER:
                return getNominativeNeuter();
        }
        throw new IllegalArgumentException();
    }

    public String getAccusativeForm(GeneratorUtils.Gender gender) {
        // TODO: implement
        return getNominative(gender);
    }

    public String getInstrumentalForm(GeneratorUtils.Gender gender) {
        // TODO: implement
        return getGenitiveForm(gender);
    }
}
