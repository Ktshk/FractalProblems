package com.dinoproblems.server;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Katushka on 06.02.2019.
 */
public class ProblemWithPossibleTextAnswers implements Problem {

    private final String theme;
    private String text;
    private int answer;
    private Set<String> possibleTextAnswers;
    private State state = State.NEW;


    public ProblemWithPossibleTextAnswers(String text, int answer, String theme, Set<String> possibleTextAnswers) {
        this.text = text;
        this.answer = answer;
        this.theme = theme;
        this.possibleTextAnswers = possibleTextAnswers;
    }

    public String getText() {
        return text;
    }

    public int getAnswer() {
        return answer;
    }

    @Override
    public boolean checkAnswer(String proposedAnswer) {
        return possibleTextAnswers.contains(proposedAnswer) || proposedAnswer.equals("" + answer);
    }

    @Override
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

    public String getTheme() {
        return theme;
    }


}
