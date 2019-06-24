package com.dinoproblems.server.generators;

import com.dinoproblems.server.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.dinoproblems.server.utils.NumberWord;
import com.dinoproblems.server.utils.TextWithTTSBuilder;
import com.dinoproblems.server.utils.OrdinalNumber;


import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.dinoproblems.server.utils.*;

import javax.annotation.Nonnull;

import static com.dinoproblems.server.Problem.Difficulty.EXPERT;
import static com.dinoproblems.server.Problem.Difficulty.MEDIUM;
import static com.dinoproblems.server.ProblemCollection.RANGE;
import static com.dinoproblems.server.utils.Dictionary.NUMBER;
import static com.dinoproblems.server.utils.Dictionary.PAGE;
import static com.dinoproblems.server.utils.GeneratorUtils.Gender.MASCULINE;
import static com.dinoproblems.server.utils.GeneratorUtils.Gender.NEUTER;
import static com.dinoproblems.server.utils.GeneratorUtils.Gender.FEMININE;
import static com.dinoproblems.server.utils.GeneratorUtils.findAvailableScenario;
import static com.dinoproblems.server.utils.GeneratorUtils.getNumWithString;
import static com.dinoproblems.server.utils.GeneratorUtils.randomInt;
import static com.dinoproblems.server.Problem.Difficulty.EASY;
/* Scribbled by
    Egor Ovsyannkiov
	30.03.2019
 */

public class RangeGenerator implements ProblemGenerator {

    private final static ProblemScenario NUMBERS = new ProblemScenarioImpl(ProblemCollection.RANGE + "_" + "NUMBERS");
    private final static ProblemScenario NUMBERS_NOT_LESS_NOT_GREATER = new ProblemScenarioImpl(ProblemCollection.RANGE + "_" + "NUMBERS_NOT_LESS_NOT_GREATER");
    private final static ProblemScenario READ_PAGES = new ProblemScenarioImpl(ProblemCollection.RANGE + "_" + "READ_PAGES");
    private final static ProblemScenario TORN_OUT_PAGES_EXPERT = new ProblemScenarioImpl(ProblemCollection.RANGE + "_" + "TORN_OUT_PAGES_EXPERT", true);
    private final static ProblemScenario TORN_OUT_PAGES = new ProblemScenarioImpl(ProblemCollection.RANGE + "_" + "TORN_OUT_PAGES");
    private final static ProblemScenario DATES = new ProblemScenarioImpl(ProblemCollection.RANGE + "_" + "DATES");

    private static final ProblemScenario[] SCENARIOS = {NUMBERS, NUMBERS_NOT_LESS_NOT_GREATER, READ_PAGES, TORN_OUT_PAGES, DATES};

