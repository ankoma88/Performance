package com.ankoma88.datastructures.performance.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OverallComparisonDto {
    private String recommendation;
    private AverageTimeDto averageTime;

    @Override
    public String toString() {
        return "OverallComparison: {" +
                "AverageTime: " + averageTime +
                ", Recommendation: " + recommendation +
                '}';
    }
}
