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
        final int i = ThreadLocalRandom.current().nextInt(0, 3);
        if (true/*i == 0*/) {
            String[] twoLegs = new String[]{"куры", "утки", "петухи"};
            String[] fourLegs = new String[]{"коровы", "овцы", "козы"};
            String[] twoLegsMods = new String[]{"кур", "уток", "петухов"};
            String[] fourLegsMods = new String[]{"коров", "овец", "коз"};

            final int i2 =  ThreadLocalRandom.current().nextInt(0, 3);
            final int i4 =  ThreadLocalRandom.current().nextInt(0, 3);

            final int quest = ThreadLocalRandom.current().nextInt(0, 2);

            final int heads = ThreadLocalRandom.current().nextInt(5, 12);
            final int ducks = ThreadLocalRandom.current().nextInt(1, heads);
            final int cows = heads - ducks;

            String text = "Во дворе гуляют " + twoLegs[i2] + " и " + fourLegs[i4]
                    + ". У них вместе " + heads + " голов и " + getLegsString((ducks * 2 + cows * 4))
                    + "Сколько " + (quest == 0 ? twoLegsMods[i2] : fourLegsMods[i4]) + " гуляет во дворе?";
            int answer = quest == 0 ? ducks : cows;
            String possibleAnswer = answer + " " + (quest == 0 ? twoLegsMods[i2] : fourLegsMods[i4]);
            final HashSet<String> possibleTextAnswers = new HashSet<>();
            possibleTextAnswers.add(possibleAnswer);
            return new Problem(text, answer, possibleTextAnswers);
        }
        return null;
    }

    private static String getLegsString(int legs) {
        if (legs >=5 && legs <= 20) {
            return legs + " ног. ";
        } else {
            final int lastDigit = legs % 10;
            if (lastDigit == 1) {
                return legs + " нога. ";
            } else if (lastDigit >= 2 && lastDigit <= 4) {
                return legs + " ноги. ";
            } else {
                return legs + " ног. ";
            }
        }
    }


}
