package com.dinoproblems.server.generators;

import com.dinoproblems.server.Problem;
import com.dinoproblems.server.ProblemGenerator;
import com.dinoproblems.server.ProblemWithPossibleTextAnswers;
import com.dinoproblems.server.utils.AbstractNoun;
import com.dinoproblems.server.utils.NumberWord;
import com.dinoproblems.server.utils.ProblemTextBuilder;
import com.dinoproblems.server.utils.Verb;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.dinoproblems.server.ProblemCollection.EILER_CIRCLES;
import static com.dinoproblems.server.utils.Dictionary.*;
import static com.dinoproblems.server.utils.GeneratorUtils.*;

/**
 * Created by Katushka on 07.03.2019.
 */
public class EilerCirclesGenerator implements ProblemGenerator {

    public static final String[] VARIABLES = {"x", "y", "z", "total", "x + y", "y + z"};

    private static final Verb GO_TO_GROUP = new Verb("ходит в кружок", "ходят в кружок");
    private static final Verb LIKE = new Verb("увлекается", "увлекаются");
    private static final Verb BE_INTERESTED = new Verb("интересуется", "интересуются");
    private static final Verb BUSY_ONESELF = new Verb("занимается", "занимаются");
    private static final Verb GET = new Verb("получил", "получили");
    private static final Verb EAT = new Verb("съел", "съели");

    private static final String CLASS = "в классе";
    private static final String CAMP = "в лагере";
    private static final String PARTY = "день рождения";
    private static final String[] SCENARIOS = new String[]{CLASS/*, CAMP, PARTY*/};

    private static final Map<String, AbstractNoun[]> SCENARIO_OBJECTS = new HashMap<>();
    private static final Map<AbstractNoun[], Verb[]> VERBS = new HashMap<>();

    private static final Map<Verb, Function<AbstractNoun, String>> GOVERN_FORM = new HashMap<>();

    static {
        SCENARIO_OBJECTS.put(CLASS, SUBJECTS);
        SCENARIO_OBJECTS.put(CAMP, SUBJECTS);
        SCENARIO_OBJECTS.put(PARTY, CANDIES);

        VERBS.put(SUBJECTS, new Verb[]{BE_INTERESTED/*, GO_TO_GROUP*/, BUSY_ONESELF, LIKE});
        VERBS.put(CANDIES, new Verb[]{GET, EAT});

//        GOVERN_FORM.put(GO_TO_GROUP, AbstractNoun::getGenitive);
        GOVERN_FORM.put(LIKE, AbstractNoun::getInstrumentalForm);
        GOVERN_FORM.put(BUSY_ONESELF, AbstractNoun::getInstrumentalForm);
        GOVERN_FORM.put(BE_INTERESTED, AbstractNoun::getInstrumentalForm);
        GOVERN_FORM.put(EAT, AbstractNoun::getAccusativePluralForm);
        GOVERN_FORM.put(GET, AbstractNoun::getAccusativePluralForm);
    }


