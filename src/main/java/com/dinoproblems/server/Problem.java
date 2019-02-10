package com.dinoproblems.server;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Katushka on 06.02.2019.
 */
public class Problem {

    private final String theme;
    private String text;
    private int answer;
    private Set<String> possibleTextAnswers;
    private State state = State.NEW;

    enum State {
        NEW, HINT_PROPOSED, HINT_GIVEN, ANSWER_PROPOSED, ANSWER_GIVEN, SOLVED
    }

    enum Difficulty {
        EASY, MEDIUM, HARD
    }

    public Problem(String text, int answer, String theme, Set<String> possibleTextAnswers) {
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
