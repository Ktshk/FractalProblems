package com.dinoproblems.server.generators;

/**
 * Created by Katushka on 27.02.2019.
 */
public interface AbstractNoun {

    GeneratorUtils.Gender getGender();

    String getNominative();

    String getGenitive();

    String getCountingForm();
}
