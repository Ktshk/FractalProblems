package com.dinoproblems.server;

import com.dinoproblems.server.generators.*;
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

    public static final String LEGS_AND_HEADS = "Legs and heads";
    public static final String BRICK_AND_HALF = "Brick and a half";
    public static final String SNAIL = "Snail";
    public static final String SPACES = "Spaces";
    public static final String WITH_CLOSED_EYES = "With closed eyes";
    public static final String AT_LEAST_ONE_FOUND = "At least one found";
    public static final String FROM_END_TO_BEGIN ="From end to begin";

    private Map<String, ProblemGenerator> generators = new HashMap<>();
    private Table<Problem.Difficulty, String, ProblemGenerator> availableGeneratorsPerDifficulty = HashBasedTable.create();

    private ProblemCollection() {
        generators.put(LEGS_AND_HEADS, new LegsAndHeadsGenerator());
        generators.put(BRICK_AND_HALF, new BrickAndHalfGenerator());
        generators.put(SNAIL, new SnailGenerator());
        generators.put(SPACES, new SpacesGenerator());
        generators.put(WITH_CLOSED_EYES, new WithClosedEyesGenerator());
        generators.put(AT_LEAST_ONE_FOUND, new FoundAtLeastOneGenerator());
        generators.put(FROM_END_TO_BEGIN,new FromEndToBeginGenerator());

        for (Map.Entry<String, ProblemGenerator> problemGeneratorEntry : generators.entrySet()) {
            final ProblemGenerator problemGenerator = problemGeneratorEntry.getValue();
            final Set<Problem.Difficulty> availableDifficulties = problemGenerator.getAvailableDifficulties();
            for (Problem.Difficulty difficulty : availableDifficulties) {
                availableGeneratorsPerDifficulty.put(difficulty, problemGeneratorEntry.getKey(), problemGenerator);
            }
        }
    }

    public Problem generateProblem(Session session) {
        final Map<String, ProblemGenerator> generatorMap = availableGeneratorsPerDifficulty.row(session.getCurrentDifficulty());

        final Set<Problem> problemsSolved = session.getSolvedProblems();

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
        return generators.get(entry.getKey()).generateProblem(session.getCurrentDifficulty());
    }

}
