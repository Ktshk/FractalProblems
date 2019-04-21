package com.dinoproblems.server.generators;

import com.dinoproblems.server.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashSet;

import static com.dinoproblems.server.utils.Dictionary.BROTHER;
import static com.dinoproblems.server.utils.Dictionary.CHILD;
import static com.dinoproblems.server.utils.Dictionary.SISTER;
import static com.dinoproblems.server.utils.GeneratorUtils.findAvailableScenario;
import static com.dinoproblems.server.utils.GeneratorUtils.getNumWithString;
import static com.dinoproblems.server.utils.GeneratorUtils.randomInt;

/**
 * Created by Katushka on 19.04.2019.
 */
public class BrothersAndSistersGenerator implements ProblemGenerator {
    private static ProblemScenario COUNT_CHILDREN = new ProblemScenarioImpl(ProblemCollection.BROTHERS_AND_SISTERS + "_COUNT_CHILDREN");

    @Nonnull
    @Override
    public Problem generateProblem(Problem.Difficulty difficulty, ProblemAvailability problemAvailability) {
        if (problemAvailability.getScenario().equals(COUNT_CHILDREN)) {
            int sisters = randomInt(2, 7);
            int brothers = randomInt(2, 7);

            final String text = "В одной семье у каждого брата " + getNumWithString(sisters, SISTER) +
                    ", а у каждой сестры " + getNumWithString(brothers, BROTHER) +
                    ". Сколько детей в семье?";
            final String hint = "Не забывайте, что если у одной девочки " + getNumWithString(brothers, BROTHER) +
                    ", то и у ее сестры столько же братьев. И это одни и те же мальчики.";
            return new ProblemWithPossibleTextAnswers(text, sisters + brothers, ProblemCollection.FIND_NUMBER,
                    Sets.newHashSet(getNumWithString(sisters + brothers, CHILD)), hint, COUNT_CHILDREN, Problem.Difficulty.EASY);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Nullable
    @Override
    public ProblemAvailability hasProblem(@Nonnull Collection<Problem> alreadySolvedProblems, @Nonnull Problem.Difficulty difficulty) {
        switch (difficulty) {
            case EASY:
                return findAvailableScenario(difficulty, alreadySolvedProblems, Lists.newArrayList(COUNT_CHILDREN), new HashSet<>());
            case MEDIUM:
            case DIFFICULT:
            case EXPERT:
                return null;

        }

        throw new IllegalArgumentException();
    }
}
