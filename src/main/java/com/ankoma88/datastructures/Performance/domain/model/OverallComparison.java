package com.ankoma88.datastructures.performance.domain.model;

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
