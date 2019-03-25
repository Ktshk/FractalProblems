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

    private static final Verb LIKE = new Verb("увлекается", "увлекаются");
    private static final Verb BE_INTERESTED = new Verb("интересуется", "интересуются");
    private static final Verb BUSY_ONESELF = new Verb("занимается", "занимаются");
    private static final Verb GET = new Verb("получил", "получили");
    private static final Verb EAT = new Verb("съел", "съели");
    private static final Verb TREAT = new Verb("угостился", "угостились");

    private static final String CLASS = "в классе";
    private static final String CAMP = "в лагере";
    private static final String PARTY = "день рождения";
    private static final String[] SCENARIOS = new String[]{CLASS, CAMP, PARTY};

    private static final Map<String, AbstractNoun[]> SCENARIO_OBJECTS = new HashMap<>();
    private static final Map<AbstractNoun[], Verb[]> VERBS = new HashMap<>();

    private static final Map<Verb, Function<AbstractNoun, String>> GOVERN_FORM = new HashMap<>();

    static {
        SCENARIO_OBJECTS.put(CLASS, SUBJECTS);
        SCENARIO_OBJECTS.put(CAMP, SUBJECTS);
        SCENARIO_OBJECTS.put(PARTY, CANDIES);

        VERBS.put(SUBJECTS, new Verb[]{BE_INTERESTED, BUSY_ONESELF, LIKE});
        VERBS.put(CANDIES, new Verb[]{GET, EAT, TREAT});

        GOVERN_FORM.put(LIKE, AbstractNoun::getInstrumentalForm);
        GOVERN_FORM.put(BUSY_ONESELF, AbstractNoun::getInstrumentalForm);
        GOVERN_FORM.put(BE_INTERESTED, AbstractNoun::getInstrumentalForm);
        GOVERN_FORM.put(EAT, AbstractNoun::getAccusativePluralForm);
        GOVERN_FORM.put(GET, AbstractNoun::getAccusativePluralForm);
        GOVERN_FORM.put(TREAT, AbstractNoun::getInstrumentalForm);
    }


    @Override
    public Problem generateProblem(Problem.Difficulty difficulty) {
        int x = difficulty == Problem.Difficulty.EASY ? randomInt(1, 6) : randomInt(8, 16);
        int z = difficulty == Problem.Difficulty.EASY ? randomInt(1, 6) : randomInt(8, 16);
        int y = difficulty == Problem.Difficulty.EASY ? randomInt(Math.max(1, 8 - x - z), Math.min(8, 13 - x - z)) :
                randomInt(5, Math.min(16, 40 - x - z));

        int total = x + y + z;

        String[][] heroes = new String[][]{{"Петя", "Петином", "Пете"},
                {"Вася", "Васином", "Васе"},
                {"Миша", "Мишином", "Мише"},
                {"Саша", "Сашином", "Саше"}};
        boolean excludeHero = randomInt(0, 2) == 0;
        if (excludeHero) {
            total = total + 1;
        }
        String[] hero = heroes[randomInt(0, heroes.length)];


        int scenario = randomInt(0, SCENARIOS.length);

        final AbstractNoun[] chosenSubjects = chooseRandom(SCENARIO_OBJECTS.get(SCENARIOS[scenario]), 2, AbstractNoun[]::new);

        final HashMap<String, Condition> conditions = Maps.newHashMap();
        conditions.put("x", new Condition(x, "не", chosenSubjects[1], chosenSubjects[0], false));
        conditions.put("z", new Condition(z, "не", chosenSubjects[0], chosenSubjects[1], false));
        conditions.put("y", new Condition(y, "", chosenSubjects[0], chosenSubjects[1], true));
        conditions.put("x + y", new Condition(x + y, "", chosenSubjects[0]));
        conditions.put("y + z", new Condition(y + z, "", chosenSubjects[1]));
        conditions.put("total", new Condition(total, getScenarioVerb(SCENARIOS[scenario], excludeHero)));

        final HashSet<String> chosenVariables = chooseVariables(difficulty == Problem.Difficulty.EASY ? new String[]{"y", "x + y", "y + z", "total"} : VARIABLES);
        final Set<String> scenarioVariables = new HashSet<>(chosenVariables);

        ProblemTextBuilder text = new ProblemTextBuilder();

        boolean startSentence = false;
        if (chosenVariables.contains("total")) {
            if (SCENARIOS[scenario].equals(CLASS)) {
                text.append("В ").append(hero[1]).append(" классе ").append(getNumWithString(total, STUDENT));
                if (!excludeHero) {
                    text.append(" ");
                }
                startSentence = true;
            } else if (SCENARIOS[scenario].equals(CAMP)) {
                text.append("В лагерь приехали ").append(getNumWithString(total, CHILD));
                if (!excludeHero) {
                    text.append(" и ");
                }
                startSentence = true;
            } else if (SCENARIOS[scenario].equals(PARTY)) {
                text.append("На день рождения к ").append(hero[2]).append(" пришли ").append(getNumWithString(total - 1, CHILD));
                if (excludeHero) {
                    text.append(", и ").append(hero[0]).append(" угостил всех конфетами");
                } else {
                    text.append(" и на праздничном столе все дети ели конфеты");
                }
                startSentence = true;
            }
            chosenVariables.remove("total");
        } else {
            if (SCENARIOS[scenario].equals(CLASS)) {
                text.append("В ").append(hero[1]).append(" классе ");
                if (!excludeHero) {
                    text.append("все ");
                }
                startSentence = !excludeHero;
            } else if (SCENARIOS[scenario].equals(CAMP)) {
                text.append("В лагере ");
                startSentence = !excludeHero;
            } else if (SCENARIOS[scenario].equals(PARTY)) {
                if (!excludeHero) {
                    text.append("На ").append(hero[1]).append(" дне рождения ").append(" все дети ели конфеты");
                } else {
                    text.append("На своём дне рождения ").append(hero[0]).append(" угостил всех гостей конфетами");
                }
                startSentence = true;
            }
        }
        if (!excludeHero) {
            if (SCENARIOS[scenario].equals(CLASS)) {
                text.append("занимаются ")
                        .append(chosenSubjects[0].getInstrumentalForm())
                        .append(" или ").append(chosenSubjects[1].getInstrumentalForm());
            } else if (SCENARIOS[scenario].equals(CAMP)) {
                text.append("оказалось, что все дети занимаются ").append(chosenSubjects[0].getInstrumentalForm())
                        .append(" или ").append(chosenSubjects[1].getInstrumentalForm());
            }
        }
        if (startSentence) {
            text.append(". ");
        }

        int verbCount = 0;
        if (chosenVariables.contains("x + y")) {
            final Verb verb = selectVerb(SCENARIOS[scenario], verbCount++);
            if (chosenVariables.contains("y + z")) {
                text.append(conditions.get("x + y").getTextWithCount(verb));
                text.append(", а " + conditions.get("y + z").getTextWithCountWithoutVerb(GOVERN_FORM.get(verb)));
                chosenVariables.remove("y + z");
            } else if (chosenVariables.contains("x")) {
                text.append(startSentence ? "Среди " : "среди ").append(Integer.toString(x + y), NumberWord.getStringForNumber(x + y, Gender.MASCULINE, Case.GENITIVE))
                        .append(" детей, которые ").append(verb.getPlural()).append(" ").append(GOVERN_FORM.get(verb).apply(chosenSubjects[0]))
                        .append(", ").append(conditions.get("x").getTextWithCount(selectVerb(SCENARIOS[scenario], verbCount++)));
                chosenVariables.remove("x");
            } else if (chosenVariables.contains("y")) {
                text.append(startSentence ? "Среди " : "среди ").append(Integer.toString(x + y), NumberWord.getStringForNumber(x + y, Gender.MASCULINE, Case.GENITIVE))
                        .append(" детей, которые ").append(verb.getPlural()).append(" ").append(GOVERN_FORM.get(verb).apply(chosenSubjects[0]))
                        .append(", ").append(conditions.get("y + z").getTextWithCount(y, selectVerb(SCENARIOS[scenario], verbCount++)));
                chosenVariables.remove("y");
            } else {
                text.append(conditions.get("x + y").getTextWithCount(selectVerb(SCENARIOS[scenario], verbCount++)));
            }
            chosenVariables.remove("x + y");
            text.append(". ");
            startSentence = true;
        }

        if (chosenVariables.contains("y + z")) {
            final Verb verb = selectVerb(SCENARIOS[scenario], verbCount++);
            if (chosenVariables.contains("z")) {
                text.append(startSentence ? "Среди " : "среди ").append(Integer.toString(y + z), NumberWord.getStringForNumber(y + z, Gender.MASCULINE, Case.GENITIVE))
                        .append(" детей, которые ").append(verb.getPlural()).append(" ").append(GOVERN_FORM.get(verb).apply(chosenSubjects[1]))
                        .append(", ").append(conditions.get("z").getTextWithCount(selectVerb(SCENARIOS[scenario], verbCount++)));
                chosenVariables.remove("z");
            } else if (chosenVariables.contains("y")) {
                text.append(startSentence ? "Среди " : "среди ").append(Integer.toString(y + z), NumberWord.getStringForNumber(y + z, Gender.MASCULINE, Case.GENITIVE))
                        .append(" детей, которые ").append(verb.getPlural()).append(" ").append(GOVERN_FORM.get(verb).apply(chosenSubjects[1]))
                        .append(", ").append(conditions.get("x + y").getTextWithCount(y, selectVerb(SCENARIOS[scenario], verbCount++)));
                chosenVariables.remove("x + y");
            } else {
                text.append(conditions.get("y + z").getTextWithCount(selectVerb(SCENARIOS[scenario], verbCount++)));
            }
            chosenVariables.remove("y + z");
            text.append(". ");
        }


        for (String chosenVariable : chosenVariables) {
            if (excludeHero) {
                text.append(conditions.get(chosenVariable).getFullTextWithCount(selectVerb(SCENARIOS[scenario], verbCount++), selectVerb(SCENARIOS[scenario], verbCount++)));
            } else {
                text.append(conditions.get(chosenVariable).getTextWithCount(selectVerb(SCENARIOS[scenario], verbCount++)));
            }
            text.append(". ");
        }

        final String question = chooseQuestion(scenarioVariables);

        int answer = conditions.get(question).count;
        text.append("Сколько детей ");
        if (excludeHero) {
            text.append(conditions.get(question).getFullQuestion(selectVerb(SCENARIOS[scenario], verbCount++), selectVerb(SCENARIOS[scenario], verbCount)));
            if (SCENARIOS[scenario].equals(CLASS) || SCENARIOS[scenario].equals(CAMP)) {
                text.append(", если только ").append(hero[0]).append(" не занимается ни в одном кружке");
            }
        } else {
            text.append(conditions.get(question).getQuestion(selectVerb(SCENARIOS[scenario], verbCount)));
        }
        text.append("?");

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

    private HashSet<String> chooseVariables(String[] initialVariables) {
        final HashSet<String> allVariables = Sets.newHashSet(initialVariables);
        final String[] firstAndSecond = chooseRandomString(initialVariables, 2);
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
        private final boolean bothObjects;

        Condition(int count, String text, AbstractNoun object) {
            this(count, text, object, null, false);
        }

        Condition(int count, String text, AbstractNoun object1, AbstractNoun object2, boolean bothObjects) {
            this.count = count;
            this.text = text;
            this.object1 = object1;
            this.object2 = object2;
            this.bothObjects = bothObjects;
        }

        public Condition(int count, String text) {
            this(count, text, null, null, false);
        }

        String getTextWithCount(Verb verb) {
            if (object1 == null) {
                return getNumWithString(count, CHILD) + " " + (text.isEmpty() ? "" : (text + " "));
            } else {
                return getNumWithString(count, CHILD) + " "
                        + (text.isEmpty() ? "" : (text + " "))
                        + (count % 10 == 1 ? verb.getSingular() : verb.getPlural()) + " "
                        + (bothObjects ? "и " : "")
                        + GOVERN_FORM.get(verb).apply(object1)
                        + (bothObjects ? (", и " + GOVERN_FORM.get(verb).apply(object2)) : "");
            }
        }

        String getTextWithCount(int count, Verb verb) {
            if (object1 == null) {
                return getNumWithString(count, CHILD) + " " + (text.isEmpty() ? "" : (text + " "));
            } else {
                return getNumWithString(count, CHILD) + " "
                        + (text.isEmpty() ? "" : (text + " "))
                        + (count % 10 == 1 ? verb.getSingular() : verb.getPlural()) + " "
                        + (bothObjects ? "и " : "")
                        + GOVERN_FORM.get(verb).apply(object1)
                        + (bothObjects ? (", и " + GOVERN_FORM.get(verb).apply(object2)) : "");
            }
        }

        String getFullTextWithCount(Verb verb, Verb verb2) {
            if (object1 == null || object2 == null || bothObjects) {
                return getTextWithCount(verb);
            }
            return getNumWithString(count, CHILD) + " "
                    + (count % 10 == 1 ? verb.getSingular() : verb.getPlural()) + " "
                    + GOVERN_FORM.get(verb).apply(object2)
                    + ", но " + text
                    + (count % 10 == 1 ? verb2.getSingular() : verb2.getPlural()) + " "
                    + GOVERN_FORM.get(verb2).apply(object1);

        }


        String getTextWithCountWithoutVerb(Function<AbstractNoun, String> governForm) {
            return getNumWithString(count, CHILD) + " " + text + " - "
                    + (bothObjects ? "и " : "")
                    + governForm.apply(object1)
                    + (bothObjects ? (" и " + governForm.apply(object2)) : "");
        }

        String getQuestion(Verb verb) {
            if (object1 == null) {
                return text;
            } else {
                return (text.isEmpty() ? "" : (text + " "))
                        + verb.getPlural() + " "
                        + (bothObjects ? "и " : "")
                        + GOVERN_FORM.get(verb).apply(object1)
                        + (bothObjects ? (", и " + GOVERN_FORM.get(verb).apply(object2)) : "");
            }
        }

        String getFullQuestion(Verb verb, Verb verb2) {
            if (object1 == null || object2 == null || bothObjects) {
                return getQuestion(verb);
            }
            return verb.getPlural() + " "
                    + GOVERN_FORM.get(verb).apply(object2)
                    + ", но " + text
                    + verb2.getPlural() + " "
                    + GOVERN_FORM.get(verb2).apply(object1);

        }
    }

    private String getScenarioVerb(String scenario, boolean excludeHero) {
        if (excludeHero) {
            if (scenario.equals(CLASS) || scenario.equals(CAMP)) {
                return randomInt(0, 2) == 0 ? "ходят в кружки" : "ходят хотя бы в один кружок";
            } else if (scenario.equals(PARTY)) {
                return randomInt(0, 2) == 0 ? "пришли в гости" : "cъели хотя бы одну конфету";
            }
        }
        if (scenario.equals(CLASS)) {
            return "учится в классе";
        } else if (scenario.equals(CAMP)) {
            return "приехали в лагерь";
        } else if (scenario.equals(PARTY)) {
            return "были на дне рождения";
        }
        throw new IllegalArgumentException();
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
