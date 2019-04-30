package com.dinoproblems.server.generators;

import com.dinoproblems.server.*;
import com.dinoproblems.server.Problem.Difficulty;
import com.dinoproblems.server.utils.GeneratorUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import javax.annotation.Nonnull;
import java.util.*;

import static com.dinoproblems.server.utils.GeneratorUtils.Gender.MASCULINE;
import static com.dinoproblems.server.utils.GeneratorUtils.chooseRandomElement;
import static com.dinoproblems.server.utils.GeneratorUtils.randomInt;

/**
 * Created by Katushka on 12.02.2019.
 */
public class BrickAndHalfGenerator implements ProblemGenerator {

    private final static ProblemScenario WAY = new ProblemScenarioImpl(ProblemCollection.BRICK_AND_HALF + "_" + "WAY");
    private final static ProblemScenario BRICK_WEIGHT = new ProblemScenarioImpl(ProblemCollection.BRICK_AND_HALF + "_" + "BRICK_WEIGHT");
    private final static ProblemScenario BRICK_PRICE = new ProblemScenarioImpl(ProblemCollection.BRICK_AND_HALF + "_" + "BRICK_PRICE");

    private static final ProblemScenario[] SCENARIOS = {WAY, BRICK_WEIGHT, BRICK_PRICE};

    // TODO: add difficult problem:  Арбуз весит как дыня и четверть арбуза. Дыня весит как 6 яблок и еще треть дыни. Дыня вместе с яблокомвесят 2кг. Сколько весит арбуз?

    @Nonnull
    @Override
    public Problem generateProblem(Difficulty difficulty, ProblemAvailability problemAvailability) {
        int k = difficulty == Difficulty.EASY ? 2 : randomInt(3, 5);
        int n = (difficulty == Difficulty.EASY ? randomInt(1, 4) :
                randomInt(2, 6)) * (k - 1);
        final int answer = n * k / (k - 1);

        final ProblemScenario scenario = problemAvailability.getScenario();

        if (scenario.equals(WAY)) {
            final String text = "Миша прошёл " + n * 100 + " метров и ещё " + getPartString(k) + " пути. Какова длина пути?";
            return new ProblemWithPossibleTextAnswers(text, answer * 100, ProblemCollection.BRICK_AND_HALF,
                    Sets.newHashSet(answer * 100 + " м", answer * 100 + " метров"),
                    "Подумайте, какую часть пути составляют " + n * 100 + " метров.", scenario, difficulty);
        } else {
            String[] things1 = new String[]{"Кирпич", "Арбуз", "Мешок картошки"};
            String[] things = new String[]{"кирпич", "арбуз", "мешок картошки"};
            String[] thingsMod = new String[]{"кирпича", "арбуза", "мешка картошки"};

            int i = randomInt(0, 3);
            if (scenario.equals(BRICK_WEIGHT)) {
                final String text = things1[i] + " весит " + n + " кг и ещё " + getPartString(k) + " " + thingsMod[i] + ". Сколько весит " + things[i] + "?";
                final HashSet<String> possibleTextAnswers = Sets.newHashSet(answer + " кг", GeneratorUtils.getNumWithString(answer, "килограмм", "килограмма", "килограммов", MASCULINE));
                final String hint = "Подумайте, какую часть " + thingsMod[i] + (n % 10 == 1 && (n / 10 == 0 || n / 10 >= 2) ? " составляет " : " составляют ") + n + " кг.";
                return new ProblemWithPossibleTextAnswers(text, answer, ProblemCollection.BRICK_AND_HALF,
                        possibleTextAnswers, hint, scenario, difficulty);
            } else {
                final String text = things1[i] + " стоит " + n * 10 + " рублей и ещё " + getPartString(k) + " " + thingsMod[i] + ". Сколько стоит " + things[i] + "?";
                final String hint = "Подумайте, какую часть " + thingsMod[i] + " можно купить за  " + n * 10 + " рублей.";

                return new ProblemWithPossibleTextAnswers(text, answer * 10, ProblemCollection.BRICK_AND_HALF,
                        Sets.newHashSet(answer * 10 + " рублей"), hint, scenario, difficulty);

            }
        }
    }

    private String getPartString(int k) {
        if (k == 2) {
            return "половину";
        } else if (k == 3) {
            return "треть";
        } else if (k == 4) {
            return "четверть";
        } else {
            throw new UnsupportedOperationException("Not implemented");
        }
    }

    public ProblemAvailability hasProblem(@Nonnull Collection<Problem> alreadySolvedProblems, @Nonnull Difficulty difficulty) {
        if (difficulty == Difficulty.HARD || difficulty == Difficulty.EXPERT) {
            return null;
        }

        return GeneratorUtils.findAvailableScenario(difficulty, alreadySolvedProblems, Lists.newArrayList(SCENARIOS),
                difficulty == Difficulty.MEDIUM ? Sets.newHashSet(SCENARIOS) : new HashSet<>());
    }
}
