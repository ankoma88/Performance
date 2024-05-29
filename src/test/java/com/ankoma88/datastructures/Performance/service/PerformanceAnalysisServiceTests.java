package com.ankoma88.datastructures.performance.service;

import com.ankoma88.datastructures.performance.data.entities.MeasurementRecord;
import com.ankoma88.datastructures.performance.data.repository.MeasurementsRepository;
import com.ankoma88.datastructures.performance.domain.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PerformanceAnalysisServiceTests {

    @InjectMocks
    private PerformanceAnalysisService performanceAnalysisService;

    @Mock
    private MeasurementsRepository measurementsRepository;

    @Mock
    private ExecutorService measurementsThreadPool;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetLatestMeasurements() {
        PageRequest pageRequest = PageRequest.of(0, 2);
        MeasurementRecord record = new MeasurementRecord("ARRAYLIST", "CREATE", 1L, 2L, 3L, null);
        Page<MeasurementRecord> page = new PageImpl<>(Collections.singletonList(record));
        when(measurementsRepository.getMeasurementRecords(pageRequest)).thenReturn(page);

        List<PerformanceMeasurementDto> measurements = performanceAnalysisService.getLatestMeasurements(2);

        assertEquals(1, measurements.size());
        assertEquals("ARRAYLIST", measurements.getFirst().getDataStructure());
        assertEquals("CREATE", measurements.getFirst().getOperation());
    }
}
