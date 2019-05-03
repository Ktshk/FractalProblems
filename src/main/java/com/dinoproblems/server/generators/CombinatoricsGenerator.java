package com.dinoproblems.server.generators;

import com.dinoproblems.server.*;
import com.dinoproblems.server.utils.AbstractNoun;
import com.dinoproblems.server.utils.GeneratorUtils;
import com.dinoproblems.server.utils.NumberWord;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashSet;

import static com.dinoproblems.server.utils.Dictionary.*;
import static com.dinoproblems.server.utils.GeneratorUtils.*;
import static com.dinoproblems.server.utils.GeneratorUtils.Case.GENITIVE;
import static com.dinoproblems.server.utils.GeneratorUtils.Gender.MASCULINE;

/**
 * Created by Katushka on 18.04.2019.
 */
public class CombinatoricsGenerator implements ProblemGenerator {
    private static final ProblemScenario FLOWERS_SCENARIO = new ProblemScenarioImpl(ProblemCollection.COMBINATORICS + "_" + "FLOWERS", true);
    private static final ProblemScenario CHESS_SCENARIO = new ProblemScenarioImpl(ProblemCollection.COMBINATORICS + "_" + "CHESS", true);
    private static final ProblemScenario NUMBER_SAME_DIGITS = new ProblemScenarioImpl(ProblemCollection.COMBINATORICS + "_" + "NUMBER_SAME_DIGITS", false);
    private static final ProblemScenario CARS = new ProblemScenarioImpl(ProblemCollection.COMBINATORICS + "_" + "CARS", true);
//    private static final ProblemScenario DANCING = new ProblemScenarioImpl(ProblemCollection.COMBINATORICS + "_" + "DANCING", true);

    private static final Collection<ProblemScenario> EASY_SCENARIOS = Lists.newArrayList(CARS, FLOWERS_SCENARIO, CHESS_SCENARIO, NUMBER_SAME_DIGITS/*, DANCING*/);
    private static final Collection<ProblemScenario> MEDIUM_SCENARIOS = Lists.newArrayList(FLOWERS_SCENARIO, CHESS_SCENARIO, NUMBER_SAME_DIGITS/*, DANCING*/);
    private static final Collection<ProblemScenario> DIFFICULT_SCENARIOS = Lists.newArrayList(FLOWERS_SCENARIO, NUMBER_SAME_DIGITS);
    private static final String[] HEROES = {"Миша", "Вася", "Петя", "Витя", "Андрей", "Лёша", "Ваня"};
    private static final String[] HEROES_GENITIVE = {"Миши", "Васи", "Пети", "Вити", "Андрея", "Лёши", "Вани"};
    private static final String[] HEROES_GIRLS = {"Маша", "Вика", "Лена", "Наташа", "Алина", "Света", "Лиза"};

