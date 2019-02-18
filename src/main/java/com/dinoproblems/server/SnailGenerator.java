package com.dinoproblems.server;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Created by Katushka on 15.02.2019.
 */
public class SnailGenerator implements ProblemGenerator{
    @Override
    public Problem generateProblem(Problem.Difficulty difficulty) {
        return null;
    }

    @Override
    public Set<Problem.Difficulty> getAvailableDifficulties() {
        return Sets.newHashSet(Problem.Difficulty.MEDIUM, Problem.Difficulty.HARD);
    }
}
