package com.dinoproblems.server.generators;

import com.dinoproblems.server.Problem;
import com.dinoproblems.server.ProblemCollection;
import com.dinoproblems.server.ProblemGenerator;
import com.dinoproblems.server.ProblemWithPossibleTextAnswers;
import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.math.*;

/*
Created by Simar 17.02.19
  */
public class FromEndToBeginGenerator implements ProblemGenerator {
    @Override
    public Problem generateProblem(Problem.Difficulty difficulty) {

        // }
        // int type=difficulty==Problem.Difficulty.EASY ? 1 : 2;//тип задачи в зависимости от сложности:
        //public static void main(String[] args) {
        //1-смешарики, 2-лилии
        // int type = 2;//определяется сложностью?
        int type = difficulty == Problem.Difficulty.EASY ? 2 : 1;
        int steps = 3;//кол-во действий
        //флаги арифметических действий:
       /* int action1=1;//сложение
        int action2=2;//вычитание
        int action3=3;//деление
        int action4=4;//произведение*/
        int[] actions = new int[steps];//последовательность действий
        String[] operands = new String[steps];//операнды действий
        final String[] hero1;//Загадывающий число смешарик
        final String[] Hero2;//Угадывающий число смешарик
        final String[] Actions;//действия с числом
        final String[] Hobbies;//Интересы для 3-его действия
        final String[] Beginning;//фон
        final int[] Growing;//скорость увеличения цветков лилий на озере
        final String[] Questions1;//вопросы первого типа для задачи 2
        final String[] Questions2;//вопросы второго типа для задачи 2
        int Day;//день заполнения лилиями озера
        final String[] Times;//склонения раз и раза
        int token1 = ThreadLocalRandom.current().nextInt(0, 8);//выбор первого героя
        int token2 = ThreadLocalRandom.current().nextInt(0, 8);//выбор второго героя
        int token3 = ThreadLocalRandom.current().nextInt(0, 5);//выбор первого вопроса
        int token4 = ThreadLocalRandom.current().nextInt(0, 4);//выбор второго вопроса
        int answer = ThreadLocalRandom.current().nextInt(0, 100);//загаданное число
        final int secondoperand = ThreadLocalRandom.current().nextInt(10, 100);//операнд второго действия
        // final int answer=92;
        int actionnumber1 = 0;//число 1, полученное из загаданного после первого действия
        int actionnumber2 = 0;//число 2, полученное из числа 1 после второго действия
        int actionnumber3 = 0;//число 3-результат, полученный из числа после третьего действия
        hero1 = new String[]{"Крош", "Ёжик", "Бараш", "Лосяш", "Копатыч", "Пин", "Кар-Карыч", "Биби"};
        Hero2 = new String[]{"Крошу", "Ёжику", "Барашу", "Лосяшу", "Копатычу", "Пину", "Кар-Карычу", "Биби"};
        Hobbies = new String[]{" вечно бегающий любитель морковки ",
                " спокойный коллекционер кактусов и фантиков ",
                " вечно печальный великий поэт ",
                " всезнающий ученый, кушая очередной будерброд, ",
                " неунывающий ценитель мёда ",
                " мастер на все руки и пилот ",
                " талантливый актёр, фокусник и певец ",
                " только-только познающий мир герой "};
        Actions = new String[]{"прибавил к нему ", "отнял от него ", "поделил его на ", "умножил его на ", "его удвоил", "его утроил"};
        Beginning = new String[]{"Ясным днём ", "Солнечным утром ",
                "Как-то раз, гуляя по лесу, ", "Во время прогулки по пляжу, "
                , "Считая звёзды ночью, ", "Сидя у потрескивающего костра, "
                , "Желая испытать друга, ", "Выполняя задание Совуньи, "};
        Growing = new int[]{2, 3, 4, 5, 10};//скорость заполнения озера лилиями (
        //Growing=2;
        Day = ThreadLocalRandom.current().nextInt(10, 100);//день, когда озеро заполнено
        Questions1 = new String[]{
                "На который день покрылась цветами половина озера?",
                "На который день покрылась цветами треть озера?",
                "На который день покрылась цветами четверть озера?",
                "На который день покралось цветами 20% озера?",
                "На который день покрылось цветами 10% озера?"};
        Questions2 = new String[]{"А если в первый день была не одна, а две лилии?",
                "А если в первый день была не одна, а четыре лилии?",
                "А если в первый день была не одна, а восемь лилий?",
                "А если в первый день была не одна, а шестнадцать лилий?"};
        Times = new String[]{" раз", " раза"};
        String choice;
        String text = null;//итоговый текст задачи
        int i = 10;
        switch (type) {
            case 1:
                for (; ; ) {
                    if (token1 != token2) break;
                    else token2 = ThreadLocalRandom.current().nextInt(0, 8);
                }
                BigInteger bigInteger = BigInteger.valueOf(answer);
                boolean probablePrime = bigInteger.isProbablePrime((int) Math.log(answer));
                if (probablePrime == true)//первое действие
                {
                    if (token1 % 2 == 0) {
                        actionnumber1 = answer * 2;
                        actions[1] = 3;
                        operands[1] = "два";
                    } else {
                        actionnumber1 = answer * 3;
                        actions[1] = 3;
                        operands[1] = "три";
                    }
                } else if (answer % 2 == 0) {
                    if (answer % 4 == 0) {
                        actionnumber1 = answer / 4;
                        actions[1] = 2;
                        operands[1] = "четыре";
                    } else if (answer % 6 == 0) {
                        actionnumber1 = answer / 6;
                        actions[1] = 2;
                        operands[1] = "шесть";
                    } else if (answer % 10 == 0) {
                        actionnumber1 = answer / 10;
                        actions[1] = 2;
                        operands[1] = "десять";
                    } else {
                        actionnumber1 = answer / 2;
                        actions[1] = 2;
                        operands[1] = "два";
                    }
                } else {
                    if (answer % 3 == 0) {
                        actionnumber1 = answer / 3;
                        actions[1] = 2;
                        operands[1] = "три";
                    } else if (answer % 5 == 0) {
                        actionnumber1 = answer / 5;
                        actions[1] = 2;
                        operands[1] = "пять";
                    } else if (answer % 7 == 0) {
                        actionnumber1 = answer / 7;
                        actions[1] = 2;
                        operands[1] = "семь";
                    }
                }
                if (actionnumber1 < secondoperand) {
                    actionnumber2 = actionnumber1 + secondoperand;
                    actions[2] = 0;
                    operands[2] = Integer.toString(secondoperand);
                }//второе действие
                else {
                    actionnumber2 = actionnumber1 - secondoperand;
                    actions[2] = 1;
                    operands[2] = Integer.toString(secondoperand);
                }

                if (actionnumber2 > 99)//третье действие actions[0] operands[0]!
                {
                    for (; ; ) {
                        if (actionnumber2 % i == 0) {
                            actionnumber3 = actionnumber2 / i;
                            actions[0] = 2;
                            operands[0] = Integer.toString(i);
                            break;
                        } else if (i == 0) break;
                        else i--;
                    }
                } else if (actionnumber2 < 50) {
                    actionnumber3 = actionnumber2 * 3;
                    actions[0] = 5;
                    operands[0] = " ";
                } else {
                    actionnumber3 = actionnumber2 * 2;
                    actions[0] = 4;
                    operands[0] = " ";
                }
          /* text=Beginning[token1]+Hero1[token1]+" загадал некоторое натуральное число "+Hero2[token2]+". Затем он "+Actions[actions[1]]+operands[1]+", получил "+Integer.toString(actionnumber1)+
           "."+"\n"+"После "+Actions[actions[2]]+operands[2]+" и получил "+Integer.toString(actionnumber2)+". Подумав, наш"+Hobbies[token1]+Actions[actions[0]]+operands[0]+
                   "."+"\n"+"В итоге результатом стало число "+Integer.toString(actionnumber3)+". Какое число загадал "+Hero1[token1]+"?";*/
                text = Beginning[token1] + hero1[token1] + " загадал некоторое натуральное число " + Hero2[token2] + ". Затем он " + Actions[actions[1]] + operands[1] +
                        "." + "\n" + "После " + Actions[actions[2]] + operands[2] + ". Подумав, наш" + Hobbies[token1] + Actions[actions[0]] + operands[0] +
                        "." + "\n" + "В итоге " + hero1[token1] + " сказал " + Hero2[token2] + " результат, равный " + Integer.toString(actionnumber3) + ". Какое число было загадано?";
                // System.out.println(probablePrime);
               // System.out.println(answer);
              //  System.out.print(text);
            case 2:
                for (; ; ) {
                    if (Day % 2 == 0) break;
                    else Day = ThreadLocalRandom.current().nextInt(10, 100);
                }
                if (Growing[token3] < 5) choice = Times[1];
                else choice = Times[0];
                answer = Day - 1;
          /*  text="На озере расцвела одна лилия. Каждый день число её цветков становилось в "+Integer.toString(Growing)+choice+" больше, а на "+Day+
                    " день всё озеро покрылось цветами. "+"\n"+Questions1[token3]+"\n"+Questions2[token4];*/
                text = "На озере расцвела одна лилия. Каждый день число её цветков становилось в " + Integer.toString(Growing[token3]) + choice + " больше, а на " + Day +
                        " день всё озеро покрылось цветами. " + "\n" + Questions1[token3];
                // System.out.println(answer);
                // System.out.print(text);

        }
        String possibleAnswer = Integer.toString(answer);//?
        final HashSet<String> possibleTextAnswers = Sets.newHashSet(Integer.toString(answer));//?
        possibleTextAnswers.add(possibleAnswer);//?
        return new ProblemWithPossibleTextAnswers(text, answer, ProblemCollection.FROM_END_TO_BEGIN, possibleTextAnswers);//?
    }

    @Override
    public Set<Problem.Difficulty> getAvailableDifficulties() {
        return Sets.newHashSet(Problem.Difficulty.EASY, Problem.Difficulty.MEDIUM);
    }


}
