package com.kwd;

import java.time.LocalDateTime;

public class Statistic {
    private String name;
    private float errorRate;
    private float wpm;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public Statistic(String name, float errorRate, float wpm, LocalDateTime start, LocalDateTime end) {
        this.name = name;
        this.wpm = wpm;
        this.errorRate = errorRate;
        this.startDate = start;
        this.endDate = end;
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

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }
}
