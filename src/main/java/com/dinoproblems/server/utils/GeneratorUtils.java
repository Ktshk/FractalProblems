package com.dinoproblems.server.utils;

import com.dinoproblems.server.Problem;
import com.dinoproblems.server.Problem.Difficulty;
import com.dinoproblems.server.ProblemGenerator.ProblemAvailability;
import com.dinoproblems.server.ProblemGenerator.ProblemAvailabilityType;
import com.dinoproblems.server.ProblemScenario;
import com.google.common.collect.Lists;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.dinoproblems.server.Problem.Difficulty.EASY;
import static com.dinoproblems.server.utils.GeneratorUtils.Gender.MASCULINE;
import static com.dinoproblems.server.utils.NumberWord.getStringForNumber;

/**
 * Created by Katushka on 10.02.2019.
 */
public class GeneratorUtils {

    public enum Gender {
        MASCULINE,
        FEMININE,
        NEUTER
    }

    public enum Case {
        NOMINATIVE,
        GENITIVE,
        ACCUSATIVE

        // TODO: add other cases
    }

    private GeneratorUtils() {

    }

    public static String getMetersString(int meters) {
        return getNumWithString(meters, "метр", "метра", "метров", MASCULINE);
    }

    public static String getNumWithString(int count, final String one, final String lessThanFive, final String moreThanFive, Gender gender) {
        return getNumWithString(count, one, lessThanFive, moreThanFive, gender, Case.NOMINATIVE);
    }

    public static String getNumWithString(int count, AbstractNoun noun) {
        return getNumWithString(count, noun.getNominative(), noun.getCountingGenitive(), noun.getCountingForm(), noun.getGender(), Case.NOMINATIVE);
    }

    public static String getNumWithString(int count, AbstractNoun noun, Case wordCase) {
        switch (wordCase) {
            case NOMINATIVE:
                return getNumWithString(count, noun.getNominative(), noun.getCountingGenitive(), noun.getCountingForm(), noun.getGender(), wordCase);
            case ACCUSATIVE:
                return getNumWithString(count, noun.getAccusativeForm(), noun.getCountingGenitive(), noun.getCountingForm(), noun.getGender(), wordCase);
            case GENITIVE:
                return getNumWithString(count, noun.getGenitive(), noun.getCountingGenitive(), noun.getCountingForm(), noun.getGender(), wordCase);
        }

        throw new IllegalArgumentException();
    }

    public static String getNumWithString(int count, final String[] wordForms, Gender gender) {
        return getNumWithString(count, wordForms[0], wordForms[1], wordForms[2], gender, Case.NOMINATIVE);
    }

    public static String getNumWithString(int count, final String one, final String lessThanFive, final String moreThanFive, Gender gender, Case wordCase) {
        if (count >= 5 && count <= 20) {
            return count + " " + moreThanFive;
        } else {
            final int lastDigit = count % 10;
            if (lastDigit == 1) {
                if (gender == Gender.MASCULINE) {
                    return count + " " + one;
                } else {
                    return getStringForNumber(count, gender, wordCase) + " " + one;
                }
            } else if (lastDigit >= 2 && lastDigit <= 4) {
                if (lastDigit == 2 && gender != Gender.MASCULINE) {
                    return getStringForNumber(count, gender, wordCase) + " " + lessThanFive;
                }
                return count + " " + lessThanFive;
            } else {
                return count + " " + moreThanFive;
            }
        }
    }

    public static int randomInt(int origin, int bound) {
        return ThreadLocalRandom.current().nextInt(origin, bound);
    }

    public static String[] chooseRandomString(String[] array, int count) {
        return chooseRandom(array, count, String[]::new);
    }

    public static <T> T[] chooseRandom(T[] array, int count, Function<Integer, T[]> arrayConstructor) {
        T[] arrayCopy = Arrays.copyOf(array, array.length);

        final T[] result = arrayConstructor.apply(count);
        for (int i = 0; i < count; i++) {
            int ind = randomInt(i, arrayCopy.length);
            result[i] = arrayCopy[ind];

            T t = arrayCopy[ind];
            arrayCopy[ind] = arrayCopy[i];
            arrayCopy[i] = t;
        }
        return result;
    }

    public static <T> T chooseRandomElement(T[] array) {
        return array[randomInt(0, array.length)];
    }

    public static <T> T chooseRandomElement(List<T> list) {
        return list.get(randomInt(0, list.size()));
    }

    public static <T> T chooseRandomElement(Collection<T> collection) {
        if (collection instanceof List) {
            return chooseRandomElement((List<T>) collection);
        }
        List<T> list = new ArrayList<>(collection);
        return chooseRandomElement(list);
    }

