package com.dinoproblems.server.generators;

import com.dinoproblems.server.Problem;
import com.dinoproblems.server.ProblemGenerator;
import com.dinoproblems.server.ProblemWithPossibleTextAnswers;
import com.dinoproblems.server.utils.AbstractNoun;
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
    @Override
    public Problem generateProblem(Problem.Difficulty difficulty) {
        int x = difficulty == Problem.Difficulty.EASY ? randomInt(1, 6) : randomInt(8, 16);
        int z = difficulty == Problem.Difficulty.EASY ? randomInt(1, 6) : randomInt(8, 16);
        int y = difficulty == Problem.Difficulty.EASY ? randomInt(Math.max(1, 8 - x - z), 6) : randomInt(5, Math.min(16, 40 - x - z));

        int total = x + y + z;

        int scenario = randomInt(0, 2);
        AbstractNoun[][] subjects = {SUBJECTS, CANDIES};
        String[][] verbs = {{"ходят в кружок", "увлекаются", "занимаются"}, {"ели", "cъели"}};
        Map<String, Function<AbstractNoun, String>> governForm = new HashMap<>();
        governForm.put("ходят в кружок", AbstractNoun::getGenitive);
        governForm.put("увлекаются", AbstractNoun::getInstrumentalForm);
        governForm.put("занимаются", AbstractNoun::getInstrumentalForm);
        governForm.put("ели", AbstractNoun::getAccusativePluralForm);
        governForm.put("cъели", AbstractNoun::getAccusativePluralForm);

        int subject = randomInt(0, subjects.length);
        final AbstractNoun[] chosenSubjects = chooseRandom(subjects[subject], 2, AbstractNoun[]::new);

        final HashMap<String, Condition> conditions = Maps.newHashMap();
        final String[] variables = new String[]{"x", "y", "z", "total", "a", "b"};
        conditions.put("x", new Condition(x, "не " + chooseRandomVerb(verbs[subject], chosenSubjects[1], governForm)));
        conditions.put("z", new Condition(z, "не " + chooseRandomVerb(verbs[subject], chosenSubjects[0], governForm)));
        conditions.put("y", new Condition(y, chooseRandomVerbWithTwoSubjects(verbs[subject], chosenSubjects[0], chosenSubjects[1], governForm)));
        conditions.put("a", new Condition(x + y, chooseRandomVerb(verbs[subject], chosenSubjects[0], governForm)));
        conditions.put("b", new Condition(y + z, chooseRandomVerb(verbs[subject], chosenSubjects[1], governForm)));
        conditions.put("total", new Condition(total, "всего детей " + getScenarioVerb(scenario, difficulty)));

        final HashSet<String> chosenVariables = Sets.newHashSet(chooseRandomString(variables, 3));

        String text = "";

        if (difficulty == Problem.Difficulty.EASY) {
            if (scenario == 0) {
                text += "Во дворе гуляли ";
            } else {
                text += "На день рождения к Маше пришли ";
            }
        } else {
            if (scenario == 0) {
                text += "В классе ";
            } else {
                text += "В летний лагерь приехали ";
            }
        }
        if (chosenVariables.contains("total")) {
            text += getNumWithString(total, CHILD);
            chosenVariables.remove("total");
        } else {
            text += "дети";
        }
        if (subject == 0) {
            text += ". Все они занимаются " + chosenSubjects[0].getInstrumentalForm() + " или " + chosenSubjects[1].getInstrumentalForm();
        } else {
            if (difficulty == Problem.Difficulty.EASY) {
                text += " и все ели конфеты. ";
            } else {
                if (scenario == 0) {
                    text += ". На перемене все купили себе конфеты. ";
                } else {
                    text += ". Всех детей угостили конфетами. ";
                }
            }
        }

        for (String chosenVariable : chosenVariables) {
            text += conditions.get(chosenVariable).getTextWithCount();
            text += ". ";
        }

        final String question = chooseRandomElement(
                Sets.newHashSet(variables).stream()
                        .filter(chosenVariables::contains)
                        .collect(Collectors.toSet()));

        int answer = conditions.get(question).count;
        text += "Сколько детей " + conditions.get(question).text + " ?";

        final HashSet<String> possibleTextAnswers = Sets.newHashSet(getNumWithString(answer, CHILD), conditions.get(question).getTextWithCount());

        final String hint = "Нарисуйте схему. В один круг поместите всех детей, которые любят " + chosenSubjects[0].getAccusativePluralForm() +
                ", а в в другой всех детей, которые любят " + chosenSubjects[1].getAccusativePluralForm();
        return new ProblemWithPossibleTextAnswers(text, answer, EILER_CIRCLES, possibleTextAnswers, hint);

    }

    private static class Condition {
        int count;
        String text;

        public Condition(int count, String text) {
            this.count = count;
            this.text = text;
        }

        String getTextWithCount() {
            return getNumWithString(count, CHILD) + " " + text;
        }
    }

    private String getScenarioVerb(int scenario, Problem.Difficulty difficulty) {
        if (difficulty == Problem.Difficulty.EASY) {
            if (scenario == 0) {
                return "гуляли во дворе";
            } else {
                return "пришли на день рождения";
            }
        } else {
            if (scenario == 0) {
                return "в классе";
            } else {
                return "приехали в лагерь";
            }
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
