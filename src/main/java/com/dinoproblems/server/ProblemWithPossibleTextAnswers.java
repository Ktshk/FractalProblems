package com.dinoproblems.server;

import com.google.common.collect.Lists;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Katushka on 06.02.2019.
 */
public class ProblemWithPossibleTextAnswers implements Problem {

    private final String theme;
    private String text;
    private final String tts;
    private int answer;
    private Set<String> possibleTextAnswers;
    private State state = null;
    private List<String> hints;
    private int currentHint = 0;
    private final Difficulty difficulty;
    private final ProblemScenario scenario;
    private String comment;
    private String commentTTS;

    public ProblemWithPossibleTextAnswers(String text, String tts, int answer, String theme, Set<String> possibleTextAnswers, List<String> hints, ProblemScenario scenario, Difficulty difficulty, String comment, String commentTTS) {
        this.text = text;
        this.tts = tts;
        this.answer = answer;
        this.theme = theme;
        this.possibleTextAnswers = possibleTextAnswers;
        this.hints = hints;
        this.scenario = scenario;
        this.difficulty = difficulty;
        this.comment = comment;
        this.commentTTS = commentTTS;
    }

    public String getText() {
        return text;
    }

    @Nullable
    @Override
    public String getComment() {
        return comment;
    }

    @Nullable
    @Override
    public String getCommentTTS() {
        return commentTTS;
    }

    @Override
    public String getNextHint() {
        final String result = hints.get(currentHint);
        currentHint++;
        return result;
    }

    @Override
    public String getLastHint() {
        return hints.get(currentHint - 1);
    }

    @Override
    public boolean hasHint() {
        return currentHint < hints.size();
    }

    @Override
    public boolean wasHintGiven() {
        return currentHint > 0;
    }

    @Override
    public ProblemScenario getProblemScenario() {
        return scenario;
    }

    @Override
    public String getTTS() {
        return tts;
    }

    public int getAnswer() {
        return answer;
    }

    @Override
    public boolean checkAnswer(String proposedAnswer) {
        return possibleTextAnswers.contains(proposedAnswer) || proposedAnswer.equals("" + answer);
    }

    @Override
    public int getNumericAnswer() {
        return answer;
    }

    @Override
    public boolean isNumericAnswer() {
        return true;
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public Difficulty getDifficulty() {
        return difficulty;
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

    @Override
    public String toString() {
        return "Problem{" +
                "text='" + text + '\'' +
                ", answer=" + answer +
                ", difficulty=" + difficulty +
                ", scenario=" + scenario +
                '}';
    }

    public static class Builder {
        private String text;
        private int answer;
        private String theme;
        private Set<String> possibleTextAnswers;
        private ProblemScenario scenario;
        private Difficulty difficulty;
        private String tts = null;
        private List<String> hints;
        private String comment;
        private String commentTTS;

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder answer(int answer) {
            this.answer = answer;
            return this;
        }

        public Builder theme(String theme) {
            this.theme = theme;
            return this;
        }

        public Builder possibleTextAnswers(Set<String> possibleTextAnswers) {
            this.possibleTextAnswers = possibleTextAnswers;
            return this;
        }

        public Builder hint(String hint) {
            if (hints == null) {
                hints = new ArrayList<>();
            }
            hints.add(hint);
            return this;
        }

        public Builder scenario(ProblemScenario scenario) {
            this.scenario = scenario;
            return this;
        }

        public Builder difficulty(Difficulty difficulty) {
            this.difficulty = difficulty;
            return this;
        }

        public Builder tts(String tts) {
            this.tts = tts;
            return this;
        }

        public Builder hints(List<String> hints) {
            this.hints = hints;
            return this;
        }

        public Builder comment(String comment) {
            this.comment = comment;
            return this;
        }

        public Builder commentTTS(String commentTTS) {
            this.commentTTS = commentTTS;
            return this;
        }

        public ProblemWithPossibleTextAnswers create() {
            return new ProblemWithPossibleTextAnswers(text, tts, answer, theme, possibleTextAnswers, hints, scenario, difficulty, comment, commentTTS);
        }
    }
}
