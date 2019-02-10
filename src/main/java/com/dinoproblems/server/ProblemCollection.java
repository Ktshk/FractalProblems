package com.dinoproblems.server;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Created by Katushka on 10.02.2019.
 */
public class ProblemCollection {
    public static final ProblemCollection INSTANCE = new ProblemCollection();

    public static final String GOLOVONOGI = "Golovonogi";

    private Map<String, ProblemGenerator> generators = new HashMap<>();
    private Table<Problem.Difficulty, String, ProblemGenerator> availableGeneratorsPerDifficulty = HashBasedTable.create();

    private ProblemCollection() {
        generators.put(GOLOVONOGI, new GolovonogiGenerator());

        for (Map.Entry<String, ProblemGenerator> problemGeneratorEntry : generators.entrySet()) {
            final ProblemGenerator problemGenerator = problemGeneratorEntry.getValue();
            final Set<Problem.Difficulty> availableDifficulties = problemGenerator.getAvailableDifficulties();
            for (Problem.Difficulty difficulty : availableDifficulties) {
                availableGeneratorsPerDifficulty.put(difficulty, problemGeneratorEntry.getKey(), problemGenerator);
            }
        }
    }

    public Problem generateProblem(Session session, Problem.Difficulty difficulty) {
        final Map<String, ProblemGenerator> generatorMap = availableGeneratorsPerDifficulty.row(difficulty);

        final Set<Problem> problemsSolved = session.getProblemsSolved();

        final Map<String, Double> themesSolved = new TreeMap<>();
        generatorMap.keySet().forEach(s -> themesSolved.put(s, 0.0));
        problemsSolved.forEach(problem -> themesSolved.put(problem.getTheme(),
                themesSolved.get(problem.getTheme()) + (problem.getState() == Problem.State.ANSWER_GIVEN ? 0.55 : 1)));

        final Optional<Map.Entry<String, Double>> min = themesSolved.entrySet().stream().min(Comparator.comparingDouble(Map.Entry::getValue));
        final Set<Map.Entry<String, Double>> minValues = themesSolved.entrySet().stream()
                .filter(stringDoubleEntry -> Objects.equals(stringDoubleEntry.getValue(), min.get().getValue()))
                .collect(Collectors.toSet());

        final int ind = ThreadLocalRandom.current().nextInt(0, minValues.size());
        Iterator<Map.Entry<String, Double>> iterator = minValues.iterator();
        Map.Entry<String, Double> entry = iterator.next();
        for (int i = 0; iterator.hasNext() && i < ind; i++) {
           entry = iterator.next();
        }
        return generators.get(entry.getKey()).generateProblem(difficulty);
    }

}