    @Nullable
    public static ProblemAvailability findAvailableScenario(Difficulty difficulty, @Nonnull Collection<Problem> alreadySolvedProblems,
                                                            @Nonnull Collection<ProblemScenario> availableScenarios,
                                                            @Nonnull Collection<ProblemScenario> easierScenarios) {
        if (alreadySolvedProblems.isEmpty()) {
            return new ProblemAvailability(ProblemAvailabilityType.newProblem, chooseRandomElement(availableScenarios));
        }
        final Map<ProblemScenario, ProblemAvailabilityType> scenarioToProblemAvailability = new HashMap<>();
        final HashSet<ProblemScenario> easierScenariosSet = easierScenarios
                .stream()
                .filter(problemScenario -> !availableScenarios.contains(problemScenario) || !problemScenario.isSingleProblem())
                .collect(Collectors.toCollection(HashSet::new));
        final Map<ProblemScenario, Map<Boolean, Set<Difficulty>>> problemsByScenario = new HashMap<>();
        for (Problem problem : alreadySolvedProblems) {
            final ProblemScenario problemScenario = problem.getProblemScenario();
            if (problem.getDifficulty() != difficulty && (difficulty == EASY || problem.getDifficulty() != difficulty.getPrevious())) {
                continue;
            }
            if (availableScenarios.contains(problemScenario)) {
                if (!problemsByScenario.containsKey(problemScenario)) {
                    problemsByScenario.put(problemScenario, new HashMap<>());
                }
                final boolean solved = problem.getState() == Problem.State.SOLVED;
                if (!problemsByScenario.get(problemScenario).containsKey(solved)) {
                    problemsByScenario.get(problemScenario).put(solved, new HashSet<>());
                }
                problemsByScenario.get(problemScenario).get(solved).add(problem.getDifficulty());
            }
            if (difficulty != EASY && problem.getDifficulty() == difficulty.getPrevious()) {
                easierScenariosSet.remove(problem.getProblemScenario());
            }
        }

        for (ProblemScenario scenario : availableScenarios) {
            if (!problemsByScenario.containsKey(scenario)) {
                scenarioToProblemAvailability.put(scenario, ProblemAvailabilityType.newScenarioProblem);
            } else {
                final Map<Boolean, Set<Difficulty>> stateDifficultyMap = problemsByScenario.get(scenario);
                if (stateDifficultyMap.containsKey(true)) {
                    if (stateDifficultyMap.get(true).contains(difficulty)) {
                        if (!scenario.isSingleProblem()) {
                            scenarioToProblemAvailability.put(scenario, ProblemAvailabilityType.minorScenarioChanges);
                        }
                    } else {
                        if (!scenario.isSingleProblem()) {
                            scenarioToProblemAvailability.put(scenario, ProblemAvailabilityType.trainProblem);
                        }
                    }
                } else {
                    if (difficulty != EASY && stateDifficultyMap.get(false).contains(difficulty.getPrevious())) {
                        if (!easierScenariosSet.isEmpty()) {
                            scenarioToProblemAvailability.put(chooseRandomElement(easierScenariosSet), ProblemAvailabilityType.easierProblem);
                        } else {
                            if (!scenario.isSingleProblem()) {
                                scenarioToProblemAvailability.put(scenario, ProblemAvailabilityType.trainProblem);
                            }
                        }
                    } else {
                        if (!easierScenariosSet.isEmpty()) {
                            if (easierScenarios.contains(scenario)) {
                                scenarioToProblemAvailability.put(scenario, ProblemAvailabilityType.easierProblem);
                            } else {
                                scenarioToProblemAvailability.put(chooseRandomElement(easierScenariosSet), ProblemAvailabilityType.easierProblem);
                            }
                        } else {
                            if (!scenario.isSingleProblem()) {
                                scenarioToProblemAvailability.put(scenario, ProblemAvailabilityType.trainProblem);
                            }
                        }
                    }
                }
            }
        }

        if (scenarioToProblemAvailability.isEmpty()) {
            return null;
        }

        final ArrayList<ProblemScenario> sortedList = scenarioToProblemAvailability.keySet()
                .stream()
                .filter(problemScenario -> availableScenarios.contains(problemScenario) ||
                        (scenarioToProblemAvailability.get(problemScenario) == ProblemAvailabilityType.easierProblem && easierScenarios.contains(problemScenario)))
                .collect(Collectors.toCollection(ArrayList::new));
        if (sortedList.isEmpty()) {
            return null;
        }
        Collections.shuffle(sortedList);
        final Optional<ProblemScenario> max = sortedList.stream().max(Comparator.comparingInt(o -> scenarioToProblemAvailability.get(o).getWeight()));
        return max.map(problemScenario -> new ProblemAvailability(scenarioToProblemAvailability.get(problemScenario), problemScenario)).orElse(null);

    }
}
