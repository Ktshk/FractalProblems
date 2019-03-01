package com.dinoproblems.server.generators;

import static com.dinoproblems.server.generators.GeneratorUtils.Gender.FEMININE;
import static com.dinoproblems.server.generators.GeneratorUtils.Gender.MASCULINE;
import static com.dinoproblems.server.generators.GeneratorUtils.Gender.NEUTER;

/**
 * Created by Katushka on 28.02.2019.
 */
public class Dictionary {
    final static Noun ORANGE = new Noun("апельсин", "апельсина", "апельсинов", "апельсины", MASCULINE);
    final static Noun BANANA = new Noun("банан", "банана", "бананов", "бананы", MASCULINE);
    final static Noun SHOE = new Noun("ботинок", "ботинка", "ботинок", "бьотинки", MASCULINE);
    final static Noun PEAR = new Noun("груша", "груши", "груш", "груши", FEMININE);
    final static Noun GIRL = new Noun("девочка", "девочки", "девочек", "девочки", FEMININE);
    final static Adjective YELLOW = new Adjective("жёлтый", "жёлтая", "жёлтое", "жёлтых", "жёлтого", "жёлтой", "жёлтые");
    final static Adjective GREEN = new Adjective("зелёный", "зелёная", "зелёное", "зелёных", "зелёного", "зелёной", "зелёные");
    final static Noun PENCIL = new Noun("карандаш", "карандаша", "карандашей", "карандаши", MASCULINE);
    final static Noun CANDY = new Noun("конфета", "конфеты", "конфет", "конфеты", FEMININE);
    final static Noun SKATES = new Noun("конёк", "конька", "коньков", "коньки", MASCULINE);
    final static Adjective RED = new Adjective("красный", "красная", "красное", "красных", "красного", "красной", "красные");
    final static Noun LOLLIPOP = new Noun("леденец", "леденца", "леденцов", "леденцы", MASCULINE);
    final static Noun BOY = new Noun("мальчик", "мальчика", "мальчиков", "мальчики", MASCULINE);
    final static Noun JELLY_CANDY = new Noun("мармеладная конфета", "мармеладных конфеты", "мармеладных конфет", "мармеладные конфеты", FEMININE);
    final static Noun DAFFODIL = new Noun("нарцисс", "нарцисса", "нарциссов", "нарциссы", MASCULINE);
    final static Noun SOCK = new Noun("носок", "носка", "носков", "носки", MASCULINE);
    final static Noun GLOVE = new Noun("перчатка", "перчатки", "перчаток", "перчатки", FEMININE);
    final static Noun CHILD = new Noun("ребенок", "ребенка", "детей", "дети", MASCULINE);
    final static Noun ROSE = new Noun("роза", "розы", "роз", "розы", FEMININE);
    final static Noun CAMOMILE = new Noun("ромашка", "ромашки", "ромашек", "ромашки", FEMININE);
    final static Adjective BLUE = new Adjective("синий", "синяя", "синее", "синих", "синего", "синей", "синие");
    final static Noun TULIP = new Noun("тюльпан", "тюльпана", "тюльпанов", "тюльпаны", MASCULINE);
    final static Noun FRUIT = new Noun("фрукт", "фрукта", "фруктов", "фрукты", MASCULINE);
    final static Noun FLOWER = new Noun("цветок", "цветка", "цветов", "цветы", MASCULINE);
    final static Adjective BLACK = new Adjective("чёрный", "чёрная", "чёрное", "чёрных", "чёрного", "чёрной", "чёрные");
    final static Noun BALL = new Noun("шарик", "шарика", "шариков", "шарики", MASCULINE);
    final static Noun CHOCOLATE_CANDY = new Noun("шоколадная конфета", "шоколадных конфеты", "шоколадных конфет", "шоколадные конфеты", FEMININE);
    final static Noun APPLE = new Noun("яблоко", "яблока", "яблок", "яблоки", NEUTER);

    private Dictionary() {

    }
}
