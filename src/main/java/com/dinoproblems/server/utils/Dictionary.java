package com.dinoproblems.server.utils;

import static com.dinoproblems.server.utils.GeneratorUtils.Gender.FEMININE;
import static com.dinoproblems.server.utils.GeneratorUtils.Gender.MASCULINE;
import static com.dinoproblems.server.utils.GeneratorUtils.Gender.NEUTER;

/**
 * Created by Katushka on 28.02.2019.
 */
public class Dictionary {
    public final static Noun ORANGE = new NounBuilder().nom("апельсин").gen("апельсина").counting("апельсинов").plural("апельсины").gender(MASCULINE).createNoun();
    public final static Noun ASTER = new NounBuilder().nom("астра").gen("астры").counting("астр").plural("астры").gender(FEMININE).createNoun();
    public final static Noun SCORE=new NounBuilder().nom("балл").gen("балла").counting("баллов").plural("баллы").gender(MASCULINE).createNoun();
    public final static Noun BANANA = new NounBuilder().nom("банан").gen("банана").counting("бананов").plural("бананы").gender(MASCULINE).createNoun();
    public final static Noun BIOLOGY = new NounBuilder().nom("биология").gen("биологии").instr("биологией").acc("биологию").gender(FEMININE).createNoun();
    public final static Noun SHOE = new NounBuilder().nom("ботинок").gen("ботинка").counting("ботинок").plural("ботинки").gender(MASCULINE).createNoun();
    public final static Noun BROTHER = new NounBuilder().nom("брат").gen("брата").counting("братьев").plural("братья").gender(MASCULINE).createNoun();
    public final static Noun BOUQUET = new NounBuilder().nom("букет").gen("букета").counting("букетов").plural("букеты").gender(MASCULINE).createNoun();
    public final static Noun PINK_FLOWER = new NounBuilder().nom("гвоздика").gen("гвоздики").counting("гвоздик").plural("гвоздики").gender(FEMININE).createNoun();
    public final static Noun PEAR = new NounBuilder().nom("груша").gen("груши").counting("груш").plural("груши").gender(FEMININE).createNoun();
    public final static Noun GIRL = new NounBuilder().nom("девочка").gen("девочки").counting("девочек").plural("девочки").gender(FEMININE).createNoun();
    public final static Noun STAR = new NounBuilder().nom("звёздочка").gen("звёздочки").counting("звёздочек").plural("звёздочки").acc("звёздочку").gender(FEMININE).createNoun();
    public final static Noun HISTORY = new NounBuilder().nom("история").gen("истории").instr("историей").acc("историю").gender(FEMININE).createNoun();
    public final static Noun PENCIL = new NounBuilder().nom("карандаш").gen("карандаша").counting("карандашей").plural("карандаши").gender(MASCULINE).createNoun();
    public final static Noun CANDY = new NounBuilder().nom("конфета").gen("конфеты").counting("конфет").plural("конфеты").gender(FEMININE).createNoun();
    public final static Noun SKATES = new NounBuilder().nom("конёк").gen("конька").counting("коньков").plural("коньки").gender(MASCULINE).createNoun();
    public final static Noun CANDY_BOX = new NounBuilder().nom("коробка конфет").gen("коробки конфет").counting("коробок конфет").plural("коробки конфет").gender(FEMININE).createNoun();
    public final static Noun BRICK = new NounBuilder().nom("кубик").gen("кубика").counting("кубиков").plural("кубики").gender(MASCULINE).createNoun();
    public final static Noun LOLLIPOP = new NounBuilder().nom("леденец").acc("леденец").gen("леденца").counting("леденцов").plural("леденцы").instr("леденцом").gender(MASCULINE).createNoun();
    public final static Noun BOY = new NounBuilder().nom("мальчик").gen("мальчика").counting("мальчиков").plural("мальчики").gender(MASCULINE).createNoun();
    public final static Noun JELLY_CANDY = new NounBuilder().nom("мармеладная конфета").acc("мармеладную конфету").gen("мармеладных конфеты").counting("мармеладных конфет").instr("мармеладной конфетой").plural("мармеладные конфеты").gender(FEMININE).createNoun();
    public final static Noun MATH = new NounBuilder().nom("математика").gen("математики").instr("математикой").acc("математику").gender(FEMININE).createNoun();
    public final static Noun COIN = new NounBuilder().nom("монета").gen("монеты").counting("монет").plural("монеты").acc("монету").gender(FEMININE).createNoun();
    public final static Noun MUSIC = new NounBuilder().nom("музыка").gen("музыки").instr("музыкой").acc("музыку").gender(FEMININE).createNoun();
    public final static Noun DAFFODIL = new NounBuilder().nom("нарцисс").gen("нарцисса").counting("нарциссов").plural("нарциссы").gender(MASCULINE).createNoun();
    public final static Noun SOCK = new NounBuilder().nom("носок").gen("носка").counting("носков").plural("носки").gender(MASCULINE).createNoun();
    public final static Noun STICK = new NounBuilder().nom("палочка").gen("палочки").counting("палочек").plural("палочки").acc("палочку").gender(FEMININE).createNoun();
    public final static Noun PAIR = new NounBuilder().nom("пара").gen("пары").counting("пар").plural("пары").gender(FEMININE).createNoun();
    public final static Noun PARTY = new NounBuilder().nom("партия").gen("партии").counting("партий").plural("партии").gender(FEMININE).createNoun();
    public final static Noun GLOVE = new NounBuilder().nom("перчатка").gen("перчатки").counting("перчаток").plural("перчатки").gender(FEMININE).createNoun();
    public final static Noun BUYER = new NounBuilder().nom("покупатель").gen("покупателя").counting("покупателей").plural("покупатели").gender(MASCULINE).createNoun();
    public final static Noun SUBJECT = new NounBuilder().nom("предмет").gen("предмета").acc("предмет").instr("предметом").counting("предметов").plural("предметы").gender(FEMININE).createNoun();
    public final static Noun BUTTON = new NounBuilder().nom("пуговица").gen("пуговицы").counting("пуговиц").plural("пуговицы").acc("пуговицу").gender(FEMININE).createNoun();
    public final static Noun CHILD = new NounBuilder().nom("ребенок").gen("ребенка").counting("детей").plural("дети").gender(MASCULINE).createNoun();
    public final static Noun ROSE = new NounBuilder().nom("роза").gen("розы").counting("роз").plural("розы").gender(FEMININE).createNoun();
    public final static Noun CAMOMILE = new NounBuilder().nom("ромашка").gen("ромашки").counting("ромашек").plural("ромашки").gender(FEMININE).createNoun();
    public final static Noun SECOND = new NounBuilder().nom("секунда").gen("секунды").counting("секунд").plural("секунды").gender(FEMININE).createNoun();
    public final static Noun SISTER = new NounBuilder().nom("сестра").gen("сестры").counting("сестёр").plural("сёстры").gender(FEMININE).createNoun();
    public final static Noun SWEETS = new NounBuilder().nom("сладость").gen("сладости").counting("сладостей").plural("сладости").gender(FEMININE).createNoun();
    public final static Noun SPORT = new NounBuilder().nom("спорт").gen("спорта").instr("спортом").acc("спорт").gender(MASCULINE).createNoun();
    public final static Noun PAGE = new NounBuilder().nom("страница").gen("страницы").instr("страницей").acc("страницу").counting("страниц").gender(MASCULINE).createNoun();
    public final static Noun CAKE = new NounBuilder().nom("торт").gen("торта").counting("тортов").plural("торты").gender(MASCULINE).createNoun();
    public final static Noun TULIP = new NounBuilder().nom("тюльпан").gen("тюльпана").counting("тюльпанов").plural("тюльпаны").gender(MASCULINE).createNoun();
    public final static Noun STUDENT = new NounBuilder().nom("ученик").gen("ученика").counting("учеников").plural("ученики").gender(MASCULINE).createNoun();
    public final static Noun PHYSICS = new NounBuilder().nom("физика").gen("физики").instr("физикой").acc("физику").gender(FEMININE).createNoun();
    public final static Noun FRUIT = new NounBuilder().nom("фрукт").gen("фрукта").counting("фруктов").plural("фрукты").gender(MASCULINE).createNoun();
    public final static Noun CHEMISTRY = new NounBuilder().nom("химия").gen("химии").instr("химией").acc("химию").gender(FEMININE).createNoun();
    public final static Noun FLOWER = new NounBuilder().nom("цветок").gen("цветка").counting("цветов").plural("цветы").gender(MASCULINE).createNoun();
    public final static Noun PEOPLE = new NounBuilder().nom("человек").gen("человека").counting("человек").plural("люди").gender(MASCULINE).createNoun();
    public final static Noun NUMBER = new NounBuilder().nom("число").gen("числа").counting("чисел").plural("числа").gender(NEUTER).createNoun();
    public final static Noun BALL = new NounBuilder().nom("шарик").gen("шарика").counting("шариков").plural("шарики").gender(MASCULINE).createNoun();
    public final static Noun CHOCOLATE_CANDY = new NounBuilder().nom("шоколадная конфета").acc("шоколадную конфету").gen("шоколадных конфеты").counting("шоколадных конфет").instr("шоколадной конфетой").plural("шоколадные конфеты").gender(FEMININE).createNoun();
    public final static Noun APPLE = new NounBuilder().nom("яблоко").gen("яблока").counting("яблок").plural("яблоки").gender(NEUTER).createNoun();

