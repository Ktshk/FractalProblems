package com.dinoproblems.server;

import com.dinoproblems.server.Problem.Difficulty;

import java.util.Set;

/**
 * Created by Katushka on 10.02.2019.
 */
public interface ProblemGenerator {
    Problem generateProblem(Difficulty difficulty);

    Set<Difficulty> getAvailableDifficulties();


}
