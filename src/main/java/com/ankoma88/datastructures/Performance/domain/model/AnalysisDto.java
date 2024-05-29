package com.ankoma88.datastructures.performance.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnalysisDto {
    private MeasurementDto create;
    private MeasurementDto read;
    private MeasurementDto update;
    private MeasurementDto delete;

    @Override
    public String toString() {
        return "{" + create + ", " + read + ", " + update + ", " + delete + "}";
    }
}