    public static final AbstractNoun[] FRUITS = {APPLE, PEAR, ORANGE, BANANA};
    public static final AbstractNoun[] CANDIES = {CHOCOLATE_CANDY, JELLY_CANDY, LOLLIPOP};
    public static final AbstractNoun[] SUBJECTS = {MUSIC, BIOLOGY, MATH, PHYSICS, CHEMISTRY, HISTORY};
    public static final AbstractNoun[] FLOWERS = {TULIP, ROSE, DAFFODIL, CAMOMILE, ASTER, PINK_FLOWER};

    public final static Adjective YELLOW = new Adjective("жёлтый", "жёлтая", "жёлтое", "жёлтых", "жёлтого", "жёлтой", "жёлтые");
    public final static Adjective GREEN = new Adjective("зелёный", "зелёная", "зелёное", "зелёных", "зелёного", "зелёной", "зелёные");
    public final static Adjective RED = new Adjective("красный", "красная", "красное", "красных", "красного", "красной", "красные");
    public final static Adjective BLUE = new Adjective("синий", "синяя", "синее", "синих", "синего", "синей", "синие");
    public final static Adjective BLACK = new Adjective("чёрный", "чёрная", "чёрное", "чёрных", "чёрного", "чёрной", "чёрные");



    private Dictionary() {

    }
}