    @Override
    public Problem generateProblem(Problem.Difficulty difficulty) {
        int x = difficulty == Problem.Difficulty.EASY ? randomInt(1, 6) : randomInt(8, 16);
        int z = difficulty == Problem.Difficulty.EASY ? randomInt(1, 6) : randomInt(8, 16);
        int y = difficulty == Problem.Difficulty.EASY ? randomInt(Math.max(1, 8 - x - z), Math.min(8, 13 - x - z)) :
                randomInt(5, Math.min(16, 40 - x - z));

        int total = x + y + z;

        int scenario = randomInt(0, SCENARIOS.length);

        final AbstractNoun[] chosenSubjects = chooseRandom(SCENARIO_OBJECTS.get(SCENARIOS[scenario]), 2, AbstractNoun[]::new);

        final HashMap<String, Condition> conditions = Maps.newHashMap();
        conditions.put("x", new Condition(x, "не", chosenSubjects[1]));
        conditions.put("z", new Condition(z, "не", chosenSubjects[0]));
        conditions.put("y", new Condition(y, "", chosenSubjects[0], chosenSubjects[1]));
        conditions.put("x + y", new Condition(x + y, "", chosenSubjects[0]));
        conditions.put("y + z", new Condition(y + z, "", chosenSubjects[1]));
        conditions.put("total", new Condition(total, getScenarioVerb(scenario)));

        final HashSet<String> chosenVariables = chooseVariables();
        final Set<String> scenarioVariables = new HashSet<>(chosenVariables);

        ProblemTextBuilder text = new ProblemTextBuilder();

        if (chosenVariables.contains("total")) {
            if (SCENARIOS[scenario].equals(CLASS)) {
                text.append("В Петином классе ").append(Integer.toString(total)).append(" учеников и все занимаются ")
                        .append(chosenSubjects[0].getInstrumentalForm())
                        .append(" или ").append(chosenSubjects[1].getInstrumentalForm()).append(". ");
                ;
            } else {
                // TODO
            }
            chosenVariables.remove("total");
        } else {
            if (SCENARIOS[scenario].equals(CLASS)) {
                text.append("В Васином классе все занимаются ").append(chosenSubjects[0].getInstrumentalForm())
                        .append(" или ").append(chosenSubjects[1].getInstrumentalForm()).append(". ");
            } else {
                // todo
            }
        }

        int verbCount = 0;
        if (chosenVariables.contains("x + y")) {
            final Verb verb = selectVerb(SCENARIOS[scenario], verbCount++);
            if (chosenVariables.contains("y + z")) {
                text.append(conditions.get("x + y").getTextWithCount(verb));
                text.append(", а " + conditions.get("y + z").getTextWithCountWithoutVerb(GOVERN_FORM.get(verb)));
                chosenVariables.remove("y + z");
            } else if (chosenVariables.contains("x")) {
                text.append("Среди ").append(Integer.toString(x + y), NumberWord.getStringForNumber(x + y, Gender.MASCULINE, Case.GENITIVE))
                        .append(", которые ").append(verb.getPlural()).append(" ").append(GOVERN_FORM.get(verb).apply(chosenSubjects[0]))
                        .append(", ").append(conditions.get("x").getTextWithCount(selectVerb(SCENARIOS[scenario], verbCount++)));
                chosenVariables.remove("x");
            } else if (chosenVariables.contains("y")) {
                text.append("Среди ").append(Integer.toString(x + y), NumberWord.getStringForNumber(x + y, Gender.MASCULINE, Case.GENITIVE))
                        .append(", которые ").append(verb.getPlural()).append(" ").append(GOVERN_FORM.get(verb).apply(chosenSubjects[0]))
                        .append(", ").append(conditions.get("y + z").getTextWithCount(y, selectVerb(SCENARIOS[scenario], verbCount++)));
                chosenVariables.remove("y");
            } else {
                text.append(conditions.get("x + y").getTextWithCount(selectVerb(SCENARIOS[scenario], verbCount++)));
            }
            chosenVariables.remove("x + y");
            text.append(". ");
        }

        if (chosenVariables.contains("y + z")) {
            final Verb verb = selectVerb(SCENARIOS[scenario], verbCount++);
            if (chosenVariables.contains("z")) {
                text.append("Среди ").append(Integer.toString(y + z), NumberWord.getStringForNumber(y + z, Gender.MASCULINE, Case.GENITIVE))
                        .append(", которые ").append(verb.getPlural()).append(" ").append(GOVERN_FORM.get(verb).apply(chosenSubjects[1]))
                        .append(", ").append(conditions.get("z").getTextWithCount(selectVerb(SCENARIOS[scenario], verbCount++)));
                chosenVariables.remove("z");
            } else if (chosenVariables.contains("y")) {
                text.append("Среди ").append(Integer.toString(y + z), NumberWord.getStringForNumber(y + z, Gender.MASCULINE, Case.GENITIVE))
                        .append(", которые ").append(verb.getPlural()).append(" ").append(GOVERN_FORM.get(verb).apply(chosenSubjects[1]))
                        .append(", ").append(conditions.get("x").getTextWithCount(y, selectVerb(SCENARIOS[scenario], verbCount++)));
                chosenVariables.remove("x + y");
            } else {
                text.append(conditions.get("y + z").getTextWithCount(selectVerb(SCENARIOS[scenario], verbCount++)));
            }
            chosenVariables.remove("y + z");
            text.append(". ");
        }


        for (String chosenVariable : chosenVariables) {
            text.append(conditions.get(chosenVariable).getTextWithCount(selectVerb(SCENARIOS[scenario], verbCount++)));
            text.append(". ");
        }

        final String question = chooseQuestion(scenarioVariables);

        int answer = conditions.get(question).count;
        text.append("Сколько детей " + conditions.get(question).getQuestion(selectVerb(SCENARIOS[scenario], verbCount)) + "?");

        final HashSet<String> possibleTextAnswers = Sets.newHashSet(getNumWithString(answer, CHILD));
        for (Verb verb : VERBS.get(SCENARIO_OBJECTS.get(SCENARIOS[scenario]))) {
            possibleTextAnswers.add(conditions.get(question).getTextWithCount(verb));
        }

        final String hint = "Нарисуйте схему. В один круг поместите всех детей, которые любят " + chosenSubjects[0].getAccusativePluralForm() +
                ", а в в другой всех детей, которые любят " + chosenSubjects[1].getAccusativePluralForm();
        return new ProblemWithPossibleTextAnswers(text.getText(), text.getTTS(), answer, EILER_CIRCLES, possibleTextAnswers, hint);

    }

