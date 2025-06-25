package com.study.tdd.spy;

public class Concert {

    private final Elvis elvis;

    public Concert(Elvis elvis) {
        this.elvis = elvis;
    }

    public String perform(String lyrics, String danceStyle) {
        String singResult = elvis.sing(lyrics);
        String danceResult = elvis.dance(danceStyle);
        return singResult + " + " + danceResult;
    }
}
