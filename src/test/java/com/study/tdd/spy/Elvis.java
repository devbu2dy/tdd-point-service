package com.study.tdd.spy;

public class Elvis {

    public String sing(String lyrics) {
        System.out.println("Elvis is singing: " + lyrics);
        return "Wow! Elvis is amazing!";
    }

    public String dance(String style) {
        System.out.println("Elvis is dancing: " + style);
        return "Elvis dances " + style + " style!";
    }
}
