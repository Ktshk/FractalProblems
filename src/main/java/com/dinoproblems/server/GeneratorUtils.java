package com.dinoproblems.server;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Katushka on 10.02.2019.
 */
public class GeneratorUtils {
    private GeneratorUtils() {

    }

    static String getNumWithString(int legs, final String one, final String lessThanFive, final String moreThanFive) {
        if (legs >= 5 && legs <= 20) {
            return legs + " " + moreThanFive;
        } else {
            final int lastDigit = legs % 10;
            if (lastDigit == 1) {
                return legs + " " + one;
            } else if (lastDigit >= 2 && lastDigit <= 4) {
                return legs + " " + lessThanFive;
            } else {
                return legs + " " + moreThanFive;
            }
        }
    }

    static int randomInt(int origin, int bound) {
        return ThreadLocalRandom.current().nextInt(origin, bound);
    }
}
