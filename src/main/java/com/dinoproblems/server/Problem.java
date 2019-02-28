package com.dinoproblems.server;

/**
 * Created by Katushka on 13.02.2019.
 */
public interface Problem {
    boolean checkAnswer(String proposedAnswer);

    boolean checkNumericAnswer(int proposedAnswer);

    String getTextAnswer();

    String getTheme();

    String getText();

    /**
     * Text-to-speech. + means accent
     * @return tts for yandex dialog, null if not specified
     */
    default String getTTS() {
        return null;
    }

    enum State {
        NEW, HINT_PROPOSED, HINT_GIVEN, ANSWER_PROPOSED, ANSWER_GIVEN, SOLVED
    }

    enum Difficulty {
        EASY, MEDIUM, HARD
    }

    State getState();

    void setState(State newState);
}
