package com.dinoproblems.server;

import com.dinoproblems.server.ProblemGenerator.ProblemAvailability;
import com.dinoproblems.server.ProblemGenerator.ProblemAvailabilityType;
import com.dinoproblems.server.generators.*;
import com.dinoproblems.server.utils.GeneratorUtils;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

import static com.dinoproblems.server.utils.GeneratorUtils.randomInt;

/**
 * Created by Katushka
 * on 10.02.2019.
 */
public class ProblemCollection {
    static final ProblemCollection INSTANCE = new ProblemCollection();

    public static final String LEGS_AND_HEADS = "Legs and heads";
    public static final String BRICK_AND_HALF = "Brick and a half";
    public static final String SNAIL = "Snail";
    public static final String SPACES = "Spaces";
    public static final String WITH_CLOSED_EYES = "With closed eyes";
    public static final String AT_LEAST_ONE_FOUND = "At least one found";
    public static final String FROM_END_TO_BEGIN = "From end to begin";
    public static final String SUM_DIFFERENCE = "Sum difference";
    public static final String EILER_CIRCLES = "Eiler Circles";
    public static final String SEQUENCE = "Sequence";
    public static final String RANGE = "Range";
    public static final String SHARE_WITH_FRIENDS = "Share with friends";
    public static final String FIND_NUMBER = "Find number";
    public static final String COMBINATORICS = "Combinatorics";
    public static final String BROTHERS_AND_SISTERS = "Brothers and sisters";

    private BiMap<String, ProblemGenerator> generators = HashBiMap.create();

    private ProblemCollection() {
        generators.put(SUM_DIFFERENCE, new SumDifferenceGenerator());
        generators.put(LEGS_AND_HEADS, new LegsAndHeadsGenerator());
        generators.put(BRICK_AND_HALF, new BrickAndHalfGenerator());
        generators.put(SNAIL, new SnailGenerator());
        generators.put(SPACES, new SpacesGenerator());
        generators.put(WITH_CLOSED_EYES, new WithClosedEyesGenerator());
        generators.put(AT_LEAST_ONE_FOUND, new FoundAtLeastOneGenerator());
        generators.put(FROM_END_TO_BEGIN, new FromEndToBeginGenerator());
        generators.put(EILER_CIRCLES, new EilerCirclesGenerator());
        generators.put(SEQUENCE, new SequenceGenerator());
        generators.put(RANGE, new RangeGenerator());
        generators.put(SHARE_WITH_FRIENDS, new ShareWithFriendsGenerator());
        generators.put(FIND_NUMBER, new FindNumberGenerator());
        generators.put(COMBINATORICS, new CombinatoricsGenerator());
        generators.put(BROTHERS_AND_SISTERS, new BrothersAndSistersGenerator());
    }

    @Nullable
    public Problem generateProblem(Session session) {
        final Problem.Difficulty currentDifficulty = session.getCurrentDifficulty();
        if (session.hasVariousProblems() &&
                ((currentDifficulty == Problem.Difficulty.EXPERT && randomInt(0, 2) == 0)
                        || ((currentDifficulty != Problem.Difficulty.EXPERT && randomInt(0, 3) == 0))) ) {
            final Problem randomVariousProblem = session.getRandomVariousProblem();
            if (randomVariousProblem != null)
                System.out.println("randomVariousProblem.getDifficulty() = " + randomVariousProblem.getDifficulty());
            return randomVariousProblem;
        }
        final Map<ProblemAvailabilityType, Map<ProblemGenerator, ProblemAvailability>> availabilityTypeToGenerator =
                new TreeMap<>(Comparator.comparingInt(problemAvailabilityType -> -problemAvailabilityType.getWeight()));
        for (Map.Entry<String, ProblemGenerator> problemGeneratorEntry : generators.entrySet()) {
            final ProblemGenerator problemGenerator = problemGeneratorEntry.getValue();
            final Collection<Problem> themedProblems = session.getSolvedProblems(problemGeneratorEntry.getKey());
            final ProblemAvailability problemAvailability = problemGenerator.hasProblem(themedProblems, currentDifficulty);
            if (problemAvailability != null) {
                if (!availabilityTypeToGenerator.containsKey(problemAvailability.getType())) {
                    availabilityTypeToGenerator.put(problemAvailability.getType(), new HashMap<>());
                }
                availabilityTypeToGenerator.get(problemAvailability.getType()).put(problemGenerator, problemAvailability);
            }
        }

        if (availabilityTypeToGenerator.isEmpty()) {
            return session.getRandomVariousProblem();
        }
        final Map<ProblemGenerator, ProblemAvailability> bestGenerators = availabilityTypeToGenerator.get(availabilityTypeToGenerator.keySet().iterator().next());

        final Map<ProblemGenerator, Set<Problem>> bestSolvedProblems = new HashMap<>();
        bestGenerators.keySet().forEach(gen -> {
            final String generatorKey = generators.inverse().get(gen);
            bestSolvedProblems.put(gen, new HashSet<>(session.getSolvedProblems(generatorKey)));
        });

        final Optional<Map.Entry<ProblemGenerator, Set<Problem>>> min = bestSolvedProblems.entrySet().stream()
                .min(Comparator.comparingInt(value -> value.getValue().size()));

        final List<Map.Entry<ProblemGenerator, Set<Problem>>> minValues = bestSolvedProblems.entrySet().stream()
                .filter(entry -> entry.getValue().size() == min.get().getValue().size())
                .collect(Collectors.toList());

        final Map.Entry<ProblemGenerator, Set<Problem>> entry = GeneratorUtils.chooseRandomElement(minValues);
        Problem.Difficulty difficulty = currentDifficulty;
        final ProblemAvailability problemAvailability = bestGenerators.get(entry.getKey());
        if (problemAvailability.getType() == ProblemAvailabilityType.easierProblem) {
            difficulty = difficulty.getPrevious();
        }
        System.out.println("problemAvailability = " + problemAvailability);
        System.out.println("difficulty = " + difficulty);
        final Problem problem = entry.getKey().generateProblem(difficulty, problemAvailability);
        System.out.println("theme = " + problem.getTheme());
        System.out.println("solved problems = " + session.getSolvedProblems(problem.getTheme()));
        return problem;
    }

    Collection<ProblemGenerator> getGenerators() {
        return generators.values();
    }
}
