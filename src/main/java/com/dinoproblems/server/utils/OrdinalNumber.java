package com.dinoproblems.server.utils;

import java.util.HashMap;
import java.util.Map;

import static com.dinoproblems.server.utils.GeneratorUtils.Gender.FEMININE;


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
    public static final OrdinalNumber ELEVENTH = new OrdinalNumber(11, "одиннадцатый", "одиннадцатая", "одиннадцатое",
            "11", "одиннадцатого", "одиннадцатой", "11");
    public static final OrdinalNumber TWELFTH = new OrdinalNumber(12, "двенадцатый", "двенадцатая", "двенадцатое",
            "12", "двенадцатого", "двенадцатой", "12");
    public static final OrdinalNumber THIRTEENTH = new OrdinalNumber(13, "тринадцатый", "тринадцатая", "тринадцатое",
            "13", "тринадцатого", "тринадцатой", "13");
    public static final OrdinalNumber FOURTEENTH = new OrdinalNumber(14, "четырнадцатый", "четырнадцатая", "четырнадцатое",
            "14", "четырнадцатого", "четырнадцатой", "14");
    public static final OrdinalNumber FIFTEENTH = new OrdinalNumber(15, "пятнадцатый", "пятнадцатая", "пятнадцатое",
            "15", "пятнадцатого", "пятнадцатой", "15");
    public static final OrdinalNumber SIXTEENTH = new OrdinalNumber(16, "шестнцадцатый", "шестнадцатая", "шестнадцатое",
            "16", "шестнадцатого", "шестнадцатой", "16");
    public static final OrdinalNumber SEVENTEENTH = new OrdinalNumber(17, "семнадцатый", "семнадцатая", "семнадцатое",
            "17", "семнадцатого", "семнадцатой", "17");
    public static final OrdinalNumber EIGHTEENTH = new OrdinalNumber(18, "восемнадцатый", "восемнадцатая", "восемнадцатое",
            "18", "восемнадцатого", "восемнадцатой", "18");
    public static final OrdinalNumber NINETEENTH = new OrdinalNumber(19, "девятнадцатый", "девятнадцатая", "девятнадцатое",
            "19", "девятнадцатого", "девятнадцатой", "19");
    public static final OrdinalNumber TWENTIETH = new OrdinalNumber(20, "двадцатый", "двадцатая", "двадцатое",
            "20", "двадцатого", "двадцатой", "20");
    public static final OrdinalNumber THIRTIETH = new OrdinalNumber(30, "тридцатый", "тридцатая", "тридцатое",
            "30", "тридцатого", "тридцатой", "20");
    public static final OrdinalNumber FORTIETH = new OrdinalNumber(40, "сороковой", "сороковая", "сороковое",
            "40", "сорокового", "сороковой", "40");

    private static final String[] TENS = {"", "", "двадцать", "тридцать", "сорок", "пятьдесят", "шестьдесят", "семьдесят",
            "восемьдесят", "девяносто"};

    private OrdinalNumber(int num, String nomMasculine, String nomFeminine, String nomNeuter, String countingForm, String genMasculine, String genFeminine, String plural) {
        super(nomMasculine, nomFeminine, nomNeuter, countingForm, genMasculine, genFeminine, plural);

        numbers.put(num, this);
    }

    public static OrdinalNumber getOrdinalTwoDigitNum(int num)
    {
        if(num < 21 || num % 10 == 0) {
            return numbers.get(num);
        }
        int firstDigit = num / 10;
        int secondDigit = num % 10;
        String digit = TENS[firstDigit];
        digit += " ";
        OrdinalNumber secondOrdinalDigit = numbers.get(secondDigit);
        OrdinalNumber result = new OrdinalNumber
                (
                        num,
                        digit + secondOrdinalDigit.getNominativeMasculine(),
                        digit + secondOrdinalDigit.getNominativeFeminine(),
                        digit + secondOrdinalDigit.getNominativeNeuter(),
                        digit + secondOrdinalDigit.getCountingForm(),
                        digit + secondOrdinalDigit.getGenitiveMasculine(),
                        digit + secondOrdinalDigit.getGenitiveFeminine(),
                        Integer.toString(num));
        return  result;
    }

    public static OrdinalNumber number(int num) {
        return numbers.get(num);
    }
}
