package com.dinoproblems.server;

import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static com.dinoproblems.server.GeneratorUtils.getNumWithString;
import static com.dinoproblems.server.GeneratorUtils.randomInt;

/**
 * Created by Katushka on 10.02.2019.
 */
public class GolovonogiGenerator implements ProblemGenerator {
    @Override
    public Problem generateProblem(Problem.Difficulty difficulty) {
        int heads = difficulty == Problem.Difficulty.EASY ? randomInt(4, 6)
                : randomInt(6, 10);
        int ducks = randomInt(1, heads);
        int cows = heads - ducks;
        final int quest = randomInt(0, 2);
        int answer = quest == 0 ? ducks : cows;

        final String[][] animals;
        final String[][] animals5more;
        final String[][] animals1;
        final String[][] animals4less;

        final int i2 = randomInt(0, 3);
        final int i4 = randomInt(0, 3);
        final int quest2 = quest == 0 ? i2 : i4;

        final String text;
        final int i = randomInt(0, 3);
        if (i == 0) {
            animals = new String[][]{{"куры", "утки", "петухи"},
                    {"коровы", "овцы", "козы"}};
            animals5more = new String[][]{{"кур", "уток", "петухов"},
                    {"коров", "овец", "коз"}};
            animals1 = new String[][]{{"курица", "утка", "петух"},
                    {"корова", "овца", "коза"}};
            animals4less = new String[][]{{"курицы", "утки", "петуха"},
                    {"коровы", "овцы", "козы"}};

            text = "Во дворе гуляют " + animals[0][i2] + " и " + animals[1][i4]
                    + ". У них вместе " + getNumWithString(heads, "голова", "головы", "голов")
                    + " и " + getLegsString((ducks * 2 + cows * 4)) + ". "
                    + "Сколько " + animals5more[quest][quest2] + " гуляет во дворе?";
        } else if (i == 1) {
            animals = new String[][]{{"цыплята", "утята", "страусы"},
                    {"ящерицы", "утконосы", "крокодилы"}};
            animals5more = new String[][]{{"цыплят", "утят", "страусов"},
                    {"ящериц", "утконосов", "крокодилов"}};
            animals1 = new String[][]{{"цыплёнок", "утёнок", "страус"},
                    {"ящерица", "утконос", "крокодил"}};
            animals4less = new String[][]{{"цыплёнка", "утёнка", "страуса"},
                    {"ящерицы", "утконоса", "крокодила"}};

            text = "Из " + heads + " яиц вылупились " + animals[0][i2] + " и " + animals[1][i4]
                    + ". У них вместе " + getLegsString((ducks * 2 + cows * 4)) + ". "
                    + "Сколько вылупилось " + animals5more[quest][quest2] + "?";

        } else {
            heads = difficulty == Problem.Difficulty.EASY ? randomInt(3, 5)
                    : randomInt(5, 9);
            ducks = randomInt(1, heads);
            cows = heads - ducks;
            answer = quest == 0 ? ducks : cows;

            animals = new String[][]{{"жуки", "бабочки", "муравьи"},
                    {"пауки", "пауки", "пауки"}};
            animals5more = new String[][]{{"жуков", "бабочек", "муравьев"},
                    {"пауков", "пауков", "пауков"}};
            animals1 = new String[][]{{"жук", "бабочка", "муравей"},
                    {"паук", "паук", "паук"}};
            animals4less = new String[][]{{"жука", "бабочки", "муравья"},
                    {"паука", "паука", "паука"}};

            text = "Вася поймал несколько " + animals5more[0][i2] + " и " + animals5more[1][i4]
                    + ". Получилось всего " + getInsectString(heads) + " и у них на всех " + getLegsString(ducks * 6 + cows * 8) +". "
                    + "Сколько " + animals5more[quest][quest2] + " поймал Вася?";
        }

        String possibleAnswer = getNumWithString(answer, animals1[quest][quest2],
                animals4less[quest][quest2], animals5more[quest][quest2]);
        final HashSet<String> possibleTextAnswers = new HashSet<>();
        possibleTextAnswers.add(possibleAnswer);
        return new ProblemWithPossibleTextAnswers(text, answer, ProblemCollection.GOLOVONOGI, possibleTextAnswers);
    }

    @Override
    public Set<Problem.Difficulty> getAvailableDifficulties() {
        return Sets.newHashSet(Problem.Difficulty.EASY, Problem.Difficulty.MEDIUM);
    }

    private static String getLegsString(int legs) {
        return getNumWithString(legs, "нога", "ноги", "ног");
    }

    private static String getInsectString(int legs) {
        return getNumWithString(legs, "букашка", "букашки", "букашек");
    }

}
