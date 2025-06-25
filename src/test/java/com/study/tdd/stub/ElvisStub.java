package com.study.tdd.stub;

public class ElvisStub implements Singer {

    @Override
    public String sing(String lyrics) {
        System.out.println("Elvis stub is performing: " + lyrics);
        return "I love you Elvis!";
    }
}