    @Nonnull
    @Override
    public Problem generateProblem(Problem.Difficulty difficulty, ProblemAvailability problemAvailability) {
        final String[] heroes = new String[]{"Катя", "Маша", "Настя", "Полина", "Лиза", "Саша", "Карина", "Кристина"};

        final int answer;
        final String hero;
        TextWithTTSBuilder text = new TextWithTTSBuilder();
        String hint;
        Set<String> possibleTextAnswers;

        int first;
        int second;
        ProblemScenario scenario = problemAvailability.getScenario();
        if (scenario.equals(NUMBERS_NOT_LESS_NOT_GREATER)) {
            first = difficulty == EASY ? randomInt(1, 7) : randomInt(1, 11);
            second = difficulty == EASY ? randomInt(11, 20) : randomInt(28, 40);
            answer = second - first + 1;
            final String numsWithText = getNumWithString(answer, NUMBER);
            possibleTextAnswers = Sets.newHashSet(numsWithText);
            text.append("Сколько существует чисел, которые не меньше ");
            text.append(Integer.toString(first), NumberWord.getStringForNumber(first, NEUTER, GeneratorUtils.Case.GENITIVE));
            text.append(", но и не больше ");
            text.append(String.valueOf(second), NumberWord.getStringForNumber(second, NEUTER, GeneratorUtils.Case.GENITIVE)).append("?");
            hint = "Какие числа не меньше "+ NumberWord.getStringForNumber(first, NEUTER, GeneratorUtils.Case.GENITIVE) + "? Подумайте, с какого числа надо начинать считать и на каком числе заканчивать.";
        } else if (scenario.equals(NUMBERS)) {
            first = difficulty == EASY ? randomInt(1, 7) : randomInt(1, 11);
            second = difficulty == EASY ? randomInt(11, 20) : randomInt(28, 40);
            answer = second - first + 1;
            possibleTextAnswers = Sets.newHashSet(String.valueOf(answer));
            text.append("Сколько чисел от ");
            text.append(String.valueOf(first), NumberWord.getStringForNumber(first, NEUTER, GeneratorUtils.Case.GENITIVE));
            text.append(" до ");
            text.append(String.valueOf(second), NumberWord.getStringForNumber(second, NEUTER, GeneratorUtils.Case.GENITIVE)).append("?");
            hint = "Попробуйте отнять от второго числа первое, но не забывайте, что первое число тоже входит в диапазон чисел.";
        } else if (scenario.equals(READ_PAGES)) {
            hero = heroes[randomInt(0, heroes.length)];
            first = difficulty == EASY ? randomInt(1, 8) : randomInt(1, 11);
            second = difficulty == EASY ? randomInt(2, 13) : randomInt(8, 28);
            answer = second + first - 1;
            final String pagesWithText = getNumWithString(second, PAGE, GeneratorUtils.Case.ACCUSATIVE);
            possibleTextAnswers = Sets.newHashSet("на странице " + answer);
            text.append(hero + " начала читать книгу ").append(getFromText(first)).append(" ").append(String.valueOf(first), OrdinalNumber.getOrdinalTwoDigitNum(first).getGenitiveForm(FEMININE));
            text.append(" страницы и прочитала ровно " + pagesWithText +
                    ". На какой странице " + hero + " закончила?");
            hint = "Попробуйте сложить страницы, но не забывайте, что страница, с которой " + hero +
                    " начала читать тоже входит в количество прочитанных страниц.";
        } else if (scenario.equals(TORN_OUT_PAGES) || scenario.equals(TORN_OUT_PAGES_EXPERT)) {
            String[] booksource = new String[]{" в библиотеке ", " у подруги ", " у друга ", " у учителя "};
            String book = booksource[randomInt(0, booksource.length)];
            hero = heroes[randomInt(0, heroes.length)];
            text.append(hero + " взяла" + book + "книгу. В середине книги она обнаружила, что после страницы ");
            if (difficulty == EXPERT) {
                int firstDigit = randomInt(1, 5);
                int secondDigit = randomInt(firstDigit + 1, 10);
                int thirdDigit = randomInt(firstDigit + 2, 10);

                if (firstDigit % 2 == 0 && secondDigit % 2 == 0) {
                    secondDigit++;

                } else if (firstDigit % 2 == 1 && secondDigit % 2 == 1) {
                    if (secondDigit == 9) {
                        secondDigit--;
                    } else {
                        secondDigit++;
                    }
                }
                if (firstDigit % 2 == 0 && thirdDigit % 2 == 1) {
                    thirdDigit--;
                }
                if (firstDigit % 2 == 1 && thirdDigit % 2 == 0) {
                    thirdDigit++;
                }
                first = firstDigit * 100 + secondDigit * 10 + thirdDigit;
                second = thirdDigit * 100 + firstDigit * 10 + secondDigit;
                text.append(first + " сразу идет страница, состоящая из тех же цифр. Сколько страниц было вырвано из книги?");
                hint = "Если первая страница на развороте " + first + ", то вторая должна быть другой чётности. Подумайте, на какую цифру она должна кончаться.";
            } else {
                first = difficulty == MEDIUM ? randomInt(1, 7) : randomInt(1, 12);
                if (first % 2 != 0) {
                    first++;
                }
                second = difficulty == MEDIUM ? randomInt(11, 20) : randomInt(28, 41);
                if (second % 2 == 0) {
                    second++;
                }

                text.append(first + " сразу идет страница " + second + ". Сколько страниц было вырвано из книги?");
                hint = "Попробуйте вычесть страницы, но не забывайте, что страницы " + first + " и " + second +
                        " не входят в количество вырванных страниц.";
            }
            answer = second - first - 1;
            final String pagesWithText = getNumWithString(answer, PAGE);
            possibleTextAnswers = Sets.newHashSet(pagesWithText);
        } else {
            String[] months = new String[]{" января ", " марта ", " мая ", " июля ", " августа ", " октября "};
            String[] monthsInstr = new String[]{" январе ", " марте ", " мае ", " июле ", " августе ", " октябре "};
            String[] nextMonths = new String[]{" февраля ", " апреля ", " июня ", " августа ", " сентября ", " ноября "};
            int monthIndex = randomInt(0, months.length);
            hero = heroes[randomInt(0, heroes.length)];
            String beginMonth = months[monthIndex];
            String endMonth = nextMonths[monthIndex];
            first = difficulty == MEDIUM ? randomInt(10, 20) : randomInt(10, 15);
            second = difficulty == MEDIUM ? randomInt(1, 6) : randomInt(10, 18);
            answer = 31 - first + second + 1;
            final String daysWithText = getNumWithString(answer, "день", "дня", "дней", MASCULINE);
            possibleTextAnswers = Sets.newHashSet(daysWithText);
            text.append("На дверях библиотеки " + hero + " прочла: «С ");
            text.append(String.valueOf(first), OrdinalNumber.getOrdinalTwoDigitNum(first).getGenitiveForm(MASCULINE));
            text.append(beginMonth + "по ").append(String.valueOf(second), OrdinalNumber.getOrdinalTwoDigitNum(second).getNominative(NEUTER));
            text.append(endMonth + "библиотека не работает». Сколько дней отпуск у библиотекаря? (В" + monthsInstr[monthIndex] + "31 день)");
            hint = "Вычтите из количества дней в" + monthsInstr[monthIndex] + "дату начала отпуска. " +
                    "Не забывайте, что день начала и день конца отпуска входят в количество дней в отпуске.";
        }
        return new ProblemWithPossibleTextAnswers.Builder().text(text.getText()).tts(text.getTTS()).answer(answer).theme(RANGE).possibleTextAnswers(possibleTextAnswers).hint(hint).scenario(scenario).difficulty(difficulty).create();
    }

