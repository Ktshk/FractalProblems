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

import static com.dinoproblems.server.utils.GeneratorUtils.chooseRandomElement;
import static com.dinoproblems.server.utils.GeneratorUtils.randomInt;

/**
 * Created by Katushka
 * on 10.02.2019.
 */
public class ProblemCollection {
    public static final ProblemCollection INSTANCE = new ProblemCollection();

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
    public static final String LOGIC = "Logic";

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
        generators.put(LOGIC, new LogicGenerator());
    }

    @Nullable
    public Problem generateProblem(UserInfo userInfo, Problem.Difficulty difficulty, Calendar date) {
        System.out.println("Generate problem: date = " + date + ", diffuculty = " + difficulty);

        if (difficulty == Problem.Difficulty.EXPERT && date != null) {
            Problem todayProblem = QuestProblemsLoader.INSTANCE.getTodayProblem(date);
            System.out.println("Today quest problem: " + todayProblem);
            if (todayProblem != null) {
                return todayProblem;
            }
        }

        if (userInfo.hasVariousProblems(difficulty) &&
                ((difficulty == Problem.Difficulty.EXPERT && randomInt(0, 2) == 0)
                        || ((difficulty != Problem.Difficulty.EXPERT && randomInt(0, 3) == 0))) ) {
            final Problem randomVariousProblem = userInfo.getRandomVariousProblem(difficulty);
            if (randomVariousProblem != null)
                System.out.println("randomVariousProblem.getDifficulty() = " + randomVariousProblem.getDifficulty());
            return randomVariousProblem;
        }
        final Map<ProblemAvailabilityType, Map<ProblemGenerator, ProblemAvailability>> availabilityTypeToGenerator =
                new TreeMap<>(Comparator.comparingInt(problemAvailabilityType -> -problemAvailabilityType.getWeight()));
        for (Map.Entry<String, ProblemGenerator> problemGeneratorEntry : generators.entrySet()) {
            final ProblemGenerator problemGenerator = problemGeneratorEntry.getValue();
            final Collection<Problem> themedProblems = userInfo.getSolvedProblemsByTheme(problemGeneratorEntry.getKey());
            final ProblemAvailability problemAvailability = problemGenerator.hasProblem(themedProblems, difficulty, userInfo);
            if (problemAvailability != null) {
                if (!availabilityTypeToGenerator.containsKey(problemAvailability.getType())) {
                    availabilityTypeToGenerator.put(problemAvailability.getType(), new HashMap<>());
                }
                availabilityTypeToGenerator.get(problemAvailability.getType()).put(problemGenerator, problemAvailability);
            }
        }

        if (availabilityTypeToGenerator.isEmpty()) {
            return userInfo.getRandomVariousProblem(difficulty);
        }
        final Map<ProblemGenerator, ProblemAvailability> bestGenerators = availabilityTypeToGenerator.get(availabilityTypeToGenerator.keySet().iterator().next());

        final Map<ProblemGenerator, Set<Problem>> bestSolvedProblems = new HashMap<>();
        bestGenerators.keySet().forEach(gen -> {
            final String generatorKey = generators.inverse().get(gen);
            bestSolvedProblems.put(gen, new HashSet<>(userInfo.getSolvedProblemsByTheme(generatorKey)));
        });

        final Optional<Map.Entry<ProblemGenerator, Set<Problem>>> min = bestSolvedProblems.entrySet().stream()
                .min(Comparator.comparingInt(value -> value.getValue().size()));

        final List<Map.Entry<ProblemGenerator, Set<Problem>>> minValues = bestSolvedProblems.entrySet().stream()
                .filter(entry -> entry.getValue().size() == min.get().getValue().size())
                .collect(Collectors.toList());

        final Map.Entry<ProblemGenerator, Set<Problem>> entry = GeneratorUtils.chooseRandomElement(minValues);
        final ProblemAvailability problemAvailability = bestGenerators.get(entry.getKey());
        return entry.getKey().generateProblem(difficulty, problemAvailability);
    }

    Collection<ProblemGenerator> getGenerators() {
        return generators.values();
    }
}
