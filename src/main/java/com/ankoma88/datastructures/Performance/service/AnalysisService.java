package com.ankoma88.datastructures.performance.service;

import com.ankoma88.datastructures.performance.domain.model.PerformanceDto;
import com.ankoma88.datastructures.performance.domain.model.PerformanceMeasurementDto;

import java.util.List;

public interface AnalysisService {

    PerformanceDto measureOperations(int listSize, int repeats);

    List<PerformanceMeasurementDto> getLatestMeasurements(int size);
}