    private String getFromText(int fromPage) {
        return fromPage == 2 ? "со" : "с";
    }

    @Override
    public ProblemAvailability hasProblem(@Nonnull Collection<Problem> alreadySolvedProblems, @Nonnull Problem.Difficulty difficulty) {
        switch (difficulty) {
            case EASY:
                return findAvailableScenario(difficulty, alreadySolvedProblems, Lists.newArrayList(NUMBERS, READ_PAGES, NUMBERS_NOT_LESS_NOT_GREATER), new HashSet<>());
            case MEDIUM:
                return findAvailableScenario(difficulty, alreadySolvedProblems, Lists.newArrayList(SCENARIOS), Sets.newHashSet(NUMBERS, READ_PAGES, NUMBERS_NOT_LESS_NOT_GREATER));
            case HARD:
                return findAvailableScenario(difficulty, alreadySolvedProblems, Lists.newArrayList(TORN_OUT_PAGES, DATES), Sets.newHashSet(SCENARIOS));
            case EXPERT:
                return null;
                // TODO: uncomment
//                return findAvailableScenario(difficulty, alreadySolvedProblems, Lists.newArrayList(TORN_OUT_PAGES_EXPERT), Sets.newHashSet(TORN_OUT_PAGES));

        }

        throw new IllegalArgumentException();
    }
}
