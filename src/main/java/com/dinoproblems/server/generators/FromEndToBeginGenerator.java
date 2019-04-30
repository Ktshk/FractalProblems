package com.dinoproblems.server.generators;

import com.dinoproblems.server.*;
import com.dinoproblems.server.Problem.Difficulty;
import com.dinoproblems.server.utils.GeneratorUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import javax.annotation.Nonnull;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

import static com.dinoproblems.server.utils.GeneratorUtils.findAvailableScenario;
import static com.dinoproblems.server.utils.GeneratorUtils.randomInt;

/*
Created by Simar 17.02.19
Modified by Simar 28.04.19
  */
public class FromEndToBeginGenerator implements ProblemGenerator {
    private static final ProblemScenario LILY_SCENARIO = new ProblemScenarioImpl(ProblemCollection.FROM_END_TO_BEGIN + "_LILY");
    private static final ProblemScenario DEFAULT_SCENARIO = new ProblemScenarioImpl(ProblemCollection.FROM_END_TO_BEGIN + "_DEFAULT");
    private static final ArrayList<ProblemScenario> SCENARIOS = Lists.newArrayList(DEFAULT_SCENARIO, LILY_SCENARIO);

    @Nonnull
    @Override
    public Problem generateProblem(Difficulty difficulty, ProblemAvailability problemAvailability) {

        ProblemScenario scenario = problemAvailability.getScenario();
        // int type=1;
        //int way = difficulty == Difficulty.EASY ? 1 : 2;//1-лёгкая, 2-сложная
        //int way=1;
        int steps = 3;//кол-во действий
        //флаги арифметических действий:
       /* int action1=1;//сложение
        int action2=2;//вычитание
        int action3=3;//деление
        int action4=4;//произведение*/
        int[] actions = new int[steps];//последовательность действий
        String[] operands = new String[steps];//операнды действий
        final String[] hero1;//Загадывающий число смешарик
        final String[] hero2;//Угадывающий число смешарик
        final String[] hero2tts;
        final String[] choiceofactions;//действия с числом
        final String[] hobbies;//Интересы для 3-его действия
        final String[] beginning;//фон
        final int[] growing;//скорость увеличения цветков лилий на озере
        final String[] questions1;//вопросы первого типа для задачи 2
        int day;//день заполнения лилиями озера
        final String[] times;//склонения раз и раза
        int token1 = ThreadLocalRandom.current().nextInt(0, 8);//выбор первого героя
        int token2 = ThreadLocalRandom.current().nextInt(0, 8);//выбор второго героя
        int token3 = 0;
        //int token3 = ThreadLocalRandom.current().nextInt(0, 5);//выбор первого вопроса
        //int token3 = difficulty == Difficulty.EASY ? randomInt(0, 3)
        //  : randomInt(3, 5);
        // int answer = ThreadLocalRandom.current().nextInt(0, 100);//загаданное число
        //  final int secondoperand = ThreadLocalRandom.current().nextInt(10, 100);//операнд второго действия
        // final int thirdoperand = ThreadLocalRandom.current().nextInt(10, 100);//операнд третьего действия для лёгкой сложности
        int answer = 0;
        int firstoperand = 0;
        int secondoperand = 0;
        int thirdoperand = 0;
        // final int answer=92;
        int actionnumber1 = 0;//число 1, полученное из загаданного после первого действия
        int actionnumber2 = 0;//число 2, полученное из числа 1 после второго действия
        int actionnumber3 = 0;//число 3-результат, полученный из числа после третьего действия
        hero1 = new String[]{"Крош", "Ёжик", "Бараш", "Лосяш", "Копатыч", "Пин", "Кар-Карыч", "Биби"};
        hero2 = new String[]{"Крошу", "Ёжику", "Барашу", "Лосяшу", "Копатычу", "Пину", "Кар-Карычу", "Биби"};
        hero2tts = new String[]{"Кр+ошу", "Ёжику", "Бар+ашу", "Лос+яшу", "Коп+атычу", "П+ину", "Кар-К+арычу", "Б+иби"};
        hobbies = new String[]{" вечно бегающий любитель морковки ",
                " спокойный коллекционер кактусов и фантиков ",
                " вечно печальный великий поэт ",
                " всезнающий ученый, кушая очередной будерброд, ",
                " неунывающий ценитель мёда ",
                " мастер на все руки и пилот ",
                " талантливый актёр, фокусник и певец ",
                " только-только познающий мир герой "};
        choiceofactions = new String[]{"прибавил к нему ", "отнял от него ", "поделил его на ", "умножил его на ", "его удвоил", "его утроил"};
        beginning = new String[]{"Ясным днём ", "Солнечным утром ",
                "Как-то раз, гуляя по лесу, ", "Во время прогулки по пляжу, "
                , "Считая звёзды ночью, ", "Сидя у потрескивающего костра, "
                , "Желая испытать друга, ", "Выполняя задание Совуньи, "};
        growing = new int[]{2, 3, 4, 5, 10};//скорость заполнения озера лилиями (
        //growing=2;
        day = ThreadLocalRandom.current().nextInt(10, 100);//день, когда озеро заполнено
        questions1 = new String[]{
                "На который день покрылась цветами половина озера?",
                "На который день покрылась цветами треть озера?",
                "На который день покрылась цветами четверть озера?",
                "На который день покралось цветами 20% озера?",
                "На который день покрылось цветами 10% озера?"};
        times = new String[]{" раз", " раза"};
        String choice;
        String text = null;//итоговый текст задачи
        String tts=null;
        int i = 10;
        String hint = null;
        for (; ; ) {
            if (token1 != token2) break;
            else token2 = ThreadLocalRandom.current().nextInt(0, 8);
        }
        switch (difficulty) {
            case EASY:
                answer = randomInt(0, 11);
                firstoperand = randomInt(1, 5);
                secondoperand = randomInt(6, 11);

                if (scenario.equals(DEFAULT_SCENARIO)) {
                    //первое действие
                    if (answer < firstoperand) {
                        actionnumber1 = answer + firstoperand;
                        actions[0] = 0;
                        operands[0] = Integer.toString(firstoperand);
                    } else {
                        actionnumber1 = answer - firstoperand;
                        actions[0] = 1;
                        operands[0] = Integer.toString(firstoperand);
                    }
                    if (actionnumber1 < secondoperand) {//второе действие
                        actionnumber2 = actionnumber1 + secondoperand;
                        actions[1] = 0;
                        operands[1] = Integer.toString(secondoperand);
                    } else {
                        actionnumber2 = actionnumber1 - secondoperand;
                        actions[1] = 1;
                        operands[1] = Integer.toString(secondoperand);
                    }

                }
                text = beginning[token1] + hero1[token1] + " загадал некоторое натуральное число " + hero2[token2] + ". Затем он " + choiceofactions[actions[0]] + operands[0] +
                        "." + " После " + choiceofactions[actions[1]] + operands[1] + " В итоге " + hero1[token1] + " сказал " + hero2[token2] + " результат, равный " +
                        Integer.toString(actionnumber2) + ". Какое число было загадано?";
                tts=beginning[token1] + hero1[token1] + " загадал некоторое натуральное число " + hero2tts[token2] + ". Затем он " + choiceofactions[actions[0]] + operands[0] +
                        "." + " После " + choiceofactions[actions[1]] + operands[1] + " В итоге " + hero1[token1] + " сказал " + hero2tts[token2] + " результат, равный " +
                        Integer.toString(actionnumber2) + ". Какое число было загадано?";
                hint = "Подумайте, какое число было до того, как " + hero1[token1] + " " + choiceofactions[actions[1]] + operands[1] + ". ";
                break;
            case MEDIUM:
                answer = randomInt(0, 21);
                secondoperand = randomInt(1, 11);
                thirdoperand = randomInt(11, 21);
                if (scenario.equals(DEFAULT_SCENARIO)) {
                    //первое действие
                    if (token1 % 2 == 0) {
                        actionnumber1 = answer * 2;
                        actions[0] = 3;
                        operands[0] = "два";
                    } else {
                        actionnumber1 = answer * 3;
                        actions[0] = 3;
                        operands[0] = "три";
                    }
                    if (actionnumber1 < secondoperand) {//второе действие
                        actionnumber2 = actionnumber1 + secondoperand;
                        actions[1] = 0;
                        operands[1] = Integer.toString(secondoperand);
                    } else {
                        actionnumber2 = actionnumber1 - secondoperand;
                        actions[1] = 1;
                        operands[1] = Integer.toString(secondoperand);
                    }

                    if (actionnumber2 > thirdoperand) {//третье действие
                        actionnumber3 = actionnumber2 - thirdoperand;
                        actions[2] = 1;
                        operands[2] = Integer.toString(thirdoperand);
                    } else {
                        actionnumber3 = actionnumber2 + thirdoperand;
                        actions[2] = 0;
                        operands[2] = Integer.toString(thirdoperand);
                    }
                    text = beginning[token1] + hero1[token1] + " загадал некоторое натуральное число " + hero2[token2] + ". Затем он " + choiceofactions[actions[0]] + operands[0] +
                            "." + " После " + choiceofactions[actions[1]] + operands[1] + ". Подумав, наш" + hobbies[token1] + choiceofactions[actions[2]] + operands[2] +
                            "." + " В итоге " + hero1[token1] + " сказал " + hero2[token2] + " результат, равный " + Integer.toString(actionnumber3) + ". Какое число было загадано?";
                    tts=beginning[token1] + hero1[token1] + " загадал некоторое натуральное число " + hero2tts[token2] + ". Затем он " + choiceofactions[actions[0]] + operands[0] +
                            "." + " После " + choiceofactions[actions[1]] + operands[1] + ". Подумав, наш" + hobbies[token1] + choiceofactions[actions[2]] + operands[2] +
                            "." + " В итоге " + hero1[token1] + " сказал " + hero2tts[token2] + " результат, равный " + Integer.toString(actionnumber3) + ". Какое число было загадано?";


                    hint = "Подумайте, какое число было до того, как " + hero1[token1] + " " + choiceofactions[actions[2]] + operands[2] + ". ";
                } else if (scenario.equals(LILY_SCENARIO)) {
                    token3 = 0;
                    for (; ; ) {
                        if (day % 2 == 0) break;
                        else day = ThreadLocalRandom.current().nextInt(10, 100);
                    }
                    if (growing[token3] < 5) choice = times[1];
                    else choice = times[0];
                    answer = day - 1;
                    text = "На озере расцвела одна лилия. Каждый день число её цветков становилось в " + Integer.toString(growing[token3]) + choice + " больше, а на " + day +
                            " день всё озеро покрылось цветами. " + questions1[token3];
                    hint = "Подумайте, что было накануне дня, когда все озеро было покрыто цветами. ";

                }
                break;
            case HARD:
                secondoperand = randomInt(1, 31);
                if (scenario.equals(DEFAULT_SCENARIO)) {
                    for (; ; ) {
                        answer = randomInt(0, 31);
                        BigInteger bigInteger = BigInteger.valueOf(answer);
                        boolean probablePrime = bigInteger.isProbablePrime((int) Math.log(answer));
                        if (!probablePrime) break;
                    }
                    if (answer % 2 == 0) {//первое действие
                        if (answer % 4 == 0) {
                            actionnumber1 = answer / 4;
                            actions[0] = 2;
                            operands[0] = "четыре";
                        } else if (answer % 6 == 0) {
                            actionnumber1 = answer / 6;
                            actions[0] = 2;
                            operands[0] = "шесть";
                        } else if (answer % 10 == 0) {
                            actionnumber1 = answer / 10;
                            actions[0] = 2;
                            operands[0] = "десять";
                        } else {
                            actionnumber1 = answer / 2;
                            actions[0] = 2;
                            operands[0] = "два";
                        }
                    } else {
                        if (answer % 3 == 0) {
                            actionnumber1 = answer / 3;
                            actions[0] = 2;
                            operands[0] = "три";
                        } else if (answer % 5 == 0) {
                            actionnumber1 = answer / 5;
                            actions[0] = 2;
                            operands[0] = "пять";
                        } else if (answer % 7 == 0) {
                            actionnumber1 = answer / 7;
                            actions[0] = 2;
                            operands[0] = "семь";
                        }
                    }
                    if (actionnumber1 < secondoperand) {//второе действие
                        actionnumber2 = actionnumber1 + secondoperand;
                        actions[1] = 0;
                        operands[1] = Integer.toString(secondoperand);
                    } else {
                        actionnumber2 = actionnumber1 - secondoperand;
                        actions[1] = 1;
                        operands[1] = Integer.toString(secondoperand);
                    }
                    if (actionnumber2 > 99)//третье действие
                    {
                        for (; ; ) {
                            if (actionnumber2 % i == 0) {
                                actionnumber3 = actionnumber2 / i;
                                actions[2] = 2;
                                operands[2] = Integer.toString(i);
                                break;
                            } else if (i == 0) break;
                            else i--;
                        }
                    } else if (actionnumber2 < 50) {
                        actionnumber3 = actionnumber2 * 3;
                        actions[2] = 5;
                        operands[2] = " ";
                    } else {
                        actionnumber3 = actionnumber2 * 2;
                        actions[2] = 4;
                        operands[2] = " ";
                    }
                    text = beginning[token1] + hero1[token1] + " загадал некоторое натуральное число " + hero2[token2] + ". Затем он " + choiceofactions[actions[0]] + operands[0] +
                            "." + " После " + choiceofactions[actions[1]] + operands[1] + ". Подумав, наш" + hobbies[token1] + choiceofactions[actions[2]] + operands[2] +
                            "." + " В итоге " + hero1[token1] + " сказал " + hero2[token2] + " результат, равный " + Integer.toString(actionnumber3) + ". Какое число было загадано?";
                    tts=beginning[token1] + hero1[token1] + " загадал некоторое натуральное число " + hero2tts[token2] + ". Затем он " + choiceofactions[actions[0]] + operands[0] +
                            "." + " После " + choiceofactions[actions[1]] + operands[1] + ". Подумав, наш" + hobbies[token1] + choiceofactions[actions[2]] + operands[2] +
                            "." + " В итоге " + hero1[token1] + " сказал " + hero2tts[token2] + " результат, равный " + Integer.toString(actionnumber3) + ". Какое число было загадано?";

                    hint = "Подумайте, какое число было до того, как " + hero1[token1] + " " + choiceofactions[actions[2]] + operands[2] + ". ";
                } else if (scenario.equals(LILY_SCENARIO)) {
                    for (; ; ) {
                        if (day % 2 == 0) break;
                        else day = ThreadLocalRandom.current().nextInt(10, 100);
                    }

                    if (growing[token3] < 5) choice = times[1];
                    else choice = times[0];
                    answer = day - 1;

                    text = "На озере расцвела одна лилия. Каждый день число её цветков становилось в " + Integer.toString(growing[token3]) + choice + " больше, а на " + day +
                            " день всё озеро покрылось цветами. " + questions1[token3];

                    hint = "Подумайте, что было накануне дня, когда все озеро было покрыто цветами. ";
                } else {
                    throw new IllegalArgumentException();
                }
        }

        String possibleAnswer = Integer.toString(answer);
        final HashSet<String> possibleTextAnswers = Sets.newHashSet(Integer.toString(answer));
        possibleTextAnswers.add(possibleAnswer);
        return new

                ProblemWithPossibleTextAnswers(text,tts, answer, ProblemCollection.FROM_END_TO_BEGIN, possibleTextAnswers, hint, scenario, difficulty);

    }

    @Override
    public ProblemAvailability hasProblem
            (@Nonnull Collection<Problem> alreadySolvedProblems, @Nonnull Difficulty difficulty) {
        switch (difficulty) {
            case EASY:
                return findAvailableScenario(difficulty, alreadySolvedProblems, Lists.newArrayList(DEFAULT_SCENARIO), new HashSet<>());
            case MEDIUM:
                return findAvailableScenario(difficulty, alreadySolvedProblems, Lists.newArrayList(DEFAULT_SCENARIO, LILY_SCENARIO), Sets.newHashSet(DEFAULT_SCENARIO));
            case HARD:
                return findAvailableScenario(difficulty, alreadySolvedProblems, Lists.newArrayList(DEFAULT_SCENARIO, LILY_SCENARIO), Sets.newHashSet(DEFAULT_SCENARIO, LILY_SCENARIO));
            case EXPERT:
                return null;
        }
        throw new IllegalArgumentException();

    }
}
