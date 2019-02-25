package com.dinoproblems.server;

import com.google.common.collect.Sets;

import java.util.Set;

import static com.dinoproblems.server.GeneratorUtils.Gender.MASCULINE;
import static com.dinoproblems.server.GeneratorUtils.getNumWithString;
import static com.dinoproblems.server.GeneratorUtils.randomInt;

/**
 * Created by Katushka on 15.02.2019.
 */
public class SnailGenerator implements ProblemGenerator {
    @Override
    public Problem generateProblem(Problem.Difficulty difficulty) {
        String[] heroes = {"Улитка", "Червяк", "Муравей", "Паучок"};
        String[] trees = {"столбу", "стволу липы", "стволу яблони", "водосточной трубе"};
        int hero = randomInt(0, heroes.length);
        int tree = randomInt(0, trees.length);

        if (difficulty == Problem.Difficulty.HARD) {
            final String text = "Однажды улитка заползла на вершину бамбука, который растет так, что каждая его точка поднимается вверх с одной и той же скоростью. " +
                    "Путь вверх занял у улитки 7 часов. Отдохнув на вершине бамбука ровно час, она спустилась на землю за 8 часов. " +
                    "Во сколько раз скорость улитки больше скорости роста бамбука?";
            return new ProblemWithPossibleTextAnswers(text, 16, ProblemCollection.SNAIL, Sets.newHashSet("в 16 раз"));
        }

        int d = difficulty == Problem.Difficulty.EASY ? randomInt(2, 5) :
                randomInt(3, 6);
        int n = randomInt(1, d);
        int h = difficulty == Problem.Difficulty.EASY ? (randomInt(3, 6) * (d - n) + d) :
                randomInt(4, 8) * (d - n) + randomInt(0, d);
        int answer = (h - n - 1) / (d - n) + 1;
        String text = heroes[hero] + " ползет по " + trees[tree] + " высотой " + getMetersString(h) + ". "
                + "За день " + (hero == 0 ? "она" : "он") + " поднимается на " + getMetersString(d) + ", а за ночь опускается на " + getMetersString(n) + ". "
                + "Сколько дней " + (hero == 0 ? "ей" : "ему") + " потребуется, чтобы подняться на вершину?";
        return new ProblemWithPossibleTextAnswers(text, answer, ProblemCollection.SNAIL, Sets.newHashSet(getNumWithString(answer, "день", "дня", "дней", MASCULINE)));
    }

    private String getMetersString(int h) {
        return getNumWithString(h, "метр", "метра", "метров", MASCULINE);
    }

    @Override
    public Set<Problem.Difficulty> getAvailableDifficulties() {
        return Sets.newHashSet(Problem.Difficulty.EASY, Problem.Difficulty.MEDIUM, Problem.Difficulty.HARD);
    }
}
