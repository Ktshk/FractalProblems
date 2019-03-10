package com.dinoproblems.server.utils;

/**
 * Created by Katushka on 27.02.2019.
 */
public interface AbstractNoun {

    GeneratorUtils.Gender getGender();

    String getNominative();

    String getGenitive();

    String getCountingGenitive();

    String getCountingForm();

    String getPluralForm();

    String getInstrumentalForm();

    String getAccusativeForm();

    String getAccusativePluralForm();

}
