package com.dinoproblems.server.generators;

import com.dinoproblems.server.*;
import com.dinoproblems.server.utils.GeneratorUtils;
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
 * Created by Katushka on 10.02.2019.
 */
public class LegsAndHeadsGenerator implements ProblemGenerator {

    private final static ProblemScenario ANIMALS = new ProblemScenarioImpl(ProblemCollection.LEGS_AND_HEADS + "_" + "ANIMALS");
    private final static ProblemScenario EGGS = new ProblemScenarioImpl(ProblemCollection.LEGS_AND_HEADS + "_" + "EGGS");
    private final static ProblemScenario INSECTS = new ProblemScenarioImpl(ProblemCollection.LEGS_AND_HEADS + "_" + "INSECTS");
    private final static ProblemScenario COINS = new ProblemScenarioImpl(ProblemCollection.LEGS_AND_HEADS + "_" + "COINS");

    private static final ProblemScenario[] MEDIUM_SCENARIOS = {ANIMALS, EGGS, INSECTS};

    @Nonnull
    @Override
    public Problem generateProblem(Problem.Difficulty difficulty, ProblemAvailability problemAvailability) {
        final ProblemScenario scenario = problemAvailability.getScenario();
        int heads = difficulty == Problem.Difficulty.EASY ? randomInt(3, 5) : randomInt(5, 10);
        int ducks = randomInt(1, heads);
        int cows = heads - ducks;
        final int quest = randomInt(0, 2);
        int answer = quest == 0 ? ducks : cows;

        final int i2 = randomInt(0, 3);
        final int i4 = randomInt(0, 3);
        final int quest2 = quest == 0 ? i2 : i4;

        final String text;
        final String hint;
        final HashSet<String> possibleTextAnswers = new HashSet<>();
        if (!scenario.equals(COINS)) {
            final String[][] animals;
            final GeneratorUtils.Gender[][] animalGender;
            final String[][] animals5more;
            final String[][] animals1;
            final String[][] animals4less;

            if (scenario.equals(ANIMALS)) {
                animals = new String[][]{{"куры", "утки", "петухи"},
                        {"коровы", "овцы", "бараны"}};
                animalGender = new GeneratorUtils.Gender[][]{{FEMININE, FEMININE, MASCULINE},
                        {FEMININE, FEMININE, MASCULINE}};
                animals5more = new String[][]{{"кур", "уток", "петухов"},
                        {"коров", "овец", "баранов"}};
                String[][] animalsAcc = new String[][]{{"курицу", "утку", "петуха"},
                        {"корову", "овцу", "барана"}};
                String[][] animalsDat = new String[][]{{"курице", "утке", "петуху"},
                        {"корове", "овце", "барану"}};
                animals1 = new String[][]{{"курица", "утка", "петух"},
                        {"корова", "овца", "баран"}};
                animals4less = new String[][]{{"курицы", "утки", "петуха"},
                        {"коровы", "овцы", "барана"}};

                hint = "Предположим, что все животные во дворе - это " + animals[0][i2]
                        + ", сколько тогда у них ног? А сколько ног надо добавить " + animalsDat[0][i2] + ", чтобы превратить " + (i2 < 2 ? "её" : "его")
                        + " в " + animalsAcc[1][i4] + ". ";

                text = "Во дворе гуляют " + animals[0][i2] + " и " + animals[1][i4]
                        + ". У них вместе " + GeneratorUtils.getNumWithString(heads, "голова", "головы", "голов", FEMININE)
                        + " и " + getLegsString((ducks * 2 + cows * 4)) + ". "
                        + "Сколько " + animals5more[quest][quest2] + " гуляет во дворе?";
            } else if (scenario.equals(EGGS)) {
                animals = new String[][]{{"цыплята", "утята", "страусы"},
                        {"ящерицы", "утконосы", "крокодилы"}};
                animalGender = new GeneratorUtils.Gender[][]{{MASCULINE, MASCULINE, MASCULINE},
                        {FEMININE, MASCULINE, MASCULINE}};
                animals5more = new String[][]{{"цыплят", "утят", "страусов"},
                        {"ящериц", "утконосов", "крокодилов"}};
                animals1 = new String[][]{{"цыплёнок", "утёнок", "страус"},
                        {"ящерица", "утконос", "крокодил"}};
                animals4less = new String[][]{{"цыплёнка", "утёнка", "страуса"},
                        {"ящерицы", "утконоса", "крокодила"}};
                String[][] animalsAcc = new String[][]{{"цыплёнка", "утёнка", "страуса"},
                        {"ящерицу", "утконоса", "крокодила"}};
                String[][] animalsDat = new String[][]{{"цыплёнку", "утёнку", "страусу"},
                        {"ящерице", "утконосу", "крокодилу"}};

                hint = "Представьте, что все, кто вылупился, - это " + animals[0][i2]
                        + ". А потом подумайте, сколько ног надо добавить " + animalsDat[0][i2] + ", чтобы превратить его"
                        + " в " + animalsAcc[1][i4] + ". ";

                text = "Из " + heads + " яиц вылупились " + animals[0][i2] + " и " + animals[1][i4]
                        + ". У них вместе " + getLegsString((ducks * 2 + cows * 4)) + ". "
                        + "Сколько вылупилось " + animals5more[quest][quest2] + "?";

            } else if (scenario.equals(INSECTS)) {
                heads = difficulty == Problem.Difficulty.DIFFICULT ? randomInt(5, 9) : randomInt(3, 5);
                ducks = randomInt(1, heads);
                cows = heads - ducks;
                answer = quest == 0 ? ducks : cows;

                animals = new String[][]{{"жуки", "бабочки", "муравьи"},
                        {"пауки", "пауки", "пауки"}};
                animalGender = new GeneratorUtils.Gender[][]{{MASCULINE, FEMININE, MASCULINE},
                        {MASCULINE, MASCULINE, MASCULINE}};
                animals5more = new String[][]{{"жуков", "бабочек", "муравьев"},
                        {"пауков", "пауков", "пауков"}};
                animals1 = new String[][]{{"жук", "бабочка", "муравей"},
                        {"паук", "паук", "паук"}};
                animals4less = new String[][]{{"жука", "бабочки", "муравья"},
                        {"паука", "паука", "паука"}};
                String[][] animalsAcc = new String[][]{{"жука", "бабочку", "муравью"},
                        {"паука", "паука", "паука"}};
                String[][] animalsDat = new String[][]{{"жуку", "бабочке", "муравью"},
                        {"пауку", "пауку", "пауку"}};

                hint = "Сколько ног было бы у букашек, если бы они все были - " + animals[0][i2]
                        + ". А потом подумайте, сколько ног надо добавить " + animalsDat[0][i2] + ", чтобы превратить " + (i2 < 2 ? "её" : "его")
                        + " в " + animalsAcc[1][i4] + ". ";

                text = "Вася поймал несколько " + animals5more[0][i2] + " и " + animals5more[1][i4]
                        + ". Получилось всего " + getInsectString(heads) + " и у них на всех " + getLegsString(ducks * 6 + cows * 8) + ". "
                        + "Сколько " + animals5more[quest][quest2] + " у Васи?";
            } else {
                throw new IllegalArgumentException();
            }

            String possibleAnswer = GeneratorUtils.getNumWithString(answer, animals1[quest][quest2],
                    animals4less[quest][quest2], animals5more[quest][quest2], animalGender[quest][quest2]);

            possibleTextAnswers.add(possibleAnswer);
        } else {
            heads = randomInt(5, 11);
            int firstCoin = randomInt(1, 3);
            int secondCoin = randomInt(firstCoin + 1, firstCoin + 4);
            ducks = randomInt(2, heads - 2);
            cows = heads - ducks;
            int sum = firstCoin * ducks + secondCoin * cows;
            answer = firstCoin * cows + secondCoin * ducks;

            possibleTextAnswers.add(Integer.toString(answer));

            hint = "Подумайте, какой стороной могли лежать " + heads + " монет, если сумма на них равна " + sum + ". Что если на всех монетах " + firstCoin + "?";
            text = "У Пети есть " + heads + " фишек, на одной стороне которых написана цифра " + firstCoin + ", а на другой " + secondCoin +
                    ". Он выложил их на стол случайным образом и посчитал сумму. Получилось " + sum +
                    ". Затем он перевернул каждую фишку на другую сторону и снова посчитал сумму. Сколько у него получилось?";
        }

        return new ProblemWithPossibleTextAnswers(text, answer, ProblemCollection.LEGS_AND_HEADS, possibleTextAnswers, hint, scenario, difficulty);
    }

    @Override
    public ProblemAvailability hasProblem(@Nonnull Collection<Problem> alreadySolvedProblems, @Nonnull Problem.Difficulty difficulty) {
        switch (difficulty) {
            case EASY:
                return findAvailableScenario(difficulty, alreadySolvedProblems, Lists.newArrayList(ANIMALS, EGGS), new HashSet<>());
            case MEDIUM:
                return findAvailableScenario(difficulty, alreadySolvedProblems, Lists.newArrayList(MEDIUM_SCENARIOS), Lists.newArrayList(ANIMALS, EGGS));
            case DIFFICULT:
                return findAvailableScenario(difficulty, alreadySolvedProblems, Lists.newArrayList(ANIMALS, EGGS, INSECTS, COINS), Sets.newHashSet(MEDIUM_SCENARIOS));
            case EXPERT:
                return null;

        }

        throw new IllegalArgumentException();
    }

    private static String getLegsString(int legs) {
        return GeneratorUtils.getNumWithString(legs, "нога", "ноги", "ног", FEMININE);
    }

    private static String getInsectString(int legs) {
        return GeneratorUtils.getNumWithString(legs, "букашка", "букашки", "букашек", FEMININE);
    }

}
