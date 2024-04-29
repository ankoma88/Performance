package com.ankoma88.datastructures.Performance.domain.model;

public record OverallComparison (
    String recommendation,
    AverageTime averageTime
) {

    @Override
    public String toString() {
        return "OverallComparison: {" +
                "AverageTime: " + averageTime +
                "Recommendation: " + recommendation +
                '}';
    }
}
