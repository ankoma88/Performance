package com.ankoma88.datastructures.performance.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MeasurementDto {
    private Long start;
    private Long middle;
    private Long end;

    @Override
    public String toString() {
        return "{" +
                "Start: " + start + " ms, " +
                "Middle: " + middle + " ms, " +
                "End: " + end + " ms" +
                '}';
    }
}
