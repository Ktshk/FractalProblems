package com.dinoproblems.server.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Katushka on 28.02.2019.
 */
public enum NumberWord {

    ONE(1, "один", "одного"),
    TWO(2, "два", "двух"),
    THREE(3, "три", "трех"),
    FOUR(4, "четыре", "четырех"),
    FIVE(5, "пять", "пяти"),
    SIX(6, "шесть", "шести"),
    SEVEN(7, "семь", "семи"),
    EIGHT(8, "восемь", "восьми"),
    NINE(9, "девять", "девяти"),
    TEN(10, "десять", "десяти"),
    ELEVEN(11, "одиннадцать", "одиннадцати"),
    TWELVE(12, "двенадцать", "двенадцати"),
    THIRTEEN(13, "тринадцать", "тринадцати"),
    FOURTEEN(14, "четырнадцать", "четырнадцати"),
    FIFTEEN(15, "пятнадцать", "пятнадцати"),
    SIXTEEN(16, "шестнадцать", "шестнадцати"),
    SEVENTEEN(17, "семнадцать", "семнадцати"),
    EIGHTEEN(18, "восемнадцать", "восемнадцати"),
    NINETEEN(19, "девятнадцать", "девятнадцати"),
    TWENTY(20, "двадцать", "двадцати"),
    THIRTY(30, "тридцать", "тридцати"),
    FORTY(40, "сорок", "сорок+а"),
    FIFTY(50, "пятьдесят", "пятидесяти"),
    SIXTY(60, "шестьдесят", "шестидесяти"),
    SEVENTY(70, "семьдесят", "семидесяти"),
    EIGHTY(80, "восемьдесят", "восьмидесяти"),
    NINETY(90, "девносто", "девяноста"),
    HUNDRED(100, "сто", "ста"),
    TWO_HUNDRED(200, "двести", "двести"),
    THREE_HUNDRED(300, "триста", "триста"),
    FOUR_HUNDRED(400, "четыреста", "четыреста"),
    FIVE_HUNDRED(500, "пятьсот", "пятиста"),
    SIX_HUNDRED(600, "шестьсот", "шестиста"),
    SEVEN_HUNDRED(700, "семьсот", "семиста"),
    EIGHT_HUNDRED(800, "восемьсот", "восьмиста"),
    NINE_HUNDRED(900, "девятьсот", "девятиста");

    private final int number;

    private Map<GeneratorUtils.Case, String> cases = new HashMap<>();

    private static Map<Integer, NumberWord> numberToWord = new HashMap<>();

    static {
        for (NumberWord numberWord : values()) {
            numberToWord.put(numberWord.getNumber(), numberWord);
        }
    }

    NumberWord(int number, String nominative, String genitive) {
        this.number = number;
        cases.put(GeneratorUtils.Case.GENITIVE, genitive);
        cases.put(GeneratorUtils.Case.NOMINATIVE, nominative);
        cases.put(GeneratorUtils.Case.ACCUSATIVE, nominative);
    }

    public int getNumber() {
        return number;
    }

    public String getGenitive() {
        return cases.get(GeneratorUtils.Case.GENITIVE);
    }

    public static String getStringForNumber(int number, GeneratorUtils.Gender gender, GeneratorUtils.Case wordCase) {
        if (number >= 1000) {
            throw new IllegalArgumentException();
        }
        String result = "";
        if (number / 100 > 0) {
            result += numberToWord.get(number / 100 * 100).cases.get(wordCase);
            number = number % 100;
        }
        if (number >= 20) {
            if (!result.isEmpty()) {
                result += " ";
            }
            result += numberToWord.get(number / 10 * 10).cases.get(wordCase);
            number = number % 10;
        }
        if (number > 0) {
            if (!result.isEmpty()) {
                result += " ";
            }
            if (number == 2) {
                result += getTwoInGender(gender, wordCase);
            } else if (number == 1) {
                result += getOneInGender(gender, wordCase);
            } else {
                result += numberToWord.get(number).cases.get(wordCase);
            }
        }

        return result;
    }

    private static String getTwoInGender(GeneratorUtils.Gender gender, GeneratorUtils.Case wordCase) {
        if (wordCase == GeneratorUtils.Case.GENITIVE) {
            return "двух";
        } else {
            if (gender == GeneratorUtils.Gender.FEMININE) {
                return "две";
            } else {
                return "два";
            }
        }
    }

    private static String getOneInGender(GeneratorUtils.Gender gender, GeneratorUtils.Case wordCase) {
        if (gender == GeneratorUtils.Gender.MASCULINE) {
            switch (wordCase) {
                case NOMINATIVE:
                case ACCUSATIVE:
                    return "один";
                case GENITIVE:
                    return "одного";

            }
        } else if (gender == GeneratorUtils.Gender.FEMININE) {
            switch (wordCase) {
                case NOMINATIVE:
                    return "одна";
                case ACCUSATIVE:
                    return "одну";
                case GENITIVE:
                    return "одной";
            }
        } else {
            switch (wordCase) {
                case NOMINATIVE:
                case ACCUSATIVE:
                    return "одно";
                case GENITIVE:
                    return "одного";
            }
        }
        return "один";
    }
}
