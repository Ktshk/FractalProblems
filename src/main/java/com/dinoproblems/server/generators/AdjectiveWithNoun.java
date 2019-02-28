package com.dinoproblems.server.generators;

/**
 * Created by Katushka on 27.02.2019.
 */
public class AdjectiveWithNoun implements AbstractNoun {
    private final Adjective adjective;
    private final AbstractNoun noun;

    public AdjectiveWithNoun(Adjective adjective, AbstractNoun noun) {
        this.adjective = adjective;
        this.noun = noun;
    }


    @Override
    public GeneratorUtils.Gender getGender() {
        return noun.getGender();
    }

    @Override
    public String getNominative() {
        return adjective.getNominative(noun.getGender()) + " " + noun.getNominative();
    }

    @Override
    public String getGenitive() {
        return adjective.getGenitive() + " " + noun.getGenitive();
    }

    @Override
    public String getCountingForm() {
        return adjective.getGenitive() + " " + noun.getCountingForm();
    }
}
