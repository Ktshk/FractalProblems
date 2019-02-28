package com.dinoproblems.server.generators;

/**
 * Created by Katushka on 27.02.2019.
 */
public class Adjective {
    private final String nominativeMasculine;
    private final String nominativeFeminine;
    private final String nominativeNeuter;
    private final String genitive;

    public Adjective(String nominativeMasculine, String nominativeFeminine, String nominativeNeuter, String genitive) {
        this.nominativeMasculine = nominativeMasculine;
        this.nominativeFeminine = nominativeFeminine;
        this.nominativeNeuter = nominativeNeuter;
        this.genitive = genitive;
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

    public String getGenitive() {
        return genitive;
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
}
