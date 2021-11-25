package com.dinoproblems.server;

import com.dinoproblems.server.utils.TextWithTTSBuilder;

import javax.annotation.Nullable;

/**
 * Created by Katushka on 13.02.2019.
 */
public interface Problem {
    boolean checkAnswer(String proposedAnswer);

    int getNumericAnswer();

    boolean isNumericAnswer();

    String getTextAnswer();

    String getTheme();

    String getText();

    @Nullable
    String getComment();

    @Nullable
    String getCommentTTS();

    String getNextHint(int currentHint);

    String getLastHint(int currentHint);

    boolean hasHint(int currentHint);

    boolean wasHintGiven(int currentHint);

    ProblemScenario getProblemScenario();

    /**
     * Text-to-speech. + means accent
     *
     * @return tts for yandex dialog, null if not specified
     */
    @Nullable
    default String getTTS() {
        return null;
    }

    boolean hasSolution();

    TextWithTTSBuilder getSolution();

    String[] getHints();

    String[] getTextAnswers();

    enum Difficulty {
        EASY, MEDIUM, HARD, EXPERT;

        public Difficulty getPrevious() {
            switch (this) {
                case EASY:
                    throw new IllegalStateException();
                case MEDIUM:
                    return EASY;
                case HARD:
                    return MEDIUM;
                case EXPERT:
                    return HARD;
            }
            throw new IllegalStateException();
        }

        public Difficulty getNext() {
            switch (this) {
                case EASY:
                    return MEDIUM;
                case MEDIUM:
                    return HARD;
                case HARD:
                    return EXPERT;
                case EXPERT:
                    throw new IllegalStateException();
            }
            throw new IllegalStateException();
        }
    }

    Difficulty getDifficulty();
}
