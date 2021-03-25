package com.kwd;

public class Statistic {
    private String name;
    private float errorRate;
    private float wpm;

    public Statistic(String name, float errorRate, float wpm) {
        this.name = name;
        this.wpm = wpm;
        this.errorRate = errorRate;
    }

    public String getName() {
        return name;
    }

    public float getWpm() {
        return wpm;
    }

    public float getErrorRate() {
        return errorRate;
    }
}
