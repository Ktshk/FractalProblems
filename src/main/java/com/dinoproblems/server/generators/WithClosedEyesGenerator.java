package com.dinoproblems.server.generators;

import com.dinoproblems.server.Problem;
import com.dinoproblems.server.ProblemGenerator;
import com.dinoproblems.server.ProblemWithPossibleTextAnswers;
import com.dinoproblems.server.utils.Adjective;
import com.dinoproblems.server.utils.AdjectiveWithNoun;
import com.dinoproblems.server.utils.GeneratorUtils;
import com.dinoproblems.server.utils.Noun;
import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

import static com.dinoproblems.server.ProblemCollection.WITH_CLOSED_EYES;
import static com.dinoproblems.server.utils.Dictionary.*;
import static com.dinoproblems.server.utils.GeneratorUtils.Gender.MASCULINE;
import static com.dinoproblems.server.utils.GeneratorUtils.getNumWithString;
import static com.dinoproblems.server.utils.GeneratorUtils.randomInt;

/**
 * Created by Katushka on 27.02.2019.
 */
public class WithClosedEyesGenerator implements ProblemGenerator {

    private static final Adjective[] COLORS = {RED, BLUE, YELLOW, GREEN, BLACK};

    private static final Noun[] THINGS = {BALL, PENCIL, SOCK};
    private static final Noun[] PAIRED_THINGS = {SHOE, GLOVE, SKATES};
    private static final String[] WHERE = {"коробке", "ящике", "сумке", "пакете"};