    @Nonnull
    @Override
    public Problem generateProblem(Problem.Difficulty difficulty, ProblemAvailability problemAvailability) {
        final ProblemScenario scenario = problemAvailability.getScenario();
        if (scenario.equals(CARS)) {
            int heroIndex = randomInt(0, HEROES.length);
            String text = "У " + HEROES_GENITIVE[heroIndex] + " есть две красные машинки, черная и желтая. " +
                    "Сколькими способами " + HEROES[heroIndex] + " может составить их в ряд? ";
            String hint = "Если первая машинка будет красной, то какой может быть вторая третья и четвертая? А если первая машинка жёлтая или чёрная?";
            int answer = 12;
            return new ProblemWithPossibleTextAnswers(text, answer, ProblemCollection.COMBINATORICS,
                    Sets.newHashSet("12 способов"), hint, CARS, difficulty);
//        } else if (scenario.equals(DANCING)) {
//            int n = randomInt(4, 6);
//            String[] heroes = chooseRandomString(HEROES, n);
//            String[] heroesGirls = chooseRandomString(HEROES_GIRLS, n);
//            String text = "В кружок бального танца записались ";
//            for (String hero : heroes) {
//                text += hero + ", ";
//            }
//            for (int i = 0; i < heroesGirls.length; i++) {
//                String heroesGirl = heroesGirls[i];
//                text += heroesGirl + (i < heroesGirls.length - 1 ? ", " : ". ");
//            }
//            text += " Сколько танцевальных пар девочка - мальчик может образоваться?";
//            String hint = "Подумайте, если первым в паре будет " + heroes[0] + ", то сколько для него есть партнерш. А если первым будет " + heroes[1] + "?";
//            int answer = n * n;
//            return new ProblemWithPossibleTextAnswers(text, answer, ProblemCollection.COMBINATORICS,
//                    Sets.newHashSet(getNumWithString(answer, PAIR)), hint, scenario, difficulty);
        } else if (scenario.equals(FLOWERS_SCENARIO)) {
            int k = difficulty == Problem.Difficulty.MEDIUM || difficulty == Problem.Difficulty.EASY
                    ? 3
                    : (randomInt(1, 3) * 2 + 1);
            int n = difficulty == Problem.Difficulty.EASY
                    ? 2
                    : (difficulty == Problem.Difficulty.MEDIUM
                    ? 3
                    : (k == 3 ? 4 : 3));

            final AbstractNoun[] flowers = GeneratorUtils.chooseRandom(FLOWERS, n, AbstractNoun[]::new);
            final String hero = HEROES[randomInt(0, HEROES.length)];
            String text = "Сколько различных букетов из " + NumberWord.getStringForNumber(k, MASCULINE, GENITIVE) + " цветов может собрать " + hero
                    + ", если у него есть ";
            for (int i = 0; i < flowers.length; i++) {
                AbstractNoun flower = flowers[i];
                text += flower.getPluralForm();
                if (i < flowers.length - 2) {
                    text += ", ";
                } else if (i == flowers.length - 2) {
                    text += " и ";
                }
            }
            text += "?";
            int answer = c(n + k - 1, k);
            final String hint = "Попробуйте выписать все возможные букеты, которые может собрать " + hero + ". " +
                    "Например, три " + flowers[0].getGenitive() + " или " + flowers[0].getNominative() + " и две " + flowers[1].getGenitive() + ".";
            return new ProblemWithPossibleTextAnswers(text, answer, ProblemCollection.COMBINATORICS,
                    Sets.newHashSet(getNumWithString(answer, BOUQUET)), hint, scenario, difficulty);
        } else if (scenario.equals(CHESS_SCENARIO)) {
            int children = difficulty == Problem.Difficulty.EASY ? 4 : randomInt(5, 7);
            final String[] heroes = chooseRandom(HEROES, children, String[]::new);
            String text = "";
            for (int i = 0; i < heroes.length; i++) {
                text += heroes[i];
                if (i < heroes.length - 2) {
                    text += ", ";
                } else if (i == heroes.length - 2) {
                    text += " и ";
                }
            }
            String[] games = {"шахматы", "шашки"};
            text += " играли в " + games[randomInt(0, games.length)] +
                    ". Каждый сыграл с каждым по одной партии. Сколько всего партий было сыграно?";
            final int answer = children * (children - 1) / 2;
            String hint = heroes[0] + " сыграл с каждым, то есть он сыграл " + getNumWithString(children - 1, PARTY) + "" +
                    ". " + heroes[1] + " тоже сыграл с каждым " + getNumWithString(children - 1, PARTY) +
                    ". Но не забывайте, что одну партию они сыграли друг с другом, то есть её надо посчитать всего один раз.";
            return new ProblemWithPossibleTextAnswers(text, answer, ProblemCollection.COMBINATORICS,
                    Sets.newHashSet(getNumWithString(answer, PARTY)), hint, scenario, difficulty);
        } else if (scenario.equals(NUMBER_SAME_DIGITS)) {
            String[] digits = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
            int num = difficulty == Problem.Difficulty.EASY
                    ? randomInt(2, 4)
                    : difficulty == Problem.Difficulty.MEDIUM
                    ? 3
                    : randomInt(4, 6);
            boolean hasZero = (difficulty == Problem.Difficulty.EASY && num == 3) || (difficulty != Problem.Difficulty.EASY && randomInt(0, 2) == 0);
            String[] chosenDigits = chooseRandomString(digits, difficulty == Problem.Difficulty.EASY ? 2 : 3);
            String text = "Сколько существует " + getDigitsAdjective(num) + " чисел, состоящих только из цифр ";
            for (int i = 0; i < chosenDigits.length; i++) {
                text += chosenDigits[i];
                if (i < chosenDigits.length - 2 || (hasZero && i < chosenDigits.length - 1)) {
                    text += ", ";
                } else if (i < chosenDigits.length - 1 || (hasZero && i == chosenDigits.length - 1)) {
                    text += " и ";
                }
            }
            if (hasZero) {
                text += "0";
            }
            text += "?";
            int answer = chosenDigits.length;
            for (int i = 1; i < num; i++) {
                answer *= hasZero ? (chosenDigits.length + 1) : chosenDigits.length;
            }
            String hint = "Подумайте, с каких цифр может начинаться число. А сколько цифр может стоять на втором месте?";
            return new ProblemWithPossibleTextAnswers(text, answer, ProblemCollection.COMBINATORICS,
                    Sets.newHashSet(Integer.toString(answer)), hint, scenario, difficulty);
        } else {
            throw new IllegalArgumentException("Scenario: " + scenario.getScenarioId());
        }
    }

    private String getDigitsAdjective(int num) {
        switch (num) {
            case 2:
                return "двухзначных";
            case 3:
                return "трехзначных";
            case 4:
                return "четырехзначных";
            case 5:
                return "пятизначных";
            case 6:
                return "шестизначных";
        }
        throw new IllegalArgumentException();
    }

    @Nullable
    @Override
    public ProblemAvailability hasProblem(@Nonnull Collection<Problem> alreadySolvedProblems, @Nonnull Problem.Difficulty difficulty) {
        switch (difficulty) {
            case EASY:
                return findAvailableScenario(difficulty, alreadySolvedProblems, EASY_SCENARIOS, new HashSet<>());
            case MEDIUM:
                return findAvailableScenario(difficulty, alreadySolvedProblems, MEDIUM_SCENARIOS, new HashSet<>(EASY_SCENARIOS));
            case HARD:
                return findAvailableScenario(difficulty, alreadySolvedProblems, DIFFICULT_SCENARIOS, new HashSet<>(MEDIUM_SCENARIOS));
            case EXPERT:
                return null;

        }

        throw new IllegalArgumentException();
    }

    private static long factorial(int a) {
        if (a == 0 || a == 1) return 1;
        else {
            long fact = 1;
            for (int i = 2; i <= a; i++) {
                fact *= i;
            }
            return fact;
        }
    }

    private static long p(int n, int k) {
        return factorial(n) / factorial(n - k);

    }

    public static int c(int n, int k) {
        return (int) (p(n, k) / factorial(k));

    }
}
