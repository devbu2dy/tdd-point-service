package com.study.tdd.stub;

public class Concert {

    private final Singer singer;

    public Concert(Singer singer) {
        this.singer = singer;
    }

    public String perform(String lyrics) {
        return singer.sing(lyrics);
    }
}
