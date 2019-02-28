package com.dinoproblems.server.generators;

import static com.dinoproblems.server.generators.GeneratorUtils.Gender.FEMININE;
import static com.dinoproblems.server.generators.GeneratorUtils.Gender.MASCULINE;
import static com.dinoproblems.server.generators.GeneratorUtils.Gender.NEUTER;

/**
 * Created by Katushka on 28.02.2019.
 */
public class Dictionary {
    final static Noun ORANGE = new Noun("апельсин", "апельсина", "апельсинов", MASCULINE);
    final static Noun BANANA = new Noun("банан", "банана", "бананов", MASCULINE);
    final static Noun PEAR = new Noun("груша", "груши", "груш", FEMININE);
    final static Noun GIRL = new Noun("девочка", "девочки", "девочек", FEMININE);
    final static Adjective YELLOW = new Adjective("жёлтый", "жёлтая", "жёлтое", "жёлтых");
    final static Adjective GREEN = new Adjective("зелёный", "зелёная", "зелёное", "зелёных");
    final static Noun CANDY = new Noun("конфета", "конфеты", "конфет", FEMININE);
    final static Adjective RED = new Adjective("красный", "красная", "красное", "красных");
    final static Noun LOLLIPOP = new Noun("леденец", "леденца", "леденцов", MASCULINE);
    final static Noun BOY = new Noun("мальчик", "мальчика", "мальчиков", MASCULINE);
    final static Noun JELLY_CANDY = new Noun("мармеладная конфета", "мармеладных конфеты", "мармеладных конфет", FEMININE);
    final static Noun DAFFODIL = new Noun("нарцисс", "нарцисса", "нарциссов", MASCULINE);
    final static Noun CHILD = new Noun("ребенок", "ребенка", "детей", MASCULINE);
    final static Noun ROSE = new Noun("роза", "розы", "роз", FEMININE);
    final static Noun CAMOMILE = new Noun("ромашка", "ромашки", "ромашек", FEMININE);
    final static Adjective BLUE = new Adjective("синий", "синяя", "синее", "синих");
    final static Noun TULIP = new Noun("тюльпан", "тюльпана", "тюльпанов", MASCULINE);
    final static Noun FRUIT = new Noun("фрукт", "фрукта", "фруктов", MASCULINE);
    final static Noun FLOWER = new Noun("цветок", "цветка", "цветов", MASCULINE);
    final static Noun BALL = new Noun("шарик", "шарика", "шариков", MASCULINE);
    final static Noun CHOCOLATE_CANDY = new Noun("шоколадная конфета", "шоколадных конфеты", "шоколадных конфет", FEMININE);
    final static Noun APPLE = new Noun("яблоко", "яблока", "яблок", NEUTER);

    private Dictionary() {

    }
}