    @Override
    public Problem generateProblem(Problem.Difficulty difficulty) {
        int scenario = randomInt(0, 4);
        int answer;
        final HashSet<String> possibleTextAnswers = Sets.newHashSet();
        String where = WHERE[randomInt(0, WHERE.length)];

        Noun[] things = (difficulty == Problem.Difficulty.HARD || scenario < 3) ? THINGS : PAIRED_THINGS;
        int thing = randomInt(0, things.length);

        StringBuilder text = new StringBuilder("В " + where + " лежат " + things[thing].getPluralForm() + ": ");

        int[] count = difficulty == Problem.Difficulty.EASY ? new int[2] :
                (difficulty == Problem.Difficulty.HARD ? new int[3] : new int[randomInt(2, scenario < 3 ? 5 : 4)]);
        Adjective[] chosenColors = GeneratorUtils.chooseRandom(COLORS, count.length, Adjective[]::new);

        int min = Integer.MAX_VALUE;
        int max = 0;
        for (int i = 0; i < count.length; i++) {
            count[i] = difficulty == Problem.Difficulty.EASY ? randomInt(4, 8) :
                    (difficulty == Problem.Difficulty.HARD ? randomInt(3, 9) : randomInt(6, scenario < 3 ? 20 : 12));
            min = Math.min(min, count[i]);
            max = Math.max(max, count[i]);
        }

        String hint;

        if (difficulty == Problem.Difficulty.HARD) {
            for (int i = 0; i < count.length; i++) {
                if (i == count.length - 1) {
                    text.append(" и ");
                } else if (i > 0) {
                    text.append(", ");
                }
                text.append(count[i]).append(" ").append(chosenColors[i].getGenitiveForm(MASCULINE));
            }
            text.append(" цвета. Чтобы вытащить 1 ").append(chosenColors[0].getNominative(MASCULINE)).append(" ").append(things[thing].getNominative());
            text.append(", нужно взять наугад ").append(getNumWithString(count[1] + count[2] + 1, things[thing]));

            text.append(". Чтобы вытащить 1 ").append(chosenColors[1].getNominative(MASCULINE)).append(" ").append(things[thing].getNominative());
            text.append(", нужно взять наугад ").append(getNumWithString(count[0] + count[2] + 1, things[thing]));

            text.append(". Чтобы вытащить 1 ").append(chosenColors[2].getNominative(MASCULINE)).append(" ").append(things[thing].getNominative());
            text.append(", нужно взять наугад ").append(getNumWithString(count[0] + count[1] + 1, things[thing]));

            int question = randomInt(0, 4);
            if (question == 0) {
                text.append(". Сколько всего ").append(things[thing].getCountingForm());
                answer = count[0] + count[1] + count[2];
            } else {
                AdjectiveWithNoun questionedThing = new AdjectiveWithNoun(chosenColors[question - 1], things[thing]);
                text.append(". Сколько ").append(questionedThing.getCountingForm());
                answer = count[question - 1];
                possibleTextAnswers.add(getNumWithString(answer, questionedThing));
            }
            text.append(" в ").append(where).append("?");
            possibleTextAnswers.add(getNumWithString(answer, things[thing]));
            hint = "Чтобы вытащить 1 " + chosenColors[0].getNominative(MASCULINE) + " " + things[thing].getNominative() +
                    ", нужно сначала вытащить все " + chosenColors[1].getPluralForm() + chosenColors[2].getPluralForm();
        } else {
            for (int i = 0; i < count.length; i++) {
                if (i == count.length - 1) {
                    text.append(" и ");
                } else if (i > 0) {
                    text.append(", ");
                }
                text.append(count[i]).append(scenario < 3 ? " " : " пар ").append(chosenColors[i].getCountingForm());
            }

            if (scenario < 3) {

                int question = randomInt(scenario == 2 ? 1 : 2, min - 1);

                text.append(". Какое наименьшее количество ")
                        .append(things[thing].getCountingForm())
                        .append(" нужно вытащить, не глядя, чтобы среди них обязательно ")
                        .append(question == 1 ? "оказался " : "оказалось ");

                if (scenario == 0) {
                    text.append(getNumWithString(question, things[thing]));
                    text.append(" одного (любого) цвета?");
                    answer = count.length * (question - 1) + 1;
                    hint = "Допустим, что сначала нам не повезет и мы будем вытаскивать шары разных цветов.";
                } else if (scenario == 1) {
                    text.append(getNumWithString(2, things[thing]));
                    text.append(" разных цветов?");
                    answer = max + 1;
                    hint = "Что если сначала мы будем вытаскивать шары одного цвета? Подумайте, как долго это может продолжаться.";
                } else {
                    text.append(getNumWithString(question, things[thing]));
                    int questionColor = randomInt(0, count.length);
                    text.append(" ");
                    text.append(chosenColors[questionColor].getGenitiveForm(MASCULINE));
                    text.append(" цвета?");
                    answer = 0;
                    for (int i = 0; i < count.length; i++) {
                        if (i != questionColor) {
                            answer += count[i];
                        }
                    }
                    answer += question;
                    hint = "Если сначала нам не повезет, мы будем вытаскивать шары других цветов. " +
                            "Но в когда-то нам точно должен попасться " + chosenColors[questionColor].getNominativeNeuter() + ". ";
                }
                possibleTextAnswers.add(getNumWithString(answer, things[thing]));
            } else {
                text.append(". Какое наименьшее количество ")
                        .append(things[thing].getCountingForm())
                        .append(" нужно вытащить, не глядя, чтобы среди них обязательно оказалась пара ")
                        .append(things[thing].getCountingForm())
                        .append(" одного цвета?");
                answer = 1 + IntStream.of(count).sum();
                possibleTextAnswers.add(getNumWithString(answer, things[thing]));
                hint = "Что если мы будем сначала доставать только левые " + things[thing].getPluralForm() + "? Но для пары нам нужен и левый, и правый. ";
            }
        }
        return new ProblemWithPossibleTextAnswers(text.toString(), answer, WITH_CLOSED_EYES, possibleTextAnswers, hint);
    }

    @Override
    public Set<Problem.Difficulty> getAvailableDifficulties() {
        return Sets.newHashSet(Problem.Difficulty.EASY, Problem.Difficulty.MEDIUM);
    }
}
