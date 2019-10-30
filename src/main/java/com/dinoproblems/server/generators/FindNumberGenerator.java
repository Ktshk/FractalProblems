package com.dinoproblems.server.generators;

import com.dinoproblems.server.*;
import com.dinoproblems.server.utils.TextWithTTSBuilder;
import com.google.common.collect.Sets;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import static com.dinoproblems.server.utils.GeneratorUtils.findAvailableScenario;

/**
 * Created by Katushka on 18.04.2019.
 */
public class FindNumberGenerator implements ProblemGenerator {
    private static final Map<Problem.Difficulty, Map<ProblemScenario, Problem>> PROBLEMS = new HashMap<>();

    static {
        PROBLEMS.put(Problem.Difficulty.EASY, new HashMap<>());
        PROBLEMS.put(Problem.Difficulty.MEDIUM, new HashMap<>());
        PROBLEMS.put(Problem.Difficulty.HARD, new HashMap<>());
        PROBLEMS.put(Problem.Difficulty.EXPERT, new HashMap<>());

        TextWithTTSBuilder text = new TextWithTTSBuilder();
        text.append("Найди самое большое трёхзначное число, состоящее из разных нечётных цифр.");
        String hint = "Какая самая большая нечетная цифра? Подумайте, на каком месте она должна стоять";
        ProblemScenario scenario = new ProblemScenarioImpl(ProblemCollection.FIND_NUMBER + "_ODD_FIGURES", true);
        PROBLEMS.get(Problem.Difficulty.EASY).put(scenario, new ProblemWithPossibleTextAnswers.Builder().text(text.getText()).tts(text.getTTS()).answer(975).theme(ProblemCollection.FIND_NUMBER).possibleTextAnswers(Sets.newHashSet("975")).hint(hint).scenario(scenario).difficulty(Problem.Difficulty.EASY).create());

        text = new TextWithTTSBuilder();
        text.append("Миша выписал подряд без ").append("пробелов", "проб+елов").append(" и запятых все числа с 1 до 100. " +
                "Какое минимальное количество цифр подряд с начала ему надо вычеркнуть, чтобы число начиналось с 31?");
        hint = "Выпишите числа от 1 и посмотрите внимательно, когда после 3 идет 1.";
        scenario = new ProblemScenarioImpl(ProblemCollection.FIND_NUMBER + "_31", true);
        PROBLEMS.get(Problem.Difficulty.EASY).put(scenario, new ProblemWithPossibleTextAnswers.Builder().text(text.getText()).tts(text.getTTS()).answer(16).theme(ProblemCollection.FIND_NUMBER).possibleTextAnswers(Sets.newHashSet("16 цифр")).hint(hint).scenario(scenario).difficulty(Problem.Difficulty.EASY).create());

        text = new TextWithTTSBuilder();
        text.append("Найди самое маленькое трехзначное число, сумма цифр которого равна четырём, и при этом ")
                .append("все", "все")
                .append(" три цифры различные.");
        hint = "Подумайте, какие три различных числа в сумме дадут 4.";
        scenario = new ProblemScenarioImpl(ProblemCollection.FIND_NUMBER + "_DIGITS_SUM_4", true);
        PROBLEMS.get(Problem.Difficulty.EASY).put(scenario, new ProblemWithPossibleTextAnswers.Builder().text(text.getText()).tts(text.getTTS()).answer(103).theme(ProblemCollection.FIND_NUMBER).possibleTextAnswers(Sets.newHashSet("103")).hint(hint).scenario(scenario).difficulty(Problem.Difficulty.EASY).create());

        text = new TextWithTTSBuilder();
        text.append("Сумма двух чисел равна 385. Первое из них оканчивается нулём. Если 0 зачеркнуть, то получится второе число. Чему равно первое число?");
        hint = "Во сколько раз второе число меньше первого? Подумайте, сколько вторых чисел поместится в 385.";
        scenario = new ProblemScenarioImpl(ProblemCollection.FIND_NUMBER + "_NUMBERS_SUM_385", true);
        PROBLEMS.get(Problem.Difficulty.HARD).put(scenario, new ProblemWithPossibleTextAnswers.Builder().text(text.getText()).tts(text.getTTS()).answer(350).theme(ProblemCollection.FIND_NUMBER).possibleTextAnswers(Sets.newHashSet("350")).hint(hint).scenario(scenario).difficulty(Problem.Difficulty.HARD).create());


//        Найдите наибольшее шестизначное число, у которого каждая цифра, начиная
//        с третьей, равна сумме двух предыдущих цифр.

        text = new TextWithTTSBuilder();
        text.append("Найдите наибольшее шестизначное число, у которого каждая цифра, начиная с третьей, равна сумме двух предыдущих цифр.");
        hint = "Заметьте, когда в двух числах количество цифр совпадает, то больше будет то, у которого больше первая цифра. ";
        scenario = new ProblemScenarioImpl(ProblemCollection.FIND_NUMBER + "_DIGIT_IS_SUM_DIGITS", true);
        final ProblemWithPossibleTextAnswers problem = new ProblemWithPossibleTextAnswers.Builder()
                .text(text.getText()).tts(text.getTTS())
                .answer(303369)
                .theme(ProblemCollection.FIND_NUMBER)
                .possibleTextAnswers(Sets.newHashSet("303369"))
                .hint(hint).scenario(scenario)
                .solution(new TextWithTTSBuilder().append("Если первая цифра была a, а вторая  — b, то третья будет (a + b), четвёртая  — (a + 2b), пятая  — (2a + 3b), шестая  — (3a + 5b). Нам надо подобрать максимальное возможное значение a, чтобы выполнялось неравенство 3a + 5b < 10. Это возможно при a = 3, b = 0, то есть искомое число будет 303369. "))
                .difficulty(Problem.Difficulty.EXPERT).create();
        PROBLEMS.get(Problem.Difficulty.EXPERT).put(scenario, problem);

    }

    @Nonnull
    @Override
    public Problem generateProblem(Problem.Difficulty difficulty, ProblemAvailability problemAvailability) {
        final ProblemScenario scenario = problemAvailability.getScenario();
        if (PROBLEMS.containsKey(difficulty) && PROBLEMS.get(difficulty).containsKey(scenario)) {
            return PROBLEMS.get(difficulty).get(scenario);
        }
        throw new IllegalArgumentException();
    }

    @Nullable
    @Override
    public ProblemAvailability hasProblem(@Nonnull Collection<Problem> alreadySolvedProblems, @Nonnull Problem.Difficulty difficulty) {
        if (PROBLEMS.containsKey(difficulty) && !PROBLEMS.get(difficulty).isEmpty()) {
            return findAvailableScenario(difficulty, alreadySolvedProblems, PROBLEMS.get(difficulty).keySet(), new HashSet<>());
        } else {
            return null;
        }
    }
}
