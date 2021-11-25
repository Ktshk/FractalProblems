package com.dinoproblems.server.generators;

import com.dinoproblems.server.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.HashSet;

import com.dinoproblems.server.utils.*;

import javax.annotation.Nonnull;

import static com.dinoproblems.server.Problem.Difficulty.MEDIUM;
import static com.dinoproblems.server.ProblemCollection.SUM_DIFFERENCE;
import static com.dinoproblems.server.utils.GeneratorUtils.findAvailableScenario;
import static com.dinoproblems.server.utils.GeneratorUtils.getNumWithString;
import static com.dinoproblems.server.utils.GeneratorUtils.randomInt;
import static com.dinoproblems.server.Problem.Difficulty.EASY;
/*
Created by Simar 10.03.19
  */

public class SumDifferenceGenerator implements ProblemGenerator {
    private final static ProblemScenario DEFAULT_SCENARIO = new ProblemScenarioImpl(ProblemCollection.SUM_DIFFERENCE);

    @Nonnull
    @Override
    public Problem generateProblem(Problem.Difficulty difficulty, ProblemAvailability problemAvailability) {
        final String[] hero;//владельцы вкусняшек
        //кол-во вкусняшек у первого
        int first = 0;
        //кол-во вкусняшек у второго
        int second = 0;
        switch (difficulty) {
            case EASY:
                first = randomInt(1, 8);
                second = randomInt(1, 8);
                break;
            case MEDIUM:
                first = randomInt(1, 33);
                second = randomInt(1, 33);
                break;
            case HARD:
                first = randomInt(1, 49);
                second = randomInt(1, 49);
                break;
        }
        int sum;
        int difference;
        hero = new String[]{"Кроша", "Ёжика", "Бараша", "Лосяша", "Копатыча", "Пина", "Кар-Карыча", "Биби"};
        int token1 = randomInt(0, 8);//выбор первого героя
        int token2 = randomInt(0, 8);//выбор второго героя
        int token3 = 0;//герой, у которого больше вкусняшек
        Noun[] things = {
                Dictionary.FRUIT,
                Dictionary.CANDY,
                Dictionary.CHOCOLATE_CANDY,
                Dictionary.LOLLIPOP,
                Dictionary.ORANGE,
                Dictionary.APPLE,
                Dictionary.BANANA,
                Dictionary.PEAR,
        };
        String text;//итоговый текст задачи
        String hint;//подсказка
        int answer;
        for (; ; ) {
            if (token1 != token2) break;
            else token2 = randomInt(0, 8);
        }
        for (; ; ) {
            if (first != second) {
                if (first > second) {
                    difference = first - second;
                    token3 = token1;
                    answer = first;
                } else {
                    difference = second - first;
                    token3 = token2;
                    answer = second;
                }
                sum = first + second;
                break;
            } else {
                second = difficulty == EASY ? randomInt(5, 16) :
                        (difficulty == MEDIUM ? randomInt(10, 26) :
                                randomInt(26, 51));
            }
        }
        final String choiceSum = "У " + hero[token1] + " и " + hero[token2] + " вместе " +
                getNumWithString(sum, things[token1]) + ".";
        final String choiceDifference = " У " + hero[token3] + " на " +
                getNumWithString(difference, things[token1], GeneratorUtils.Case.ACCUSATIVE) + " больше.";

        hint = "Что если отнять у " + hero[token3] + " " + getNumWithString(difference, things[token1], GeneratorUtils.Case.ACCUSATIVE) +
                ", теперь поровну " + things[token1].getCountingForm() + " между нашими героями.";
        text = choiceSum + choiceDifference + " Сколько " +
                things[token1].getCountingForm() + " у " + hero[token3] + "?";

        final HashSet<String> possibleTextAnswers = Sets.newHashSet(Integer.toString(answer));
        return new ProblemWithPossibleTextAnswers.Builder().text(text).answer(answer).theme(SUM_DIFFERENCE).possibleTextAnswers(possibleTextAnswers).hint(hint).scenario(DEFAULT_SCENARIO).difficulty(difficulty).create();
    }

    @Override
    public ProblemAvailability hasProblem(@Nonnull Collection<Problem> alreadySolvedProblems, @Nonnull Problem.Difficulty difficulty, UserInfo userInfo) {
        switch (difficulty) {
            case EASY:
                return findAvailableScenario(difficulty, alreadySolvedProblems, Lists.newArrayList(DEFAULT_SCENARIO), new HashSet<>(), userInfo);
            case MEDIUM:
                return findAvailableScenario(difficulty, alreadySolvedProblems, Lists.newArrayList(DEFAULT_SCENARIO), Sets.newHashSet(DEFAULT_SCENARIO), userInfo);
            case HARD:
                return findAvailableScenario(difficulty, alreadySolvedProblems, Lists.newArrayList(DEFAULT_SCENARIO), Sets.newHashSet(DEFAULT_SCENARIO), userInfo);
            case EXPERT:
                return null;
        }
        throw new IllegalArgumentException();
    }
}