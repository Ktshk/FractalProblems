package com.dinoproblems.server.utils;

/**
 * Created by Katushka on 20.03.2019.
 */
public class Verb {
    private final String singular;
    private final String plural;

    public Verb(String singular, String plural) {
        this.singular = singular;
        this.plural = plural;
    }

    public String getSingular() {
        return singular;
    }

    public String getPlural() {
        return plural;
    }
}
