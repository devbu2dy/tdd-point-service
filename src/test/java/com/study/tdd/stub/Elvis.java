package com.study.tdd.stub;

public class Elvis implements Singer {

    @Override
    public String sing(String lyrics) {
        System.out.println("Elvis is singing: " + lyrics);
        return "Wow! Elvis is amazing!";
    }
}
