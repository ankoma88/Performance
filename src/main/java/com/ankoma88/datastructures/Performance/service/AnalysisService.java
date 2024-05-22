package com.ankoma88.datastructures.performance.service;

import com.ankoma88.datastructures.performance.domain.model.Performance;
import com.ankoma88.datastructures.performance.domain.model.PerformanceMeasurement;

import java.util.List;

public interface AnalysisService {

    Performance measureOperations(int listSize, int repeats);

    List<PerformanceMeasurement> getLatestMeasurements(int size);
}
