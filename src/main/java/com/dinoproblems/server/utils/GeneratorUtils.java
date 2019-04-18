package com.dinoproblems.server.utils;

import com.dinoproblems.server.Problem;
import com.dinoproblems.server.Problem.Difficulty;
import com.dinoproblems.server.ProblemGenerator.ProblemAvailability;
import com.dinoproblems.server.ProblemGenerator.ProblemAvailabilityType;
import com.dinoproblems.server.ProblemScenario;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

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

    public static ProblemAvailability findAvailableScenario(Difficulty difficulty, @Nonnull Collection<Problem> alreadySolvedProblems,
                                                            @Nonnull Collection<ProblemScenario> availableScenarios,
                                                            @Nonnull Collection<ProblemScenario> easierScenarios) {
        Map<ProblemScenario, ProblemAvailabilityType> scenarioToProblemAvailability = new HashMap<>();
        for (ProblemScenario problemScenario : availableScenarios) {
            scenarioToProblemAvailability.put(problemScenario, ProblemAvailabilityType.newScenarioProblem);
        }

        if (alreadySolvedProblems.isEmpty()) {
            return new ProblemAvailability(ProblemAvailabilityType.newProblem, chooseRandomElement(availableScenarios));
        }

        for (Problem problem : alreadySolvedProblems) {
            final ProblemScenario problemScenario = problem.getProblemScenario();
            if (!scenarioToProblemAvailability.containsKey(problemScenario)) {
                continue;
            }
            if (problem.getState() == Problem.State.SOLVED) {
                if (problem.getDifficulty().compareTo(difficulty) < 0) {
                    if (scenarioToProblemAvailability.get(problemScenario) != ProblemAvailabilityType.minorScenarioChanges) {
                        scenarioToProblemAvailability.put(problemScenario, ProblemAvailabilityType.trainProblem);
                    }
                } else {
                    if (problemScenario.isSingleProblem()) {
                        scenarioToProblemAvailability.remove(problemScenario);
                    } else {
                        scenarioToProblemAvailability.put(problemScenario, ProblemAvailabilityType.minorScenarioChanges);
                    }
                }
            } else if (problem.getState() == Problem.State.SOLVED_WITH_HINT || problem.getState() == Problem.State.ANSWER_GIVEN) {
                final ProblemAvailabilityType availabilityType = scenarioToProblemAvailability.get(problemScenario);
                if (availabilityType == ProblemAvailabilityType.newScenarioProblem) {
                    if (!easierScenarios.isEmpty()) {
                        scenarioToProblemAvailability.put(problemScenario, ProblemAvailabilityType.easierProblem);
                    }
                }
            }
        }

        if (scenarioToProblemAvailability.isEmpty()) {
            return null;
        }

        List<ProblemScenario> bestChoice = new ArrayList<>();
        List<ProblemScenario> otherChoices = new ArrayList<>();
        for (ProblemScenario problemScenario : scenarioToProblemAvailability.keySet()) {
            if (scenarioToProblemAvailability.get(problemScenario).equals(ProblemAvailabilityType.minorScenarioChanges)) {
                otherChoices.add(problemScenario);
            } else {
                bestChoice.add(problemScenario);
            }
        }

        ProblemScenario scenario;
        if (!bestChoice.isEmpty()) {
            scenario = chooseRandomElement(bestChoice);
        } else {
            scenario = chooseRandomElement(otherChoices);
        }

        final ProblemAvailabilityType type = scenarioToProblemAvailability.get(scenario);
        if (type == ProblemAvailabilityType.easierProblem) {
            if (!easierScenarios.contains(scenario)) {
                scenario = chooseRandomElement(easierScenarios);
            }
        }
        return new ProblemAvailability(type, scenario);
    }
}
