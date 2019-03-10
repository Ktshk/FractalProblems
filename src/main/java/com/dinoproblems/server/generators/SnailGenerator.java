package com.dinoproblems.server.generators;

import com.dinoproblems.server.*;
import com.dinoproblems.server.utils.GeneratorUtils;
import com.dinoproblems.server.utils.OrdinalNumber;
import com.google.common.collect.Sets;

import java.util.Set;

import static com.dinoproblems.server.utils.GeneratorUtils.Gender.FEMININE;
import static com.dinoproblems.server.utils.GeneratorUtils.Gender.MASCULINE;
import static com.dinoproblems.server.utils.GeneratorUtils.getNumWithString;
import static com.dinoproblems.server.utils.GeneratorUtils.randomInt;

/**
 * Created by Katushka on 15.02.2019.
 */
public class SnailGenerator implements ProblemGenerator {
    @Override
    public Problem generateProblem(Problem.Difficulty difficulty) {
        String[] heroes = {"Улитка", "Червяк", "Муравей", "Паучок"};
        String[] heroesLowCase = {"улитка", "червяк", "муравей", "паучок"};
        String[] trees = {"столбу", "стволу липы", "стволу яблони", "водосточной трубе"};
        int hero = randomInt(0, heroes.length);
        int tree = randomInt(0, trees.length);

        String hint;
        if (difficulty == Problem.Difficulty.HARD) {
            final String text = "Однажды улитка заползла на вершину бамбука, который растет так, что каждая его точка поднимается вверх с одной и той же скоростью. " +
                    "Путь вверх занял у улитки 7 часов. Отдохнув на вершине бамбука ровно час, она спустилась на землю за 8 часов. " +
                    "Во сколько раз скорость улитки больше скорости роста бамбука?";
            hint = "Подумайте, насколько вырос бамбук, пока улитка отдыхала.";
            return new ProblemWithPossibleTextAnswers(text, 16, ProblemCollection.SNAIL,
                    Sets.newHashSet("в 16 раз", "16 раз", "в 16 раз больше"), hint);
        }

        int d = difficulty == Problem.Difficulty.EASY ? randomInt(2, 5) :
                randomInt(3, 6);
        int n = randomInt(1, d);
        int answer = difficulty == Problem.Difficulty.EASY ? randomInt(4, 7) : randomInt(5, 11);
        int h = answer * (d - n) + 2 * n - d + 1 + (difficulty == Problem.Difficulty.EASY ? (d - n - 1) : randomInt(0, d - n));
        String text = heroes[hero] + " ползет по " + trees[tree] + " высотой " + getMetersString(h) + ". "
                + "За день " + (hero == 0 ? "она" : "он") + " поднимается на " + getMetersString(d) + ", а за ночь опускается на " + getMetersString(n) + ". "
                + "Сколько дней " + (hero == 0 ? "ей" : "ему") + " потребуется, чтобы подняться на вершину?";
        hint = "Посчитайте, где будет " + heroesLowCase[hero] + " вечером на " + OrdinalNumber.number(answer - 1).getNominativeMasculine() + " день," +
                "  и где " + (hero == 0 ? "она" : "он") + " будет утром после " + OrdinalNumber.number(answer - 1).getGenitiveForm(FEMININE) + " ночи. ";
        return new ProblemWithPossibleTextAnswers(text, answer, ProblemCollection.SNAIL, Sets.newHashSet(GeneratorUtils.getNumWithString(answer, "день", "дня", "дней", MASCULINE)), hint);
    }

    private String getMetersString(int h) {
        return GeneratorUtils.getNumWithString(h, "метр", "метра", "метров", MASCULINE);
    }

    @Override
    public Set<Problem.Difficulty> getAvailableDifficulties() {
        return Sets.newHashSet(Problem.Difficulty.EASY, Problem.Difficulty.MEDIUM, Problem.Difficulty.HARD);
    }
}
