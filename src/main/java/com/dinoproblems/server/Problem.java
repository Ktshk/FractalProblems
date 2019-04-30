package com.dinoproblems.server;

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

    String getNextHint();

    String getLastHint();

    boolean hasHint();

    boolean wasHintGiven();

    ProblemScenario getProblemScenario();

    /**
     * Text-to-speech. + means accent
     *
     * @return tts for yandex dialog, null if not specified
     */
    default String getTTS() {
        return null;
    }

    enum State {
        ANSWER_GIVEN, SOLVED, SOLVED_WITH_HINT
    }

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
    }

    State getState();//вынести в TaskResult с удалением из Problem

    Difficulty getDifficulty();

    void setState(State newState);
}