    private String chooseQuestion(Set<String> scenarioVariables) {
        return chooseRandomElement(
                Sets.newHashSet(VARIABLES).stream()
                        .filter(o -> {
                            if (scenarioVariables.contains(o)) {
                                return false;
                            }
                            final ArrayList<HashSet<String>> incorrectCombinations =
                                    Lists.newArrayList(Sets.newHashSet("x", "y + z", "total"),
                                            Sets.newHashSet("z", "x + y", "total")
                                            /*, Sets.newHashSet("x", "y", "x + y"),
                                            Sets.newHashSet("y", "z", "y + z")*/);
                            for (HashSet<String> incorrectCombination : incorrectCombinations) {
                                if (incorrectCombination.contains(o)) {
                                    incorrectCombination.remove(o);
                                    if (scenarioVariables.containsAll(incorrectCombination)) {
                                        return false;
                                    }
                                }
                            }
                            return true;
                        })
                        .collect(Collectors.toSet()));
    }

    private HashSet<String> chooseVariables() {
        final HashSet<String> allVariables = Sets.newHashSet(VARIABLES);
        final String[] firstAndSecond = chooseRandomString(VARIABLES, 2);
        allVariables.remove(firstAndSecond[0]);
        allVariables.remove(firstAndSecond[1]);
        final ArrayList<HashSet<String>> incorrectCombinations = Lists.newArrayList(Sets.newHashSet("x", "y + z", "total"),
                Sets.newHashSet("z", "x + y", "total"),
                Sets.newHashSet("x", "y", "z"),
                Sets.newHashSet("x", "y", "x + y"),
                Sets.newHashSet("y", "z", "y + z"));

        for (HashSet<String> incorrectCombination : incorrectCombinations) {
            if (incorrectCombination.contains(firstAndSecond[0]) && incorrectCombination.contains(firstAndSecond[1])) {
                allVariables.removeAll(incorrectCombination);
            }
        }

        final ArrayList<String> rest = Lists.newArrayList(allVariables);
        final String third = rest.get(randomInt(0, rest.size()));

        return Sets.newHashSet(firstAndSecond[0], firstAndSecond[1], third);
    }

