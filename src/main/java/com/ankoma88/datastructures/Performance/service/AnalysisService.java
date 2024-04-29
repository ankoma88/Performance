package com.ankoma88.datastructures.Performance.service;

import com.ankoma88.datastructures.Performance.domain.model.Performance;
import com.ankoma88.datastructures.Performance.domain.model.PerformanceMeasurement;

import java.util.List;

public interface AnalysisService {

    Performance measureOperations(int listSize, int repeats);

    List<PerformanceMeasurement> getLatestMeasurements(int size);
}
