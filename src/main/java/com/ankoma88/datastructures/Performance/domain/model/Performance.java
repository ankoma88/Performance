package com.ankoma88.datastructures.performance.domain.model;

public record Performance(Details details, OverallComparison overallComparison) {

    @Override
    public String toString() {
        return "{" +
                "details: " + details +
                ", overallComparison: " + overallComparison +
                '}';
    }
}
