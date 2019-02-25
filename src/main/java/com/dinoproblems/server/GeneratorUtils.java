package com.dinoproblems.server;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import static com.dinoproblems.server.GeneratorUtils.Gender.MASCULINE;

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

    static String getNumWithString(int count, final String one, final String lessThanFive, final String moreThanFive, Gender gender, Case wordCase) {
        if (count >= 5 && count <= 20) {
            return count + " " + moreThanFive;
        } else {
            final int lastDigit = count % 10;
            if (lastDigit == 1) {
                if (gender == Gender.MASCULINE) {
                    return count + " " + one;
                } else {
                    return getNumberEndsWithOneOrTwo(count, gender, wordCase) + " " + one;
                }
            } else if (lastDigit >= 2 && lastDigit <= 4) {
                if (lastDigit == 2 && gender == Gender.FEMININE) {
                    return getNumberEndsWithOneOrTwo(count, gender, wordCase) + " " + lessThanFive;
                }
                return count + " " + lessThanFive;
            } else {
                return count + " " + moreThanFive;
            }
        }
    }

    private static String getNumberEndsWithOneOrTwo(int count, Gender gender, Case wordCase) {
        String result = "";

        if (count / 100 > 0) {
            result += HUNDREDS[count / 100];
            count = count / 100;
        }

        if (count / 10 > 0) {
            if (!result.isEmpty()) {
                result += " ";
            }
            result += TENS[count / 10];
            count = count / 10;
        }

        if (count % 10 == 1) {
            if (!result.isEmpty()) {
                result += " ";
            }
            if (gender == Gender.MASCULINE) {
                switch (wordCase) {
                    case NOMINATIVE:
                    case ACCUSATIVE:
                        return result + "один";

                }
            } else if (gender == Gender.FEMININE) {
                switch (wordCase) {
                    case NOMINATIVE:
                        return result + "одна";
                    case ACCUSATIVE:
                        return result + "одну";
                }
            } else {
                switch (wordCase) {
                    case NOMINATIVE:
                    case ACCUSATIVE:
                        return result + "одно";
                }
            }
        } else if (count % 10 == 2) {
            if (!result.isEmpty()) {
                result += " ";
            }
            if (gender == Gender.FEMININE) {
                result += "две";
            } else {
                result += "два";
            }
        } else if (count % 10 > 0) {
            if (!result.isEmpty()) {
                result += " ";
            }
            result += ONES[count % 10];
        }

        return result;
    }

    static int randomInt(int origin, int bound) {
        return ThreadLocalRandom.current().nextInt(origin, bound);
    }

    static String[] chooseRandom(String[] array, int count) {
        String[] arrayCopy = Arrays.copyOf(array, array.length);

        final String[] result = new String[count];
        for (int i = 0; i < count; i++) {
            int ind = randomInt(i, arrayCopy.length);
            result[i] = arrayCopy[ind];

            String t = arrayCopy[ind];
            arrayCopy[ind] = arrayCopy[i];
            arrayCopy[i] = t;
        }
        return result;
    }
}
