package com.dinoproblems.server.generators;

import com.dinoproblems.server.*;
import com.dinoproblems.server.utils.ProblemTextBuilder;
import com.google.common.collect.Sets;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import static com.dinoproblems.server.utils.GeneratorUtils.findAvailableScenario;

/**
 * Created by Katushka on 18.04.2019.
 */
public class FindNumberGenerator implements ProblemGenerator {
    private final static Map<ProblemScenario, Problem> EASY_PROBLEMS = new HashMap<>();
    private final static Map<ProblemScenario, Problem> DIFFICULT_PROBLEMS = new HashMap<>();

    static {
        ProblemTextBuilder text = new ProblemTextBuilder();
        text.append("Найди самое большое трёхзначное число, состоящее из разных нечётных цифр.");
        String hint = "Какая самая большая нечетная цифра? Подумайте, на каком месте она должна стоять";
        ProblemScenario scenario = new ProblemScenarioImpl(ProblemCollection.FIND_NUMBER + "_ODD_FIGURES", true);
        EASY_PROBLEMS.put(scenario, new ProblemWithPossibleTextAnswers.Builder().text(text.getText()).tts(text.getTTS()).answer(975).theme(ProblemCollection.FIND_NUMBER).possibleTextAnswers(Sets.newHashSet("975")).hint(hint).scenario(scenario).difficulty(Problem.Difficulty.EASY).create());

        text = new ProblemTextBuilder();
        text.append("Миша выписал подряд без ").append("пробелов", "проб+елов").append(" и запятых все числа с 1 до 100. " +
                "Какое минимальное количество цифр подряд с начала ему надо вычеркнуть, чтобы число начиналось с 31?");
        hint = "Выпишите числа от 1 и посмотрите внимательно, когда после 3 идет 1.";
        scenario = new ProblemScenarioImpl(ProblemCollection.FIND_NUMBER + "_31", true);
        EASY_PROBLEMS.put(scenario, new ProblemWithPossibleTextAnswers.Builder().text(text.getText()).tts(text.getTTS()).answer(16).theme(ProblemCollection.FIND_NUMBER).possibleTextAnswers(Sets.newHashSet("16 цифр")).hint(hint).scenario(scenario).difficulty(Problem.Difficulty.EASY).create());

        text = new ProblemTextBuilder();
        text.append("Найди самое маленькое трехзначное число, сумма цифр которого равна четырём, и при этом ")
                .append("все", "все")
                .append(" три цифры различные.");
        hint = "Подумайте, какие три различных числа в сумме дадут 4.";
        scenario = new ProblemScenarioImpl(ProblemCollection.FIND_NUMBER + "_DIGITS_SUM_4", true);
        EASY_PROBLEMS.put(scenario, new ProblemWithPossibleTextAnswers.Builder().text(text.getText()).tts(text.getTTS()).answer(103).theme(ProblemCollection.FIND_NUMBER).possibleTextAnswers(Sets.newHashSet("103")).hint(hint).scenario(scenario).difficulty(Problem.Difficulty.EASY).create());

        text = new ProblemTextBuilder();
        text.append("Сумма двух чисел равна 385. Первое из них оканчивается нулём. Если 0 зачеркнуть, то получится второе число. Чему равно первое число?");
        hint = "Во сколько раз второе число меньше первого? Подумайте, сколько вторых чисел поместится в 385.";
        scenario = new ProblemScenarioImpl(ProblemCollection.FIND_NUMBER + "_NUMBERS_SUM_385", true);
        DIFFICULT_PROBLEMS.put(scenario, new ProblemWithPossibleTextAnswers.Builder().text(text.getText()).tts(text.getTTS()).answer(350).theme(ProblemCollection.FIND_NUMBER).possibleTextAnswers(Sets.newHashSet("350")).hint(hint).scenario(scenario).difficulty(Problem.Difficulty.HARD).create());

    }

    @Nonnull
    @Override
    public Problem generateProblem(Problem.Difficulty difficulty, ProblemAvailability problemAvailability) {
        final ProblemScenario scenario = problemAvailability.getScenario();
        if (difficulty == Problem.Difficulty.EASY) {
            if (EASY_PROBLEMS.containsKey(scenario)) {
                return EASY_PROBLEMS.get(scenario);
            }
        }
        throw new IllegalArgumentException();
    }

    @Nullable
    @Override
    public ProblemAvailability hasProblem(@Nonnull Collection<Problem> alreadySolvedProblems, @Nonnull Problem.Difficulty difficulty) {
        switch (difficulty) {
            case EASY:
                return findAvailableScenario(difficulty, alreadySolvedProblems, EASY_PROBLEMS.keySet(), new HashSet<>());
            case MEDIUM:
            case HARD:
            case EXPERT:
                return null;
        }

        throw new IllegalArgumentException();
    }
}
