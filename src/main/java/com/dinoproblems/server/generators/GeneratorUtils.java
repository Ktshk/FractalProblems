package com.dinoproblems.server.generators;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

import static com.dinoproblems.server.generators.GeneratorUtils.Gender.MASCULINE;
import static com.dinoproblems.server.generators.NumberWord.getStringForNumber;

/**
 * Created by Katushka on 10.02.2019.
 */
public class GeneratorUtils {
    enum Gender {
        MASCULINE,
        FEMININE,
        NEUTER
    }

    enum Case {
        NOMINATIVE,
        GENITIVE,
        ACCUSATIVE

        // TODO: add other cases
    }

    private static final String[] HUNDREDS = {"", "сто", "двести", "триста", "четыреста", "пятьсот", "шестьсот", "семьсот",
            "восемьсот", "девятьсот"};
    private static final String[] TENS = {"", "", "двадцать", "тридцать", "сорок", "пятьдесят", "шестьдесят", "семьдесят",
            "восемьдесят", "девяносто"};
    private static final String[] ONES = {"", "один", "два", "три", "четыре", "пять", "шесть", "семь", "восемь", "девять"};

    private GeneratorUtils() {

    }

    static String getMetersString(int meters) {
        return getNumWithString(meters, "метр", "метра", "метров", MASCULINE);
    }

    static String getNumWithString(int count, final String one, final String lessThanFive, final String moreThanFive, Gender gender) {
        return getNumWithString(count, one, lessThanFive, moreThanFive, gender, Case.NOMINATIVE);
    }

    static String getNumWithString(int count, AbstractNoun noun) {
        return getNumWithString(count, noun.getNominative(), noun.getGenitive(), noun.getCountingForm(), noun.getGender(), Case.NOMINATIVE);
    }

    static String getNumWithString(int count, final String[] wordForms, Gender gender) {
        return getNumWithString(count, wordForms[0], wordForms[1], wordForms[2], gender, Case.NOMINATIVE);
    }

    static String getNumWithString(int count, final String one, final String lessThanFive, final String moreThanFive, Gender gender, Case wordCase) {
        if (count >= 5 && count <= 20) {
            return count + " " + moreThanFive;
        } else {
            final int lastDigit = count % 10;
            if (lastDigit == 1) {
                if (gender == Gender.MASCULINE) {
                    return count + " " + one;
                } else {
                    return getStringForNumber(count, gender, wordCase) + " " + one;
                }
            } else if (lastDigit >= 2 && lastDigit <= 4) {
                if (lastDigit == 2 && gender != Gender.MASCULINE) {
                    return getStringForNumber(count, gender, wordCase) + " " + lessThanFive;
                }
                return count + " " + lessThanFive;
            } else {
                return count + " " + moreThanFive;
            }
        }
    }

    static int randomInt(int origin, int bound) {
        return ThreadLocalRandom.current().nextInt(origin, bound);
    }

    static String[] chooseRandomString(String[] array, int count) {
        return chooseRandom(array, count, String[]::new);
    }

    static <T> T[] chooseRandom(T[] array, int count, Function<Integer, T[]> arrayConstructor) {
        T[] arrayCopy = Arrays.copyOf(array, array.length);

        final T[] result = arrayConstructor.apply(count);
        for (int i = 0; i < count; i++) {
            int ind = randomInt(i, arrayCopy.length);
            result[i] = arrayCopy[ind];

            T t = arrayCopy[ind];
            arrayCopy[ind] = arrayCopy[i];
            arrayCopy[i] = t;
        }
        return result;
    }
}