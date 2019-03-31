package com.dinoproblems.server.generators;

import com.dinoproblems.server.Problem;
import com.dinoproblems.server.ProblemGenerator;
import com.dinoproblems.server.ProblemWithPossibleTextAnswers;
import com.google.common.collect.Sets;


import java.util.Set;

import com.dinoproblems.server.utils.*;

import static com.dinoproblems.server.ProblemCollection.RANGE;
import static com.dinoproblems.server.utils.Dictionary.PAGE;
import static com.dinoproblems.server.utils.GeneratorUtils.Gender.MASCULINE;
import static com.dinoproblems.server.utils.GeneratorUtils.Gender.FEMININE;
import static com.dinoproblems.server.utils.GeneratorUtils.Gender.NEUTER;
import static com.dinoproblems.server.utils.GeneratorUtils.getNumWithString;
import static com.dinoproblems.server.utils.GeneratorUtils.randomInt;
import static com.dinoproblems.server.Problem.Difficulty.EASY;
/* Scribbled by
    Egor Ovsyannkiov
	30.03.2019
 */

public class RangeGenerator implements ProblemGenerator {

    @Override
    public Problem generateProblem(Problem.Difficulty difficulty) {
        final String[] heroes = new String[]{"Катя", "Маша", "Настя", "Полина", "Лиза", "Саша", "Карина", "Кристина"};

        final int ANSWER;
        final String hero;
        String text;
        String hint;
        Set<String> possibleTextAnswers;

        int first;
        int second;
        int scenario = GeneratorUtils.randomInt(0, 4);
        if (scenario == 0) {

            first = difficulty == EASY ? randomInt(1, 7) : randomInt(1, 11);
            second = difficulty == EASY ? randomInt(11, 20) : randomInt(28, 40);
            ANSWER = second - first + 1;
            final String numsWithText = getNumWithString(ANSWER, "число", "числа", "чисел", NEUTER);
            possibleTextAnswers = Sets.newHashSet(numsWithText);
            text = "Сколько чисел от " + first + " до " + second + "?";
            hint = "Попробуйте отнять от второго числа первое, но не забывайте, что первое число тоже входит в диапазон чисел.";
        } else if (scenario == 1) {

            hero = heroes[randomInt(0, heroes.length)];
            first = difficulty == EASY ? randomInt(1, 8) : randomInt(1, 11);
            second = difficulty == EASY ? randomInt(2, 13) : randomInt(8, 28);
            ANSWER = second + first - 1;
            final String pagesWithText = getNumWithString(second, PAGE, GeneratorUtils.Case.ACCUSATIVE);
            possibleTextAnswers = Sets.newHashSet(ANSWER + " странице");
            text = hero + " начала читать книгу с " + first + " страницы и прочитала ровно " + pagesWithText +
                    ". На какой странице " + hero + " закончила?";
            hint = "Попробуйте сложить страницы, но не забывайте, что страница, с которой " + hero +
                    " начала читать тоже входит в количество прочитанных страниц.";
        } else if (scenario == 2) {
            String[] booksource = new String[]{" в библиотеке ", " у подруги ", " у друга ", " у учителя "};
            String book = booksource[randomInt(0, booksource.length)];
            hero = heroes[randomInt(0, heroes.length)];
            first = difficulty == EASY ? randomInt(1, 7) : randomInt(1, 11);
            second = difficulty == EASY ? randomInt(11, 20) : randomInt(28, 41);
            ANSWER = second - first - 1;
            final String pagesWithText = getNumWithString(ANSWER, PAGE);
            possibleTextAnswers = Sets.newHashSet(pagesWithText);
            text = hero + " взяла" + book + "книгу. В середине книги она обнаружила, что после страницы " + first +
                    " сразу идет страница " + second + ". Сколько страниц было вырвано из книги?";
            hint = "Попробуйте вычесть страницы, но не забывайте, что " + first + " " + second +
                    " не входят в количество вырванных страниц.";
        } else {
            String[] months = new String[]{" января ", " марта ", " мая ", " июля ", " августа ", " октября "};
            String[] nextMonths = new String[]{" февраля ", " апреля ", " июня ", " августа ", " сентября ", " ноября "};
            int monthIndex = randomInt(0, months.length);
            hero = heroes[randomInt(0, heroes.length)];
            String beginMonth = months[monthIndex];
            String endMonth = nextMonths[monthIndex];
            first = difficulty == EASY ? randomInt(10, 20) : randomInt(10, 15);
            second = difficulty == EASY ? randomInt(1, 6) : randomInt(10, 18);
            ANSWER = 31 - first + second + 1;
            final String daysWithText = getNumWithString(ANSWER, "день", "дня", "дней", MASCULINE);
            possibleTextAnswers = Sets.newHashSet(daysWithText);
            text = "На дверях библиотеки " + hero + " прочла: «С " + first + beginMonth + "по " + second + endMonth +
                    "библиотека не работает». Сколько дней отпуск у библиотекаря? (В месяце выхода в отпуск 31 день)";
            hint = "Попробуйте из количества дней в месяце, в котором начинается отпуск, вычесть дату начала отпуска. " +
                    "Не забывайте, что день начала и день конца отпуска входят в количество дней в отпуске.";
        }
        return new ProblemWithPossibleTextAnswers(text, first, RANGE, possibleTextAnswers, hint);
    }

    @Override
    public Set<Problem.Difficulty> getAvailableDifficulties() {
        return Sets.newHashSet(Problem.Difficulty.EASY, Problem.Difficulty.MEDIUM);
    }
}
		