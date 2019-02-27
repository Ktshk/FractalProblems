package com.dinoproblems.server.generators;

import com.dinoproblems.server.Problem;
import com.dinoproblems.server.ProblemGenerator;
import com.dinoproblems.server.ProblemWithPossibleTextAnswers;
import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

import static com.dinoproblems.server.ProblemCollection.WITH_CLOSED_EYES;
import static com.dinoproblems.server.generators.GeneratorUtils.getNumWithString;
import static com.dinoproblems.server.generators.GeneratorUtils.randomInt;

/**
 * Created by Katushka on 27.02.2019.
 */
public class WithClosedEyesGenerator implements ProblemGenerator {

    private static final String[][] COLORS = {{"красный", "красного", "красных"}, {"синий", "синего", "синих"},
            {"жёлтый", "жёлтого", "жёлтых"}, {"чёрный", "чёрного", "чёрных"}};
    private static final String[][] THINGS = {{"шарики", "шарик", "шарика", "шариков"},
            {"карандаши", "карандаш", "карандаша", "карандашей"},
            {"носки", "носок", "носка", "носков"}};
    private static final String[][] PAIRED_THINGS = {{"ботинки", "ботинок", "ботинка", "ботинок"},
            {"перчатки", "перчатка", "перчатки", "перчаток"},
            {"коньки", "конёк", "конька", "коньков"}};
    private static final String[] WHERE = {"коробке", "ящике", "сумке", "пакете"};

    @Override
    public Problem generateProblem(Problem.Difficulty difficulty) {
        int scenario = randomInt(0, 4);
        int answer;
        StringBuilder text;
        final HashSet<String> possibleTextAnswers = Sets.newHashSet();

        String[][] things = scenario < 3 ? THINGS : PAIRED_THINGS;
        int thing = randomInt(0, things.length);
        String where = WHERE[randomInt(0, WHERE.length)];

        if (scenario < 3) {
            int[] count = difficulty == Problem.Difficulty.EASY ? new int[2] : new int[randomInt(2, 5)];
            String[][] chosenColors = GeneratorUtils.chooseRandom(COLORS, count.length, String[][]::new);

            int min = Integer.MAX_VALUE;
            int max = 0;
            for (int i = 0; i < count.length; i++) {
                count[i] = difficulty == Problem.Difficulty.EASY ? randomInt(4, 8) : randomInt(6, 20);
                min = Math.min(min, count[i]);
                max = Math.max(max, count[i]);
            }

            text = new StringBuilder("В " + where + " лежат " + things[thing][0] + ": ");
            for (int i = 0; i < count.length; i++) {
                if (i == count.length - 1) {
                    text.append(" и ");
                } else if (i > 0) {
                    text.append(", ");
                }
                text.append(count[i]).append(" ").append(chosenColors[i][2]);
            }
            int question = randomInt(scenario == 2 ? 1 : 2, min - 1);

            text.append(". Какое наименьшее количество ")
                    .append(things[thing][3])
                    .append(" нужно вытащить, не глядя, чтобы среди них обязательно ")
                    .append(question == 1 ? "оказался " : "оказалось ");

            if (scenario == 0) {
                text.append(getNumWithString(question, things[thing][1], things[thing][2], things[thing][3], GeneratorUtils.Gender.MASCULINE));
                text.append(" одного (любого) цвета?");
                answer = count.length * (question - 1) + 1;
            } else if (scenario == 1) {
                text.append(getNumWithString(2, things[thing][1], things[thing][2], things[thing][3], GeneratorUtils.Gender.MASCULINE));
                text.append(" разных цветов?");
                answer = max + 1;
            } else {
                text.append(getNumWithString(question, things[thing][1], things[thing][2], things[thing][3], GeneratorUtils.Gender.MASCULINE));
                int questionColor = randomInt(0, count.length);
                text.append(" ");
                text.append(chosenColors[questionColor][1]);
                text.append(" цвета?");
                answer = 0;
                for (int i = 0; i < count.length; i++) {
                    if (i != questionColor) {
                        answer += count[i];
                    }
                }
                answer += question;
            }
            possibleTextAnswers.add(getNumWithString(answer, things[thing][1], things[thing][2], things[thing][3], GeneratorUtils.Gender.MASCULINE));
        } else {
            int[] count = difficulty == Problem.Difficulty.EASY ? new int[2] : new int[randomInt(2, 3)];
            String[][] chosenColors = GeneratorUtils.chooseRandom(COLORS, count.length, String[][]::new);

            int min = Integer.MAX_VALUE;
            int max = 0;
            for (int i = 0; i < count.length; i++) {
                count[i] = difficulty == Problem.Difficulty.EASY ? randomInt(4, 8) : randomInt(6, 12);
                min = Math.min(min, count[i]);
                max = Math.max(max, count[i]);
            }

            text = new StringBuilder("В " + where + " лежат " + things[thing][0] + ": ");
            for (int i = 0; i < count.length; i++) {
                if (i == count.length - 1) {
                    text.append(" и ");
                } else if (i > 0) {
                    text.append(", ");
                }
                text.append(count[i]).append(" пар ").append(chosenColors[i][2]);
            }

            text.append(". Какое наименьшее количество ")
                    .append(things[thing][3])
                    .append(" нужно вытащить, не глядя, чтобы среди них обязательно оказалась пара ")
                    .append(things[thing][3])
                    .append(" одного цвета?");
            answer = 1 + IntStream.of(count).sum();
            possibleTextAnswers.add(getNumWithString(answer, things[thing][1], things[thing][2], things[thing][3], GeneratorUtils.Gender.MASCULINE));
        }
        return new ProblemWithPossibleTextAnswers(text.toString(), answer, WITH_CLOSED_EYES, possibleTextAnswers);
    }

    @Override
    public Set<Problem.Difficulty> getAvailableDifficulties() {
        return Sets.newHashSet(Problem.Difficulty.EASY, Problem.Difficulty.MEDIUM);
    }
}
