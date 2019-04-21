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

    String getHint();

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
        NEW, HINT_GIVEN, ANSWER_GIVEN, SOLVED, SOLVED_WITH_HINT
    }

    enum Difficulty {
        EASY, MEDIUM, DIFFICULT, EXPERT;

        public Difficulty getPrevious() {
            switch (this) {
                case EASY:
                    throw new IllegalStateException();
                case MEDIUM:
                    return EASY;
                case DIFFICULT:
                    return MEDIUM;
                case EXPERT:
                    return DIFFICULT;
            }
            throw new IllegalStateException();
        }
    }

    State getState();//вынести в TaskResult с удалением из Problem

    Difficulty getDifficulty();

    void setState(State newState);
}
