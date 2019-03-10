package com.dinoproblems.server.generators;

import com.dinoproblems.server.*;
import com.dinoproblems.server.utils.GeneratorUtils;
import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.Set;

import static com.dinoproblems.server.utils.GeneratorUtils.Gender.MASCULINE;
import static com.dinoproblems.server.utils.GeneratorUtils.randomInt;

/**
 * Created by Katushka on 12.02.2019.
 */
public class BrickAndHalfGenerator implements ProblemGenerator {

    @Override
    public Problem generateProblem(Problem.Difficulty difficulty) {
        int k = difficulty == Problem.Difficulty.EASY ? 2 : randomInt(3, 5);
        int n = (difficulty == Problem.Difficulty.EASY ? randomInt(1, 4) :
                randomInt(1, 5)) * (k - 1);
        final int answer = n * k / (k - 1);

        int problemType = randomInt(0, 3);

        if (problemType == 0) {
            final String text = "Миша прошёл " + n * 100 + " метров и ещё " + getPartString(k) + " пути. Какова длина пути?";
            return new ProblemWithPossibleTextAnswers(text, answer * 100, ProblemCollection.BRICK_AND_HALF,
                    Sets.newHashSet(answer + " м", answer * 100 + " метров"),
                    "Подумайте, какую часть пути составляют " + n * 100 + " метров.");
        } else {
            String[] things1 = new String[]{"Кирпич", "Арбуз", "Мешок картошки"};
            String[] things = new String[]{"кирпич", "арбуз", "мешок картошки"};
            String[] thingsMod = new String[]{"кирпича", "арбуза", "мешка картошки"};

            int i = randomInt(0, 3);
            if (problemType == 1) {
                final String text = things1[i] + " весит " + n + " кг и ещё " + getPartString(k) + " " + thingsMod[i] + ". Сколько весит " + things[i] + "?";
                final HashSet<String> possibleTextAnswers = Sets.newHashSet(answer + " кг", GeneratorUtils.getNumWithString(answer, "килограмм", "килограмма", "килограммов", MASCULINE));
                final String hint = "Подумайте, какую часть " + thingsMod[i] + (n % 10 == 1 && (n / 10 == 0 || n / 10 >= 2) ? " составляет " : " составляют ") + n + " кг.";
                return new ProblemWithPossibleTextAnswers(text, answer, ProblemCollection.BRICK_AND_HALF,
                        possibleTextAnswers, hint);
            } else {
                final String text = things1[i] + " стоит " + n * 10 + " рублей и ещё " + getPartString(k) + " " + thingsMod[i] + ". Сколько стоит " + things[i] + "?";
                final String hint = "Подумайте, какую часть " + thingsMod[i] + " можно купить за  " + n * 10 + " рублей.";
                ;
                return new ProblemWithPossibleTextAnswers(text, answer * 10, ProblemCollection.BRICK_AND_HALF,
                        Sets.newHashSet(answer * 10 + " рублей"), hint);

            }
        }
    }

    private String getPartString(int k) {
        if (k == 2) {
            return "половину";
        } else if (k == 3) {
            return "треть";
        } else if (k == 4) {
            return "четверть";
        } else {
            throw new UnsupportedOperationException("Not implemented");
        }
    }

    @Override
    public Set<Problem.Difficulty> getAvailableDifficulties() {
        return Sets.newHashSet(Problem.Difficulty.EASY, Problem.Difficulty.MEDIUM);
    }
}
