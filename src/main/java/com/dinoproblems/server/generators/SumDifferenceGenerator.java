package com.dinoproblems.server.generators;

import com.dinoproblems.server.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.HashSet;

import com.dinoproblems.server.utils.*;

import javax.annotation.Nonnull;

import static com.dinoproblems.server.Problem.Difficulty.EXPERT;
import static com.dinoproblems.server.ProblemCollection.SUM_DIFFERENCE;
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
        int first = difficulty == EASY ? randomInt(10, 26) : randomInt(26, 51);
        //кол-во вкусняшек у второго
        int second = difficulty == EASY ? randomInt(10, 26) : randomInt(26, 51);
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
        String choiceSum;
        String choiceDifference;
        for (; ; ) {
            if (token1 != token2) break;
            else token2 = randomInt(0, 8);
        }
        for (; ; ) {
            if (first != second) {
                if (first > second) {
                    difference = first - second;
                    token3 = token1;
                } else {
                    difference = second - first;
                    token3 = token2;
                }
                sum = first + second;
                break;
            } else {
                second = difficulty == EASY ? randomInt(10, 26) : randomInt(26, 51);
            }
        }
        if (sum % 10 == 1)
            choiceSum = "У " + hero[token1] + " и " + hero[token2] + " вместе " + sum + " " + things[token1].getNominative() +
                    ".";
        else if (sum % 10 < 5 && sum % 10 != 0)
            choiceSum = "У " + hero[token1] + " и " + hero[token2] + " вместе " + sum + " " + things[token1].getGenitive() +
                    ".";
        else
            choiceSum = "У " + hero[token1] + " и " + hero[token2] + " вместе " + sum + " " + things[token1].getCountingForm() +
                    ".";

        if (difference % 10 == 1)
            choiceDifference = " У " + hero[token3] + " на " + difference + " " + things[token1].getNominative() + " больше.";
        else if (difference % 10 < 5)
            choiceDifference = " У " + hero[token3] + " на " + difference + " " + things[token1].getGenitive() + " больше.";
        else
            choiceDifference = " У " + hero[token3] + " на " + difference + " " + things[token1].getCountingForm() + " больше.";
        text = choiceSum + choiceDifference + " Сколько " +
                things[token1].getCountingForm() + " у " + hero[token3] + "?";
        hint = "Что если отнять у " + hero[token3] + " его превосходящее количество " + things[token1].getCountingForm() +
                ", теперь поровну " + things[token1].getCountingForm() + " между нашими героями.";
        final HashSet<String> possibleTextAnswers = Sets.newHashSet(Integer.toString(first));
        return new ProblemWithPossibleTextAnswers(text, first, SUM_DIFFERENCE, possibleTextAnswers, hint, DEFAULT_SCENARIO, difficulty);
    }

    @Override
    public ProblemAvailability hasProblem(@Nonnull Collection<Problem> alreadySolvedProblems, @Nonnull Problem.Difficulty difficulty) {
        if (difficulty == EXPERT) {
            return null;
        }

        return GeneratorUtils.findAvailableScenario(difficulty, alreadySolvedProblems,
                Lists.newArrayList(DEFAULT_SCENARIO), difficulty == EASY ? new HashSet<>() : Sets.newHashSet(DEFAULT_SCENARIO));
    }
}