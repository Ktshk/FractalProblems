package com.dinoproblems.server.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Katushka on 04.03.2019.
 */
public class OrdinalNumber extends Adjective {
    private static Map<Integer, OrdinalNumber> numbers = new HashMap<>();

    public static final OrdinalNumber FIRST = new OrdinalNumber(1, "первый", "первая", "первое",
            "первых", "первого", "первой", "первые");
    public static final OrdinalNumber SECOND = new OrdinalNumber(2, "второй", "вторая", "второе",
            "вторых", "второго", "второй", "вторые");
    public static final OrdinalNumber THIRD = new OrdinalNumber(3, "третий", "третяя", "третее",
            "3", "третьего", "третьей", "3");
    public static final OrdinalNumber FORTH = new OrdinalNumber(4, "четвертый", "четвертая", "четвертое",
            "4", "четвертого", "четвертой", "4");
    public static final OrdinalNumber FIFTH = new OrdinalNumber(5, "пятый", "пятая", "пятое",
            "5", "пятого", "пятой", "5");
    public static final OrdinalNumber SIXTH = new OrdinalNumber(6, "шестой", "шестая", "шестое",
            "6", "шестого", "шестой", "5");
    public static final OrdinalNumber SEVENTH = new OrdinalNumber(7, "седьмой", "седьмая", "седьмое",
            "7", "седьмого", "седьмой", "7");
    public static final OrdinalNumber EIGHTH = new OrdinalNumber(8, "восьмой", "восьмая", "восьмое",
            "8", "восьмого", "восьмой", "8");
    public static final OrdinalNumber NINTH = new OrdinalNumber(9, "девятый", "девятая", "девятое",
            "9", "девятого", "девятой", "9");
    public static final OrdinalNumber TENTH = new OrdinalNumber(10, "десятый", "десятая", "десятое",
            "10", "десятого", "десятой", "10");

    private OrdinalNumber(int num, String nomMasculine, String nomFeminine, String nomNeuter, String countingForm, String genMasculine, String genFeminine, String plural) {
        super(nomMasculine, nomFeminine, nomNeuter, countingForm, genMasculine, genFeminine, plural);

        numbers.put(num, this);
    }

    public static OrdinalNumber number(int num) {
        return numbers.get(num);
    }
}
