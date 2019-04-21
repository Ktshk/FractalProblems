package com.dinoproblems.server.generators;

import com.dinoproblems.server.*;
import com.dinoproblems.server.Problem.Difficulty;
import com.dinoproblems.server.utils.GeneratorUtils;
import com.dinoproblems.server.utils.GeneratorUtils.Case;
import com.dinoproblems.server.utils.ProblemTextBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashSet;

import static com.dinoproblems.server.ProblemCollection.SHARE_WITH_FRIENDS;
import static com.dinoproblems.server.utils.Dictionary.CANDY;
import static com.dinoproblems.server.utils.GeneratorUtils.getNumWithString;
import static com.dinoproblems.server.utils.GeneratorUtils.randomInt;

/**
 * Created by Katushka on 18.04.2019.
 */
public class ShareWithFriendsGenerator implements ProblemGenerator {
    private final static ProblemScenario DEFAULT_SCENARIO = new ProblemScenarioImpl(SHARE_WITH_FRIENDS + "DEFAULT");
    private final static ProblemScenario INDIRECT_SCENARIO = new ProblemScenarioImpl(SHARE_WITH_FRIENDS + "INDIRECT");

    @Nonnull
    @Override
    public Problem generateProblem(Difficulty difficulty, ProblemAvailability problemAvailability) {
        String[][] heroes1 = new String[][]{{"Миша", "Миши", "Мише"}, {"Вася", "Васи", "Васе"},
                {"Петя", "Пети", "Пете"}, {"Витя", "Вити", "Вите"}, {"Андрей", "Андрея", "Андрею"}};
        String[][] heroes2 = new String[][]{{"Маша", "Маши", "Маше"}, {"Вика", "Вики", "Вике"},
                {"Даша", "Даши", "Даше"}, {"Алёна", "Алёны", "Алёне"}, {"Катя", "Кати", "Кате"}};
        String[] hero1 = heroes1[randomInt(0, heroes1.length)];
        String[] hero2 = heroes2[randomInt(0, heroes2.length)];

        final ProblemScenario scenario = problemAvailability.getScenario();

        if (scenario.equals(DEFAULT_SCENARIO)) {
            int candies = randomInt(2, 7);
            ProblemTextBuilder text = new ProblemTextBuilder();
            text.append("У ").append(hero1[1]).append(" и ").append(hero2[1]).append(" было поровну конфет. ")
                    .append(hero1[0]).append(" отдал ").append(hero2[2]).append(" ").append(getNumWithString(candies, CANDY, Case.ACCUSATIVE)).append(". ")
                    .append("На сколько конфет у ").append(hero2[1]).append(" стало больше, чем у ").append(hero1[1]).append("?");
            int answer = candies * 2;
            String hint = "Подумай, на сколько конфет у " + hero2[1] + " стало больше, чем было до этого. " +
                    "И на сколько конфет у " + hero1[1] + " стало меньше, чем было.";
            final HashSet<String> possibleTextAnswers = Sets.newHashSet("На " + getNumWithString(answer, CANDY, Case.ACCUSATIVE),
                    "На " + getNumWithString(answer, CANDY, Case.ACCUSATIVE) + " больше");
            return new ProblemWithPossibleTextAnswers(text.getText(), text.getTTS(), answer, SHARE_WITH_FRIENDS,
                    possibleTextAnswers, hint, scenario, difficulty);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Nullable
    @Override
    public ProblemAvailability hasProblem(@Nonnull Collection<Problem> alreadySolvedProblems, @Nonnull Difficulty difficulty) {
        if (difficulty == Difficulty.EASY) {
            return GeneratorUtils.findAvailableScenario(Difficulty.EASY, alreadySolvedProblems, Lists.newArrayList(DEFAULT_SCENARIO), new HashSet<>());
        }
//        else if (difficulty == Difficulty.MEDIUM) {
//            return GeneratorUtils.findAvailableScenario(Difficulty.EASY, alreadySolvedProblems, Lists.newArrayList(INDIRECT_SCENARIO), Sets.newHashSet(DEFAULT_SCENARIO));
//        }
        return null;
    }
}
