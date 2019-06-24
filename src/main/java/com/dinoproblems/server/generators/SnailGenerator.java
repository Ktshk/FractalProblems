package com.dinoproblems.server.generators;

import com.dinoproblems.server.*;
import com.dinoproblems.server.Problem.Difficulty;
import com.dinoproblems.server.utils.GeneratorUtils;
import com.dinoproblems.server.utils.OrdinalNumber;
import com.dinoproblems.server.utils.TextWithTTSBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashSet;

import static com.dinoproblems.server.utils.GeneratorUtils.Gender.FEMININE;
import static com.dinoproblems.server.utils.GeneratorUtils.Gender.MASCULINE;
import static com.dinoproblems.server.utils.GeneratorUtils.findAvailableScenario;
import static com.dinoproblems.server.utils.GeneratorUtils.randomInt;

/**
 * Created by Katushka on 15.02.2019.
 */
public class SnailGenerator implements ProblemGenerator {

    private final static ProblemScenario HARD_SCENARIO = new ProblemScenarioImpl(ProblemCollection.SNAIL + "_" + "HARD", true);
    private final static ProblemScenario MEDIUM_AND_EASY_SCENARIO = new ProblemScenarioImpl(ProblemCollection.SNAIL + "_" + "MEDIUM_AND_EASY");
    private final static Problem TEREMOK;
    private final static ProblemScenario TEREMOK_SCENARIO = new ProblemScenarioImpl(ProblemCollection.SNAIL + "_" + "TEREMOK", true);

    private final static Problem HARD_PROBLEM;

    static {
        TextWithTTSBuilder text = new TextWithTTSBuilder();
        text.append("Однажды улитка заползла на вершину бамбука, который растет так, что каждая его точка поднимается вверх с одной и той же скоростью. " +
                "Путь вверх занял у улитки 7 часов. Отдохнув на вершине бамбука ровно час, она спустилась на землю за 8 часов. " +
                "Во сколько раз скорость улитки больше скорости роста бамбука?");
        String hint = "Подумайте, насколько вырос бамбук, пока улитка отдыхала.";
        HARD_PROBLEM = new ProblemWithPossibleTextAnswers.Builder().text(text.getText()).tts(text.getTTS()).answer(16).theme(ProblemCollection.SNAIL).possibleTextAnswers(Sets.newHashSet("в 16 раз", "16 раз", "в 16 раз больше")).hint(hint).scenario(HARD_SCENARIO).difficulty(Difficulty.EXPERT).create();

        text = new TextWithTTSBuilder();
        text.append("Мышка, Заяц и Ежик строили один общий восемнадцатиэтажный теремок. За день Мышка ")
                .append("строила", "стр+оила").append(" один этаж, Ежик - два ").append("этажа", "этаж+а")
                .append(", а Заяц - три. Каждую ночь  приходил Серый Волк и начисто ломал два верхних этажа. На какой день Теремок будет достроен?");
        hint = "До какого этажа достроят теремок звери за три дня? Подумайте, что будет потом";
        TEREMOK = new ProblemWithPossibleTextAnswers.Builder().text(text.getText()).tts(text.getTTS()).answer(4).theme(ProblemCollection.SNAIL).possibleTextAnswers(Sets.newHashSet("4 дня", "За 4 дня")).hint(hint).scenario(TEREMOK_SCENARIO).difficulty(Difficulty.MEDIUM).create();

    }

    @Nonnull
    @Override
    public Problem generateProblem(Difficulty difficulty, ProblemAvailability problemAvailability) {
        if (difficulty == Difficulty.EXPERT) {
            return HARD_PROBLEM;
        }
        if (problemAvailability.getScenario().equals(TEREMOK_SCENARIO)) {
            return TEREMOK;
        }

        String[] heroes = {"Улитка", "Червяк", "Муравей", "Паучок"};
        String[] heroesLowCase = {"улитка", "червяк", "муравей", "паучок"};
        String[] trees = {"столбу", "стволу липы", "стволу яблони", "водосточной трубе"};
        int hero = randomInt(0, heroes.length);
        int tree = randomInt(0, trees.length);

        String hint;

        int d = difficulty == Difficulty.MEDIUM ? randomInt(2, 5) :
                randomInt(3, 7);
        int n = randomInt(Math.max(1, d - 4), difficulty == Difficulty.HARD ? d - 1 : d);
        int answer = difficulty == Difficulty.MEDIUM ? randomInt(4, 7) : randomInt(8, 16);
        int h = answer * (d - n) + 2 * n - d + 1 + (difficulty == Difficulty.MEDIUM ? (d - n - 1) : randomInt(0, d - n));
        if (h == answer * (d - n)) {
            h++;
        }
        String text = heroes[hero] + " ползет по " + trees[tree] + " высотой " + getMetersString(h) + ". "
                + "За день " + (hero == 0 ? "она" : "он") + " поднимается на " + getMetersString(d) + ", а за ночь опускается на " + getMetersString(n) + ". "
                + "Сколько дней " + (hero == 0 ? "ей" : "ему") + " потребуется, чтобы подняться на вершину?";
        hint = "Посчитайте, где будет " + heroesLowCase[hero] + " вечером на " + OrdinalNumber.number(answer - 1).getNominativeMasculine() + " день," +
                "  и где " + (hero == 0 ? "она" : "он") + " будет утром после " + OrdinalNumber.number(answer - 1).getGenitiveForm(FEMININE) + " ночи. ";
        return new ProblemWithPossibleTextAnswers.Builder().text(text).answer(answer).theme(ProblemCollection.SNAIL).possibleTextAnswers(Sets.newHashSet(GeneratorUtils.getNumWithString(answer, "день", "дня", "дней", MASCULINE))).hint(hint).scenario(problemAvailability.getScenario()).difficulty(difficulty).create();
    }

    private String getMetersString(int h) {
        return GeneratorUtils.getNumWithString(h, "метр", "метра", "метров", MASCULINE);
    }

    @Override
    public ProblemAvailability hasProblem(@Nonnull Collection<Problem> alreadySolvedProblems, @Nonnull Difficulty difficulty) {
        switch (difficulty) {
            case EASY:
                return null;
            case MEDIUM:
                return findAvailableScenario(difficulty, alreadySolvedProblems, Lists.newArrayList(MEDIUM_AND_EASY_SCENARIO, TEREMOK_SCENARIO), new HashSet<>());
            case HARD:
                return findAvailableScenario(difficulty, alreadySolvedProblems, Lists.newArrayList(MEDIUM_AND_EASY_SCENARIO),
                        Sets.newHashSet(MEDIUM_AND_EASY_SCENARIO));
            case EXPERT:
                return findAvailableScenario(difficulty, alreadySolvedProblems, Lists.newArrayList(HARD_SCENARIO),
                        Sets.newHashSet(MEDIUM_AND_EASY_SCENARIO));

        }

        throw new IllegalArgumentException();
    }
}
