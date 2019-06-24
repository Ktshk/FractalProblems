package com.dinoproblems.server.utils;

/**
 * Created by Katushka on 16.03.2019.
 */
public class TextWithTTSBuilder {//текст один, речь другая
    private StringBuilder text;
    private StringBuilder tts = null;

    public TextWithTTSBuilder() {
        text = new StringBuilder();
    }

    public TextWithTTSBuilder append(String text) {
        this.text.append(text);
        if (tts != null) {
            tts.append(text);
        }
        return this;
    }

    public TextWithTTSBuilder append(String text, String tts) {
        if (tts == null) {
            return append(text);
        }
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
