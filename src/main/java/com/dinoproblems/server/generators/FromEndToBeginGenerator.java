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

import static com.dinoproblems.server.utils.GeneratorUtils.randomInt;

/*
Created by Simar 17.02.19
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
        int way = difficulty == Difficulty.EASY ? 1 : 2;//1-лёгкая, 2-сложная
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
        final String[] choiceofactions;//действия с числом
        final String[] hobbies;//Интересы для 3-его действия
        final String[] beginning;//фон
        final int[] growing;//скорость увеличения цветков лилий на озере
        final String[] questions1;//вопросы первого типа для задачи 2
        int day;//день заполнения лилиями озера
        final String[] times;//склонения раз и раза
        int token1 = ThreadLocalRandom.current().nextInt(0, 8);//выбор первого героя
        int token2 = ThreadLocalRandom.current().nextInt(0, 8);//выбор второго героя
        //int token3 = ThreadLocalRandom.current().nextInt(0, 5);//выбор первого вопроса
        int token3 = difficulty == Difficulty.EASY ? randomInt(0, 3)
                : randomInt(3, 5);
        int answer = ThreadLocalRandom.current().nextInt(0, 100);//загаданное число
        final int secondoperand = ThreadLocalRandom.current().nextInt(10, 100);//операнд второго действия
        final int thirdoperand = ThreadLocalRandom.current().nextInt(10, 100);//операнд третьего действия для лёгкой сложности
        // final int answer=92;
        int actionnumber1 = 0;//число 1, полученное из загаданного после первого действия
        int actionnumber2 = 0;//число 2, полученное из числа 1 после второго действия
        int actionnumber3 = 0;//число 3-результат, полученный из числа после третьего действия
        hero1 = new String[]{"Крош", "Ёжик", "Бараш", "Лосяш", "Копатыч", "Пин", "Кар-Карыч", "Биби"};
        hero2 = new String[]{"Крошу", "Ёжику", "Барашу", "Лосяшу", "Копатычу", "Пину", "Кар-Карычу", "Биби"};
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
        String text;//итоговый текст задачи
        int i = 10;
        String hint;
        if (scenario.equals(DEFAULT_SCENARIO)) {

            for (; ; ) {
                if (token1 != token2) break;
                else token2 = ThreadLocalRandom.current().nextInt(0, 8);
            }
            BigInteger bigInteger = BigInteger.valueOf(answer);
            boolean probablePrime = bigInteger.isProbablePrime((int) Math.log(answer));
            if (probablePrime)//первое действие
            {
                if (token1 % 2 == 0) {
                    actionnumber1 = answer * 2;
                    actions[0] = 3;
                    operands[0] = "два";
                } else {
                    actionnumber1 = answer * 3;
                    actions[0] = 3;
                    operands[0] = "три";
                }
            } else if (answer % 2 == 0) {
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
            if (actionnumber1 < secondoperand) {
                actionnumber2 = actionnumber1 + secondoperand;
                actions[1] = 0;
                operands[1] = Integer.toString(secondoperand);
            }//второе действие
            else {
                actionnumber2 = actionnumber1 - secondoperand;
                actions[1] = 1;
                operands[1] = Integer.toString(secondoperand);
            }
            if (way == 2) {
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
            } else {
                if (actionnumber2 > thirdoperand) {
                    actionnumber3 = actionnumber2 - thirdoperand;
                    actions[2] = 1;
                    operands[2] = Integer.toString(thirdoperand);
                } else {
                    actionnumber3 = actionnumber2 + thirdoperand;
                    actions[2] = 0;
                    operands[2] = Integer.toString(thirdoperand);
                }
            }
          /* text=beginning[token1]+Hero1[token1]+" загадал некоторое натуральное число "+hero2[token2]+". Затем он "+choiceofactions[actions[1]]+operands[1]+", получил "+Integer.toString(actionnumber1)+
           "."+"\n"+"После "+choiceofactions[actions[2]]+operands[2]+" и получил "+Integer.toString(actionnumber2)+". Подумав, наш"+hobbies[token1]+choiceofactions[actions[0]]+operands[0]+
                   "."+"\n"+"В итоге результатом стало число "+Integer.toString(actionnumber3)+". Какое число загадал "+Hero1[token1]+"?";*/
            text = beginning[token1] + hero1[token1] + " загадал некоторое натуральное число " + hero2[token2] + ". Затем он " + choiceofactions[actions[0]] + operands[0] +
                    "." + " После " + choiceofactions[actions[1]] + operands[1] + ". Подумав, наш" + hobbies[token1] + choiceofactions[actions[2]] + operands[2] +
                    "." + " В итоге " + hero1[token1] + " сказал " + hero2[token2] + " результат, равный " + Integer.toString(actionnumber3) + ". Какое число было загадано?";

            hint = "Подумайте, какое число было до того, как " + hero1[token1] + " " + choiceofactions[actions[2]] + operands[2] + ". ";

        } else if (scenario.equals(LILY_SCENARIO)) {
            for (; ; ) {
                if (day % 2 == 0) break;
                else day = ThreadLocalRandom.current().nextInt(10, 100);
            }

            if (growing[token3] < 5) choice = times[1];
            else choice = times[0];
            answer = day - 1;
          /*  text="На озере расцвела одна лилия. Каждый день число её цветков становилось в "+Integer.toString(growing)+choice+" больше, а на "+day+
                    " день всё озеро покрылось цветами. "+"\n"+questions1[token3]+"\n"+Questions2[token4];*/
            text = "На озере расцвела одна лилия. Каждый день число её цветков становилось в " + Integer.toString(growing[token3]) + choice + " больше, а на " + day +
                    " день всё озеро покрылось цветами. " + questions1[token3];
            // System.out.println(answer);
            // System.out.print(text);
            hint = "Подумайте, что было накануне дня, когда все озеро было покрыто цветами. ";
        } else {
            throw new IllegalArgumentException();
        }
        String possibleAnswer = Integer.toString(answer);//?
        final HashSet<String> possibleTextAnswers = Sets.newHashSet(Integer.toString(answer));//?
        possibleTextAnswers.add(possibleAnswer);//?
        return new ProblemWithPossibleTextAnswers(text, answer, ProblemCollection.FROM_END_TO_BEGIN, possibleTextAnswers, hint, scenario, difficulty);//?
    }

    @Override
    public ProblemAvailability hasProblem(@Nonnull Collection<Problem> alreadySolvedProblems, @Nonnull Difficulty difficulty) {
        if (difficulty == Difficulty.DIFFICULT) {
            return null;
        }
        return GeneratorUtils.findAvailableScenario(difficulty, alreadySolvedProblems, SCENARIOS,
                difficulty == Difficulty.MEDIUM ? Sets.newHashSet(SCENARIOS) : new HashSet<>());
    }


}
