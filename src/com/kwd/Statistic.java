package com.kwd;

public class Statistic {
    private String name;
    private float errorRate;
    private float WPM;

    public Statistic(String name, float errorRate, float WPM) {
        this.name = name;
        this.WPM = WPM;
        this.errorRate = errorRate;
    }

    public String getName() {
        return name;
    }

    public float getWPM() {
        return WPM;
    }

    public float getErrorRate() {
        return errorRate;
    }
}
