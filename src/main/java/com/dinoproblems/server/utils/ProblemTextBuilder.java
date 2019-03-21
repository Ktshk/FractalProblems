package com.dinoproblems.server.utils;

/**
 * Created by Katushka on 16.03.2019.
 */
public class ProblemTextBuilder {
    private StringBuilder text;
    private StringBuilder tts = null;

    public ProblemTextBuilder() {
        text = new StringBuilder();
    }

    public ProblemTextBuilder append(String text) {
        this.text.append(text);
        if (tts != null) {
            tts.append(text);
        }
        return this;
    }

    public ProblemTextBuilder append(String text, String tts) {
        if (this.tts == null) {
            this.tts = new StringBuilder(this.text);
        }
        this.text.append(text);
        this.tts.append(tts);
        return this;
    }

    public String getText() {
        return text.toString();
    }

    public String getTTS() {
        return tts == null ? null : tts.toString();
    }
}
