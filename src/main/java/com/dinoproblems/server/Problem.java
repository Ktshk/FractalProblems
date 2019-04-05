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

    /**
     * Text-to-speech. + means accent
     * @return tts for yandex dialog, null if not specified
     */
    default String getTTS() {
        return null;
    }

    enum State {
        NEW, HINT_GIVEN, ANSWER_GIVEN, SOLVED
    }

    enum Difficulty {
        EASY, MEDIUM, HARD
    }

    State getState();//вынести в TaskResult с удалением из Problem
    Difficulty getDifficulty();
    void setState(State newState);
}
