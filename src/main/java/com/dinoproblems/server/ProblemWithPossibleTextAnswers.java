package com.dinoproblems.server;

import java.util.Set;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProblemWithPossibleTextAnswers that = (ProblemWithPossibleTextAnswers) o;

        if (answer != that.answer) return false;
        if (theme != null ? !theme.equals(that.theme) : that.theme != null) return false;
        return text != null ? text.equals(that.text) : that.text == null;
    }

    @Override
    public int hashCode() {
        int result = theme != null ? theme.hashCode() : 0;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + answer;
        return result;
    }
}
