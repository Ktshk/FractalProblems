package com.dinoproblems.server;

import com.google.common.collect.Sets;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Katushka on 12.02.2019.
 */
public class KirpichGenerator implements ProblemGenerator {

    @Override
    public Problem generateProblem(Problem.Difficulty difficulty) {
        int k = difficulty == Problem.Difficulty.EASY ? 2 : ThreadLocalRandom.current().nextInt(3, 5);
        int n = (difficulty == Problem.Difficulty.EASY ? ThreadLocalRandom.current().nextInt(1, 4) :
                ThreadLocalRandom.current().nextInt(1, 5)) * (k - 1);
        final int answer = n * k / (k - 1);


        int problemType = ThreadLocalRandom.current().nextInt(0, 3);

        if (problemType == 0) {
            final String text = "Миша прошёл " + n * 100 + " метров и ещё " + getPartString(k) + " пути. Какова длина пути?";
            return new ProblemWithPossibleTextAnswers(text, answer * 100, ProblemCollection.KIRPICH,
                    Sets.newHashSet(answer + " м", answer * 100 + " метров"));
        } else {
            String[] things1 = new String[]{"Кирпич", "Арбуз", "Мешок картошки"};
            String[] things = new String[]{"кирпич", "арбуз", "мешок картошки"};
            String[] thingsMod = new String[]{"кирпича", "арбуза", "мешка картошки"};

            int i = ThreadLocalRandom.current().nextInt(0, 3);
            if (problemType == 1) {
                final String text = things1[i] + " весит " + n + " кг и ещё " + getPartString(k) + " " + thingsMod[i] + ". Сколько весит " + things[i] + "?";
                return new ProblemWithPossibleTextAnswers(text, answer, ProblemCollection.KIRPICH,
                        Sets.newHashSet(answer + " кг", GeneratorUtils.getNumWithString(answer, "килограмм", "килограмма", "килограммов")));
            } else {
                final String text = things1[i] + " стоит " + n * 10 + " рублей и ещё " + getPartString(k) + " " + thingsMod[i] + ". Сколько стоит " + things[i] + "?";
                return new ProblemWithPossibleTextAnswers(text, answer, ProblemCollection.KIRPICH,
                        Sets.newHashSet(answer * 10 +  " рублей"));

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
