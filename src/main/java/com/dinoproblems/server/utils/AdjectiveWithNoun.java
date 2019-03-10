package com.dinoproblems.server.utils;

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
        return adjective.getGenitiveForm(noun.getGender()) + " " + noun.getGenitive();
    }

    @Override
    public String getCountingGenitive() {
        return adjective.getCountingForm() + " " + noun.getCountingGenitive();
    }

    @Override
    public String getCountingForm() {
        return adjective.getCountingForm() + " " + noun.getCountingForm();
    }

    @Override
    public String getPluralForm() {
        return adjective.getPluralForm() + " " + noun.getPluralForm();
    }

    @Override
    public String getInstrumentalForm() {
        return adjective.getInstrumentalForm(noun.getGender()) + " " + noun.getInstrumentalForm();
    }

    @Override
    public String getAccusativeForm() {
        return adjective.getAccusativeForm(noun.getGender()) + " " + noun.getAccusativeForm();
    }

    @Override
    public String getAccusativePluralForm() {
        return (noun.getPluralForm() != null ? getPluralForm() : getAccusativeForm());
    }
}
