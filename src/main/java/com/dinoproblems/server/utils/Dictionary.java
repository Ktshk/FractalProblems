package com.dinoproblems.server.utils;

import static com.dinoproblems.server.utils.GeneratorUtils.Gender.FEMININE;
import static com.dinoproblems.server.utils.GeneratorUtils.Gender.MASCULINE;
import static com.dinoproblems.server.utils.GeneratorUtils.Gender.NEUTER;

/**
 * Created by Katushka on 28.02.2019.
 */
public class Dictionary {
    public final static Noun ORANGE = new NounBuilder().nom("апельсин").gen("апельсина").counting("апельсинов").plural("апельсины").gender(MASCULINE).createNoun();
    public final static Noun BANANA = new NounBuilder().nom("банан").gen("банана").counting("бананов").plural("бананы").gender(MASCULINE).createNoun();
    public final static Noun BIOLOGY = new NounBuilder().nom("биология").gen("биологии").instr("биологией").acc("биологию").gender(FEMININE).createNoun();
    public final static Noun SHOE = new NounBuilder().nom("ботинок").gen("ботинка").counting("ботинок").plural("ботинки").gender(MASCULINE).createNoun();
    public final static Noun PEAR = new NounBuilder().nom("груша").gen("груши").counting("груш").plural("груши").gender(FEMININE).createNoun();
    public final static Noun GIRL = new NounBuilder().nom("девочка").gen("девочки").counting("девочек").plural("девочки").gender(FEMININE).createNoun();
    public final static Noun HISTORY = new NounBuilder().nom("история").gen("истории").instr("историей").acc("историю").gender(FEMININE).createNoun();
    public final static Noun PENCIL = new NounBuilder().nom("карандаш").gen("карандаша").counting("карандашей").plural("карандаши").gender(MASCULINE).createNoun();
    public final static Noun CANDY = new NounBuilder().nom("конфета").gen("конфеты").counting("конфет").plural("конфеты").gender(FEMININE).createNoun();
    public final static Noun SKATES = new NounBuilder().nom("конёк").gen("конька").counting("коньков").plural("коньки").gender(MASCULINE).createNoun();
    public final static Noun LOLLIPOP = new NounBuilder().nom("леденец").gen("леденца").counting("леденцов").plural("леденцы").instr("леденцами").gender(MASCULINE).createNoun();
    public final static Noun BOY = new NounBuilder().nom("мальчик").gen("мальчика").counting("мальчиков").plural("мальчики").gender(MASCULINE).createNoun();
    public final static Noun JELLY_CANDY = new NounBuilder().nom("мармеладная конфета").gen("мармеладных конфеты").counting("мармеладных конфет").instr("мармеладными конфетами").plural("мармеладные конфеты").gender(FEMININE).createNoun();
    public final static Noun MATH = new NounBuilder().nom("математика").gen("математики").instr("математикой").acc("математику").gender(FEMININE).createNoun();
    public final static Noun MUSIC = new NounBuilder().nom("музыка").gen("музыки").instr("музыкой").acc("музыку").gender(FEMININE).createNoun();
    public final static Noun DAFFODIL = new NounBuilder().nom("нарцисс").gen("нарцисса").counting("нарциссов").plural("нарциссы").gender(MASCULINE).createNoun();
    public final static Noun SOCK = new NounBuilder().nom("носок").gen("носка").counting("носков").plural("носки").gender(MASCULINE).createNoun();
    public final static Noun GLOVE = new NounBuilder().nom("перчатка").gen("перчатки").counting("перчаток").plural("перчатки").gender(FEMININE).createNoun();
    public final static Noun SUBJECT = new NounBuilder().nom("предмет").gen("предмета").acc("предмет").instr("предметом").counting("предметов").plural("предметы").gender(FEMININE).createNoun();
    public final static Noun CHILD = new NounBuilder().nom("ребенок").gen("ребенка").counting("детей").plural("дети").gender(MASCULINE).createNoun();
    public final static Noun ROSE = new NounBuilder().nom("роза").gen("розы").counting("роз").plural("розы").gender(FEMININE).createNoun();
    public final static Noun CAMOMILE = new NounBuilder().nom("ромашка").gen("ромашки").counting("ромашек").plural("ромашки").gender(FEMININE).createNoun();
    public final static Noun SPORT = new NounBuilder().nom("спорт").gen("спорта").instr("спортом").acc("спорт").gender(MASCULINE).createNoun();
    public final static Noun PAGE = new NounBuilder().nom("страница").gen("страницы").instr("страницей").acc("страницу").counting("страниц").gender(MASCULINE).createNoun();
    public final static Noun TULIP = new NounBuilder().nom("тюльпан").gen("тюльпана").counting("тюльпанов").plural("тюльпаны").gender(MASCULINE).createNoun();
    public final static Noun STUDENT = new NounBuilder().nom("ученик").gen("ученика").counting("учеников").plural("ученики").gender(MASCULINE).createNoun();
    public final static Noun PHYSICS = new NounBuilder().nom("физика").gen("физики").instr("физикой").acc("физику").gender(FEMININE).createNoun();
    public final static Noun FRUIT = new NounBuilder().nom("фрукт").gen("фрукта").counting("фруктов").plural("фрукты").gender(MASCULINE).createNoun();
    public final static Noun CHEMISTRY = new NounBuilder().nom("химия").gen("химии").instr("химией").acc("химию").gender(FEMININE).createNoun();
    public final static Noun FLOWER = new NounBuilder().nom("цветок").gen("цветка").counting("цветов").plural("цветы").gender(MASCULINE).createNoun();
    public final static Noun BALL = new NounBuilder().nom("шарик").gen("шарика").counting("шариков").plural("шарики").gender(MASCULINE).createNoun();
    public final static Noun CHOCOLATE_CANDY = new NounBuilder().nom("шоколадная конфета").gen("шоколадных конфеты").counting("шоколадных конфет").instr("шоколадными конфетами").plural("шоколадные конфеты").gender(FEMININE).createNoun();
    public final static Noun APPLE = new NounBuilder().nom("яблоко").gen("яблока").counting("яблок").plural("яблоки").gender(NEUTER).createNoun();

    public static final AbstractNoun[] FRUITS = {APPLE, PEAR, ORANGE, BANANA};
    public static final AbstractNoun[] CANDIES = {CHOCOLATE_CANDY, JELLY_CANDY, LOLLIPOP};
    public static final AbstractNoun[] SUBJECTS = {MUSIC, BIOLOGY, MATH, PHYSICS, CHEMISTRY, HISTORY};

    public final static Adjective YELLOW = new Adjective("жёлтый", "жёлтая", "жёлтое", "жёлтых", "жёлтого", "жёлтой", "жёлтые");
    public final static Adjective GREEN = new Adjective("зелёный", "зелёная", "зелёное", "зелёных", "зелёного", "зелёной", "зелёные");
    public final static Adjective RED = new Adjective("красный", "красная", "красное", "красных", "красного", "красной", "красные");
    public final static Adjective BLUE = new Adjective("синий", "синяя", "синее", "синих", "синего", "синей", "синие");
    public final static Adjective BLACK = new Adjective("чёрный", "чёрная", "чёрное", "чёрных", "чёрного", "чёрной", "чёрные");



    private Dictionary() {

    }
}
