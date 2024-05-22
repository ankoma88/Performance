package com.ankoma88.datastructures.performance.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PerformanceDto {
    private DetailsDto details;
    private OverallComparisonDto overallComparison;

    @Override
    public String toString() {
        return "{" +
                "details: " + details +
                ", overallComparison: " + overallComparison +
                '}';
    }
}
