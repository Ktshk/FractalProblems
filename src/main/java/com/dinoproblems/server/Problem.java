package com.dinoproblems.server;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Katushka on 06.02.2019.
 */
public class Problem {

    enum State {
        NEW, HINT_PROPOSED, HINT_GIVEN, ANSWER_PROPOSED, ANSWER_GIVEN, SOLVED
    }

    private String text;
    private int answer;
    private Set<String> possibleTextAnswers;
    private State state = State.NEW;


    public Problem(String text, int answer, Set<String> possibleTextAnswers) {
        this.text = text;
        this.answer = answer;
        this.possibleTextAnswers = possibleTextAnswers;
    }

    public String getText() {
        return text;
    }

    public int getAnswer() {
        return answer;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getTextAnswer() {
        return possibleTextAnswers.iterator().next();
    }

    public Set<String> getPossibleTextAnswers() {
        return possibleTextAnswers;
    }

    static Problem generateProblem() {
        final int heads = ThreadLocalRandom.current().nextInt(5, 10);
        final int ducks = ThreadLocalRandom.current().nextInt(1, heads);
        final int cows = heads - ducks;
        final int quest = ThreadLocalRandom.current().nextInt(0, 2);
        int answer = quest == 0 ? ducks : cows;

        final String[][] animals;
        final String[][] animals5more;
        final String[][] animals1;
        final String[][] animals4less;

        final int i2 = ThreadLocalRandom.current().nextInt(0, 3);
        final int i4 = ThreadLocalRandom.current().nextInt(0, 3);
        final int quest2 = quest == 0 ? i2 : i4;

        final String text;
        final int i = ThreadLocalRandom.current().nextInt(0, 3);
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
                    + ". У них вместе " + heads + " голов и " + getLegsString((ducks * 2 + cows * 4)) + ". "
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
            animals = new String[][]{{"жуки", "бабочки", "муравьи"},
                    {"пауки", "пауки", "пауки"}};
            animals5more = new String[][]{{"жуков", "бабочек", "муравьев"},
                    {"пауков", "пауков", "пауков"}};
            animals1 = new String[][]{{"жук", "бабочка", "муравей"},
                    {"паук", "паук", "паук"}};
            animals4less = new String[][]{{"жука", "бабочки", "муравья"},
                    {"паука", "паука", "паука"}};

            text = "Вася поймал несколько " + animals5more[0][i2] + " и " + animals[1][i4]
                    + ". Получилось всего " + getInsectString(heads) + " и у них на всех " + getLegsString(ducks * 6 + cows * 8) +". "
                    + "Сколько " + animals5more[quest][quest2] + " поймал Вася?";
        }

        String possibleAnswer = getNumWithString(answer, animals1[quest][quest2],
                animals4less[quest][quest2], animals5more[quest][quest2]);
        final HashSet<String> possibleTextAnswers = new HashSet<>();
        possibleTextAnswers.add(possibleAnswer);
        return new Problem(text, answer, possibleTextAnswers);

    }

    private static String getLegsString(int legs) {
        return getNumWithString(legs, "нога", "ноги", "ног");
    }

    private static String getInsectString(int legs) {
        return getNumWithString(legs, "букашка", "букашки", "букашек");
    }

    private static String getNumWithString(int legs, final String one, final String lessThanFive, final String moreThanFive) {
        if (legs >= 5 && legs <= 20) {
            return legs + " " + moreThanFive;
        } else {
            final int lastDigit = legs % 10;
            if (lastDigit == 1) {
                return legs + " " + one;
            } else if (lastDigit >= 2 && lastDigit <= 4) {
                return legs + " " + lessThanFive;
            } else {
                return legs + " " + moreThanFive;
            }
        }
    }


}