    private Verb selectVerb(String scenario, int i) {
        final Verb[] verbs = VERBS.get(SCENARIO_OBJECTS.get(scenario));
        return verbs[i % verbs.length];
    }


    private static class Condition {
        int count;
        private final String text;
        private final AbstractNoun object1;
        private final AbstractNoun object2;

        Condition(int count, String text, AbstractNoun object) {
            this(count, text, object, null);
        }

        Condition(int count, String text, AbstractNoun object1, AbstractNoun object2) {
            this.count = count;
            this.text = text;
            this.object1 = object1;
            this.object2 = object2;
        }

        public Condition(int count, String text) {
            this(count, text, null, null);
        }

        String getTextWithCount(Verb verb) {
            if (object1 == null) {
                return getNumWithString(count, CHILD) + " " + (text.isEmpty() ? "" : (text + " "));
            } else {
                return getNumWithString(count, CHILD) + " "
                        + (text.isEmpty() ? "" : (text + " "))
                        + (count % 10 == 1 ? verb.getSingular() : verb.getPlural()) + " "
                        + (object2 != null ? "и " : "")
                        + GOVERN_FORM.get(verb).apply(object1)
                        + (object2 != null ? (", и " + GOVERN_FORM.get(verb).apply(object2)) : "");
            }
        }

        String getTextWithCount(int count, Verb verb) {
            if (object1 == null) {
                return getNumWithString(count, CHILD) + " " + (text.isEmpty() ? "" : (text + " "));
            } else {
                return getNumWithString(count, CHILD) + " "
                        + (text.isEmpty() ? "" : (text + " "))
                        + (count % 10 == 1 ? verb.getSingular() : verb.getPlural()) + " "
                        + (object2 != null ? "и " : "")
                        + GOVERN_FORM.get(verb).apply(object1)
                        + (object2 != null ? (", и " + GOVERN_FORM.get(verb).apply(object2)) : "");
            }
        }


        String getTextWithCountWithoutVerb(Function<AbstractNoun, String> governForm) {
            return getNumWithString(count, CHILD) + " " + text + " - "
                    + (object2 != null ? "и " : "")
                    + governForm.apply(object1)
                    + (object2 != null ? (" и " + governForm.apply(object2)) : "");
        }

        String getQuestion(Verb verb) {
            if (object1 == null) {
                return text;
            } else {
                return (text.isEmpty() ? "" : (text + " "))
                        + verb.getPlural() + " "
                        + (object2 != null ? "и " : "")
                        + GOVERN_FORM.get(verb).apply(object1)
                        + (object2 != null ? (", и " + GOVERN_FORM.get(verb).apply(object2)) : "");
            }
        }
    }

    private String getScenarioVerb(int scenario) {
        if (scenario == 0) {
            return "учится в классе";
        } else {
            // todo
            return null;
        }
    }

    private String chooseRandomElement(Collection<String> collection) {
        final ArrayList<String> list = Lists.newArrayList(collection);
        return list.get(randomInt(0, list.size()));
    }

    private String chooseRandomVerb(String[] verb, AbstractNoun noun, Map<String, Function<AbstractNoun, String>> governForm) {
        final int chosenVerb = randomInt(0, verb.length);
        return verb[chosenVerb] + " " + governForm.get(verb[chosenVerb]).apply(noun);
    }

    private String chooseRandomVerbWithTwoSubjects(String[] verb, AbstractNoun noun1, AbstractNoun noun2, Map<String, Function<AbstractNoun, String>> governForm) {
        final int chosenVerb = randomInt(0, verb.length);
        return verb[chosenVerb] + " и " + governForm.get(verb[chosenVerb]).apply(noun1) + ", и " + governForm.get(verb[chosenVerb]).apply(noun2);
    }

    @Override
    public Set<Problem.Difficulty> getAvailableDifficulties() {
        return Sets.newHashSet(Problem.Difficulty.EASY, Problem.Difficulty.MEDIUM);
    }
}
